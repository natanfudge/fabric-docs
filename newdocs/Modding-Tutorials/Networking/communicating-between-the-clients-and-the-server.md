# Communicating between the clients and the server

## Experiment

Say you wanted to emit an explosion particle whenever your block is destroyed. Emitting particles requires access to the `ParticleManager`, which only exists on the `MinecraftClient` instance. Let's try doing that:

```java
public class MyBlock extends Block {
    @Override
    public void onBlockRemoved(BlockState before, World world, BlockPos pos, BlockState after, boolean bool) {
        if (player != null) {
            // WARNING: BAD CODE. DON'T EVER DO THIS!
            MinecraftClient.getInstance().particleManager.addParticle(
                    ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(),
                    0.0D, 0.0D, 0.0D
            );
        }
    }
}
```

If you go into try to break your block, you will see your particle works perfectly fine. Job's done, right? Well, try to [run it on a dedicated server](testing-on-a-dedicated-server.md). You break your block, and...

```text
[Server thread/FATAL]: Error executing task on Server
java.lang.RuntimeException: Cannot load class net.minecraft.client.MinecraftClient in environment type SERVER
```

### What went wrong?

There are 2 types of Minecraft jars. The _server_ jar, which is run by the server that hosts a multiplayer game. And there is the _client_ jar, which is run by every player who joins the server. Some classes, such as the `MinecraftClient`, only exist in the client jar. And for good reason - the server can't play particles! It doesn't even have a screen to display anything on.

`onBlockedRemoved` gets called by the server, which cannot do the client-only operations that `MinecraftClient` provides

* in this case, playing particles. Since `onBlockRemoved` gets called by the server **only**,

  we will need to somehow tell the clients they need to play the particles.

  In a single player settings this is easy, the client runs in the same process as the server,

  so we can just access the `MinecraftClient` and be done with it.

  In multiplayer we will need to communicate between entirely different computers. This is where packets come in.

## Server To Client \(S2C\) Packets

Server To Client, or S2C packets for short, allow the server to tell the clients to execute predefined code with some data. The clients will first need to know what to execute, so we will tell them just that during [**client** mod initialization](the-client-mod-initializer.md).

\(Note that the identifier we will put in the class of the common initializer so it can be accessed from both sides:\)

```java
public class ExampleMod implements ModInitializer {
    // Save the id of the packet so we can reference it later
    public static final Identifier PLAY_PARTICLE_PACKET_ID = new Identifier("example", "particle");
}
```

```java
public class ExampleClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // We associate PLAY_PARTICLE_PACKET_ID with this callback, so the server can then use that id to execute the callback.
        ClientSidePacketRegistry.INSTANCE.register(ExampleMod.PLAY_PARTICLE_PACKET_ID,
                (packetContext, attachedData) -> {
                    // Client sided code
                    // Caution: don't execute code here just yet
                });
    }
}
```

One thing we must make sure is that **we execute the code in the correct thread**. The packet callback is executed on the _networking thread_, while interaction with the most things, such as the particle manager, must be done in the main thread. The `packetContext` that is passed to the callback contains an easy way to do that by calling `getTaskQueue().execute`:

```java
// [...]
ClientSidePacketRegistry.INSTANCE.register(ExampleMod.PLAY_PARTICLE_PACKET_ID,
        (packetContext, attachedData) -> packetContext.getTaskQueue().execute(() -> {
            // For now we will use the player's position since we don't know (yet) how to use the BlockPos
            // in the onBlockRemoved callback. This is explained in "passing information to packets".
            Vec3d pos = packetContext.getPlayer().getPos();
            MinecraftClient.getInstance().particleManager.addParticle(
                    ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(),
                    0.0D, 0.0D, 0.0D
            );
        }));
```

The only thing that is left is for the server to actually send the packet to clients.

```java
public class MyBlock extends Block {
    @Override
    public void onBlockRemoved(BlockState before, World world, BlockPos pos, BlockState after, boolean bool) {
        // First we need to actually get hold of the players that we want to send the packets to.
        // A simple way is to obtain all players watching this position:
        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world,pos);
        // Look at the other methods of `PlayerStream` to capture different groups of players.

        // We'll get to this later
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());

        // Then we'll send the packet to all the players
        watchingPlayers.forEach(player ->
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player,ExampleMod.PLAY_PARTICLE_PACKET_ID,passedData));
        // This will work in both multiplayer and singleplayer!
    }

    // [...]
}
```

If you join a dedicated server, go into first person and break the block, you will see an explosion particle was indeed emitted in your location.

## Attaching data to packets

But wait, we wanted the explosion to occur at the block's position, not the player's! For that we will need to send the block position to the packet. It's fairly simple. Write the data to the `PacketByteBuf`:

```java
Stream<PlayerEntity> watchingPlayers = /*...*/

// Pass the `BlockPos` information
PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
passedData.writeBlockPos(pos);  // <---

watchingPlayers.forEach(player -> /*...*/);
```

And then retrieve it in the packet callback, **but make sure you do it in the network thread**.

```java
ClientSidePacketRegistry.INSTANCE.register(ExampleMod.PLAY_PARTICLE_PACKET_ID,
        (packetContext, attachedData) -> {
            // Get the BlockPos we put earlier, in the networking thread
            BlockPos pos = attachedData.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                // Use the pos in the main thread
                MinecraftClient.getInstance().particleManager.addParticle(
                        ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(),
                        0.0D, 0.0D, 0.0D
                );
            });
        });
```

Try it out in a dedicated server environment.

If you want to send additional data, just call additional `read` and `write` methods, but make sure you do them in the correct order:

```java
// Write
passedData.writeBlockPos(pos);  
passedData.writeInt(123);
passedData.writeString("hello");
```

```java
// Read
(packetContext, attachedData) -> {
    // Read in the correct, networking thread
    BlockPos pos = attachedData.readBlockPos();
    int num = attachedData.readInt();
    String str = attachedData.readString();
    packetContext.getTaskQueue().execute(() -> {
        /*
         * Use num and str in the correct, main thread
         */
        System.out.println(num);
        System.out.println(str);
    });
});
```

## Client To Server \(C2S\) Packets

Client to server packets follow the same principles. Some things can only be done from the server, such as changing the world in a way that affects other players, but you want them to be triggered by a client-only action, such as holding a keybinding. One key difference is that **you must validate what you receive in the `PacketByteBuf`**.

In this example we will replace a block with diamond when it is right clicked when a keybinding is held, using a C2S packet. If you want to know how to use hotkeys specifically, refer to the [hotkeys tutorial.](../miscellaneous/keybinds.md)

As before we'll define an identifier for our packet:

```java
public class ExampleMod implements ModInitializer {
    public static final Identifier TURN_TO_DIAMOND_PACKET_ID = new Identifier("example", "diamond");
}
```

Now we will send the packet when the block is right-clicked and the keybinding is held. We can only check the keybinding on the client, so we must only execute the code here on the client:

```java
public class MyBlock extends Block {
    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        if (world.isClient()) {
            // We are in the client now, can't do anything server-sided

            // See the keybindings tutorial for information about this if statement (the keybindings tutorial calls this variable "keyBinding")
            if(ExampleClientInit.EXAMPLE_KEYBINDING.isPressed()){
                // Pass the `BlockPos` information
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeBlockPos(pos);
                // Send packet to server to change the block for us
                ClientSidePacketRegistry.INSTANCE.sendToServer(ExampleMod.TURN_TO_DIAMOND_PACKET_ID, passedData);
            }

        }
        return true;
    }
}
```

And then we receive the packet on the server side by registering it in the common mod initializer. Make sure to **take data in the IO thread and use it in the main thread**, and to **validate the received data**:

```java
public class ExampleMod implements ModInitializer {
    public static final Identifier TURN_TO_DIAMOND_PACKET_ID = new Identifier("example", "diamond");

    @Override
    public void onInitialize() {
        ServerSidePacketRegistry.INSTANCE.register(TURN_TO_DIAMOND_PACKET_ID, (packetContext, attachedData) -> {
            // Get the BlockPos we put earlier in the IO thread
            BlockPos pos = attachedData.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                // Execute on the main thread

                // ALWAYS validate that the information received is valid in a C2S packet!
                if (packetContext.getPlayer().world.isHeightValidAndBlockLoaded(pos)) {
                    // Turn to diamond
                    packetContext.getPlayer().world.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState());
                }

            });
        });
    }
}
```


# Page Information

This page has replaced the old networking page. It is recommended to use
the new networking API described on this page. The old page can be
[found here](../tutorial/legacy/networking-v0.md).

# Networking

Networking in Minecraft is used so the client and server can communicate
with each other. Networking is a broad topic so this page is split up
into a few categories.

## Example: Why is networking important?

The importance of networking can be shown by a simple code example. This
code should **NOT** be used and is here to explain why networking is
important.

Say you had a wand which highlights the block you are looking at to all
nearby players.

```java
class HighlightingWandItem extends Item {
    public HighlightingWand(Item.Settings settings) {
        super(settings)
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Raycast and find the block the user is facing at
        BlockPos target = ...

        // BAD CODE: DON'T EVER DO THIS!
        ClientBlockHighlighting.highlightBlock(MinecraftClient.getInstance(), target);
        return super.use(world, user, hand);
    }
}
```

Upon testing, you will see the block you are facing at is highlighted
and nothing crashes. Now you want to show the mod to your friend, you
boot up a dedicated server and invite your friend on with the mod
installed. You use the item and the server crashes... You will probably
notice in the crash log an error similar to this:

    [Server thread/FATAL]: Error executing task on Server
    java.lang.RuntimeException: Cannot load class net.minecraft.client.MinecraftClient in environment type SERVER

### Why does the server crash?

The code calls logic only present on the client distribution of the
Minecraft. The reason for Mojang distributing the game in this way is to
cut down on the size of the Minecraft server jar file. There isn't
really a reason to include an entire rendering engine when your own
machine will render the world. In a development environment, client only
classes are indicated by the `@Environment(EnvType.CLIENT)` annotation.

### How do I fix the crash?

In order to fix this issue, you need to understand how Minecraft
communicates between the game client and dedicated server.

[<img src="/tutorial/sides.png" width="700" alt="sides.png" />](Minecraft's Logical Sides)

The diagram above shows that the game client and dedicated server art
separate systems bridged together using packets. This packet bridge does
not only exist between a game client and dedicated server, but also
between your client and another client connected over LAN. The packet
bridge is also present even in singleplayer! This is because the game
client will spin up a special integrated server instance to run the game
on. The key difference between the three types of connections that are
shown in the table below:

| Connection Type               | Access to game client          |
|-------------------------------|--------------------------------|
| Connected to Dedicated Server | None -&gt; Server Crash        |
| Connected over LAN            | Yes -&gt; Not host game client |
| Singleplayer (or LAN host)    | Yes -&gt; Full access          |

It may seem complicated to have communication with the server in three
different ways. However, you don't need to communicate in three
different ways with the game client. Since all three connection types
communicate with the game client using packets, you only need to
communicate with the game client like you are always running on a
dedicated server. Connection to a server over LAN or Singleplayer can be
also be treated like the server is a remote dedicated server; so your
game client cannot directly access the server instance.

## An introduction to networking

To begin, we need to fix an issue with the example code shown above.
Since we are using packets to communicate with the client, we want to
make sure the packets are only sent when an action is initiated on the
server.

### Sending a packet to the game client

```java
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Verify we are processing the use action on the logical server
        if (world.isClient()) return super.use(world, user, hand);

        // Raycast and find the block the user is facing at
        BlockPos target = ...

        // BAD CODE: DON'T EVER DO THIS!
        ClientBlockHighlighting.highlightBlock(MinecraftClient.getInstance(), target);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
```

Next, we need to send the packet to the game client. First, you need to
define an `Identifier` used to identify your packet. For this example
our Identifier will be `wiki_example:highlight_block`. In order to send
the packet to the game client, you need to specify which player's game
client you want to packet to be received by. Since the action is
occurring on the logical server, we may upcast the `player` to a
`ServerPlayerEntity`.

To send the packet to the player, we will use some of the methods inside
of `ServerPlayNetworking`. We will use the following method inside of
that class:

```java
public static void send(ServerPlayerEntity player, Identifier channelName, PacketByteBuf buf) {
    ...
```

The player in this method is the player the packet will be sent to. The
channel name is the `Identifier` you decided on earlier to identify your
packet. The `PacketByteBuf` is what stores the data for the packet. We
will return later to writing data to the packet's payload via the buf.

Since we are not writing any data to the packet, for now, we will send
the packet with an empty payload. A buf with an empty payload may be
created using `PacketByteBufs.empty()`.

```java
    ....
    ServerPlayNetworking.send((ServerPlayerEntity) user, ModNetworkingConstants.HIGHLIGHT_PACKET_ID, PacketByteBufs.empty());
    return TypedActionResult.success(user.getHandStack(hand));
}
```

Though you have sent a packet to the game client, the game client cannot
do anything with the packet since the client does not know how to
receive the packet. Information on receiving a packet on the game client
is shown below:

### Receiving a packet on the game client

To receive a packet from a server on the game client, your mod needs to
specify how it will handle the incoming packet. In your client
entrypoint, you will register the receiver for your packet using
`PlayClientNetworking.registerGlobalReceiver(Identifier channelName, ChannelHandler channelHandler)`

The `Identifier` should match the same Identifier you use to send the
packet to the client. The `ChannelHandler` is the functional interface
you will use to implement how the packet is handled. **Note the
`ChannelHandler` should be the one that is a nested interface of
`ClientPlayNetworking`**

The example below implements the play channel handler as a lambda:

```java
ClientPlayNetworking.registerGlobalReceiver(ModNetworkingConstants.HIGHLIGHT_PACKET_ID, (client, handler, buf, responseSender) -> {
    ...
});
```

However, you cannot draw the highlight box immediately. This is because
the receiver is called on the netty event loop. The event loop runs on
another thread, and you must draw the highlight box on the render
thread.

In order to draw the highlight box, you need to schedule the task on the
game client. This may be done with the `client` field that is provided
in the channel handler. Typically you will run the task on the client by
using the `execute` method:

```java
ClientPlayNetworking.registerGlobalReceiver(ModNetworkingConstants.HIGHLIGHT_PACKET_ID, (client, handler, buf, responseSender) -> {
    client.execute(() -> {
        // Everything in this lambda is run on the render thread
        ClientBlockHighlighting.highlightBlock(client, target);
    });
});
```

You may have noticed you are not told where the block to highlight is.
You can write this data to the packet byte buf. Instead of sending
`PacketByteBufs.empty()` to the game client in your item's `use` method,
instead, you will create a new packet byte buf and send that instead.

```java
PacketByteBuf buf = PacketByteBufs.create();
```

Next, you need to write the data to the packet byte buf. It should be
noted that you must read data in the same order you write it.

```java
PacketByteBuf buf = PacketByteBufs.create();

buf.writeBlockPos(target);
```

Afterwards, you will send the `buf` field through the `send` method.

To read this block position on the game client, you can use
`PacketByteBuf.readBlockPos()`.

You should read all data from the packet on the network thread before
scheduling a task to occur on the client thread. You will get errors
related to the ref count if you try to read data on the client thread.
If you must read data on the client thread, you need to `retain()` the
data and then read it on the client thread. If you do `retain()` the
data, make sure you `release()` the data when you no longer need it.

In the end, the client's handler would look like this:

```java
ClientPlayNetworking.registerGlobalReceiver(ModNetworkingConstants.HIGHLIGHT_PACKET_ID, (client, handler, buf, responseSender) -> {
    // Read packet data on the event loop
    BlockPos target = buf.readBlockPos();

    client.execute(() -> {
        // Everything in this lambda is run on the render thread
        ClientBlockHighlighting.highlightBlock(client, target);
    });
});
```

### Sending packets to the server and receiving packets on the server

Sending packets to a server and receiving a packet on the server is very
similar to how you would on the client. However, there are a few key
differences.

Firstly sending a packet to the server is done through
`ClientPlayNetworking.send`. Receiving a packet on the server is similar
to receiving a packet on the client, using the
`ServerPlayNetworking.registerGlobalReceiver(Identifier channelName, ChannelHandler channelHandler)`
method. The `ChannelHandler` for the server networking also passes the
`ServerPlayerEntity` (player) who sent the packet through the `player`
parameter.

## The concept of tracking and why you only see the highlighted block

Now that the highlighting wand properly uses networking so the dedicated
server does not crash, you invite your friend back on the server to show
off the highlighting wand. You use the wand and the block is highlighted
on your client and the server does not crash. However, your friend does
not see the highlighted block. This is intentional with the code that
you already have here. To solve this issue let us take a look at the
item's use code:

```java
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Verify we are processing the use action on the logical server
        if (world.isClient()) return super.use(world, user, hand);

        // Raycast and find the block the user is facing at
        BlockPos target = ...
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(target);

        ServerPlayNetworking.send((ServerPlayerEntity) user, ModNetworkingConstants.HIGHLIGHT_PACKET_ID, buf);
        return TypedActionResult.success(user.getHandStack(hand));
    }
```

You may notice the item will only send the packet to the player who used
the item. To fix this, we can use the utility methods in `PlayerLookup`
to get all the players who can see the highlighted block.

Since we know where the highlight will occur, we can use
`PlayerLookup.tracking(ServerWorld world, BlockPos pos)` to get a
collection of all players who can see that position in the world. Then
you would simply iterate through all players in the returned collection
and send the packet to each player:

```java
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Verify we are processing the use action on the logical server
        if (world.isClient()) return super.use(world, user, hand);

        // Raycast and find the block the user is facing at
        BlockPos target = ...
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(target);

        // Iterate over all players tracking a position in the world and send the packet to each player
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, target)) {
            ServerPlayNetworking.send(player, ModNetworkingConstants.HIGHLIGHT_PACKET_ID, buf);
        }

        return TypedActionResult.success(user.getHandStack(hand));
    }
```

After this change, when you use the wand, your friend should also see
the highlighted block on their own client.

# Advanced Networking topics

The Networking system Fabric API supplies is very flexible and supports
additional features other than just sending and receiving simple
packets. As some of these more advanced topics are long, here are links
to their specific pages:

| Networking Topic                                                                  | Description                                                                                                    |
|-----------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| [Connection Network connection events](../tutorial/networking/connection_events.md)    | Events related to the the lifecycle of a connection to a client or server                                      |
| [Channel registration events](../tutorial/networking/channel_events.md)                | Events related to a server of client declaring the ability to receive a packet on a channel of a specific name |
| [Login phase networking](../tutorial/networking/login.md)                              | Sending requests to a client during login; and allowing delay of login for a short amount of time              |
| [Dynamic registration of channel handlers](../tutorial/networking/dynamic_handlers.md) | Allowing for a connection to receive a packet with a special handler                                           |

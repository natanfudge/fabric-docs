## Creating a Custom Projectile

// It is important to read through and understand [this
tutorial](https://fabricmc.net/wiki/tutorial:entity) as well as [this
tutorial](https://fabricmc.net/wiki/tutorial:items), as this will help
you understand most of the elements included in this tutorial.//  
This tutorial will teach you how to create your own custom projectile,
including the `ProjectileEntity`, as well as the projectile item itself.
This guide will go over how to define the projectile, register the
projectile, rendering the projectile, as well as creating the projectile
item itself.

ProjectileEntities are used to, well, create and operate projectiles.
Some basic projectiles include:

- Snowballs
- Ender Pearls

We will be creating a custom snowball-like projectile that applies some
very nasty effects to the entity that has been hit.

// If you would like to look over the source code yourself, all of the
following code was done
[here](https://github.com/spxctreofficial/fabric-projectile-tutorial).
Before the tutorial begins, I would like to let you know that I would be
using PascalCase to name the methods. Feel free to change the naming
scheme to however you like, but I personally swear by PascalCase. //

## Creating & Registering a Projectile Entity

To start, we will need to create a new class for the `ProjectileEntity`,
extending `ThrownItemEntity`.

```java
/*
We will be creating a custom snowball-like projectile that deals some nasty debuffs.
Since this is a thrown projectile, we will extending ThrownItemEntity.
Some ThrownItemEntities include:
- Snowballs
- Ender Pearls
 */
public class PackedSnowballEntity extends ThrownItemEntity {
[. . .]
}
```

Your IDE should complain about unimplemented methods, so implement that.

```java
public class PackedSnowballEntity extends ThrownItemEntity {
    @Override
    protected Item getDefaultItem() {
        return null; // We will configure this later, once we have created the ProjectileItem.
    }
}
```

Your IDE should complain about not having the required constructors, but
I would not recommend using the default constructors, but instead using
the following constructors shown in the code, as they were heavily
modified from the default constructors and should work fine, if not
better.

```java
public class PackedSnowballEntity extends ThrownItemEntity {
    public PackedSnowballEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public PackedSnowballEntity(World world, LivingEntity owner) {
        super(null, owner, world); // null will be changed later
    }

    public PackedSnowballEntity(World world, double x, double y, double z) {
        super(null, x, y, z, world); // null will be changed later
    }

    @Override
    protected Item getDefaultItem() {
        return null; // We will configure this later, once we have created the ProjectileItem.
    }
}
```

Your IDE should not complain about any more major issues if you followed
those instructions correctly.  
We will continue adding features related to our projectile. Keep in mind
that the following code is entirely customizable, and I am encouraging
those who follow this tutorial to be creative here.

```java
public class PackedSnowballEntity extends ThrownItemEntity {
    public PackedSnowballEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public PackedSnowballEntity(World world, LivingEntity owner) {
        super(null, owner, world); // null will be changed later
    }

    public PackedSnowballEntity(World world, double x, double y, double z) {
        super(null, x, y, z, world); // null will be changed later
    }

    @Override
    protected Item getDefaultItem() {
        return null; // We will configure this later, once we have created the ProjectileItem.
    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() { // Not entirely sure, but probably has do to with the snowball's particles. (OPTIONAL)
        ItemStack itemStack = this.getItem();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) { // Also not entirely sure, but probably also has to do with the particles. This method (as well as the previous one) are optional, so if you don't understand, don't include this one.
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity(); // sets a new Entity instance as the EntityHitResult (victim)
        int i = entity instanceof BlazeEntity ? 3 : 0; // sets i to 3 if the Entity instance is an instance of BlazeEntity
        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), (float)i); // deals damage

        if (entity instanceof LivingEntity) { // checks if entity is an instance of LivingEntity (meaning it is not a boat or minecart)
            ((LivingEntity) entity).addStatusEffect((new StatusEffectInstance(StatusEffects.BLINDNESS, 20 * 3, 0))); // applies a status effect
            ((LivingEntity) entity).addStatusEffect((new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 3, 2))); // applies a status effect
            ((LivingEntity) entity).addStatusEffect((new StatusEffectInstance(StatusEffects.POISON, 20 * 3, 1))); // applies a status effect
            entity.playSound(SoundEvents.AMBIENT_CAVE, 2F, 1F); // plays a sound for the entity hit only
        }
    }

    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        if (!this.world.isClient) { // checks if the world is client
            this.world.sendEntityStatus(this, (byte)3); // particle?
            this.remove(); // kills the projectile
        }

    }
}
```

We are now finished with the core code of the projectile. We will be
adding on to the projectile class, however, once we have defined and
registered other items.  
We have created the projectile class, but we haven't defined and
registered it yet. To register a projectile, you can follow [this
tutorial](https://fabricmc.net/wiki/tutorial:entity) or you can follow
the code below.

```java
public class ProjectileTutorialMod implements ModInitializer {
    public static final String ModID = "projectiletutorial"; // This is just so we can refer to our ModID easier.

    public static final EntityType<PackedSnowballEntity> PackedSnowballEntityType = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(ModID, "packed_snowball"),
            FabricEntityTypeBuilder.<PackedSnowballEntity>create(SpawnGroup.MISC, PackedSnowballEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
                    .trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
                    .build() // VERY IMPORTANT DONT DELETE FOR THE LOVE OF GOD PSLSSSSSS
    );
    
    @Override
    public void onInitialize() {

    }
}
```

Finally, add the EntityType to our class' constructors.

```java
public PackedSnowballEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public PackedSnowballEntity(World world, LivingEntity owner) {
        super(ProjectileTutorialMod.PackedSnowballEntityType, owner, world);
    }

    public PackedSnowballEntity(World world, double x, double y, double z) {
        super(ProjectileTutorialMod.PackedSnowballEntityType, x, y, z, world);
    }
```

## Creating a Projectile Item

While we're at it, we should quickly create a projectile item. Most of
the things that are required to create an item are repeated from [this
tutorial](https://fabricmc.net/wiki/tutorial:items), so if you're
unfamiliar with creating an item, refer to that tutorial.  
First, it is necessary to create a new class for the item that extends
`Item`.

```java
public class PackedSnowballItem extends Item {
}
```

Create the constructor, as shown.

```java
public class PackedSnowballItem extends Item {
    public PackedSnowballItem(Settings settings) {
        super(settings);
    }
}
```

Now, we will create a new `TypedActionResult` method. Follow along:

```java
public class PackedSnowballItem extends Item {
    public PackedSnowballItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand); // creates a new ItemStack instance of the user's itemStack in-hand
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 1F); // plays a globalSoundEvent
        /*
        user.getItemCooldownManager().set(this, 5);
        Optionally, you can add a cooldown to your item's right-click use, similar to Ender Pearls.
        */
        if (!world.isClient) {
            PackedSnowballEntity snowballEntity = new PackedSnowballEntity(world, user);
            snowballEntity.setItem(itemStack);
            snowballEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 0F);
            world.spawnEntity(snowballEntity); // spawns entity
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.abilities.creativeMode) {
            itemStack.decrement(1); // decrements itemStack if user is not in creative mode
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
```

Make sure that the projectile that you are launching with this item is
indeed your custom `ProjectileEntity`. Verify this by checking
`PackedSnowballEntity snowballEntity = new PackedSnowballEntity(world, user);`.  
Now, we are finished with creating an item for the `ProjectileEntity`.
Keep in mind that if you do not understand how to create an item, refer
to the ["Item" tutorial](https://fabricmc.net/wiki/tutorial:items).  
Finally, register your item.

```java
public static final Item PackedSnowballItem = new PackedSnowballItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16));

[...]

@Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(ModID, "packed_snowball"), PackedSnowballItem);
    }
```

Back in our `ProjectileEntity` class, we must add the `getDefaultItem()`
into our method.

```java
@Override
    protected Item getDefaultItem() {
        return ProjectileTutorialMod.PackedSnowballItem;
    }
```

Make sure you have the texture for the item in the correct spot, or else
neither the entity or the item will have a texture.

## Rendering your Projectile Entity

Your projectile entity is now defined and registered, but we are not
done. Without a renderer, the `ProjectileEntity` will crash Minecraft.
To fix this, we will define and register the `EntityRenderer` for our
`ProjectileEntity`. To do this, we will need a `EntityRenderer` in the
**ClientModInitializer** and a spawn packet to make sure the texture is
rendered correctly.  
Before we start, we will quickly define an Identifier that we will be
using a lot: our PacketID.

```java
public static final Identifier PacketID = new Identifier(ProjectileTutorialMod.ModID, "spawn_packet");
```

First on the list, we should get the `EntityRenderer` out of the way. Go
into your **ClientModInitializer** and write the following:

```java
@Override
public void onInitializeClient() {
    EntityRendererRegistry.INSTANCE.register(ProjectileTutorialMod.PackedSnowballEntityType, (dispatcher, context) ->
            new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer()));
    [. . .]
}
```

In order for the projectileEntity to be registered, we will need a spawn
packet. Create a new class named `EntitySpawnPacket`, and put this in
that class.

```java
public class EntitySpawnPacket {
    public static Packet<?> create(Entity e, Identifier packetID) {
        if (e.world.isClient)
            throw new IllegalStateException("SpawnPacketUtil.create called on the logical client!");
        PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(Registry.ENTITY_TYPE.getRawId(e.getType()));
        byteBuf.writeUuid(e.getUuid());
        byteBuf.writeVarInt(e.getEntityId());

        PacketBufUtil.writeVec3d(byteBuf, e.getPos());
        PacketBufUtil.writeAngle(byteBuf, e.pitch);
        PacketBufUtil.writeAngle(byteBuf, e.yaw);
        return ServerSidePacketRegistry.INSTANCE.toPacket(packetID, byteBuf);
    }
    public static final class PacketBufUtil {

        /**
         * Packs a floating-point angle into a {@code byte}.
         *
         * @param angle
         *         angle
         * @return packed angle
         */
        public static byte packAngle(float angle) {
            return (byte) MathHelper.floor(angle * 256 / 360);
        }

        /**
         * Unpacks a floating-point angle from a {@code byte}.
         *
         * @param angleByte
         *         packed angle
         * @return angle
         */
        public static float unpackAngle(byte angleByte) {
            return (angleByte * 360) / 256f;
        }

        /**
         * Writes an angle to a {@link PacketByteBuf}.
         *
         * @param byteBuf
         *         destination buffer
         * @param angle
         *         angle
         */
        public static void writeAngle(PacketByteBuf byteBuf, float angle) {
            byteBuf.writeByte(packAngle(angle));
        }

        /**
         * Reads an angle from a {@link PacketByteBuf}.
         *
         * @param byteBuf
         *         source buffer
         * @return angle
         */
        public static float readAngle(PacketByteBuf byteBuf) {
            return unpackAngle(byteBuf.readByte());
        }

        /**
         * Writes a {@link Vec3d} to a {@link PacketByteBuf}.
         *
         * @param byteBuf
         *         destination buffer
         * @param vec3d
         *         vector
         */
        public static void writeVec3d(PacketByteBuf byteBuf, Vec3d vec3d) {
            byteBuf.writeDouble(vec3d.x);
            byteBuf.writeDouble(vec3d.y);
            byteBuf.writeDouble(vec3d.z);
        }

        /**
         * Reads a {@link Vec3d} from a {@link PacketByteBuf}.
         *
         * @param byteBuf
         *         source buffer
         * @return vector
         */
        public static Vec3d readVec3d(PacketByteBuf byteBuf) {
            double x = byteBuf.readDouble();
            double y = byteBuf.readDouble();
            double z = byteBuf.readDouble();
            return new Vec3d(x, y, z);
        }
    }
}
```

This will basically read and write vectors and angles that will allow
the entity's texture to be rendered correctly. I will not go in-depth
about spawn packets here, but you could read up on what they do and how
they function. For now, we can include this and move on.  
Back to our **ClientModInitializer**, we will create a new method and
put the following in that method.

```java
public void receiveEntityPacket() {
    ClientSidePacketRegistry.INSTANCE.register(PacketID, (ctx, byteBuf) -> {
        EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
        UUID uuid = byteBuf.readUuid();
        int entityId = byteBuf.readVarInt();
        Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
        float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
        float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
        ctx.getTaskQueue().execute(() -> {
            if (MinecraftClient.getInstance().world == null)
                throw new IllegalStateException("Tried to spawn entity in a null world!");
            Entity e = et.create(MinecraftClient.getInstance().world);
            if (e == null)
                throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
            e.updateTrackedPosition(pos);
            e.setPos(pos.x, pos.y, pos.z);
            e.pitch = pitch;
            e.yaw = yaw;
            e.setEntityId(entityId);
            e.setUuid(uuid);
            MinecraftClient.getInstance().world.addEntity(entityId, e);
        });
    });
}
```

Back in our `ProjectileEntity` class, we must add a method to make sure
everything works correctly.

```java
    @Override
public Packet createSpawnPacket() {
    return EntitySpawnPacket.create(this, ProjectileTutorialClient.PacketID);
}
```

Finally, make sure to call this method in the **onInitializeClient()**
method.

```java
@Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(ProjectileTutorialMod.PackedSnowballEntityType, (dispatcher, context) ->
                new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer()));
        receiveEntityPacket();
    }
```

## Hoping to God It Works

Now, your projectile should be working in-game! Just make sure your
textures are in the right place, and your item and projectile should be
working.

If you would like to try out this projectile, download
[here](https://github.com/spxctreofficial/fabric-projectile-tutorial/releases/download/v1.0.0/projectile-tutorial-1.0.0.jar).

\[INSERT USABLE PICTURE HERE\]

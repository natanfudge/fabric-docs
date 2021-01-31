# Minecraft 1.16 and later

The information below applies to Minecraft 1.15 and earlier, while in
Minecraft 1.16, dimensions can be defined in data packs. More
information about custom dimensions in 1.16 can be found in the
[official wiki](https://minecraft.gamepedia.com/Custom_dimension).

However, you must still create a portal using fabric, [more infomation
about custom portals here.](../Modding-Tutorials/custom_portals.md)

An example that also shows some of the fabric-api specific code can be
found in the [fabric-dimensions-v1
testmod](https://github.com/FabricMC/fabric/tree/1.16/fabric-dimensions-v1/src/testmod).

# Creating a Dimension \[WIP\] (1.15 and earlier)

Creating your own dimension is an advanced topic. This tutorial assumes
you have already read through the previous tutorials on world
generation, and have other basic knowledge such as how to create your
own blocks.

#### Creating your DimensionType

The first thing you want to do is register your custom
[DimensionType](../Modding-Tutorials/World-Generation/dimensionconcepts.md). We are going to create a
bee dimension.

```java
public class TutorialDimensions {
    public static final FabricDimensionType BEE = FabricDimensionType.builder()
        .defaultPlacer((oldEntity, destinationWorld, portalDir, horizontalOffset, verticalOffset) -> new BlockPattern.TeleportTarget(new Vec3d(destinationWorld.getTopPosition(Heightmap.Type.WORLD_SURFACE, BlockPos.ORIGIN)), oldEntity.getVelocity(), (int) oldEntity.yaw))
        .factory(BeeDimension::new)
        .skyLight(false)
        .buildAndRegister(new Identifier(TutorialMod.MOD_ID, "bee"));
    
    public static void register() {
        // load the class
    }
}
```

```java
public class TutorialMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // ...
        TutorialDimensions.register();
    }
}
```

The `defaultPlacer` determines the default placement when an entity is
teleported into this dimension. Here we have made it so the entity
spawns on the top block at 0, 0 when entering the dimension. If you want
custom portal logic, this is the place to do it. See the [EntityPlacer
documentation](https://github.com/FabricMC/fabric/blob/1.15/fabric-dimensions-v1/src/main/java/net/fabricmc/fabric/api/dimension/v1/EntityPlacer.java)
for details.

#### The Dimension class

```java
public class BeeDimension extends Dimension {
    private static final Vec3d FOG_COLOR = new Vec3d(0.54, 0.44, 0.16);
    
    public BeeDimension(World world, DimensionType type) {
        // The third argument indicates how visually bright light level 0 is, with 0 being no extra brightness and 1 being like night vision.
        // The overworld and the end set this to 0, and the Nether sets this to 0.1. We want our dimension to be a bit brighter.
        super(world, type, 0.5f);
    }
    
    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        // For now, we'll create a superflat world to get a basic dimension working.
        // We'll come back and change this later.
        FlatChunkGeneratorConfig generatorConfig = FlatChunkGeneratorConfig.getDefaultConfig();
        // The biome everywhere will be jungle
        FixedBiomeSourceConfig biomeConfig = BiomeSourceType.FIXED.getConfig(world.getLevelProperties()).setBiome(Biomes.JUNGLE);
        return ChunkGeneratorType.FLAT.create(world, BiomeSourceType.FIXED.applyConfig(biomeConfig), generatorConfig);
    }
    
    // The following 2 methods relate to the dimension's spawn point.
    // You can return null if you don't want the player to be able to respawn in these dimensions.
    
    @Override
    public BlockPos getSpawningBlockInChunk(ChunkPos chunkPos, boolean checkMobSpawnValidity) {
        return null;
    }
    
    @Override
    public BlockPos getTopSpawningBlockPosition(int x, int z, boolean checkMobSpawnValidity) {
        return null;
    }
    
    @Override
    public float getSkyAngle(long worldTime, float tickDelta) {
        // Returns a sky angle ranging between 0 and 1.
        // This is a very simple implementation that approximates the overworld sky angle, but is easier to understand.
        // In the overworld, the sky does not quite move at a constant rate, see the OverworldDimension code for details.
        final int dayLength = 24000;
        double daysPassed = ((double) worldTime + tickDelta) / dayLength;
        return MathHelper.fractionalPart(daysPassed - 0.25);
    }
    
    @Override
    public boolean hasVisibleSky() {
        return true;
    }
    
    // Fog color RGB
    @Environment(EnvType.CLIENT)
    @Override
    public Vec3d getFogColor(float skyAngle, float tickDelta) {
        return FOG_COLOR;
    }
    
    @Override
    public boolean canPlayersSleep() {
        return false;
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public boolean isFogThick(int x, int z) {
        return false;
    }
    
    @Override
    public DimensionType getType() {
        return TutorialDimensions.BEE;
    }
}
```

#### Creating a ChunkGenerator \[TODO\]

#### Creating a BiomeSource \[TODO\]

#### Creating a SurfaceBuilder \[TODO\]


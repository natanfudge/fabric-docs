# Adding Structure Features \[1.16.3\]

We're going to look at registering and placing structures in your world.

To view examples of vanilla structures in action, `IglooFeature` and
`IglooGenerator` are a good place to start.

You are going to need a feature and generator for the most basic
structure. The feature handles the process of registering the structure
and loading it in when the world is generating. The generator handles
the placement of blocks or loading in a structure file if you choose to
do so.

Note that this tutorial depends on [Biome Modification API in Fabric
API](https://github.com/FabricMC/fabric/pull/1097) which is marked as
experimental. If the API doesn't work, consider using [the mixin
version](../?rev=1599808070.md).

## Creating a Feature

To create a basic feature, we recommend creating a class that extends
`StructureFeature<DefaultFeatureConfig>`. Various vanilla structures,
such as shipwrecks, igloos, and temples, use
`StructureFeature<DefaultFeatureConfig>` as a base.

You will have to override `getStructureStartFactory` method. For
`getStructureStartFactory`, most vanilla structures make a class that
extends `StructureStart` inside their feature class.

```java
public class MyFeature extends StructureFeature<DefaultFeatureConfig> {
  public MyFeature(Codec<DefaultFeatureConfig> codec) {
    super(codec);
  }

  @Override
  public StructureFeature.StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
    return Start::new;
  }

  public static class Start extends StructureStart<DefaultFeatureConfig> {
    public Start(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references,
        long seed) {
      super(feature, chunkX, chunkZ, box, references, seed);
    }

    // Called when the world attempts to spawn in a new structure, and is the gap between your feature and generator.
    public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX,
        int chunkZ, Biome biome, DefaultFeatureConfig config) {
      int x = chunkX * 16;
      int z = chunkZ * 16;
      int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
      BlockPos pos = new BlockPos(x, y, z);
      BlockRotation rotation = BlockRotation.random(this.random);
      MyGenerator.addPieces(manager, pos, rotation, this.children);
      this.setBoundingBoxFromChildren();
    }
  }
}
```

## Creating a Generator

As you have probably noticed, we need to create a generator.

This is where structure files and generating straight from a `generate`
method part ways. There are two ways to go about this:

- If you want, you can simply override `generate` in your piece class
  and use `addBlock` to place blocks directly in the world. This is a
  valid option and was popular pre-1.13.
- Use structure files. These are rather powerful at this point and are
  highly recommended.

In this tutorial, we'll use a structure file. It doesn't need to
override anything, but does require the following:

- An identifier that points to your structure file; use `"igloo/top"`
  if you need an example.
- Some sort of setup method - `addPieces` is a good name.

```java
public class MyGenerator {
  private static final Identifier IGLOO_TOP = new Identifier("igloo/top");

  public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces) {
    pieces.add(new MyPiece(manager, pos, IGLOO_TOP, rotation));
  }
}
```

In your `addPieces` method, you can choose which structure pieces are
added to your generation process.

We're now going to create the piece we just referenced; make a class
called `MyPiece` that extends `SimpleStructurePiece` *within your
generator class*.

Override required methods, and add a constructor that takes in a
`StructureManager`, `BlockPos`, `Identifier` and `Rotation`. **toNbt
isn't required but is available if you need it**. We're also
implementing `initializeStructureData`, which is not an override. We
also have 2 constructors: 1 for our own pieces, and one for registry. A
basic template would be:

```java
public static class MyPiece extends SimpleStructurePiece {
  private final BlockRotation rotation;
  private final Identifier template;

  public MyPiece(StructureManager structureManager, CompoundTag compoundTag) {
    super(ExampleMod.MY_PIECE, compoundTag);
    this.template = new Identifier(compoundTag.getString("Template"));
    this.rotation = BlockRotation.valueOf(compoundTag.getString("Rot"));
    this.initializeStructureData(structureManager);
  }

  public MyPiece(StructureManager structureManager, BlockPos pos, Identifier template, BlockRotation rotation) {
    super(ExampleMod.MY_PIECE, 0);
    this.pos = pos;
    this.rotation = rotation;
    this.template = template;

    this.initializeStructureData(structureManager);
  }

  private void initializeStructureData(StructureManager structureManager) {
    Structure structure = structureManager.getStructureOrBlank(this.template);
    StructurePlacementData placementData = (new StructurePlacementData())
      .setRotation(this.rotation)
      .setMirror(BlockMirror.NONE)
      .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
    this.setStructureData(structure, this.pos, placementData);
  }

  protected void toNbt(CompoundTag tag) {
    super.toNbt(tag);
    tag.putString("Template", this.template.toString());
    tag.putString("Rot", this.rotation.name());
  }

  @Override
  protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random,
      BlockBox boundingBox) {
  }
}
```

`handleMetadata` is where you look at data blocks within your structure
and do tasks based on what you find. In vanilla structures, data blocks
are placed above chests so they can be filled with loot in this method.

We set the `StructurePieceType` to `ExampleMod.MY_PIECE`; this is the
variable that holds your registered structure piece.

## Registering Structures

The last step is to register our structures. We're going to need to
register:

- structure
- piece
- configured structure

```java
public class ExampleMod implements ModInitializer {
  public static final StructurePieceType MY_PIECE = MyGenerator.MyPiece::new;
  private static final StructureFeature<DefaultFeatureConfig> MY_STRUCTURE = new MyFeature(DefaultFeatureConfig.CODEC);
  private static final ConfiguredStructureFeature<?, ?> MY_CONFIGURED = MY_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT);

  @Override
  public void onInitialize() {
    Registry.register(Registry.STRUCTURE_PIECE, new Identifier("tutorial", "my_piece"), MY_PIECE);
    FabricStructureBuilder.create(new Identifier("tutorial", "my_structure"), MY_STRUCTURE)
      .step(GenerationStep.Feature.SURFACE_STRUCTURES)
      .defaultConfig(32, 8, 12345)
      .adjustsSurface()
      .register();

    RegistryKey<ConfiguredStructureFeature<?, ?>> myConfigured = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN,
        new Identifier("tutorial", "my_structure"));
    BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, myConfigured.getValue(), MY_CONFIGURED);
  }
}
```

## Adding a configured feature to biomes

In this tutorial, we add our structure to all biomes.

```java
public class ExampleMod implements ModInitializer {
  [...]
 
  @Override
  public void onInitialize() {
    [...]

    BiomeModifications.addStructure(BiomeSelectors.all(), myConfigured);
  }
}
```

## Result

You should be met with igloos. You can use below command to find your
structure in the world.

    /locate tutorial:my_structure

<img src="/tutorial/structures.png" width="800" alt="structures.png" />


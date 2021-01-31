## Adding Features \[1.16.3\]

Rocks, trees, ores, and ponds are all examples of features. They are
simple generation additions to the world which generate depending on how
they are configured. In this tutorial, we'll look at generating a simple
stone spiral feature randomly.

There are 3 steps that are required to add a feature to a biome.

- Create a feature
- Configure a feature
- Use [Biome Modification API in Fabric API](https://github.com/FabricMC/fabric/pull/1097) to add your
  feature to biomes.

Note that the Biome Modification API is marked as experimental. If the
API doesn't work, consider using [the mixin version](../?rev=1599388928.md).

### Creating a feature

A simple Feature looks like this:

```java
public class StoneSpiralFeature extends Feature<DefaultFeatureConfig> {
  public StoneSpiralFeature(Codec<DefaultFeatureConfig> config) {
    super(config);
  }

  @Override
  public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos,
      DefaultFeatureConfig config) {
    BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos);
    Direction offset = Direction.NORTH;

    for (int y = 1; y <= 15; y++) {
      offset = offset.rotateYClockwise();
      world.setBlockState(topPos.up(y).offset(offset), Blocks.STONE.getDefaultState(), 3);
    }

    return true;
  }
}
```

The `Feature<DefaultFeatureConfig>` constructor takes in a
`Codec<DefaultFeatureConfig>`. You can pass in
`DefaultFeatureConfig.CODEC` for default config features, either
directly in the super call in the constructor or when you instantiate
the feature.

`generate` is called when the chunk decides to generate the feature. If
the feature is configured to spawn every chunk, this would be called for
each chunk being generated as well. In the case of the feature being
configured to spawn at a certain rate per biome, `generate` would only
be called in instances where the world wants to spawn the structure.

In our implementation, we'll build a simple 15-block tall spiral of
stone starting at the top block in the world.

Features can be registered like most other content in the game, and
there aren't any special builders or mechanics you'll have to worry
about.

```java
public class ExampleMod implements ModInitializer {
  private static final Feature<DefaultFeatureConfig> STONE_SPIRAL = new StoneSpiralFeature(DefaultFeatureConfig.CODEC);

  @Override
  public void onInitialize() {
    Registry.register(Registry.FEATURE, new Identifier("tutorial", "stone_spiral"), STONE_SPIRAL);
  }
}
```

### Configuring a feature

We need to give a configuration to a feature. Make sure to register
configured feature as well as feature.

```java
public class ExampleMod implements ModInitializer {
  public static final ConfiguredFeature<?, ?> STONE_SPIRAL_CONFIGURED = STONE_SPIRAL.configure(FeatureConfig.DEFAULT)
      .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(5)));

  @Override
  public void onInitialize() {
    [...]
    
    RegistryKey<ConfiguredFeature<?, ?>> stoneSpiral = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
        new Identifier("tutorial", "stone_spiral"));
    Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, stoneSpiral.getValue(), STONE_SPIRAL_CONFIGURED);
  }
}
```

The Decorator represents how the world chooses to place your feature. To
choose the correct Decorator, check out vanilla features with a similar
style to your own. The decorator config branches off this; in the case
of `CHANCE`, you would pass in an instance of `ChanceDecoratorConfig`.

### Adding a configured feature to a biome

We use the Biome Modification API.

```java
public class ExampleMod implements ModInitializer {
  [...]

  @Override
  public void onInitialize() {
    [...]
    BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Feature.UNDERGROUND_ORES, stoneSpiral);
  }
}
```

The first argument of `addFeature` determines what biomes the structure
is generated in.

The second argument helps determine when the structure is generated. For
above-ground houses you may go with `SURFACE_STRUCTURES`, and for caves,
you might go with `RAW_GENERATION`.

#### Result

![](https://i.imgur.com/Kr59o0B.png)

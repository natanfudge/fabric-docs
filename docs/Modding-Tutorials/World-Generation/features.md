## Generating Features in your World

Rocks, trees, ores, and ponds are all examples of Features. They are
simple generation additions to the world which generate depending on how
they are configured. In this tutorial, we'll look at generating a simple
stone spiral feature in our world randomly.

### Creating a Feature class

A simple Feature looks like this:

```java
public class StoneSpiralFeature extends Feature<DefaultFeatureConfig> {

    public StoneSpiralFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos);
        Direction offset = Direction.NORTH;

        for (int y = 1; y < 16; y++) {
            offset = offset.rotateYClockwise();
            world.setBlockState(topPos.up(y).offset(offset), Blocks.STONE.getDefaultState(), 3);
        }

        return true;
    }
}
```

The constructor takes in a `Function<Dynamic<? extends
DefaultFeatureConfig>>`, which is a factory for data fixer config
instances. You can pass in `DefaultFeatureConfig::deserialize` for
default config features, either directly in the super call or when you
instantiate the feature.

\`generate\` is called when the chunk decides to generate the feature.
If the feature is configured to spawn every chunk, this would be called
for each chunk being generated as well. In the case of the feature being
configured to spawn at a certain rate per biome, \`generate\` would only
be called in instances where the world wants to spawn the structure.

In our implementation, we'll build a simple 16-block tall spiral of
stone starting at the top block in the world:

```java
@Override
public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
    BlockPos topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos);
    Direction offset = Direction.NORTH;

    for (int y = 1; y < 16; y++) {
        offset = offset.rotateYClockwise();
        world.setBlockState(topPos.up(y).offset(offset), Blocks.STONE.getDefaultState(), 3);
    }

    return true;
}
```

### Registering a Feature

Features can be registered like most other content in the game, and
there aren't any special builders or mechanics you'll have to worry
about.

```java
private static final Feature<DefaultFeatureConfig> LAVA_HOLE = Registry.register(
    Registry.FEATURE,
    new Identifier("tutorial", "stone_spiral"),
    new StoneSpiralFeature(DefaultFeatureConfig::deserialize)
);
```

### Adding a Feature to a Biome

Biome has a method called `addFeature`, which is used to add Features to
the biome's generation process. You can view more detailed usage of this
method inside each Biome class (such as `ForestBiome` or
`SavannaBiome`).

We can iterate over `Registry.BIOME` to add our Feature to every Biome.

```java
Registry.BIOME.forEach(biome -> biome.addFeature(
        GenerationStep.Feature.RAW_GENERATION,
    Biome.configureFeature(
        LAVA_HOLE,
        new DefaultFeatureConfig(),
        Decorator.CHANCE_HEIGHTMAP,
        new ChanceDecoratorConfig(100)
    )
));
```

The first argument of `addFeature` helps determine when the structure is
generated. For above-ground houses you may go with `SURFACE_STRUCTURES`,
and for caves, you might go with `RAW_GENERATION`.

The second argument of `addFeature` is a ConfiguredFeature, which you
can create through `Biome.configureFeature`. The latter takes in an
instance of your feature, an instance of your feature's config class, a
decorator, and a decorator config.

The Decorator represents how the world chooses to place your Feature.
`CHANCE_HEIGHTMAP` works by looking at a heightmap, whereas
`NOISE_HEIGHTMAP_32` works with noise. To choose the correct Decorator,
check out vanilla Features with a similar style to your own. The
decorator config branches off this; in the case of `CHANCE_HEIGHTMAP`,
you would pass in an instance of `ChanceHeightmapDecorator`.

#### Results

![https://i.imgur.com/Kr59o0B.png](https://i.imgur.com/Kr59o0B.png)

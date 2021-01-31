# Generating Custom Ores \[1.16.3\]

A lot of mods add their own ores, and you'll need a way to place them in
existing biomes for players to find. In this tutorial, we'll look at
adding ores to existing biomes. There are 2 steps that are required to
add ores to biomes.

- Make a ConfiguredFeatures. This defines how your ore block is
  spawned.
- Use [Biome Modification API in Fabric API](https://github.com/FabricMC/fabric/pull/1097) to add your
  feature to biomes.

Note that the Biome Modification API is marked as experimental. If the
API doesn't work, consider using [the mixin version](../?rev=1599388827.md).

We'll assume you've already created your own ore block at this point.
Wool block will serve as our replacement throughout this tutorial.
Replace references to wool with your ore when appropriate.

### Adding to the overworld biomes

In this section, our goal will be spawning the ore in the overworld.

We need to create a ConfiguredFeature. Make sure to register your
ConfiguredFeature at `onInitialize`. Feel free to change the values to
suit your mod.

```java
public class ExampleMod implements ModInitializer {
  private static ConfiguredFeature<?, ?> ORE_WOOL_OVERWORLD = Feature.ORE
    .configure(new OreFeatureConfig(
      OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
      Blocks.WHITE_WOOL.getDefaultState(),
      9)) // vein size
    .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(
      0, // bottom offset
      0, // min y level
      64))) // max y level
    .spreadHorizontally()
    .repeat(20); // number of veins per chunk

  @Override
  public void onInitialize() {
    RegistryKey<ConfiguredFeature<?, ?>> oreWoolOverworld = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
        new Identifier("tutorial", "ore_wool_overworld"));
    Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreWoolOverworld.getValue(), ORE_WOOL_OVERWORLD);
    BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, oreWoolOverworld);
  }
}
```

#### Result

You should see wool spawning in the overworld. You can use below command
to remove stone blocks surrounding you.

    /fill ~-8 0 ~-8 ~8 ~ ~8 minecraft:air replace minecraft:stone

<img src="/tutorial/ores.png" width="800" alt="ores.png" />

### Adding to the nether biomes

In this section, based on the code in the previous section, we will add
the ore to the nether biomes.

We have to replace `OreFeatureConfig.Rules.BASE_STONE_OVERWORLD` with
`OreFeatureConfig.Rules.BASE_STONE_NETHER` because blocks used as base
block are different in the overall biomes and the nether biomes.

```java
public class ExampleMod implements ModInitializer {
  private static ConfiguredFeature<?, ?> ORE_WOOL_NETHER = Feature.ORE
    .configure(new OreFeatureConfig(
      OreFeatureConfig.Rules.BASE_STONE_NETHER, // We use OreFeatureConfig.Rules.BASE_STONE_NETHER for nether
      Blocks.WHITE_WOOL.getDefaultState(),
      9))
    .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(
      0,
      0,
      64)))
    .spreadHorizontally()
    .repeat(20);

  @Override
  public void onInitialize() {
    RegistryKey<ConfiguredFeature<?, ?>> oreWoolNether = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
        new Identifier("tutorial", "ore_wool_nether"));
    Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreWoolNether.getValue(), ORE_WOOL_NETHER);
    BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, oreWoolNether);
  }
}
```

### Adding to the end biomes

In this section, based on the code in the overworld section, we will add
the ore to the end biomes.

We replace `OreFeatureConfig.Rules.BASE_STONE_OVERWORLD` with
`new BlockMatchRuleTest(Blocks.END_STONE)` because endstone is used as a
base block in the end biomes.

```java
public class ExampleMod implements ModInitializer {
  private static ConfiguredFeature<?, ?> ORE_WOOL_END = Feature.ORE
    .configure(new OreFeatureConfig(
      new BlockMatchRuleTest(Blocks.END_STONE), // base block is endstone in the end biomes
      Blocks.WHITE_WOOL.getDefaultState(),
      9))
    .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(
      0,
      0,
      64)))
    .spreadHorizontally()
    .repeat(20);

  @Override
  public void onInitialize() {
    RegistryKey<ConfiguredFeature<?, ?>> oreWoolEnd = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
        new Identifier("tutorial", "ore_wool_end"));
    Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreWoolEnd.getValue(), ORE_WOOL_END);
    BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, oreWoolEnd);
  }
}
```


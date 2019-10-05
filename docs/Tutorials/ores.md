# Adding Ores to the World

A lot of mods add their own ores, and you'll need a way to place them in
existing biomes for players to find. In this tutorial, we'll look at
adding ores to existing biomes and biomes added by other mods. There are
2 steps that are required to add ores to biomes.

- Iterate over the biome registry to add your ores to existing biomes.
- Use the RegistryEntryAddedCallback to ensure your ore gets added
  into any biomes added by mods.

We'll assume you've already created your own ore block at this point.
Quartz Ore will serve as our replacement, and our goal will be spawning
it in overworld biomes. Replace references to Quartz Ore with your ore
when appropriate.

### Adding ores to a biome

First we need to create a method to process a biome, checking if it is a
valid biome then adding the ore.

```java
private void handleBiome(Biome biome) {
    if(biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
        biome.addFeature(
                GenerationStep.Feature.UNDERGROUND_ORES,
                Biome.configureFeature(
                    Feature.ORE,
                new OreFeatureConfig(
                    OreFeatureConfig.Target.NATURAL_STONE,
                    Blocks.NETHER_QUARTZ_ORE.getDefaultState(),
                        8 //Ore vein size
                ),
                    Decorator.COUNT_RANGE,
                new RangeDecoratorConfig(
                    8, //Number of veins per chunk
                    0, //Bottom Offset
                    0, //Min y level
                    64 //Max y level
                )));
    }
}
```

This method adds your ore to the overworld, with the provided spawn
settings. Feel free to change the values to suit your mod.

### Iterating Biome Registry

What we need to do next is process all biomes that have been registered
as well as all biomes that will be registered in the future (as added by
other mods). We first iterate over the current registry, then register a
listener that will be called for future additions.

```java
@Override
public void onInitialize() {
    //Loop over existing biomes
    Registry.BIOME.forEach(this::handleBiome);

    //Listen for other biomes being registered
    RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> handleBiome(biome));
}
```

### Conclusion

You should see quartz ore spawning in the overworld:

![Quartz Ores](https://i.imgur.com/UemsMaI.png)

# Adding a Biome

## Introduction

This wiki tutorial is focused on registering and adding biomes to the world.

To add a biome, we will first need to create and register the biome, then add it to the world using helper methods from Fabric API. This tutorial will go over:

* Creating a biome
* Registering a biome
* Adding a biome to a climate zone in the world
* Allowing the player to spawn in the biome

We will also briefly go over other helpful biome-adding methods in the api.

## Creating a Biome

To create a biome, create a class that extends Biome. This is the base class that holds biome information, and all vanilla biomes extend this. This class defines:

* The basic properties of the biome
* What features \(trees, plants and structures\) generate here
* What entities spawn here

We need to pass a `Biome.Settings` instance with **all** the basic properties of the biome to the super constructor. Missing one property will likely cause the game to crash. It is recommended to look at vanilla biomes such as `MountainsBiome` and `ForestBiome` as examples.

Some important settings are depth \(height\), scale \(hill size\), and precipitation \(weather\)

```java
public class MyBiome extends Biome
{
    public MyBiome()
    {
        super(new Biome.Settings().configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG).precipitation(Biome.Precipitation.RAIN).category(Biome.Category.PLAINS).depth(0.24F).scale(0.2F).temperature(0.6F).downfall(0.7F).waterColor(4159204).waterFogColor(329011).parent((String)null));
    }
}
```

We then need to specify the features and entities that spawn in the biome. Aside from some structures, trees, rocks, plants and custom entities, these are mostly the same for each biome. Vanilla configured features for biomes are defined through methods in `DefaultBiomeFeatures`.

```java
public class MyBiome extends Biome
{
    public MyBiome()
    {
        super(new Biome.Settings().configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG).precipitation(Biome.Precipitation.RAIN).category(Biome.Category.PLAINS).depth(0.24F).scale(0.2F).temperature(0.6F).downfall(0.7F).waterColor(4159204).waterFogColor(329011).parent((String)null));

        this.addStructureFeature(Feature.MINESHAFT, new MineshaftFeatureConfig(0.004D, MineshaftFeature.Type.NORMAL));
        this.addStructureFeature(Feature.STRONGHOLD, FeatureConfig.DEFAULT);
        this.addStructureFeature(Feature.VILLAGE, new VillageFeatureConfig("village/plains/town_centers", 6));
        DefaultBiomeFeatures.addLandCarvers(this);
        DefaultBiomeFeatures.addDefaultStructures(this);
        DefaultBiomeFeatures.addDefaultLakes(this);
        DefaultBiomeFeatures.addDungeons(this);
        DefaultBiomeFeatures.addExtraMountainTrees(this);
        DefaultBiomeFeatures.addDefaultFlowers(this);
        DefaultBiomeFeatures.addDefaultGrass(this);
        DefaultBiomeFeatures.addMineables(this);
        DefaultBiomeFeatures.addDefaultOres(this);
        DefaultBiomeFeatures.addDefaultDisks(this);
        DefaultBiomeFeatures.addDefaultVegetation(this);
        DefaultBiomeFeatures.addSprings(this);
        DefaultBiomeFeatures.addFrozenTopLayer(this);
        this.addSpawn(EntityCategory.CREATURE, new Biome.SpawnEntry(EntityType.SHEEP, 12, 4, 4));
        this.addSpawn(EntityCategory.CREATURE, new Biome.SpawnEntry(EntityType.PIG, 10, 4, 4));
        this.addSpawn(EntityCategory.CREATURE, new Biome.SpawnEntry(EntityType.CHICKEN, 10, 4, 4));
        this.addSpawn(EntityCategory.CREATURE, new Biome.SpawnEntry(EntityType.COW, 8, 4, 4));
        this.addSpawn(EntityCategory.AMBIENT, new Biome.SpawnEntry(EntityType.BAT, 10, 8, 8));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.SPIDER, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIE, 95, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.SKELETON, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.CREEPER, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.SLIME, 100, 4, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.ENDERMAN, 10, 1, 4));
        this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.WITCH, 5, 1, 1));
    }
}
```

## Registering Biomes

To register your biome, we will create a field which holds a biome instance, and add our biome to `Registry.BIOME`. It is recommended to make a class to hold your biome objects.

```java
public class TutorialBiomes
{
    public static final Biome MY_BIOME = Registry.register(Registry.BIOME, new Identifier("tutorial", "my_biome"), new MyBiome());
}
```

You should also give your biome a language entry in your `en_us.json` file:

```javascript
{
  "biome.tutorial.my_biome": "My Biome"
}
```

## Adding a biome to the world generator

To make your biome spawn in the world, we will use the helper methods provided by the Fabric-Biomes API module. The code for this should ideally be run during mod initialization.

We need to specify the climate to which the biome is added, the biome which we are adding, and the weight of the biome \(a double value\). The weight is a measurement of the chance the biome has to spawn. A higher weight corresponds to a higher chance for the biome to spawn, proportional to the weights of other biomes. The Javadoc comments of each climate give the vanilla biome weights in each climate. You may want to give your biome a higher weight during testing so you can find the biome more easily.

In this tutorial, we will add the custom biome to the `TEMPERATE` and `COOL` climates as an example:

```java
public class ExampleMod implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        OverworldBiomes.addContinentalBiome(OverworldClimate.TEMPERATE, TutorialBiomes.MY_BIOME, 2D);
        OverworldBiomes.addContinentalBiome(OverworldClimate.COOL, TutorialBiomes.MY_BIOME, 2D);
    }
}
```

To make the player able to spawn in the biome, we will use another method from the fabric-biomes api module:

```java
FabricBiomes.addSpawnBiome(TutorialBiomes.MY_BIOME);
```

**Congratulations!** Your biome should now be generating in the world!

## Other useful biome methods

There are other useful methods in the fabric biomes api that you may want to use, that add extra functionality.

* **Setting the river biome**

For example, setting your biome not to generate a river:

```java
OverworldBiomes.setRiverBiome(TutorialBiomes.MY_BIOME, null);
```

* **Adding biome variants**

The third number is the chance \(out of 1\) for the biome to replaced with the specified variant. For example, setting your biome to be a variant of plains, 33% of the time:

```java
OverworldBiomes.addBiomeVariant(Biomes.PLAINS, TutorialBiomes.MY_BIOME, 0.33);
```

### The following methods take a weight value which specifies how common the biome is relative to other specified variants

* **Adding hills biomes**

For example, setting mountains to be a "hills" variant of your biome:

```java
OverworldBiomes.addHillsBiome(TutorialBiomes.MY_BIOME, Biomes.MOUNTAINS, 1);
```

* **Adding biome edges**

For example, making forest generate on the edge of your biome:

```java
OverworldBiomes.addEdgeBiome(TutorialBiomes.MY_BIOME, Biomes.FOREST, 1);
```

* **Adding biome shores / beaches**

For example, making stone beach generate on the shore of your biome:

```java
OverworldBiomes.addShoreBiome(TutorialBiomes.MY_BIOME, Biomes.STONE_BEACH, 1);
```


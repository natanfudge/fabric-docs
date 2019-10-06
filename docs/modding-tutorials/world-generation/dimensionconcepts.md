# Dimension Concepts

Creating a dimension is an advanced concept that takes quite a bit of work to fully understand. A simple dimension can be created fairly easy, but as soon as you dive deeper in you'll be bombarded with tons of new classes that can be hard to grasp at times. This tutorial acts as an overview of the main concepts you'll be using when creating a dimension. Categories are titled with relevant class names.

## Dimension

The Dimension class is the core of your new dimension. It handles the overarching logic surrounding your dimension. Can a player sleep in a bed here? Does it have a world border? Can the player see the sky? It also is responsible for creating your ChunkGenerator to generate land.

## DimensionType

DimensionType is the registry wrapper around your dimension. It has an ID and is what you register to add your Dimension. It's also responsible for saving and loading your dimension from a file.

## ChunkGenerator

Responsible for using noise functions to place blocks in the world. It is not \(or should not be\) responsible for actually decorating and choosing separate blocks-- in most cases, you will only be placing stone or another base block of your choice. If we didn't have decoration or any extra steps after this, the overworld would theoretically just be a landscape entirely made of stone and nothing else.

## ChunkGeneratorType

Again, the registry wrapper around your ChunkGenerator. Can't be registered and requires hacks to use-- a fix is _majorly_ needed for this.

## Biome

Biome is a section of your dimension that chooses how that area looks. It is responsible for mob spawns, plants, lakes and rivers, caves, grass color, and much more. In a proper setup, it is also responsible for replacing already generated stone with proper blocks, such as top grass/dirt and ores.

## BiomeSource

When creating a dimension, you can either choose to use a single biome everywhere or use a BiomeSource. BiomeSource is used to pick randomly from several different biomes.

## SurfaceBuilder

A SurfaceBuilder is responsible for replacing the stone we mentioned earlier with other blocks. Each biome has a SurfaceBuilder attached to it. As an example, Plains & Forest both use the DEFAULT SurfaceBuilder, because they both want grass and dirt as their top block. They are different because the biome can still choose to set trees or different height scaling-- in other words, a SurfaceBuilder is semi-reusable depending on the situation.


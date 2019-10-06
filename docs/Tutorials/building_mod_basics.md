# Building a Mod Pt. 1 - Basics (DRAFT)

By falseresync, don’t change pls.

In this tutorial I’ll show you all of the basics of the modding through
the creation of a real working mod\! By the end of the tutorial you
would be able to create items, blocks, give them textures and make them
craftable.

## Prerequisites

To build a mod you need to have JDK and some IDE installed. This
tutorial assumes that you are familiar with Java development at least at
the level of understanding the code. *This is not a Java tutorial\!*

Firstly, you need to download [a modding
kit](https://github.com/FabricMC/fabric-example-mod) from Github. Then
unpack it wherever you want and import it to your IDE.

That’s it\! You’re ready for modding\! If you have troubles following
this guide you can read more about installation [here](../Tutorials/setup.md).
If you’re still struggling don’t be afraid to ask for help on our
[Discord server](https://discord.gg/v6v4pMv)\!

## Structure Overview

As you can see there’s a lot of files in the folder. You don’t need most
of them. Right now you should care about these files:

- **gradle.properties** Here you configure most essential meta.
- **build.gradle** Here you can add dependencies.

As well as about these folders:

- **src/main/java** Here’s your code stored.
- **src/main/resources** Here’s the mod’s assets, meta, confines, etc
  are located. Right now there’s very few files:
  - **fabric.mod.json** All kinds of information about your mod are
    here. You can read more about that file
    [here](../Documentation/fabric_mod_json.md).
  - **modid.mixin.json** You’re not going to use it. Just ignore it.

## Adding an Item

## Adding a Block

## Adding Recipes

## Adding Loot Tables

## Refactoring

## Result Overview


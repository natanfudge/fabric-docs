# Welcome to the Fabric Wiki\!

This is an early work-in-progress wiki dedicated to documenting the
methods, tools and APIs of the Fabric project.

Fabric is a modular modding toolchain targetting Minecraft 1.14 and
above, including snapshots.

**Make sure to check out [our community
spaces](http://fabricmc.net/discuss), and read our [Community
Rules](rules.md)\!**

- [Frequently Asked Questions (from users)](FAQ/user.md)
- [Frequently Asked Questions (expert mode)](FAQ/expert.md) - a
  supplement to the above with more detailed answers

This wiki is also available in the following languages:

|                                               |
| --------------------------------------------- |
| ![](images/france_flag.png) [Français](French/accueil.md) |

-----

## Installing and Using Fabric

If you're willing to use Fabric as a player, server administrator or
modpack developers, these articles are for you:

### Tutorials

- [Installing Fabric](Setup/install.md)
- [Installing Fabric using the Minecraft Launcher](Setup/install_with_minecraft_launcher.md)
- [Installing Fabric using MultiMC](Setup/install_with_multimc.md)
- [Installing a Minecraft/Fabric Server](Setup/installing_minecraft_fabric_server.md)
- [Adding mods](Setup/adding_mods.md)
- [Updating an existing MultiMC Instance](Setup/updating_fabric_using_multimc_launcher.md)
- [Install/Verify Java](Setup/install_java.md)

-----

## Modpack and Launcher Developers

**Please keep in mind that we recommend using MultiMC and MCUpdater for
modpack play and distribution.** However, that doesn't stop us from
trying to support other solutions\!

### Tutorials

- [Using MCUpdater for Fabric modpacks](Modpacks/mcupdater_modpacks.md)
  (covers usage and creation)
- [Publishing Fabric modpacks on Technic](Modpacks/technic_modpacks.md)
- [Publishing Fabric modpacks on ATLauncher](Modpacks/atlauncher_modpacks.md)

### Documentation

- [Modpack-related endpoints](Documentation/modpack_related_endpoints.md)

-----

## Mod Developers

If you'd like to start developing with Fabric, here are some articles
which might interest you.

### Tutorials

#### Setup

- [Setting up a development environment](Tutorials/setup.md)
- [Enabling Log4j debug messages](https://wiki.vg/Debugging)
- [Publishing mods with CurseGradle](Tutorials/cursegradle.md)

#### Basics

- Conventions and Terminology
  - [Basic conventions and terminology](Tutorials/Conventions And Terminology/terms.md)
  - [Server and client side terminology](Tutorials/side.md)
- Development Tools
  - [Third-party library mods](Documentation/libraries.md)
  - [Applying changes without restarting   Minecraft](Tutorials/applychanges.md)
- [Creating a lang file](Tutorials/lang.md)
- [Using mappings](Tutorials/mappings.md)
- [Adding a crafting recipe](Tutorials/recipes.md)

#### Items and Item Groups

- [Adding an item](Tutorials/items.md)
- [Adding an item group](Tutorials/itemgroup.md)
- [Adding a tooltip](Tutorials/tooltip.md)

#### Blocks and Block Entities

- [Adding a block](Tutorials/blocks.md)
- [Giving a block state](Tutorials/blockstate.md)
- [Adding a BlockEntity](Tutorials/blockentity.md)
- [Storing items in a block as an inventory](Tutorials/inventory.md)
- [Make a block change color depending on biome](Tutorials/biomecoloring.md)
- [Manipulating a Block's appearance](Tutorials/blockappearance.md)
- [Rendering blocks and items dynamically using block entity renderers](Tutorials/blockentityrenderers.md)

#### Fluids

- [Creating a fluid](Tutorials/fluids.md)

#### Entities

- [Adding an Entity](Tutorials/entity.md)

#### World Generation

- [Generating custom ores in the world](Tutorials/ores.md)
- [Generating structures](Tutorials/structures.md)
- [Adding a Biome](Tutorials/biome.md)
- [Dimension Concepts](Tutorials/dimensionconcepts.md)
- [Creating a basic dimension](Tutorials/dimension.md)

#### Miscellaneous

- [Adding Armor](Tutorials/armor.md)
- [Adding custom enchantments](Tutorials/enchantments.md)
- [Custom Keybindings](Tutorials/keybinds.md)
- [Creating custom events](Tutorials/events.md)
- [Adding items to existing loot tables](Tutorials/adding_to_loot_tables.md)
- [Creating Commands](Tutorials/commands.md)
- [Playing Sounds](Tutorials/sounds.md)
- [List of useful tutorials](Tutorials/list_of_useful_gists.md)

#### Advanced

- [Modding Tips](Tutorials/modding_tips.md)
- [Updating from Loader 0.3.x to 0.4.x](Tutorials/loader04x.md)
- [Updating Yarn mappings in a Java codebase](Tutorials/migratemappings.md)

#### Documentation

- [fabric.mod.json](Documentation/fabric_mod_json.md)
- [Mixin library wiki](http://github.com/SpongePowered/Mixin/wiki)
- [Rendering in Fabric (DRAFT)](Documentation/rendering.md)
- [Fabric build pipelines (DRAFT)](Documentation/build_pipelines.md)
- [Fabric Loader](Documentation/fabric_loader.md)
- [Fabric Loom](Documentation/fabric_loom.md)

### Examples

- [Mod environment template](https://github.com/FabricMC/fabric-example-mod)
- [Fabric API test mods](https://github.com/FabricMC/fabric/tree/master/fabric-testmods/java/net/fabricmc/fabric)
  - not a direct tutorial, but may come in handy

-----

## Fabric Сontributors

If you'd like to contribute to Fabric, you might be interested in these
links:

- [The Fabric Feature Procedure](Tutorials/feature_procedure.md) - or how
  to get your feature accepted (or not\!)
- [Fabric on GitHub](https://github.com/FabricMC)
- [Game mappings repository + contribution documentation](https://github.com/FabricMC/yarn)

-----

## Extremely Strange People

- [Updating Yarn to a new Minecraft version](Tutorials/updating_yarn.md)
  - for prospective Yarn developers, **not** regular users or
    modders\!

-----

## Wiki Meta

- [Wiki Meta](wiki_meta.md) - Starting point for contributing to the wiki
- [Wiki Agenda](wiki/agenda.md) - See what is on the current agenda, and
  what other contributors are currently working on.


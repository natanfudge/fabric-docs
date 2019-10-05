# Welcome to the Fabric Wiki\!

This is an early work-in-progress wiki dedicated to documenting the
methods, tools and APIs of the Fabric project.

Fabric is a modular modding toolchain targetting Minecraft 1.14 and
above, including snapshots.

**Make sure to check out [our community
spaces](http://fabricmc.net/discuss), and read our [Community
Rules](rules.md)\!**

- [Frequently Asked Questions (from users)](faq/user.md)
- [Frequently Asked Questions (expert mode)](faq/expert.md) - a
  supplement to the above with more detailed answers

This wiki is also available in the following languages:

|                                               |
| --------------------------------------------- |
| ![](images/france_flag.png) [Français](fr/accueil.md) |

-----

## Installing and Using Fabric

If you're willing to use Fabric as a player, server administrator or
modpack developers, these articles are for you:

### Tutorials

- [Installing Fabric](install.md)
- [Installing Fabric using the Minecraft
  Launcher](tutorial/install_with_minecraft_launcher.md)
- [Installing Fabric using MultiMC](tutorial/install_with_multimc.md)
- [Installing a Minecraft/Fabric
  Server](tutorial/installing_minecraft_fabric_server.md)
- [Adding mods](tutorial/adding_mods.md)
- [Updating an existing MultiMC
  Instance](tutorial/updating_fabric_using_multimc_launcher.md)
- [Install/Verify Java](tutorial/install_java.md)

-----

## Modpack and Launcher Developers

**Please keep in mind that we recommend using MultiMC and MCUpdater for
modpack play and distribution.** However, that doesn't stop us from
trying to support other solutions\!

### Tutorials

- [Using MCUpdater for Fabric modpacks](tutorial/mcupdater_modpacks.md)
  (covers usage and creation)
- [Publishing Fabric modpacks on Technic](tutorial/technic_modpacks.md)
- [Publishing Fabric modpacks on
  ATLauncher](tutorial/atlauncher_modpacks.md)

### Documentation

- [Modpack-related
  endpoints](documentation/modpack_related_endpoints.md)

-----

## Mod Developers

If you'd like to start developing with Fabric, here are some articles
which might interest you.

### Tutorials

#### Setup

- [Setting up a development environment](tutorial/setup.md)
- [Enabling Log4j debug messages](https://wiki.vg/Debugging)
- [Publishing mods with CurseGradle](tutorial/cursegradle.md)

#### Basics

- Conventions and Terminology
  - [Basic conventions and terminology](tutorial/terms.md)
  - [Server and client side terminology](tutorial/side.md)
- Development Tools
  - [Third-party library mods](documentation/libraries.md)
  - [Applying changes without restarting
    Minecraft](tutorial/applychanges.md)
- [Creating a lang file](tutorial/lang.md)
- [Using mappings](tutorial/mappings.md)
- [Adding a crafting recipe](tutorial/recipes.md)

#### Items and Item Groups

- [Adding an item](tutorial/items.md)
- [Adding an item group](tutorial/itemgroup.md)
- [Adding a tooltip](tutorial/tooltip.md)

#### Blocks and Block Entities

- [Adding a block](tutorial/blocks.md)
- [Giving a block state](tutorial/blockstate.md)
- [Adding a BlockEntity](tutorial/blockentity.md)
- [Storing items in a block as an inventory](tutorial/inventory.md)
- [Make a block change color depending on
  biome](tutorial/biomecoloring.md)
- [Manipulating a Block's appearance](tutorial/blockappearance.md)
- [Rendering blocks and items dynamically using block entity
  renderers](tutorial/blockentityrenderers.md)

#### Fluids

- [Creating a fluid](tutorial/fluids.md)

#### Entities

- [Adding an Entity](tutorial/entity.md)

#### World Generation

- [Generating custom ores in the world](tutorial/ores.md)
- [Generating structures](tutorial/structures.md)
- [Adding a Biome](tutorial/biome.md)
- [Dimension Concepts](tutorial/dimensionconcepts.md)
- [Creating a basic dimension](tutorial/dimension.md)

#### Miscellaneous

- [Adding Armor](tutorial/armor.md)
- [Adding custom enchantments](tutorial/enchantments.md)
- [Custom Keybindings](tutorial/keybinds.md)
- [Creating custom events](tutorial/events.md)
- [Adding items to existing loot
  tables](tutorial/adding_to_loot_tables.md)
- [Creating Commands](tutorial/commands.md)
- [Playing Sounds](tutorial/sounds.md)
- [List of useful tutorials](tutorial/list_of_useful_gists.md)

#### Advanced

- [Modding Tips](tutorial/modding_tips.md)
- [Updating from Loader 0.3.x to 0.4.x](tutorial/loader04x.md)
- [Updating Yarn mappings in a Java
  codebase](tutorial/migratemappings.md)

#### Documentation

- [fabric.mod.json](documentation/fabric_mod_json.md)
- [Mixin library wiki](http://github.com/SpongePowered/Mixin/wiki)
- [Rendering in Fabric (DRAFT)](documentation/rendering.md)
- [Fabric build pipelines (DRAFT)](documentation/build_pipelines.md)
- [Fabric Loader](documentation/fabric_loader.md)
- [Fabric Loom](documentation/fabric_loom.md)

### Examples

- [Mod environment
  template](https://github.com/FabricMC/fabric-example-mod)
- [Fabric API test
  mods](https://github.com/FabricMC/fabric/tree/master/fabric-testmods/java/net/fabricmc/fabric)
  - not a direct tutorial, but may come in handy

-----

## Fabric Сontributors

If you'd like to contribute to Fabric, you might be interested in these
links:

- [The Fabric Feature Procedure](tutorial/feature_procedure.md) - or how
  to get your feature accepted (or not\!)
- [Fabric on GitHub](https://github.com/FabricMC)
- [Game mappings repository + contribution
  documentation](https://github.com/FabricMC/yarn)

-----

## Extremely Strange People

- [Updating Yarn to a new Minecraft version](tutorial/updating_yarn.md)
  - for prospective Yarn developers, **not** regular users or
    modders\!

-----

## Wiki Meta

- [Wiki Meta](wiki_meta.md) - Starting point for contributing to the wiki
- [Wiki Agenda](wiki/agenda.md) - See what is on the current agenda, and
  what other contributors are currently working on.


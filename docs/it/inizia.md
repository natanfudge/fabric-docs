# NOT FINISHED! NON ANCORA FINITO!

# Benventuto sulla wiki di Fabric!

Questa Ã¨ una wiki in fase di sviluppo dedicata alla documentazione dei
metodi, degli strumenti e delle API di Fabric

Fabric is a modular modding toolchain targetting Minecraft 1.14 and
above, including snapshots.

Fabric Ã¨ una toolchain utilizzabile per la creazione di mod a partire
dalla versione 1.14 di minecraft in su (snapshot inclusi)

**Assicurati di vedere [la nostra
community](http://fabricmc.net/discuss) e di leggere le nostre
[regole](../rules.md)!**

- [FAQ (dagli utenti)](../FAQ/user.md)
- [FAQ (per gli esperti)](../FAQ/expert.md) - uguale al precedente ma piÃ¹
  dettagliato

Questa wiki Ã¨ disponibile anche nelle seguenti lingue:

- [FranÃ§ais (Francese)](../French/accueil.md)
- [ç®€ä½“ä¸­æ–‡ (Cinese semplificato)](../zh_cn/start.md)
- [Ð ÑƒÑ�Ñ�ÐºÐ¸Ð¹ (Russo)](../ru/start.md)
- [English (Inglese)](../README.md)

------------------------------------------------------------------------

## Installare e utilizzare Fabric

Se hai intenzione di usare Fabric come player, amministratore di un
server o sviluppatore di modpack, questi articoli fanno al caso tuo:

### Tutorial

- [Installare Fabric](../Setup/install.md)
- [Installare Fabric usando il Minecraft Launcher (Windows)](../player/tutorials/install_mcl/windows.md)
- [Installare Fabric usando MultiMC (Windows)](../player/tutorials/install_multimc/windows.md)
- [Installare Fabric usando MultiMC (macOS)](../player/tutorials/install_multimc/mac.md)
- [Installare un server Minecraft/Fabric](../Setup/installing_minecraft_fabric_server.md)
- [Agiungere mod](../Setup/adding_mods.md)
- [Aggiornare un istanza di MultiMC](../Setup/updating_fabric_using_multimc_launcher.md)
- [Installare Java (Windows)](../player/tutorials/java/windows.md)
- [Installare Java (macOS)](../player/tutorials/java/mac.md)
- [Installare Java (GNU/Linux)](../player/tutorials/java/linux.md)

------------------------------------------------------------------------

## Sviluppatori di launcher e modpack

**Ricorda che noi raccomandiamo l'utilizzo di MultiMC e MCUpdater per
giocare o distribure modpack.** Comunque sia, ciÃ  non ci ferma dal
supportare altre soluzioni!

### Tutorial

- [Usare MCUpdater per i modpack Fabric](../Modpacks/mcupdater_modpacks.md)
  (covers usage and creation)
- [Pubblicare modpack Fabric sul TechnicLauncher](../Modpacks/technic_modpacks.md)
- [Pubblicare modpack Fabric sull'ATLauncher](../Modpacks/atlauncher_modpacks.md)

### Documentazione

- [Endpoint relativi ai modpack](../Documentation/modpack_related_endpoints.md)

------------------------------------------------------------------------

## Sviluppatori di mod

Se ti interessa iniziare a sviluppare mod con Fabric, questi articoli
fanno al caso tuo:

### Tutorial

#### Configurazione

- [Configurare un ambiente di sviluppo](../Modding-Tutorials/setup.md)
- [Abilitare i messaggi di debug Log4j](https://wiki.vg/Debugging)
- [Pubblicare mod con CurseGradle](../Modding-Tutorials/Gradle/cursegradle.md)

#### Basi

- [Introduzione allo sviluppo di mod con Fabric](../Modding-Tutorials/introduction.md)
- Convenzioni e Terminologia
  - [Convenzioni e Terminologia di base](../Modding-Tutorials/Conventions-And-Terminology/terms.md)
  - [Terminologia del Client e del Server](../Modding-Tutorials/Conventions-And-Terminology/side.md)
- Registri
  - [Introduzione ai registri](../Modding-Tutorials/registry.md)
  - [Registri Standard](../Modding-Tutorials/registry_types.md)
- Tool di sviluppo
  - [Librerie di terze parti](../Modding-Tutorials/Development-Tools/libraries.md)
  - [Applicare le modifiche senza riavviare   Minecraft](../Modding-Tutorials/Development-Tools/applychanges.md)
- [Creare un file lang](../Modding-Tutorials/Miscellaneous/lang.md)
- [Usare le Mappature](../Documentation/mappings.md)

#### Item

- [Documentazione degli item](../Modding-Tutorials/items_docs.md)
- [Esempio pratico: aggiungere un item](../Modding-Tutorials/Items/item.md)
  - [Creare ItemGroup per i tuoi item](../Modding-Tutorials/Items/itemgroup.md)
  - [Aggiungere un tooltip al tuo item](../Modding-Tutorials/Items/tooltip.md)
- [Aggiungere una Recipe di Crafting](../Modding-Tutorials/Crafting-Recipes/basic.md)
- [Aggiungere armature](../Modding-Tutorials/Miscellaneous/armor.md)
- [Aggiungere strumenti](../Modding-Tutorials/tools.md)
- [Aggiungere incantesimi](../Modding-Tutorials/Miscellaneous/enchantments.md)

#### Blocchi e Block Entity

- [Aggiungere blocchi](../Modding-Tutorials/Blocks-and-Block-Entities/block.md)
- [Aggiungere un BlockState ad un blocco](../Modding-Tutorials/Blocks-and-Block-Entities/blockstate.md)
- [Creare blocchi direzionali](../Modding-Tutorials/directionalblock.md)
- [Aggiungere un BlockEntity](../Modding-Tutorials/Blocks-and-Block-Entities/blockentity.md)
- [Immagazzinare item in un blocco come in un inventario](../Modding-Tutorials/Blocks-and-Block-Entities/inventory.md)
- [Cambiare il colore di un blocco o di un item dinamicamente](../Modding-Tutorials/Blocks-and-Block-Entities/colorprovider.md)
- [Manipolare l'aspetto di un blocco](../Modding-Tutorials/Blocks-and-Block-Entities/blockappearance.md)
- [Render dinamico di item e blocchi](../Modding-Tutorials/dynamic_block_rendering.md)
  - [Render dinamico di item e blocchi utilizzando un modello   personalizzato](../Modding-Tutorials/custom_model.md)
  - [Render dinamico di item e blocchi utilizzando un i Block Entity   Render](../Modding-Tutorials/Blocks-and-Block-Entities/blockentityrenderer.md)
- [Esempio pratico: Creare un blocco contenitore](../Modding-Tutorials/screenhandler.md)
  - [Sincronizzare dati personalizzati con gli ScreenHandler   estesi](../Modding-Tutorials/extendedscreenhandler.md)
  - [Sincronizzare interi con un   PropertyDelegate](../Modding-Tutorials/propertydelegates.md)

#### Fluids

- [Creating a Fluid](../Modding-Tutorials/Fluids/fluid.md)

#### Entities

- [Adding an Entity](../Modding-Tutorials/Entities/entity.md)

#### World Generation

- [Generating Custom Ores in the World](../Modding-Tutorials/World-Generation/ores.md)
- [Generating Features](../Modding-Tutorials/World-Generation/features.md)
- [Generating StructureFeatures](../Modding-Tutorials/World-Generation/structures.md)
- [Adding your Structure to /locate](../Modding-Tutorials/World-Generation/locate.md)
- [Adding a Biome](../Modding-Tutorials/biomes.md)
- [Dimension Concepts](../Modding-Tutorials/World-Generation/dimensionconcepts.md)
- [Creating a Basic Dimension](../Modding-Tutorials/dimensions.md)

#### Miscellaneous

- [Mining Levels](../Modding-Tutorials/mining_levels.md)
- [Raycasting](../Modding-Tutorials/pixel_raycast.md)
- [Custom Keybindings](../Modding-Tutorials/Miscellaneous/keybinds.md)
- [Creating Commands](../Modding-Tutorials/Miscellaneous/commands.md)
- [Playing Sounds](../Modding-Tutorials/Miscellaneous/sounds.md)
- [Networking](../Modding-Tutorials/networking.md)
- [Tag Conventions](../Modding-Tutorials/tags.md)
- [List of Useful Tutorials](../Modding-Tutorials/Miscellaneous/list_of_useful_gists.md)

#### Events

- [Listening to Events (DRAFT)](../Modding-Tutorials/callbacks.md)
- [Creating Custom Events](../Modding-Tutorials/Miscellaneous/events.md)
- [Adding Items to Existing Loot Tables](../Modding-Tutorials/Miscellaneous/adding_to_loot_tables.md)

#### Mixins

- [Introduction](../Modding-Tutorials/mixin_introduction.md)
- [Injects](../Modding-Tutorials/mixin_injects.md)
- [Redirectors](../Modding-Tutorials/mixin_redirectors.md)
  - [Method redirectors](../Modding-Tutorials/mixin_redirectors_methods.md)
- [Hotswapping Mixins](../Modding-Tutorials/Mixins/mixin_hotswaps.md)
- [Exporting Mixin Classes](../Modding-Tutorials/mixin_export.md)

#### Dynamic Data Generation

- [Dynamic Recipe Generation](../Modding-Tutorials/dynamic_recipe_generation.md)
- [Dynamic Model Generation](../Modding-Tutorials/dynamic_model_generation.md)

#### Advanced

- [Modding Tips](../Modding-Tutorials/Advanced/modding_tips.md)
- [Updating from Loader 0.3.x to 0.4.x](../Modding-Tutorials/Advanced/loader04x.md)
- [Updating Yarn mappings in a Java codebase](../Modding-Tutorials/Advanced/migratemappings.md)
- [DataFixers \[WIP](../Modding-Tutorials/DataFixers/datafixer.md)\]
- [Access Wideners](../Modding-Tutorials/accesswideners.md)

#### Tutorials for Minecraft 1.15

- [Using Jigsaws in StructureFeatures](../Modding-Tutorials/1.15/jigsaw)

#### Tutorials for Minecraft 1.14

- [Rendering blocks and items dynamically using block entity renderers](../Modding-Tutorials/1.14/blockentityrenderers)
- [Manipulating a Block's appearance](../Modding-Tutorials/1.14/blockappearance)
- [Adding a Cookie Creeper Entity](../Modding-Tutorials/entity-old.md)

#### Documentation

- [Structure of fabric.mod.json](../Documentation/fabric_mod_json.md)
- [fabric.mod.json specification](../Documentation/fabric_mod_json_spec.md)
- [Entrypoints](../Documentation/entrypoint.md)
- [Mixin library wiki](http://github.com/SpongePowered/Mixin/wiki)
- [Rendering in Fabric (DRAFT)](../Documentation/rendering.md)
- [Fabric build pipelines (DRAFT)](../Documentation/build_pipelines.md)
- [Fabric Loader](../Documentation/fabric_loader.md)
- [Fabric Loom](../Documentation/fabric_loom.md)

### Examples

- [Mod environment template](https://github.com/FabricMC/fabric-example-mod)
- [Fabric API test mods](https://github.com/FabricMC/fabric/tree/master/fabric-testmods/java/net/fabricmc/fabric)
  (Outdated) - not a direct tutorial, but may come in handy

------------------------------------------------------------------------

## Fabric Ð¡ontributors

If you'd like to contribute to Fabric, you might be interested in these
links:

- [The Fabric Feature Procedure](../Modding-Tutorials/Fabric-Contributors/feature_procedure.md) - or how
  to get your feature accepted (or not!)
- [FabLabs](../Modding-Tutorials/fablabs.md) - a testing ground for drafting new
  Fabric features before submitting PRs
- [Fabric on GitHub](https://github.com/FabricMC)
- [Game mappings repository + contribution documentation](https://github.com/FabricMC/yarn)

------------------------------------------------------------------------

## Extremely Strange People

- [Updating Yarn to a new Minecraft version](../Modding-Tutorials/Fabric-Contributors/updating_yarn.md) - for prospective Yarn developers,
  **not** regular users or modders!

------------------------------------------------------------------------

## Wiki Meta

- [Wiki Meta](../wiki_meta.md) - Starting point for contributing to the wiki
- [Wiki Agenda](../wiki/agenda.md) - See what is on the current agenda, and
  what other contributors are currently working on.


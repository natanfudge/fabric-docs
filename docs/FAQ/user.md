# Frequently Asked Questions (from users)

This is the simplified version! For technical answers, see the ["expert
mode" supplement](../FAQ/expert.md).

## General

### What Minecraft versions does Fabric support?

For most cases, the answer is "snapshot 18w43b and above, releases 1.14
and above".

There exist ways to run Fabric with older versions - modded or vanilla,
but they're currently non-trivial - see the expert FAQ for more details.

### Are there any premade Fabric modpacks?

A few modpacks are currently being distributed via MultiMC exports, such
as [All of Fabric](https://github.com/AllOfFabric), and some are also
available on CurseForge. Don't bother with the Launchpacks - they are
generally "proof of concept" and not intended for production gameplay.

### What launchers can I use to play with Fabric mods (on a custom/home-assembled modpack)?

As of 9th May 2020, the following launchers are available for this:

- MultiMC - a guide is available
  [here](../Setup/install_with_multimc.md); newest development builds
  also include an "Install Fabric" button.
- Vanilla - an installer is available on Fabric's download page.
- Twitch (Unofficial) - by adding the
  [Jumploader](https://www.curseforge.com/minecraft/mc-mods/jumploader)
  mod to your modpack.
- GDLauncher - supports Fabric, just choose "Fabric" then select a
  version when creating an instance.
- ATLauncher - downloads are available on Fabric's download page.
- HMCL - supports Fabric download and installation, including sources
  from mirror websites.

**Please keep in mind** that Fabric API is a separate component and must
be downloaded separately - you can find it
[here](https://www.curseforge.com/minecraft/mc-mods/fabric-api).

We recommend MultiMC due to its superior user experience for modded
environments.

### What launchers can I use to share a Fabric modpack?

As of 30th June 2019, the following launchers are available for this:

- MCUpdater - [guide](../Modpacks/mcupdater_modpacks.md)
- Technic (Solder) - [guide](../Modpacks/technic_modpacks.md)
- ATLauncher - [guide](../Modpacks/atlauncher_modpacks.md)
- MultiMC/Vanilla - you can always export a modpack or use a ZIP file!
- GDLauncher - Right click on an instance and click "Export Instance".

Please note that we have no information or ETA regarding support by the
Twitch Launcher.

## Interoperability

### Can Fabric run together with Bukkit/Spigot/Paper?

Not at the moment. This may change by the end of the year, but is
unlikely to be officially supported.

### Can Fabric run together with Forge?

- Fabric does not currently work on top of Forge on modern Minecraft
  versions.
- It is theoretically possible to create a way to do so - that is to
  say, there are no known major technical obstacles in accomplishing
  such.
- There were some experiments and discussions done in this regard (as
  of 30th June), but nothing usable by end users or mod developers.

The development team does not consider Forge interop a high-priority
goal, as our focus in our limited time dedicated to the project is on
supporting the Fabric community and its developers and users.

### Can Fabric run together with OptiFine?

Consider using an Optifine alternative, as unofficial mods such as
OptiFabric may not be compatible with your mods.

[OptiFine alternatives on
Fabric](https://gist.github.com/LambdAurora/1f6a4a99af374ce500f250c6b42e8754)

[The future of Optifabric and recommended
alternatives](https://gist.github.com/modmuss50/deff1658c4550ca8b16cb5d40ceaa468)

### Oh no! I've tried enabling shaders on OptiFine and my world looks all weird!

Fabric's rendering patches, while as minimally invasive as possible,
make some assumptions about the data format used internally by the
vanilla rendering system. Shaders break this assumption, so things go
awry. Workarounds do exist and are implemented by the most recent
versions of said unofficial mods.

**DO NOT** downgrade Fabric API as a workaround for this. It's a bad
idea.

### Can Fabric run together with Sponge?

Not at the moment. Sponge does not yet have a ready API or a working
implementation for 1.14, the Minecraft version that Fabric runs on.

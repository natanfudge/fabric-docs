# Frequently Asked Questions: Expert Mode

This is the "expert/technical" extension of the user FAQ.

## Interoperability

### What's the progress on Bukkit/Spigot/Paper support?

There is some experimental functionality available in Loader's 0.5.0
development branch to run Fabric Loader on top of Paper specifically,
but there are many caveats pertaining to Bukkit API's deficiencies (such
as hardcoding the list of blocks/items in an enum, making it very
difficult to support modded blocks or items). This will probably never
be officially supported and should be treated more as a
curiosity/special-case scenario.

### Can Fabric be made to run together with Sponge?

The best approach here would be to port SpongeCommon and a modified
version of SpongeVanilla as a Fabric mod implementing the Sponge API. As
Fabric is using a fork of SpongePowered Mixin at its core, this is made
somewhat easier - but the mappings differences make up for this, so to
say.

### Why does Fabric API break OptiFine shaders?

The rendering patch Fabric API uses, Indigo, assumes (for performance
and code simplicity reasons) that the vanilla vertex format is kept
intact. Mods generally don't change it, however ShadersMod and similar
mods are a popular exception. As such, Indigo doesn't play nicely with
it as-is.

Solutions for this have been looked into, but not yet functional. In
addition, some of the unofficial mod projects are trying to work on one
themselves. The current workarounds will not work in the presence of
content mods which actually make use of Fabric's rendering API.

## Interoperability (Retro)

### What versions does Fabric actually run on?

In theory, there is nothing stopping you from running Fabric's mod
loader on any version of Minecraft, under any obfuscation layer, all the
way down to c0.0.11a. However, Yarn mappings don't exist for most of
these versions - as such, making mods is made... a tad more complicated.

### Can Fabric run on older Minecraft versions with mods?

Yes\! Generally, all JAR mods (such as old versions of OptiFine, or
Better Than Wolves) should work fine, and allow the same degree of
moddability you'd get with an unmodded version of Minecraft. However,
there are some special notes:

For most pre-1.13 mod loaders, you must enable *compatibility mode* in
Fabric Loader, by adding the JVM flag
`%%-Dfabric.loader.useCompatibilityClassLoader=true%%`.

With regards to running Minecraft Forge:

- 1.6 \~ 1.12.2: While Fabric used to run on top of LaunchWrapper,
  this functionality is currently unmaintained and in need of rework.
- 1.3.1 \~ 1.5.2: Not currently planned.
- 1.2.5 and below: Functional, just like ModLoader.

With regards to running ModLoader: No known problems.

## Philosophy

### Why did you create your own mappings instead of utilizing MCP or Spigot's existing mappings?

With regards to the Mod Coder Pack, MCP:

- MCP's mappings have no clear licensing terms between the old MCP
  license ([as preserved on the Minecraft Gamepedia](https://minecraft.gamepedia.com/Programs_and_editors/Mod_Coder_Pack#License_and_terms_of_use))
  and the new MCPConfig repository. In contrast, Yarn is
  [CC0-licensed](https://github.com/FabricMC/yarn/blob/1.14.4/LICENSE)
  and free to use, modify and redistribute as necessary.
  - While other projects like OptiFine and Sponge utilize MCP,
    redistributing MCP mappings - which is forbidden without
    explicit permission - is necessary to support SRG-obfuscated
    mods, something [only done by   Forge](https://github.com/MinecraftForge/MinecraftForge/blob/1.14.x/LICENSE.txt#L32-L35).
  - Even if we had been granted permission, it is unlikely that we
    would be freely able to extend this permission to other
    developers. This would go against Fabric's core values of
    development freedom and transparency.
- MCP's mappings are not always updated to every non-"release"
  Minecraft version, nor are others generally allowed to do it
  themselves. This would forbid Fabric from updating to snapshots or
  other experimental releases.
  - Additionally, MCP's updating toolchain - as of the last few
    years - relies on proprietary Mojang information, not accessible
    to common users, as well as software without clear licensing
    terms. In contrast, Fabric's updating toolchain relies solely on
    free software tools (Stitch, Matcher, Tiny-Remapper, Enigma),
    and the artifacts of the process - just like the mappings - are
    publicly available.
- MCP's mappings have an updating process we consider insufficiently
  open to code review, in our opinion, utilizing [IRC bot submissions](http://mcpbot.bspk.rs/) over a merge request system.
  (However, this is largely a matter of preference.)

With regards to Spigot:

- Spigot's mappings only cover the server side and are very
  incomplete,
- Spigot's mappings suffer from similar licensing issues as MCP.


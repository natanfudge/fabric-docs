# Fabric build pipelines

Gradle was chosen to build Fabric mods. This document will dive into the
details of the pipeline steps, and the tools used.

## Mod building

Fabric Mods' building depends on fabric loom Gradle plugin, which sets
up the Minecraft dependency for Fabric Mods and properly remap their
binaries to intermediary so that they can run across Minecraft snapshots
as long as the part of vanilla Minecraft they depend on does not change.

The main job of loom: generate a Minecraft dependency from the Minecraft
version and mappings references supplied, and convert the built mod to
intermediary.

## Fabric Toolchain

So Minecraft is available as published by Mojang at
[launchermeta](https://launchermeta.mojang.com/mc/game/version_manifest.json),
how about Mappings? How does Fabric generate or develop mappings?

Fabric has a mapping repository called Yarn. It uses Enigma, a
deobfuscation tool.

To set up Minecraft for Enigma, yarn uses stitch which can generate
intermediary (as to allow Fabric mods to work across snapshots if the
code they depend on did not change) and to merge minecraft client and
server jars (so as to ease mapping/development).

## Tools

### Loom

A Gradle plugin. Sets up the Minecraft dependency for Fabric Mods and
properly remap their binaries to intermediary so that they can run
across Minecraft snapshots as long as the part of vanilla Minecraft they
depend on does not change.

### Yarn

Yarn is an open-source repository that contains all the mappings used to
turn Minecraft obfuscated names into the useful names Fabric mod
developers use.

For more information on how to contribute mappings take a look here:
[Source](https://github.com/FabricMC/yarn)

### Enigma

Enigma is a tool to deobfuscate Java applications, like Minecraft.
Fabric uses a fork of Enigma that has been heavily modified to fix lots
of bug and work more effectively with Yarn files.

The code is not for the faint of heart but can be seen here
[Source](https://github.com/FabricMC/Enigma)

### Stitch

Stitch generates and manages intermediary mappings of APIs across
Minecraft versions. Each intermediary mapping is uploaded to
[FabricMC/intermediary](https://github.com/FabricMC/intermediary/tree/master/mappings)
and is used used as part of Yarn's build process.

### Matcher

Matcher is a project used to update intermediary and yarn when a new
Minecraft version comes out. Not directly involved in Fabric toolchain,
it is still integral as it enables Fabric to swiftly update.

### Tiny-Remapper

The tool utilized by loom to convert yarn-named mod binaries to
intermediary-named usable mod binaries and by fabric loader to convert
obfuscated vanilla Minecraft to intermediary.

### Mercury

[Mercury](https://github.com/CadixDev/Mercury) is a library based on
[Eclipse's JDT Core](https://www.eclipse.org/jdt/core/index.php) that
allows for
[processors](https://github.com/CadixDev/Mercury/blob/2027ef98c6b835eb516bf8c59153a4acd44ee57f/src/main/java/org/cadixdev/mercury/SourceProcessor.java)
to be run against Java codebases. Mercury's prime use is for applying
de-obfuscation mappings to a codebases, and includes a [processor for
doing just
that](https://github.com/CadixDev/Mercury/blob/6379e58e914160b5c9d9f4e822d5269586e2260b/src/main/java/org/cadixdev/mercury/remapper/MercuryRemapper.java).
Mercury's open nature allows for anyone to write a processor and for
multiple to be run (consequtively) in one go - for example there is an
external processor,
[MercuryMixin](https://github.com/CadixDev/MercuryMixin), that is
capable of remapping usage of the Mixin library.

Mercury is utilised by Loom for its [migrateMappings
task](../Modding-Tutorials/Advanced/migratemappings.md), allowing mods to be quickly updated to
newer versions of Yarn without tedious manual work. Since the Tiny
format isn't directly supported by Lorenz, the mapping library used by
Mercury, [lorenz-tiny](https://github.com/FabricMC/lorenz-tiny) exists
to read and write Lorenz mapping sets using the existing
[tiny-mappings-parser](https://github.com/FabricMC/tiny-mappings-parser)
library.

Mercury is maintained and developed by a third party, the [Cadix
Development Team](https://www.cadixdev.org/). Queries and bug reports
should be directed to them.

- Mercury Bug Tracker: <https://github.com/CadixDev/Mercury/issues>
- Cadix Development Team IRC Channel: \#cadix on
  [EsperNet](https://esper.net/)


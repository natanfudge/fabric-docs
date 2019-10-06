# Fabric build pipelines

Gradle was chosen to build Fabric mods. This document will dive into the
details of the pipeline steps, and the tools used.

## gradle build

## Tools

### Loom

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
MineCraft versions. Each intermediary mapping is uploaded to
[FabricMC/intermediary](https://github.com/FabricMC/intermediary/tree/master/mappings)
and is used used as part of Yarn's build process.

### Matcher

### Tiny-Remapper


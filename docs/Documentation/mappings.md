# Mappings

### Introduction

Mappings define the names of classes, fields and methods. In an ordinary
loom environment, [Yarn](https://github.com/FabricMC/yarn) mappings are
used, which provide meaningful names for the Minecraft codebase as
decided by the community.
[Intermediary](https://github.com/FabricMC/intermediary) is also an
essential type of mapping used by Fabric. The need for mappings comes
from the obfuscation of Minecraft releases, which presents multiple
challenges. Remapping is the process of applying mappings to compiled
classes or source files.

### Using mappings

In Loom, the mappings define the names for Minecraft classes, fields and
methods used in your development environment. These names may vary from
one development environment to another depending on the installed
mappings.

Yarn is the default mapping used by Loom. Yarn gradually improves and
receives new releases as contributions are accepted. Mappings in Loom
are specified using the `mappings` dependency configuration in the
buildscript and can be updated by updating the dependency. Minecraft as
well as dependencies included with mod-augmented dependency
configurations like `modCompile` are remapped with the mapping. Classes,
fields and methods that are not mapped in Yarn are given Intermediary
names like `class_1234`, `method_1234` and `field_1234`.

    dependencies {
        [...]
        mappings "net.fabricmc:yarn:${project.yarn_mappings}"
    }

By changing the mappings in your development environment, you can expect
that names of classes, methods and fields in Minecraft and any included
mods have changed, and that your code might have to be updated to
reference the changed names. [This process can be partially
automated](../Modding Tutorials/Advanced/migratemappings.md). You will also have to run
`genSources` to access Minecraft sources with the updated mappings.

Loom's `remapJar` task will produce the primary mod artifact, which is a
built jar using intermediary names. Additionally, if a `sourcesJar` task
is present, `remapSourcesJar` will produce a sources jar using
intermediary names. These jars can be installed as mods or included in a
development environment with the `modCompile` dependency configuration.

- **The '-dev' jar (the `jar` task output) does not use intermediary
  names, and is therefore not useful.** It cannot be installed as a
  mod outside a development environment and will only work in a
  development environment with matching mappings. The regular jar (the
  `remapJar` task output) should be used instead and installed in
  development environments using mod-augmented dependency
  configurations like `modCompile`.
- **Yarn names are only applied in a development environment**.
  Outside a development environment, only intermediary names exist,
  meaning the code will not match exactly what you see and wrote. Loom
  transparently handles this transition for you, but be cautious when
  using reflection.

### Remapping

Remapping is the process of applying mappings to code, transforming from
one set of names to another. Both Java source code and compiled Java
code can be remapped. It involves changing the names of references
according to the mappings, as well as carefully renaming methods to
preserve overrides. It does not change what the code does, although it
will affect names used in reflection.

[Tiny Remapper](https://github.com/FabricMC/tiny-remapper) is a tool
that can remap compiled Java code. It has a command line interface as
well as a programmable interface. Loom uses Tiny Remapper for a number
of tasks, and Fabric Loader uses Tiny Remapper to remap the Minecraft
code to intermediary. Loom is also capable of remapping Java source
code.

### Obfuscation and deobfuscation

Releases of Minecraft Java Edition are obfuscated jar files, which means
they are compiled binaries stripped of any meaningful naming
information, leaving only the bare logic behind. The motivation behind
obfuscation is to prevent reverse engineering and to reduce file sizes.
Java programs like Minecraft are rather simple to decompile, but the
obfuscation is stripping away a lot of information that would be useful
for modding purposes. One might wonder how it is possible to develop for
Minecraft in the first place.

Mappings like Yarn provide meaningful names for development. Using
mappings it is possible to make sense of the Minecraft code and create
mods for it. Mapping can provide names for classes, fields, methods,
parameters, and local variables. It should be obvious these mappings are
not perfect. Mapping the entirety of Minecraft involves a lot guesswork
from multiple contributors. Mappings may be incomplete and sometimes
change as more accurate names are found.

### Intermediary

A property of Minecraft's obfuscation is that it is not always
consistent between Minecraft versions. A class may be called `abc` in
one version of Minecraft, and `abd` in another. The same inconsistency
applies to fields and methods. The inconsistency creates binary
incompatibility between Minecraft versions.

Java code may be compiled for one version of a library and still work
with another, making the two versions of the library binary compatible.
Put simply, binary compatibility is achieved if the library exposes at
least the same classes with the same methods and fields with the same
names. The inconsistency in Minecraft's obfuscation presents a challenge
when using Minecraft as a library for mods because of the lack of binary
compatibility.

Intermediary defines stable names for Minecraft's internals across
Minecraft versions. The purpose of an Intermediary name is that it will
always refer to the same class, field or method. Unlike Yarn names,
Intermediary names are not meaningful and instead follow a numeric
pattern like `class_1234`, `method_1234` and `field_1234`.

Being a stable mapping, Intermediary can make Minecraft binary
compatible across multiple versions (such as snapshot versions)\!
Compatibility is guaranteed only for the parts of the game that are
unchanged between versions. When installed outside a development
environment, Fabric Loader provides an environment with intermediary
names by remapping Minecraft (and the Realms client) before the game is
started. This can be observed by looking at a crash report from a
production environment with Fabric Loader installed, which will contain
intermediary names. Mods, compiled with intermediary names as applied by
Loom, are naturally compatible with this environment.

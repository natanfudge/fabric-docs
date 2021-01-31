# Fabric Loader

Fabric Loader is Fabric's lightweight mod loader. It provides the
necessary tools to make Minecraft modifiable without depending on a
specific version of the game. Game specific (and game version specific)
hooks belong in Fabric API. It is possible to adapt Fabric Loader for
many Java applications (for instance games like Slay the Spire and
Starmade).

Fabric Loader has services to allow mods to have some code executed
during initialization, to transform classes, declare and provide mod
dependencies, all in a number of different environments.

## Features

### Mods

A mod is a jar with a [fabric.mod.json](../Documentation/fabric_mod_json.md)
mod metadata file in its root declaring how it should be loaded. It
primarily declares a mod ID and version as well as
[entrypoints](../Documentation/entrypoint.md) and mixin configurations. The
mod ID identifies the mod so that any mod with the same ID is considered
to be the same mod. Only one version of a mod may be loaded at a time. A
mod may declare other mods that it depends on or conflicts with. Fabric
Loader will attempt to satisfy dependencies and load the appropriate
versions of mods, or fail to launch otherwise.

Fabric Loader makes all mods equally capable of modifying the game. As
an example, anything Fabric API does can be done by any other mod.

Mods are loaded both from the classpath and from the mods directory.
They are expected to match the mappings in the current environment,
meaning Fabric Loader will not remap any mods.

### Nested JARs

Nested JARs allow a mod to provide its own dependencies, so Fabric
Loader can pick the best version matching the dependencies instead of
requiring separate installation of dependencies. They also allow clean
packaging of submodules, so each module can be used separately. Non-mod
libraries can be repackaged as mods for nested JAR usage. A mod may
bundle a number of other mods within its JAR. A nested JAR must itself
also be a mod, which again can have nested JARs. Fabric Loader will load
nested JARs while attempting to satisfy dependency constraints.

Nested JARs are not extracted, they are instead loaded in in-memory file
system using jimfs. See the
[guidelines](https://fabricmc.net/wiki/tutorial:loader04x#nested_jars)
for how to use nested JARs effectively. Nested JARs must be declared by
their paths relative to the containing JAR's root.

### Entrypoints

Fabric Loader has an [entrypoint](../Documentation/entrypoint.md) system,
which is used by mods to expose parts of the code for usage by Fabric
Loader or other mods. Fabric Loader uses it for mod initialization.
Initializers are loaded and called early during the game's
initialization which allows a mod to run some code to make its
modifications. These entrypoints are typically used to bootstrap mods by
registering registry objects, event listeners and other callbacks for
doing things later.

### Mixin

Mixin allows mods to transform Minecraft classes and even mod classes,
and is the only method of class transformation that Fabric Loader
officially supports. A mod can declare its own mixin configuration which
enables the use of Mixin.

Mixin was not specifically made for Fabric, so Fabric Loader uses a
slightly modified version of Mixin. However, the documentation of the
upstream version is still mostly valid. The modifications are mostly
related to making it work without LegacyLauncher/LaunchWrapper.

### Mappings

Fabric Loader provides an API to determine names of classes, fields and
methods with respect to the different environments that mods may be
loaded in. This can be used to support reflection in any environment
provided Fabric Loader has access to mappings to resolve the name.

## Fabric Loader internals

### Deobfuscation

When launched in a non-development environment, Fabric Loader will
[remap](../Documentation/mappings.md) the Minecraft jar and realms client jar to
intermediary names. Mods are expected to be mapped to intermediary,
which will be compatible with this environment. The remapped jars are
cached and saved in
`${gameDir}/.fabric/remappedJars/${minecraftVersion}` for re-use across
launches.

### Class loading and transformation

Fabric Loader depends on a custom class loader to transform some classes
at runtime. Classes belonging to a mod or Minecraft are loaded with a
class loader that applies transformations to classes before they are
loaded. Other classes, those belonging to other libraries, cannot be
transformed. With Knot, these classes are delegated to the default
classloader for isolation and performance.

Fabric Loader will perform side stripping on mod classes and Minecraft
classes depending on the physical side that is launched. This involves
completely removing classes, methods and fields annotated with
`@Environment` annotations where the environment does not match. It also
involves removing interface implementations on classes annotated with
`@EnvironmentInterface` where the environment does not match. On
Minecraft classes, this is used to simulate which classes and members
that are available in the targeted runtime development environment. The
annotation can be applied to mod classes to avoid class loading issues.

Package access hacks might be applied to Minecraft classes depending on
the mappings in the current environment. With official (obfuscated)
names and intermediary names, most classes are placed in the same
package. However, Yarn mappings place classes in various packages which
sometimes creates illegal access violations due to the access rules of
protected and package-private members. Therefore, in a development
environment where such access issues are expected to exist, Minecraft
classes are transformed so that package-private and protected members
are made public. Outside a development environment we know that the
package structure is flat, so the package access hack is not needed.
Note that this transformation is applied at runtime, which means it is
not visible in the source.

### Launchers

A launcher (not to be confused with the game launcher) is something
provides a method to use Fabric Loader in a Java process. A launcher
must provide a few features to support Fabric Loader's functionality
such as class transformation and dynamic class loading. Knot and
LegacyLauncher/LaunchWrapper are the current supported launchers.

Knot is the default launcher included in Fabric Loader, designed
specifically for Fabric Loader's features with support for modern
versions of Java. Knot has the main classes
`net.fabricmc.loader.launch.knot.KnotClient` and
`net.fabricmc.loader.launch.knot.KnotServer` for clients and servers
respectively.

When launching a server using Knot in a production environment, the
`net.fabricmc.loader.launch.server.FabricServerLauncher` main class must
be used, which is a main class that wraps the launch of KnotServer. It
can be configured with the `fabric-server-launcher.properties` placed in
the current working directory. The file has one property, `serverJar`,
whose value is 'server.jar' by default, which is used to configure the
path to the minecraft server jar.

Fabric Loader can also be launched with LegacyLauncher/LaunchWrapper
using the tweakers `net.fabricmc.loader.launch.FabricClientTweaker` and
`net.fabricmc.loader.launch.FabricServerTweaker` for clients and servers
respectively. However, LegacyLauncher/LaunchWrapper support is currently
outdated.

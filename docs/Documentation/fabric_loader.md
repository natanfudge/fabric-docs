# Fabric Loader

Fabric Loader is Fabric's lightweight mod loader. It provides the
necessary tools to make Minecraft modifiable without depending on a
specific version of the game. Game specific (and game version specific)
hooks belong in Fabric API. It is possible to adapt Fabric Loader for
many Java applications.

Fabric Loader has services to allow mods to have some code executed
during initialization, to transform classes, declare and provide mod
dependencies, all in a number of different environments.

### Mods

A mod is a jar with a [fabric.mod.json](../Documentation/fabric_mod_json.md)
mod metadata file in its root declaring how it should be loaded. It
primarily declares a mod ID and version as well entrypoints and mixin
configurations. The mod ID identifies the mod so that any mod with the
same ID is considered to be the same mod. Only one version of a mod may
be loaded at a time. A mod may declare other mods that it depends on or
conflicts with. Fabric Loader will attempt to satisfy dependencies and
load the appropriate versions of mods, or fail to launch otherwise.

Mods are loaded both from the classpath and from the mods directory.
They are expected to match the mappings in the current environment,
meaning Fabric Loader will not remap any mods.

Fabric Loader makes all mods equally capable of modifying the game. As
an example, anything Fabric API does can be done by any other mod.

### Nested JARs

A mod may bundle a number of other mods within its JAR. See the
[guidelines](https://fabricmc.net/wiki/tutorial:loader04x#nested_jars)
for how to use nested JARs effectively. Nested JARs must be declared by
their paths relative to the containing JAR's root. A nested JAR must
itself also be a mod, which again can have nested JARs. Fabric Loader
will load nested JARs while attempting to satisfy dependency
constraints.

Nested JARs allow a mod to provide its own dependencies, so Fabric
Loader can pick the best version matching the dependencies instead of
requiring separate installation of dependencies. They also allow clean
packaging of submodules, so each module can be used separately. Non-mod
libraries can be repackaged as mods for nested JAR usage.

When loaded, nested JARs are loaded into an in-memory file system using
jimfs, then added to the classpath.

### Entrypoints

Entrypoints are a way for mods to expose code objects such as classes,
fields and methods for usage by Fabric Loader or other mods. Fabric
Loader uses entrypoints for mod initialization, and other mods can
specify their own entrypoint types for mod integrations. Entrypoints are
loaded by language adapters, which will attempt to produce a Java object
of a provided type using the name of the code object.

An entrypoint is only loaded when entrypoints of the entrypoint's type
are requested, which makes an entrypoint an excellent tool for optional
mod integrations. A mod may support a specific entrypoint type by
declaring that other mods should provide entrypoints under a specified
name using a class or interface that the mod provides in its API. The
entrypoint will only be loaded when the mod requests it, avoiding any
issues with loading classes that may not be present when the mod is not
installed.

Fabric Loader has three built-in entrypoint types for mod initialization
in relation to physical sides (see [side](../Tutorials/side.md)). These
entrypoints are executed about as early as possible during the game's
initialization, which means not all things are initialized or ready for
modification. These entrypoints are typically used to bootstrap mods by
registering registry objects, event listeners and other callbacks for
doing things later.

- **main**: The first entrypoint to run. Expects the type
  `ModInitializer` and will call `onInitialize`.
- **client**: Will run after main only in a physical client. Expects
  the type `ClientModInitializer` and will call `onInitializeClient`.
- **server**: Will run after main only in a physical server. Expects
  the type `DedicatedServerModInitializer` and will call
  `onInitializeServer`.

The default language adapter is designed for Java, and supports the
following types of code objects:

- **Class**: `net.fabricmc.example.ExampleMod` refers to the class by
  this name. The class must have a public constructor without
  arguments. The class must implement or extend the expected type. The
  return value is an instance of the class.
- **Method**: `net.fabricmc.example.ExampleMod::method` refers to a
  public method in that class by the name 'method'. If the method is
  nonstatic, an instance of the class is constructed for the method to
  be called on, meaning the class must also have a public constructor
  without arguments. Methods can only be used for interface types. The
  method must take the same arguments and have the same return type as
  the abstract method(s) in the interface. The return value is a proxy
  implementation of the interface, which will implement abstract
  methods by delegating to the method.
- **Field**: `net.fabricmc.example.ExampleMod::field` refers to the
  field in that class by the name 'field'. The field must be static
  and public. The the field type must implement or extend the expected
  type. The return value is the value of the field.

References to class members must be unambiguous, meaning the class must
contain one and only one member with the targeted name. In case of
ambiguity, the entrypoint will fail to resolve.

Language adapters for other languages can be implemented by mods.
[fabric-language-kotlin](https://github.com/FabricMC/fabric-language-kotlin)
provides a language adapter for Kotlin.

### Mixin

Mixin allows mods to transform Minecraft classes and even mod classes,
and is the only method of class transformation that Fabric Loader
allows. A mod can declare its own mixin configuration which enables the
use of Mixin. Fabric Loader uses a slightly modified version of Mixin,
but the documentation of the unmodified version is still mostly valid.
The modifications are mostly related to making it work without
LegacyLauncher/LaunchWrapper.

### Deobfuscation

When launched in a non-development environment, Fabric Loader will
[remap](../Tutorials/mappings.md) the Minecraft jar and realms client jar to
intermediary names. Mods are expected to be mapped to intermediary,
which will be compatible with this environment. The remapped jars are
cached and saved in
`${gameDir}/.fabric/remappedJars/${minecraftVersion}` for re-use across
launches.

### Mappings

Fabric Loader provides an API to determine names of classes, fields and
methods with respect to the different environments that mods may be
loaded in. This can be used to support reflection in any environment
provided Fabric Loader has access to mappings to resolve the name.

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

A launcher is something provides a method to use Fabric Loader in a Java
process. A launcher must provide a few features to support Fabric
Loader's functionality such as class transformation and dynamic class
loading. Knot and LegacyLauncher/LaunchWrapper are the current supported
launchers.

Knot is the default launcher included in Fabric Loader designed
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
respectively.

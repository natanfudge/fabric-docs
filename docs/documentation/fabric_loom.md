# Fabric Loom

Fabric Loom, or just Loom for short, is a [Gradle](https://gradle.org/) plugin for development of mods in the Fabric ecosystem. Loom provides utilities to install Minecraft and mods in a development environment so that you can link against them with respect to Minecraft obfuscation and its differences between distributions and versions. It also provides run configurations for use with Fabric Loader, Mixin compile processing and utilities for Fabric Loader's jar-in-jar system.

## Dependency configurations

* `minecraft`: Defines the version of Minecraft to be used in the

  development environment.

* `mappings`: Defines the mappings to be used in the development

  environment.

* `modCompile`, `modImplementation`, `modApi` and `modRuntime`:

  Augmented variants of `compile`, `implementation`, `api` and

  `runtime` for mod dependencies. Will be remapped to match the

  mappings in the development environment and has any nested JARs

  removed. Nested JARs can optionally be extracted and remapped.

* `include`: Declares a dependency that should be included as a

  jar-in-jar in the `remapJar` output. This dependency configuration

  is not transitive. For non-mod dependencies, Loom will generate a

  mod JAR with a fabric.mod.json using the name as the mod ID and the

  same version.

## Default tasks

* `cleanLoomBinaries`: For the configured version of Minecraft and the

  configured mappings, deletes the merged Minecraft jar, the

  intermediary Minecraft jar and the mapped Minecraft jar from the

  user cache.

* `cleanLoomMappings`: For the configured version of Minecraft and the

  configured mappings, deletes the mappings, the intermediary

  Minecraft jar and the mapped Minecraft jar from the user cache. Also

  clears the root project build cache.

* `migrateMappings`: Migrates the current source to the specified

  mappings. See [migratemappings](../modding-tutorials/advanced/migratemappings.md).

* `remapJar`: Produces a jar containing the remapped output of the

  `jar` task. Also appends any included mods for jar-in-jar.

* `genSources`: Delegates to two tasks.
  * `genSourcesDecompile` decompiles the mapped Minecraft jar using

    FernFlower to create a sources jar and additionally generates a

    linemap.

  * `genSourcesRemapLineNumbers` then applies the generated linemap

    to produce a linemapped jar which re-aligns line numbers in the

    binary and source jars. The linemapped jar replaces the mapped

    jar.
* `downloadAssets`: Downloads the asset index and asset objects for

  the configured version of Minecraft into the user cache.

* `genIdeaWorkspace`: Depends on `idea` and `genSources`. Installs run

  configurations in the IntelliJ project of the root project and

  creates the run directory if it does not already exist.

* `genEclipseRuns`: Depends on `genSources`. Installs Eclipse run

  configurations and creates the run directory if it does not already

  exist.

* `vscode`: Depends on `genSources`. Generates or overwrites a Visual

  Studio Code `launch.json` file with launch configurations in the

  `.vscode` directory and creates the run directory if it does not

  already exist.

* `remapSourcesJar`: Only exists if an AbstractArchiveTask

  `sourcesJar` exists. Remaps the output of the `sourcesJar` task in

  place.

* `runClient`: A JavaExec task to launch Fabric Loader as a Minecraft

  client.

* `runServer`: A JavaExec task to launch Fabric Loader as a Minecraft

  dedicated server.

## Default configuration

* Applies the following plugins: `java`, `eclipse` and `idea`.
* Adds the following Maven repositories: Fabric

  [https://maven.fabricmc.net/](https://maven.fabricmc.net/), Mojang

  [https://libraries.minecraft.net/](https://libraries.minecraft.net/), Maven Central and JCenter.

* Configures the `idea` extension to exclude directories `.gradle`,

  `build`, `.idea` and `out`, to download javadocs sources and to

  inherit output directories.

* Configures the `idea` task to be finalized by the `genIdeaWorkspace`

  task.

* Configures the `eclipse` task to be finalized by the

  `genEclipseRuns` task.

* If an `.idea` folder exists in the root project, downloads assets

  \(if not up-to-date\) and installs run configurations in

  `.idea/runConfigurations`.

* Adds `net.fabricmc:fabric-mixin-compile-extensions` and its

  dependencies with the `annotationProcessor` dependency

  configuration.

* Configures all non-test JavaCompile tasks with configurations for

  the Mixin annotation processor.

* Configures the `remapJar` task to output a JAR with the same name as

  the `jar` task output, then adds a "dev" classifier to the `jar`

  task.

* Configures the `remapSourcesJar` task to process the `sourcesJar`

  task output if the task exists.

* Adds the `remapJar` task and the `remapSourcesJar` task as

  dependencies of the `build` task.

* Configures the `remapJar` task and the `remapSourcesJar` task to add

  their outputs as `archives` artifacts when executed.

* For each MavenPublication \(from the `maven-publish` plugin\):
  * Manually appends dependencies to the POM for mod-augmented

    dependency configurations, provided the dependency configuration

    has a Maven scope.

All run configurations have the run directory `${projectDir}/run` and the VM argument `-Dfabric.development=true`. The main classes for run configurations is usually defined by a `fabric-installer.json` file in the root of Fabric Loader's JAR file when it is included as a mod dependency, but the file can be defined by any mod dependency. If no such file is found, the main classes defaults to `net.fabricmc.loader.launch.knot.KnotClient` and `net.fabricmc.loader.launch.knot.KnotServer`.

The client run configuration is configured with `--assetsIndex` and `--assetsDir` program arguments pointing to the loom cache directory containing assets and the index file for the configured version of Minecraft. When running on OSX, the "-XstartOnFirstThread" VM argument is added.

## Configuration

`minecraft` extension properties:

* `runDir` \(String\): `"run"` by default. Defines the run directory

  used by run configurations and the `runServer` and `runClient`

  tasks.

* `refmapName` \(String\): `"${projectName}-refmap.json"` by default.

  Defines the name of the mixin refmap.

* `loaderLaunchMethod` \(String\): Empty string by default. Defines the

  method used to launch Fabric Loader in run configurations. The

  launch method used is Knot by default. If set to another value

  `method`, Loom will attempt to use read

  `fabric-installer.${method}.json` for run configurations and fall

  back to `fabric-installer.json` if none can be found. If set to

  `"launchwrapper"` and no fabric installer definitions can be found,

  run configurations will use default LaunchWrapper run configuration

  for Fabric Loader.

* `remapMod` \(boolean\): `true` by default. If false, disables the

  configuration of the `remapJar` task, the `remapSourcesJar` task and

  the `jar` task.

* `autoGenIDERuns` \(boolean\): `true` by default. If false, disables

  the automatic downloading of assets and generation of IntelliJ run

  configurations if an `.idea` folder exists in the root project.

* `extractJars` \(boolean\): `false` by default. If true, Loom will

  recursively extract and remap nested JARs of mod dependencies.

## Publishing

The output of the `remapJar` task is generally the artifact that should be published, NOT the output of the `jar` task. It is important that any publishing task using the `remapJar` task output depends on the task. Unlike the `jar` task, the `remapJar` task is not an AbstractArchiveTask, which means it requires extra care to set up task dependencies correctly when integrating with plugins like CurseGradle or `maven-publish`. The output of the `remapSourcesJar` should be used similarly when publishing sources.

When using the `maven-publish` plugin, avoid using `from components.java`, and instead declare artifacts as follows:

```text
mavenJava(MavenPublication) {
    artifact(jar.archivePath) {
        builtBy remapJar
    }
    // artifact(sourcesJar) {
    //     builtBy remapSourcesJar
    // }
    ...
}
```

When publishing a project using nested jars to a Maven repository for usage in development environments, it may be desireable to publish artifacts without nested dependencies and instead rely on transitive dependencies. Transitive dependencies declared in the POM are added to a consumer's development environment by their build system with better integration opportunities. It allows the consumer to attach sources to transitive dependencies and requires no extra configuration to be added to the compile and runtime classpath.

## Useful task types

* `net.fabricmc.loom.task.RemapJarTask`: Takes an input JAR and

  outputs a remapped JAR. Should be configured to depend on the task

  that produces the input JAR. This task is not an

  AbstractArchiveTask. Has the following properties:

  * `input` \(Object\): `null` by default. Defines the JAR file to be

    remapped. Resolved using `Project.file`.

  * `output` \(Object\): `null` by default. Defines the output JAR

    file. Resolved using `Project.file`.

  * `addNestedDependencies` \(boolean\): `false` by default. If true,

    Loom will nest dependencies added with the `include` dependency

    configuration into `META-INF/jars` in the jar and declare them

    in the fabric.mod.json file.

* `net.fabricmc.loom.task.RemapSourcesJarTask`: Takes an input Java

  sources JAR and outputs a remapped Java sources JAR. Should be

  configured to depend on the task that produces the input JAR. This

  task is not an AbstractArchiveTask. Has the following properties:

  * `input` \(Object\): `null` by default. Defines the sources JAR

    file to be remapped. Resolved using `Project.file`.

  * `output` \(Object\): `null` by default. Defines the output sources

    JAR file. Resolved using `Project.file`.

  * `targetNamespace` \(String\): `"intermediary"` by default. Defines

    the namespace to remap to. Remaps to intermediary as long as the

    value is not `"named"`.

## Development environment setup

Loom is designed to work out of the box by simply setting up a workspace in the user's IDE of choice. It does quite a few things behind the scenes to create a development environment with Minecraft:

1. Downloads the client and server jar from official channels for the

   configured version of Minecraft.

2. Merges the client and server jar to produce a merged jar with

   `@Environment` and `@EnvironmentInterface` annotations.

3. Downloads the configured mappings.
4. Remaps the merged jar with intermediary mappings to produce an

   intermediary jar.

5. Remaps the merged jar with yarn mappings to produce a mapped jar.
6. Optional: Decompiles the mapped jar to produce a mapped sources jar

   and linemap, and applies the linemap to the mapped jar.

7. Adds dependencies of Minecraft.
8. Downloads Minecraft assets.
9. Processes and includes mod-augmented dependencies \(and optionally

   extracts and remaps nested JARs\).

## Caches

* `${GRADLE_HOME}/caches/fabric-loom`: The user cache, a cache shared

  by all Loom projects for a user. Used to cache Minecraft assets,

  jars, merged jars, intermediary jars and mapped jars.

* `.gradle/loom-cache`: The root project persistent cache, a cache

  shared by a project and its subprojects. Used to cache remapped mods

  as well as generated included mod JARs.

* `build/loom-cache`: The root project build cache.
* `**/build/loom-cache`: The \(sub\)project build cache.


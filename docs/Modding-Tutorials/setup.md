# Setting up a mod development environment

## Prerequisites

- A Java Development Kit (JDK) for Java 8 (recommended) or newer
  <https://adoptopenjdk.net/>
- Any Java IDE, for example [Intellij IDEA](https://www.jetbrains.com/idea/download/#section=windows) and
  [Eclipse](https://www.eclipse.org/downloads/). You may also use any
  other code editors, such as [Visual Studio Code](https://code.visualstudio.com/).

## Mod Startup

There are two main ways to make a new mod for Minecraft based on Fabric.
You can either manually download fabric-example-mod and setup by
yourself, or use automatic tools for you.

### Manual Steps

1. Copy the starting files from
   [fabric-example-mod](https://github.com/FabricMC/fabric-example-mod/)
   (or from [the Kotlin  version](https://github.com/natanfudge/fabric-example-mod-kotlin),
   if you wish to use Kotlin,) excluding the `LICENSE` and `README.md`
   files - as those apply to the template itself, not necessarily to
   your mod.
2. Edit `gradle.properties`:
   - Make sure to set `archives_base_name` and `maven_group` to your
     preferred values.
   - Make sure to update the versions of Minecraft, the mappings and
     the loader - all of which can be queried through [this    website](https://modmuss50.me/fabric.html) - to match the
     versions you wish to target.
   - Add any other dependencies you plan to use in `build.gradle`.
3. Import the build.gradle file to your IDE. You may refer to the next
   section for specific IDE's.
4. Happy modding!

You can generate the Minecraft sources for reference (since IDEA already
has a decompiler this is only useful for searching through the code) :
Run the `genSources` Gradle task. If your IDE doesn't have Gradle
integration, then run the following command in the terminal:
`gradlew genSources` (or `./gradlew genSources` on Linux/MacOS).

#### IntelliJ IDEA

If you are using IntelliJ IDEA by JetBrains, please follow these steps:

    - In the IDEA main menu, select 'Import Project' (or File -> Open... if you already have a project open).
    - Select the project's build.gradle file to import the project.
    - After Gradle is done setting up, close (File -> Close Project) and re-open the project to fix run configurations not displaying correctly.
    - (If the run configurations still don't show up, try reimporting the Gradle project from the Gradle tab in IDEA.)

*Optional, but recommended*: By default, IntelliJ delegates to Gradle to
build the project. This is unnecessary for Fabric and causes longer
build times and hotswapping related weirdness, among other problems. To
make it use the builtin compiler:

    - Open the 'Gradle Settings' dialog from the Gradle tab.
    - Change the 'Build and run using' and 'Run tests using' fields to 'IntelliJ IDEA'.
    - Go to File -> Project Structure -> Project and set 'Project compiler output' to ''$PROJECT_DIR$/out''.

Unfortunately, it is currently impossible to set an IDE-wide default for
the 'Build and run using' and 'Run tests using' options, so these steps
have to be repeated for every new project.

**NOTE:** Don't run `./gradlew idea` as it messes up with gradle and
breaks develop environment.

#### Eclipse

If you are using Eclipse and you would like to have the IDE run configs
you can run `gradlew eclipse`.

#### Visual Studio Code

If you are using VSCode by Microsoft, please follow [these
instructions](../Other/vscode_setup.md)

### MinecraftDev IntelliJ IDEA Plugin

If you are using IntelliJ IDEA you can use the MinecraftDev plugin. This
plugin adds support for automatically generating Fabric projects as well
as some mixin related features like inspections, generating
accessors/shadow fields, and copying Mixin Target References (JVM
Descriptors). The plugin can be found [in the IntelliJ plugin
repository](https://plugins.jetbrains.com/plugin/8327), so you can
install it using IntelliJ's internal plugin browser by navigating to
File â†’ Settings â†’ Plugins, then clicking the Marketplace tab and
searching for Minecraft.

### Generator

If you are unable to use the MinecraftDev plugin or the
[fabric-example-mod](https://github.com/FabricMC/fabric-example-mod/)
repo, you may also use
[GeneratorFabricMod](https://github.com/ExtraCrafTX/GeneratorFabricMod)
by ExtraCrafTX, a convenient tool to automatically generate new fabric
mods from template. Follow these steps:

1. Download latest release of GeneratorFabricMod
   [here](https://github.com/ExtraCrafTX/GeneratorFabricMod/releases)
   and extract them.
2. Make a new directory, then run a command shell there and type
   `path/to/GeneratorFabricMod/bin/GeneratorFabricMod`.
3. After letting it get its dependencies, follow the instructions and
   input required information.
4. You're now done, just open the project folder with in your IDE.

## Getting started

Try [adding an item](../Modding-Tutorials/Items/item.md) or [a block](../Modding-Tutorials/Blocks-and-Block-Entities/block.md).
It's also a good idea to visit [Applying changes without restarting
Minecraft](../Modding-Tutorials/Development-Tools/applychanges.md).

## Advice

- While Fabric API is not strictly necessary for developing mods, its
  primary goal is to provide cross-compatibility and hooks where the
  game engine does not, and as such it is highly recommended! Even
  some of the tutorials on the wiki implicitly require Fabric API.
- Occasionally, with development of fabric-loom (our Gradle build
  plugin) issues may crop up which require resetting the cache files.
  This can be done by running `gradlew cleanloom`. Running
  `gradlew --stop` can also help with a few rare issues.
- Don't hesitate to ask questions! We're here to help you and work
  with you to make your dream mod a reality.

## Troubleshooting

### Missing sounds

Sometimes, when importing the Gradle project into an IDE, the assets
might not download correctly. In this case, run the `downloadAssets`
task manually - either using IDE's built-in menu or by simply running
`gradlew downloadAssets`.

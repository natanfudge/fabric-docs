# Setting up a mod development environment

## Prerequisites

- A Java Development Kit (JDK) for Java 8 or newer
  <https://adoptopenjdk.net/>
- Any IDE, for example [Intellij IDEA](https://www.jetbrains.com/idea/download/#section=windows)

## Steps

```
- Copy the starting files from [[https://github.com/FabricMC/fabric-example-mod/|fabric-example-mod]] (or from [[https://github.com/natanfudge/fabric-example-mod-kotlin|the Kotlin version]], if you wish to use Kotlin,) excluding the ''LICENSE'' and ''README.md'' files - as those apply to the template itself, not necessarily to your mod.
- Edit ''gradle.properties'':
    * Make sure to set ''archives_base_name'' and ''maven_group'' to your preferred values.
    * Make sure to update the versions of Minecraft, the mappings and the loader - all of which can be queried through [[https://modmuss50.me/fabric.html|this website]] - to match the versions you wish to target.
    * Add any other dependencies you plan to use in ''build.gradle''.
- Import the build.gradle file to your IDE. Follow [[tutorial:vscode_setup|these instructions]] to import the project to Visual Studio Code.
- For IntelliJ, press ''Add Configuration...'' and then select ''Minecraft Client'' and press ''OK''.      For Eclipse, If you would like to have the IDE run configs you can run ''gradlew eclipse''.
- Happy modding!
```

You can generate the Minecraft sources for reference (since IDEA already
has a decompiler this is only useful for searching through the code) :
Run the `genSources` Gradle task. If your IDE doesn't have Gradle
integration, then run the following command in the terminal: `gradlew
genSources` (or `.gradlew genSources` on Linux/MacOS).

## Getting started

Try [adding an item](../Modding-Tutorials/Items/item.md) or [a block](../Modding-Tutorials/Blocks-and-Block-Entities/block.md).
Itâ€™s also a good idea to visit [Applying changes without restarting
Minecraft](../Modding-Tutorials/Development-Tools/applychanges.md).

## Advice

- While Fabric API is not strictly necessary for developing mods, its
  primary goal is to provide cross-compatibility and hooks where the
  game engine does not, and as such it is highly recommended\!
- As Fabric is in early development, occasionally, with development of
  fabric-loom (our Gradle build plugin) issues may crop up which
  require a manual clearing (deleting) of the cache (which can be
  found in `.gradle/caches/fabric-loom`). Those will generally be
  announced as they are identified.
- Don't hesitate to ask questions\! We're here to help you and work
  with you to make your dream mod a reality.

## Troubleshooting

### Missing sounds

Sometimes, when importing the Gradle project into an IDE, the assets
might not download correctly. In this case, run the `downloadAssets`
task manually - either using IDE's built-in menu or by simply running
`gradlew downloadAssets`.

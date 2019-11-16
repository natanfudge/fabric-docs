# Updating Yarn mappings in a Java codebase

# Loom 0.2.6 and above

Currently, this process will only affect Java files.

Say you want to migrate from 1.14.4 to 19w46b.

1. Go [here](https://modmuss50.me/fabric.html?&version=19w46b), select
   `19w46b`, and copy the `yarn_mappings` value, for example
   `19w46b+build.1`. DO NOT modify your gradle.properties or
   build.gradle yet.
2. In the root of your gradle project, run `gradlew migrateMappings
   --mappings "19w46b+build.1"`
3. Your migrated source will appear in `remappedSrc`. Verify your code
   was not nuked and copy it over.
4. Update your gradle.properties file according to the instructions in
   [this](https://modmuss50.me/fabric.html?&version=19w46b) site.
5. You're done.

#### Additional customization

- Specify from where to take your Java files with `--input
  path/to/source`. Default: `src/main/java`.
- Specify where to output the remapped source with `--output
  path/to/output`. Default: 'remappedSrc'.
- Specify a custom place to retrieve the mappings from with
  `--mappings some_group:some_artifact:some_version:some_qualifier`.
  Default: `net.fabricmc:yarn:<version-you-inputted>:v2`.

# Loom 0.2.5 and below

### Requirements

- Fabric-Loom 0.2.2 or above
- A Java codebase - Kotlin and Scala will not, currently, do.
- Some assembly required

### Instructions

1. Figure out your target mappings version. For example,
   "net.fabricmc:yarn:1.14.1 Pre-Release 2+build.2".
2. Make sure the mappings for this version get created. This is the
   hacky part, as currently the only way to do it is to edit the
   "minecraft" and "mappings" fields in a build.gradle to the new
   version, run any Gradle command ("gradle build" will do, even if it
   crashes), then **change the fields back**.
3. Run the following magical wizardry command: `%%gradle
   migrateMappings -PtargetMappingsArtifact="net.fabricmc:yarn:1.14.1
   Pre-Release 2+build.2" -PinputDir=src/main/java
   -PoutputDir=remappedSrc%%`, where:
   - "targetMappingsArtifact" refers to the target mappings version.
     It is imperative that the build.gradle be set to the current
     mappings version of the mod when running this command\!
   - "inputDir" is the input directory, containing Java source code,
   - "outputDir" is the output directory. It will be created if it is
     missing.
4. Copy the remapped source code to the input directory, if
   everything's fine,
5. Hope for the best.

*Note: You may need to specify the full paths in quotes, try this if you
get file not found issues.*

This should work across Minecraft versions as well, provided we haven't
massively broken Intermediaries or done something equally silly (aka:
most of the time).

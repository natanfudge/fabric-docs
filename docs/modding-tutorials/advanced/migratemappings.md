# Updating Yarn mappings in a Java codebase

## The Hacky Way

### Requirements

* Fabric-Loom 0.2.2 or above
* A Java codebase - Kotlin and Scala will not, currently, do.
* Some assembly required

### Instructions

1. Figure out your target mappings version. For example,

   "net.fabricmc:yarn:1.14.1 Pre-Release 2+build.2".

2. Make sure the mappings for this version get created. This is the

   hacky part, as currently the only way to do it is to edit the

   "minecraft" and "mappings" fields in a build.gradle to the new

   version, run any Gradle command \("gradle build" will do, even if it

   crashes\), then **change the fields back**.

3. Run the following magical wizardry command: \`%%gradle

   migrateMappings -PtargetMappingsArtifact="net.fabricmc:yarn:1.14.1

   Pre-Release 2+build.2" -PinputDir=src/main/java

   -PoutputDir=remappedSrc%%\`, where:

   * "targetMappingsArtifact" refers to the target mappings version.

     It is imperative that the build.gradle be set to the current

     mappings version of the mod when running this command!

   * "inputDir" is the input directory, containing Java source code,
   * "outputDir" is the output directory. It will be created if it is

     missing.

4. Copy the remapped source code to the input directory, if

   everything's fine,

5. Hope for the best.

_Note: You may need to specify the full paths in quotes, try this if you get file not found issues._

This should work across Minecraft versions as well, provided we haven't massively broken Intermediaries or done something equally silly \(aka: most of the time\).

## The Non-Hacky Way

Coming soon! \(Hopefully.\)


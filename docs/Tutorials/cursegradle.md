# Publishing mods on CurseForge with CurseGradle

To familiarize yourself with CurseGradle, please read the [project's
official wiki](https://github.com/matthewprenger/CurseGradle/wiki).

## Fabric-specific changes

(Note: Last updated for Loom 0.2.5.)

The additions necessary for usage with Fabric have been highlighted in
green. If you are using Loom 0.2.5 `remapJar.output` should be just
`remapJar` instead.

![](../images/tutorial/cursegradle_changes.png)

They are, in order:

- `%%afterEvaluate { ... }%%` - Loom's remapJar tweaks currently
  happen after evaluation, and as such remapJar.output can only be
  read then,
- `%%mainArtifact(remapJar)%%` (or `mainArtifact(remapJar.output)` for
  Loom 0.2.4 and earlier) - the primary artifact submitted to
  CurseForge should be the output of remapJar, that is the remapped
  (production-ready) mod .JAR file,
- `%%uploadTask.dependsOn(remapJar)%%` - make sure that the CurseForge
  upload task only runs once the remapped JAR has been built,
- `%%forgeGradleIntegration = false%%` - as you're not using
  ForgeGradle, that specific integration has to be disabled.


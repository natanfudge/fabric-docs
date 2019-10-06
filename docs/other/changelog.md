# Changelog, Summarized

This is a log of significant changes in and additions to Fabric. Minor changes, renames, minor bugfixes, etc. will not be mentioned here, to remain easily readable for someone who just wants to catch up.

## 2018-12-12

* **\[Everything\]** We're 18w50a now. Took a bit over two hours, but

  Mojang took liberties with the amount of refactors this time around.

* **\[API\]** Crash reports now list the mods installed.
* **\[API\]** Entity registration has been fixed - albeit you should

  use FabricEntityTypeBuilder in place of EntityType.Builder.

* **\[Loom\]** Fixed runClient/runServer not working right with

  mixins! At last!

## 2018-12-11

* **\[API\]** Added FabricEntityTypeBuilder and EntityTrackingRegistry

  for registering custom server-&gt;client synchronization rules.

* **\[Enigma\]** Fixed very annoying bug with renaming method

  arguments in interfaces.

* **\[Loader\]** Fixed very annoying bug with directories with spaces

  and other special characters not working correctly.


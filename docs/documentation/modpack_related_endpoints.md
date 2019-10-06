# Modpack-related endpoints

## Downloading templates

### Available GET parameters

* **intermediary** Intermediary version, generally equal to a given

  Minecraft version as defined in the game's version manifest.

* **yarn** Yarn version. Only used if "intermediary" not present;

  generally, "intermediary" is preferred for production use due to

  updating once for each given version of the game.

* **loader** Fabric Loader version.
* **format** Download format.

### Available endpoints

* **/download/mcupdater** - The result is a XML file attachable via an

  \ entry to an MCUpdater ServerPack XML. No custom formats

  are available.

- **/download/multimc** - The default result is a ZIP file containing an importable MultiMC instance. Alternate formats:

* **patchJson** Returns a JSON representing a MultiMC patch for

  Fabric.

- **/download/vanilla** - The default result is a ZIP containing a Fabric game profile, which can be extracted to .minecraft/versions. Alternate formats:

* **profileJson** Returns a JSON representing a vanilla Fabric

  profile.

* **shimJar** Returns a JAR containing a single file,

  `version.json`, with the vanilla Fabric profile. This is

  intended for future SKCraft/Solder support.

## Version querying

### maven-metadata.xml

The Maven metadata XML files can be used to query the latest and available versions of Fabric components.

* Fabric Loader:

  [https://maven.fabricmc.net/net/fabricmc/fabric-loader/maven-metadata.xml](https://maven.fabricmc.net/net/fabricmc/fabric-loader/maven-metadata.xml)

* Yarn:

  [https://maven.fabricmc.net/net/fabricmc/yarn/maven-metadata.xml](https://maven.fabricmc.net/net/fabricmc/yarn/maven-metadata.xml)

### Fabric Meta API

A HTTP JSON API is provided to allow lookup of Fabric versions for a specific Minecraft version, among other things. See the documentation on the [Github](https://github.com/FabricMC/fabric-meta), the API service is hosted at [https://meta.fabricmc.net/](https://meta.fabricmc.net/) .

If you find something missing from the meta service open an issue on [Github](https://github.com/FabricMC/fabric-meta) for it to be evaluated.


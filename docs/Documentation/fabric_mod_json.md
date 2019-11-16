# fabric.mod.json

The fabric.mod.json file is a mod metadata file used by Fabric Loader to
load mods. In order to be loaded, a mod must have this file with the
exact name placed in the root directory of the mod JAR.

## Mandatory fields

- **schemaVersion** Needed for internal mechanisms. Must always be
  `1`.
- **id** Defines the mod's identifier - a string of Latin letters,
  digits, underscores with length from 1 to 63.
- **version** Defines the mod's version - a string value, optionally
  matching the [Semantic Versioning 2.0.0](https://semver.org/)
  specification.

## Optional fields

### Mod loading

- **environment**: Defines where mod runs: only on the client side
  (client mod), only on the server side (plugin) or on both sides
  (regular mod). Contains the environment identifier:
  - **`*`** Runs everywhere. Default.
  - **client** Runs on the client side.
  - **server** Runs on the server side.
- **entrypoints** Defines main classes of your mod, that will be
  loaded.
  - There are 3 default entry points for your mod:
    - **main** Will be run first. For classes implementing
      `ModInitializer`.
    - **client** Will be run second and only on the client side.
      For classes implementing `ClientModInitializer`.
    - **server** Will be run second and only on the server side.
      For classes implementing `DedicatedServerModInitializer`.
  - Each entry point can contain any number of classes to load.
    Classes (or methods or static fields) could be defined in two
    ways:
    - If you're using Java, then just list the classes (or else)
      full names. For example:

<!-- end list --->

```json
"main": [
    "net.fabricmc.example.ExampleMod",
    "net.fabricmc.example.ExampleMod::handle"
]
```

- If you're using any other language, consult the language adapter's
  documentation. The Kotlin one is located
  [here](https://github.com/FabricMC/fabric-language-kotlin/blob/master/README.md).

<!-- end list --->- **jars** A list of nested JARs inside your mod's JAR to load. Before
  using the field, check out [the guidelines on the usage of the nested JARs](../Modding-Tutorials/Advanced/loader04x.md#nested_jars). Each entry is an
  object containing `file` key. That should be a path inside your
  mod's JAR to the nested JAR. For example:

<!-- end list --->

```json
"jars": [
   {
      "file": "nested/vendor/dependency.jar"
   }
]
```

- **languageAdapters** A dictionary of adapters for used languages to
  their adapter classes full names. For example:

<!-- end list --->

```json
"languageAdapters": {
   "kotlin": "net.fabricmc.language.kotlin.KotlinAdapter"
}
```

- **mixins** A list of mixin configuration files. Each entry is the
  path to the mixin configuration file inside your mod's JAR or an
  object containing following fields:
  - **config** The path to the mixin configuration file inside your
    mod's JAR.
  - **environment** The same as upper level **environment** field.
    See above. For example:

<!-- end list --->

```json
"mixins": [
   "modid.mixins.json",
   {
      "config": "modid.client-mixins.json",
      "environment": "client"
   }
]
```

### Dependency resolution

The key of each entry of the objects below is a Mod ID of the
dependency.

The value of each key is a string or array of strings declaring
supported version ranges. In the case of an array, an "OR" relationship
is assumed - that is, only one range has to match for the collective
range to be satisfied.

In the case of all versions, \* is a special string declaring that any
version is matched by the range. In addition, exact string matches must
be possible regardless of the version type.

- **depends** For dependencies required to run. Without them a game
  will crash.
- **recommends** For dependencies not required to run. Without them a
  game will log a warning.
- **suggests** For dependencies not required to run. Use this as a
  kind of metadata.
- **breaks** For mods whose together with yours might cause a game
  crash. With them a game will crash.
- **conflicts** For mods whose together with yours cause some kind of
  bugs, etc. With them a game will log a warning.

### Metadata

- **name** Defines the user-friendly mod's name. If not present,
  assume it matches **id**.
- **description** Defines the mod's description. If not present,
  assume empty string.
- **contact** Defines the contact information for the project. It is
  an object of the following fields:
  - **email** Contact e-mail pertaining to the mod. Must be a valid
    e-mail address.
  - **irc** IRC channel pertaining to the mod. Must be of a valid
    URL format - for example: `irc://irc.esper.net:6667/charset` for
    `#charset` at EsperNet - the port is optional, and assumed to be
    6667 if not present.
  - **homepage** Project or user homepage. Must be a valid
    HTTP/HTTPS address.
  - **issues** Project issue tracker. Must be a valid HTTP/HTTPS
    address.
  - **sources** Project source code repository. Must be a valid URL
    - it can, however, be a specialized URL for a given VCS (such as
      Git or Mercurial).
  - The list is not exhaustive - mods may provide additional,
    non-standard keys (such as **discord**, **slack**, **twitter**,
    etc) - if possible, they should be valid URLs.
- **authors** A list of authors of the mod. Each entry is a single
  name or an object containing following fields:
  - **name** The real name, or username, of the person. Mandatory.
  - **contact** Person's contact information. The same as upper
    level **contact**. See above. Optional.
- **contributors** A list of contributors to the mod. Each entry is
  the same as in **author** field. See above.
- **license** Defines the licensing information. Can either be a
  single license string or a list of them.
  - This should provide the complete set of preferred licenses
    conveying the entire mod package. In other words, compliance
    with all listed licenses should be sufficient for usage,
    redistribution, etc. of the mod package as a whole.
  - For cases where a part of code is dual-licensed, choose the
    preferred license. The list is not exhaustive, serves primarily
    as a kind of hint, and does not prevent you from granting
    additional rights/licenses on a case-by-case basis.
  - To aid automated tools, it is recommended to use [SPDX License   Identifiers](https://spdx.org/licenses/) for open-source
    licenses.
- **icon** Defines the mod's icon. Icons are square PNG files.
  (Minecraft resource packs use 128Ã—128, but that is not a hard
  requirement - a power of two is, however, recommended.) Can be
  provided in one of two forms:
  - A path to a single PNG file.
  - A dictionary of images widths to their files' paths.

## Custom fields

You can add any field you want to add inside `custom` field. Loader
would ignore them. However *it's highly recommended to namespace your
fields* to avoid conflicts if your fields (names) would be added to the
standard specification.

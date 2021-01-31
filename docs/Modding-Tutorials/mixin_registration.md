# Registering Mixins

### Introduction

In this tutorial, you will learn how to register your Mixins through the
`resources/fabric.mod.json`.

Inside your `resources` folder is where your `fabric.mod.json` should
be.

Use this link to view the Fabric Example Mod's resources folder: [Fabric
Example Mod
Resources](https://github.com/FabricMC/fabric-example-mod/tree/master/src/main/resources)

Inside your `fabric.mod.json` is where you define where Fabric should
look for your `mixins.json`.

### Register Mixins with Fabric

To register a mixin you have to tell Fabric where to look. To tell
Fabric where to look you need to add elements to the `mixins` array
inside `fabric.mod.json`

```json
{
  "mixins": [
    "modid.mixins.json"
  ]
}
```

Providing a String `"<modid>.mixins.json"` inside the mixins array tells
Fabric to load the mixins defined inside the file `<modid>.mixins.json`.

### Register Mixins

In the previous section, you learned about registering your
`<modid>.mixins.json` files.

We still have to define which mixins to load and where these mixins are
located.

Inside your registered `<modid>.mixins.json`:

```json
{
  "required": true,
  "minVersion": "0.8",
  "package": "net.fabricmc.example.mixin",
  "compatibilityLevel": "JAVA_8",
  "mixins": [],
  "client": [
    "TitleScreenMixin"
  ],
  "server": [],
  "injectors": {
    "defaultRequire": 1
  }
}
```

The **4** main fields you should worry about when getting started with
mixins are the `package` field, and the `mixins`, `client`, `server`
arrays.

The `package` field defines which folder (package) to find the Mixins
in.

The `mixins` array defines which classes should be loaded on both the
client and server.

The `client` array defines which classes should be loaded on the client.

The `server` array defines which classes should be loaded on the server.

Following that logic: `net.fabricmc.example.mixin.TitleScreenMixin` is
the mixin class that will be loaded on the client.

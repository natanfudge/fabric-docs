# Installing Fabric

## Regular installation

### MultiMC

**NOTE** MultiMC is a modding-friendly alternate launcher for Minecraft, which is recommended for working with Fabric and can be found here. If you don't wish to use MultiMC or don't know or care what launcher you're using, follow through to the [regular client](https://github.com/natanfudge/fabric-docs/tree/6eb2caa259990638fff8a0b0ad24c9b0543e6a77/docs/Setup/.md/README.md#regular_client) instructions.

1. Open [the download page](https://fabricmc.net/use/) and choose the

   game, mapping and loader versions. Click “Copy MultiMC instance

   url”.

   * Generally, choosing the latest mapping version for a given game

     version is advisable, as mods do not depend on the deobfuscated

     names staying the same.

   * The loader should be mostly game version-independent. If this

     situation changes, it will be pointed out, so don't worry!

2. Start MultiMC. Click on “Add Instance” in the top-left, then select

   “Import from ZIP” and paste the URL in the text field. Don't

   forget to set the name!

3. Press OK. Your Fabric instance is ready to go - feel free to add

   mods to it!

For more detailed instructions visit this guide: [Installing Fabric using the MultiMC launcher](https://github.com/natanfudge/fabric-docs/tree/6eb2caa259990638fff8a0b0ad24c9b0543e6a77/docs/Setup/Setup/install_with_multimc.md)

### Regular client

1. Download the Fabric installer from [the download  page](https://fabricmc.net/use/).
   * You can download the installer directly from [modmuss50's    Jenkins](https://jenkins.modmuss50.me/job/FabricMC/job/fabric-installer/job/master/)

     as well.
2. Open the installer. In the window you need to configure the mapping

   and loader version \(as per advice in the [MultiMC  section](https://github.com/natanfudge/fabric-docs/tree/6eb2caa259990638fff8a0b0ad24c9b0543e6a77/docs/Setup/.md/README.md#multimc)\) and the install location \(the default should be

   fine on most platforms\).  

   **NOTE** You need to enable snapshots in order to make the installer

   show mapping options for Minecraft snapshots.

3. Press Install. A new game version and profile will be created in the

   launcher's menu, which you can now use to launch Fabric.

### Server

**NOTE** **Up-to-date as of Loader 0.4.4+.** Older versions choose different techniques. Installer 0.4.0+ required!

1. Download the Fabric installer from [the download  page](https://fabricmc.net/use/).
   * You can download the installer directly from [modmuss50's    Jenkins](https://jenkins.modmuss50.me/job/FabricMC/job/fabric-installer/job/master/)

     as well.
2. Open the installer. Select the "Server" tab at the top of the

   window.

3. In the window you need to configure the mapping and loader version

   and the install location.  

   **NOTE** You need to enable snapshots in order to make the installer

   show mapping options for Minecraft snapshots.

4. Press Install. In the output directory, a `fabric-server-launch.jar`

   will be created. It expects a `server.jar`, which is the vanilla

   server JAR, generally found on Mojang's version announcement blog

   posts, next to it, but will also generate a properties file on first

   launch where you can change the path.  

   **NOTE** If you are upgrading your existing server, remember to

   remove the `.fabric` folder if the installer did not remove that for

   you! Or you will get classloading errors.

## CLI installation

The fabric installer has full support from installing the client and the server from the command line. This could be used to automate the installation. The installer has a number of commands that can be used for a headless install.

### Available options

* **-snapshot** Enables the usage of snapshot versions of Minecraft.
* **-dir** Used to select the installation dir, defaults to the

  current working directory.

* **-mcversion** Used to select the minecraft version, defaults to the

  latest stable version.

* **-loader** Used to select the loader version, defaults to the

  latest.

* **-downloadMinecraft** Used to automatically download the Minecraft

  server jar

### Available commands

* **help** Prints out all of the commands available along with the

  latest mappings and loader versions. Ignores options. Example:\`java

  -jar fabric-installer.jar help

  \`

* **server** Used to create the required files for a Fabric server.

  Accepts all options, none are required. Example: \`java -jar

  fabric-installer.jar server

  \`

* **client** Used to create the required files for a Fabric client.

  Accepts all options, **-dir** is required. Example:\`java -jar

  fabric-installer.jar client -dir "~/Games/.minecraft"

  \`


# Ð£Ñ�Ñ‚Ð°Ð½Ð¾Ð²ÐºÐ° Fabric

## ÐžÐ±Ñ‹Ñ‡Ð½Ð°Ñ� ÑƒÑ�Ñ‚Ð°Ð½Ð¾Ð²ÐºÐ°

### MultiMC

**ÐžÐ‘Ð Ð�Ð¢Ð˜Ð¢Ð• Ð’Ð�Ð˜ÐœÐ�Ð�Ð˜Ð•** MultiMC - Ñ�Ñ‚Ð¾ Ñ�Ð¾Ð²Ð¼ÐµÑ�Ñ‚Ð¸Ð¼Ñ‹Ð¹ Ñ� Ð¼Ð¾Ð´Ð¸Ñ„Ð¸ÐºÐ°Ñ†Ð¸Ñ�Ð¼Ð¸ Ð»Ð°ÑƒÐ½Ñ‡ÐµÑ€
Minecraft, Ð¸ Ð¼Ñ‹ Ñ€ÐµÐºÐ¾Ð¼ÐµÐ½Ð´ÑƒÐµÐ¼ ÐµÐ³Ð¾ Ð¸Ñ�Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÑŒ, ÐµÑ�Ð»Ð¸ Ñ€ÐµÑ‡ÑŒ Ð¸Ð´Ñ‘Ñ‚ Ð¾ Fabric.
Ð•Ñ�Ð»Ð¸ MultiMC or don't know or care what launcher you're using, follow
through to the [regular client](../.md#regular_client) instructions.

1. Create a new game instance.
2. Select "Edit Instance" in the instance context menu or on the right
   side of the window.
3. On the Version tab of the configuration window, click "Install
   Fabric" to bring up a dialog with all the available Fabric Loader
   versions. Pick one and click OK to add it to the instance.
   - Generally using the latest available Loader version is
     recommended.
   - The loader should be mostly game version-independent. If this
     situation changes, it will be pointed out, so don't worry!
4. Press OK. Your Fabric instance is ready to go - feel free to add
   mods to it!

For more detailed instructions visit this guide: [Installing Fabric
using the MultiMC launcher](../Setup/install_with_multimc.md)

### Mojang's Minecraft Launcher

1. Download the Fabric installer from [the download  page](https://fabricmc.net/use/).
   - You can download the installer directly from [modmuss50's    Jenkins](https://jenkins.modmuss50.me/job/FabricMC/job/fabric-installer/job/master/)
     as well.
2. Open the installer. In the window you need to configure the mapping
   and loader version (as per advice in the [MultiMC  section](../.md#multimc)) and the install location (the default should be
   fine on most platforms).  
   **NOTE** You need to enable snapshots in order to make the installer
   show mapping options for Minecraft snapshots.
3. Press Install. A new game version and profile will be created in the
   launcher's menu, which you can now use to launch Fabric.

For more detailed instructions visit this guide: [Install using the
Minecraft Launcher](../Setup/install_with_minecraft_launcher.md)

### Server

**NOTE** **Up-to-date as of Loader 0.4.4+.** Older versions choose
different techniques. Installer 0.4.0+ required!

1. Download the Fabric installer from [the download  page](https://fabricmc.net/use/).
   - You can download the installer directly from [modmuss50's    Jenkins](https://jenkins.modmuss50.me/job/FabricMC/job/fabric-installer/job/master/)
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

For more detailed instructions visit this guide: [Installing a
Minecraft/Fabric Server](../Setup/installing_minecraft_fabric_server.md)

## CLI installation

The fabric installer has full support from installing the client and the
server from the command line. This could be used to automate the
installation. The installer has a number of commands that can be used
for a headless install.

### Available options

- **-snapshot** Enables the usage of snapshot versions of Minecraft.
- **-dir** Used to select the installation dir, defaults to the
  current working directory.
- **-mcversion** Used to select the minecraft version, defaults to the
  latest stable version.
- **-loader** Used to select the loader version, defaults to the
  latest.
- **-downloadMinecraft** Used to automatically download the Minecraft
  server jar
- **-noprofile** Skip creating client profile
- **-mavenurl** (advanced) Use a custom maven url when installing
- **-metaurl** (advanced) Use a custom meta server url

### Available commands

- **help** Prints out all of the commands available along with the
  latest mappings and loader versions. Ignores options. Example:

<!-- --->

    java -jar fabric-installer.jar help

- **server** Used to create the required files for a Fabric server.
  Accepts all options, none are required. Example:

<!-- --->

    java -jar fabric-installer.jar server

- **client** Used to create the required files for a Fabric client.
  Accepts all options, **-dir** is required. Example:

<!-- --->

    java -jar fabric-installer.jar client -dir "~/Games/.minecraft"


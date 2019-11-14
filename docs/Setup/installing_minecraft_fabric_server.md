# Installing a Minecraft/Fabric Server

### STEP 1: Create a MultiMC Minecraft/Fabric single player instance

Before setting up a Minecraft server with Fabric Mod loader, it may be a
good idea to install Fabric in a MultiMc instance, download and test all
the mods you want in the server in that instance, and once you have
everything set up and working, only then set up the server. You can do
that here: [Installing Fabric using the MultiMC
launcher](../Setup/install_with_multimc.md)

-----

### STEP 2: Checking if Java is installed

A Minecraft server requires the Java Runtime Environment. To check if
java is installed in your computer, open the `Run` dialog by using the
`Windows+R` hotkey. Enter `cmd` in the textbox and click `OK` This will
open a command window.

![](../images/wiki/install_server_06.jpg)

In the command window type `java -version` and press `Enter`

![](../images/wiki/install_server_07.jpg)

If a Java version number is reported similar to this:

![](../images/wiki/install_server_08.jpg)

You may skip **STEP 3** if you don't want to update or replace the Java
version installed.

If Java is not installed, the result will be something like this:

![](../images/wiki/install_server_09.jpg)

-----

### STEP 3: Optional: Installing JAVA

If in STEP 2 you determined that you don't have Java installed, you will
need to install it.

If you already have at least `Java 8` installed you can skip this step.

To download the Java installer, go
[here](https://adoptopenjdk.net/releases.html)

![](../images/wiki/install_server_10.jpg)

Pick Version 8 of the JRE, and the HotSpot JVM, download the installer
for your operating system and install JAVA.

![](../images/wiki/install_server_10a.jpg)

-----

### STEP 4: Install Fabric in the Server Folder

Go to <https://fabricmc.net/use/> and select the â€œServerâ€� option, then
download the Windows exe installer or the Universal JAR installer.

Run the Fabric Installer jar/exe you downloaded. Select the `Server`
tab. Select the `Minecraft Version` and the `Loader Version`

In `Select Install Location` enter the name of the folder where you want
the server to be installed. If the folder does not exist the installer
will create it.

Click `Install`

![](../images/wiki/install_server_01.jpg)

The installation process may take some time, when the install is done,
you will see this:

![](../images/wiki/install_server_02.jpg)

Click on `Download server jar` to download the Minecraft server.jar file

Click on `Generate` to generate `start.bat` and `start.sh` You can use
those batch files to start the server.

![](../images/wiki/install_server_03.jpg)

You may now close the Fabric Installer.

In the Command Window you opened in **STEP 2** type:

`%%cd "c:\mc-server" [ENTER]%%`

-----

*Note: If the server path is in another drive, first switch to that
drive, for example if the server path is in the D: drive, enter this at
the command prompt:*

`d: [ENTER]`

-----

The command prompt should change to:

`c:\mc-server>`

Now type the following to create the `eula.txt` file:

`echo eula=true>eula.txt [ENTER]`

-----

### STEP 5: Starting the server

**NOTE:** If you are upgrading Fabric in this server, delete the
`.fabric` folder inside the server folder if it exists. If you are also
upgrading the Minecraft version, remember to download the correct
`server.jar` as explained before.

Start the server by typing in the command window:

`java -jar fabric-server-launch.jar nogui [ENTER]`

If you are using Windows and get a Windows Security Alert, click on
`Allow access`

![](../images/wiki/install_server_13.jpg)

You should see something like this in the command window:

![](../images/wiki/install_server_14.jpg)

Now that the server is up and running, you can stop it and create/edit a
batch file that starts the server. Type

`stop [ENTER]`

in the command window to stop the server. Then type the following:

`echo java -jar fabric-server-launch.jar nogui>start.bat [ENTER]`

If you want to specify how much RAM to allocate to the server, type this
instead of line above:

`echo java Xms1024m -Xmx2048m -jar fabric-server-launch.jar
nogui>start.bat [ENTER]`

Use the values you want for Xms and Xmx

Now type this:

`echo pause>>start.cmd [ENTER]` (that's two "\>", not "\>\>")

This will create a `c:\mc-server\start.bat` file that you can use to
start the server by double clicking it.

Now you can add mods to the `c:\mc-server\mods` folder. The easiest way
to do this is to copy the mods you have in the MultiMC instance you
created to test the mods to include in the server.

You may want to review the mod list in the MultiMC instance by opening
the Mod Menu GUI. Any mod labeled as **CLIENT** you can exclude from the
server mods folder.

![](../images/wiki/install_server_15.jpg)

**If you are using Optifine in the multimc instance, do not copy the
optifine and optifabric jar files in the server mods folder.**

After copying the mod files to the server mods folder, you may also want
to copy the MultiMC instance `config` folder files to the server
`config` folder.

Now restart the server by double clicking the `start.bat` file.

-----

### NOTES

Some mods that are intended to only be installed in the CLIENT may have
bugs and crash the server if you copy those mods to the server mods
folder. You will have to determine the mod that is crashing the server
by removing mods one by one, or half the mods at a time until you find
the mod that is causing the crash. If you encounter this problem, please
open an issue in the mod issues page.

If you are going to use a hosted server, and the provider only allows
you to use `server.jar`:

Rename `server.jar` to `vanilla.jar`

Rename `fabric-server-launch.jar` to `server.jar`

Edit the `fabric-server-launcher.properties` config file:
`serverJar=vanilla.jar`

This may work if the hosting server only checks for the file name of the
jar. If it seems that you are not able to setup a Fabric service on a
hosted server, ask the hosting company to add support for Fabric.

-----

### Links

- <https://fabricmc.net/wiki/tutorial:install_with_multimc>
- <https://fabricmc.net/use/>
- <https://www.minecraft.net/en-us/download/server/>
- <https://adoptopenjdk.net/releases.html>
- <https://minecraft.gamepedia.com/Tutorials/Setting_up_a_server>


# Installing Fabric using the MultiMC launcher

MultiMC is a free, open source launcher for Minecraft. It allows you to
have multiple, cleanly separated instances of Minecraft (each with their
own mods, texture packs, saves, etc) and helps you manage them and their
associated options with a simple and powerful interface.

### STEP 1: Install MultiMC

Download and install MultiMC from [here](https://multimc.org/). If you
are on Windows, you can extract and move the MultiMC folder inside the
zip you downloaded to the root folder of your C: drive, so you end up
with MultiMC installed on C:\\MultiMC, or use any other location to
unzip the MultiMC folder.

-----

### STEP 2: Create a new Fabric Minecraft instance in MultimMC 0.6.6 or higher versions

Open MultiMC and configure it using the setup steps. Click the `Add
Instance` button on the MultiMC toolbar:

![](.)

Enter a name for the instance and select a Minecraft version, then click
`OK`

![](.)

Now with the instance selected, click on the `Edit Instance` button on
the right:

![](.)

Now click on `Install Fabric`

![](.)

Select the Fabric Loader version and click `OK`

![](.)

Check that the Intermediary Mappings for the Minecraft Version of the
Instance and the Fabric Loader are listed. Click `Close`

![](.)

Now run the instance you just created so MultiMC will download the
appropiate Minecraft and library files and create the instance
subfolders.

Check that Minecraft/Fabric starts and everything is fine:

![](.)

Close the Minecraft instance for now.

Skip to **STEP 3**.

-----

### STEP 2A: Create a new Fabric Minecraft instance in older versions of MultimMC \< 0.6.6

Go to <https://fabricmc.net/use/> and select the "MultiMC" option,
select mapping and loader versions. Generally, choosing the latest
mapping version for a given game version is recommended. Take note of
the Minecraft version you choose (for example "1.14.2"), you will need
that information later.

Click on "Copy MultiMC instance url":

![](.)

Now that you have copied the instance url, open MultiMC
(C:\\MultiMC\\MultiMC.exe). Click the "Add Instance" button on the
MultiMC toolbar:

![](.)

Select "Import from zip" on the left, and paste the instance link you
just copied into the textbox:

![](.)

Edit the instance name and then click "OK" to create the instance:

![](.)

Now run the instance you just created so MultiMC will download the
appropiate Minecraft and library files and create the instance
subfolders.

Check that Minecraft/Fabric starts and everything is fine:

![](.)

Close the Minecraft instance for now.

-----

### STEP 3: Install the Fabric API

The instance you just created contains the Fabric Mod Loader, you now
have to install the Fabric API before being able to use most Fabric
mods.

The Fabric API is the core library for the most common hooks and
intercompatibility measures utilized by mods using the Fabric toolchain.

If that sounds too complicated just consider the Fabric API as just
another mod that will be loaded by the Fabric Loader, and almost all
Fabric mods depend on it, so this will be the first mod to install.

Go to
<https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all> and
download the latest file for the version of Minecraft you used in the
MultiMC instance you just created.

Be aware that Curse usually is not up to date to the latest Minecraft
versions, so the version indicated in the "Game Version" column may not
be the real actual version of the file\! Check the file name for the
real Minecraft version the file is intended for.

For example if your instance is for Minecraft 1.14.2, the correct Fabric
Api file would be "\[1.14.2\] Fabric API 0.3.0 build 175" (there may be
a newer version of the 1.14.2 Fabric Api after this tutorial was
written).

![](.)

Download the jar file to the instance mods folder.

For example if MultiMC is located in "C:\\MultiMC" and your instance
name is "FABRIC-1.14.2" then the instance mods folder will be located in
"C:\\MultiMC\\instances\\FABRIC-1.14.2\\.minecraft\\mods"

The paths `C:\MultiMC` and
`C:\MultiMC\instances\FABRIC-1.14.2\.minecraft\mods` are used as
examples, the actual path in your own computer will probably be
different:

`//path-where-you-unzipped-multimc//\MultiMC\instances\//the-name-of-your-instance//\mods`

![](.)

Run the Minecraft instance to make sure everything is working as
expected.

-----

### STEP 4: Install Fabric Mods

To be able to see the list of mods installed and change mod settings,
you may want to install the mod **Mod Menu (Fabric)**. Mod Menu adds a
button to the Minecraft main screen:

![](.)

Clicking the button opens a list of installed fabric mods. Mod settings
can be changed by clicking the gear button located on the top right:

![](.)

To install Mod Menu, go to
<https://www.curseforge.com/minecraft/mc-mods/modmenu> and click on
"files" and download the correct version for the Minecraft MultiMC
instance you just created.

Now check out Fabric mods
[here](https://www.curseforge.com/minecraft/mc-mods/fabric?filter-game-version=&filter-sort=2).
Download the mods to the instance mods folder. Again, check for MC
versions in the actual file names, do not rely solely on the "Game
Version" column. Usually older version mods will work fine in newer
versions of MC/Fabric, for example 1.14 and 1.14.1 mods may work fine in
1.14.2 MC/Fabric, so feel free to test them out.

Also be aware that some mods depend on other mods to work. When
downloading a mod file, click on the file name, go to the bottom of the
page and check out if there is a "Related Projects" entry with
dependencies listed. You will have to download the mods listed as
required dependencies and may also download the mods listed as optional
dependencies.

-----

### STEP 5: HAVE FUN\!


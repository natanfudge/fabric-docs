# Updating an existing MultiMC Instance

**OUTDATED JUST USE MULTIMC TO UPDATE**

When a new version of the Fabric loader is available, you can update
your existing MultiMC to the newest version.

### STEP 1: Verify the current version of Fabric installed

In MultiMC, open the version tab of the MultiMC instance you want to
update by either right clicking on the instance icon and selecting "Edit
Instance" in the context menu, or click the instance icon and then click
the "Edit Instance" button on the right side vertical toolbar in
MultiMC.

In the Version tab you will see the Fabric loader Game/Mappings version
and the Fabric Loader version:

![](.)

In this example the versions are:

Game/mappings version: `1.14.2 Pre-Release 3+build.2`

Loader version: `0.4.7+build.152`

### STEP 2: Verify the current available versions

Go to <https://fabricmc.net/use/> and select the "MultiMC" option. Check
"Show snapshot versions" if you are updating a Snapshot version.

Check the available versions for the Minecraft version of your MultiMC
instance:

![](.)

In this example there are newer versions available for download:

Game/mappings version: `1.14.2 Pre-Release 3+build.3`

Loader version: `0.4.8+build.154`

### STEP 3: Update Fabric

Still in <https://fabricmc.net/use/>, check "Show expert options" to
display the "Download MultiMC patch JSON" button. Click on that button
to download the JSON file:

![](.)

Now locate your MultiMC instance folder and save the `net.fabricmc.json`
file in the "patches" folder located inside the MultiMC intance folder,
overwriting the existing file with the same name.

To overwrite the existing file instead of having your browser rename the
file to `net.fabricmc (1).json` just click on the existing file name in
the "Save As" dialog and then click on "Save":

![](.)

### STEP 4: Verify that the Fabric loader in the MultiMC instance has been updated

In MultiMc, go back to the instance version tab and check the versions
displayed for the Fabric Loader. You may have to click the "Reload"
button on the lower right.

![](.)

In this example, the MultiMC instance Fabric loader was upgraded
successfully:

Game/mappings version: `1.14.2 Pre-Release 3+build.2 --> 1.14.2
Pre-Release 3+build.3`

Loader version: `0.4.7+build.152 --> 0.4.8+build.154`

### STEP 5: Delete the .fabric folder inside the minecraft folder

Before starting the instance, and to prevent potential problems, delete
the `.fabric` folder located inside the `.minecraft` folder located
inside the instance folder.

![](.)

### STEP 6: Verify if the Fabric Api should be updated

Go to <https://minecraft.curseforge.com/projects/fabric/files> and
compare the latest version of the Fabric API file available to the one
you have in the mods folder. Update the Fabric Api jar file if required.

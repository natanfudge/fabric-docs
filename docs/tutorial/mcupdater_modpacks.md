# Fabric MCUpdater Modpack Support

## Installing MCUpdater (User)

Just download MCUpdater from [mcupdater.com](https://mcupdater.com/)\!
To add modpacks, press the "+" in the top-left corner and insert the
.XML modpack definition URL provided by the modpack creator. To update a
modpack, press "Update"; keep in mind launching Minecraft does not
automatically update the modpack.

## Creating a Modpack (The Easy Way)

### Prerequisities

- Publicly accessible HTTP web server,
- Familiarity with the terminal or command line, mostly\! No GUIs
  here, sorry.

### Steps

1. Prepare your modpack directory (config/, mods/, ...)
2. Download
   [MCU-FastPack-latest.jar](http://files.mcupdater.com/MCU-FastPack-latest.jar)
3. Run the following command: `java -jar MCU-FastPack-latest.jar --path
   MODPACK_PATH --baseURL HTTP_WEB_SERVER_URL --mc MC_VERSION --fabric
   //FABRIC_VERSION// --out XML_FILE`, where:
   - *MODPACK\_PATH* is the location of the modpack files created in
     the first step,
   - *HTTP\_WEB\_SERVER\_URL* is the upload location URL (for
     instance, <http://example.com/modpack/>),
   - *MC\_VERSION* is the Minecraft version (for instance, 18w50a),
   - *FABRIC\_VERSION* is the Fabric Loader version (for instance,
     0.3.6.107 - check with the site to find the latest\!),
   - *XML\_FILE* is the output XML filename.
4. Upload the modpack files created in the first step as to make them
   visible beneath *HTTP\_WEB\_SERVER\_URL*,
5. Upload the XML file to your HTTP web server URL - the location and
   filename does not matter other than for linking users. If you want
   to update the modpack later, just replace that XML file in the same
   location\!

You're done\! You now have an MCUpdater Fabric modpack.

## Creating a Modpack (The Hard Way)

### Prerequisities

- Publicly accessible HTTP web server,
- Familiarity with the terminal or command line, mostly\! No GUIs
  here, sorry.

### Steps

1. Prepare your modpack directory (config/, mods/, ...)
2. Download
   [MCU-FastPack-latest.jar](http://files.mcupdater.com/MCU-FastPack-latest.jar)
3. Run the following command: `java -jar MCU-FastPack-latest.jar --path
   MODPACK_PATH --baseURL HTTP_WEB_SERVER_URL --mc MC_VERSION --out
   XML_FILE`, where:
   - *MODPACK\_PATH* is the location of the modpack files created in
     the first step,
   - *HTTP\_WEB\_SERVER\_URL* is the upload location URL (for
     instance, <http://example.com/modpack/>),
   - *MC\_VERSION* is the Minecraft version (for instance, 18w50a),
   - *XML\_FILE* is the output XML filename. You will edit and upload
     this in later steps.
4. Visit <https://fabricmc.net/use>, select the desired Fabric loader
   versions and click "copy MCUpdater \<Import\> Entry".
5. Open the XML file in your favourite editor of choice and perform two
   changes (example provided below):
   1. Add the entry copied from the previous step below \<Server...\>:
      it will look like \<Import url="..."\>fabric\</Import\>.
   2. Add the following attribute to the \<Server\> element:
      mainClass="net.fabricmc.loader.launch.knot.KnotClient".
6. Upload the modpack files created in the first step as to make them
   visible beneath *HTTP\_WEB\_SERVER\_URL*,
7. Upload the XML file to your HTTP web server URL - the location and
   filename does not matter other than for linking users. If you want
   to update the modpack later, just replace that XML file in the same
   location\!

You're done\! You now have an MCUpdater Fabric modpack.

![](../images/modpack/mcupdater_xml_additions.png)

## Creating a Branded/Customized Launcher

A full guide is provided [here](https://github.com/MCUpdater/workspace).

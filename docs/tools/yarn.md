# Yarn

See [readme](https://github.com/FabricMC/yarn/#yarn) for more
information.

Yarn depends on [enigma](../enigma.md) and [stitch](../stitch.md).

## Usage

See [yarn](../Documentation/yarn.md).

### Contributing mappings

Run

    gradlew yarn

and the enigma user interface will appear. Edit the mappings; when you
are ready, save it in enigma, commit in git (preferably in a custom
branch), and submit a pull request.

If you have changes done manually/from github suggestions, merge the
remote branch, close enigma, and run the command again so that enigma
can have the updated mapping contents.

### Using mappings in mods

See [mappings](../Documentation/mappings.md).

### Building mapped jars for decompilers

Though enigma comes with procyon, you may want other decompilers to
decompile the Minecraft jar with yarn names. Run

    gradlew mapNamedJar

After running the gradle task, a &lt;version&gt;-named.jar will be
available, which you can put into your desired decompiler.

# Enigma

[Enigma](https://github.com/FabricMC/Enigma) is a GUI tool to
deobfuscate Java Applications. It is backed by the [procyon](../procyon.md)
decompiler.

Enigma is originally authored by cuchaz; FabricMC has maintained a fork
of it since 2016, and has since been using it for [yarn](../yarn.md)
development.

## Usage

See [enigma](../Documentation/enigma.md) documentation.

### Graphical User Interface

Open enigma jar by double clicking it. In the file menu, select "open
jar" to choose the jar to map; then select "open mappings" to select the
mappings to continue working on (or just start mapping if you are
starting fresh).

When you want to save your work, you can go to save option in the file
menu and save your mappings.

#### Advanced

Certain features of enigma, such as custom services, profiles, etc. are
only available through command line. Hence, [yarn](../yarn.md)'s enigma can
only be launched properly from the gradle task because of its usage of
advanced features.

### Command Line Interface

A few commands are available in the main class

    cuchaz.enigma.CommandMain

They are usually used for building [yarn](../yarn.md) or working with
[stitch](../stitch.md) and has since replaced the functionality of
[weave](../weave.md).

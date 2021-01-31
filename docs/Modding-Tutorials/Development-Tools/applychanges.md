# Applying changes without restarting Minecraft

Restarting Minecraft can take a hefty amount of time. Thankfully, there
are tools that allow you to apply some changes while the game is
running.

### Reload changed classes

In Eclipse or Intellij IDEA, run Minecraft in debug mode. To apply
changes in code, do `Run -> Reload Changed Classes` in Intellij IDEA or
save in Eclipse. Note: this only allows you to change method bodies. If
you do any other kind of change, You will have to restart. However, if
you use [DCEVM](http://dcevm.github.io/), you will be able to do most
changes, including adding and removing methods and classes.

### Reload assets

After you make changes to assets such as textures and block/item models,
you can reload changed classes and press `F3 + T` to apply changes
without restarting Minecraft. More specifically, this is how to reload
anything the mod provides as a resource pack.

### Reload data

You can apply any changes made in the `data/` directory such as recipes,
loot tables and tags by reloading changed classes and then using the
in-game command `/reload`. More specifically, this reloads anything the
mod provides as a data pack.

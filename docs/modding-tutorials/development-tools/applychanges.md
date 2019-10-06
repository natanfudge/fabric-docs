# Applying changes without restarting Minecraft

Restarting Minecraft can take a hefty amount of time. Thankfully, there are tools that allow you to apply some changes while the game is running.

## Reload changed classes

In Eclipse or Intellij IDEA, run Minecraft in debug mode. To apply changes in code, do `run -> reload changes classes` in Intellij or save in Eclipse. Note: this only allows you to change method bodies. If you do any other kind of change, You will have to restart. However, if you use [DCEVM](http://dcevm.github.io/), you will be able to do most changes, including adding and removing methods and classes.

## Reload textures

After you make a change in a texture \(`.png`\) asset, you can reload changed classes and press `F3 + T` to apply the change without restarting Minecraft.

## Reload recipes and loot tables

You can apply any change you made in the `data/` directory \(including recipes and loot tables\) by reloading changed classes and then using the Minecraft command `/reload`.


# Exporting Mixin Classes

When debugging mixins, it's useful to be able to see the finalized
classes with your changes and injects inserted. Mixin provides a flag
that allows for this:

`-Dmixin.debug.export=true`

This should be placed in your VM options. Once your classes have been
loaded, they will appear in `\run\.mixin.out`

![](https://i.imgur.com/d7oKQkg.png)

#### Notes

Some classes may not appear until the game is running (or a world is
loading).

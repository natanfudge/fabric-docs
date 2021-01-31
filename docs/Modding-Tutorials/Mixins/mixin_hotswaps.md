# Hotswapping Mixins

Mixins can't be hot-swapped in a default Fabric environment. To enable
this functionality, you'll have to specify the `-javaagent` flag inside
your VM options.

The only pre-requisite is having a copy of the sponge mixin jar. Fabric
pulls this by default, so you should be able to locate it within your
Gradle cache folder. IDEA users can look at the bottom of their project
view on the left side of the screen to find the library:

![](https://i.imgur.com/fUrhss5.png)

Copy the full path to the jar and open up your run configurations. Under
`Minecraft Client`, expand the VM options tab. You'll need to set the
`-javaagent` flag to the path of the mixin jar:

![](https://i.imgur.com/SuW9MlV.png)

Run your game. You can now reload mixins using the same rules as normal:

- no adding or removing methods
- no changing method parameters
- no adding or removing fields

#### Notes

DCEVM does not seem to support mixin swapping:

- When run on Java 1.8:181, DCEVM and Mixin reloads do not work.
- When run on DCEVM Java 11, you can reload, but the hot swap task
  freezes.

If you're able to get DCEVM and Mixin swapping to work together, let us
know!

# Introduction to Modding with Fabric

This is a quick introduction to some common techniques you can use while
making Fabric mods. To make mods for Minecraft, you'll often have to
interact in non-standard ways with Minecraft's code. While Minecraft has
increasingly become flexible to changes, it's still not inherently built
to be modded.

Unlike other modding APIs, the Fabric Loader does not overwrite
Minecraft's class files to add more functionality. Instead, code is
injected using [the Mixin
library](https://github.com/SpongePowered/Mixin/wiki). However, most of
the time you will not use this library directly.

Mixins can be fragile, and at times can cause conflicts. Therefore, some
common functionality has already been implemented by the Fabric API for
you. If it doesn't exist in the core Fabric API, often it will exist in
a third-party library. In almost every situation it's preferable to use
either the Fabric API or a third party library over implementing a mixin
yourself.

Sometimes though, you don't need any of that. While Minecraft's not made
for modding, it still contains a lot of features you can access without
any additional changes at all.

This article will go over all the ways you can affect Minecraft, in
order of preference.

## Native Minecraft APIs

If Minecraft already lets you do something, don't re-invent the wheel. A
good example of this is the "Registry" class, which lets you add blocks
and items without any modifications to Minecraft's code.

Minecraft also uses JSON data files for various data-driven features.
You can add JSON files to your mod, which are then injected by the
Fabric API. For example, block models and loot tables are implemented
through JSON files.

## The Fabric API

Fabric itself, as installed into a client, is split up into two parts.

1. The Fabric Loader, which loads your mod and calls your entry point.
2. The Fabric API, an optional library that provides some common useful
   APIs.

The API is intentionally kept relatively small, to make porting Fabric
to newer Minecraft versions faster.

You can find out what's included in the Fabric API by looking over [its
source code on GitHub](https://github.com/FabricMC/fabric). The Fabric
API contains a lot of common event hooks and general utilities for
things like networking and rendering.

## Third Party APIs

Because the Fabric API is intentionally kept small and focused, third
party APIs exist to fill in the gaps. Mixins allow any third party
library to affect Minecraft's code in the same way as the core Fabric
API can. You should use these instead of writing your own mixins where
possible to minimize the possibility of conflicts.

You can find an incomplete list of [third party
libraries](../Modding-Tutorials/Development-Tools/libraries.md) on this wiki.

## Mixins

Finally, you can use mixins. Mixins are a powerful feature that lets you
change any of Minecraft's own code. Some mixins can cause conflict but
used responsibly these are key to adding unique behavior to your mod.

Mixins come in a variety of flavors, in rough order of preference:

- Adding Interfaces
- Callback Injectors
- Redirect Injectors
- Overwrites, you should never use these

This is not a complete list, but rather a quick overview. Some mixin
types are omitted here.

### Adding Interfaces

This is probably one of the safest ways to use mixins. New interface
implementations can be added to any Minecraft class. You can then access
the interface by casting the class to it. This doesn't change anything
about the class, it only adds new things, and is therefore very unlikely
to conflict.

One caveat is that the function signature (name + parameter types) you
inject must be unique. So if you use common parameter types, be sure to
give it a very unique name.

### Callback Injectors

Callback injectors let you add callback hooks to existing methods, as
well as on specific method calls within that method. They also let you
intercept and change the return value of a method, and return early.
Callback injects can stack, and are therefore unlikely to cause
conflicts between mods.

### Redirect Injectors

Redirects let you wrap method calls or variable access within a target
method with your own code. Use these very sparingly, a target call or
access can only be redirected once between all mods. If two mods
redirect the same value, that will cause a conflict. Consider callback
injects first.

### Overwrite

Avoid overwrites completely. They replace a method entirely, removing
all existing code and conflicting with any other types of mixins on the
method. They are extremely likely to conflict not just with other mods,
but also with changes to Minecraft itself. You most likely do not need
an overwrite to do what you want to do, please use something else.

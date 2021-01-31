# Creating a custom portal to access your dimension

So, you've made your dimension, registered it, added biomes and filled
it with cool creatures and features.

Lets make a portal that can allow survival players to access it!

## Getting Started

Kyrptonaught has created a very useful library that allows you to easily
create a custom portal to link your dimension to the overworld and other
dimensions. For more infomation, [checkout the library's github
here.](https://github.com/kyrptonaught/customportalapi)

First of all, add the following repository to your `build.gradle` file.

```java
maven { 
    url "https://dl.bintray.com/kyrptonaught/customportalapi" 
}
```

Then add the following dependencies:

```java
modImplementation 'net.kyrptonaught:customportalapi:0.0.1-beta18-1.16'
include 'net.kyrptonaught:customportalapi:0.0.1-beta18-1.16'
```

**Note:** The library only works currently for 1.16, a 1.17 version will
release soon.

## Registering your Portal

To register a basic portal, lets say a Gold Block frame and Flint and
Steel. You can place a simple method in your `ModInitializer`[1] The
portals created using the CustomPortalApi act like vanilla portals, and
can be as big as 23Ã—23.

```java
//  CustomPortalApiRegistry.addPortal(Block frameBlock, Identifier dimID, int r, int g, int b)
CustomPortalApiRegistry.addPortal(Blocks.GOLD_BLOCK, new Identifier("my_mod_id", "my_dimension_id"), 234, 183, 8);
```

Now, this would the following portal (Custom Portals can work in any
dimension!):

![](https://raw.githubusercontent.com/kyrptonaught/customportalapi/main/images/2020-11-15_17.07.38.png)

However, this is limited to the flint and steel. Let say we want to make
a portal with a lava bucket as an ignition source. Easy! This can be
easily done by using a `PortalIgnitionSource`

```java
//  CustomPortalApiRegistry.addPortal(Block frameBlock, PortalIgnitionSource ignitionSource, Identifier dimID, int r, int g, int b) 
CustomPortalApiRegistry.addPortal(Blocks.NETHERITE_BLOCK, PortalIgnitionSource.FluidSource(Fluids.LAVA), new Identifier("my_mod_id", "my_dimension_id"), 51, 52, 49) 
```

Now we have a cool netherite portal that can be lit by using lava and a
golden portal!

![](https://raw.githubusercontent.com/kyrptonaught/customportalapi/main/images/2020-11-15_17.06.44.png)

### Extras

The portal API supports custom portal blocks, allowing you to create
your own textures. Currently it doesn't support horizontal portals, but
it may in the future.

The API also supports the use of events, allowing the portal to not be
ignited by the player, but through a event being invoked in your code.
[See here for more
infomation.](https://github.com/kyrptonaught/customportalapi#:~:text=Lastly%20we%20have%20a%20custom%20source.%20The%20ignitionSourceID%20should%20be%20unique%20to%20prevent%20overlapping.%20The%20identifier%20should%20feature%20your%20modid,%20and%20a%20uniqie%20id.%20This%20is%20a%20completely%20custom%20source%20with%20no%20functionality%20by%20default%20allowing%20you%20to%20get%20as%20creative%20as%20you%20want.%20You%20also%20then%20need%20to%20trigger%20the%20custom%20activation%20attempt,%20when%20desired.%20The%20result%20should%20be%20saved%20for%20use%20in%20your%20activation%20attempt,%20Like%20so:)

[1] Not Client or Server

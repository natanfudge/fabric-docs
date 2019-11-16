# Adding an Item

### Introduction

Adding a basic item is one of the first steps in modding. You're going
to need to create an `Item` object, register it, and give it a texture.
To add additional behavior to the item you will need a custom Item
class. In this tutorial and all future ones, the "tutorialâ€� namespace is
used as a placeholder. If you have a separate modid, feel free to use it
instead.

### Registering an Item

First, create an instance of Item. We'll store it at the top of our
initializer class. The constructor takes in an Item.Settings object,
which is used to set item properties such as the inventory category,
durability, and stack count.

```java
public class ExampleMod implements ModInitializer
{
    // an instance of our new item
    public static final Item FABRIC_ITEM = new Item(new Item.Settings().group(ItemGroup.MISC));
    [...]
}
```

You'll use the vanilla registry system for registering new content. The
basic syntax is `Registry#register(Registry Type, Identifier, Content)`.
Registry types are stored as static fields in the `Registry` object, and
the identifier is what labels your content. Content is an instance of
whatever you're adding. This can be called anywhere as long as it occurs
during initialization.

```java
public class ExampleMod implements ModInitializer
{
    // an instance of our new item
    public static final Item FABRIC_ITEM = new Item(new Item.Settings().group(ItemGroup.MISC));
      
    @Override
    public void onInitialize()
    {
        Registry.register(Registry.ITEM, new Identifier("tutorial", "fabric_item"), FABRIC_ITEM);
    } 
}
```

Your new item has now been added to Minecraft. Run the \`runClient\`
gradle task to see it in action.

![](../images/tutorial/2019-02-17_16.50.44.png)

### Adding Item textures

Registering a texture for an item requires an item model .json file and
a texture image. You're going to need to add these to your resource
directory. The direct path of each is:

```
Item model: .../resources/assets/tutorial/models/item/fabric_item.json
Item texture: .../resources/assets/tutorial/textures/item/fabric_item.png
```

Our example texture can be found
[here](https://i.imgur.com/CqLSMEQ.png).

If you registered your item properly in the first step, your game will
complain about a missing texture file in a fashion similar to this:

```
[Server-Worker-1/WARN]: Unable to load model: 'tutorial:fabric_item#inventory' referenced from: tutorial:fabric_item#inventory: java.io.FileNotFoundException: tutorial:models/item/fabric_item.json
```

It conveniently tells you exactly where it expects your asset\[s\] to be
found-- when in doubt, check the log.

A basic item model template is:

```JavaScript
{
  "parent": "item/generated",
  "textures": {
    "layer0": "tutorial:item/fabric_item"
  }
}
```

The parent of your item changes how it's rendered in the hand and comes
in useful for things like block items in the inventory. "item/handheld"
is used for tools that are held from the bottom left of the texture.
textures/layer0 is the location of your image file.

Final textured result:

![](../images/tutorial/item_texture.png)

### Creating an Item class

To add additional behavior to the item you will need to create an Item
class. The default constructor requires an Item.Settings object.

```java
public class FabricItem extends Item
{
    public FabricItem(Settings settings)
    {
        super(settings);
    }
}
```

A practical use-case for a custom item class would be making the item
play a sound when you click with it:

```java
public class FabricItem extends Item
{
    public FabricItem(Settings settings)
    {
        super(settings);
    }
      
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand)
    {
        playerEntity.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);
        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }
}
```

Replace the old Item object with an instance of your new item class:

```java
public class ExampleMod implements ModInitializer
{
    // an instance of our new item
    public static final FabricItem FABRIC_ITEM = new FabricItem(new Item.Settings().group(ItemGroup.MISC));
    [...]
}
```

If you did everything correctly, using the item should now play a sound.

### What if I want to change the stack size of my item?

For this you would use `maxCount(int size)` inside ItemSettings to
specify the max stack size. Note that if your item is damageable you
cannot specify a maximum stack size or the game will throw a
RuntimeException.

```java
public class ExampleMod implements ModInitializer
{
    // an instance of our new item, where the maximum stack size is 16
    public static final FabricItem FABRIC_ITEM = new FabricItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16));
    [...]
}
```

### Next Steps

[Add your item to your own ItemGroup](../Modding-Tutorials/Items/itemgroup.md).

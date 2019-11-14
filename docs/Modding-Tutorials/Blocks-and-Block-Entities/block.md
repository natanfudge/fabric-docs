# Adding a Block

### Introduction

To add a block to your mod, you will need to register a new instance of
the Block class. For more control over your block, you can create a
custom block class. We'll also look at adding a block model.

### Creating a Block

To start, create an instance of Block in your main mod class. Block's
constructor uses the FabricBlockSettings builder to set up basic
properties of the block, such as hardness and resistance:

```java
public class ExampleMod implements ModInitializer
{
    // an instance of our new block
    public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).build());
    [...]
}
```

### Registering a Block

Registering blocks is the same as registering items. Call
*Registry.register* and pass in the appropriate arguments.

```java
public class ExampleMod implements ModInitializer
{
    // block creation
    [â€¦]
    
    @Override
    public void onInitialize()
    {
        Registry.register(Registry.BLOCK, new Identifier("tutorial", "example_block"), EXAMPLE_BLOCK);
    }
}
```

Your block will *not* be accessible as an item, but it can be seen
in-game by using /setblock \~ \~ \~ tutorial:example\_block.

### Registering a BlockItem

In most cases, you want to be able to place your block using an item. To
do this, you need to register a corresponding BlockItem in the item
registry. You can do this by registering an instance of BlockItem under
Registry.ITEM. The registry name of the item should usually be the same
as the registry name of the block.

```java
public class ExampleMod implements ModInitializer
{
    // block creation
    [â€¦]
    
    @Override
    public void onInitialize()
    {
        // block registration
        [...]
        
        Registry.register(Registry.ITEM, new Identifier("tutorial", "example_block"), new BlockItem(EXAMPLE_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    }
}
```

### Giving your block a model

As you probably have noticed, the block is simply a purple and black
checkerboard pattern in-game. This is Minecraft's way of showing you
that the block has no model. Modeling a block is a little bit more
difficult than modeling an item. You will need three files: A blockstate
file, a block model file, and an item model file if the block has a
BlockItem. Textures are also required if you don't use vanilla ones. The
files should be located here:

    Blockstate: src/main/resources/assets/tutorial/blockstates/example_block.json
    Block Model: src/main/resources/assets/tutorial/models/block/example_block.json
    Item Model: src/main/resources/assets/tutorial/models/item/example_block.json
    Block Texture: src/main/resources/assets/tutorial/textures/block/example_block.png

The blockstate file determines which model that the block should use
depending on it's blockstate. As our block has only one state, the file
is a simple as this:

```JavaScript
{
  "variants": {
    "": { "model": "tutorial:block/example_block" }
  }
}
```

The block model file defines the shape and texture of your block. We
will use block/cube\_all, which will allow us to easily set the same
texture on all sides of the block.

```JavaScript
{
  "parent": "block/cube_all",
  "textures": {
    "all": "tutorial:block/example_block"
  }
}
```

In most cases you want the block to look the same in hand. To do this,
you can make an item file that inherits from the block model file:

```JavaScript
{
  "parent": "tutorial:block/example_block"
}
```

Load up Minecraft and your block should finally have a texture\!

### Adding a block loot table

The block must have a loot table for any items to drop when the block is
broken. Assuming you have created an item for your block and registered
it using the same name as the block, the following file will produce
regular block drops
`src/main/resources/data/wikitut/loot_tables/blocks/example_block.json`.

```JavaScript
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "tutorial:example_block"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:survives_explosion"
        }
      ]
    }
  ]
}
```

When broken in survival mode, the block will now drop an item.

### Creating a Block class

When creating a simple block the above approach works well, but
sometimes you want a *special* block with unique mechanics. We'll create
a separate class that extends Block to do this. The class needs a
constructor that takes in a BlockSettings argument.

```java
public class ExampleBlock extends Block
{
    public ExampleBlock(Settings settings)
    {
        super(settings);
    }
}
```

Just like we did in the item tutorial, you can override methods in the
block class for custom functionality. Say you want your block to be
transparent:

```java
@Environment(EnvType.CLIENT)
public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
}
```

To add this block into the game, replace *new Block* with *new
ExampleBlock* when you register it.

```java
public class ExampleMod implements ModInitializer
{
    // an instance of our new item
    public static final ExampleBlock EXAMPLE_BLOCK = new ExampleBlock(Block.Settings.of(Material.STONE));
    [...]
}
```

Your custom block should now be transparent\!

### Next Steps

[Adding simple state to a block, like ints and
booleans](../Modding-Tutorials/Blocks-and-Block-Entities/blockstate.md).

[Giving blocks a block entity so they can have advanced state like
inventories and classes](../Modding-Tutorials/Blocks-and-Block-Entities/blockentity.md). Also needed for many
things like GUI and custom block rendering.

# Adding a Block

Adding blocks to your mod follows a similar process to [adding an
item](../Modding-Tutorials/Items/item.md). You can create an instance of `Block` or a
custom class, and then register it under `Registry.BLOCK`. You also need
to provide a texture and blockstate/model file to give your block
visuals. For more information on the block model format, view the
[Minecraft Wiki Model page](https://minecraft.gamepedia.com/Model).

## Creating a Block

Start by creating an instance of `Block`. It can be stored at any
location, but we will start at the top of your `ModInitializer`. The
`Block` constructor requires an `AbstractBlock.Settings` instance, which
is a builder for configuring block properties. Fabric provides a
`FabricBlockSettings` builder class with more available options.

```java
public class ExampleMod implements ModInitializer {

    /* Declare and initialize our custom block instance.
       We set our block material to `METAL`, which requires a pickaxe to efficiently break.
       
       `strength` sets both the hardness and the resistance of a block to the same value.
       Hardness determines how long the block takes to break, and resistance determines how strong the block is against blast damage (e.g. explosions).
       Stone has a hardness of 1.5f and a resistance of 6.0f, while Obsidian has a hardness of 50.0f and a resistance of 1200.0f.
       
       You can find the stats of all vanilla blocks in the class `Blocks`, where you can also reference other blocks.
    */
    public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    
    @Override
    public void onInitialize() {
        
    }
}
```

### Registering your Block

Blocks should be registered under the `Registry.BLOCK` registry. Call
*Registry.register* and pass in the appropriate arguments.

```java
public class ExampleMod implements ModInitializer {

    public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    
    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new Identifier("tutorial", "example_block"), EXAMPLE_BLOCK);
    }
}
```

Your custom block will *not* be accessible as an item yet, but it can be
seen in-game by using the command `/setblock tutorial:example_block`.

### Registering an Item for your Block

In most cases, you want to be able to place your block using an item. To
do this, you need to register a corresponding BlockItem in the item
registry. You can do this by registering an instance of BlockItem under
Registry.ITEM. The registry name of the item should usually be the same
as the registry name of the block.

```java
public class ExampleMod implements ModInitializer {

    public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    
    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new Identifier("tutorial", "example_block"), EXAMPLE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("tutorial", "example_block"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
    }
}
```

## Giving your Block Visuals

At this point, your new block will appear as a purple and black
checkerboard pattern in-game. This is Minecraft's way of showing you
that something went wrong while loading the block's assets (or visuals).
A full list of issues will be printed to your log when you run your
client. You will need these files to give your block visuals:

- A blockstate file
- A block model file
- A texture
- An item model file (if the block has an item associated with it).

The files should be located here:

    Blockstate: src/main/resources/assets/tutorial/blockstates/example_block.json
    Block Model: src/main/resources/assets/tutorial/models/block/example_block.json
    Item Model: src/main/resources/assets/tutorial/models/item/example_block.json
    Block Texture: src/main/resources/assets/tutorial/textures/block/example_block.png

The blockstate file determines which model a block should use depending
on its blockstate. Our block doesn't have any potential states, so we
cover everything with `""`.

```JavaScript
{
  "variants": {
    "": { "model": "tutorial:block/example_block" }
  }
}
```

The block model file defines the shape and texture of your block. Our
model will have `block/cube_all`as a parent, which applies the texture
`all` to *all* sides of the block.

```JavaScript
{
  "parent": "block/cube_all",
  "textures": {
    "all": "tutorial:block/example_block"
  }
}
```

In most cases, you will want the block to look the same in item form.
You can make an item model that has the block model file as a parent,
which makes it appear exactly like the block:

```JavaScript
{
  "parent": "tutorial:block/example_block"
}
```

Load up Minecraft and your block should have visuals!

## Configuring Block Drops

To make your block drop items when broken, you will need a *loot table*.
The following file will cause your block to drop its respective item
form when broken:

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

## Creating a Custom Block Class

The above approach works well for simple items but falls short when you
want a block with *unique* mechanics. We'll create a *separate* class
that extends `Block` to do this. The class needs a constructor that
takes in an `AbstractBlock.Settings` argument:

```java
public class ExampleBlock extends Block {

    public ExampleBlock(Settings settings) {
        super(settings);
    }
}
```

You can override methods in the block class for custom functionality.
Here's an implementation of the `onUse` method, which is called when you
right-click the block. We check if the interaction is occurring on the
server, and then send the player a message saying, *"Hello, world!"*

```java
public class ExampleBlock extends Block {

    public ExampleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            player.sendMessage(new LiteralText("Hello, world!"), false);
        }

        return ActionResult.SUCCESS;
    }
}
```

To use your custom block class, replace *new Block* with *new
ExampleBlock*:

```java
public class ExampleMod implements ModInitializer {

    public static final ExampleBlock EXAMPLE_BLOCK = new ExampleBlock(Block.Settings.of(Material.STONE).hardness(4.0f));
    
    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new Identifier("tutorial", "example_block"), EXAMPLE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("tutorial", "example_block"), new BlockItem(EXAMPLE_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    }
}
```

### Custom VoxelShape

When using block models that do not *entirely* fill the block (eg.
Anvil, Slab, Stairs), adjacent blocks hide their faces:

<img src="/tutorial/voxelshape_wrong.png" width="200" />

To fix this, we have to define the `VoxelShape` of the new block:

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1.0f, 0.5f);
    }

Note that the *collision shape* of the block defaults to the outline
shape if it is not specified.

<img src="/tutorial/voxelshape_fixed.png" width="200" />

## Next Steps

[Adding simple state to a block, like ints and
booleans](../Modding-Tutorials/Blocks-and-Block-Entities/blockstate.md).

[Giving blocks a block entity so they can have advanced state like
inventories](../Modding-Tutorials/Blocks-and-Block-Entities/blockentity.md). Also needed for many things like
GUI and custom block rendering.

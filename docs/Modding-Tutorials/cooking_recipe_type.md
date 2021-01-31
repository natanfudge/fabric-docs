**This page needs some revisions**

# Creating a custom cooking recipe type

Creating a cooking recipe type is just like creating a custom recipe
type with extra steps. Vanilla minecraft has abstracts classes for
furnaces and cooking recipe type so we'll use them to save some time and
code! If it is your first time creating a container block you should go
check this tutorial before: [screenhandler](../Modding-Tutorials/screenhandler.md)

The final result of this tutorial can be found here:
<https://github.com/Legorel/CookingRecipeExample>

## Adding the Block and BlockEntity

First we need or furnace, for this our class will extends
`AbstractFurnaceBlock`. Some of the classes will be created later :

```java
// Do not forget that we need every class to extends the abstract furnace classes or it won't work!
public class TestFurnace extends AbstractFurnaceBlock {
    public TestFurnace(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new TestFurnaceBlockEntity();
    }

    @Override
    public void openScreen(World world, BlockPos pos, PlayerEntity player) {
        //This is called by the onUse method inside AbstractFurnaceBlock so
        //it is a little bit different of how you open the screen for normal container
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof TestFurnaceBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
            // Optional: increment player's stat
            player.incrementStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
}
```

Now we need or BlockEntity, some classes will be created later :

```java
public class TestFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    //Since we already now the BlockEntityType and RecipeType we don't need them in the constructor's parameters
    public TestFurnaceBlockEntity() {
        super(CookingRecipeExample.TEST_FURNACE_BLOCK_ENTITY, CookingRecipeExample.TEST_RECIPE_TYPE);
    }

    @Override
    public Text getContainerName() {
        //you should use a translation key instead but this is easier
        return Text.of("test furnace");
    }

    @Override
    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new TestFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
```

Now we need to register or `Block`, `BlockItem` and `BlockEntity` :

```java
public class CookingRecipeExample implements ModInitializer {
public static final String MOD_ID = "cookingrecipeexample";

    public static final Block TEST_FURNACE_BLOCK;
    public static final BlockEntityType TEST_FURNACE_BLOCK_ENTITY;

    static {
        //Block
        TEST_FURNACE_BLOCK = Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "test_furnace"), new TestFurnace(FabricBlockSettings.of(Material.METAL)));
        //BlockItem
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "test_furnace"), new BlockItem(TEST_FURNACE_BLOCK, new Item.Settings().group(ItemGroup.DECORATIONS)));
        //BlockEntity
        TEST_FURNACE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "test_furnace"), BlockEntityType.Builder.create(TestFurnaceBlockEntity::new, TEST_FURNACE_BLOCK).build(null));
    }

    @Override
    public void onInitialize() {  
    }
}
```

## Creating the Recipe class

Now we will create the recipe class, again we need to extend some
vanilla classes :

```java
public class TestRecipe extends AbstractCookingRecipe {
    //Same for the BlockEntity, we don't need some of the parameters in the constructor since we already now the type
    public TestRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(CookingRecipeExample.TEST_RECIPE_TYPE, id, group, input, output, experience, cookTime);
    }

    @Override
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(Items.BLACKSTONE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        //The Serializer will be created later
        return CookingRecipeExample.TEST_RECIPE_SERIALIZER;
    }
}
```

Then we need to register it :

```java
public class CookingRecipeExample implements ModInitializer {
    [...]
    public static final RecipeType<TestRecipe> TEST_RECIPE_TYPE;
    
    static {
        [...]
        TEST_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID, "test_furnace"), new RecipeType<TestRecipe>() {
            @Override
            public String toString() {return "test_furnace";}
        });

    }
}
```

## Creating the recipe serializer

This is where it gets different from a normal recipe type, you can make
your own serializer if your furnace works differently from vanilla one
or just register a new `CookingRecipeSerializer` to save some code :

```java
public class CookingRecipeExample implements ModInitializer {
    [...]
    public static final RecipeSerializer<TestRecipe> TEST_RECIPE_SERIALIZER;
    
    static {
        [...]
        TEST_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "test_furnace"), new CookingRecipeSerializer<>(TestRecipe::new, 200));
    }
}
```

There is just one problem, `CookingRecipeSerializer` uses an interface
`RecipeFactory` that we don't have access to. We could just make our own
serializer but this will mean that we couldn't use the classes like
`AbstractFurnaceBlockEntity` because of some `Generic Types`. To counter
this we will use an `Access Widener` to get access to the interface. The
tutorial on `Access Widener` can be found here :
[accesswideners](../Modding-Tutorials/accesswideners.md)

    AW path : .../src/main/resources/modid.accesswidener

```text
accessWidener v1 named

accessible class net/minecraft/recipe/CookingRecipeSerializer$RecipeFactory
```

Now we need to add our AW to `fabric.mod.json` and `build.gradle`

    fabric.mod.json: .../src/main/resources/fabric.mod.json

```text
{
  [...],
  "accessWidener": "cookingrecipeexample.accesswidener"
}
```

```text
minecraft {
    accessWidener "src/main/resources/cookingrecipeexample.accesswidener"
}
```

After that you need to reload gradle changes and the `RecipeFactory`
interface should now be public !

## Creating the ScreenHandler

Now we need to create our screen handler so the furnace's inventory can
work. We still need to emove some of the constructor's parameters since
we already now their value :

```java
public class TestFurnaceScreenHandler extends AbstractFurnaceScreenHandler {
    public TestFurnaceScreenHandler(int i, PlayerInventory playerInventory) {
        super(CookingRecipeExample.TEST_FURNACE_SCREEN_HANDLER, CookingRecipeExample.TEST_RECIPE_TYPE, RecipeBookCategory.FURNACE, i, playerInventory);
    }

    public TestFurnaceScreenHandler(int i, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(CookingRecipeExample.TEST_FURNACE_SCREEN_HANDLER, CookingRecipeExample.TEST_RECIPE_TYPE, RecipeBookCategory.FURNACE, i, playerInventory, inventory, propertyDelegate);
    }
}
```

Unfortunately, there is no easy way to create a custom recipe book
screen since they are hard-coded in `Enums`. We'll use the furnace
recipe book screen instead.

Then we register it :

```java
public class CookingRecipeExample implements ModInitializer {
    [...]
    public static final ScreenHandlerType<TestFurnaceScreenHandler> TEST_FURNACE_SCREEN_HANDLER;
    
    static {
        [...]
        TEST_FURNACE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "test_furnace"), TestFurnaceScreenHandler::new);
    }
}
```

## Creating the Screen

The last thing we need to do is add the `Screen`, we will need to
register it in the client-side:

```java
@Environment(EnvType.CLIENT)
public class TestFurnaceScreen extends AbstractFurnaceScreen<TestFurnaceScreenHandler> {
    //You can replace the background with whatever you like, just remember there will always be the recipe book button
    private static final Identifier BACKGROUND = new Identifier("textures/gui/container/furnace.png");

    public TestFurnaceScreen(TestFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new FurnaceRecipeBookScreen(), inventory, title, BACKGROUND);
    }
}
```

And we register it:

```java
@Environment(EnvType.CLIENT)
public class CookingRecipeExampleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(CookingRecipeExample.TEST_FURNACE_SCREEN_HANDLER, TestFurnaceScreen::new);
    }
}
```

Then we add our `ClientModInitializer` in the `fabric.mod.json` file,
just change the package to yours:

```javascript
"entrypoints": {
  "main": [   [...]
  ],
  "client": [
    "fr.legorel.cookingrecipeexample.client.CookingRecipeExampleClient"
  ],
  "server": []
},
```

## Adding a recipe

Now it is the time to test our recipe type, before continuing you should
make sure that every class we created is imported in the classes where
they are used. To create our recipe we simply follow the official way:
<https://minecraft.gamepedia.com/Recipe#JSON_format>

    Recipe path: .../src/main/resources/data/modid/recipes/test_recipe.json

```javascript
{
  "type": "cookingrecipeexample:test_furnace",
  "ingredient": {
    "item": "minecraft:stone"
  },
  "result": "minecraft:diamond",
  "experience": 0.2
}
```

And finally, we can run our client and test our new furnace and recipe
type:

<img src="/recipe_type_test.png" class="align-center" alt="Custom recipe type test" />

## Next step

We have our recipe type, our furnace, but no textures for our block. You
can learn how to add visuals to your block here :
[blocks\#giving\_your\_block\_visuals](../Modding-Tutorials/Blocks-and-Block-Entities/block.md#giving_your_block_visuals)

But don't forget that we extended `AbstractFurnaceBlock`, this mean our
block already have 2 `BlockState`, one for the direction it is facing
and the other for when it is lit or not. You can find a tutorial on
`BlockStates` here:
[blockstate\#adding\_models\_for\_your\_blockstates](../Modding-Tutorials/Blocks-and-Block-Entities/blockstate.md#adding_models_for_your_blockstates)

Just in case, i'll put the furnace blockstates file as an example:

```javascript
{
  "variants": {
    "facing=east,lit=false": {
      "model": "minecraft:block/furnace",
      "y": 90
    },
    "facing=east,lit=true": {
      "model": "minecraft:block/furnace_on",
      "y": 90
    },
    "facing=north,lit=false": {
      "model": "minecraft:block/furnace"
    },
    "facing=north,lit=true": {
      "model": "minecraft:block/furnace_on"
    },
    "facing=south,lit=false": {
      "model": "minecraft:block/furnace",
      "y": 180
    },
    "facing=south,lit=true": {
      "model": "minecraft:block/furnace_on",
      "y": 180
    },
    "facing=west,lit=false": {
      "model": "minecraft:block/furnace",
      "y": 270
    },
    "facing=west,lit=true": {
      "model": "minecraft:block/furnace_on",
      "y": 270
    }
  }
}
```


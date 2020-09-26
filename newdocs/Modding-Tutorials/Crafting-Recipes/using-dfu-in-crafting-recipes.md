# Defining Custom Crafting Recipes and deserializing them using Codecs

Minecraft already has some predefined crafting recipe types: shaped crafting, shapeless crafting, furnace crafting and others. You can use the same system to add crafting recipes types of your own. In this tutorial we will enable creating crafting recipes that accept 2 items and output an `ItemStack`.

Defining custom crafting recipes has a couple of steps. It is lengthy but fairly straight-forward.

## Step 1:  Implement Recipe

Every recipe defined by the user needs to become a single `Recipe` instance, which defines everything Minecraft needs to know about individual recipes.

The `Recipe` interface expects an `Inventory` class as a type parameter. Usually you want this to be the inventory that contains the items for the recipe, for example in a shaped crafting recipe this is the inventory of the crafting table. For simplicity will use the built-in `SimpleInventory`. Start off by creating a new class that implements `Recipe<Your-Inventory-Class>` and let your editor generate stubs for the methods you need to implement, or copy this:

```java
public class ExampleRecipe implements Recipe<Inventory> {
    @Override
    public boolean matches(Inventory inventory, World world) {
        return false;
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return null;
    }

    @Override
    public Identifier getId() {
        return null;
    }

    @Override
    public RecipeSerializer<? extends ExampleRecipe> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<? extends ExampleRecipe> getType() {
        return null;
    }
}

```

Right off the bat we don't need to care about `fits` and `craft`, as they are not really needed. In `craft` it's a good idea to return `ItemStack.EMPTY` to prevent any surprise `NullPointerException`s, `fits` can be left as-is.
```java
public class ExampleRecipe implements Recipe<Inventory> {
   //[...]   
    @Override
    public ItemStack craft(Inventory inv) {
        return ItemStack.EMPTY;
    }
}
```

Now we need to define what makes up the recipe, in Minecraft terms. Every recipe needs to have an `Identifier`. We also need an `ItemStack` to be outputted.

```java
public class ExampleRecipe implements Recipe<Inventory> {
    public static final Identifier ID = new Identifier("example:two_slot_recipe");
    private final Item input1;
    private final Item input2;
    private final ItemStack output;
    
    public ExampleRecipe(Item input1, Item input2, ItemStack output) {
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
    }

    public Item getInput1() {
        return this.input1;
    }
    
    public Item getInput2() {
        return this.input2;
    }
   // [...]
}
```

Now we can start properly implementing the other methods.

In `matches` we need to say if a given inventory satisfies a recipe's input. In this example we'll return true if the first slot matches the first input, and the second slot matches the second input. Note that `method_8093` says whether or not an `ItemStack` satisfies an item, it is simply not named because of some limitation.

```java
public class ExampleRecipe implements Recipe<Inventory> {
   // [...]   
    @Override
    public boolean matches(Inventory inventory, World world) {
        if (inventory.size() < 2) {
            return false;
        }
        return this.input1.equals(inventory.getStack(0).getItem()) && this.input2.equals(inventory.getStack(1).getItem());
    }

}
```

`getOutput()` and `getId` are pretty self-explanatory.

```java
public class ExampleRecipe implements Recipe<SimpleInventory> {    
    // [...]
    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
```

The `Type` of the recipe is not too important, we just need an instance of a class that implements `RecipeType<Your-Recipe>`:

```java
public class ExampleMod implements ModInitializer {
    public static final RecipeType<ExampleRecipe> RECIPE_TYPE = RecipeType.register("example:recipe_type");
    
    //[...]
}
```
```java
public class ExampleRecipe implements Recipe<Inventory> {      
    // [...]
    public static final 
    @Override
    public RecipeType<?> getType() {
        return ExampleMod.RECIPE_TYPE;
    }
}
```

For `getSerializer()` we will need a `RecipeSerializer` for our recipe, which we will create in step 4.

## Step 2: Create a Codec for your item
Instances of crafting recipes are made using JSON files. Minecraft defines its own format for the JSON different types of crafting recipes. In our example, we will need **the first input item**, **the second input item** and an **item stack of the output item**, to be part of each individual recipe. The Codec should looks like this to match the constructor of the Recipe:

```java
public class ExampleRecipe implements Recipe<Inventory> {
    public static final Codec<ExampleRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Registry.ITEM.fieldOf("input1").forGetter(ExampleRecipe::getInput1),
                Registry.ITEM.fieldOf("input2").forGetter(ExampleRecipe::getInput2),
                ItemStack.CODEC.fieldOf("result").forGetter(ExampleRecipe::getOutput)
        ).apply(instance, ExampleRecipe::new));
}
```

## Step 3: Implement RecipeSerializer

A `RecipeSerializer` does two things:

1. Turns JSON into `Recipe` for Minecraft to load recipe JSON.
2. Turns `PacketByteBuf` to `Recipe`, and `Recipe` into `PacketByteBuf` for Minecraft to sync the recipe through the network.

Start off with a stub implementation of `RecipeSerializer<ExampleRecipe>`, just like in `Recipe`. This example will be using an `enum` type to ensure their singleton status.

```java
public enum ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe> {
    INSTANCE;

    @Override
    // Turns json into Recipe
    public ExampleRecipe read(Identifier id, JsonObject json) {
        return null;
    }
    @Override
    // Turns Recipe into PacketByteBuf
    public void write(PacketByteBuf packetData, ExampleRecipe recipe) {
    }

    @Override
    // Turns PacketByteBuf into Recipe
    public ExampleRecipe read(Identifier id, PacketByteBuf packetData) {
        return null;
    }
}
```

First off we'll implement JSON deserializing. DataFixerUpper can convert the json object into an instance of the recipe class using the Codec that was made in step 2. 

```java
public enum ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe> {
    INSTANCE;
    // [...]      

    @Override
    // Turns json into Recipe
    public ExampleRecipe read(Identifier id, JsonObject json) {
        return ExampleRecipe.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow(false, System.err::println).getFirst();
    }
    // [...]
}
```
What the above code does is:
- `decode` attempts to deserialize the `JsonObject` into an `ExampleRecipe`, which returns a `DataResult`
- `getOrThrow` gets the object that the `DataResult` is holding, which is a `Pair<ExampleRecipe, JsonElement>`. This throws a `RuntimeException` if the `DataResult` is not successful and does not contain a full value.
- `getFirst` returns the first object present in the pair, which, in our case, is an instance of `ExampleRecipe`


Implementing `write` and the other `read` is fairly simple. We just write into the buf what composes the recipe, and then read out of the buf what composes the recipe:

```java
public enum ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe> {
    INSTANCE;
    // [...]  
    @Override
    public ExampleRecipe read(Identifier id, PacketByteBuf buf) {
        Item input1 = Registry.ITEM.get(buf.readIdentifier());
        Item input2 = Registry.ITEM.get(buf.readIdentifier());
        ItemStack output = buf.readItemStack();
        return new ExampleRecipe(input1, input2, output);
    }
    
    @Override
    public void write(PacketByteBuf buf, ExampleRecipe recipe) {
        buf.writeIdentifier(Registry.ITEM.getId(recipe.getInput1()));
        buf.writeIdentifier(Registry.ITEM.getId(recipe.getInput2()));
        buf.writeItemStack(recipe.getOutput());
    }
}
```

Now we can finally complete the `Recipe` implementation:

```java
public class ExampleRecipe implements Recipe<SimpleInventory> {
    // [...]
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ExampleRecipeSerializer.INSTANCE;
    }
}
```

## Step 4: Register your RecipeSerializer

In the common mod initializer:

```java
public class ExampleMod implements ModInitializer {
    // [...]
    @Override
    public void onInitialize() {
        // [...]
        Registry.register(Registry.RECIPE_SERIALIZER, ExampleRecipe.ID, ExampleRecipeSerializer.INSTANCE);
    }
}
```

## Step 5: Match an inventory with your recipe and craft

In our example we'll try to craft when the player right clicks a block, using the items in his main hand and offhand. Find a recipe that matches an inventory by using the world's recipe manager's `getFirstMatch` method:

```java
public class MyBlock extends Block { 
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        // Something that gives the player items should always go through the server.
        // If you need to notify the client in some way, check in the server and then send a packet to the client.
        if (!world.isClient()) {
            // For the sake of simplicity we draw the items off of the player's hands and create an inventory from that.
            // Usually you use an inventory of yours instead.
            SimpleInventory inventory = new SimpleInventory(player.getMainHandStack(), player.getOffHandStack());
    
            // Or use .getAllMatches if you want all of the matches
            Optional<ExampleRecipe> match = world.getRecipeManager().getFirstMatch(ExampleMod.RECIPE_TYPE, inventory, world);
    
            if (match.isPresent()) {
                // Give the player the item and remove from what he has. Make sure to copy the ItemStack to not ruin it!
                player.inventory.offerOrDrop(world, match.get().getOutput().copy());
                player.getMainHandStack().decrement(1);
                player.getOffHandStack().decrement(1);
                return ActionResult.SUCCESS;
            } else {
                // If it doesn't match we tell the player
                player.sendMessage(new LiteralText("No match!"), false);
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }
}
```

## Step 6: Create a recipe JSON with your newly created recipe format to test it out

In the same place you put normal recipes \(data/modid/recipes\) put some recipe JSON. Make sure to put in the `type` field the same value as the ID of the `RecipeSerializer`.   
Using a codec allows us to generate the recipe json with some code. To gain an idea on how the item will look, encode an instance of your recipe in your mod initializer. 
```java
public class ExampleMod implements ModInitializer {
    public void onInitialize() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Damage", 16);
        ItemStack output = new ItemStack(Items.GOLDEN_PICKAXE, 1);
        output.setTag(tag);
        ExampleRecipe recipe = new ExampleRecipe(Items.DIRT, Items.EGG, output);
        String json = ExampleRecipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow(false, System.err::println).toString();
        System.out.println(json);
    }
}
```
This will print out the following in the console.  
`{"input1":"minecraft:dirt","input2":"minecraft:egg","result":{"id":"minecraft:golden_pickaxe","Count":1,"tag":{"Damage":16}}}`

which, after some formatting, leads us to
```json
{
  "input1": "minecraft:dirt",
  "input2": "minecraft:egg",
  "result": {
    "id": "minecraft:golden_pickaxe",
    "Count": 1,
    "tag": {
       "Damage": 16
    }
  }
}
```

And you're done! Most of the steps described here you should be able to get through pretty quickly. Usually the most complicated part is implementing the `matches` method in `Recipe`.


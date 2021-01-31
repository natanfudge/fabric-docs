# Defining Custom Crafting Recipes

Minecraft already has some predefined crafting recipe types: shaped crafting, shapeless crafting, furnace crafting and others. You can use the same system to add crafting recipes types of your own. In this tutorial we will enable creating crafting recipes that accept 2 items and output an `ItemStack`.

Defining custom crafting recipes has a couple of steps. It is lengthy but fairly straight-forward.

## Step 1:  Implement Recipe

Every recipe defined by the user needs to become a single `Recipe` instance, which defines everything Minecraft needs to know about individual recipes.

The `Recipe` interface expects an `Inventory` class as a type parameter. Usually you want this to be the inventory that contains the ingredients for the recipe, for example in a shaped crafting recipe this is the inventory of the crafting table. For simplicity will use the built-in `BasicInventory`. Start off by creating a new class that implements `Recipe<Your-Inventory-Class>` and let your editor generate stubs for the methods you need to implement, or copy this:

```java
public class ExampleRecipe implements Recipe<BasicInventory> {
    @Override
    public boolean matches(BasicInventory inventory, World world) {
        return false;
    }
    @Override
    public ItemStack craft(BasicInventory inventory) {
        return null;
    }
    @Override
    public boolean fits(int var1, int var2) {
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
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
    @Override
    public RecipeType<?> getType() {
        return null;
    }
}
```

Right off the bat we don't need to care about `fits` and `craft`, as they are not really needed. In `craft` it's a good idea to return `ItemStack.Empty` to prevent any surprise NPE's, `fits` can be left as-is.

```java
public class ExampleRecipe implements Recipe<BasicInventory> {
   //[...]   
    @Override
    public ItemStack craft(BasicInventory inventory) {
        return ItemStack.EMPTY;
    }
}
```

Now we need to define what makes up the recipe, in Minecraft terms. Every recipe needs to have an `Identifier`. We also need an `ItemStack` to be outputted. For the inputs, you should **always** use `Ingredient`, so you can support tags:

```java
public class ExampleRecipe implements Recipe<BasicInventory> {
    private final Ingredient inputA;
    private final Ingredient inputB;
    private final ItemStack outputStack;
    private final Identifier id;

    public ExampleRecipe(Ingredient inputA, Ingredient inputB, ItemStack outputStack, Identifier id) {
        this.inputA = inputA;
        this.inputB = inputB;
        this.outputStack = outputStack;
        this.id = id;
    }

    public Ingredient getInputA() {
        return inputA;
    }

    public Ingredient getInputB() {
        return inputB;
    }
   // [...]
}
```

Now we can start properly implementing the other methods.

In `matches` we need to say if a given inventory satisfies a recipe's input. In this example we'll return true if the first slot matches the first input, and the second slot matches the second input. Note that `method_8093` says whether or not an `ItemStack` satisfies an ingredient, it is simply not named because of some limitation.

```java
public class ExampleRecipe implements Recipe<BasicInventory> {
   // [...]   
    @Override
    public boolean matches(BasicInventory inventory, World world) {
        if (inventory.getInvSize() < 2) return false;
        return inputA.method_8093(inventory.getInvStack(0)) && inputB.method_8093(inventory.getInvStack(1));
    }
}
```

`getOutput()` and `getId` are pretty self-explanatory.

```java
public class ExampleRecipe implements Recipe<BasicInventory> {    
    // [...]
    @Override
    public ItemStack getOutput() {
        return outputStack;
    }

    @Override
    public Identifier getId() {
        return id;
    }
}
```

The `Type` of the recipe is not too important, we just need an instance of a class that implements `RecipeType<Your-Recipe>`:

```java
public class ExampleRecipe implements Recipe<BasicInventory> {      
    // [...]
    public static class Type implements RecipeType<ExampleRecipe> {
        // Define ExampleRecipe.Type as a singleton by making its constructor private and exposing an instance.
        private Type() {}
        public static final Type INSTANCE = new Type();

        // This will be needed in step 4
        public static final String ID = "two_slot_recipe";
    }
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
}
```

For `getSerializer()` we will need a `RecipeSerializer` for our recipe, which we will create in step 4.

## Step 2: Think about how your recipes will look like in JSON.

As you know, instances of crafting recipes are made using JSON files. Minecraft defines it's own format for the JSON different types of crafting recipes. In our example, we will need **the first input ingredient**, **the second input ingredient**, **the type of item that will be outputted**, and **how much will be outputted**, to be part of each individual recipe. That makes a format that looks like this natural:

```javascript
{
  "inputA" : {
    "item": "minecraft:redstone"
  },
  "inputB": {
    "tag": "minecraft:logs"
  },
  "outputItem": "minecraft:cobblestone",
  "outputAmount": "20"
}
```

To match with this format, we will define a Java class with the same attributes \(add visibility modifiers and getters/setters if you want to\):

```java
    class ExampleRecipeJsonFormat {
        JsonObject inputA;
        JsonObject inputB;
        String outputItem;
        int outputAmount;
    }
```

The reason `inputA` and `inputB` are `JsonObject`s is because Minecraft has handy methods for turning `JsonObject`s into `Ingredient`s. This class will become very useful in step 3.

## Step 3: Implement RecipeSerializer

A `RecipeSerializer` does two things:

1. Turns JSON into `Recipe` for Minecraft to load recipe JSON.
2. Turns `PacketByteBuf` to `Recipe`, and `Recipe` into `PacketByteBuf` for Minecraft to sync the recipe through the network.

Start off with a stub implementation of `RecipeSerializer<Your-Recipe-Class>`, just like in `Recipe`:

```java
public class ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe>{
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

First off we'll implement JSON =&gt; Recipe. Remember the `ExampleRecipeJsonFormat` class we made? We can use Gson, a library that is bundled with Minecraft to 'magically' turn JSON into an instance of that class, as long as the names of the fields match.

```java
public class ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe>{
    @Override
    // Turns json into Recipe
    public ExampleRecipe read(Identifier id, JsonObject json) {
        ExampleRecipeJsonFormat recipeJson = new Gson().fromJson(json, ExampleRecipeJsonFormat.class);
    }
    // [...]
}
```

Now we will transform that instance into a `Recipe`. \(Note: This snippet has unsafe code, which we will fix up in the next snippet\)

```java
public class ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe> {    
    @Override
    public ExampleRecipe read(Identifier recipeId, JsonObject json) {
        ExampleRecipeJsonFormat recipeJson = new Gson().fromJson(json, ExampleRecipeJsonFormat.class);

        // Ingredient easily turns JsonObjects of the correct format into Ingredients
        Ingredient inputA = Ingredient.fromJson(recipeJson.inputA);
        Ingredient inputB = Ingredient.fromJson(recipeJson.inputB);
        // The json will specify the item ID. We can get the Item instance based off of that from the Item registry.
        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem)).get();
        ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);

        return new ExampleRecipe(inputA, inputB, output, recipeId);
    }
    // [...]
}
```

That code is very naïve, assuming the user will input perfect JSON. Let's add some validation to take care of user error:

```java
public class ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe> {    
    @Override
    public ExampleRecipe read(Identifier recipeId, JsonObject json) {
        ExampleRecipeJsonFormat recipeJson = new Gson().fromJson(json, ExampleRecipeJsonFormat.class);
        // Validate all fields are there
if (recipeJson.inputA == null || recipeJson.inputB == null || recipeJson.outputItem == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }
        // We'll allow to not specify the output, and default it to 1.
        if (recipeJson.outputAmount == 0) recipeJson.outputAmount = 1;

        Ingredient inputA = Ingredient.fromJson(recipeJson.inputA);
        Ingredient inputB = Ingredient.fromJson(recipeJson.inputB);
        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem))
            // Validate the inputted item actually exists
            .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.outputItem));
        ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);

        return new ExampleRecipe(inputA, inputB, output, recipeId);
    }
  // [...]
}
```

Implementing `write` and the other `read` is fairly simple. We just write into the buf what composes the recipe, and then read out of the buf what composes the recipe:

```java
public class ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe> {    
    // [...]  
    @Override
    public void write(PacketByteBuf packetData, ExampleRecipe recipe) {
        recipe.getInputA().write(packetData);
        recipe.getInputB().write(packetData);
        packetData.writeItemStack(recipe.getOutput());
    }

    @Override
    public ExampleRecipe read(Identifier recipeId, PacketByteBuf packetData) {
        // Make sure the read in the same order you have written!
        Ingredient inputA = Ingredient.fromPacket(packetData);
        Ingredient inputB = Ingredient.fromPacket(packetData);
        ItemStack output = packetData.readItemStack();
        return new ExampleRecipe(inputA, inputB, output, recipeId);
    }
}
```

Now we need an instance of our serializer we can use in our `Recipe` implementation, and an ID we will use in step 4.

```java
public class ExampleRecipeSerializer implements RecipeSerializer<ExampleRecipe> {
    // Define ExampleRecipeSerializer as a singleton by making its constructor private and exposing an instance.
    private ExampleRecipeSerializer() {
    }

    public static final ExampleRecipeSerializer INSTANCE = new ExampleRecipeSerializer();

    // This will be the "type" field in the json
    public static final Identifier ID = new Identifier("example:example_recipe");

   // [...]
}
```

Now we can finally complete the `Recipe` implementation:

```java
public class ExampleRecipe implements Recipe<BasicInventory> {
    // [...]
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ExampleRecipeSerializer.INSTANCE;
    }
}
```

## Step 4: Register your  RecipeSerializer and RecipeType

In the common mod initializer:

```java
public class ExampleMod implements ModInitializer {
    // [...]
    @Override
    public void onInitialize() {
        // [...]
        Registry.register(Registry.RECIPE_SERIALIZER, ExampleRecipeSerializer.ID,
                ExampleRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier("example", ExampleRecipe.Type.ID), ExampleRecipe.Type.INSTANCE);
    }
}
```

## Step 5: Match an inventory with your recipe and craft

In our example we'll try to craft when the player right clicks a block, using the items in his main hand and offhand. Find a recipe that matches an inventory by using the world's recipe manager's `getFirstMatch` method:

```java
public class MyBlock extends Block { 
 @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        // Something that gives the player items should always go through the server.
        // If you need to notify the client in some way, check in the server and then send a packet to the client.
        if (!world.isClient()) {
            // For the sake of simplicity we draw the items off of the player's hands and create an inventory from that.
            // Usually you use an inventory of yours instead.
            BasicInventory inventory = new BasicInventory(player.getMainHandStack(), player.getOffHandStack());

            // Or use .getAllMatches if you want all of the matches
            Optional<ExampleRecipe> match = world.getRecipeManager()
                    .getFirstMatch(ExampleRecipe.Type.INSTANCE, inventory, world);

            if (match.isPresent()) {
                // Give the player the item and remove from what he has. Make sure to copy the ItemStack to not ruin it!
                player.inventory.offerOrDrop(world, match.get().getOutput().copy());
                player.getMainHandStack().decrement(1);
                player.getOffHandStack().decrement(1);
            } else {
                // If it doesn't match we tell the player
                player.sendMessage(new LiteralText("No match!"));
            }
        }

        return true;
    }
}
```

## Step 6: Create a recipe JSON with your newly created recipe format to test it out

In the same place you put normal recipes \(data/modid/recipes\) put some recipe JSON that matches your format. Make sure to put in the `type` field the same value as the ID of the `RecipeSerializer`:

```javascript
{
  "inputA" : {
    "item": "minecraft:redstone"
  },
  "inputB": {
    "tag": "minecraft:logs"
  },
  "outputItem": "minecraft:cobblestone",
  "outputAmount": "20"
}
```

And you're done! Most of the steps described here you should be able to get through pretty quickly. Usually the most complicated part is implementing the `matches` method in `Recipe`.


# Dynamic Recipe Registration

Dynamically added recipes are recipes added through code instead of
.json files. This can be used for, for example, changing a recipe if a
certain mod is installed alongside your mod, or changing the recipe to
use tags from another mod.

To start off, we want to have some sort of function that will create a
Json Object for our custom recipe.

```java
    public static JsonObject createShapedRecipeJson(ArrayList<Character> keys, ArrayList<Identifier> items, ArrayList<String> type, ArrayList<String> pattern, Identifier output) {
        //Creating a new json object, where we will store our recipe.
        JsonObject json = new JsonObject();
        //The "type" of the recipe we are creating. In this case, a shaped recipe.
        json.addProperty("type", "minecraft:crafting_shaped");
        //This creates:
        //"type": "minecraft:crafting_shaped"

        //We create a new Json Element, and add our crafting pattern to it.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(pattern.get(0));
        jsonArray.add(pattern.get(1));
        jsonArray.add(pattern.get(2));
        //Then we add the pattern to our json object.
        json.add("pattern", jsonArray);
        //This creates:
        //"pattern": [
        //  "###",
        //  " | ",
        //  " | "
        //]

        //Next we need to define what the keys in the pattern are. For this we need different JsonObjects per key definition, and one main JsonObject that will contain all of the defined keys.
        JsonObject individualKey; //Individual key
        JsonObject keyList = new JsonObject(); //The main key object, containing all the keys

        for (int i = 0; i < keys.size(); ++i) {
            individualKey = new JsonObject();
            individualKey.addProperty(type.get(i), items.get(i).toString()); //This will create a key in the form "type": "input", where type is either "item" or "tag", and input is our input item.
            keyList.add(keys.get(i) + "", individualKey); //Then we add this key to the main key object.
            //This will add:
            //"#": { "tag": "c:copper_ingots" }
            //and after that
            //"|": { "item": "minecraft:sticks" }
            //and so on.
        }

        json.add("key", keyList);
        //And so we get:
        //"key": {
        //  "#": {
        //    "tag": "c:copper_ingots"
        //  },
        //  "|": {
        //    "item": "minecraft:stick"
        //  }
        //},

        //Finally, we define our result object
        JsonObject result = new JsonObject();
        result.addProperty("item", output.toString());
        result.addProperty("count", 1);
        json.add("result", result);
        //This creates:
        //"result": {
        //  "item": "modid:copper_pickaxe",
        //  "count": 1
        //}

        return json;
    }
```

### Main Mod File

First off, we want to check if a certain mod is loaded. To do that, we
check using

```java
FabricLoader.getInstance().isModLoaded("custom_mod");
```

Where "custom\_mod" is the name of the mod we want to check for. If the
mod is loaded, we can create our recipe.

```java
    public class ExampleMod implements ModInitializer {
    
        public static JsonObject COPPER_PICKAXE_RECIPE = null;

        @Override
        public void onInitialize() {
            if (FabricLoader.getInstance().isModLoaded("custom_mod")) {
                COPPER_PICKAXE_RECIPE = createShapedRecipeJson(
                    Lists.newArrayList(
                        '#',
                        '|'
                    ), //The keys we are using for the input items/tags.
                    Lists.newArrayList(new Identifier("c", "copper_ingots"), new Identifier("stick")), //The items/tags we are using as input.
                    Lists.newArrayList("tag", "item"), //Whether the input we provided is a tag or an item.
                    Lists.newArrayList(
                        "###",
                        " | ",
                        " | "
                    ), //The crafting pattern.
                    new Identifier("examplemod:copper_pickaxe") //The crafting output
                );
            }
        }
        
        //The function we previously made.
        public static JsonObject createShapedRecipeJson(...) {
            [...]
        }

    }
```

### RecipeManager Mixin

Finally, we want to mixin into RecipeManager, so we can pass the recipe
into Minecraft.

```java
@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        if (ExampleMod.COPPER_PICKAXE_RECIPE != null) {
            map.put(new Identifier("examplemod", "copper_pickaxe"), ExampleMod.COPPER_PICKAXE_RECIPE);
        }
    }
}
```


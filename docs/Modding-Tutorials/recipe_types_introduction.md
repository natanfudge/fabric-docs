# Introduction to RecipeTypes

## What is a RecipeType

A RecipeType is different from a recipe itself. Let's see an example of
a recipe to understand it better :

```javascript
{
  "type": "minecraft:smelting",
  "ingredient": {
    "tag": "minecraft:logs_that_burn"
  },
  "result": "minecraft:charcoal",
  "experience": 0.15,
  "cookingtime": 200
}
```

The file itself define the recipe to get charcoal, the type of this
recipe is smelting. A RecipeType can be defined as a group of recipes.
This RecipeType is used by the furnace block, but you could create
another furnace that also use this recipe type but with different stats.

## How a RecipeType works

A RecipeType needs two things to work, a Recipe class and a
RecipeSerializer class.

The Recipe class defines the inputs, outputs, how to craft with them and
other things. When creating a recipe like the example of the charcoal,
it creates an instance of your Recipe class that is used to register to
RecipeType.

The RecipeSerializer is used to serialize an object (here it is an
instance of your Recipe class which defines a recipe) to a packet, and
to deserialize from a packet or a json object to an instance of your
Recipe class. The RecipeSerializer helps us create recipes faster
without having to instanciate ourself our Recipe class for each recipe
we need.

## How to use a RecipeType

To test if different items in an inventory work as inputs of a recipe
and get the expected output, we use the RecipeManager class. It has some
usefull method such as getAllOfType returning a map of all the recipes
of a certain type with their Identifier or getAllMatches returning a
List of recipes usable with the inventory of a ScreenHandler.

The easiest way to add a RecipeType is to use some vanilla classes to
save a lot of code. An example of this would be all the abstract classes
related to cooking. Theses classes are used for the furnace, the smoker,
the blast furnace and even the campfire. If your RecipeType behave the
same has a vanilla RecipeType you should first try to extends the used
classes if they exist(sometime a RecipeType for a block does't have
classes that you can use and it is easier to create your RecipeType from
scratch).

## In depth tutorials

- [Adding a recipe type](../Modding-Tutorials/recipe_type.md)
- [Adding a cooking recipe type](../Modding-Tutorials/cooking_recipe_type.md)
- [Adding a cutting recipe type](../Modding-Tutorials/cutting_recipe_type.md)


# Crafting recipes

## Adding a basic crafting recipe

Make sure you [added an item](../tutorial/items.md) before reading this
tutorial, we will be using it.

So far, our item is obtainable through the creative menu or commands. To
make it available to survival players, we'll add a crafting recipe for
the item.

Create a file named `fabric_item.json` under
`resources/data/tutorial/recipes/` (replace tutorial with your mod id if
appropriate). Here's an example recipe for the `fabric_item` we made:

```javascript
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "WWW",
    "WR ",
    "WWW"
  ],
  "key": {
    "W": {
      "tag": "minecraft:logs"
    },
    "R": {
      "item": "minecraft:redstone"
    }
  },
  "result": {
    "item": "tutorial:fabric_item",
    "count": 4
  }
}
```

Breakdown of the recipe:

- **type**: This is a shaped crafting recipe.
- **result**: This is a crafting recipe for 4 `tutorial:fabric_item`.
  The `count` field is optional. If you don't specify a `count`, it
  will default to 1.
- **pattern**: A pattern that represents the crafting recipe. Each
  letter represents one item. An empty space means that no item is
  required in that slot. What each letter represents is defined in
  **key**.
- **key**: What each letter in the pattern represents. `W` represents
  any item with the `minecraft:logs` tag (all logs). `R` represent the
  redstone item specificly. For more information about tags see
  [here](https://minecraft.gamepedia.com/Tag)

In total, the crafting recipe would look like this:

| Recipe for 4 fabric\_item |          |         |
| ------------------------- | -------- | ------- |
| Any Log                   | Any Log  | Any Log |
| Any Log                   | Redstone | Nothing |
| Any Log                   | Any Log  | Any Log |

For more information about the format of basic recipes, see
[here](https://minecraft.gamepedia.com/Recipe).

## Adding a custom crafting recipe

The `type` value can be changed to support a custom recipe \[more
information needed\].

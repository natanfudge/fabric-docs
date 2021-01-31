# Mining Levels

### Introduction

Vanilla has a terrible system for mining levels, values are hardcoded
and there isn't support for mining levels except for pickaxes.

### Setting Mining Level of a Block

To start, use the `breakByTool` method in `FabricBlockSettings` to set
the block mining levels, it requires an item tag, which is provided by
fabric in `FabricToolTags`:

```java
settings.breakByTool(FabricToolTags.PICKAXES, 2)
```

Here's the list of mining levels:

    0 -> Wooden / Golden Pickaxe
    1 -> Stone Pickaxe
    2 -> Iron Pickaxe
    3 -> Diamond Pickaxe
    4 -> Netherite Pickaxe

### Dealing with Block Material Madness

Vanilla pickaxes are effective for `STONE`, `METAL` and `ANVIL`.

Vanilla axes are effective for `WOOD`, `NETHER_WOOD`, `PLANT`,
`REPLACEABLE_PLANT`, `BAMBOO`, `PUMPKIN`.

If you use any of these materials, the tool will break your block even
if it is under the required mining level.

To avoid this, you must create your own clone of the material. Let's say
you want to create a clone for `Material.STONE`, take a look at the code
for `Material.STONE`:

```java
new Material.Builder(MaterialColor.STONE).requiresTool().build()
```

Turn `Material.Builder` into `FabricMaterialBuilder` and you will get
this:

```java
new FabricMaterialBuilder(MaterialColor.STONE).requiresTool().build()
```

### Disable drops when using the invalid tool (For 1.15.x!)

You will need to set `requiresTool` in the material of the block,
therefore you will want to create a clone for your material.

Let's say you want to create a clone for `Material.WOOD` to make your
wooden block drop only when using the correct tool, take a look at the
code for `Material.STONE`:

```java
new Material.Builder(MaterialColor.WOOD).burnable().build()
```

Turn `Material.Builder` into `FabricMaterialBuilder` and **add
`requiresTool()`**, you will get this:

```java
new FabricMaterialBuilder(MaterialColor.WOOD).burnable().requiresTool().build()
```

### Disable drops when using the invalid tool (For 1.16.x!)

You will need to set `requiresTool` in the settings of the block.

### Making a custom tool

You will need to add your tool into the fabric tool tags to support
modded blocks.

Example of adding a pickaxe to the `pickaxes` tag:

File Location: /src/main/resources/data/fabric/tags/items/pickaxes.json

```javascript
{
  "replace": false,
  "values": [
    "examplemod:example_pickaxe"
  ]
}
```


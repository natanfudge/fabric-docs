# Items

Items are the pieces of content that appear in your inventory. They can
perform actions when you click, act as food, or spawn entities. The
following documentation will give you a rundown of the entire `Item`
class and everything related to it. For a tutorial you can follow along,
visit [Practical Example: Adding an Item](../Modding-Tutorials/Items/item.md).

## Item Settings

The `Item` constructor requires an `Item.Settings` instance. This
builder class defines behavior such as stack size, durability, and
whether the item is edible. A full table of available builder methods is
shown below:

| Method            | Args            | Description                                                                                             |
|-------------------|-----------------|---------------------------------------------------------------------------------------------------------|
| food              | `FoodComponent` | Changes the Item to be edible based on the given `FoodComponent`.                                       |
| maxCount          | `int`           | Sets the Item's max stack count. Can't be used in combination with damage.                              |
| maxDamageIfAbsent | `int`           | Sets the Item's maximum damage count if it hasn't already been set.                                     |
| maxDamage         | `int`           | Sets the Item's max stack durability.                                                                   |
| recipeRemainder   | `Item`          | Sets the Item's recipe remainder, which is given back to the player after the Item is used in a recipe. |
| group             | `ItemGroup`     | Sets the ItemGroup of the Item, which is used for creative tabs.                                        |
| rarity            | `Rarity`        | Sets the rarity of the Item, which changes its name color.                                              |
| fireproof         | None            | Marks the item as being fireproof. Prevents the item from being destroyed by lava and fire.             |

## Fabric Item Settings

The Fabric Item API provides extra methods to set even more attributes.
To use it, just replace `new Item.Settings()` with
`new FabricItemSettings()`. Heres the list of additional functionalities
`FabricItemSettings` provides:

| Method        | Args                    | Description                                   |
|---------------|-------------------------|-----------------------------------------------|
| equipmentSlot | `EquipmentSlotProvider` | Sets the equipment slot provider of the item. |
| customDamage  | `CustomDamageHandler`   | Sets the custom damage handler of the item.   |

------------------------------------------------------------------------

### Food

```java
public Item.Settings food(FoodComponent foodComponent)
```

`foodComponent` - instance of'FoodComponent. When set, the Item will be
edible based on the settings provided by the FoodComponent builder. For
an in-depth explanation of the available options, view the FoodComponent
Overview page.

------------------------------------------------------------------------

### Max Stack Count

```java
public Item.Settings maxCount(int maxCount)
```

`maxCount` - the maximum count of an ItemStack for the given Item. If
`maxDamage()` has already been called, a RuntimeException is thrown, as
an Item cannot have both damage and count. Keeping the max count at or
below 64 is recommended, as any value above that can lead to various
issues.

------------------------------------------------------------------------

### Max Damage if Absent

```java
public Item.Settings maxDamageIfAbsent(int maxDamage)
```

`maxDamage` - max durability of the given `Item` when in `ItemStack`
form.

If `maxDamage()` has not been called yet, the max durability of the Item
is set to the value passed in. This is used for cases such as tools and
armor, where the Item's durability is only set to the ToolMaterial's
durability *if* it has not been set yet.

------------------------------------------------------------------------

### Max Damage

```java
public Item.Settings maxDamage(int maxDamage)
```

`maxDamage` - max durability of the given Item when in ItemStack form.

------------------------------------------------------------------------

### Recipe Remainder

```java
public Item.Settings recipeRemainder(Item recipeRemainder)
```

`recipeRemainder` - Item to return as a remainder when the base Item is
used in a crafting recipe.

When a recipe remainder is set on an Item, any recipe using that Item
will return the remainder on craft. This is used for buckets (Water,
Lava, Milk) and bottles (Dragon Breath, Honey) returning their
respective empty items when used in recipes.

------------------------------------------------------------------------

### Group

```java
public Item.Settings group(ItemGroup group)
```

`group` - ItemGroup to add Item in.

Each ItemGroup appears as a tab in the creative inventory. Adding an
Item to this group will add it to the tab. The order of the group is
based on registry order. For more information on creating a group, see
the [ItemGroups page](../Modding-Tutorials/Items/itemgroup.md).

------------------------------------------------------------------------

### Rarity

```java
public Item.Settings rarity(Rarity rarity)
```

`rarity` - Rarity of the given Item.

If Rarity is set, the given Item will have a custom name color. An
Item's Rarity defaults to common.

| Rarity   | Color        |
|----------|--------------|
| Common   | White        |
| Uncommon | Yellow       |
| Rare     | Aqua         |
| Epic     | Light Purple |

------------------------------------------------------------------------

### Fireproof

```java
public Item.Settings fireproof()
```

Marks the item as being fireproof, and protects the item entity that
contains the item from fire and lava.

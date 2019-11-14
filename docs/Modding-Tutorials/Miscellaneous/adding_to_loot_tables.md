# Adding items to existing loot tables

## Introduction

Sometimes you want to add items to [loot
tables](https://minecraft.gamepedia.com/Loot_table), for example adding
your own drops to a vanilla block or entity. The simplest solution,
replacing the loot table file, can break other mods â€“ what if they want
to change them as well? Weâ€™ll take a look at how you can add items to
loot tables without overriding the table.

Our example will be adding eggs to the coal ore loot table.

## Listening to loot table loading

Fabric API has an event thatâ€™s fired when loot tables are loaded,
`%%LootTableLoadingCallback%%`. You can register an event listener for
it in your initializer. Letâ€™s also check that the current loot table is
`%%minecraft:blocks/coal_ore%%`.

```java
// No magic constants!
private static final Identifier COAL_ORE_LOOT_TABLE_ID = new Identifier("minecraft", "blocks/coal_ore");

// Actual code

LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
    if (COAL_ORE_LOOT_TABLE_ID.equals(id)) {
        // Our code will go here
    }
});
```

## Adding items to the table

In loot tables, items are stored in *loot entries,* and entries are
stored in *loot pools*. To add an item, weâ€™ll need to add a pool with an
item entry to the loot table.

We can make a pool with `%%FabricLootPoolBuilder%%`, and add it to the
loot table:

```java
LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
    if (COAL_ORE_LOOT_TABLE_ID.equals(id)) {
        FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                .withRolls(ConstantLootTableRange.create(1)); // Same as "rolls": 1 in the loot table json

        supplier.withPool(poolBuilder);
    }
});
```

Our pool doesnâ€™t have any items yet, so weâ€™ll make an item entry and add
it to the pool, and we're done:

```java
LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
    if (COAL_ORE_LOOT_TABLE_ID.equals(id)) {
        FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                .withRolls(ConstantLootTableRange.create(1))
                .withEntry(ItemEntry.builder(Items.EGG));

        supplier.withPool(poolBuilder);
    }
});
```

![](../images/tutorial/coal_ore_egg.png)

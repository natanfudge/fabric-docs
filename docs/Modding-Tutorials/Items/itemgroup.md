# Item Groups

### Creating a simple Item Group

To have your `ItemGroup` properly show up in the creative menu, use the
`FabricItemGroupBuilder` to create them:

```java
public class ExampleMod implements ModInitializer {

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier("tutorial", "general"),
        () -> new ItemStack(Blocks.COBBLESTONE));
    
    public static final ItemGroup OTHER_GROUP = FabricItemGroupBuilder.create(
        new Identifier("tutorial", "other"))
        .icon(() -> new ItemStack(Items.BOWL))
        .build();
    // ...
}
```

Once `FabricItemGroupBuilder#build` is called, your group will be added
to the list of item groups in the creative menu.

Make sure you replace the arguments [1] you pass to the `Identifier`
constructor with your actual mod ID and the translation key you want to
give your item group for localization [2] later on.

#### Adding your Items to your Item Group

When creating a custom Item, call `Item.Settings#group` on your settings
and pass in your custom group:

```java
public static final Item YOUR_ITEM = new Item(new Item.Settings().group(ExampleMod.ITEM_GROUP));
```

### Making an Item Group display specific Items in a particular order

Call `FabricItemGroupBuilder#appendItems` and pass any
`Consumer<List<ItemStack>>`. You can then add whatever stacks you want
to the given list in some order. `ItemStack.EMPTY` can be used to place
empty spaces in your group.

```java
public class ExampleMod implements ModInitializer {
    
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier("tutorial", "general"),
        () -> new ItemStack(Blocks.COBBLESTONE));
    
    public static final ItemGroup OTHER_GROUP = FabricItemGroupBuilder.create(
        new Identifier("tutorial", "other"))
        .icon(() -> new ItemStack(Items.BOWL))
        .appendItems(stacks -> {
            stacks.add(new ItemStack(Blocks.BONE_BLOCK));
            stacks.add(new ItemStack(Items.APPLE));
            stacks.add(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
            stacks.add(ItemStack.EMPTY);
            stacks.add(new ItemStack(Items.IRON_SHOVEL));
        })
        .build();
    // ...
}
```

<img src="/tutorial/item_group_append_items.png" width="400" />

[1] Remember that the arguments you pass to the `Identifier` constructor
can only contain certain characters.  
Both arguments (the `namespace` & `path`) can contain *lowercase
letters*, *numbers*, *underscores*, *periods*, or *dashes*.
`[a-z0-9_.-]`  
The second argument (the `path`) can also include *slashes*.
`[a-z0-9/._-]`  
Avoid using other symbols, else an `InvalidIdentifierException` would be
thrown!

[2] The full translation key for the first example `ItemGroup` would be
`itemGroup.mod_id.general`

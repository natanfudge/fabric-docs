# Groupes d'objets

### CrÃ©er un groupe d'objets simple

Pour que votre `ItemGroup` apparaisse correctement dans le menu crÃ©atif,
utilisez le `FabricItemGroupBuilder` pour le crÃ©er.

```java
public class ExampleMod implements ModInitializer
{
    // ...
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

Une fois que `FabricItemGroupBuilder#build` est appelÃ©, votre groupe
sera ajoutÃ© Ã  la liste des groupes d'objets dans le menu crÃ©atif.

Assurez-vous de remplacer les arguments \[1\] que vous insÃ©rez dans le
constructeur `Identifier` par votre ID de mod rÃ©el et par la clÃ© de
traduction que vous souhaitez donner Ã  votre groupe d'objets \[2\].

#### Ajouter vos objets dans votre groupe d'objets

Lors de la crÃ©ation d'un objet personnalisÃ©, appelez
`Item.Settings#group` dans vos paramÃ¨tres et insÃ©rez votre groupe
personnalisÃ©.

```java
public static final Item YOUR_ITEM = new Item(new Item.Settings().group(ExampleMod.ITEM_GROUP));
```

### Afficher des objets spÃ©cifiques dans un ordre particulier

Appelez `FabricItemGroupBuilder#appendItems` et insÃ©rez un
`Consumer<List<ItemStack//>//>`. Vous pouvez alors ajouter les stacks
d'objets de votre choix Ã  la liste donnÃ©e, dans un ordre quelconque.
`ItemStack.EMPTY` peut Ãªtre utilisÃ© pour placer des espaces vides dans
votre groupe.

```java
public class ExampleMod implements ModInitializer
{
    // ...
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier("tutorial", "general"),
        () -> new ItemStack(Blocks.COBBLESTONE));
    
    public static final ItemGroup OTHER_GROUP = FabricItemGroupBuilder.create(
        new Identifier("tutorial", "other"))
        .icon(() -> new ItemStack(Items.BOWL))
        .appendItems(stacks ->
        {
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

![](../../images/tutorial/item_group_append_items.png)

1. Rappelez-vous que les arguments insÃ©rÃ©s dans le constructeur
   `Identifier` peuvent contenir seulement certains caractÃ¨res.  
   Chacun des arguments (le `namespace` et le `path`) peut contenir des
   *lettres minuscules*, des *nombres*, des *tirets bas*, des *points*
   ou des *tirets*. `[a-z0-9_.-]`  
   Le second argument (le `path`) peut Ã©galement contenir des *barres
   obliques*. `[a-z0-9/._-]`  
   Ã‰vitez d'utiliser d'autres symboles, sinon une erreur
   `InvalidIdentifierException` sera envoyÃ©e \!

2. La clÃ© de traduction complÃ¨te pour le premier exemple d`'ItemGroup`
   serait `itemGroup.mod_id.general`


# Adding Tools

### Creating a Tool Material

Tools require a `ToolMaterial`, which defines the following behavior:

- durability
- mining speed
- attack damage
- mining level
- enchantability
- repair ingredient

In other words, Tool Materials defines the *base* functionality for
tools of that type, and tools can choose to use the values provided by
the material, or roll with their own.

Vanilla Tool Materials can be found in `ToolMaterials`. We will create a
separate class for our material:

```java
public class PotatoToolMaterial implements ToolMaterial {
    
    [...]
}
```

`ToolMaterial` has a number of methods you will need to implement:

#### Durability

`getDurability` defines the base durability tools will have when they
use this material. In vanilla, all tools of the same type have the same
durability.

```java
@Override
public int getDurability() {
    return 500;
}
```

#### Mining Speed

`getMiningSpeedMultiplier` defines how fast tools are when mining
blocks. For a general sense of scale, Wooden has a speed of 2.0F, and
Diamond has a speed of 8.0F.

```java
@Override
public float getMiningSpeedMultiplier() {
    return 5.0F;
}
```

#### Attack Damage

`getAttackDamage` returns the base damage of the tool. Note that *most*
tools ask for an integer damage amount in their constructor, which means
the resulting damage is `(float) materialDamage + (int) toolDamage + 1`.
If you want your tool to entirely control its damage amount in its
constructor, you can make your material return an attack damage of 0F.

```java
@Override
public float getAttackDamage() {
    return 3.0F;
}
```

#### Mining Level

`getMiningLevel` sets the mining level of a tool. Diamond has a mining
level of 3, and a value of 3+ is required to mine Obsidian.

```java
@Override
public int getMiningLevel() {
    return 2;
}
```

#### Enchantability

`getEnchantability` defines how enchantable a tool is. Gold comes in at
22 Enchatability, while Diamond sits at 10. Higher enchantability means
better (and higher-level) enchantments.

```java
@Override
public int getEnchantability() {
    return 15;
}
```

#### Repair Ingredient

`getRepairIngredient` returns the `Ingredient` required to repair a tool
in an anvil.

```java
@Override
public Ingredient getRepairIngredient() {
    return Ingredient.ofItems(Items.POTATO);
}
```

`ToolMaterial`s do *not* have to be registered. A good way to pass them
out to tools that require them is by keeping an instance somewhere (and
then referencing it when you need it). In this case, we will put our
instance at the top of the Tool Material class:

```java
public class PotatoToolMaterial implements ToolMaterial {

    public static final PotatoToolMaterial INSTANCE = new PotatoToolMaterial();
    
    [...]
}
```

`PotatoToolMaterial` can now be referenced with
`PotatoToolMaterial.INSTANCE`.

### Creating Tools

All base tool classes (`PickaxeItem`, `ShovelItem`, `HoeItem`,
`AxeItem`, `SwordItem`) require a `ToolMaterial`, an attack speed
(float), an additional attack damage amount (int), and an
`Item.Settings` instance.

```java
public static ToolItem POTATO_SHOVEL = new ShovelItem(PotatoToolMaterial.INSTANCE, 1.5F, -3.0F, new Item.Settings().group(ItemGroup.TOOLS));
public static ToolItem POTATO_SWORD = new SwordItem(PotatoToolMaterial.INSTANCE, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
```

\`PickaxeItem\` , \`HoeItem\` and \`AxeItem\` have protected
constructors, which means you will need to create your own sub-class
with a public constructor:

```java
public class CustomPickaxeItem extends PickaxeItem {
    public CustomPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
}
```

Using the custom subclass:

```java
public static ToolItem POTATO_PICKAXE = new CustomPickaxeItem(PotatoToolMaterial.INSTANCE, 1, -2.8F, new Item.Settings().group(ItemGroup.TOOLS));
public static ToolItem POTATO_AXE = new CustomAxeItem(PotatoToolMaterial.INSTANCE, 7.0F, -3.2F, new Item.Settings().group(ItemGroup.TOOLS));
public static ToolItem POTATO_HOE = new CustomHoeItem(PotatoToolMaterial.INSTANCE, 7, -3.2F, new Item.Settings().group(ItemGroup.TOOLS));
```

If you want to add any special attributes or behaviors to your tool,
create a subclass that extends one of the base tool classes, and
override any required methods.

### Registering Tools

For a recap on registering items, read through the item tutorial
[here](../Modding-Tutorials/Items/item.md).

### Making your tool work with non-vanilla blocks

Visit the last section of
<https://fabricmc.net/wiki/tutorial:mining_levels>

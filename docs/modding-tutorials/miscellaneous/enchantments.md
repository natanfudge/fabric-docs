## Adding Enchantments

To add enchantments to your mod, you'll need to:

- create a class that extends `Enchantment` or another existing
  Enchantment (such as `DamageEnchantment`)
- register your enchantment
- add custom functionality or mechanics if needed
- add translations for your enchantment \[1\]

Enchantments can either have custom functionality implemented separately
(such as smelting ores mined) or can use already existing mechanics
(such as `DamageEnchantment`), which are applied when appropriate. The
base `Enchantment` class also has several methods to create
functionality, such as an "on enemy hit" method.

### Creating Enchantment Class

We'll be creating an enchantment called *Frost*, which slows mobs. The
slowness effect durability & potency will grow relative to the level of
the enchantment.

```java
public class FrostEnchantment extends Enchantment 
{
    public WrathEnchantment(Weight weight, EnchantmentTarget target, EquipmentSlot[] slots)
    {
        super(weight, target, slots)
    }
}
```

You'll have to override a few basic methods for basic functionality:

`getMinimumPower` is the minimum level required to get the enchant in an
enchanting table. We'll set it to 1, so you can get it at any level:

```java
@Override
public int getMinimumPower(int int_1)
{
    return 1;
}
```

`getMaximumLevel` is the number of tiers the enchantment has. \[2\]

```java
@Override
public int getMaximumLevel()
{
    return 3;
}
```

Finally, we'll implement our slowness effect in the `onTargetDamage`
method, which is called when you whack an enemy with a tool that has
your enchantment.

```java
@Override
public void onTargetDamaged(LivingEntity user, Entity target, int level)
{
    if(target instanceof LivingEntity)
    {
        ((LivingEntity) target).addPotionEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 2 * level, level - 1));
    }

    super.onTargetDamaged(user, target, level);
}
```

Pretty simple logic: if the entity we're hitting can have status
effects, give it slowness. The time is 2 seconds per level, and the
potency is equivalent to the level.

### Registering Enchantment

Registering enchantments follows the same process as usual:

```java
private static Enchantment FROST;

@Override
public void onInitialize()
{
    FROST = Registry.register(
        Registry.ENCHANTMENT,
    new Identifier("tutorial", "frost"),
    new FrostEnchantment(
        Enchantment.Weight.VERY_RARE,
        EnchantmentTarget.WEAPON,
        new EquipmentSlot[] {
        EquipmentSlot.MAINHAND
        }
    )
    );
}
```

This registers our enchantment under the namespace `tutorial:frost`,
sets it as a very rare enchantment, and only allows it on main hand
tools.

### Adding Translations & Testing

You'll need to add a translation to your enchantment as well. Head over
to your [mod lang file](../Modding-Tutorials/Miscellaneous/lang.md) and add a new entry:

```json
{
    "enchantment.tutorial.frost": "Frost"
}
```

If you go in-game, [you should be able to enchant main hand weapons with
your new enchant.](https://i.imgur.com/31nFl2H.png)

1. When you register enchantments, books are automatically added to the
   game for each level. The translated name of the enchantment
   (`enchantment.modid.enchantname`) is what appears as the book name.

2. Enchantments with more than a single tier will have roman numerals
   after the name to show the level. If the enchantment only has a
   single level, nothing is added.


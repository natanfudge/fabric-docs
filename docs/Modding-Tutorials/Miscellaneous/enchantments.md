## Adding Enchantments

To add enchantments to your mod, you'll need to:

- create a class that extends `Enchantment` or another existing
  Enchantment (such as `DamageEnchantment`)
- register your enchantment
- add custom functionality or mechanics if needed
- add translations for your enchantment [1]

Enchantments can either have custom functionality implemented separately
(such as smelting ores mined) or can use already existing mechanics
(such as `DamageEnchantment`), which are applied when appropriate. The
base `Enchantment` class also has several methods to create
functionality, such as an "on enemy hit" method.

### Creating Enchantment Class

Our new enchantment is called *Frost* and slows mobs on hit. The
slowness effect, durability, and potency will grow relative to the level
of the enchantment. In our enchantment class, we pass up `UNCOMMON` as
the enchantment rarity, `WEAPON` as the enchantment target, and
`MAINHAND` as the only valid tool type for our enchantment.

```java
public class FrostEnchantment extends Enchantment {

    public FrostEnchantment() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }
}
```

We will now override a few basic methods for basic functionality:

`getMinPower` is related to the minimum level needed to see the enchant
in a table, but it is not a 1:1 ratio. Most enchantments return
something like `10 * level`, with different scales depending on the max
level and rarity of the enchantment. We will return 1 so it is always
available. Note that the max power of an enchantment is set to
`min(level) + 5` by default, which means this enchantment will only
appear at very low levels. You will have to tweak your enchantment
properties on your own and look at similar enchantment values to find
the sweet number spot.

```java
@Override
public int getMinPower(int level) {
    return 1;
}
```

`getMaxLevel` is the number of levels the enchantment has. Sharpness has
a max level of 5. [2]

```java
@Override
public int getMaxLevel() {
    return 3;
}
```

Finally, we will implement our slowness effect in the `onTargetDamage`
method, which is called when you whack an enemy with a tool that has
your enchantment.

```java
@Override
public void onTargetDamaged(LivingEntity user, Entity target, int level) {
    if(target instanceof LivingEntity) {
        ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 2 * level, level - 1));
    }

    super.onTargetDamaged(user, target, level);
}
```

If the entity we are hitting can have status effects (`LivingEntity`s
can have status effects, but not `Entity`), give it the slowness effect.
The duration of the effect is 2 seconds per level, and the potency is
equivalent to the level.

### Registering Enchantment

Registering enchantments follows the same process as usual:

```java
public class EnchantingExample implements ModInitializer {

    private static Enchantment FROST = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier("tutorial", "frost"),
            new FrostEnchantment()
    );

    @Override
    public void onInitialize() {

    }
}
```

This registers our enchantment under the namespace `tutorial:frost`. All
non-treasure enchantments are available in an enchanting table,
including the ones you register.

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

[1] When you register enchantments, books are automatically added to the
game for each level. The translated name of the enchantment
(`enchantment.modid.enchantname`) is what appears as the book name.

[2] Enchantments with more than a single level will have roman numerals
after the name to show the level. If the enchantment only has a single
level, nothing is added.

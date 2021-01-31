# Adding Armor

### Introduction

While armor is a bit more complicated to implement than a normal block
or item, once you understand it, it becomes simple to implement. To add
armor, we'll first make a CustomArmorMaterial class, then register the
items. We'll also take a look at how to texture them. There's a special
chapter at the end of this document that explains how to add knockback
to the armor, since the method is only accessible through a mixin (as of
1.16.3).

An example for this document can be found in [this mod GitHub
repository](https://github.com/CumulusMC/Gilded-Netherite).

### Creating an Armor Material class

Since new armor needs to be set with a new name (as well as extra things
like armor points and durability), we'll have to create a new class for
our CustomArmorMaterial.

This class will implement ArmorMaterial, and it'll start by assigning
values to armor points (called PROTECTION\_VALUES). All its following
arguments will make use of @Override.

```java
public class CustomArmorMaterial implements ArmorMaterial {
    private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};   private static final int[] PROTECTION_VALUES = new int[] {A, B, C, D}; 

    // In which A is helmet, B chestplate, C leggings and D boots. 
    // For reference, Leather uses {1, 2, 3, 1}, and Diamond/Netherite {3, 6, 8, 3}
}
```

The next arguments are defined as follows (don't worry about the names,
you'll see how we implement it below them):

1. getDurability: how many hits can armor take before breaking. Uses
   the int we wrote on 'BASE\_DURABILITY' to calculate. Leather uses 5,
   Diamond 33, Netherite 37.
2. getPretectionAmount: calls for the 'PROTECTION\_VALUES' int we
   already wrote above.
3. getEnchantability: This will be how likely the armor can get high
   level or multiple enchantments in an enchantment book.
4. SoundEvent getEquipSound: The standard used by vanilla armor is
   `SoundEvents.ITEM_ARMOR_EQUIP_X`, X being the type of armor.
5. Ingredient getRepairIngridient: what item are we gonna be using to
   repair the armor on an anvil. It can be either a vanilla item or one
   of your own.
6. String getName: what the parent item of the armor is. In Diamond
   armor, it'd be "diamond".
7. getToughness: This is a second protection value where the armor is
   more durable against high value attacks. Value goes as 'X.0F'

And the new value introduced on 1.16

1. getKnockbackResistance: leave this value at 0. If you want to
   implement it, write '0.XF' (in which X is how much knockback
   protection you want), and I'll teach you how to make it work later
   on.

I'll leave all variables written as X or A, B, C, D. With those
arguments, it should now look something like this:

```java
public class CustomArmorMaterial implements ArmorMaterial {
    private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};   private static final int[] PROTECTION_VALUES = new int[] {A, B, C, D};

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * X;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return PROTECTION_VALUES[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return X;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_X;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(RegisterItems.X);
    }

    @Override
    public String getName() {
        return "name";
    }

    @Override
    public float getToughness() {
        return X.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.XF;
    }
}
```

Now that you have the basics of the armor material class, let's register
your armor items in a new class we'll simply call RegisterItems.

### Creating Armor Items

We're gonna make a new class called RegisterItems to implement your new
armor pieces. This will also be the place to, for example, register
tools, if you're making a new item like an ingot (We'll refer to this as
a "Custom\_Material"). This setup will also put the items on a new
Creative tab, but you're free to delete that part.

The syntax of groups is
*.group(YourModName.YOUR\_MOD\_NAME\_BUT\_IN\_CAPS\_GROUP)*. I'll be
referring to it as ExampleMod:

```java
public class RegisterItems {

   public static final ArmorMaterial customArmorMaterial = new CustomArmorMaterial();
   public static final Item CUSTOM_MATERIAL = new CustomMaterialItem(new Item.Settings().group(ExampleMod.EXAMPLE_MOD_GROUP));
    // If you made a new material, this is where you would note it.
    public static final Item CUSTOM_MATERIAL_HELMET = new ArmorItem(CustomArmorMaterial, EquipmentSlot.HEAD, new Item.Settings().group(ExampleMod.EXAMPLE_MOD_GROUP));
    public static final Item CUSTOM_MATERIAL_CHESTPLATE = new ArmorItem(CustomArmorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(ExampleMod.EXAMPLE_MOD_GROUP));
    public static final Item CUSTOM_MATERIAL_LEGGINGS = new ArmorItem(CustomArmorMaterial, EquipmentSlot.LEGS, new Item.Settings().group(ExampleMod.EXAMPLE_MOD_GROUP));
    public static final Item CUSTOM_MATERIAL_BOOTS = new ArmorItem(CustomArmorMaterial, EquipmentSlot.FEET, new Item.Settings().group(ExampleMod.EXAMPLE_MOD_GROUP));

}
```

Now that your items are properly created, lets register them and give
them proper names. Your first parameter is going to be your namespace,
which is your ModID, and then next one the name you want to give to your
item.

We'll be writing this right below your last ArmorItem.

```java
public static void register() {
    Registry.register(Registry.ITEM, new Identifier("examplemod", "custom_material"), CUSTOM_MATERIAL);
    Registry.register(Registry.ITEM, new Identifier("examplemod", "custom_material_helmet"), CUSTOM_MATERIAL_HELMET);
    Registry.register(Registry.ITEM, new Identifier("examplemod", "custom_material_chestplate"), CUSTOM_MATERIAL_CHESTPLATE);
    Registry.register(Registry.ITEM, new Identifier("examplemod", "custom_material_leggings"), CUSTOM_MATERIAL_LEGGINGS);
    Registry.register(Registry.ITEM, new Identifier("examplemod", "custom_material_boots"), CUSTOM_MATERIAL_BOOTS);
}
```

Your armor items are done. Now we'll just call the Registry on our main
class (and annotate the new group).

```java
public static final ItemGroup EXAMPLE_MOD_GROUP = FabricItemGroupBuilder.create(
            new Identifier("examplemod", "example_mod_group"))
            .icon(() -> new ItemStack(RegisterItems.CUSTOM_MATERIAL)) // This uses the model of the new material you created as an icon, but you can reference to whatever you like
            .build();

@Override
    public void onInitialize() {
        RegisterItems.register();
    }
```

That's it! Your armor should now exist in game, untextured still, but
present and able to be given with /give.

Now we'll be assigning the textures to each piece.

### Texturing

We're going to assume you

- Have the textures for each armor item (x\_helmet.png,
  x\_chestplate.png etc.)
- Have the textures for the armor in body (x\_layer\_1.png and
  x\_layer\_2.png)

And assign them to each armor item.

The following should be the same with all armor items, only changing
which part are we using. We'll use helmet for our example.

```JSON
{
    "parent": "item/generated",
    "textures": {
        "layer0": "examplemod:item/custom_material_helmet"
    }
}
```

Repeat with all armor items.

To give your on-body armor a texture, you'll simply put the layer\_1.png
and layer\_2.png on 'resources/assets/minecraft/textures/models/armor'.

If you followed everything, you should now be able to have a full armor
set!

### Adding Knockback Protection

And here comes the so very cursed!

Mojang decided that they were not only going to hardcode
getKnockbackResistance, but they were also gonna make it immutable! Fun
stuff.

To get around this, we're gonna make a mixin that goes into ArmorItem.
If this is your first time, [here's how to register mixins on your
fabric.mod.json](../Modding-Tutorials/mixin_registration.md)

We'll make a class called ArmorItemMixin, and write:

```java
@Mixin (ArmorItem.class)
public abstract class ArmorItemMixin {

}
```

Now we have to make a @Shadow to modify knockbackResistance, which is an
EntityAttribute

```java
@Mixin (ArmorItem.class)
public abstract class ArmorItemMixin {
    @Shadow @Final private static UUID[] MODIFIERS;
    @Shadow @Final @Mutable private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    @Shadow @Final protected float knockbackResistance;
}
```

Next we @Inject our GENERIC\_KNOCKBACK\_RESISTANCE into the
ArmorMaterial constructor.

```java
@Mixin (ArmorItem.class)
public abstract class ArmorItemMixin {

    @Shadow @Final private static UUID[] MODIFIERS;
    @Shadow @Final @Mutable private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    @Shadow @Final protected float knockbackResistance;
    
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void constructor(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings, CallbackInfo ci) {
        UUID uUID = MODIFIERS[slot.getEntitySlotId()];

        if (material == RegisterItems.customArmorMaterial) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();

            this.attributeModifiers.forEach(builder::put);

            builder.put(
                    EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                    new EntityAttributeModifier(uUID,
                            "Armor knockback resistance",
                            this.knockbackResistance,
                            EntityAttributeModifier.Operation.ADDITION
                    )
            );

            this.attributeModifiers = builder.build();
        }
    }
    
}
```

Now your armor has the knockback resistance value you assigned to it
back on CustomArmorMaterial.

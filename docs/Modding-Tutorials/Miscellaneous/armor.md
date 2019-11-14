# Adding Armor

### Introduction

While Armor is a bit more complicated to add then a normal block/item,
once you can understand it, it becomes simple to make. To add Armor,
we'll first make a custom material class, then register the items. We'll
also take a look at how to texture them.

### Creating an Armor Material class

Since new armor needs to be set with a new name (as well as extra things
like armor points and durability), we'll have to create a new class for
custom ArmorMaterial.

This class will implement ArmorMaterial and will be an enum type. It'll
need a lot of arguments, mainly the name, durability, etc., so for now
we'll just leave it empty. Don't worry about any errors for now.

```java
public enum CustomArmorMaterial implements ArmorMaterial {
    CustomArmorMaterial() {
        
    }
}
```

Since there's a lot of arguments needed, here's a list explaining each
one of them.

1. A String name. This will be used as a sort of "armor tag" for later.
2. A durability multiplier. This will be the number that will be used
   to determine the durability based on the base values.
3. Armor values, or "Protection Amounts" in the vanilla code. This will
   be an int array.
4. Enchantability. This will be how likely the armor can get high level
   or multiple enchantments in an enchantment book.
5. A sound event. The standard used by vanilla armor is
   `SoundEvents.ITEM_ARMOR_EQUIP_X`, X being the type of armor.
6. Toughness. This is a second protection value where the armor is more
   durable against high value attacks.
7. A repair ingredient. This will be a `Supplier<Ingredient>` instance
   instead of an `Item`, which will go over in a bit.

With those arguments, it should now look something like this:

```java
public enum CustomArmorMaterial implements ArmorMaterial {
    CustomArmorMaterial(String name, int durabilityMultiplier, int[] armorValueArr, int enchantability, SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairIngredient) {
        
    }
}
```

We'll also have to define those values and make it usable, so now it'll
look like this:

```java
public enum CustomArmorMaterial implements ArmorMaterial {
    private final String name;
    private final int durabilityMultiplier;
    private final int[] armorValues;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final Lazy<Ingredient> repairIngredient;
    
    CustomArmorMaterial(String name, int durabilityMultiplier, int[] armorValueArr, int enchantability, SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.armorValues = armorValueArr;
        this.enchantability = enchantability;
        this.equipSound = soundEvent;
        this.toughness = toughness;
        this.repairIngredient = new Lazy(repairIngredient); // We'll need this to be a Lazy type for later.
    }
}
```

`ArmorMaterial` also needs several other methods, so we'll add them real
quick here.

We'll also have to add our base durability values, so for now we'll use
the vanilla values `[13, 15, 16, 11]`

```java
public enum CustomArmorMaterial implements ArmorMaterial {
    private static final int[] baseDurability = {13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] armorValues;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final Lazy<Ingredient> repairIngredient;
    
    CustomArmorMaterial(String name, int durabilityMultiplier, int[] armorValueArr, int enchantability, SoundEvent soundEvent, float toughness, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.armorValues = armorValueArr;
        this.enchantability = enchantability;
        this.equipSound = soundEvent;
        this.toughness = toughness;
        this.repairIngredient = new Lazy(repairIngredient);
    }
    
    public int getDurability(EquipmentSlot equipmentSlot_1) {
        return BASE_DURABILITY[equipmentSlot_1.getEntitySlotId()] * this.durabilityMultiplier;
    }

    public int getProtectionAmount(EquipmentSlot equipmentSlot_1) {
        return this.protectionAmounts[equipmentSlot_1.getEntitySlotId()];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public Ingredient getRepairIngredient() {
        // We needed to make it a Lazy type so we can actually get the Ingredient from the Supplier.
        return this.repairIngredientSupplier.get();
    }

    @Environment(EnvType.CLIENT)
    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }
}
```

Now that you have the basics of the armor material class, you can now
make your own material for armor. This can be done at the top of the
code like so:

```java
public enum CustomArmorMaterial implements ArmorMaterial {
    WOOL("wool", 5, new int[]{1,3,2,1}, 15, SoundEvents.BLOCK_WOOL_PLACE, 0.0F, () -> {
        return Ingredient.ofItems(Items.WHITE_WOOL);
    });
    [...]
}
```

Feel free to change any values.

### Creating Armor Items

Back in the main class, you can now create it like so:

```java
public class ExampleMod implements ModInitializer {
    public static final Item WOOL_HELMET = new ArmorItem(CustomArmorMaterial.WOOL, EquipmentSlot.HEAD, (new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item WOOL_CHESTPLATE = new ArmorItem(CustomArmorMaterial.WOOL, EquipmentSlot.CHEST, (new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item WOOL_LEGGINGS = new ArmorItem(CustomArmorMaterial.WOOL, EquipmentSlot.LEGS, (new Item.Settings().group(ItemGroup.COMBAT)));
    public static final Item WOOL_BOOTS = new ArmorItem(CustomArmorMaterial.WOOL, EquipmentSlot.FEET, (new Item.Settings().group(ItemGroup.COMBAT)));
}
```

### Registering Armor Items

Register them the same way you'd register a normal item.

```java
[...]
public void onInitialize() {
    Registry.register(Registry.ITEM,new Identifier("tutorial","wool_helmet"), WOOL_HELMET);
Registry.register(Registry.ITEM,new Identifier("tutorial","wool_chestplate"), WOOL_CHESTPLATE);
Registry.register(Registry.ITEM,new Identifier("tutorial","wool_leggings"), WOOL_LEGGINGS);
Registry.register(Registry.ITEM,new Identifier("tutorial","wool_boots"), WOOL_BOOTS);
}
```

### Texturing

Since you already know how to make item models and textures, we won't go
over them here. (They're done exactly the same as items.) Armor textures
are done a little differently since Minecraft thinks it's a vanilla
armor item. For this, we'll make a `pack.mcmeta` file so our resources
can act like a resource pack.

```JavaScript
{
    "pack":{
        "pack_format":4,
        "description":"Tutorial Mod"
    }
}
```

Now you can finally place your textures here in
`src/main/resources/assets/minecraft/textures/models/armor/`. Keep in
mind that they're separated in 2 pictures. (Use vanilla textures for
reference.)

If you followed everything, you should now be able to have a full armor
set\!

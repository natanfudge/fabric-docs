## Ajouter des enchantements

Pour ajouter des enchantements Ã  votre mod, vous devrez :

- crÃ©er une classe qui Ã©tend `Enchantment` ou un autre enchantement
  existant (tel que `DamageEnchantment`)
- enregistrer votre enchantement
- ajouter des fonctionnalitÃ©s ou des mÃ©caniques personnalisÃ©es si
  besoin
- ajouter des traductions pour votre enchantement [1]

Des enchantements peuvent soit avoir des fonctionnalitÃ©s personnalisÃ©es
implÃ©mentÃ©es sÃ©parÃ©ment (telles que la cuisson de minerais minÃ©s), soit
utiliser des mÃ©caniques existantes (telles que `DamageEnchantment`), qui
sont appliquÃ©es lorsque cela est appropriÃ©. La classe `Enchantment` de
base a Ã©galement plusieurs mÃ©thodes pour crÃ©er des fonctionnalitÃ©s,
telles que la mÃ©thode `onTargetDamaged`.

### CrÃ©er une classe d'enchantement

Nous allons crÃ©er un enchantement appelÃ© *Frost*, qui ralentit les
crÃ©atures. La durabilitÃ© et la puissance de l'effet de lenteur
augmenteront en fonction du niveau de l'enchantement.

```java
public class FrostEnchantment extends Enchantment 
{
    public WrathEnchantment(Weight weight, EnchantmentTarget target, EquipmentSlot[] slots)
    {
        super(weight, target, slots)
    }
}
```

Vous devrez remplacer quelques mÃ©thodes pour dÃ©finir des fonctionnalitÃ©s
de base.

`getMinimumPower` est le niveau minimum requis pour obtenir
l'enchantement dans une table d'enchantement. Nous allons le dÃ©finir Ã 
1, pour que vous puissiez l'obtenir Ã  n'importe quel niveau :

```java
@Override
public int getMinimumPower(int int_1)
{
    return 1;
}
```

`getMaximumLevel` est le nombre de niveaux que l'enchantement possÃ¨de.
[2]

```java
@Override
public int getMaximumLevel()
{
    return 3;
}
```

Enfin, nous allons implÃ©menter notre effet de lenteur dans la mÃ©thode
`onTargetDamage`, qui est appelÃ©e lorsque vous frappez un ennemi avec un
outil qui possÃ¨de votre enchantement.

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

La logique est assez simple : si l'entitÃ© que nous frappons peut avoir
des effets de potion, un effet de lenteur lui ait donnÃ©. La durÃ©e est de
2 secondes par niveau et la puissance est Ã©quivalente au niveau.

### Enregistrer un enchantement

Enregistrer des enchantements suit le mÃªme processus que d'habitude :

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

Cela enregistre notre enchantement sous l'espace de noms
`tutorial:frost`, le dÃ©finit comme un enchantement trÃ¨s rare et
l'autorise seulement sur les outils de la main principale.

### Ajouter des traductions et tester

Vous aurez Ã©galement besoin d'ajouter une traduction pour votre
enchantement. Ouvrez le [fichier .lang de votre mod](../../French/tutoriel/lang.md)
[(en)](../../Modding-Tutorials/Miscellaneous/lang.md) et ajoutez une nouvelle entrÃ©e :

```json
{
    "enchantment.tutorial.frost": "Frost"
}
```

Si vous allez en jeu, [vous devriez pouvoir enchanter des armes de la
main principale avec votre nouvel
enchantement.](https://i.imgur.com/31nFl2H.png)

[1] Lorsque vous enregistrez des enchantements, des livres sont
automatiquement ajoutÃ©s au jeu pour chaque niveau. Le nom traduit de
l'enchantement (`enchantment.modid.nom_enchantement`) est ce qui
apparaÃ®t comme le nom du livre.

[2] Les enchantements ayant plus d'un niveau auront des chiffres romains
aprÃ¨s leur nom pour afficher le niveau. Si l'enchantement a seulement un
niveau, rien n'est ajoutÃ©.

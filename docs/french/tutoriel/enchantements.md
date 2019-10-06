## Ajouter des enchantements

Pour ajouter des enchantements à votre mod, vous devrez :

- créer une classe qui étend `Enchantment` ou un autre enchantement
  existant (tel que `DamageEnchantment`)
- enregistrer votre enchantement
- ajouter des fonctionnalités ou des mécaniques personnalisées si
  besoin
- ajouter des traductions pour votre enchantement \[1\]

Des enchantements peuvent soit avoir des fonctionnalités personnalisées
implémentées séparément (telles que la cuisson de minerais minés), soit
utiliser des mécaniques existantes (telles que `DamageEnchantment`), qui
sont appliquées lorsque cela est approprié. La classe `Enchantment` de
base a également plusieurs méthodes pour créer des fonctionnalités,
telles que la méthode `onTargetDamaged`.

### Créer une classe d'enchantement

Nous allons créer un enchantement appelé *Frost*, qui ralentit les
créatures. La durabilité et la puissance de l'effet de lenteur
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

Vous devrez remplacer quelques méthodes pour définir des fonctionnalités
de base.

`getMinimumPower` est le niveau minimum requis pour obtenir
l'enchantement dans une table d'enchantement. Nous allons le définir à
1, pour que vous puissiez l'obtenir à n'importe quel niveau :

```java
@Override
public int getMinimumPower(int int_1)
{
    return 1;
}
```

`getMaximumLevel` est le nombre de niveaux que l'enchantement possède.
\[2\]

```java
@Override
public int getMaximumLevel()
{
    return 3;
}
```

Enfin, nous allons implémenter notre effet de lenteur dans la méthode
`onTargetDamage`, qui est appelée lorsque vous frappez un ennemi avec un
outil qui possède votre enchantement.

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

La logique est assez simple : si l'entité que nous frappons peut avoir
des effets de potion, un effet de lenteur lui ait donné. La durée est de
2 secondes par niveau et la puissance est équivalente au niveau.

### Enregistrer un enchantement

Enregistrer des enchantements suit le même processus que d'habitude :

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
`tutorial:frost`, le définit comme un enchantement très rare et
l'autorise seulement sur les outils de la main principale.

### Ajouter des traductions et tester

Vous aurez également besoin d'ajouter une traduction pour votre
enchantement. Ouvrez le [fichier .lang de votre mod](../../French/tutoriel/lang.md)
[(en)](../../Modding-Tutorials/Miscellaneous/lang.md) et ajoutez une nouvelle entrée :

```json
{
    "enchantment.tutorial.frost": "Frost"
}
```

Si vous allez en jeu, [vous devriez pouvoir enchanter des armes de la
main principale avec votre nouvel
enchantement.](https://i.imgur.com/31nFl2H.png)

1. Lorsque vous enregistrez des enchantements, des livres sont
   automatiquement ajoutés au jeu pour chaque niveau. Le nom traduit de
   l'enchantement (`enchantment.modid.nom_enchantement`) est ce qui
   apparaît comme le nom du livre.

2. Les enchantements ayant plus d'un niveau auront des chiffres romains
   après leur nom pour afficher le niveau. Si l'enchantement a
   seulement un niveau, rien n'est ajouté.


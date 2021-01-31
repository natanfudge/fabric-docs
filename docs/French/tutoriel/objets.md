# Ajouter un objet

### Introduction

Ajout un objet de base est l'une des premiÃ¨res Ã©tapes du modding. Vous
allez devoir crÃ©er un objet `Item`, l'enregistrer, et lui donner une
texture. Pour ajouter un comportement supplÃ©mentaire Ã  l'objet, vous
aurez besoin d'une classe d'objet personnalisÃ©e. Dans ce tutoriel et
tous ceux Ã  venir, l'espace de noms `tutorial` est utilisÃ© comme un
espace de noms gÃ©nÃ©rique. Si vous avez un ID de mod diffÃ©rent, n'hÃ©sitez
pas Ã  l'utiliser Ã  la place.

### Enregistrer un objet

PremiÃ¨rement, crÃ©ez une instance de `Item`. Nous allons la stocker en
haut de notre classe d'initialisation. Le constructeur contient un objet
`Item.Settings`, qui est utilisÃ© pour dÃ©finir les propriÃ©tÃ©s de l'objet
telles que la catÃ©gorie d'inventaire, la durabilitÃ© et le nombre
d'objets par *stack*.

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouvel objet
    public static final Item FABRIC_ITEM = new Item(new Item.Settings().group(ItemGroup.MISC));
    [...]
}
```

Vous allez utiliser le systÃ¨me de registre vanilla pour enregistrer du
nouveau contenu. La syntaxe de base est
`Registry#register(type de registre, identifiant, contenu)`. Les types
de registre sont stockÃ©s sous forme de champs statiques dans l'objet
`Registry`, et l'identifiant est ce qui nomme votre contenu. Le contenu
est une instance de ce que vous ajoutez. Cette instance peut Ãªtre
appelÃ©e n'importe oÃ¹ tant que cela se produit pendant l'initialisation.

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouvel objet
    public static final Item FABRIC_ITEM = new Item(new Item.Settings().group(ItemGroup.MISC));
      
    @Override
    public void onInitialize()
    {
        Registry.register(Registry.ITEM, new Identifier("tutorial", "fabric_item"), FABRIC_ITEM);
    } 
}
```

Votre nouvel objet a dÃ©sormais Ã©tÃ© ajoutÃ© Ã  Minecraft. ExÃ©cutez la tÃ¢che
Gradle `runClient` pour le voir en jeu.

<img src="/tutorial/2019-02-17_16.50.44.png" width="400" />

### Ajouter une texture d'objet

Enregistrer une texture pour un objet nÃ©cessite un fichier *.json* de
modÃ¨le d'objet et une image de texture. Vous aurez besoin de les ajouter
dans votre rÃ©pertoire `resources`. Le chemin direct de chacun est :

    ModÃ¨le de l'objet : .../resources/assets/tutorial/models/item/fabric_item.json
    Texture de l'objet : .../resources/assets/tutorial/textures/item/fabric_item.png

Notre exemple de texture peut Ãªtre trouvÃ©
[ici](https://i.imgur.com/CqLSMEQ.png).

Si vous avez enregistrÃ© votre objet correctement lors de la premiÃ¨re
Ã©tape, votre jeu se plaindra d'un fichier de texture manquant d'une
maniÃ¨re semblable Ã  celle-ci :

    [Server-Worker-1/WARN]: Unable to load model: 'tutorial:fabric_item#inventory' referenced from: tutorial:fabric_item#inventory: java.io.FileNotFoundException: tutorial:models/item/fabric_item.json

Il vous indique simplement Ã  quel endroit il s'attend Ã  trouver votre
fichier -- en cas de doute, consultez le journal.

Voici un exemple de modÃ¨le d'objet de base :

```JavaScript
{
  "parent": "item/generated",
  "textures": {
    "layer0": "tutorial:item/fabric_item"
  }
}
```

Le parent de votre objet change la faÃ§on dont il est rendu dans la main
et est utile pour des choses telles que les blocs dans l'inventaire.
`item/handheld` est utilisÃ© pour les outils qui sont tenus par la partie
en bas Ã  gauche de la texture. `textures/layer0` est l'emplacement de
votre fichier de texture.

RÃ©sultat final avec la texture :

<img src="/tutorial/item_texture.png" width="400" />

### CrÃ©er une classe d'objet

Pour ajouter un comportement supplÃ©mentaire Ã  l'objet, vous devrez crÃ©er
une classe d'objet. Le constructeur par dÃ©faut nÃ©cessite un objet
`Item.Settings`.

```java
public class FabricItem extends Item
{
    public FabricItem(Settings settings)
    {
        super(settings);
    }
}
```

Un cas d'utilisation pratique pour une classe d'objet personnalisÃ©e
serait de jouer un son lorsque vous cliquez avec l'objet :

```java
public class FabricItem extends Item
{
    public FabricItem(Settings settings)
    {
        super(settings);
    }
      
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand)
    {
        playerEntity.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);
        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }
}
```

Remplacez l'ancien objet `Item` par une instance de votre nouvelle
classe d'objet :

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouvel objet
    public static final FabricItem FABRIC_ITEM = new FabricItem(new Item.Settings().group(ItemGroup.MISC));
    [...]
}
```

Si vous avez tout fait correctement, utiliser l'objet devrait maintenant
jouer un son.

### Et si je veux changer la taille du stack de mon objet ?

Pour cela, vous devez utiliser `maxCount(int taille)` dans les
paramÃ¨tres de l'objet pour spÃ©cifier la taille maximale du *stack*.
Notez que si votre objet est endommageable, vous ne pouvez pas spÃ©cifier
une taille de *stack* maximale, ou le jeu enverra une RuntimeException.

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouvel objet, oÃ¹ la taille de stack maximale est 16
    public static final FabricItem FABRIC_ITEM = new FabricItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16));
    [...]
}
```

### Ã‰tapes suivantes

[Ajouter votre objet Ã  son propre groupe
d'objets](../../French/tutoriel/groupes_objets.md).

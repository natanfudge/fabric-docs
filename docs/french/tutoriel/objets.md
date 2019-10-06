# Ajouter un objet

## Introduction

Ajout un objet de base est l'une des premières étapes du modding. Vous allez devoir créer un objet `Item`, l'enregistrer, et lui donner une texture. Pour ajouter un comportement supplémentaire à l'objet, vous aurez besoin d'une classe d'objet personnalisée. Dans ce tutoriel et tous ceux à venir, l'espace de noms `tutorial` est utilisé comme un espace de noms générique. Si vous avez un ID de mod différent, n'hésitez pas à l'utiliser à la place.

## Enregistrer un objet

Premièrement, créez une instance de `Item`. Nous allons la stocker en haut de notre classe d'initialisation. Le constructeur contient un objet `Item.Settings`, qui est utilisé pour définir les propriétés de l'objet telles que la catégorie d'inventaire, la durabilité et le nombre d'objets par _stack_.

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouvel objet
    public static final Item FABRIC_ITEM = new Item(new Item.Settings().group(ItemGroup.MISC));
    [...]
}
```

Vous allez utiliser le système de registre vanilla pour enregistrer du nouveau contenu. La syntaxe de base est `Registry#register(type de registre, identifiant, contenu)`. Les types de registre sont stockés sous forme de champs statiques dans l'objet `Registry`, et l'identifiant est ce qui nomme votre contenu. Le contenu est une instance de ce que vous ajoutez. Cette instance peut être appelée n'importe où tant que cela se produit pendant l'initialisation.

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

Votre nouvel objet a désormais été ajouté à Minecraft. Exécutez la tâche Gradle `runClient` pour le voir en jeu.

![](../../.gitbook/assets/2019-02-17_16.50.44%20%282%29.png)

## Ajouter une texture d'objet

Enregistrer une texture pour un objet nécessite un fichier _.json_ de modèle d'objet et une image de texture. Vous aurez besoin de les ajouter dans votre répertoire `resources`. Le chemin direct de chacun est :

```text
Modèle de l'objet : .../resources/assets/tutorial/models/item/fabric_item.json
Texture de l'objet : .../resources/assets/tutorial/textures/item/fabric_item.png
```

Notre exemple de texture peut être trouvé [ici](https://i.imgur.com/CqLSMEQ.png).

Si vous avez enregistré votre objet correctement lors de la première étape, votre jeu se plaindra d'un fichier de texture manquant d'une manière semblable à celle-ci :

```text
[Server-Worker-1/WARN]: Unable to load model: 'tutorial:fabric_item#inventory' referenced from: tutorial:fabric_item#inventory: java.io.FileNotFoundException: tutorial:models/item/fabric_item.json
```

Il vous indique simplement à quel endroit il s'attend à trouver votre fichier -- en cas de doute, consultez le journal.

Voici un exemple de modèle d'objet de base :

```javascript
{
  "parent": "item/generated",
  "textures": {
    "layer0": "tutorial:item/fabric_item"
  }
}
```

Le parent de votre objet change la façon dont il est rendu dans la main et est utile pour des choses telles que les blocs dans l'inventaire. `item/handheld` est utilisé pour les outils qui sont tenus par la partie en bas à gauche de la texture. `textures/layer0` est l'emplacement de votre fichier de texture.

Résultat final avec la texture :

![](../../.gitbook/assets/item_texture%20%281%29.png)

## Créer une classe d'objet

Pour ajouter un comportement supplémentaire à l'objet, vous devrez créer une classe d'objet. Le constructeur par défaut nécessite un objet `Item.Settings`.

```java
public class FabricItem extends Item
{
    public FabricItem(Settings settings)
    {
        super(settings);
    }
}
```

Un cas d'utilisation pratique pour une classe d'objet personnalisée serait de jouer un son lorsque vous cliquez avec l'objet :

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

Remplacez l'ancien objet `Item` par une instance de votre nouvelle classe d'objet :

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouvel objet
    public static final FabricItem FABRIC_ITEM = new FabricItem(new Item.Settings().group(ItemGroup.MISC));
    [...]
}
```

Si vous avez tout fait correctement, utiliser l'objet devrait maintenant jouer un son.

## Et si je veux changer la taille du stack de mon objet ?

Pour cela, vous devez utiliser `maxCount(int taille)` dans les paramètres de l'objet pour spécifier la taille maximale du _stack_. Notez que si votre objet est endommageable, vous ne pouvez pas spécifier une taille de _stack_ maximale, ou le jeu enverra une RuntimeException.

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouvel objet, où la taille de stack maximale est 16
    public static final FabricItem FABRIC_ITEM = new FabricItem(new Item.Settings().group(ItemGroup.MISC).maxCount(16));
    [...]
}
```

## Étapes suivantes

[Ajouter votre objet à son propre groupe d'objets](https://github.com/natanfudge/fabric-docs/tree/14ef4ec35beb42aa82da4d0c6a5e40f2806428b6/docs/French/tutoriel/groupes_objets.md).


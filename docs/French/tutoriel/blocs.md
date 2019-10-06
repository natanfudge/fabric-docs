# Ajouter un bloc

### Introduction

Pour ajouter un bloc à votre mod, vous devrez enregistrer une nouvelle
instance de la classe `Block`. Pour avoir plus de contrôle sur votre
bloc, vous pourrez créer une classe de bloc personnalisée. Nous verrons
également comment ajouter un modèle de bloc.

### Créer un bloc

Pour commencer, créez une instance de `Block` dans la classe principale
de votre mod. Le constructeur de `Block` utilise le builder
*FabricBlockSettings* pour définir les propriétés de base du bloc,
telles que la dureté et la résistance aux explosions.

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouveau bloc
    public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).build());
    [...]
}
```

### Enregistrer un bloc

Enregistrer des blocs est la même chose qu'enregistrer des objets.
Appelez `Registry.register` et écrivez les arguments appropriés.

```java
public class ExampleMod implements ModInitializer
{
    // création du bloc
    […]
    
    @Override
    public void onInitialize()
    {
        Registry.register(Registry.BLOCK, new Identifier("tutorial", "example_block"), EXAMPLE_BLOCK);
    }
}
```

Votre bloc ne sera *pas* accessible en tant qu'objet, mais il peut être
vu en jeu grâce à la commande `/setblock ~ ~ ~ tutorial:example_block`.

### Enregistrer un BlockItem

Dans la plupart des cas, vous souhaitez être capable de placer votre
bloc avec un objet. Pour cela, vous devez enregistrer un *BlockItem*
correspondant dans le registre des objets. Vous pouvez faire cela en
enregistrant une instance de `BlockItem` sous `Registry.ITEM`. Le nom de
registre de l'objet devrait normalement être le même que le nom de
registre du bloc.

```java
public class ExampleMod implements ModInitializer
{
    // création du bloc
    […]
    
    @Override
    public void onInitialize()
    {
        // enregistrement du bloc
        [...]
        
        Registry.register(Registry.ITEM, new Identifier("tutorial", "example_block"), new BlockItem(EXAMPLE_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    }
}
```

### Donner un modèle à votre bloc

Comme vous l'avez probablement remarqué, le bloc est simplement un motif
en damier noir et violet en jeu. C'est la façon qu'utilise Minecraft
pour vous montrer que le bloc n'a pas de modèle. Faire le modèle d'un
bloc est un peu plus difficile que faire le modèle d'un objet. Vous
aurez besoin de trois fichiers : un fichier d'états de bloc, un fichier
de modèle de bloc et un fichier de modèle d'objet si le bloc a un
`BlockItem`. Des textures sont également nécessaires si vous n'utilisez
pas celles vanilla. Les fichiers doivent être situés ici :

    États de bloc : src/main/resources/assets/tutorial/blockstates/example_block.json
    Modèle de bloc : src/main/resources/assets/tutorial/models/block/example_block.json
    Modèle d'objet : src/main/resources/assets/tutorial/models/item/example_block.json
    Texture de bloc : src/main/resources/assets/tutorial/textures/block/example_block.png

Le fichier d'états de bloc détermine quel modèle le bloc doit utiliser
en fonction de son état de bloc. Puisque notre bloc a seulement un état,
le fichier est aussi simple que cela :

```JavaScript
{
  "variants": {
    "": { "model": "tutorial:block/example_block" }
  }
}
```

Le fichier de modèle de bloc définit la forme et la texture de votre
bloc. Nous allons utiliser `block/cube_all`, qui nous permettra de
définir facilement la même texture sur toutes les faces du bloc.

```JavaScript
{
  "parent": "block/cube_all",
  "textures": {
    "all": "tutorial:block/example_block"
  }
}
```

Dans la plupart des cas, vous souhaitez que le bloc ait la même
apparence tenu en main. Pour cela, vous pouvez créer un fichier de
modèle d'objet qui hérite du fichier de modèle de bloc :

```JavaScript
{
  "parent": "tutorial:block/example_block"
}
```

Chargez Minecraft et votre bloc devrait finalement avoir une texture \!

### Ajouter une table de butin de bloc

Le bloc doit avoir une table de butin pour que des objets apparaissent
lorsque le bloc est détruit. En supposant que vous ayez créé un objet
pour votre bloc et que vous l'ayez enregistré avec le même nom que le
bloc, le fichier ci-dessous produira un butin de bloc régulier.

```JavaScript
{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "name": "tutorial:example_block"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:survives_explosion"
        }
      ]
    }
  ]
}
```

Lorsqu'il sera détruit en mode survie, le bloc donnera maintenant un
objet.

### Créer une classe de bloc

Afin de créer un bloc simple, l'approche ci-dessus fonctionne bien, mais
vous souhaitez parfois un bloc *spécial* ayant des mécaniques uniques.
Pour cela, nous allons créer une classe distincte qui étend `Block`. La
classe nécessite un constructeur qui prend un argument `BlockSettings`.

```java
public class ExampleBlock extends Block
{
    public ExampleBlock(Settings settings)
    {
        super(settings);
    }
}
```

Comme nous l'avons fait dans le tutoriel sur les objets, vous pouvez
remplacer des méthodes dans la classe du bloc pour avoir des
fonctionnalités personnalisées. Disons que vous souhaitez rendre votre
bloc transparent :

```java
@Environment(EnvType.CLIENT)
public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
}
```

Pour ajouter ce bloc dans le jeu, remplacez `new Block` par `new
ExampleBlock` lorsque vous l'enregistrez.

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouveau bloc
    public static final ExampleBlock EXAMPLE_BLOCK = new ExampleBlock(Block.Settings.of(Material.STONE));
    [...]
}
```

Votre bloc personnalisé devrait maintenant être transparent \!

### Étapes suivantes

[Ajouter des états simples à un bloc, comme des entiers et des
booléens](../../French/tutoriel/etats_de_bloc.md) [(en)](../../Modding Tutorials/Blocks and Block Entities/blockstate.md).

[Donner à des blocs une entité de bloc pour qu'ils puissent avoir des
états avancés, comme des inventaires et des
classes](../../French/tutoriel/entites_de_bloc.md) [(en)](../../Modding Tutorials/Blocks and Block Entities/blockentity.md).
Également nécessaire pour de nombreuses choses, comme des interfaces
graphiques et du rendu de bloc personnalisé.

# Ajouter un bloc

### Introduction

Pour ajouter un bloc Ã  votre mod, vous devrez enregistrer une nouvelle
instance de la classe `Block`. Pour avoir plus de contrÃ´le sur votre
bloc, vous pourrez crÃ©er une classe de bloc personnalisÃ©e. Nous verrons
Ã©galement comment ajouter un modÃ¨le de bloc.

### CrÃ©er un bloc

Pour commencer, crÃ©ez une instance de `Block` dans la classe principale
de votre mod. Le constructeur de `Block` utilise le builder
*FabricBlockSettings* pour dÃ©finir les propriÃ©tÃ©s de base du bloc,
telles que la duretÃ© et la rÃ©sistance aux explosions.

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouveau bloc
    public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).build());
    [...]
}
```

### Enregistrer un bloc

Enregistrer des blocs est la mÃªme chose qu'enregistrer des objets.
Appelez `Registry.register` et Ã©crivez les arguments appropriÃ©s.

```java
public class ExampleMod implements ModInitializer
{
    // crÃ©ation du bloc
    [â€¦]
    
    @Override
    public void onInitialize()
    {
        Registry.register(Registry.BLOCK, new Identifier("tutorial", "example_block"), EXAMPLE_BLOCK);
    }
}
```

Votre bloc ne sera *pas* accessible en tant qu'objet, mais il peut Ãªtre
vu en jeu grÃ¢ce Ã  la commande `/setblock ~ ~ ~ tutorial:example_block`.

### Enregistrer un BlockItem

Dans la plupart des cas, vous souhaitez Ãªtre capable de placer votre
bloc avec un objet. Pour cela, vous devez enregistrer un *BlockItem*
correspondant dans le registre des objets. Vous pouvez faire cela en
enregistrant une instance de `BlockItem` sous `Registry.ITEM`. Le nom de
registre de l'objet devrait normalement Ãªtre le mÃªme que le nom de
registre du bloc.

```java
public class ExampleMod implements ModInitializer
{
    // crÃ©ation du bloc
    [â€¦]
    
    @Override
    public void onInitialize()
    {
        // enregistrement du bloc
        [...]
        
        Registry.register(Registry.ITEM, new Identifier("tutorial", "example_block"), new BlockItem(EXAMPLE_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    }
}
```

### Donner un modÃ¨le Ã  votre bloc

Comme vous l'avez probablement remarquÃ©, le bloc est simplement un motif
en damier noir et violet en jeu. C'est la faÃ§on qu'utilise Minecraft
pour vous montrer que le bloc n'a pas de modÃ¨le. Faire le modÃ¨le d'un
bloc est un peu plus difficile que faire le modÃ¨le d'un objet. Vous
aurez besoin de trois fichiers : un fichier d'Ã©tats de bloc, un fichier
de modÃ¨le de bloc et un fichier de modÃ¨le d'objet si le bloc a un
`BlockItem`. Des textures sont Ã©galement nÃ©cessaires si vous n'utilisez
pas celles vanilla. Les fichiers doivent Ãªtre situÃ©s ici :

    Ã‰tats de bloc : src/main/resources/assets/tutorial/blockstates/example_block.json
    ModÃ¨le de bloc : src/main/resources/assets/tutorial/models/block/example_block.json
    ModÃ¨le d'objet : src/main/resources/assets/tutorial/models/item/example_block.json
    Texture de bloc : src/main/resources/assets/tutorial/textures/block/example_block.png

Le fichier d'Ã©tats de bloc dÃ©termine quel modÃ¨le le bloc doit utiliser
en fonction de son Ã©tat de bloc. Puisque notre bloc a seulement un Ã©tat,
le fichier est aussi simple que cela :

```JavaScript
{
  "variants": {
    "": { "model": "tutorial:block/example_block" }
  }
}
```

Le fichier de modÃ¨le de bloc dÃ©finit la forme et la texture de votre
bloc. Nous allons utiliser `block/cube_all`, qui nous permettra de
dÃ©finir facilement la mÃªme texture sur toutes les faces du bloc.

```JavaScript
{
  "parent": "block/cube_all",
  "textures": {
    "all": "tutorial:block/example_block"
  }
}
```

Dans la plupart des cas, vous souhaitez que le bloc ait la mÃªme
apparence tenu en main. Pour cela, vous pouvez crÃ©er un fichier de
modÃ¨le d'objet qui hÃ©rite du fichier de modÃ¨le de bloc :

```JavaScript
{
  "parent": "tutorial:block/example_block"
}
```

Chargez Minecraft et votre bloc devrait finalement avoir une texture !

### Ajouter une table de butin de bloc

Le bloc doit avoir une table de butin pour que des objets apparaissent
lorsque le bloc est dÃ©truit. En supposant que vous ayez crÃ©Ã© un objet
pour votre bloc et que vous l'ayez enregistrÃ© avec le mÃªme nom que le
bloc, le fichier ci-dessous produira un butin de bloc rÃ©gulier.

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

Lorsqu'il sera dÃ©truit en mode survie, le bloc donnera maintenant un
objet.

### CrÃ©er une classe de bloc

Afin de crÃ©er un bloc simple, l'approche ci-dessus fonctionne bien, mais
vous souhaitez parfois un bloc *spÃ©cial* ayant des mÃ©caniques uniques.
Pour cela, nous allons crÃ©er une classe distincte qui Ã©tend `Block`. La
classe nÃ©cessite un constructeur qui prend un argument `BlockSettings`.

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
remplacer des mÃ©thodes dans la classe du bloc pour avoir des
fonctionnalitÃ©s personnalisÃ©es. Disons que vous souhaitez rendre votre
bloc transparent :

```java
@Environment(EnvType.CLIENT)
public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
}
```

Pour ajouter ce bloc dans le jeu, remplacez `new Block` par
`new ExampleBlock` lorsque vous l'enregistrez.

```java
public class ExampleMod implements ModInitializer
{
    // une instance de notre nouveau bloc
    public static final ExampleBlock EXAMPLE_BLOCK = new ExampleBlock(Block.Settings.of(Material.STONE));
    [...]
}
```

Votre bloc personnalisÃ© devrait maintenant Ãªtre transparent !

### Ã‰tapes suivantes

[Ajouter des Ã©tats simples Ã  un bloc, comme des entiers et des
boolÃ©ens](../../French/tutoriel/etats_de_bloc.md) [(en)](../../Modding-Tutorials/Blocks-and-Block-Entities/blockstate.md).

[Donner Ã  des blocs une entitÃ© de bloc pour qu'ils puissent avoir des
Ã©tats avancÃ©s, comme des inventaires et des
classes](../../French/tutoriel/entites_de_bloc.md) [(en)](../../Modding-Tutorials/Blocks-and-Block-Entities/blockentity.md).
Ã‰galement nÃ©cessaire pour de nombreuses choses, comme des interfaces
graphiques et du rendu de bloc personnalisÃ©.

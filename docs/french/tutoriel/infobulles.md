# Ajouter une infobulle

Dans votre classe d'[objet](https://github.com/natanfudge/fabric-docs/tree/14ef4ec35beb42aa82da4d0c6a5e40f2806428b6/docs/French/tutoriel/objets.md), remplacez la méthode `appendTooltip` comme ceci \(voir les fichiers [.lang](https://github.com/natanfudge/fabric-docs/tree/14ef4ec35beb42aa82da4d0c6a5e40f2806428b6/docs/French/tutoriel/lang.md) [\(en\)](https://github.com/natanfudge/fabric-docs/tree/14ef4ec35beb42aa82da4d0c6a5e40f2806428b6/docs/Modding-Tutorials/Miscellaneous/lang.md) pour savoir comment traduire l'infobulle\) :

```java
@Override
public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
    tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip"));
}
```

Pour un bloc, faites la même chose mais remplacez la méthode `buildTooltip` dans votre classe de [bloc](https://github.com/natanfudge/fabric-docs/tree/14ef4ec35beb42aa82da4d0c6a5e40f2806428b6/docs/French/tutoriel/blocs.md) [\(en\)](https://github.com/natanfudge/fabric-docs/tree/14ef4ec35beb42aa82da4d0c6a5e40f2806428b6/docs/Modding-Tutorials/Blocks-and-Block-Entities/block.md).


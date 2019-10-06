# Ajouter une infobulle

Dans votre classe d'[objet](objets.md), remplacez la méthode `appendTooltip` comme ceci \(voir les fichiers [.lang](https://github.com/natanfudge/fabric-docs/tree/fb92e6ab23f58adab5aea8a405e821d5669beb39/docs/French/tutoriel/lang.md) [\(en\)](../../modding-tutorials/miscellaneous/lang.md) pour savoir comment traduire l'infobulle\) :

```java
@Override
public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
    tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip"));
}
```

Pour un bloc, faites la même chose mais remplacez la méthode `buildTooltip` dans votre classe de [bloc](blocs.md) [\(en\)](../../modding-tutorials/blocks-and-block-entities/block.md).


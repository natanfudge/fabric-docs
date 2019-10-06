# Ajouter une infobulle

Dans votre classe d'[objet](.), remplacez la méthode
`appendTooltip` comme ceci (voir les fichiers [.lang](.)
[(en)](.) pour savoir comment traduire l'infobulle) :

```java
@Override
public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
    tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip"));
}
```

Pour un bloc, faites la même chose mais remplacez la méthode
`buildTooltip` dans votre classe de [bloc](.)
[(en)](.).

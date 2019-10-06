# Adding a tooltip

In your [item](../Tutorials/items.md) class, override `appendTooltip` like so
(see [lang](../Tutorials/lang.md) for how to translate the tooltip) :

```java
@Override
public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
    tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip"));
}
```

For a block, do the same but override `buildTooltip` in your
[block](../Tutorials/blocks.md) class.

# Adding a tooltip

In your [item](.) class, override `appendTooltip` like so
(see [lang](.) for how to translate the tooltip) :

```java
@Override
public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
    tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip"));
}
```

For a block, do the same but override `buildTooltip` in your
[block](.) class.

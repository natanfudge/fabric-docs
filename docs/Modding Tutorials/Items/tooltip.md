# Adding a tooltip

In your [item](../Modding Tutorials/Items/item.md) class, override `appendTooltip` like so
(see [lang](../Modding Tutorials/Miscellaneous/lang.md) for how to translate the tooltip) :

```java
@Override
public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
    tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip"));
}
```

For a block, do the same but override `buildTooltip` in your
[block](../Modding Tutorials/Blocks and Block Entities/block.md) class.

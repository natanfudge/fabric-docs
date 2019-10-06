# Adding a tooltip

In your [item](https://github.com/natanfudge/fabric-docs/tree/fb92e6ab23f58adab5aea8a405e821d5669beb39/docs/Modding%20Tutorials/Modding%20Tutorials/Items/item.md) class, override `appendTooltip` like so \(see [lang](https://github.com/natanfudge/fabric-docs/tree/fb92e6ab23f58adab5aea8a405e821d5669beb39/docs/Modding%20Tutorials/Modding%20Tutorials/Miscellaneous/lang.md) for how to translate the tooltip\) :

```java
@Override
public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
    tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip"));
}
```

For a block, do the same but override `buildTooltip` in your [block](https://github.com/natanfudge/fabric-docs/tree/fb92e6ab23f58adab5aea8a405e821d5669beb39/docs/Modding%20Tutorials/Modding%20Tutorials/Blocks%20and%20Block%20Entities/block.md) class.


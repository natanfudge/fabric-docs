# Block Biome Coloring

In this tutorial, we'll look at adding biome-dependent colors to new
blocks. To start, you'll need a block with a model that accounts for
tintindex. To see an example of this, view the base leaves or
grass\_block model file.

Remember to keep visual-related logic client-side,
(*onInitializeClient*) or it will crash on a server. To register a
custom block coloring, use *ColorProviderRegistry.BLOCK.register*, and
for items, use *ColorProviderRegistry.ITEM.register*. In this tutorial,
the grass biome color will be the one used. Replace the final argument
by passing in your block.

```java
public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> {
            BlockColorProvider provider = ColorProviderRegistry.BLOCK.get(Blocks.GRASS);
            return provider == null ? -1 : provider.getColor(block, pos, world, layer);
        }, YOUR_BLOCK_INSTANCE);
    }
}
```

So, what's happening here? The register method wants a color returned,
and in this case, that color is taken from the grass block. Coloring an
item is very similar. Like blocks, the returned color could be any, and
also remember to replace the final argument with an instance of your
item.

```java
public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register((item, layer) -> {
            // These values are represented as temperature and humidity, and used as coordinates for the color map
            double temperature = 0.5D; // a double value between 0 and 1
            double humidity = 1.0D; // a double value between 0 and 1
            return GrassColorHandler.getColor(temperature, humidity);
        }, YOUR_ITEM_INSTANCE);
    }
}
```

Finished\!

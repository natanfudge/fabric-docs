# Adding Generator Types

Generator type is a wrapper around a chunk generator and shows up on the
world generation menu as world type. If you don't know what generator
type is, you may want to scroll down and see the result section of this
page first.

There are 2 steps that are required to add a generator type.

- Creating a generator type
- Registering a generator type

In this tutorial, we will add a simple generator type, which generates
nothing in a world.

## Creating a Generator Type

To create a generator type, you have to make a class that extends
`GeneratorType` and implement `getChunkGenerator` method. In this
tutorial, we reuse existing chunk generator `FlatChunkGenerator` to
achieve void world. Feel free to replace it with your custom chunk
generator.

```java
public class ExampleMod implements ModInitializer {
  private static final GeneratorType VOID = new GeneratorType("void") {
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry,
        Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
      FlatChunkGeneratorConfig config = new FlatChunkGeneratorConfig(
          new StructuresConfig(Optional.empty(), Collections.emptyMap()), biomeRegistry);
      config.updateLayerBlocks();
      return new FlatChunkGenerator(config);
    }
  };
}
```

The argument of the constructor is a translation key. It is used when it
localizes the world generation menu.

The constructor of `GeneratorType` is a private, so you have to use
[Access Wideners](../Modding-Tutorials/accesswideners.md) to loosen the access. Your
access widener file should look like this:

    accessWidener v1 named
    
    extendable method net/minecraft/client/world/GeneratorType <init> (Ljava/lang/String;)V

## Registering a Generator Type

We register our generator type at the entrypoint `onInitialize`.

```java
public class ExampleMod implements ModInitializer {
  @Override
  public void onInitialize() {
    GeneratorTypeAccessor.getValues().add(VOID);
  }
}
```

```java
@Mixin(GeneratorType.class)
public interface GeneratorTypeAccessor {
  @Accessor("VALUES")
  public static List<GeneratorType> getValues() {
    throw new AssertionError();
  }
}
```

You should also give your generator type a language entry in your
`en_us.json` file:

```JavaScript
{
  "generator.void": "Nothing but Void"
}
```

## Result

Try your own generator type!

![generator\_type.png](../images/tutorial/generator_type.png)

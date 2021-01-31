# Mixin Accessors

Mixin Accessors allow you to access fields and methods that are not
visible (private) or final.

## Accessor

`@Accessor` allows you to access fields. Suppose we want to access
`itemUseCooldown` field of `MinecraftClient` class.

### Getting a value from the field

```java
@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor("itemUseCooldown")
    public int getItemUseCooldown();
}
```

Usage:

```java
int itemUseCooldown = ((MinecraftClientAccessor) MinecraftClient.getInstance()).getItemUseCooldown();
```

### Setting a value to the field

```java
@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor("itemUseCooldown")
    public void setItemUseCooldown(int itemUseCooldown);
}
```

Usage:

```java
((MinecraftClientAccessor) MinecraftClient.getInstance()).setItemUseCooldown(100);
```

## Accessor for static fields

Suppose we want to access `BIOMES` field of `VanillaLayeredBiomeSource`
class.

### Getting a value from the field

```java
@Mixin(VanillaLayeredBiomeSource.class)
public interface VanillaLayeredBiomeSourceAccessor {
  @Accessor("BIOMES")
  public static List<RegistryKey<Biome>> getBiomes() {
    throw new AssertionError();
  }
}
```

Usage:

```java
List<RegistryKey<Biome>> biomes = VanillaLayeredBiomeSourceAccessor.getBiomes();
```

### Setting a value to the field

```java
@Mixin(VanillaLayeredBiomeSource.class)
public interface VanillaLayeredBiomeSourceAccessor {
  @Accessor("BIOMES")
  public static void setBiomes(List<RegistryKey<Biome>> biomes) {
    throw new AssertionError();
  }
}
```

Usage:

```java
VanillaLayeredBiomeSourceAccessor.setBiomes(biomes);
```

## Invoker

`@Invoker` allows you to access methods. Suppose we want to invoke
`teleportTo` method of `EndermanEntity` class.

```java
@Mixin(EndermanEntity.class)
public interface EndermanEntityInvoker {
  @Invoker("teleportTo")
  public boolean invokeTeleportTo(double x, double y, double z);
}
```

Usage:

```java
EndermanEntity enderman = ...;
((EndermanEntityInvoker) enderman).invokeTeleportTo(0.0D, 70.0D, 0.0D);
```

## Invoker for static methods

Suppose we want to invoke `registerPotionType` method of
`BrewingRecipeRegistry` class.

```java
@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryInvoker {
  @Invoker("registerPotionType")
  public static void invokeRegisterPotionType(Item item) {
    throw new AssertionError();
  }
}
```

Usage:

```java
BrewingRecipeRegistryInvoker.invokeRegisterPotionType(item);
```


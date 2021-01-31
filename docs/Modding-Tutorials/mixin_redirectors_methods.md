# Redirecting methods

Method redirectors can use the following [injection point
references](https://github.com/SpongePowered/Mixin/wiki/Injection-Point-Reference):

- `INVOKE`; and
- `INVOKE_STRING`.

## INVOKE

The `INVOKE` injection point reference is used for invocations of
`target` in `method`, which means that it can be used in order to
redirect a method immediately before it is called.

### Redirecting a static method

Static method redirectors should have the same parameters as the
`target`.

redirecting the `ItemStack::fromTag(ListTag)` call in
`SimpleInventory::readTags` to return `null`:

```java
@Mixin(SimpleInventory.class)
abstract class SimpleInventoryMixin {
    @Redirect(method = "readTags",
              at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;fromTag(Lnet/minecraft/nbt/ListTag;)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack returnNull(ListTag tag) {
        return null;
    }
}
```

### Redirecting an instance method

Instance method redirectors are similar to static methood redirectors,
but they should have an additional parameter at the start of their
parameter lists for the objects on which their `target`s are invoked.

redirecting the `Entity::dropItem(ItemConvertible, int)` call in
`Entity::dropItem(ItemConvertible)` to remove diamonds instead of
dropping them by replacing them with air:

```java
@Mixin(Entity.class)
abstract class EntityMixin {
    @Redirect(method = "dropItem",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;dropItem(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/entity/ItemEntity;"))
    private ItemEntity replaceDroppedItem(Entity droppingEntity, ItemConvertible item, int yOffset) {
        return droppingEntity.dropItem(item == Items.DIAMOND ? Items.AIR : item, yOffset);
    }
}
```

## INVOKE\_STRING

The `INVOKE_STRING` injection point reference is used for matching
invocations of `target` in `method` if `target` is a method with a
single `String` parameter and a `String` literal is passed to it. The
`String` literal to capture should be specified in the `args` property
of `At`.

redirecting the `Profiler::push` invocation with `"tick"` passed to it
in `MinecraftClient::render` in order to modify the `location` passed in
the said invocation:

```java
@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {
    @Redirect(method = "render",
              at = @At(value = "INVOKE_STRING",
                       target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V",
                       args = "ldc=tick"))
    private void redirectPush(Profiler profiler, String location) {
        profiler.push("modified tick");
        System.out.println(location);
    }
}
```


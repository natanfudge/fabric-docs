# Adding a BlockEntity

## Introduction

A BlockEntity is primarily used to store data within blocks. Before
creating one, you will need a [Block](.). This tutorial
will cover the creation of your BlockEntity class, and it's
registration.

## Creating a BlockEntity

The simplest Block Entity simply extends `BlockEntity`, and uses the
default constructor. This is perfectly valid, but will not grant any
special functionality to your block.

```java
public class DemoBlockEntity extends BlockEntity {
   public DemoBlockEntity() {
      super(ExampleMod.DEMO_BLOCK_ENTITY);
   }
}
```

Bellow will show you how to create the `ExampleMod.DEMO_BLOCK_ENTITY`
field.

You can simply add variables to this barebone class or implement
interfaces such as `Tickable` and `Inventory` to add more functionality.
`Tickable` provides a single `tick()` method, which is called once per
tick for every loaded instance of your Block in the world., while
`Inventory` allows your BlockEntity to interact with automation such as
hoppers - there will likely be a separate tutorial dedicated entirely to
this interface later.

## Registring your BlockEntity

Once you have created the `BlockEntity` class, you will need to register
it for it to function. The first step of this process is to create a
`BlockEntityType`, which links your `Block` and `BlockEntity` together.
Assuming your `Block` has been created and saved to a local variable
`DEMO_BLOCK`, you would create the matching `BlockEntityType` with the
line below. `modid:demo` should be replaced by your Mod ID and the name
you want your `BlockEntity` to be registered under.

The `BlockEntityType` should be registered in your `onInitialize`
method, this is to ensure it gets registered at the correct time.

```java
public static BlockEntityType<DemoBlockEntity> DEMO_BLOCK_ENTITY;

@Override
public void onInitialize() {
   DEMO_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, "modid:demo", BlockEntityType.Builder.create(DemoBlockEntity::new, DEMO_BLOCK).build(null));
}
```

Once your `BlockEntityType` has been created and registered as seen
above, you can simply implement `BlockEntityProvider` in your `Block`
class:

```java
@Override
public BlockEntity createBlockEntity(BlockView blockView) {
   return new DemoBlockEntity();
}
```

## Serializing Data

If you want to store any data in your `BlockEntity`, you will need to
save and load it, or it will only be held while the `BlockEntity` is
loaded, and the data will reset whenever you come back to it. Luckily,
saving and loading is quite simple - you only need to override `toTag()`
and `fromTag()`.

`toTag()` returns a `CompoundTag`, which should contain all of the data
in your `BlockEntity`. This data is saved to the disk and also send
through packets if you need to sync your `BlockEntity` data with
clients. It is very important to call the default implementation of
`toTag`, as it saves "Identifying Data" (position and ID) to the tag.
Without this, any further data you try and save will be lost as it is
not associated with a position and `BlockEntityType`. Knowing this, the
example below demonstrates saving an integer from your `BlockEntity` to
the tag. In the example, the integer is saved under the key `"number"` -
you can replace this with any string, but you can only have one entry
for each key in your tag, and you will need to remember the key in order
to retrieve the data later.

```java
public class DemoBlockEntity extends BlockEntity {

   // Store the current value of the number
   private int number = 7;
   
   public DemoBlockEntity() {
      super(ExampleMod.DEMO_BLOCK_ENTITY);
   }
   
   // Serialize the BlockEntity
   public CompoundTag toTag(CompoundTag tag) {
      super.toTag(tag);
      
      // Save the current value of the number to the tag
      tag.putInt("number", number);
      
      return tag;
   }
}
```

In order to retrieve the data later, you will also need to override
`fromTag`. This method is the opposite of `toTag` - instead of saving
your data to a `CompoundTag`, you are given the tag which you saved
earlier, enabling you to retrieve any data that you need. As with
`toTag`, it is essential that you call `super.fromTag`, and you will
need to use the same keys to retrieve data that you saved. To retrieve,
the number we saved earlier, see the example below.

```java
// Deserialize the BlockEntity
public void fromTag(CompoundTag tag) {
   super.fromTag(tag);
   number = tag.getInt("number");
}
```

Once you have implemented the `toTag` and `fromTag` methods, you simply
need to ensure that they are called at the right time. Whenever your
`BlockEntity` data changes and needs to be saved, call `markDirty()`.
This will force the `toTag` method to be called when the world is next
saved by marking the chunk which your block is in as dirty. As a general
rule of thumb, simply call `markDirty()` whenever you change any custom
variable in your `BlockEntity` class.

If you need to sync some of your `BlockEntity` data to the client, for
purposes such as rendering, you should implement
`BlockEntityClientSerializable` from the Fabric API. This class provides
the `fromClientTag` and `toClientTag` methods, which work much the same
as the previously discussed `fromTag` and `toTag` methods, except that
they are used specifically for sending to and receiving data on the
client.

## Overview

You should now have your very own `BlockEntity`, which you can expand in
various ways to suit your needs. You registered a `BlockEntityType`, and
used it to connect your `Block` and `BlockEntity` classes together.
Then, you implemented `BlockEntityProvider` in your `Block` class, and
used the interface to provide an instance of your new `BlockEntity`.
Finally, you learned how to save data to your `BlockEntity`, and how to
retrieve for use later.

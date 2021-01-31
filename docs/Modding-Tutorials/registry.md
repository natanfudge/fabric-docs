# Registry System

You will need to register most content you add to the game. This helps
with:

- Letting the game know your content exists
- Verifying game content between client & server
- Handling invalid content in a save
- Preventing conflicts between different mods
- Compression for client &lt;-&gt; server communication and data
  saving
- Abstracting or hiding numerical IDs

When registering any type of content, you pass in an `Identifier`, which
is a label for your addition. Identifiersâ€“ often abbreviated as IDsâ€“
have a namespace and path. In most cases, the namespace is the ID of
your mod, and the path is the name of the content you're registering.
For example, the standard dirt block has the ID of `minecraft:dirt`.

Using custom content without registering it can lead to buggy behavior,
such as missing textures, world save issues, and crashes. The game will
usually let you know if you forget to register something.

## Registry Types

When registering content, you need to specify which registry you are
adding content to. The base game provides registries for all vanilla
content, which can be found in `Registry`. Two examples of registries
you may use include `Registry.ITEM` for items and `Registry.BLOCK` for
blocks.

For a deeper overview and description of all available registries, read
the [registry types](../Modding-Tutorials/registry_types.md) page.

## Registering Content

Use `Registry.register` for adding content to registries:

```java
public static <T> T register(Registry<? super T> registry, Identifier id, T entry) {
    return ((MutableRegistry)registry).add(id, entry);
}
```

**registry** - an instance of the registry you want to add content to. A
list of all vanilla registries, located in `Registry`, can be found in
the [registry types](../Modding-Tutorials/registry_types.md) page.

**id** - an identifying label for your content inside the registry.
Standard convention is `modid:name`, as seen with `minecraft:dirt`.

**entry** - an instance of the content you want to register.

## Registry Methods

`get` - returns the entry associated with an ID inside a registry. If
the entry doesn't exist, `DefaultedRegistry` returns the default
registry value, and `SimpleRegistry` returns null.

```java
@Nullable
public abstract T get(@Nullable Identifier id);
```

------------------------------------------------------------------------

`getId` - returns the `Identifier` associated with an entry inside a
registry. If the entry doesn't exist, `DefaultedRegistry` returns the
default registry identifier, and `SimpleRegistry` returns null.

```java
@Nullable
public abstract Identifier getId(T entry);
```

------------------------------------------------------------------------

`getRawId` - returns the internal integer ID associated with an entry
inside a registry. If the entry doesn't exist, `DefaultedRegistry`
returns the raw ID of the default registry value, and `SimpleRegistry`
returns -1.

```java
public abstract int getRawId(@Nullable T entry);
```


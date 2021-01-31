# Color Providers

Ever wonder how grass and leaves change hues depending on the biome, or
how leather armor can have seemingly infinite color patterns? Meet color
providers, which allow you to hue and tint block & item model textures
based on properties such as location, NBT, or block states.

#### Existing Examples

First, what existing vanilla content uses color providers? A few
examples include:

- grass
- leaves
- leather armor dying
- redstone wire
- plants such as melons, sugarcane, and lilypads
- tipped arrows

The color provider is powerful, but Mojang has opted to stick with
individual textures for colored blocks such as concrete, wool, and
glass. The primary use case at this point is for biome shaded blocks and
small tweaks to existing textures, such as the colored end of a tipped
arrow.

The concept behind color providers is simple. You register a block or
item to them, and when the block or item's model is rendered, the color
provider applies a hue tweak to each layer of the texture. Both
providers give you access to the layer of the model, which means you can
hue each portion of a model separately, which is the case in leather
armor & tipped arrows. This is useful for when you only want to change a
few pixels, but not the entire texture.

Remember that the color provider is a client-side mechanic. Make sure to
put any code related to it inside a client initializer.

## Registering a Block Color Provider

To register a block to the block color provider, you'll need to use
Fabric's `ColorProviderRegistry`. There is an instance of the `BLOCK`
and `ITEM` provider inside this class, which you can call register on.
The register method takes an instance of your color provider and a
varargs of every block you want to color with the provider.

```java
ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x3495eb, MY_BLOCK);
```

All we do here is say, "Hi, `MY_BLOCK` should be colored 0x3495eb,"
which is a blue color. You have `BlockState`, `World`, and `BlockPos`
context, which is how you can change colors based on biome or position.
The final int is the tintIndex; each one asks for a color individually,
but in this case, we're always returning blue.

If you need to access `BlockEntity` data in the color provider, you'll
want to implement `RenderAttachmentBlockEntity` to return the data you
need. This is because blocks can be rendered on separate threads, so
accessing the data directly is not safe. Additionally, if you query
blocks with `getBlockState` you won't be able to view the entire world -
make sure you only query within +-2 blocks x/y/z of the current
position.

The model is also important: the main note here is that you are
*required* to define a tintindex for each portion of the model you want
to hue. To see an example of this, check out `leaves.json`, which is the
base model used for vanilla leaves. Here's the model used for our block:

```json
{
  "parent": "block/block",
  "textures": {
    "all": "block/white_concrete",
    "particle": "#all"
  },
  "elements": [   {   "from": [ 0, 0, 0 ],
      "to": [ 16, 16, 16 ],
      "faces": {
        "down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#all", "tintindex": 0, "cullface": "down" },       "up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#all", "tintindex": 0, "cullface": "up" },
        "north": { "uv": [ 0, 0, 16, 16 ], "texture": "#all", "tintindex": 0, "cullface": "north" },       "south": { "uv": [ 0, 0, 16, 16 ], "texture": "#all", "tintindex": 0, "cullface": "south" },
        "west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#all", "tintindex": 0, "cullface": "west" },       "east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#all", "tintindex": 0, "cullface": "east" }
      }
    }
  ]
}
```

In this instance, we're adding a single tintindex, which is what would
appear in the `tintIndex` parameter (tint index 0).

Here's the final result-- note that the original model used the
`white_concrete` texture: ![](https://i.imgur.com/fZLS10g.png)

## Registering an Item Color Provider

Items are similar; the difference is the context provided. Instead of
having a state, world, or position, you have access to the `ItemStack`.

```java
ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0x3495eb, COLORED_ITEM);
```

This would hue the item in our inventory in the same fashion as the
block.

#### Limitations

One key issue with using the color provider is the lack of context in
the item provider. This is why vanilla grass doesn't change colors in
your inventory depending on where you stand. For implementing things
such as color variants of blocks (concrete, glass, wool, etc.), you're
encouraged to simply provide an individual texture for each version.

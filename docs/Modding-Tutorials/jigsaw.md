# Jigsaws

Jigsaws are good for advanced structures such as dungeons & villages,
and allow you to spend more time on actually building content vs.
messing with procedural generation code.

A repository with finished code can be found
[here](https://github.com/Draylar/jigsaw-example-mod).

### Creating a StructureFeature

A `StructureFeature` is an advanced `Feature`: it keeps track of its
location and bounds, and also has the ability to generate itself from a
structure file\[1\]. If it helps, you can think of it as a `Structure` +
`Feature`. We'll need to create one for our jigsaw generated structure.
To start, create a class that extends
`StructureFeature<DefaultFeatureConfig>`\[2\]. Feature naming convention
is "structure name" + "Feature"; a few vanilla examples are
`EndCityFeature`, `OceanRuinFeature`, and `VillageFeature`.

*Note: while Feature is the proper name for something generated in the
world, we'll refer to our addition as a Structure. This is to
distinguish between a StructureFeature and a standard Feature.*

We'll keep the constructor as-is. The `Function<Dynamic<?>>` parameter
is the structure config-- if you don't have any plans to mess with it,
you can simply pass `DefaultFeatureConfig::deserialize` into super:

```java
public ExampleFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> config) {
    super(config);
}
```

*shouldStartAt* answers the question "should I start generating at the
given Chunk?" `AbstractTempleFeature` is a class offered by vanilla that
offers an answer to this question: it guarantees each structure is
spaced out from others of the same type. The standard village feature
uses the same logic but isn't a child of the class. By returning true,
every chunk will have your feature. This is good for testing, so we'll
use that route for now.

```java
@Override
public boolean shouldStartAt(ChunkGenerator<?> chunkGenerator, Random random, int x, int z) {
    return true;
}
```

*getName* is the name of your structure. It's used for a few things,
including:

- Serializing and Deserializing your structure from a chunk
- Locating your structure
- Labeling your structure

Vanilla convention is a properly capitalized title. "Igloo," "Village,"
and "Fortress," are all valid.\[3\]

```java
@Override
public String getName() {
    return "Example Feature";
}
```

The exact usage for *getRadius* is still under discussion, but my guess
(as the writer) is that it's related to translation within a chunk. A
structure with a radius of 4 may be moved around within the chunk
bounds, while a structure with a radius of 8 will always be centered.
This may be wrong. For now, the radius should be half the size of your
structure; if your structure will take up more than a single chunk (or
fill it entirely), return 8.

```java
@Override
public int getRadius() {
    return 2;
}
```

Finally, *getStructureStartFactory* is the generation portion of your
StructureFeature. You'll have to return a factory method to create new
StructureStarts-- we can simply method reference the constructor. Our
implementation will look like this, with `ExampleStructureStart` being
the next step in this tutorial:

```java
@Override
public StructureStartFactory getStructureStartFactory() {
    return ExampleStructureStart::new;
}
```

Our finalized `ExampleFeature` class:

```java
import com.mojang.datafixers.Dynamic;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;
import java.util.function.Function;

public class ExampleFeature extends StructureFeature<DefaultFeatureConfig> {

    public ExampleFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean shouldStartAt(ChunkGenerator<?> chunkGenerator, Random random, int x, int z) {
        return true;
    }

    @Override
    public StructureStartFactory getStructureStartFactory() {
        return ExampleStructureStart::new;
    }

    @Override
    public String getName() {
        return "Example Feature";
    }

    @Override
    public int getRadius() {
        return 2;
    }
}
```

### Creating a StructureStart class

`StructureStart` is like the initialization stage of generating our
structure in the world. For now, create a simple child class with a
constructor that also overrides initialize:

```java
public class ExampleStructureStart extends StructureStart {

    ExampleStructureStart(StructureFeature<?> feature, int x, int z, Biome biome, MutableIntBoundingBox box, int int_3, long seed) {
        super(feature, x, z, biome, box, int_3, seed);
    }

    @Override
    public void initialize(ChunkGenerator<?> chunkGenerator, StructureManager structureManager, int x, int z, Biome biome) {
    
    }
}
```

To understand what happens here, we'll have to dive into jigsaws
(<https://minecraft.gamepedia.com/Jigsaw_Block>) and structure blocks
(<https://minecraft.gamepedia.com/Structure_Block>).

Structure Blocks are a simple way of saving a structure to a .nbt file
for future use. Jigsaws are a component of structure blocks that
assemble multiple structures into a single one; similar to normal
jigsaws, each piece of the structure connects at a jigsaw block, which
is like a connection wedge in a puzzle piece. We'll assume you're
familiar with saving structures-- if you aren't, read up on the
structure block page before going any further.

The jigsaw menu consists of 3 fields: \* target pool \* attachment type
\* turns into

![Blank Jigsaw](https://i.imgur.com/owaJ0k2.png)

When thinking about this as a puzzle, the target pool is the group of
puzzle pieces you can search through. If you have a total of 10 pieces,
one target pool may have 7 of the total pieces. This field is how a
jigsaw specifies, "Hi, I'd like a piece from group B to connect to me\!"
In the case of a village, this may be a road saying, "Give me a house\!"
The target pools of 2 jigsaws do not have to match: the requestor gets
to decide who they select from. It is **not** defining what type *the
given* jigsaw block is, but rather what type should be on the *other
side*.

The attachment type can be seen as a more specific filter within target
pools-- a jigsaw can only connect to other jigsaws with the same
attachment type. This is like the type of connector on a puzzle piece.
The usages for this are a little bit more specific.

Finally, the "turns into" field is simply what the jigsaw is replaced
with after it finds a match. If the jigsaw is inside your cobblestone
floor, it should probably turn into cobblestone.

Here's an example implementation: the given jigsaw will draw from the
*tutorial:my\_pool* structure pool, looks for any jigsaws with the
*tutorial:any* type, and turns into cobblestone when it's done.

![Example Finished Jigsaw](https://i.imgur.com/f9tP2sv.png)

Our finalized structure will consist of multiple colored squares
connecting to each other. It will have a white or a black square in the
center, and orange, magenta, light blue, and lime squares branching off
on the sides randomly. Here is the setup of our 2 initial squares:
![Initial Squares](https://i.imgur.com/dVFADy8.png)

This jigsaw will ask for any other jigsaw that: \* is in the
*tutorial:color\_pool* target pool \* has an attachment type of
*tutorial:square\_edge* It then turns into white concrete to match the
rest of the platform.

For demo purposes, we've made 2 starting platforms: one is white, and
one is black. The only difference is what they turn into. We'll save
these as structure files using structure blocks: ![Finalized Initial
Squares](https://i.imgur.com/31LAORw.png)

For our randomized edge platforms, we've made 4 extra squares of
different colors. Again, despite being used for a different purpose, the
jigsaw construction is *the same* aside from the "turns into" field.
![Colored Squares](https://i.imgur.com/OngxweJ.png)

We now have 6 saved `.nbt` files. These can be found in our world save
folder under `generated`: ![Saved NBT
files](https://i.imgur.com/ZKIoZT9.png)

For usage, we'll move these to `resources/data/tutorial/structures`,
where "tutorial" is your modid: ![Moved NBT
files](https://i.imgur.com/kaiy84U.png)

The setup is complete\! We now have 6 total squares. Let's briefly recap
the goal: \* have a white or black square selected as the center for our
structure \* have a pool of the 4 other colors \* branch off from the
center square with our 4 extra colors

Let's head back to our `ExampleStructureStart` class. First, we'll need
2 Identifiers to label our 2 pools (black\&white, 4 colors):

```java
private static final Identifier BASE_POOL = new Identifier("tutorial:base_pool");
private static final Identifier COLOR_POOL = new Identifier("tutorial:color_pool");
```

Remember: every jigsaw ends up searching through the color pool, but we
still have a base pool\! This is to keep our black & white squares out
of the outside generated squares. It's also going to be our origin pool,
where we randomly select 1 structure from to begin our generation.

In a static block at the bottom of our class, we're going to register
our structure pools using `StructurePoolBasedGenerator.REGISTRY`:

```java
static {
        StructurePoolBasedGenerator.REGISTRY.add(
                new StructurePool(
                        BASE_POOL,
                        new Identifier("empty"),
                        ImmutableList.of(
                                Pair.of(new SinglePoolElement("tutorial:black_square"), 1),
                                Pair.of(new SinglePoolElement("tutorial:white_square"), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );

        StructurePoolBasedGenerator.REGISTRY.add(
                new StructurePool(
                        COLOR_POOL,
                        new Identifier("empty"),
                        ImmutableList.of(
                                Pair.of(new SinglePoolElement("tutorial:lime_square"), 1),
                                Pair.of(new SinglePoolElement("tutorial:magenta_square"), 1),
                                Pair.of(new SinglePoolElement("tutorial:orange_square"), 1),
                                Pair.of(new SinglePoolElement("tutorial:light_blue_square"), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
    }
```

Here, we're registering 2 pools (base & color) and then adding their
respective children to them. The StructurePool constructor is as
follows: \* registry name of the pool, same as target pool at top of a
jigsaw \* @Draylar if you know what this one does \* a list of pool
elements \* the projection type of the pool

For the list of elements, we add Pairs\[4\] of pool elements and
integers. The string passed into the element is the location of the
structure in the data directory, and the int is the weight of the
element within the entire target pool. Using 1 for each element ensures
each one will be picked evenly.

The projection is how the pool is placed in the world. Rigid means it
will be placed directly as is, and terrain matching means it will be
bent to sit on top of the terrain. The latter may be good for a wheat
field structure that moves with the terrain shape, whereas the first
would be better for houses with solid floors.

Now all we have to do is add our starting piece in our `initialize`
method:

```java
@Override
public void initialize(ChunkGenerator<?> chunkGenerator, StructureManager structureManager, int x, int z, Biome biome) {
    StructurePoolBasedGenerator.addPieces(BASE_POOL, 7, ExamplePiece::new, chunkGenerator, structureManager, new BlockPos(x * 16, 150, z * 16), children, random);
    setBoundingBoxFromChildren();
}
```

The Identifier is the starting pool to select from, the int is the size
of the entire structure (with 7 being "7 squares out"), and the 3rd
argument is a factory for the piece we'll register in a second.

### Creating a Piece

This portion is very simple. A piece represents one section or element
in your full structure. You'll need to create a basic piece class, and
we'll register it later:

```java
public class ExamplePiece extends PoolStructurePiece {

    ExamplePiece(StructureManager structureManager_1, StructurePoolElement structurePoolElement_1, BlockPos blockPos_1, int int_1, BlockRotation blockRotation_1, MutableIntBoundingBox mutableIntBoundingBox_1) {
        super(ExampleMod.EXAMPLE_PIECE, structureManager_1, structurePoolElement_1, blockPos_1, int_1, blockRotation_1, mutableIntBoundingBox_1);
    }

    public ExamplePiece(StructureManager manager, CompoundTag tag) {
        super(manager, tag, ExampleMod.EXAMPLE_PIECE);
    }
}
```

Where `ExampleMod.EXAMPLE_PIECE` is a reference to our registered piece.

### Registering Everything

We'll need to register our structure as both a feature *and* a structure
feature, and also register our piece. Registering your structure as a
StructureFeature is optional, and is used for saving it to the chunk. If
the world is stopped half-way through your structure loading, having
this registered will allow it to continue after the world is re-opened.
If it is not registered to a structure feature and this happens, the
structure will stop half-way through (which would mostly only occur in
larger, multiple chunk wide structures).

```java
public static final StructureFeature<DefaultFeatureConfig> EXAMPLE_FEATURE = Registry.register(
    Registry.FEATURE,
    new Identifier("tutorial", "example_feature"),
    new ExampleFeature(DefaultFeatureConfig::deserialize)
);

public static final StructureFeature<DefaultFeatureConfig> EXAMPLE_STRUCTURE_FEATURE = Registry.register(
    Registry.STRUCTURE_FEATURE,
    new Identifier("tutorial", "example_structure_feature"),
    EXAMPLE_FEATURE
);

public static final StructurePieceType EXAMPLE_PIECE = Registry.register(
    Registry.STRUCTURE_PIECE,
    new Identifier("tutorial", "example_piece"),
    ExamplePiece::new
);
```

### Spawning Our Structure

Finally, we'll have to spawn our structure. A basic example which adds
it to every biome is:

```java
Registry.BIOME.forEach(biome -> {
    biome.addFeature(GenerationStep.Feature.RAW_GENERATION, Biome.configureFeature(EXAMPLE_FEATURE, new DefaultFeatureConfig(), Decorator.NOPE, DecoratorConfig.DEFAULT));
    biome.addStructureFeature(EXAMPLE_FEATURE, new DefaultFeatureConfig());
});
```

#### Finished\!

As you can see, we have a single white square in the center, with boxes
going off the edges. Note that the radius in this screenshot was
increased to 14 instead of the 7 used in the tutorial.
![Finalized](https://i.imgur.com/qndZzZu.png)

1. While you can generate your StructureFeature from a `.nbt` file,
   most vanilla StructureFeatures simply override the `generate` method
   inside their given Piece class.

2. AbstractTempleFeature is another option. It automatically spaces out
   the structures similar to the existing temples-- this logic is also
   used by villages.

3. Do not use a `TranslatableText` for this: while the intent is
   valiant, things will break.

4. com.mojang.datafixers.util


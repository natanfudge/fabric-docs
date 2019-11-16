# Giving a block state

Every type of block in Minecraft is represented by a singular `Block`
instance. This makes it impossible to change a specific block's state by
simply changing the `Block` instance's state, as every other block of
that type will be affected\! But, what if you *do* want to give a
singular block state, so it can change based on some condition? This is
what `BlockState`s are for. Say we wanted a block to have a hardness of
`0.5` normally, but if we right click it before we try to break it, it
would become harder and gain a hardness of `2`.

First we define the boolean property of the block - whether or not it is
hard (careful not to import the wrong BooleanProperty\!):

```java
public class MyBlock extends Block {
    public static final BooleanProperty HARDENED = BooleanProperty.of("hardened");
}
```

Then we need to register the property by overriding `appendProperties`:

```java
public class MyBlock extends Block {
    [...]
    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory) {
        stateFactory.add(HARDENED);
    }
    
}
```

Then we need to set the default state of our property in the block
constructor:

```java
public class MyBlock extends Block {
    [...]
    public MyBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateFactory().getDefaultState().with(HARDENED, false));
    }
    
}
```

(To set multiple properties, chain `with()` calls)

Now, to set the property we need to call `world.setBlockState()`:

(Replace `MyBlocks.MY_BLOCK_INSTANCE` with your block's instance)

```java
public class MyBlock extends Block {
    [...]
    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        world.setBlockState(pos, MyBlocks.MY_BLOCK_INSTANCE.getDefaultState().with(HARDENED, true));
        return true;
    }
}
```

And to use the property we call `blockState.get(<our-property-name>)`:

```java
public class MyBlock extends Block {
    [...]
    @Override
    public float getHardness(BlockState blockState, BlockView blockView, BlockPos pos) {
        boolean hardened = blockState.get(HARDENED);
        if(hardened) return 2.0f;
        else return 0.5f;
    }
}
```

### Adding models for your blockstates

You can also make the texture and model of your block change based on
the state. This is done through a JSON file called a Blockstate JSON.
All blocks need a blockstate JSON, whether they have multiple states or
not, but the contents of the JSON can be as simple or complex as you
like. If you want to change the textures of your block based on the
state, you *will* need multiple models.

Let's say you register an instance of `MyBlock` to the ID
`mymod:my_block`. Minecraft would look for a file at the location
`src/main/resources/assets/mymod/blockstates/my_block.json` to load the
state from. If you don't want your block to change models between
states, the blockstate JSON can be very simple. It would look something
like this:

```JavaScript
{
    "variants": {
        "": { "model": "mymod:block/my_block" }
    }
}
```

Let's break this simple example down. There are a couple important parts
to this JSON:

\- The `"variants"` block will be where all possible variations for your
blockstate go. We'll explore variants more in a little. - A variant
named `""` will apply to *every* permutation of a blockstate. If you
have a `""` variant, you shouldn't have any other variants in the JSON,
or Minecraft will get upset. - The object assigned to the `""` variant
can have various properties added to it like rotation or texture
manipulation. Check out the linked Model page below for more
documentation on what properties can be added. All variants *must*
contain a `"model"` property. - The `"model"` property is always passed
an ID of a model. In this case, the game will look at the location
`src/main/resources/assets/mymod/models/block/my_block.json`. The ID
here can be anything. It doesn't *need* to be the same as your block's
ID, but if you only have one variant, it probably should. Block models
have their own setup, which is documented very well on the Minecraft
wiki page linked below. You can either write the JSON by hand or use a
program like [Blockbench](https://blockbench.net) to generate it more
easily.

If you *do* want to have different models for each blockstate, you'd
want to add multiple variants. For the same
`src/main/resources/assets/mymod/blockstates/my_block.json` location we
used above, your could would probably look like such:

```JavaScript
{
    "variants": {
        "hardened=false": { "model": "mymod:block/my_block" },
        "hardened=true": { "model": "mymod:block/my_block_hardened" }
    }
}
```

In this JSON, there are two variants, one for each possibility of the
`HARDENED` property we defined above. Since we gave the property the
string name of `hardened` in the Java, that's what we use here. Booleans
only have two states, but if you use properties based on integers or
enums, you'll have more variants.

Variants are based on possible permutations of the properties added to
your block. A property can be totally ignored in the blockstate JSON if
you want, like in the first blockstate JSON where we ignored the
`hardened` property, but if you want to include a property in one
variant, it must be included in *all* variants. If `mymod:my_block` also
had a boolean property called `glowing`, and you wanted to change the
model based on whether it was glowing and based on whether it was
hardened, you would need four variants: hardened off and glowing off,
hardened on and glowing off, hardened off and glowing on, and hardened
on and glowing on. The same model can be assigned to multiple variants
if you need it to be.

This is only a simple introduction to blockstate JSONs. All of the
tricks you can do with blockstate and model JSONs are documented on the
[Minecraft wiki](https://minecraft.gamepedia.com/Model), along with
examples of how the features are used in vanilla. Best of luck\!

### A note about performance

Every possible state of a block is registered at the start of the game.
This means that if you have 14 boolean properties, the block has 2^14 =
16384 different states and 2^14 states are registered. For this reason
blocks should not contain too many blockstate properties. Rather,
blockstates should be mostly reserved for visuals, and [Block
Entities](../Modding-Tutorials/Blocks-and-Block-Entities/blockentity.md) should be used for more advanced state.

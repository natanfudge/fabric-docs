# Creating a fluid

## Overview

Here we'll cover creation of a custom fluid. If you plan to create
several fluids, it is recommended to make an abstract basic fluid class
where you'll set necessary defaults that will be shared in its
subclasses. We'll also make it generate in the world like lakes.

## Making an abstract fluid

Vanilla fluids extend **net.minecraft.fluid.BaseFluid** class, and so
will our abstract fluid. It can be like this:

```java
public abstract class BasicFluid extends BaseFluid
{
    /**
     * @return does it produce infinite fluid (like water)?
     */
    @Override
    protected boolean isInfinite()
    {
        return false;
    }

    // make it transparent
    @Override
    protected BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    /**
     *
     * @return an associated item that "holds" this fluid
     */
    @Override
    public abstract Item getBucketItem();

    /**
     *
     * @return a blockstate of the associated {@linkplain net.minecraft.block.FluidBlock} with {@linkplain net.minecraft.block.FluidBlock#LEVEL}
     */
    @Override
    protected abstract BlockState toBlockState(FluidState var1);

    /**
     *
     * @return flowing static instance of this fluid
     */
    @Override
    public abstract Fluid getFlowing();

    /**
     *
     * @return still static instance of this fluid
     */
    @Override
    public abstract Fluid getStill();

    // how much does the height of the fluid block decreases
    @Override
    protected int getLevelDecreasePerBlock(ViewableWorld world)
    {
        return 1;
    }

    /**
     * 
     * @return update rate
     */
    @Override
    public int getTickRate(ViewableWorld world)
    {
        return 5;
    }

    @Override
    protected float getBlastResistance()
    {
        return 100;
    }

    // this seems to determine fluid's spread speed (higher value means faster)
    @Override
    protected int method_15733(ViewableWorld world)
    {
        return 4;
    }

    // I don't know what this does, but it's present in the water fluid
    @Override
    protected void beforeBreakingBlock(IWorld world, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = blockState.getBlock().hasBlockEntity() ? world.getBlockEntity(blockPos) : null;
        Block.dropStacks(blockState, world.getWorld(), blockPos, blockEntity);
    }

    // also don't know what it does
    public boolean method_15777(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN;
    }

    /**
     *
     * @return is given fluid instance of this fluid?
     */
    @Override
    public abstract boolean matchesType(Fluid fluid);

}
```

## Implementation

Now let's make an actual fluid; it will have a *still* and *flowing*
variants; will name it "Acid":

```java
public abstract class Acid extends BasicFluid
{
    @Override
    public Item getBucketItem()
    {
        return null;
    }
    @Override
    protected BlockState toBlockState(FluidState var1)
    {
        return null;
    }

    @Override
    public Fluid getFlowing()
    {
        return null;
    }

    @Override
    public Fluid getStill()
    {
        return null;
    }

    @Override
    public boolean matchesType(Fluid fluid)
    {
        return false;
    }

    // still acid
    public static class Still extends Acid
    {

        @Override
        public boolean isStill(FluidState fluidState)
        {
            return true;
        }

        /**
         * @return height of the fluid block
         */
        @Override
        public int getLevel(FluidState fluidState)
        {
            return 8;
        }
    }

    // flowing acid
    public static class Flowing extends  Acid
    {

        @Override
        public boolean isStill(FluidState fluidState)
        {
            return false;
        }

        /**
         * @return height of the fluid block
         */
        @Override
        public int getLevel(FluidState fluidState)
        {
            return fluidState.get(LEVEL);
        }

        @Override
        protected void appendProperties(StateFactory.Builder<Fluid, FluidState> stateFactoryBuilder)
        {
            super.appendProperties(stateFactoryBuilder);
            stateFactoryBuilder.add(LEVEL);
        }
    }
}
```

Next, we'll make static instances of still and flowing acid variants,
and an acid bucket. In your **ModInitializer**:

```java

    
    public static Acid stillAcid;
    public static Acid flowingAcid;
    
    public static BucketItem acidBucket;

    @Override
    public void onInitialize()
    {
    
        stillAcid = Registry.register(Registry.FLUID, new Identifier(MODID,"acid_still"), new Acid.Still());
        flowingAcid = Registry.register(Registry.FLUID, new Identifier(MODID,"acid_flowing"), new Acid.Flowing());
        
        acidBucket = new BucketItem(stillAcid, new Item.Settings().maxCount(1));
        Registry.register(Registry.ITEM, new Identifier(MODID,"acid_bucket"), acidBucket);
    }    
```

To make the custom fluid behave like water or lava, you must add it to a
corresponding fluid tag: make a file
"data/minecraft/tags/fluids/water.json" and write identifiers of your
fluids in there:

```json
{
  "replace": false,
  "values": [
    "modid:acid_still",
    "modid:acid_flowing"
  ]
}
```

### Making a fluid block

Next we need to create a block which will represent acid in the world.
**net.minecraft.block.FluidBlock** is the class we need to use, but for
"mojang" reasons its constructor is protected. The solution is
well-known - make a subclass of it and change the visibility of the
constructor:

```java
public class BaseFluidBlock extends FluidBlock
{
    public BaseFluidBlock(BaseFluid fluid, Settings settings)
    {
        super(fluid, settings);
    }
}
```

Now make a static block instance:

```java
    ...
    
    public static FluidBlock acid;

    @Override
    public void onInitialize()
    {
    
        ...
        
        acid = new BaseFluidBlock(stillAcid, FabricBlockSettings.of(Material.WATER).dropsNothing().build());
        Registry.register(Registry.BLOCK, new Identifier(MODID, "acid_block"), acid);
    }    
```

Now when we have these static objects, we go back to **Acid** class and
complete the overridden methods:

```java
public abstract class Acid extends BasicFluid
{
    @Override
    public Item getBucketItem()
    {
        return Mod.acidBucket;
    }
    
    @Override
    protected BlockState toBlockState(FluidState fluidState)
    {
        //don't ask me what **method_15741** does...
        return Mod.acid.getDefaultState().with(FluidBlock.LEVEL, method_15741(fluidState));
    }

    @Override
    public Fluid getFlowing()
    {
        return Mod.flowingAcid;
    }

    @Override
    public Fluid getStill()
    {
        return Mod.stillAcid;
    }

    @Override
    public boolean matchesType(Fluid fluid_1)
    {
        return fluid_1==Mod.flowingAcid || fluid_1==Mod.stillAcid;
    }
    
    ...
    
}    
```

Now we can assert that the Acid class is complete.

## Rendering setup

Time to do client-side things. In your **ClientModInitializer** you need
to specify locations of sprites for your fluids and define their
rendering. I will reuse water textures and just change the color applied
to them.

```java
    @Override
    public void onInitializeClient()
    {
        Identifier stillSpriteLocation = new Identifier("block/water_still");
        Identifier dynamicSpriteLocation = new Identifier("block/water_flow");
        // here I tell to use only 16x16 area of the water texture
        FabricSprite stillAcidSprite = new FabricSprite(stillSpriteLocation, 16, 16);
        // same, but 32
        FabricSprite dynamicAcidSprite = new FabricSprite(dynamicSpriteLocation, 32, 32);
        
        // adding the sprites to the block texture atlas
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((spriteAtlasTexture, registry) -> {
            registry.register(stillAcidSprite);
            registry.register(dynamicAcidSprite);
        });

        // this renderer is responsible for drawing fluids in a world
        FluidRenderHandler acidRenderHandler = new FluidRenderHandler()
        {
            // return the sprites: still sprite goes first into the array, flowing/dynamic goes last
            @Override
            public Sprite[] getFluidSprites(ExtendedBlockView extendedBlockView, BlockPos blockPos, FluidState fluidState)
            {
                return new Sprite[] {stillAcidSprite, dynamicAcidSprite};
            }

            // apply light green color
            @Override
            public int getFluidColor(ExtendedBlockView view, BlockPos pos, FluidState state)
            {
                return 0x4cc248;
            }
        };

        // registering the same renderer for both fluid variants is intentional

        FluidRenderHandlerRegistry.INSTANCE.register(Mod.stillAcid, acidRenderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(Mod.flowingAcid, acidRenderHandler);
```

Then what's left to do is to create necessary Json files and textures,
but you should know how to do that at this point.

## Generation in a world

To make acid lakes generate in the world, you can use
**net.minecraft.world.gen.feature.LakeFeature**, which you create in the
ModInitializer:

```java
        
        LakeFeature acidFeature = Registry.register(Registry.FEATURE, new Identifier(MODID,"acid_lake"), new LakeFeature(dynamic -> new LakeFeatureConfig(acid.getDefaultState())));

```

Then put it into desired biomes to generate:

```java
// I tell it to generate like water lakes, with a rarity of 40 (the higher is the number, the lesser is the generation chance):
Biomes.FOREST.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, Biome.configureFeature(acidFeature, new LakeFeatureConfig(acid.getDefaultState()), Decorator.WATER_LAKE, new LakeDecoratorConfig(40)));
```

This is the end of the tutorial.

# Creating a fluid

## Overview

Here we'll cover creation of a custom fluid. If you plan to create
several fluids, it is recommended to make an abstract basic fluid class
where you'll set necessary defaults that will be shared in its
subclasses. We'll also make it generate in the world like lakes.

## Making an abstract fluid

Vanilla fluids extend `net.minecraft.fluid.FlowableFluid`, and so shall
we.

```java
public abstract class TutorialFluid extends FlowableFluid
{
    /**
     * @return is the given fluid an instance of this fluid?
     */
    @Override
    public boolean matchesType(Fluid fluid)
    {
        return fluid == getStill() || fluid == getFlowing();
    }
    
    /**
     * @return is the fluid infinite like water?
     */
    @Override
    protected boolean isInfinite()
    {
        return false;
    }
    
    /**
     * Perform actions when fluid flows into a replaceable block. Water drops
     * the block's loot table. Lava plays the "block.lava.extinguish" sound.
     */
    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state)
    {
        final BlockEntity blockEntity = state.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }
    
    /**
     * Lava returns true if its FluidState is above a certain height and the
     * Fluid is Water.
     * 
     * @return if the given Fluid can flow into this FluidState?
     */
    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction)
    {
        return false;
    }
    
    /**
     * Possibly related to the distance checks for flowing into nearby holes?
     * Water returns 4. Lava returns 2 in the Overworld and 4 in the Nether.
     */
    @Override
    protected int getFlowSpeed(WorldView worldView)
    {
        return 4;
    }
    
    /**
     * Water returns 1. Lava returns 2 in the Overworld and 1 in the Nether.
     */
    @Override
    protected int getLevelDecreasePerBlock(WorldView worldView)
    {
        return 1;
    }
    
    /**
     * Water returns 5. Lava returns 30 in the Overworld and 10 in the Nether.
     */
    @Override
    public int getTickRate(WorldView worldView)
    {
        return 5;
    }
    
    /**
     * Water and Lava both return 100.0F.
     */
    @Override
    protected float getBlastResistance()
    {
        return 100.0F;
    }
}
```

## Implementation

Now let's make an actual fluid which'll have still and flowing variants.
For this tutorial, we will call it Acid. The missing references will be
filled in shortly.

```java
public abstract class AcidFluid extends TutorialFluid
{
    @Override
    public Fluid getStill()
    {
        return <YOUR_STILL_FLUID_HERE>;
    }
    
    @Override
    public Fluid getFlowing()
    {
        return <YOUR_FLOWING_FLUID_HERE>;
    }
    
    @Override
    public Item getBucketItem()
    {
        return <YOUR_BUCKET_ITEM_HERE>;
    }
    
    @Override
    protected BlockState toBlockState(FluidState fluidState)
    {
        // method_15741 converts the LEVEL_1_8 of the fluid state to the LEVEL_15 the fluid block uses
        return <YOUR_FLUID_BLOCK_HERE>.getDefaultState().with(Properties.LEVEL_15, method_15741(fluidState));
    }
    
    public static class Flowing extends AcidFluid
    {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder)
        {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }
        
        @Override
        public int getLevel(FluidState fluidState)
        {
            return fluidState.get(LEVEL);
        }
        
        @Override
        public boolean isStill(FluidState fluidState)
        {
            return false;
        }
    }
    
    public static class Still extends AcidFluid
    {
        @Override
        public int getLevel(FluidState fluidState)
        {
            return 8;
        }
        
        @Override
        public boolean isStill(FluidState fluidState)
        {
            return true;
        }
    }
}
```

Next, we'll make static instances of still and flowing acid variants,
and an acid bucket. In your `ModInitializer`:

```java
// ...

public static FlowableFluid STILL_ACID;
public static FlowableFluid FLOWING_ACID;

public static Item ACID_BUCKET;

// ...

@Override
public void onInitialize()
{
    // ...
    
    STILL_ACID = Registry.register(Registry.FLUID, new Identifier(MOD_ID, "acid"), new AcidFluid.Still());
    
    FLOWING_ACID = Registry.register(Registry.FLUID, new Identifier(MOD_ID, "flowing_acid"), new AcidFluid.Flowing());
    
    ACID_BUCKET = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "acid_bucket"), new BucketItem(STILL_ACID, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));
    
    // ...
}

// ...
```

To make a custom fluid behave more like water or lava, you must add it
to a corresponding fluid tag: For water, make a
`data/minecraft/tags/fluids/water.json` file and write the identifiers
of your fluids in there:

```json
{
    "replace": false,
    "values":
    [
        "your_mod_id:acid",
        "your_mod_id:flowing_acid"
    ]
}
```

## Making a fluid block

Next we need to create a block which will represent acid in the world.
`net.minecraft.block.FluidBlock` is the class we need to use, but since
its constructor is protected, we can't construct it directly. Some ways
to use it are to make a subclass or an anonymous subclass. Here we will
be showing the latter. In your `ModInitializer`:

```java
// ...

public static Block ACID;

// ...

@Override
public void onInitialize()
{
    // ...
    
    ACID = Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "acid"), new FluidBlock(STILL_ACID, FabricBlockSettings.copy(Blocks.WATER)){});
    
    // ...
}

// ...    
```

Now that we have these static objects, we can go back to `AcidFluid` and
fill in the overridden methods:

```java
public abstract class AcidFluid extends TutorialFluid
{
    @Override
    public Fluid getStill()
    {
        return TutorialMod.STILL_ACID;
    }
    
    @Override
    public Fluid getFlowing()
    {
        return TutorialMod.FLOWING_ACID;
    }
    
    @Override
    public Item getBucketItem()
    {
        return TutorialMod.ACID_BUCKET;
    }
    
    @Override
    protected BlockState toBlockState(FluidState fluidState)
    {
        // method_15741 converts the LEVEL_1_8 of the fluid state to the LEVEL_15 the fluid block uses
        return TutorialMod.ACID.getDefaultState().with(Properties.LEVEL_15, method_15741(fluidState));
    }
    
    // ...
}    
```

## Rendering setup

For your fluids to have textures or be tinted with a color, you will
need to register a `FluidRenderHandler` for them. Here, we will reuse
water's textures and just change the tint color applied to them. To make
sure the textures are rendered as translucent, you can use Fabric's
`BlockRenderLayerMap`.

```java
public class TutorialModClient implements ClientModInitializer
{
    // ...
    
    @Override
    public void onInitializeClient()
    {
        // ...
        
        setupFluidRendering(TutorialMod.STILL_ACID, TutorialMod.FLOWING_ACID, new Identifier("minecraft", "water"), 0x4CC248);
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), TutorialMod.STILL_ACID, TutorialMod.FLOWING_ACID);
        
        // ...
    }
    
    // ...
    
    public static void setupFluidRendering(final Fluid still, final Fluid flowing, final Identifier textureFluidId, final int color)
    {
        final Identifier stillSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_still");
        final Identifier flowingSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_flow");
        
        // If they're not already present, add the sprites to the block atlas
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((atlasTexture, registry) ->
        {
            registry.register(stillSpriteId);
            registry.register(flowingSpriteId);
        });
        
        final Identifier fluidId = Registry.FLUID.getId(still);
        final Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");
        
        final Sprite[] fluidSprites = { null, null };
        
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener()
        {
            @Override
            public Identifier getFabricId()
            {
                return listenerId;
            }
            
            /**
             * Get the sprites from the block atlas when resources are reloaded
             */
            @Override
            public void apply(ResourceManager resourceManager)
            {
                final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
                fluidSprites[0] = atlas.apply(stillSpriteId);               fluidSprites[1] = atlas.apply(flowingSpriteId);
            }
        });
        
        // The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
        final FluidRenderHandler renderHandler = new FluidRenderHandler()
        {
            @Override
            public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state)
            {
                return fluidSprites;
            }
            
            @Override
            public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state)
            {
                return color;
            }
        };
        
        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
    }
    
    // ...
}
```

If you want to use your own fluid textures, you can refer to vanilla's
assets [1] as a template.

## Generation in the world

To make lakes of acid generate in the world, you can create a
`net.minecraft.world.gen.feature.LakeFeature` in your `ModInitializer`
and then add it to the biomes you want it to generate in:

```java
// ...

public static LakeFeature ACID_LAKE;

// ...

@Override
public void onInitialize()
{
    // ...
    
    ACID_LAKE = Registry.register(Registry.FEATURE, new Identifier(MOD_ID, "acid_lake"), new LakeFeature(SingleStateFeatureConfig::deserialize));
    
    // generate in swamps, similar to water lakes, but with a chance of 40 (the higher the number, the lower the generation chance)
    Biomes.SWAMP.addFeature(
        GenerationStep.Feature.LOCAL_MODIFICATIONS,
        ACID_LAKE.configure(new SingleStateFeatureConfig(ACID.getDefaultState()))
            .createDecoratedFeature(Decorator.WATER_LAKE.configure(new ChanceDecoratorConfig(40)))
    );
    
    // ...
}

// ...
```

[1] `assets/minecraft/blockstates/water.json`  
`assets/minecraft/models/block/water.json`  
`assets/minecraft/textures/block/water_still.png`  
`assets/minecraft/textures/block/water_still.png.mcmeta`  
`assets/minecraft/textures/block/water_flow.png`  
`assets/minecraft/textures/block/water_flow.png.mcmeta`

# Rendering Blocks and Items Dynamically using a custom Model

It is possible to add models to the game using block model JSON files,
but it is also possible to render them through Java code. In this
tutorial, we will add a four-sided furnace model to the game.

Note that models are rendered when the chunks are rebuilt. If you need
more dynamic rendering, you can use a `BlockEntityRenderer`:
[blockentityrenderers](../Modding-Tutorials/Blocks-and-Block-Entities/blockentityrenderer.md).

## Creating the model

When a model is first registered in Minecraft, its raw data is contained
in an `UnbakedModel`. This data can include shapes or texture names for
example. Later during the initialization, `UnbakedModel::bake()` creates
a `BakedModel`, ready for rendering. For rendering to be as fast as
possible, as many operations as possible need to be done during baking.
We will also implement `FabricBakedModel` to make use of the Fabric
Renderer API. Let's create a single `FourSidedFurnace` model that will
implement all three interfaces.

```java
public class FourSidedFurnaceModel implements UnbakedModel, BakedModel, FabricBakedModel {
```

### Sprites

A `Sprite` is necessary for rendering a texture. We must first create a
`SpriteIdentifier` and then get the corresponding `Sprite` while baking
the model. Here, we will use two furnace textures. They are block
textures, so they must be loaded from the block atlas
`SpriteAtlasTexture.BLOCK_ATLAS_TEX`.

```java
private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
        new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft:block/furnace_front_on")),
        new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft:block/furnace_top"))
};
private Sprite[] SPRITES = new Sprite[2];
```

### Meshes

A `Mesh` is a game shape that is ready for rendering with the Fabric
Rendering API. We will add one to our class, and we will build it during
model baking.

```java
private Mesh mesh;
```

### UnbakedModel methods

```java
    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList(); // This model does not depend on other models.
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Arrays.asList(SPRITE_IDS); // The textures this model (and all its model dependencies, and their dependencies, etc...!) depends on.
    }


    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        // Get the sprites
        for(int i = 0; i < 2; ++i) {
            SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
        }
        // Build the mesh using the Renderer API
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        for(Direction direction : Direction.values()) {
            int spriteIdx = direction == Direction.UP || direction == Direction.DOWN ? 1 : 0;
            // Add a new face to the mesh
            emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
            // Set the sprite of the face, must be called after .square()
            // We haven't specified any UV coordinates, so we want to use the whole texture. BAKE_LOCK_UV does exactly that.
            emitter.spriteBake(0, SPRITES[spriteIdx], MutableQuadView.BAKE_LOCK_UV);
            // Enable texture usage
            emitter.spriteColor(0, -1, -1, -1, -1);
            // Add the quad to the mesh
            emitter.emit();
        }
        mesh = builder.build();

        return this;
    }
```

### BakedModel methods

Not all the methods here are used by the Fabric Renderer, so we don't
really care about the implementation.

```java
    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
        return null; // Don't need because we use FabricBakedModel instead
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true; // we want the block to have a shadow depending on the adjacent blocks
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public Sprite getSprite() {
        return SPRITES[1]; // Block break particle, let's use furnace_top
    }

    @Override
    public ModelTransformation getTransformation() {
        return null;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return null;
    }
```

### FabricBakedModel methods

```java
    @Override
    public boolean isVanillaAdapter() {
        return false; // False to trigger FabricBakedModel rendering
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
        // Render function
        
        // We just render the mesh
        renderContext.meshConsumer().accept(mesh);
    }

    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {

    }
}
```

## Registering the model

Let's first write a `ModelResourceProvider`, an interface that allows
you to provide an `UnbakedModel` before the game tries to load it from
JSON. Have a look at [the
documentation](https://github.com/FabricMC/fabric/blob/1.16/fabric-models-v0/src/main/java/net/fabricmc/fabric/api/client/model/ModelResourceProvider.java)
for more details. The important part is that `loadModelResource()` will
be called for every model.

Let's register the model under the name
`tutorial:block/four_sided_furnace`.

```java
public class TutorialModelProvider implements ModelResourceProvider {
    public static final Identifier FOUR_SIDED_FURNACE_MODEL = new Identifier("tutorial:block/four_sided_furnace");
    @Override
    public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) throws ModelProviderException {
        if(identifier.equals(FOUR_SIDED_FURNACE_MODEL)) {
            return new FourSidedFurnaceModel();
        } else {
            return null;
        }
    }
}
```

Now we have to register this class in the client initializer, the entry
point for client-specific code.

```java
public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new TutorialModelProvider());
        
        /* Other client-specific initialization */
    }
}
```

Don't forget to register this entrypoint in `fabric.mod.json` if you
haven't done it yet:

```json
/* ... */
  "entrypoints": {
    /* ... */
    "client": [
      "tutorial.path.to.ExampleModClient"
    ]
  },
```

## Using the model

You can now register your block to use your new model. For example, if
your block only has one block state, put this in
`assets/your_mod_id/blockstates/your_block_id.json`.

```json
{
  "variants": {
    "": { "model": "tutorial:block/four_sided_furnace" }
  }
}
```

Of course, you can implement much more complex rendering. Have fun!

<img src="/tutorial/four_sided_furnace_render.png" width="600" />

## Rendering the item

As you can see in the picture, the item is not rendered correctly. Let's
fix this.

### Updating the model

We will re-use the same model class, with just a small change:

- We will need a `ModelTransformation` that rotates/translates/scales
  the model depending on its position (in right hand, in left hand, in
  gui, in item frame, etc...). As we are creating a model for a
  regular block, we will use the one from "minecraft:block/block"
  which we will load during model baking.

We will update our `FourSidedFurnaceModel` class as follows:

```java
    // The minecraft default block model
    private static final Identifier DEFAULT_BLOCK_MODEL = new Identifier("minecraft:block/block");

    private ModelTransformation transformation;
    
    // We need to add the default model to the dependencies
    public Collection<Identifier> getModelDependencies() {
        return Arrays.asList(DEFAULT_BLOCK_MODEL);
    }
    
    // We need to add a bit of logic to the bake function
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        // Load the default block model
        JsonUnbakedModel defaultBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL);
        // Get its ModelTransformation
        transformation = defaultBlockModel.getTransformations();
        
        /* Previous code */
    }
    
    // We need to implement getTransformation() and getOverrides()
    @Override
    public ModelTransformation getTransformation() {
        return transformation;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }
    
    // We will also implement this method to have the correct lighting in the item rendering. Try to set this to false and you will see the difference.
    @Override
    public boolean isSideLit() {
        return true;
    }
    
    // Finally, we can implement the item render function
    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
        renderContext.meshConsumer().accept(mesh);
    }
```

### Loading the model

Let's update the `ModelResourceProvider` we created earlier:

```java
public class TutorialModelProvider implements ModelResourceProvider {
    public static final FourSidedFurnaceModel FOUR_SIDED_FURNACE_MODEL = new FourSidedFurnaceModel();
    public static final Identifier FOUR_SIDED_FURNACE_MODEL_BLOCK = new Identifier("tutorial:block/four_sided_furnace");
    public static final Identifier FOUR_SIDED_FURNACE_MODEL_ITEM = new Identifier("tutorial:item/four_sided_furnace");

    @Override
    public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) throws ModelProviderException {
        if(identifier.equals(FOUR_SIDED_FURNACE_MODEL_BLOCK) || identifier.equals(FOUR_SIDED_FURNACE_MODEL_ITEM)) {
            return FOUR_SIDED_FURNACE_MODEL;
        } else {
            return null;
        }
    }
}
```

## Final result

<img src="/tutorial/four_sided_furnace_render_final.png" width="600" />

Et voilÃ ! Enjoy!

## More dynamic rendering

The `renderContext` parameter in `emitBlockQuads` and `emitItemQuads`
contains a `QuadEmitter` which you can use to build a model on the fly.

```java
@Override
public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
    QuadEmitter emitter = renderContext.getEmitter();
    /* With this emitter, you can directly append the quads to the chunk model. */
}
```


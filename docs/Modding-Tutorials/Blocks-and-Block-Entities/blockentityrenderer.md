# Rendering blocks and items dynamically using block entity renderers

*This is the 1.15 & 1.16 version of this tutorial. For the 1.14 version,
see [Rendering blocks and items dynamically using block entity renderers
(1.14)](../Modding-Tutorials/1.14/blockentityrenderers).*

Make sure you [added a block entity](../Modding-Tutorials/Blocks-and-Block-Entities/blockentity.md) before
reading this tutorial!

## Introduction

Blocks by themselves aren't that interesting, they just stay static at a
certain location and a certain size until broken. We can use block
entity renderers to render items and blocks associated with a block
entity far more dynamically - render multiple different items, at
differing locations and sizes, and more.

## Example

In this tutorial we'll build off the block entity we created by adding a
`BlockEntityRenderer` to it. The renderer will display a jukebox
floating above the block, going up and down and spinning.

The first thing we need to do is create our `BlockEntityRenderer` class:

```java
public class MyBlockEntityRenderer extends BlockEntityRenderer<DemoBlockEntity> {
    // A jukebox itemstack
    private static ItemStack stack = new ItemStack(Items.JUKEBOX, 1);
    
    public MyBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }
    
    @Override
    public void render(DemoBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
    }
}
```

We're going to need to register our `BlockEntityRenderer`, but only for
the client. This wouldn't matter in a single-player setting, since the
server runs in the same process as the client. However, in a multiplayer
setting, where the server runs in a different process than the client,
the server code has no concept of a "BlockEntityRenderer", and as a
result would not accept registering one. To run initialization code only
for the client, we need to setup a `client` entrypoint.

Create a new class next to your main class that implements
`ClientModInitializer`:

```java
public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Here we will put client-only registration code
    }
}
```

Set this class as the `client` entrypoint in your `fabric.mod.json`
(modify the path as needed):

```javascript
"entrypoints": {
    [...]
    "client": [
      {
        "value": "tutorial.path.to.ExampleModClient"
      }
    ]
}    
```

And register the `BlockEntityRenderer` in our ClientModInitializer:

```java
@Override
public void onInitializeClient() {
    BlockEntityRendererRegistry.INSTANCE.register(DEMO_BLOCK_ENTITY, MyBlockEntityRenderer::new);
}
```

We override the `render` method which gets called every frame(!), and in
it we will do our rendering - for starters, call `matrices.push();`
which is mandatory when doing GL calls (we will doing those right
after):

```java
public void render(DemoBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
   matrices.push();
}
```

We then perform the movement of the jukebox (matrices.translate) and
rotation (matrices.multiply). There are two parts to the translation: we
translate it to 0.5, 1.25, and 0.5 which is above the center of our
block. The second part is the part that changes: the offset in the y
value. The offset is the height of the item for any given frame. We
recalculate this each time because we want it to be animating bouncing
up and down. We calculate this by:

- Getting the current world time, which changes over time.
- Adding the partial ticks. (The partial ticks is a fractional value
  representing the amount of time that's passed between the last full
  tick and now. We use this because otherwise the animation would be
  jittery because there are fewer ticks per second than frames per
  second.)
- Dividing that by 8 to slow the movement down.
- Taking the sine of that to produce a value that ranges between -1
  and 1, like a [sine wave](https://www.electronicshub.org/wp-content/uploads/2015/07/11.jpg).
- Dividing that by 4 to compress the sine wave vertically so the item
  doesn't move up and down as much.

```java
    public void render(DemoBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        [...]
        // Calculate the current offset in the y value
        double offset = Math.sin((blockEntity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
        // Move the item
        matrices.translate(0.5, 1.25 + offset, 0.5);

        // Rotate the item
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((blockEntity.getWorld().getTime() + tickDelta) * 4));
    }
```

Finally, we will get the Minecraft 'ItemRenderer' and render the jukebox
item by using `renderItem`. We also pass
`ModelTransformation.Type.GROUND` to `renderItem` because we want a
similiar effect to an item lying on the ground. Try experimenting with
this value and see what happens (it's an enum). We also need to call
`matrices.pop();` after these GL calls:

```java
    public void render(DemoBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        [...]
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);

        // Mandatory call after GL calls
        matrices.pop();
    }
```

You can try your newly created block entity renderer right now. However,
if you didn't make your block transparent, you will notice something is
amiss - the floating block, the jukebox, is pitch black! This is because
by default, *whatever you render in the block entity, will receive light
as if it's in the same position as the block entity*. So the floating
block receives light from *inside* our opaque block, which means it
receives no light! To fix this, we will tell Minecraft to receive light
from *one block above* the location of the block entity.

To get the light, we call `WorldRenderer#getLightmapCoordinates()` on
the position above our block entity, and to use the light we use it in
`renderItem()`:

```java
@Override
public void render(DemoBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
    [...]
        int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
    MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
        [...]
}
```

The jukebox should now have the proper lighting.

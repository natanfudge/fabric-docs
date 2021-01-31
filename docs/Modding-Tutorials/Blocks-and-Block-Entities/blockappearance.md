# Manipulating a Block's appearance

*This is the 1.15 version of this tutorial. For the 1.14 version, see
[Manipulating a Block's appearance
(1.14)](../Modding-Tutorials/1.14/blockappearance).*

## Making a block transparent

You may have noticed that even if your block's texture is transparent,
it still looks opaque. To fix this, you need to set your block's render
layer to cutout or transparent.

In a client-sided mod initializer, add:

```java
BlockRenderLayerMap.INSTANCE.putBlock(ExampleMod.MY_BLOCK, RenderLayer.getCutout());
// Replace `RenderLayer.getCutout()` with `RenderLayer.getTranslucent()` if you have a translucent texture.
```

You probably also want to make your block transparent. To do that, use
the `nonOpaque` method on your block settings.

```java
class MyBlock extends Block {
    public MyBlock() {
        super(Settings.of(Material.STONE).nonOpaque());
    }

    [...]
}

```

If you do not mark your block as non-opaque like this, then block faces
behind the block will not render and you will be able to see through the
world.

## Making a block invisible

First we need to make the block appear invisible. To do this we override
`getRenderType` in our block class and return
`BlockRenderType.INVISIBLE`:

```java
@Override
public BlockRenderType getRenderType(BlockState blockState) {
    return BlockRenderType.INVISIBLE;
}
```

We then need to make our block unselectable by making its outline shape
be non-existent. So override `getOutlineShape` and return an empty
`VoxelShape`:

```java
@Override
public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
   return VoxelShapes.empty();
}
```


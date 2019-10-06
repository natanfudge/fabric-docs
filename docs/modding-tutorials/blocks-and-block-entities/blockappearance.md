# Manipulating a Block's appearance

## Making a block transparent

You may have noticed that even if your block's texture is transparent,
it still looks opaque. To fix this, override `getRenderLayer` and return
`BlockRenderLayer.TRANSLUCENT`:

```java
class MyBlock extends Block {
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    [...]
}

```

You probably also want to make your block transparent. To do that, use
the `Material` constructor to set `blocksLight` to false.

```java
class MyBlock extends Block {
     private static Material myMaterial = new Material(
            MaterialColor.AIR,   //materialColor,
            false,   //isLiquid,
            false, // isSolid,
            true, // blocksMovement,
            false,// blocksLight,  <----- Important part, the other parts change as you wish
            true,//  !requiresTool,
            false, //  burnable,
            false,//  replaceable,
            PistonBehavior.NORMAL//  pistonBehavior
    );

    public MyBlock() {
        super(Settings.of(myMaterial);
    }

    [...]
}

```

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

We then need to make our block unselectable by making its
\`outlineShape\` be non-existent. So override ''getOutlineShape' and
return an empty `VoxelShape`:

```java
@Override
public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
   return VoxelShapes.cuboid(0,0,0,0,0,0);
}
```


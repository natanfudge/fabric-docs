### Directional Blocks

Making blocks directional (facing into certain directions) is also done
using block states. This example describes a vertical version of the
andesite slab.

<img src="/tutorial/vertslab.png" width="200" />

```java
public class PolishedAndesiteSideBlock extends HorizontalFacingBlock {

    public PolishedAndesiteSideBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ctx) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case NORTH:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f);
            case SOUTH:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.5f, 1.0f, 1.0f, 1.0f);
            case EAST:
                return VoxelShapes.cuboid(0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case WEST:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f);
            default:
                return VoxelShapes.fullCube();
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

}
```

### Defining Blockstate

polished\_andesite\_side\_block.json

    {
      "variants": {
        "facing=north": { "model": "bitmod:block/polished_andesite_side_block" },
        "facing=east":  { "model": "bitmod:block/polished_andesite_side_block", "y":  90},
        "facing=south": { "model": "bitmod:block/polished_andesite_side_block", "y": 180 },
        "facing=west":  { "model": "bitmod:block/polished_andesite_side_block", "y": 270 }
      }
    }

### Defining Block Models

side.json

    {   "parent": "block/block",
        "textures": {
            "particle": "#side"
        },
        "elements": [           {   "from": [ 0, 0, 0 ],
                "to": [  16, 16, 8 ],
                "faces": {
                    "down":  { "uv": [ 0, 8, 16, 16 ], "texture": "#bottom", "cullface": "down" },                   "up":    { "uv": [ 0, 8, 16, 16 ], "texture": "#top",    "cullface": "up" },
                    "north": { "uv": [ 0, 0, 16, 16 ], "texture": "#side",   "cullface": "north" },                   "south": { "uv": [ 0, 0, 16, 16 ], "texture": "#side"  },
                    "west":  { "texture": "#side",   "cullface": "west" },
                    "east":  { "texture": "#side",   "cullface": "east" }
                }
            }
        ]
    }

polished\_andesite\_side\_block.json

    {
        "parent": "bitmod:block/side",
        "textures": {
            "bottom": "block/polished_andesite",
            "top": "block/polished_andesite",
            "side": "block/polished_andesite"
        }
    }


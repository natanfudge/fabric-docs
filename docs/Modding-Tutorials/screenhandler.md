# Creating a Container Block

In this tutorial we will create simple storage block similar to a
dispenser, explaining how to build a user interface with the
`ScreenHandler` API from Fabric and Vanilla Minecraft along the way.

Let us first explain some vocabulary:

**Screenhandler:** A `ScreenHandler` is a class responsible for
synchronizing inventory contents between the client and the server. It
can sync additional integer values like furnace progress as well, which
will be showed in the next tutorial. Our subclass will have two
constructors here: one will be used on the server side and will contain
the real `Inventory` and another one will be used on the client side to
hold the `ItemStack`s and synchronize them.

**Screen:** The `Screen` class only exists on the client and will render
the background and other decorations for your `ScreenHandler`.

## Block and BlockEntity classes

First we need to create the `Block` and its `BlockEntity`.

```java
public class BoxBlock extends BlockWithEntity {
    protected BoxBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BoxBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        //With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
            //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                //With this call the server will request the client to open the appropriate Screenhandler
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }
    
    
    //This method will drop all items onto the ground when the block is broken
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BoxBlockEntity) {
                ItemScatterer.spawn(world, pos, (BoxBlockEntity)blockEntity);
                // update comparators
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
    
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
    
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
    }
}
```

Now we will create our `BlockEntity`, it will use the
`ImplementedInventory` interface from the [Inventory
Tutorial](../Modding-Tutorials/Blocks-and-Block-Entities/inventory.md).

```java
public class BoxBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public BoxBlockEntity() {
        super(ExampleMod.BOX_BLOCK_ENTITY);
    }


    //From the ImplementedInventory Interface

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;

    }

    //These Methods are from the NamedScreenHandlerFactory Interface
    //createMenu creates the ScreenHandler itself
    //getDisplayName will Provide its name which is normally shown at the top

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new BoxScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
    
    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, this.inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.inventory);
        return tag;
    }
}

```

## Registering Block, BlockItem and BlockEntity

```java
public class ExampleMod implements ModInitializer {

    public static final Block BOX_BLOCK;
    public static final BlockItem BOX_BLOCK_ITEM;
    public static final BlockEntityType<BoxBlockEntity> BOX_BLOCK_ENTITY;

    public static final String MOD_ID = "testmod";
    // a public identifier for multiple parts of our bigger chest
    public static final Identifier BOX = new Identifier(MOD_ID, "box_block");

    static {
        BOX_BLOCK = Registry.register(Registry.BLOCK, BOX, new BoxBlock(FabricBlockSettings.copyOf(Blocks.CHEST)));
        BOX_BLOCK_ITEM = Registry.register(Registry.ITEM, BOX, new BlockItem(BOX_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

        //The parameter of build at the very end is always null, do not worry about it
        BOX_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, BOX, BlockEntityType.Builder.create(BoxBlockEntity::new, BOX_BLOCK).build(null));
    }

    @Override
    public void onInitialize() {

    }
}


```

## ScreenHandler and Screen

As explained earlier, we need both a `ScreenHandler` and a
`HandledScreen` to display and sync the GUI. `ScreenHandler` classes are
used to synchronize GUI state between the server and the client.
`HandledScreen` classes are fully client-sided and are responsible for
drawing GUI elements.

```java
public class BoxScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public BoxScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(9));
    }

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public BoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ExampleMod.BOX_SCREEN_HANDLER, syncId);
        checkSize(inventory, 9);
        this.inventory = inventory;
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job
        int m;
        int l;
        //Our inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 3; ++l) {
                this.addSlot(new Slot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18));
            }
        }
        //The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }
}

```

```java
public class BoxScreen extends HandledScreen<ScreenHandler> {
    //A path to the gui texture. In this example we use the texture from the dispenser
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");

    public BoxScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
```

## Registering our Screen and ScreenHandler

As screens are a client-only concept, we can only register them on the
client.

```java
@Environment(EnvType.CLIENT)
public class ExampleClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ExampleMod.BOX_SCREEN_HANDLER, BoxScreen::new);
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

`ScreenHandler`s exist both on the client and on the server and
therefore have to be registered on both.

```java
public class ExampleMod implements ModInitializer {
    [...]
    public static final ScreenHandlerType<BoxScreenHandler> BOX_SCREEN_HANDLER;
    [...]
    static {
        [...]
        //We use registerSimple here because our Entity is not an ExtendedScreenHandlerFactory
        //but a NamedScreenHandlerFactory.
        //In a later Tutorial you will see what ExtendedScreenHandlerFactory can do!
        BOX_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(BOX, BoxScreenHandler::new);
    }

    @Override
    public void onInitialize() {

    }
}
```

## Result

You have now created your own container Block, you could easily change
it to contain a smaller or bigger Inventory. Maybe even apply a texture
:-P

<img src="/tutorial/bildschirmfoto_vom_2020-08-14_18-32-07.png" width="400" />

## Further Reading

\- [Syncing Custom Data with Extended ScreenHandlers when screen is
opened](../Modding-Tutorials/extendedscreenhandler.md)

\- [Syncing Integers continuously with
PropertyDelegates](../Modding-Tutorials/propertydelegates.md)

\- An example mod using the `ScreenHandler` API: [ExampleMod on
Github](https://github.com/FabricMC/fabric/tree/1.16/fabric-screen-handler-api-v1/src/testmod).

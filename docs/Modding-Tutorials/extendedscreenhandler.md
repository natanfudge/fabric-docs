# Syncing Custom Data with Extended ScreenHandlers

In this tutorial we will use the ExtendedScreenHandler to transfer
arbitary data from the server to the client ScreenHandler when the
ScreenHandler is opened.

In our example we will send the position of the block and render it as
the container's title.

To understand this tutorial you need to read the first
[Screenhandler](../Modding-Tutorials/screenhandler.md) tutorial. Methods which have no
code here were already shown in that tutorial.

# BlockEntity

As the Block class does not need to be changed at all we leave it out
here.

Our block entity now implements `ExtendedScreenHandlerFactory`, this
interfaces provides us the `writeScreenOpeningData` method, which will
be called on the server when it requests the client to open a
`ScreenHandler`. The data you write into the `PacketByteBuf` will be
transferred to the client over the network.

```java
public class BoxBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

    public BoxBlockEntity() {
        super(Test.BOX_BLOCK_ENTITY);
    }


    //From the ImplementedInventory Interface

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;

    }

    //These Methods are from the NamedScreenHandlerFactory Interface

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide this to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new BoxScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    //This Method is from the ExtendedScreenHandlerFactory

    //This method gets called on the Server when it requests the client to open the screenHandler.
    //The contents you write into the packetByteBuf will automatically be transferred in a packet to the client
    //and the ScreenHandler Constructor with the packetByteBuf argument gets called on the client
    //
    //The order you insert things here is the same as you need to extract them. You do not need to reverse the order!
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        //The pos field is a public field from BlockEntity
        packetByteBuf.writeBlockPos(pos);
    }
}

```

# Our new ExtendedScreenHandler

```java
public class BoxScreenHandler extends ScreenHandler {
    //We save the blockPos we got from the Server and provide a getter for it so the BoxScreen can read that information
    private BlockPos pos;
    private final Inventory inventory;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the super constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server

    //NEW: The constructor of the client now gets the PacketByteBuf we filled in the BlockEntity
    public BoxScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(9));
        pos = buf.readBlockPos();
    }

    //This constructor gets called from the BlockEntity on the server, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the Client
    public BoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        //[...]
        // See first Screenhandler Tutorial for the rest of the code
        
        //Why do we use BlockPos.ORIGIN here?
        //This is because the packetByteBuf with our blockPosition is only availible on the Client, so we need a placeholder
        //value here. This is not a problem however, as the Server version of the ScreenHandler does not really need this
        //information.
        pos = BlockPos.ORIGIN;

        [...]
    }

    //this getter will be used by our Screen class
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // See Screenhandler Tutorial
    // Shift + Player Inv Slot
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot);
}
```

# Using the Information of the ExtendedScreenHandler in our Screen

```java
public class BoxScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");

    public BoxScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, getPositionText(handler).orElse(title));
        //We try to get the block position to use it as our title, if that fails for some reason we will use the default title
    }

    //This method will try to get the Position from the ScreenHandler, as ScreenRendering only happens on the client we
    //get the ScreenHandler instance here which has the correct BlockPos in it!
    private static Optional<Text> getPositionText(ScreenHandler handler) {
        if (handler instanceof BoxScreenHandler) {
            BlockPos pos = ((BoxScreenHandler) handler).getPos();
            return pos != null ? Optional.of(new LiteralText("(" + pos.toShortString() + ")")) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) { [...] }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) { [...] }

    @Override
    protected void init() { [...] }
}

```

# Registering our ScreenHandler

```java
public class ExampleMod implements ModInitializer {

    [...]
    public static final ScreenHandlerType<BoxScreenHandler> BOX_SCREEN_HANDLER;

    static {
        [...]
       
        //we now use registerExtended as our screenHandler now accepts a packetByteBuf in its Constructor
        BOX_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(BOX, BoxScreenHandler::new);
    }

    @Override
    public void onInitialize() {

    }
}
```

# Result

You have now seen how to transfer data when the ScreenHandler is opened.
In the image you can see the result: The Block's title is now the block
position. Do note that this is just a demonstration, there are easier
ways of setting the position as the title.

You might be wondering: *Can I transfer this data again even after the
Screen was opened?* This is possible by sending custom Packets (see:
[Networking Tutorial](../Modding-Tutorials/networking.md)) after the Screen has been
opened.  
You might also want to have a look at the
`BlockEntityClientSerializable` interface from the Fabric API.

If you only want to sync integer values you can use `PropertyDelegate`s:
[propertydelegates](../Modding-Tutorials/propertydelegates.md).

<img src="/tutorial/bildschirmfoto_vom_2020-08-14_18-37-51.png" width="400" />


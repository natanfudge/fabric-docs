# Syncing Integers with PropertyDelegates

**PropertyDelegate:** A `PropertyDelegate` is a kind of Container which
contains a specific amounts of integer values which can be read or
changed.

In this Tutorial we will sync Integer values between the client and the
server, an example for this in Vanilla would be the smelting progress of
a furnace.

To understand this tutorial you need to read the first
[ScreenHandler](../Modding-Tutorials/screenhandler.md) tutorial. Methods which have no
code here were already shown in that tutorial.

We will not use the
[ExtendedScreenHandler](../Modding-Tutorials/extendedscreenhandler.md) in this
tutorial any more to save us some complexity.

# BlockEntity

As the Block class does not need to be changed at all we leave it out
here.

Our `BlockEntity` now implements `Tickable`, this will provide the
`tick()` method which gets called every tick. We use it to increase our
Integer we want to sync.

```java
public class BoxBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, Tickable {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    //this is the int we want to sync, it gets increased by one each tick 
    private int syncedInt;
    
    //PropertyDelegate is an interface which we will implement inline here.
    //It can normally contain multiple integers as data identified by the index, but in this example we only have one.
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return syncedInt;
        }

        @Override
        public void set(int index, int value) {
            syncedInt = value;
        }

        //this is supposed to return the amount of integers you have in your delegate, in our example only one
        @Override
        public int size() {
            return 1;
        }
    };

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

        //Similar to the inventory: The server has the PropertyDelegate and gives it to the server instance of the screen handler directly
        return new BoxScreenHandler(syncId, playerInventory, this,propertyDelegate);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    //increase the synced Integer by one each tick, we only do this on the server for demonstration purposes.
    @Override
    public void tick() {
    if(!world.isClient)
        syncedInt++;
    }
}

```

# Our new ScreenHandler

```java
public class BoxScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    PropertyDelegate propertyDelegate;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the super constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server

    //Similar to the inventory, the client will allocate an empty propertyDelegate which will be synced with the Server automatically
    
    public BoxScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(9),new ArrayPropertyDelegate(1));
    }

    //This constructor gets called from the BlockEntity on the server, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory aswell as the propertyDelegate will then be synced to the Client
    public BoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Test.BOX_SCREEN_HANDLER, syncId);
        checkSize(inventory, 9);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);
        
        //we need to tell the screenhandler about our propertyDelegate, otherwise it will not sync the data inside.
        this.addProperties(propertyDelegate);

        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        [...]

    }
    
    //we provide this getter for the synced integer so the Screen can access this to show it on screen
    public int getSyncedNumber(){
        return propertyDelegate.get(0);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {[...]}
}


```

# Showing the Information with the Screen

As the screen gets the `ScreenHandler` in its constructor, we have
access to the property delegate from above and can render the integer on
screen.

```java
public class BoxScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");
    BoxScreenHandler screenHandler;

    public BoxScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        //we save a reference to the screenhandler so we can render the number from our propertyDelegate on screen
        screenHandler = (BoxScreenHandler) handler;

    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {[...]}

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //We just render our synced number somewhere in the container, this is a demonstration after all
        //the last argument is a color code, making the font bright green
        textRenderer.draw(matrices, Integer.toString(screenHandler.getSyncedNumber()), 0, 0, 65280);
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

# Result

As the registration of the `ScreenHandler` is identical to that of the
first tutorial we can see the result already! When the BlockEntity is
placed it will increase the `syncedInt` by one each tick; when we look
inside the container, the integer will automatically be synced to the
client and rendered in the top left corner.

[Example Video](https://streamable.com/7aic8q)

If you want a more realistic example, you might want to have a look at
`AbstractFurnaceEntity` and `AbstractFurnaceScreenHandler` in the
Minecraft code.

# Custom Events

Fabric API provides a system that allows mods to react to events that
occur in the game. Events are hooks that satisfy common use cases and/or
provide enhanced compatibility and performance between mods that hook
into the same areas of the code. The use of events often substitutes the
use of mixins. Fabric API provides events for important areas in the
Minecraft codebase that multiple modders may be interested in hooking
into. Some areas do not have hooks, so you can opt to use a mixin, or
create your own event.

In this tutorial, we'll look at creating an event that is triggered when
sheep are sheared. The process of creating an event is:

- creating the event callback interface
- triggering the event from a Mixin
- creating a test implementation

### Creating a Callback Interface

The callback interface describes what must be implemented by event
listeners that will listen to your event. The callback interface also
describes how the event will be called from our mixin. It is
conventional to place an Event object as a field in the callback
interface, which will identify our actual event.

For our Event implementation we will choose to use an array backed
event. The array will contain all event listeners that are listening to
the event. Our implementation will call the event listeners in order
until one of them does not return `ActionResult.PASS`. This means that a
listener can say "cancel this", "approve this" or "don't care, leave it
to the next event listener" using its return value. Using ActionResult
as a return value is a conventional way to make event handlers cooperate
in this fashion.

You'll need to create an interface that has an `Event` instance and
method for response implementation. A basic setup for our sheep shear
callback is:

```java
public interface SheepShearCallback {

    Event<SheepShearCallback> EVENT = EventFactory.createArrayBacked(SheepShearCallback.class,
        (listeners) -> (player, sheep) -> {
            for (SheepShearCallback listener : listeners) {
                ActionResult result = listener.interact(player, sheep);
                
                if(result != ActionResult.PASS) {
                    return result;
                }
            }

        return ActionResult.PASS;
    });

    ActionResult interact(PlayerEntity player, SheepEntity sheep);
}
```

Let's look at this more in depth. When the invoker is called, we iterate
over all listeners:

```java
(listeners) -> (player, sheep) -> {
    for (SheepShearCallback event : listeners) {
```

We then call our method (in this case, `interact`) on the listener to
get its response:

```java
ActionResult result = event.interact(player, sheep);
```

If the listener says we have to cancel (`ActionResult.FAIL`) or fully
finish (`ActionResult.SUCCESS`), the callback returns the result and
finishes the loop. `ActionResult.PASS` moves on to the next listener,
and in most cases should result in success if there are no more
listeners registered:

```java
// ....
    if(result != ActionResult.PASS) {
        return result;
    }
}

return ActionResult.PASS;
```

In the [Fabric API](https://github.com/FabricMC/fabric), we add Javadoc
comments to the top of callback classes to document what each
ActionResult does. In our case, it might be:

```java
/**
 * Callback for shearing a sheep.
 * Called before the sheep is sheared, items are dropped, and items are damaged.
 * Upon return:
 * - SUCCESS cancels further processing and continues with normal shearing behavior.
 * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available
 * - FAIL cancels further processing and does not shear the sheep.
/**
```

### Triggering the event from a Mixin

We now have the basic event skeleton, but we need to trigger it. Because
we want to have the event called when a player attempts to shear a
sheep, we call the event invoker in `SheepEntity#interactMob` when
`dropItems()` is called (ie. sheep can be sheared and player is holding
shears):

```java
@Mixin(SheepEntity.class)
public class SheepShearMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SheepEntity;dropItems()V"), method = "interactMob", cancellable = true)
    private void onShear(final PlayerEntity player, final Hand hand, final CallbackInfoReturnable<Boolean> info) {
        ActionResult result = SheepShearCallback.EVENT.invoker().interact(player, (SheepEntity) (Object) this);
        
        if(result == ActionResult.FAIL) {
            info.cancel();
        }
    }
}
```

In this simple mixin, we call the event invoker
(`SheepShearCallback.EVENT.invoker().[...]`), which then calls all
active listeners to see what it should do. It returns an ActionResult
based on this, and if the result is FAIL, we don't shear the sheep, drop
items, or damage the player's item (`info.cancel();`). **Make sure to
register your mixin in your mixins.json file!**

### Testing Event with a Listener

Now we need to test our event. You can register a listener in your
initialization method (or other areas if you prefer) and add custom
logic there. Here's an example that drops a diamond instead of wool at
the sheep's feet:

```java
SheepShearCallback.EVENT.register((player, sheep) -> {
    sheep.setSheared(true);

    // create diamond item entity at sheep position
    ItemStack stack = new ItemStack(Items.DIAMOND);
    ItemEntity itemEntity = new ItemEntity(player.world, sheep.x, sheep.y, sheep.z, stack);
    player.world.spawnEntity(itemEntity);

    return ActionResult.FAIL;
});
```

Note that this event also sets the sheep to be sheared manually, as it
is normally canceled if we return FAIL. If you don't *need* to cancel
the event, make sure you return `PASS` so other listeners are allowed to
operate as well. Failing to follow these "not spoken rules" may result
in angry modders on your doorstep.

If you enter into your game and shear a sheep, a diamond should drop
instead of wool.

<video src="https://i.imgur.com/dG73Z6G.mp4" width="400" controls=""><a href="https://i.imgur.com/dG73Z6G.mp4">Video</a></video>

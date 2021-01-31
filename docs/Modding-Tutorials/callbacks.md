# Listening to Events (DRAFT)

In this tutorial, you are going to achieve:

1. Understand Events and Callbacks
2. Be able to register a Callback on an existing Event

## Callback Interfaces

There is a series of interfaces named \[EventName\]Callback. They will
handle the events (get called by mixins), invoke callbacks which are
registered on mod initialization.

### Callback Interfaces in Fabric API

Event Callbacks provided by Fabric API can be found in
`net.fabricmc.fabric.api.event` package.

Here is a partial list of existing callbacks.

#### Player Interactive Events

Player:
[AttackBlockCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-interaction-v0/src/main/java/net/fabricmc/fabric/api/event/player/AttackBlockCallback.java)
/
[AttackEntityCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-interaction-v0/src/main/java/net/fabricmc/fabric/api/event/player/AttackEntityCallback.java)
/
[UseBlockCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-interaction-v0/src/main/java/net/fabricmc/fabric/api/event/player/UseBlockCallback.java)
/
[UseEntityCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-interaction-v0/src/main/java/net/fabricmc/fabric/api/event/player/UseEntityCallback.java)
/
[UseItemCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-interaction-v0/src/main/java/net/fabricmc/fabric/api/event/player/UseItemCallback.java)

Player (Client):
[ClientPickBlockApplyCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-interaction-v0/src/main/java/net/fabricmc/fabric/api/event/client/player/ClientPickBlockApplyCallback.java)
/
[ClientPickBlockCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-interaction-v0/src/main/java/net/fabricmc/fabric/api/event/client/player/ClientPickBlockCallback.java)
/
[ClientPickBlockGatherCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-interaction-v0/src/main/java/net/fabricmc/fabric/api/event/client/player/ClientPickBlockGatherCallback.java)

#### Registry Events

[BlockConstructedCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-object-builders-v0/src/main/java/net/fabricmc/fabric/api/event/registry/BlockConstructedCallback.java)
/
[ItemConstructedCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-object-builders-v0/src/main/java/net/fabricmc/fabric/api/event/registry/ItemConstructedCallback.java)

[RegistryEntryAddedCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-registry-sync-v0/src/main/java/net/fabricmc/fabric/api/event/registry/RegistryEntryAddedCallback.java)
/
[RegistryEntryRemovedCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-registry-sync-v0/src/main/java/net/fabricmc/fabric/api/event/registry/RegistryEntryRemovedCallback.java)
/
[RegistryIdRemapCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-registry-sync-v0/src/main/java/net/fabricmc/fabric/api/event/registry/RegistryIdRemapCallback.java)

#### Looting Events

[LootTableLoadingCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-loot-tables-v1/src/main/java/net/fabricmc/fabric/api/loot/v1/event/LootTableLoadingCallback.java)

There is an example using `LootTableLoadingCallback` you can find
[here](../Modding-Tutorials/Miscellaneous/adding_to_loot_tables.md).

#### World Events

[WorldTickCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-lifecycle-v0/src/main/java/net/fabricmc/fabric/api/event/world/WorldTickCallback.java)

#### Server Events

[ServerStartCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-lifecycle-v0/src/main/java/net/fabricmc/fabric/api/event/server/ServerStartCallback.java)
/
[ServerStopCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-lifecycle-v0/src/main/java/net/fabricmc/fabric/api/event/server/ServerStopCallback.java)
/
[ServerTickCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-events-lifecycle-v0/src/main/java/net/fabricmc/fabric/api/event/server/ServerTickCallback.java)

#### Network Events

[C2SPacketTypeCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-networking-v0/src/main/java/net/fabricmc/fabric/api/event/network/C2SPacketTypeCallback.java)
/
[S2CPacketTypeCallback](https://github.com/FabricMC/fabric/blob/1.15/fabric-networking-v0/src/main/java/net/fabricmc/fabric/api/event/network/S2CPacketTypeCallback.java)

### Custom Callbacks

Although there are plenty of events already provided by Fabric API, you
can still make your own events. Please refer to
[events](../Modding-Tutorials/Miscellaneous/events.md).

## Practice

&lt;!-- TODO: Add explaination --&gt;

Let's see Take `AttackBlockCallback` as an example for how register a
listener

Basically, we are going to ... (an event listener) callback to listen
the event.

Since there is not more a method that is able to be called on a block
clicked, you may want to. If you want to make a ;

As stated in javadoc of `AttackBlockCallback`, this event accepts ; You
can interrupt and stop continuing by sending ActionResult.SUCCESS;

```java
/**
 * Callback for left-clicking ("attacking") a block.
 * Is hooked in before the spectator check, so make sure to check for the player's game mode as well!
 *
 * <p>Upon return:
 * <ul><li>SUCCESS cancels further processing and, on the client, sends a packet to the server.
 * <li>PASS falls back to further processing.
 * <li>FAIL cancels further processing and does not send a packet to the server.</ul>
 *
 * <p>ATTACK_BLOCK does not let you control the packet sending process yet.
 */
```

&lt;!-- TODO: Really do sth. --&gt;

```java
public class ExampleMod implements ModInitializer
{
    [...]
    
    @Override
    public void onInitialize() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            // Do sth...
            if ([condition]) {
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
            
        })
    }
}
```

&lt;!-- TODO: An image of the effect of something have done --&gt;

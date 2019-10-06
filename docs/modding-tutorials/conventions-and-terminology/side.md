# Side

Minecraft uses the [Client-server
model](https://en.wikipedia.org/wiki/Client-server_model), that is users
install the game client and connect to a server to play the game. Fabric
allows mods to target either the Minecraft client or the Minecraft
server, but also both at the same time.

In the past, there used to be a simple division into the client and the
server, but with the switch of the single player game mode to an
internal dedicated server, this simple model doesn't fit anymore. Thus,
we have two dimensions when distinguishing between client and server in
Minecraft. These are the physical and logical sides.

For both types of sides, there is a 'client' and a 'server'. However, a
logical client is not equivalent to a physical client, and a logical
server is not equivalent to a physical server either. A logical client
is instead **hosted by** a physical client, and a logical server is
hosted by either a physical server or a physical client.

The logical sides are central in the architecture of both distributions
of Minecraft. Therefore, an understanding of logical sides is vital for
any mod development with Fabric.

## Physical Sides

The physical sides or the environment refer to the two distributions
(jars) of Minecraft game, the client (what the vanilla launcher
launches) and the server (download available on <https://minecraft.net>
for free). A physical side refers to which code is available in the
current environment.

The client and server environment are minified distributions of the same
program, containing only the required parts of the code.

In Fabric, you can often see annotations like
`@Environment(EnvType.CLIENT)`. This indicates that some code is present
only in one environment; in this example, the client.

In Fabric fabric.mod.json and the mixin config, the client/server refers
to the environment.

Each physical side ships classes used by its entry point and the data
generator classes with entrypoint `net.minecraft.data.Main`.

## Logical Sides

Logical sides are about the actual game logic. The logical client does
rendering, sends player inputs to the server, handles resource packs and
partially simulates the game world. The server handles the core game
logic, data packs and maintains the true state of the game world.

The client maintains a partial replica of the server's world, with
copies of objects such as:`net.minecraft.world.World
net.minecraft.entity.Entity
net.minecraft.block.entity.BlockEntity
` These replicated objects allow clients and servers to perform some
common game logic. The client can interact with these objects while the
server is responsible for keeping them in sync. Usually, to distinguish
objects on the logical clients from the ones on the logical server,
access the world of the object and check its 'isClient' field. This can
be used to perform authoritative actions on the server such as spawning
entities, and to simulate actions on the client. This technique is
necessary to avoid desynchronization between the two logical sides.

## Detailed look into all sides

With an understanding of which sides there are and how to distingish
between them, we can now look into every single side with a detailed
look.

### Physical Client

The physical client is the minecraft jar downloaded by the vanilla
launcher. It contains a logical client and a logical server (integrated
server). Its entrypoint is `net.minecraft.client.main.Main`.

As a physical client can load several different worlds each within a
separate logical server, but only one at a time.

Compared to the logical server of the physical server (dedicated
server), the logical server of the physical client (integrated server)
can be controlled by the logical client on the physical client (e.g.,
F3+T reloads data packs and shutting down the client also shuts down the
integrated server). It can also load resource packs bundled in a world
to the logical client on the physical client.

All the logical client contents are exclusive to the physical client.
Hence, you see many environment annotations on rendering, sound, and
other logical client code.

Some mods target physical clients exclusively, for instance, Liteloader,
Optifine, and Minecraft PvP clients (Badlion, Hyperium).

### Physical Server

The physical server is the java dedicated server. Compared to a physical
client, it only has a logical server (dedicated server). Its entrypoint
is `net.minecraft.server.MinecraftServer` and the physical server can
only have one world during its runtime. If a server should switch to
another world, a server restart is required.

Its logical server differs slightly from that of a physical client as
only one logical server instance is ever present when the physical
server is running. Moreover, the logical server of the physical server
can be controlled remotely via Rcon, has a config file called
server.properties, and can send server resource packs.

Despite these differences, most mods are applicable with problems to the
logical servers of both the physical client and the physical server as
long as they do not refer to logical client contents.

Its features of single world and resource pack sending, however, make
vanilla mod (data pack and resource pack combination) installation much
easier compared to on clients, as vanilla physical clients set up when
connecting to the server automatically.

Some mods target physical server exclusively. For instance, Bukkit and
its derivatives (Spigot, Paper, Cauldron, Xxx-Bukkit hybrids) always run
on the physical server.

### Logical Client

The logical client is the interface to the player. Rendering (LWJGL),
resource pack, player input handling, and sounds, happen on the logical
client. It is not present on the physical server.

### Logical Server

The logical server is where most of the game logic is executed. Data
packs, world updates, block entity and entity ticks, mob AI, game/world
saving, and world generation, happen on the logical server.

The logical server on the physical client is called the "Integrated
Server", while the logical server on the physical server is called the
"Dedicated Server" (which is also the name of the physical server
itself).

The logical server runs in its own main thread, even on physical
servers, and has a few worker threads. The lifetime of a logical server
depends on the physical side it is hosted on. On a physical server, a
logical server exists for as long as long as the process is running. On
a physical client, multiple logical servers may be created, but only one
logical server may exist at a time. A new logical server is created when
the player loads a local save and closed when the player closes the
local save.

Most universal mods target the logical server so that they can work both
in single player and multi player scenarios.

## Communication

The only correct way to exchange data between logical clients and
servers by exchanging packets. The packets (as documented on
<https://wiki.vg>) are sent between logical clients and logical servers,
not physical sides. Mods can add packets to transfer custom information
between two logical sides. Packets are exchanged in-memory for a logical
client connected to its own integrated server, and exchanged over a
networking protocol otherwise.

Logical clients send C2S (Client-To-Server) packets to the logical
server. The logical server sends S2C (Server-To-Client) packets the
logical clients. Packets are sent by a write method in a network thread
and received by a call to a read method in a network thread.

For more details on how to handle networking, see [this
article](../Modding-Tutorials/networking.md).

## Common misconceptions about logical servers

Most of the time, mods exclusively targeting the physical server also
work on logical servers inside of physical clients.

However, modders for physical servers usually have assumptions which do
not apply to integrated servers, including but not limited to:

- Only one logical server instance exists on one game run
- The world and entities should always calculate the game logic (i.e.,
  the isClient field of the world object is always false)
- Remote control, resource pack sending, and Favicon are present

These assumptions need to be corrected to make mods that run on logical
servers.

## Conclusion

Possible combinations of physical and logical sides:

|                 |                         |                                                       |
| --------------- | ----------------------- | ----------------------------------------------------- |
|                 | Logical Client          | Logical Server                                        |
| Physical Client | Singleton Always Exists | Exists when in local save; new instance for each play |
| Physical Server | Does Not Exist          | Singleton Always Exists                               |

Ultimately, the main confusion comes from the fact that logical servers
exist on physical clients.

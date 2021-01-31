# Custom Data/Resource pack Resources

## A Foreword

While not necessary to any extent, it is suggested that you first get
accustomed to the basics of Fabric modding and Json parsing before
diving into this. Going through the process of [adding a recipe
type](../Modding-Tutorials/Crafting-Recipes/basic.md) is a good way to get a handle on Json.

Furthermore, most examples in this list will be dealing with data packs.
However, the process for processing resource packs is effectively the
same.

## Reload Listeners

Data packs and resource packs are wonderful things, they are the heart
of data-driven design in the Minecraft modding scene, and as such,
learning to wrangle them is an important step of one's growth as a
modder.

Vanilla comes with a variety of data and resource pack resources,
commonly known are things like textures, models, and after the havoc
that was wrecked in 1.16.2, biomes, but this can be extended to be able
to load custom data from data and resource packs. The process of
extending the resource system to suit your mod's needs will be the main
topic of this article, and it all starts with the use of
`ResourceManagerHelper`.

Any and all work you plan to do regarding resources must be registered
through `ResourceManagerHelper`, or more specifically, one of its
resource types through `ResourceManagerHelper#get(ResourceType)`, where
the type can be either `SERVER_DATA` or `CLIENT_RESOURCES`,
corresponding to data pack and resource pack resources respectively, or
in a dev env, the files found within the `resources/assets` and
`resources/data` folders respectively (in case you are wondering, yes,
your mod's assets and data are handled in the same way as resource packs
and data packs internally, so this will also handle anything put in
there).

From here on forward, examples will be centered around data packs and
use `ResourceType.SERVER_DATA` were applicable, do remember that the
process for data and resource pack resource processing is the same
except for what `ResourceType` is being called.

Continuing on the registry side of things, registering your reload
listener is done through
`ResourceManagerHelper#get(ResourceType).registerReloadListener(IdentifiableResourceReloadListener)`.
Notably once registered your listener will not only handle the initial
load of resources, but any subsequent reloads such as those done by
`/reload` and `F3 + T`

```java
public class ExampleMod implements ModInitializer {
    ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener([...]);
    
    [...]
}
```

## Reload Listeners 2: The Listener

We have now gone over the process of registering your reload listeners,
but you have yet to actually *write* one, that part of the process will
be discussed in this section (specifically, this section will discuss
`SimpleSynchronousResourceReloadListener`, async listeners will be
discussed later).

By all laws of programming, you should not be able to instantiate an
interface in java, its methods are too abstract to call. However,
modders don't care about what actual programmers think so we will do it
anyway.

```java
new SimpleSynchronousResourceReloadListener() {
    @Override
    public Identifier getFabricId() {
        return new Identifier("tutorial", "my_resources");
    }

    @Override
    public void apply(ResourceManager manager) {
        [...]
    }
}
```

What you are seeing here is the actual resource reload listener
(technically *a* resource reload listener, this is not the only type,
however, it is the easiest to implement) that will be registered through
`ResourceManagerHelper`. As you can see there are two notable parts to
it, the `getFabricId` and `apply` methods. The `getFabricId` method is
the simpler of the two, you just need to return a unique identifier for
the reload listener in question, nothing fancy. The apply method,
however, is the main area of interest.

The `apply` method supplies you with a `ResourceManager` with which to
load whatever data you want within the constraints of the registry's
resource type (remember, SERVER\_DATA can only access data packs,
CLIENT\_RESOURCES can only access resource packs). What you do with the
resource manager is really mostly up to you, however, if you are making
some aspect of your mod data-driven then you probably want to read from
a specific *folder*. Doing this is simple but it does require a bit of
care.

Firstly, you are going to want to clear or otherwise prepare to update
anything that is storing the info you are going to be fetching through
the manager, otherwise, your mod will likely break whenever `/reload` or
`F3 + T` is used. Once that is done you should then use
`ResourceManager#findResources(String path, Predicate<String> pathPredicate)`
to fetch the files contained within whatever folder you want (note: the
path is implicitly rooted in either the data or assets folder of all
processed data/resource packs). This will get you a collection with the
paths to all files that are within the specified path and meet the
predicate's criteria (you are probably want to filter for a specific
file type, `.json` in this tutorial), you will then need to iterate
through the collection and do stuff with those paths.

```java
    @Override
    public void apply(ResourceManager manager) {
        // Clear caches here

        for(Identifier id : manager.findResources("my_resource_folder", path -> path.endsWith(".json"))) {
            try(IntputStream stream = manager.getResource(id).getInputStream()) {
                // Consume the stream however you want, medium, rare, or well done.
            } catch(Exception e) (
                TUTORIAL_LOG.error("OOPSIE WOOPSIE!! Uwu We made a fucky wucky!! A wittle fucko boingo! Error occurred while loading funni resource json " + id.toString(), e);
            }
             
         }
        [...]
    }
}
```

There are two important things to note here. Firstly, all resources must
be processed as `InputStreams`. This means that you need to *close* the
resources after processing. Forgetting to do this is an easy way to end
up with a resource leak. The easiest way to make sure any and all
processed resources get closed is to process them using a
try-with-resources block as shown above. The second thing to note is
that there is no way to obtain the raw file you are processing, so the
"must" part of "all files must be processed as `InputStreams`" is an
actual must and not a should. Conveniently, there are many parsers that
will get you your desired file from an input stream, particularly for
Json.

Once you are done, your code should look somewhat like this. At this
point, you can pat yourself in the back as you have successfully made a
part of your mod data-driven! Any files contained in the
"my\_resource\_folder" folder of data packs or mod data will get picked
up by the this.

```java
public class ExampleMod implements ModInitializer {
    ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
        @Override
        public Identifier getFabricId() {
            return new Identifier("tutorial", "my_resources");
        }

        @Override
        public void apply(ResourceManager manager) {
            // Clear Caches Here

            for(Identifier id : manager.findResources("my_resource_folder", path -> path.endsWith(".json"))) {
                try(IntputStream stream = manager.getResource(id).getInputStream()) {
                    // Consume the stream however you want, medium, rare, or well done.
                } catch(Exception e) (
                    TUTORIAL_LOG.error("OOPSIE WOOPSIE!! Uwu We made a fucky wucky!! A wittle fucko boingo! Error occurred while loading funni resource json " + id.toString(), e);
                }
            }
        }
    });
    [...]
}
```


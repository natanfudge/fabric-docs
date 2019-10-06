# Resource

Resource system is the backbone of *Minecraft* resource and data pack
contents. It is important for modders to learn to use the resource
system correctly and efficiently.

## Data and Resource Packs

[Data Pack on Minecraft
Wiki](https://minecraft.gamepedia.com/Data_Pack)  
[Resource Pack on Minecraft
Wiki](https://minecraft.gamepedia.com/Resource_Pack)

## Using resource reload listener

When mods load content from data or resource packs, they need to use a
resource reload listener.

### Basic ideas

In 1.14, Mojang introduced asynchronous resource reloading, utilizing
completable futures.

The resource reloading has two stages: prepare (asynchronous stage in
parallel executors) and apply (synchronous stage on engine
(client/server) thread).

#### Prepare stage

During this stage, you usually obtain raw data/read from the resource
manager and construct your own objects. When you have finished, you can
pass an object carrying the prepared data for the apply stage to use.

Examples:

- Particle loader loads all particle definition JSONs; then it loads
  all textures used and pass a data POJO for the apply stage
- Tag Loader load four types of tags concurrently from JSON files and
  compile them into half-baked tag loaders stored in a data POJO.
- Other JSON data are loaded from disks and converted to JSON objects
  for later deserialization into data objects (Advancements, Loot
  Tables)

#### Apply stage

During this stage, you usually compile the raw data and register them,
or do things that are not safe when off-thread.

Note that before you register your newly loaded stuff, make sure you
clean your old leftovers\! Otherwise it would be bug prone.

Examples:

- Handling dependency relations of tags (tag inclusions)
- Registering textures from the particle loader to the texture manager
  (shared by a few other reload listeners)
- Doing stuff that are not safe off-thread

When you finished this apply stage, your reload listener has finished
the reload.

### Code

The basic resource reload listener interface and its 2 handy abstract
subclasses looks like this:

```java
public interface ResourceReloadListener {
    CompletableFuture<Void> reload(
          ResourceReloadListener.Synchronizer synchronizer,
          ResourceManager manager,
          Profiler prepareProfiler,
          Profiler applyProfiler,
          Executor prepareExecutor,
          Executor applyExecutor
    );
    
    public interface ResourceReloadListener.Synchronizer {
         <T> CompletableFuture<T> whenPrepared(T preparedObject);
    }
}

public abstract class SinglePreparationResourceReloadListener<T> implements ResourceReloadListener {
    @Override
    public final CompletableFuture<Void> reload(ResourceReloadListener.Synchronizer class_3302$class_4045_1, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(() -> this.prepare(manager, prepareProfiler), prepareExecutor).thenCompose(ResourceReloadListener.Synchronizer::method_18352).thenAcceptAsync(object -> this.apply(object, manager, applyProfiler), applyExecutor);
    }
    
    protected abstract T prepare(ResourceManager manager, Profiler profiler);
    
    protected abstract void apply(T preparedObject, ResourceManager manager, Profiler profiler);
}

public interface SynchronousResourceReloadListener extends ResourceReloadListener {
    default CompletableFuture<Void> reload(ResourceReloadListener.Synchronizer class_3302$class_4045_1, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        return class_3302$class_4045_1.<Unit>whenPrepared(Unit.INSTANCE).thenRunAsync(() -> this.apply(manager), applyExecutor);
    }
    
    void apply(ResourceManager manager);
}
```

ResourceReloadListener: The basic interface for resource reloading. The
most flexible and supports the highest level of concurrency for
asynchronous resource preparation, but its usage may be harder for
newcomers to understand.

SinglePreparationResourceReloadListener: A simple implementation for a
simple asynchronous prepare to synchronous apply reloading model. This
is much easier to understand than the base interface, but it does not
support extra levels of concurrency within preparation stage.

SynchronousResourceReloadListener: Does everything in the apply stage.
Most similar to the old resource reload handlers, but the most
inefficient as well. Should be phased out for performance reasons.

Synchronizer: Offers a completeable future that is completed when all
prepare tasks are done. Generally synchronizes the reloading and
prevents synchronous tasks from running async.

### Example Usage

Example of efficient resource reloading:

```java
public class MyReloader implements ResourceReloadListener {
    @Override
    public CompletableFuture<Void> reload(
          ResourceReloadListener.Synchronizer synchronizer,
          ResourceManager manager,
          Profiler prepareProfiler,
          Profiler applyProfiler,
          Executor prepareExecutor,
          Executor applyExecutor
    ) {
        // 1. Prepare stage
        // First, note that when you use CompletableFuture methods that executes some code,
        // you must use the "Async" ones that take an "Executor" argument!
        // Pass the prepare executor for prepare jobs and apply executor for apply jobs
        CompletableFuture<DataOne> prepDataOne = CompletableFuture.supplyAsync(() -> /* supplier, prepares data one */, prepareExecutor);
        CompletableFuture<DataTwo> prepDataTwo = CompletableFuture.supplyAsync(() -> /* supplier, prepares data two */, prepareExecutor);
        // Example: Now we combine the first two data into a third one.
        CompletableFuture<DataThree> prepDataThree = prepDataOne.thenComposeAsync((dataOne) -> /* function, maps to data four */, prepareExecutor)
              .thenCombineAsync(prepDataTwo, (dataFour, dataTwo) -> /* bifunction, merges these two data to data three */, prepareExecutor);
        
        // 2. Synchronizer
        // Then we use the synchronizer to make sure we move to the apply stage
        // Don't need the async compose here luckily
        CompletableFuture<DataThree> applyStart = prepDataThree.thenCompose(synchronizer::whenPrepared);
        
        // 3. Apply stage
        // In the end, return the stage that represents the finish of apply stage
        // Remember to use async version and pass the apply executor!
        return applyStart.thenRunAsync(() -> /* runnable, uploads data three here */, applyExecutor);
    }
    
    // DataOne, DataTwo, etc. omitted
}
```

## Resource

Note that resources are closeable\! They are always backed by an input
stream.

When you finished using a resource, you must close it\! A resource, like
an input stream, must be closed once it is created, or it will cause a
resource leak.

Just be careful when you call these two methods in ResourceManager:

```java
// Use try-with-resources can help you a lot
Resource getResource(Identifier var1) throws IOException;

// Low priority resources are earlier in this list!
// Warning: You must close all resource in this! Not just closing the input stream, there is another input stream for the mcmeta file!
List<Resource> getAllResources(Identifier var1) throws IOException;  list!
```

Turning *Minecraft*'s log level to DEBUG can help you debug some of your
resource leaks from such misuse.

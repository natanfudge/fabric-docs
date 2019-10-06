# Creating Commands

Creating commands can allow a mod developer to add functionality that a player can use through a command. This tutorial will teach you how to register commands, and the command structure of Brigadier along with some more advanced commands structures.

## Registering Commands

If you just want to see how to register commands you've come to the right place here.

Registering commands is done through `CommandRegistry` with the `register` method.

The `register` method specifies two arguments, the dedicated flag and a consumer representing the `CommandDispatcher`. These methods should be placed in your mod's initializer.

The dedicated flag if set to true will tell Fabric to only register the command on a `DedicatedServer` \(if false than the command will register on both the `InternalServer` and `DedicatedServer`\).

Below are a few examples of how the commands can be registered.

```java
CommandRegistry.INSTANCE.register(false, dispatcher -> TutorialCommands.register(dispatcher)); // All commands are registered in a single class that references every command.

CommandRegistry.INSTANCE.register(false, dispatcher -> { // You can also just reference every single class also. There is also the alternative of just using CommandRegistry
    TutorialCommand.register(dispatcher);
    TutorialHelpCommand.register(dispatcher);
});

CommandRegistry.INSTANCE.register(true, dispatcher -> { // Or directly registering the command to the dispatcher.
    dispatcher.register(LiteralArgumentBuilder.literal("tutorial").executes(ctx -> execute(ctx)));
});
```

### A very basic command

Wait isn't this the exact same command from the Brigadier tutorial? Well yes it is but it is here to help explain the structure of a command.

```java
// The root of the command. This must be a literal argument.
dispatcher.register(CommandManager.literal("foo") 
// Then add an argument named bar that is an integer
    .then(CommandManager.argument("bar", integer())
    // The command to be executed if the command "foo" is entered with the argument "bar"
        .executes(ctx -> { 
            System.out.println("Bar is " + IntArgumentType.getInteger(ctx, "bar"));
        // Return a result. -1 is failure, 0 is a pass and 1 is success.
            return 1;
            }))
    // The command "foo" to execute if there are no arguments.
    .executes(ctx -> { 
        System.out.println("Called foo with no arguments");
        return 1;
    })
);
```

The main process registers the command "foo" \(Root Node\) with an optional argument of "bar" \(Child node\). Since the root node must be literal, The sender must enter the exact same sequence of letters to execute the command, so "Foo", "fOo" or "fooo" will not execute the command.

## Brigadier Explained

Brigadier starts with the `CommandDispatcher` which should be thought more as a tree rather than a list of methods. The trunk of the tree is the CommandDispatcher. The register\(LiteralArgumentBuilder\) methods specify the beginning of branches with the following then methods specifying the shape of length of the branches. The executes blocks can be seen at the leaves of the tree where it ends and also supplies the outcome of the system.

The execute blocks specify the command to be ran. As Brigadier's Command is a FunctionalInterface you can use lambdas to specify commands.

### CommandContexts

When a command is ran, Brigadier provides a CommandContext to the command that is ran. The CommandContext contains all arguments and other objects such as the inputted String and the `Command Source` \(ServerCommandSource in Minecraft's implementation\).

### Arguments

The arguments in Brigadier both parse and error check any inputted arguments. Minecraft creates some special arguments for it's own use such as the `EntityArgumentType` which represents the in-game entity selectors `@a, @r, @p, @e[type=!player, limit=1, distance=..2]`, or an `NBTTagArgumentType` that parses NBT and verifies that the input is the correct syntax.

You could do the long method of typing `CommandManager.literal("foo")` and it would work, but you can statically import the arguments and shorten that to `literal("foo")`. This also works for getting arguments, which shortens the already long `StringArgumentType.getString(ctx, "string")` to `getString(ctx, "string")`. This also works for Minecraft's arguments.

And your imports would look something like this:

```java
import static com.mojang.brigadier.arguments.StringArgumentType.getString; // getString(ctx, "string")
import static com.mojang.brigadier.arguments.StringArgumentType.word; // word(), string(), greedyString()
import static net.minecraft.server.command.CommandManager.literal; // literal("foo")
import static net.minecraft.server.command.CommandManager.argument; // argument("bar", word())
import static net.minecraft.server.command.CommandManager.*; // Import everything
```

Brigadier's default arguments are located in `com.mojang.brigadier.arguments`

Minecraft's arguments hide in `net.minecraft.command.arguments` and the CommandManager is at `net.minecraft.server.command`

### Suggestions

Suggestions can be provided to the client to recommend what to input into the command. This is used for Scoreboards and Loot Tables ingame. The game stores these in the SuggestionProviders. A few examples of Minecraft's built in suggestions providers are below

```text
SUMMONABLE_ENTITIES
AVAILIBLE_SOUNDS
ALL_RECIPES
ASK_SERVER
```

Loot tables specify their own SuggestionProvider inside LootCommand for example.

The example below is a dynamically changing SuggestionProvider that lists several words for a StringArgumentType to demonstrate how it works:

```java
public static SuggestionProvider<ServerCommandSource> suggestedStrings() {
    return (ctx, builder) -> getSuggestionsBuilder(builder, /*Access to a list here*/);
}

private static CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, List<String> list) {
    String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

    if(list.isEmpty()) { // If the list is empty then return no suggestions
        return Suggestions.empty(); // No suggestions
    }

    for (String str : list) { // Iterate through the supplied list
        if (str.toLowerCase(Locale.ROOT).startsWith(remaining)) {
            builder.suggest(str); // Add every single entry to suggestions list.
        }
    }
    return builder.buildFuture(); // Create the CompletableFuture containing all the suggestions
}
```

The SuggestionProvider is a FunctionalInterface that returns a CompletableFuture containing a list of suggestions. These suggestions are given to client as a command is typed and can be changed while server is running. The SuggestionProvider provides a CommandContext and a SuggestionBuilder to determine all the suggestions. The CommandSource can also be taken into account during the suggestion creation process as it is available through the CommandContext.

Though remember these are suggestions. The inputted command may not contain an argument you suggested so you still have to parse check inside the command to see if the argument is what you want if it's a String for example and parsers may still throw exceptions if an invalid syntax is inputted.

To use the suggestion you would append it right after the argument you want to recommend arguments for. This can be any argument and the normal client side exception popups will still work. Note this cannot be applied to literals.

```java
argument(argumentName, word())
.suggests(CompletionProviders.suggestedStrings())
    .then(/*Rest of the command*/));
```

### Requires

Lets say you have a command you only want operators to be able to execute. This is where the `requires` method comes into play. The requires method has one argument of a Predicate\ which will supply a ServerCommandSource to test with and determine if the CommandSource can execute the command.

For example this may look like the following:

```java
dispatcher.register(literal("foo")
    .requires(source -> source.hasPermissionLevel(4))
        .executes(ctx -> {
            ctx.getSource().sendFeedback(new LiteralText("You are an operator", false));
            return 1;
        });
```

This command will only execute if the Source of the command is a level 4 operator at minimum. If the predicate returns false, then the command will not execute. Also this has the side effect of not showing this command in tab completion to anyone who is not a level 4 operator.

### Exceptions

Brigadier supports command exceptions which can be used to end a command such as if an argument didn't parse properly or the command failed to execute.

All the exceptions from Brigadier are based on the CommandSyntaxException. The two main types of exceptions Brigadier provides are Dynamic and Simple exception types, of which you must `create()` the exception to throw it. These exceptions also allow you to specify the context in which the exception was thrown using `createWithContext(ImmutableStringReader)`. Though this can only be used with a custom parser. These can be defined and thrown under certain scenarios during the command. Below is a coin flip command to show an example of exceptions in use.

```java
dispatcher.register(CommandManager.literal("coinflip")
    .executes(ctx -> {
        Random random = new Random();

        if(random.nextBoolean()) { // If heads succeed.
            ctx.getSource().sendMessage(new TranslateableText("coin.flip.heads"))
            return Command.SINGLE_SUCCESS;
        }
        throw new SimpleCommandExceptionType(new TranslateableText("coin.flip.tails")).create(); // Oh no tails, you lose.
    }));
```

Though you are not just limited to a single type of exception as Brigadier also supplies Dynamic exceptions.

```java
DynamicCommandExceptionType used_name = new DynamicCommandExceptionType(name -> {
    return new LiteralText("The name: " + (String) name + " has been used");
});
```

There are more Dynamic exception types which each take a different amount of arguments into account \(`Dynamic2CommandExceptionType`, `Dynamic3CommandExceptionType`, `Dynamic4CommandExceptionType`, `DynamicNCommandExceptionType`\). You should remember that the Dynamic exceptions takes an object as an argument so you may have to cast the argument for your use.

### Redirects \(Aliases\)

Redirects are Brigadier's form of aliases. Below is how Minecraft handles /msg have an alias of /tell and /w.

```java
public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    LiteralCommandNode node = registerMain(dispatcher); // Registers main command
    dispatcher.register(literal("tell")
    .redirect(node)); // Alias 1, redirect to main command
    dispatcher.register(literal("w")
    .redirect(node)); // Alias 2, redirect to main command
}

public static LiteralCommandNode registerMain(CommandDispatcher<ServerCommandSource> dispatcher) {
    return dispatcher.register(literal("msg")
    .then(argument("targets", EntityArgumentType.players())
        .then(argument("message", MessageArgumentType.message())
        .executes(ctx -> {
            return execute(ctx.getSource(), getPlayers(ctx, "targets"), getMessage(ctx, "message"));
        }))));
}
```

The redirect registers a branch into the command tree, where the dispatcher is told when executing a redirected command to instead look on a different branch for more arguments and executes blocks. The literal argument that the redirect is placed on will also rename first literal on the targeted branch in the new command.

Redirects do not work in shortened aliases such as \`/mod thing

`having an alias of`/thing \` as Brigadier does not allow forwarding nodes with children. Though you could use alternative methods to reduce the amount of duplicate code for this case.

### Redirects \(Chainable Commands\)

Commands such as `/execute as @e[type=player] in the_end run tp ~ ~ ~` are possible because of redirects. Below is an example of a chainable command:

```java
LiteralCommandNode<ServerCommandSource> root = dispatcher.register(literal("fabric_test"));
LiteralCommandNode<ServerCommandSource> root1 = dispatcher.register(literal("fabric_test") 
// You can register under the same literal more than once, it will just register new parts of the branch as shown below if you register a duplicate branch an error will popup in console warning of conflicting commands but one will still work.
    .then(literal("extra")
        .then(literal("long")
            .redirect(root)) // Return to root for chaining
        .then(literal("short")
            .redirect(root))) // Return to root for chaining
        .then(literal("command")
            .executes(ctx -> {
                ctx.getSource().sendFeedback(new LiteralText("Chainable Command"), false);
                return Command.SINGLE_SUCCESS;
})));
```

The redirect can also modify the CommandSource.

```java
.redirect(rootNode, (commandContext_1x) -> {
    return ((ServerCommandSource) commandContext_1x.getSource()).withLookingAt(Vec3ArgumentType.getVec3(commandContext_1x, "pos"));
})
```

## ServerCommandSource

What if you wanted a command that the CommandSource must be an entity to execute? The ServerCommandSource provides this option with a couple of methods

```java
ServerCommandSource source = ctx.getSource(); 
// Get the source. This will always work.

Entity sender = source.getEntity(); 
// Unchecked, may be null if the sender was the console.

Entity sender2 = source.getEntityOrThrow(); 
// Will end the command if the source of the command was not an Entity. 
// The result of this could contain a player. Also will send feedback telling the sender of the command that they must be an entity. 
// This method will require your methods to throw a CommandSyntaxException. 
// The entity options in ServerCommandSource could return a CommandBlock entity, any living entity or a player.

ServerPlayerEntity player = source.getPlayer(); 
// Will end the command if the source of the command was not explicitly a Player. Also will send feedback telling the sender of the command that they must be a player.  This method will require your methods to throw a CommandSyntaxException
```

The ServerCommandSource also provides other information about the sender of the command.

```java
source.getPosition(); 
// Get's the sender's position as a Vec3 when the command was sent. This could be the location of the entity/command block or in the case of the console, the world's spawn point.

source.getWorld(); 
// Get's the world the sender is within. The console's world is the same as the default spawn world.

source.getRotation(); 
// Get's the sender's rotation as a Vec2f.

source.getMinecraftServer(); 
// Access to the instance of the MinecraftServer this command was ran on.

source.getName(); 
// The name of the command source. This could be the name of the entity, player, the name of a CommandBlock that has been renamed before being placed down or in the case of the Console, "Console"

source.hasPermissionLevel(int level); 
// Returns true if the source of the command has a certain permission level. This is based on the operator status of the sender. (On an integrated server, the player must have cheats enabled to execute these commands)
```

## Some actual examples

Just a few to show:

#### Broadcast a message

```java
public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
    dispatcher.register(literal("broadcast")
    .requires(source -> source.hasPermissionLevel(2)) // Must be a game master to use the command. Command will not show up in tab completion or execute to non op's or any op that is permission level 1.
        .then(argument("color", ColorArgumentType.color())
        .then(argument("message", greedyString())
            .executes(ctx -> broadcast(ctx.getSource(), getColor(ctx, "color"), getString(ctx, "message")))))); // You can deal with the arguments out here and pipe them into the command.
}

public static int broadcast(ServerCommandSource source, Formatting formatting, String message) {
    Text text = new LiteralText(message).formatting(formatting);

    source.getMinecraftServer().getPlayerManager().broadcastChatMessage(text, false);
    return Command.SINGLE_SUCCESS; // Success
}
```

#### /giveMeDiamond

First the basic code where we register "giveMeDiamond" as a literal and then an executes block to tell the dispatcher which method to run.

```java
public static LiteralCommandNode register(CommandDispatcher<ServerCommandSource> dispatcher) { // You can also return a LiteralCommandNode for use with possible redirects
    return dispatcher.register(literal("giveMeDiamond")
        .executes(ctx -> giveDiamond(ctx)));
}
```

Then since we only want to give to players, we check if the CommandSource is a player. But we can use `getPlayer` and do both at the same time and throw an error if the source is not a player.

```java
public static int giveDiamond(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
    ServerCommandSource source = ctx.getSource();

    PlayerEntity self = source.getPlayer(); // If not a player than the command ends
```

Then we add to the player's inventory, with a check to see if the inventory is full:

```java
    if(!player.inventory.insertStack(new ItemStack(Items.DIAMOND))){
        throw new SimpleCommandExceptionType(new TranslateableText("inventory.isfull")).create();
    }   
    return 1;
}
```

#### Antioch

...lobbest thou thy Holy Hand Grenade of Antioch towards thy foe. who being naughty in My sight, shall snuff it.

Aside from the joke this command summons a primed TNT to a specified location or the location of the sender's cursor.

First create an entry into the CommandDispatcher that takes a literal of antioch with an optional argument of the location to summon the entity at.

```java
public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(literal("antioch")
        .then(required("location", BlockPosArgumentType.blockPos()
        .executes(ctx -> antioch(ctx.getSource(), BlockPosArgument.getBlockPos(ctx, "location")))))
    .executes(ctx -> antioch(ctx.getSource(), null)));
}
```

Then the creation and messages behind the joke.

```java
public static int antioch(ServerCommandSource source, BlockPos blockPos) throws CommandSyntaxException { 

    if(blockPos==null) {
        blockPos = LocationUtil.calculateCursorOrThrow(source, source.getRotation()); // For the case of no inputted argument we calculate the cursor position of the player or throw an error if the nearest position is too far or is outside of the world. This class is used as an example and actually doesn't exist yet.
    }

    TntEntity tnt = new TntEntity(source.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), null);

    source.getMinecraftServer().getPlayerManager().broadcastChatMessage(new LiteralText("...lobbest thou thy Holy Hand Grenade of Antioch towards thy foe").formatting(Formatting.RED), false);
    source.getMinecraftServer().getPlayerManager().broadcastChatMessage(new LiteralText("who being naughty in My sight, shall snuff it.").formatting(Formatting.RED), false);
    source.getWorld().spawnEntity(tnt);
    return 1;
}
```

#### Finding Biomes via Command

This example shows examples of redirects, exceptions, suggestions and a tiny bit of text. Note this command when used works but can take a bit of time to work similarly to `/locate`

```java
public class CommandLocateBiome {
    // First make method to register 
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> basenode = dispatcher.register(literal("findBiome")
                .then(argument("biome_identifier", identifier()).suggests(BiomeCompletionProvider.BIOMES) // We use Biome suggestions for identifier argument
                        .then(argument("distance", integer(0, 20000))
                                .executes(ctx -> execute(ctx.getSource(), getIdentifier(ctx, "biome_identifier"), getInteger(ctx, "distance"))))
                        .executes(ctx -> execute(ctx.getSource(), getIdentifier(ctx, "biome_identifier"), 1000))));
        // Register redirect
        dispatcher.register(literal("biome")
                .redirect(basenode));
    }
    // Beginning of the method
    private static int execute(ServerCommandSource source, Identifier biomeId, int range) throws CommandSyntaxException {
        Biome biome = Registry.BIOME.get(biomeId);

        if(biome == null) { // Since the argument is an Identifier we need to check if the identifier actually exists in the registry
            throw new SimpleCommandExceptionType(new TranslatableText("biome.not.exist", biomeId)).create();
        }

        List<Biome> bio = new ArrayList<Biome>();
        bio.add(biome);

        ServerWorld world = source.getWorld();

        BiomeSource bsource = world.getChunkManager().getChunkGenerator().getBiomeSource();

        BlockPos loc = new BlockPos(source.getPosition());
        // Now here is the heaviest part of the method.
        BlockPos pos = bsource.locateBiome(loc.getX(), loc.getZ(), range, bio, new Random(world.getSeed()));

        // Since this method can return null if it failed to find a biome
        if(pos == null) {
            throw new SimpleCommandExceptionType(new TranslatableText("biome.notfound", biome.getTranslationKey())).create();
        }

        int distance = MathHelper.floor(getDistance(loc.getX(), loc.getZ(), pos.getX(), pos.getZ()));
        // Popup text that can suggest commands. This is the exact same system that /locate uses.
        Text teleportButtonPopup = Texts.bracketed(new TranslatableText("chat.coordinates", new Object[] { pos.getX(), "~", pos.getZ()})).styled((style_1x) -> {           style_1x.setColor(Formatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + pos.getX() + " ~ " + pos.getZ())).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.coordinates.tooltip", new Object[0])));
        });

        source.sendFeedback(new TranslatableText("commands.locate.success", new Object[] { new TranslatableText(Registry.BIOME.get(biomeId).getTranslationKey()), teleportButtonPopup, distance}), false);

        return 1;
    }
    // Just a normal old 2d distance method.
    private static float getDistance(int int_1, int int_2, int int_3, int int_4) {
        int int_5 = int_3 - int_1;
        int int_6 = int_4 - int_2;

        return MathHelper.sqrt((float) (int_5 * int_5 + int_6 * int_6));
    }



    public static class BiomeCompletionProvider {
        // This provides suggestions of what biomes can be selected. Since this uses the registry, mods that add new biomes will work without modification.
        public static final SuggestionProvider<ServerCommandSource> BIOMES = SuggestionProviders.register(new Identifier("biomes"), (ctx, builder) -> {
            Registry.BIOME.getIds().stream().forEach(identifier -> builder.suggest(identifier.toString(), new TranslatableText(Registry.BIOME.get(identifier).getTranslationKey())));
            return builder.buildFuture();
        });

    }
```

## Custom Arguments \(Coming Soon\)

Coming Soon

## FAQ

#### What else can I send feedback to the CommandSource?

You can choose between Brigadier's default LiteralMessage or use any one of Minecraft's Text classes \(LiteralText, TranslatableText\)

#### Why does my IDE complain saying that a method executed by my command needs to catch or throw a CommandSyntaxException

The solution to this is just to make the methods throw a CommandSyntaxException down the whole chain as the executes block handles the exceptions.

#### Can I register commands in run time?

You can do this but it is not reccomended. You would get the instance of the CommandManager and add anything you wish to the CommandDispatcher within it.

After that you will need to send the command tree to every player again using `CommandManager.sendCommandTree(PlayerEntity)`

#### Can I unregister commands in run time?

You can also do this but it is very unstable and could cause unwanted side effects. Lets just say it involves a bunch of Reflection.

Once again you will need to send the command tree to every player again using `CommandManager.sendCommandTree(PlayerEntity)` afterwards.

#### Can I register client side commands?

Well Fabric currently doesn't support this natively but there is a mod by the Cotton team that adds this functionality where the commands do not run on the server and only on the client: [https://github.com/CottonMC/ClientCommands](https://github.com/CottonMC/ClientCommands)

If you only want the command to only be visible on the integrated server like `/publish` then you would modify your requires block:

```java
dispatcher.register(literal("publish")
    // The permission level 4 on integrated server is the equivalent of having cheats enabled.
    .requires(source -> source.getMinecraftServer().isSinglePlayer() && source.hasPermissionLevel(4)));
```

#### I want to access X from my mod when a command is ran

This is going to require a way to statically access your mod with a `getInstance` call. Below is a very simple instance system you can place in your mod

```java
private static Type instance;

static { // Static option on class initalize for seperate API class for example
   instance = new Type();
}

public void onInitalize() { // If within your mod initalizer
   instance = this;
}

public static Type getInstance() {
    return instance;
}
```


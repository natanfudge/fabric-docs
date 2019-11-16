# Locate

While testing new `StructureFeature` additions, it's nice to be able to
find it quickly without pumping up the rarity to every chunk.
Thankfully, vanilla has a mechanic for doing this: /locate\!

To add your feature to the locate command, you'll need

- a `StructureFeature`-- normal `Feature` additions will *not* work.
- a mixin to `LocateCommand`
- to add your feature to the `Feature.STRUCTURES` list

### Mixin Time

Create a mixin to `LocateCommand`. You'll need to inject into `register`
at RETURN and add in your feature:

```java
@Inject(method = "register", at = @At(value = "RETURN"))
private static void onRegister(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo info) {
    dispatcher.register(literal("locate").requires(source -> source.hasPermissionLevel(2))
          .then(literal("TutorialStructure").executes(ctx -> execute(ctx.getSource(), "Tutorial_Jigsaw"))));
}
```

We're tagging onto the existing /locate command here.
*"TutorialStructure"* is the name that appears in the locate command
suggestion list, as well as the name you'll need to type in to find your
structure. *"Tutorial\_Jigsaw"* is essentially the locate ID of your
structure. It'll have to match another string we use next.

Note that:

- the literal label can have no spaces
- the execute string is converted to lowercase for comparison and can
  have spaces
- the literal label does *not* need to be the same as the execute
  string

The finalized mixin class:

```java
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.server.command.CommandManager.literal;

@Mixin(LocateCommand.class)
public abstract class LocateCommandMixin {

    private LocateCommandMixin() {
        // NO-OP
    }

    @Shadow
    private static int execute(ServerCommandSource source, String name) {
        throw new AssertionError();
    }

    @Inject(method = "register", at = @At(value = "RETURN"))
    private static void onRegister(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo info) {
        dispatcher.register(literal("locate").requires(source -> source.hasPermissionLevel(2))
                .then(literal("TutorialStructure").executes(ctx -> execute(ctx.getSource(), "Tutorial_Jigsaw"))));
    }
}
```

### Adding to STRUCTURES

Vanilla Minecraft has a hardcoded list of locatable Structure Features
in the Feature class. Fun, right? Thankfully it's public, so we can add
to it directly:

```java
Feature.STRUCTURES.put("tutorial_jigsaw", FEATURE);
```

"tutorial\_jigsaw" should match the lowercased version of the ID we used
in the previous section. FEATURE is an instance of your StructureFeature
registered from the FEATURE registry. An example would be like so:

```java
public static final StructureFeature<DefaultFeatureConfig> FEATURE = Registry.register(
    Registry.FEATURE,
    new Identifier("tutorial", "example_feature"),
    new ExampleFeature(DefaultFeatureConfig::deserialize)
);
```

#### Finished\!

Go in game, and type /locate TutorialStructure. Assuming your structure
was properly generated, you'll get a message in chat leading you to the
closest one.

# Custom Keybinds

### Keybinds: straight from the keyboard

Minecraft handles user input from peripherals such as the keyboard &
mouse using key-binds. When you press W your character moves forward,
and when you press E your inventory opens. Every keybind can also be
configured with the settings menu, so you can make your player move with
arrow keys instead of WASD if you so desire.

This tutorial assumes you have the key bindings API, if not add
`"fabric-key-binding-api-v1": "*"` to the `"depends"` block in your
[fabric.mod.json](../Documentation/fabric_mod_json_spec.md) file.

Adding a key-bind is easy. You'll need to:

- create a KeyBinding object
- react to the key being pressed

See
[here](https://github.com/FabricMC/fabric/blob/1.16/fabric-key-binding-api-v1/src/testmod/java/net/fabricmc/fabric/test/client/keybinding/KeyBindingsTest.java)
for an updated example.

### Creating your Keybind

Declare one of these in an area of your preference:

```java
private static KeyBinding keyBinding;
```

FabricKeyBinding has a Builder for initialization. It takes in an
Identifier, InputUtil.Type, key code, and binding category:

```java
keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
    "key.examplemod.spook", // The translation key of the keybinding's name
    InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
    GLFW.GLFW_KEY_R, // The keycode of the key
    "category.examplemod.test" // The translation key of the keybinding's category.
));
```

If you want a sticky key, add `() -> true` as last parameter.

`GLFW.GLFW_KEY_R` can be replaced with whatever key you want the binding
to default to. The category is related to how the keybinding is grouped
in the settings page.

### Responding to your Keybind

The code here will print "Key 1 was pressed!" ingame.

```java
ClientTickEvents.END_CLIENT_TICK.register(client -> {
    while (keyBinding.wasPressed()) {
    client.player.sendMessage(new LiteralText("Key 1 was pressed!"), false);
    }
});
```

Keep note that this is entirely client-side. To have the server respond
to a keybind, you'll need to send a custom packet and have the server
handle it separately.

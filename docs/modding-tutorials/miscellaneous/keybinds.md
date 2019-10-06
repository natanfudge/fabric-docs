# Custom Keybinds

## Keybinds: straight from the keyboard

Minecraft handles user input from peripherals such as the keyboard & mouse using key-binds. When you press W your character moves forward, and when you press E your inventory opens. Every keybind can also be configured with the settings menu, so you can make your player move with arrow keys instead of WASD if you so desire.

Adding a key-bind is easy. You'll need to:

* create a FabricKeyBinding object
* register your key
* react to the key being pressed

## Creating your Keybind

Fabric API has a **FabricKeyBinding** object, which makes it easier to create your own **KeyBinding**. Declare one of these in an area of your preference:

```java
private static FabricKeyBinding keyBinding;
```

FabricKeyBinding has a Builder for initialization. It takes in an Identifier, InputUtil.Type, key code, and binding category:

```java
keyBinding = FabricKeyBinding.Builder.create(
    new Identifier("tutorial", "spook"),
    InputUtil.Type.KEYSYM,
    GLFW.GLFW_KEY_R,
    "Wiki Keybinds"
).build();
```

GLFW.GLFW\_KEY\_R can be replaced with whatever key you want the binding to default to. The category is related to how the keybinding is grouped in the settings page.

## Registering your Keybind

To register your keybinding, register using the **KeybindingRegistry**, **in the client mod initializer**:

```java
KeyBindingRegistry.INSTANCE.register(keyBinding);
```

If you log in to your game now, you will see your key binding in the settings page.

## Responding to your Keybind

Unfortunately, there's no clear-cut way to respond to a keybinding. Most would agree the best way is to hook into the client tick event:

```java
ClientTickCallback.EVENT.register(e ->
{
    if(keyBinding.isPressed()) System.out.println("was pressed!");
});
```

Keep note that this is entirely client-side. To have the server respond to a keybind, you'll need to send a custom packet and have the server handle it separately.


# Dynamic Model Generation

Dynamically generated models can mainly be used to add models to items
or blocks that are generated, or if you want to auto-create them for a
large number of items or blocks. As always, we will start off by first
registering a new item.

```java
public class ExampleMod implements ModInitializer {

    public static final Item EXAMPLE_ITEM = new Item(new Item.Settings());

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("example_mod", "example_item"), EXAMPLE_ITEM);
    }
}
```

After that is done, we need a function that will generate the model
json.

```java
public static String createItemModelJson(String id, String type) {
    if ("generated".equals(type) || "handheld".equals(type) {
    //The two types of items. "handheld" is used mostly for tools and the like, while "generated" is used for everything else. 
        return "{\n" +
               "  \"parent\": \"item/" + type + "\",\n" +
               "  \"textures\": {\n" +
               "    \"layer0\": \"example_mod:item/" + id + "\"\n" +
               "  }\n" +
               "}";
    } else if ("block".equals(type)) {
    //However, if the item is a block-item, it will have a different model json than the previous two.
        return "{\n" +
               "  \"parent\": \"example_mod:block/" + id + "\"\n"
               "}";
    }
    else {
    //If the type is invalid, return an empty json string.
        return "";
    }
}
```

### Registering Generated Models

Finally, we need to inject the item model into Minecraft. We do this by
mixin into ModelLoader\#loadModelFromJson

```java
@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceManager;getResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/Resource;"), cancellable = true)
    public void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        //First, we check if the current item model that is being registered is from our mod. If it isn't, we continue.
        if (!"example_mod".equals(id.getNamespace())) return;
        //Here, we can do different checks to see if the current item is a block-item, a tool, or other.
        //This can be done in a lot of different ways, like putting all our items in a Set or a List and checking if the current item is contained inside.
        //For this tutorial, we only have 1 item, so we will not be doing that, and we will be going with "generated" as default item type.
        String modelJson = ExampleMod.createItemModelJson(id, "generated");
        if ("".equals(modelJson)) return;
        //We check if the json string we get is valid before continuing.
        JsonUnbakedModel model = JsonUnbakedModel.deserialize(modelJson);
        model.id = id.toString();
        cir.setReturnValue(model);
        cir.cancel();
    }
}
```


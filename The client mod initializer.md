# The Client Mod Initializer
In a multiplayer setting, the normal mod initializer will get called by both the client in the server. This will cause the server to crash when running client-only code, such as registering packets that the client will receive. To make sure that the code only runs on the client, we use a `ClientModInitializer`. 

First create a `ClientModInitializer` class and override `onInitializeClient`:

```java
public class ExampleClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Client-only code here
    }
}
```

Then define the class as a `client` entrypoint in your fabric.mod.json (replace `path.to.clientinit` with the actual path):

```json
  "entrypoints": {
    "client": [
      "path.to.clientinit.ExampleClientInit"
    ]
  }
```

You can test it out by printing something in `onInitializeClient` and see if it gets called while running the server and the client.
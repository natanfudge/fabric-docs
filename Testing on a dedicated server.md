# Testing on a dedicated Server

Whenever you write a mod you should test if it works properly on a dedicated server. This is because the server jar that the server runs has different classes and methods than the client jar. However, in single player, you are running an _integrated server_, that runs on the same process as the client, and therefore has all the classes and methods that the client has. This can make it seem that something works fine, but in reality, the server will crash because of a `ClassNotFound`/ `MethodNotFound` when running as a dedicated server.

So how do we use a dedicated server instead of an integrated one?

### Step 1: Run the server and watch it crash

In IntelliJ IDEA / Eclipse run the `Minecraft Server` launch configuration, and on VSCode run `gradlew runServer`. This will crash but at the same time will generate the necessary files to continuing. 

### Step 2: Accept the EULA

 Open the `run/eula.txt` file and change `eula=false` to `eula=true`.

### Step 3: Change the server to offline mode

Open the `run/server.properties` file and change `online-mode=true` to `online-mode=false`.

### Step 4: Host a local server

Run the server again like in step 1. 

### Step 5: Join the local server

Run the client like you normally do (`Minecraft Client / runClient`). Go to multiplayer and click "Add Server". In the `Server Address` specify `localhost` and press `Done`. After that join the `Minecraft Server` that will appear in the server list.

### Step 6: Op yourself

In the GUI that the server opened, look at the log to see what player has joined, for example - `Player475 joined the game`. Then op the player by typing `/op Player475` for example in the terminal of the GUI.

That's it! You can go into creative mode or do whatever you want.

Whenever you need to do something on the dedicated server you will need to repeat steps 4 - 6. 




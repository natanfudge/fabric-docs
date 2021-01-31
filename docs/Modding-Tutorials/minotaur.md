# Publishing your mod on Modrinth using Minotaur

Minotaur is a Gradle plugin similar to CurseGradle. This page only goes
through the basics of Minotaur, you should [checkout its GitHub
documentation for a further
understanding](https://github.com/modrinth/minotaur).

## Basic

First of all, add the minotaur plugin to your plugins list in your
`build.gradle` file as so:

```groovy
plugins {
    id "com.modrinth.minotaur" version "1.1.0"
}
```

Now you can create a new Gradle task for uploading to Modrinth.

Here is a basic example.

```groovy
import com.modrinth.minotaur.TaskModrinthUpload

task publishModrinth (type: TaskModrinthUpload) {
    token = 'mySecretToken' // Use an environment property if releasing your source code on GitHub!
    projectId = 'modrinthModID' // The ID of your modrinth project, slugs will not work.
    versionNumber = '1.0.0' // The version of the mod to upload.
    uploadFile = remapJar // This links to a task that builds your mod jar and sets "uploadFile" to the mod jar.
    addGameVersion('1.16.2') // Any minecraft version.
    addLoader('fabric') // Can be fabric or forge. Modrinth will support liteloader and rift at a later date.
}
```

Get your Modrinth [token from
here.](https://modrinth.com/dashboard/settings) You can use this token
to access the Modrinth API and alongside Minotaur.

Now when you run `gradle publishModrinth` you should see that your mod
has been compiled and uploaded to Modrinth, like so:

![](https://iili.io/KYUDq7.png)

However, this can be limiting and sometimes repetitive to upload, you
would need to edit the values every time you want to release a version.
This is where Java `stout` and `stin` come in.

## Advanced

So, you have a basic implementation. Let's make it dynamic, allowing you
to input values through the command line when the task is ran.

First of all, we would need to create a `BufferedReader`. Why a
`BufferedReader` instead of `System.out.readLine()`?
`System.out.readLine()` only works on command terminals, and doesn't
work on normal IDE terminals such as Eclipse, IntelliJ Idea and Visual
Studio Code. Since Gradle tasks are most commonly ran in the IDE, it
would be better to use `BufferedReader` as it supports in-IDE terminals.

Add a new `BufferedReader` collecting a buffer from `System.in` to the
top of your Modrinth task.

```Java
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
```

Now you can collect user input by simply calling the method:
`br.readLine()`.

Let's add this to our task, shall we? We'll also add some more data,
such as a markdown changelog and make the version name different from
the semantic versioning number.

```groovy
import com.modrinth.minotaur.TaskModrinthUpload

task publishModrinth (type: TaskModrinthUpload) {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter a Github Access Token: ");
    token = br.readLine(); // Get the GitHub Access Token you got from the basics part of this tutorial.
    projectId = "" // Enter your modrinth mod ID here.
    System.out.println("Enter the version number:");
    versionNumber = br.readLine();
    System.out.println("Enter the version name:");
    versionName = br.readLine();
    uploadFile = remapJar // This links to a task that builds your mod jar and sets "uploadFile" to the mod jar.
    System.out.println("Enter the game version number: (See minotaur docs for valids)");
    addGameVersion(br.readLine());
    System.out.println("Enter changelog:");
    changelog = br.readLine();
    addLoader("fabric")
}
```

Now, when `gradle publishModrinth` is ran, it asks you for some sweet
user input. Hell, you could even go as far as using Swing or JavaFX to
make a GUI!

Minotaur is great alongside CurseGradle. You can merge both of the tasks
together. Calling your CurseGradle task after the Modrinth one is
complete:

```groovy
task publishModrinth (type: TaskModrinthUpload) {
    // ... Modrinth Upload Stuff
    curseforge<id> // Begin the cursegradle task. Replacing ID with the id you set on the cursegradle config.
}
```


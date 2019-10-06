# Setting up Visual Studio Code with Fabric

A few plugins are recommended to ensure that you get the best experience possible. You only need to install these once.

* [Language Support for Java\(TM\) by Red Hat](https://marketplace.visualstudio.com/items?itemName=redhat.java)
* [Debugger for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-debug)
* [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

## Importing a project

Clone the project into a folder and then open the folder in Visual Studio Code. The IDE should then start importing the Gradle project \(if it does not start after about 30 seconds, open the build.gradle file\).

![](../.gitbook/assets/vscode_import%20%281%29.png)

Once this has completed, code completion for game engine code should be present, which should be helpful in mod development.

## Generating run configurations

To run the game with debugging support enabled you will need to generate the run configs. This can be done by running the \`vscode\` task in Gradle. The easiest way to open a terminal window is to go to \_View\_ -&gt; \_Terminal\_. This will then open a terminal pane in the correct directory. Next, run \`.\gradlew vscode\` - this will automatically generate the necessary launch.json file containing the run configs.

![](../.gitbook/assets/vscode_gradle%20%282%29.png)

Finally, to launch the game, select the debug menu item on the left. This will then build your mod and launch the game.

## Generating minecraft source

If you want to browse the source of minecraft you can use the \`genSources\` task from gradle, this can be done by running the following command in your terminal \`.\gradlew genSources\`.

Next you will need to fresh the java project, this can be done by pressing \`Shift+Alt+U\` while having the build.gradle file open.

To search for minecraft classes you can open the search pane with \`Shift+P\` and then prefix your searches with \`\#\` to search for the minecraft classes.


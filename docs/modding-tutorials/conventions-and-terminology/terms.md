# Learning Conventions & Terminology

Before starting with Fabric modding, it's important to understand some of the key terms and phrases used in future tutorial pages. It's also good to know basic conventions for things such as package structure and modid naming. Knowing these early will help you to understand tutorials better and enable you to ask better questions when needed.

## Mod ID

Throughout the documentation, we'll often refer to a Mod ID, or modid in code. Mod ID stands for "Mod Identifier," and it is a string that should uniquely identify your mod. Mod IDs are commonly associated with identifier namespaces of the same name, and as such, follow the same restrictions. Mod IDs can consist only of lowercase characters `a-z`, numbers `0-9`, and the symbols `_-`. For example, Minecraft uses the `minecraft` namespace. Additionally, a mod ID must consist of at least two characters.

A mod ID is often a compact version of the name of the mod which makes it short but recognizeable and prevents naming conflicts. Conventionally, a project named "My Project--" could be called `myproject`, `my_project`, or in some cases, `my-project` also works, but dashes in modids can be a slight pain to deal with at times \[citation needed\]. This mod would register items and blocks using this mod ID as a registry namespace.

Some of the starter tutorials will use a placeholder mod ID and register items and blocks under a placeholder namespace, and you can think of it as a starter template-- while leaving this unchanged is not dangerous for testing, remember to change it if you intend to release your project.

## Tags

Draft. Edit it.

Convention namespace for tags is `c`.

## Maven Group & Package Names

According to Oracle's Java documentation, they are written in all lower case to avoid conflict with the names of classes or interfaces. The reverse of your domain name is used to start the names. Read more at [https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html](https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html).


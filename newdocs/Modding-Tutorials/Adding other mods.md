# Adding other mods in Fabric

## Introduction 

Right now, the only way to add other mods to your development workspace (or to the production environment) is via Gradle dependencies, specified in `build.gradle`. In each case, you (usually) put a _repository_ in the `repositories` block:

```groovy
repositories {
   maven { url = "some.repository.org" }
}
```

**When using a Kotlin buildscript (`build.gradle.kts`), the syntax for a repository is different:**

 ```kotlin
repositories {
   maven (url = "some.repository.org")
}
 ```

In the examples we will only use the first (the Groovy - `build.gradle`) syntax. 

After adding a repository, you will need to add a _dependency_ in the `dependencies` block:

```groovy
dependencies {
    modSomething("somegroup:somemod:someversion")
}
```

Following that, remember to always refresh Gradle.

**A common problem is having Fabric API get included twice. To solve this issue see `Troubleshooting` in the end.**

##  Adding test/development-only dependencies

Development-only dependencies are always specified using `modRuntime`.

For example, you want to use Roughly Enough Items to make grabbing items and seeing recipes you made easier.

### CurseMaven

The easiest (and best IMO) way to add dev dependencies is [CurseMaven](https://github.com/Wyn-Price/CurseMaven), provided it is hosted on [CurseForge](https://www.curseforge.com/minecraft/mc-mods).   Note that when using CurseMaven, you will need to add any dependencies of the mod that are not included in the mod jar that is published on Curseforge. In most cases, this will only be [Fabric API](https://github.com/FabricMC/fabric), which you already have in your build.gradle (in the example mod), and in some mods, [Fabric Language Kotlin](https://github.com/FabricMC/fabric-language-kotlin), which you can follow the gradle instructions for in the Github README.

- Add CurseMaven as a Gradle plugin:

  ```groovy
  plugins {
    // This is at the top of the build.gradle file. You will see fabric-loom as a plugin here as well.
    id ("com.wynprice.cursemaven") version "2.1.1"
  }
  ```

  It is recommended to only use the latest version of CurseMaven, provided [here](https://login.gradle.org/plugin/com.wynprice.cursemaven).

-  Go to the mod download page ands select the version you want under `files`, for example [this file](https://www.curseforge.com/minecraft/mc-mods/roughly-enough-items/files/2844187). Copy the last part (the number) of the URL. In this case it's `2844187`. 

- Specify the special CurseMaven `modRuntime` dependency. The syntax is `modRuntime("curse.maven:<mod-name>:<copied-file-id>)"`:

  ```groovy
  dependencies {
     // You will see the 'minecraft', 'mappings', and fabric dependencies here as well.
     modRuntime ("curse.maven:roughly-enough-items:2844187")
  }
  ```

### Proper Maven

If the mod author has hosted his mod on a maven repository himself (which is uncommon), then by the far the best way is to use this method. The mod author will (usually) explain how to depend on it.  In Roughly Enough Item's case, it is actually hosted, but the usage instructions are not up to date.
As an example we will use [Not Enough Crashes](https://github.com/shedaniel/RoughlyEnoughItems.wiki.git):

```groovy
repositories {
    // [...]
    jcenter()
}
dependencies {
    modRuntime("com.lettuce.fudge:notenoughcrashes:1.1.3+1.15")
}
```

### Local file

Another way to add a mod is by downloading the mod jar itself and adding it as a local file dependency. **This method is unrecommended because it would fail for other people building your project!**. Place the jar in your project, for example in `testmods/RoughlyEnoughItems-3.2.26.jar`. Then specify it as a `files` dependency:

```groovy
dependencies {
   modRuntime(files("testmods/RoughlyEnoughItems-3.2.26.jar"))
}
```

### JitPack

If the mod is hosted on [Github](https://github.com/), you can use JitPack to retrieve it. 

- Specify the JitPack repository:

  ```groovy
  repositories {
     maven { url "https://jitpack.io" }
  }
  ```

- Look up the mod repository on [jitpack.io](https://jitpack.io/). In our case it's [shedaniel/roughlyenoughitems](https://jitpack.io/#shedaniel/roughlyenoughitems).  In some mod's case, it has releases on Github, so you can select those releases, and JitPack will give you the dependency notation you need for the selected version. In Roughly Enough Item's case, there are releases, but they are very outdated. In that case or in the case there are no releases, we select the `commits` tab and press `Get it` on the SNAPSHOT version. **Doing this is not recommended because the mod author might push breaking changes or changes that do not even compile, and bring those issues to your workspace as well**. If you do want to proceed, use the provided dependency notation with `modRunime`:

  ```groovy
  dependencies {
  	modRuntime("com.github.shedaniel:roughlyenoughitems:-SNAPSHOT")
  }
  ```

  

## Adding hard dependencies 

Hard (always present) dependencies are specified using `modImplementation`. Hard dependencies are usually libraries your mod depends on.  These libraries should have sections explaining how to depend on them. Say, [Cardinal Components API](https://github.com/NerdHubMC/Cardinal-Components-API#adding-the-api-to-your-buildscript-loom-024):

```groovy
repositories {
    maven { url = "https://maven.abusedmaster.xyz"}
}

dependencies {
    modImplementation ("com.github.NerdHubMC:Cardinal-Components-API:2.1.0")
}
```

### Bundled dependency

In almost all cases, you should bundle libraries you depend on using the `include` keyword. Exceptions are big libraries that are very commonly used (Fabric API, Fabric Language Kotlin):

```groovy
dependencies {
    // [...]
    // IN ADDITION to the modImplementation line.
    include ("com.github.NerdHubMC:Cardinal-Components-API:2.1.0")
}
```

Bundling a dependency means no additional work is required from the user. The library jar is copied to your mod jar when you publish it. When other mods `include` the same mod, Fabric Loader will only load the latest version. **This is why you should `include` mod dependencies instead of `shadow`ing them**. 

### Expected (not-bundled) dependency

If you don't want to include the library mod in your mod (Fabric API, Fabric Language Kotlin), you omit the `include` line. **Your users must add the library themselves for your mod to work**. The example mod already does this with Fabric API. 

## Adding soft dependencies

Soft (sometimes present) dependencies are specified using **both** `modCompileOnly` **and** `modRuntime`. Soft dependencies are usually other mods you want to have special interactions with, but are not required for the core of your mod.  For example, a Roughly Enough Items plugin. We will use tricks from the `Troubleshooting` section to figure out the correct notation for depending on it:

```groovy
dependencies {
    modCompileOnly("me.shedaniel:RoughlyEnoughItems:3.2.26")
    modRuntime("me.shedaniel:RoughlyEnoughItems:3.2.26")
}
```

Note that we don't use `include` because that would defeat the purpose of only sometimes having the mod.

## Adding dependencies as a library author

Library dependencies are split into 2 parts:

### Implementation dependencies 

These are dependencies required for your library to function, but your users does not interact with them. They are specified using `modImplementation` like normal dependencies.

### API dependencies

These are dependencies that the library consumer is supposed to use. They are specified using `modApi`. A common pattern is to have multiple small libraries, and then have one big library that `include`s and `modApi`s all the smaller libraries, and only publish the big library to end users (not modders). This allows modders to only include the parts they want, while not needing to publish many different mods. (You still need to publish all the small libraries to a maven for modders).

## Adding non-mod dependencies

When dealing with normal (non-mod) dependencies, drop the `mod` prefix. The best way to add a non-mod dependency is by using `implementation` and `include`. Because `include` is a Fabric construct, Loom converts the non-mod dependency to a mod so it can be included in your mod jar.

## Troubleshooting

### Fabric API modules are being included twice!

Exclude fabric API using `exclude group: "net.fabricmc.fabric-api` from your dependencies, for example:

```groovy
dependencies {
    modImplementation ("com.github.NerdHubMC:Cardinal-Components-API:2.1.0") {
        exclude group: "net.fabricmc.fabric-api"
    }
}
```

In a Kotlin buildscript the syntax is a bit different:

```kotlin
dependencies {
    modImplementation ("com.github.NerdHubMC:Cardinal-Components-API:2.1.0") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}
```

### The library author decided he doesn't want his library to be used and didn't explain how to add it to the build.gradle (or what the correct version is)!

Study the mod's build.gradle, gradle.properties, and maven on Github. We need:

- The repository (specified in the `repositories {}` block)

- The maven group and archives base name

- The version

   The final dependency notation will be `<maven-group>:<archives-base-name>:version`.

Lets look at [Roughly Enough Items](https://github.com/shedaniel/RoughlyEnoughItems/blob/3.x/build.gradle). The repository is the hardest part. [Here](https://github.com/shedaniel/RoughlyEnoughItems/blob/e965379aef6270e79e0ddadcd1b4b222fecea2e3/build.gradle#L145) we can see it referencing `modmuss50.me` in the `publishing` block,  which means it's the Fabric maven. No need to add anything because Loom adds it by default.
[Here](https://github.com/shedaniel/RoughlyEnoughItems/blob/e965379aef6270e79e0ddadcd1b4b222fecea2e3/build.gradle#L12) the archivesBaseName and group is set explicitly, but sometimes you will need to look in the `gradle.properties` to see the actual values.
Finally - the version. We know it's on the Fabric maven so we go [Here](https://maven.fabricmc.net/). We know the group is `me.shedaniel` so we follow through [me/shedaniel](https://maven.fabricmc.net/me/shedaniel/). We know the archivesbasename is `RoughlyEnoughItems` so we follow through `RoughlyEnoughItems`. Now we pick a version out of the listed files. 


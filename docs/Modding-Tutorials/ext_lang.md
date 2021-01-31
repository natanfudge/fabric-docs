# Using Crowdin to create live translations.

Crowdin is a very good platform for getting translations. Luckily for
us, a user has created a [open source
library](https://github.com/gbl/CrowdinTranslate/tree/master) that you
can use in your mod to get translations from Crowdin and apply them when
the game launches.

## Getting Started

First of all, create or login to your crowdin account and make or
navigate to your project.

If you have a crowdin membership, great! If not, you can still use
Crowdin for free (up to 15000 strings) or if your project is open source
you can apply for an open source membership for free.

Note the following down:

- Your crowdin project ID.
- Your mod ID.

Set your primary language to the language your language file is in, if
your primary language file is `en_us.json`, set it to
`English, United States`

Now, once you have selected the target languages, you can upload your
primary file.

## Implementing the Library

Get started implementing it into your mod by adding the maven repository
to the `build.gradle` file:

```Java
repositories {
    maven {
        url = "https://minecraft.guntram.de/maven/"
    }
}
```

Now add the necessary `modImplementation` and `include` to your
dependencies:

```Java
modImplementation "de.guntram.mcmod:crowdin-translate:1.2"
include "de.guntram.mcmod:crowdin-translate:1.2"
```

You can now add the following method to your `ClientModInitializer`,
replacing `projectname` with your crowdin project name and `modid` with
your mod's ID.

```Java
CrowdinTranslate.downloadTranslations("projectname", "modid");
```

Now, when launching the game, you can see that the translations will
have downloaded from crowdin and been applied to your mod.

### Troubleshooting

Q: Nothing is being downloaded/The library says the file does not exist!

A: Make sure to press "Build Project" on the crowdin project settings.
You must build your project everytime you want to update player's ingame
translations.

Q: The downloaded files are in the wrong format!/They appear as
`es_ES.json` instead of lowercase format!

A: Make sure you set your primary file to be in lowercase format, as it
won't work in lowerUpper format.

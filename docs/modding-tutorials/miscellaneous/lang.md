# Name translations

Notice how your item has a weird display name, such as
*item.tutorial.my\_item*? This is because your item's name doesn't have
a translation in your game's selected language. Translations are used to
support multiple different languages for a single string.

## Creating a lang file

You can use lang files to provide translations for translatable strings
in-game. You'll need to create a file with an appropriate file name for
your language-- to find your languages' code, visit [this
link](https://minecraft.gamepedia.com/Language). English is en\_us. Once
you have your language code, create a JSON file at
<span class="underline">resources/assets/modid/lang/</span>; a full
example for an English translation file would be
<span class="underline">resources/assets/tutorial/lang/en\_us.json</span>.

## Adding a translation

After you've created the lang file, you can use this basic template to
add translations:

```JavaScript
{
  "item.tutorial.my_item": "My Item",
  "item.tutorial.my_awesome.item": "My Awesome Item",
  [...]
}
```

where the first string is any translatable string (such as an item name,
or TranslatableText). If you're following along in the wiki tutorial,
remember to change modid to \`tutorial\`, or whatever modid you've
chosen.

## Using custom translatable text

Whenever a function accepts `Text`, you have the option of giving it a
`new LiteralText()`, which means minecraft will use the string in the
constructor argument as-is. However, this is not advisable because that
would make it difficult to translate that text to another language,
should you wish to do that. This is why whenever a `Text` object is
needed, you should give it a `new TranslatableText()` with a translation
key, and then translate the key in the lang file. For example, when
adding a tooltip, do:

```java
@Override
public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
    tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip"));
}
```

And then add in the lang file:

```JavaScript
{
  "item.tutorial.fabric_item.tooltip": "My Tooltip"
}
```

And the tooltip will be displayed as "My Tooltip" \!

### Adding dynamic values to translatable text

Say you want the text to change based on some variable, like the current
day and month. For a dynamic number, we put a %d where you want the
number to show in the lang entry value, for example:

```JavaScript
{
  "item.tutorial.fabric_item.tooltip": "My Tooltip in day %d, and month %d" 
}
```

Then we pass the variables we use in our string by the order it appears
in the text. First the day, then the month:

```java
int currentDay = 4;
int currentMonth = 7;
tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip", currentDay, currentMonth));
```

And the tooltip will be displayed as "My Tooltip in day 4, and month 7".
In order to pass a string, we use `%s` instead of `%d`. If you want for
it to literally show `%`, use `%%`. For more information, see [Java
String.format](https://dzone.com/articles/java-string-format-examples)
(it works the same way).

### Adding a new line

Making `\n` work was far too difficult for Mojang, so in order to have a
string with multiple lines you must split the translation key into
multiple keys:

```JavaScript
{
  "item.tutorial.fabric_item.tooltip_1": "Line 1 of my tooltip" 
  "item.tutorial.fabric_item.tooltip_2": "Line 2 of my tooltip" 
}
```

Then add the `TranslatableText` parts individually:

```java
tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip_1"));
tooltip.add(new TranslatableText("item.tutorial.fabric_item.tooltip_2"));
```

And the tooltip will be displayed as:

    Line 1 of my tooltip
    Line 2 of my tooltip

# Translation format

The translation key for objects you have registered is in the form

    <object-type>.<modid>.<registry-id>

| Object Type | Format                        | Example |
| ----------- | ----------------------------- | ------- |
| Block       | `block.<modid>.<registry-id>` |         |

```
"block.tutorial.example_block": "Example Block"  
```

```
|
```

|      |                                                   |
| ---- | ------------------------------------------------- |
| Item | \<code\> item.\<modid\>.\<registry-id\> \</code\> |

\<code\> "item.tutorial.my\_item": "My Item"\</code\> |

|           |                                                       |
| --------- | ----------------------------------------------------- |
| ItemGroup | \<code\> itemGroup.\<modid\>.\<registry-id\>\</code\> |

\<code\> "itemGroup.tutorial.my\_group": "My Group"\</code\>|

|       |
| ----- |
| Fluid |

\<code\> fluid.\<modid\>.\<registry-id\> \</code\> ||

|            |
| ---------- |
| SoundEvent |

\<code\> sound\_event.\<modid\>.\<registry-id\> \</code\> ||

|              |
| ------------ |
| StatusEffect |

\<code\> mob\_effect.\<modid\>.\<registry-id\> \</code\> ||

|             |
| ----------- |
| Enchantment |

\<code\> enchantment.\<modid\>.\<registry-id\> \</code\> ||

|            |
| ---------- |
| EntityType |

\<code\> entity\_type.\<modid\>.\<registry-id\> \</code\> ||

|        |
| ------ |
| Potion |

\<code\> potion.\<modid\>.\<registry-id\> \</code\> ||

|       |
| ----- |
| Biome |

\<code\> biome.\<modid\>.\<registry-id\> \</code\> ||

For types not in this list, see
`net.minecraft.util.registry.Registry.java`.

# Playing Sounds

Ready to replace those zombie sounds with your own sounds? You've come
to the right place.

## Playing a pre-existing sound

Playing a pre-exisiting sound is fairly simple. Make sure you are on the
logical server and call `world.playSound` like so:

```java
if (!world.isClient) {
      world.playSound(
              null, // Player (purpose unknown, edit if you know)
              blockPos, // The position of where the sound will come from
              SoundEvents.BLOCK_ANVIL_LAND, // The sound that will play, in this case, the sound the anvil plays when it lands.
              SoundCategory.BLOCKS, // This determines which of the volume sliders affect this sound
              1f, //Volume multiplier, 1 is normal, 0.5 is half volume, etc
              1f // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
      );
}
```

You can do this when the block is right clicked for example, by
overriding `activate`:

```java
public class ExampleBlock extends Block {
    [...]
    
    @Override
    public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity placedBy, Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            world.playSound(null, blockPos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1f, 1f);
        }

        return false;
    }
}
```

## Adding a custom sound

There's some extra steps involved in playing a sound that's not already
in the game. We'll add a "punch" sound, that is available under CC0
[here](https://freesound.org/people/Ekokubza123/sounds/104183/) for the
example.

### Step 1: Add your .ogg sound file

To play a sound, you first need a sound file. Minecraft uses the `.ogg`
file format for those. If your sound file is in a different format (like
the one in the provided link), you can use an online converter to
convert from your format to `.ogg`. Now put your .ogg file in the
`resources/assets/modid/sounds` folder. In the example case the file
will be `resources/assets/tutorial/sounds/my_sound.ogg`.

### Step 2: Add a sounds.json file, or add to it if you already have one

Under `resources/assets/modid` add a new file named `sounds.json`, if
you do not have one yet. Then add a new object entry with the name of
your sound, and the sound's identifier in `"sounds"`, for example:

```javascript
{
  "my_sound": {
    "sounds": [
      "tutorial:my_sound"
    ]
  }
}
```

You can also add a category and subtitle to your sound:

```javascript
{
  "my_sound": {
    "category": "my_sounds",
    "subtitle": "*punch*",
    "sounds": [
      "tutorial:my_sound"
    ]
  }
}
```

See the [Minecraft Wiki](https://minecraft.gamepedia.com/Sounds.json)
for more details about `sounds.json`.

### Step 3: Create your sound event

Simply create a new instance of `SoundEvent` with the identifier
`modid:sound_name`, for example:

```java
public class ExampleMod {
    [...]
    public static final Identifier MY_SOUND_ID = new Identifier("tutorial:my_sound")
    public static SoundEvent MY_SOUND_EVENT = new SoundEvent(MY_SOUND_ID);
}
```

### Step 4: Register your sound event

Register your sound event under the `SOUND_EVENT` registry:

```java
@Override
public void onInitialize(){
     [...]
     Registry.register(Registry.SOUND_EVENT, ExampleMod.MY_SOUND_ID, MY_SOUND_EVENT);
} 
```

### Step 5: Use your sound event

Use the sound event just like we explained at the start (`activate` is
just an example, use it anywhere you have access to `World` instance):

```java
public class ExampleBlock extends Block {
    @Override
    public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity placedBy, Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            world.playSound(
                    null, // Player (purpose unknown, edit if you know)
                    blockPos, // The position of where the sound will come from
                    ExampleMod.MY_SOUND_EVENT, // The sound that will play
                    SoundCategory.BLOCKS, // This determines which of the volume sliders affect this sound
                    1f, //Volume multiplier, 1 is normal, 0.5 is half volume, etc
                    1f // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
            );
        }
        return false;
    }
}
```

You should now hear a punch sound when you right click your block\!

### Troubleshooting

Don't hear anything? Try:

- Turning up your in-game volume sliders.
- Deleting the output directory.


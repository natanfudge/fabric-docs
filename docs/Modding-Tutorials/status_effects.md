## Adding Status Effects

To add status effects to your mod, you'll need to:

- create a class that extends `StatusEffect`
- register your status effect
- add custom functionality
- add translations and textures for your status effect

### Creating Status Effect Class

We will add new status effect that gives you EXP every tick.

```java
public class ExpStatusEffect extends StatusEffect {
  public ExpStatusEffect() {
    super(
      StatusEffectType.BENEFICIAL, // whether beneficial or harmful for entities
      0x98D982); // color in RGB
  }

  // This method is called every tick to check weather it should apply the status effect or not
  @Override
  public boolean canApplyUpdateEffect(int duration, int amplifier) {
    // In our case, we just make it return true so that it applies the status effect every tick.
    return true;
  }

  // This method is called when it applies the status effect. We implement custom functionality here.
  @Override
  public void applyUpdateEffect(LivingEntity entity, int amplifier) {
    if (entity instanceof PlayerEntity) {
      ((PlayerEntity) entity).addExperience(1 << amplifier); // Higher amplifier gives you EXP faster
    }
  }
}
```

### Registering Status Effect

This registers our status effect.

```java
public class ExampleMod implements ModInitializer {
  public static final StatusEffect EXP = new ExpStatusEffect();

  @Override
  public void onInitialize() {
    Registry.register(Registry.STATUS_EFFECT, new Identifier("tutorial", "exp"), EXP);
  }
}
```

### Adding Translations & Textures

You'll need to add a translation to your status effect. Head over to
your [mod lang file](../Modding-Tutorials/Miscellaneous/lang.md) and add a new entry:

```json
{
  "effect.tutorial.exp": "Experience"
}
```

You'll need to add a texture as well. The direct path is:

    .../resources/assets/tutorial/textures/mob_effect/exp.png

### Testing

You can run a command `/effect give @p tutorial:exp` in game to test
your status effect.

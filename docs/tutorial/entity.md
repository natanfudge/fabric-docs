# Adding an Entity

### Introduction

Entities are the next step to take after adding an item and block to
your game.

To add an entity, you will need 3 primary classes:

- an Entity class, which gives your creature logic/AI
- a Renderer class, which allows you to connect your entity to a model
- a Model class, which is what your player sees in the game

We're going to be creating a cookie creeper that launches cookies
everywhere when it explodes.

### Registering an Entity

Unlike blocks & items, you basically always want a class fully dedicated
to your entity. We're making a creeper clone, so we'll make our entity
class extend CreeperEntity:

```java
public class CookieCreeperEntity extends CreeperEntity {
    [...]
}
```

Your IDE should instruct you to create a constructor matching the
super-- do that now.

To register your entity, we'll use Registry.ENTITY\_TYPE. To get the
required registry instance, you can either use EntityType.Builder or
FabricEntityTypeBuilder-- we recommend using the second one.

```java
public static final EntityType<CookieCreeperEntity> COOKIE_CREEPER =
    Registry.register(
        Registry.ENTITY_TYPE,
        new Identifier("wiki-entity", "cookie-creeper"),
        FabricEntityTypeBuilder.create(EntityCategory.AMBIENT, CookieCreeperEntity::new).size(EntityDimensions.fixed(1, 2)).build()
    );
```

The size() method allows you to set the hitbox of your entity. A creeper
is 1 block wide and 2 blocks tall, so we'll use (1, 2).

If you load up your game at this point, you will be able to use /summon
to see your creation. If all went right, it should appear as a normal
creeper. I would not recommend going into survival.

### Creating a renderer

Our Cookie creeper automatically has a model because it extended the
Creeper class. We're going to change the skin to a cookie skin instead
of the normal green camo color.

First, create a MobEntityRenderer class. MobEntityRenderer has 2 generic
types: the entity & model. Because we're using the Creeper model to
start, we'll also have to tell Creeper model this is not a Creeper
Entity by giving it a type as well.

```java
public class CookieCreeperRenderer extends MobEntityRenderer<CookieCreeperEntity, CreeperEntityModel<CookieCreeperEntity>> {
    [...]
}
```

You'll need to override the getTexture method as well as adding in the
constructor. The constructor, by default, has 3 arguments
(EntityRenderDispatcher, EntityModel, float), but we can remove the last
2 and create them ourselves:

```java
public CookieCreeperRenderer(EntityRenderDispatcher entityRenderDispatcher_1)
{
    super(entityRenderDispatcher_1, new CreeperEntityModel<>(), 1);
}
```

For the getTexture method, you need to return your model's texture. If
it is null, *your entity will be invisible*. This is a 100% guaranteed
way to spend 3 hours trying to figure out why your model is not working.
For your convenience, I have created a Cookie Creeper texture available
to all, which you can download from [here](https://imgur.com/a/o3TOlxN).

The default entity texture folder convention is:
textures/entity/entity\_name/entity.png. Here's an example
implementation:

```java
@Override
protected Identifier getTexture(CookieCreeperEntity cookieCreeperEntity)
{
    return new Identifier("wiki-entity:textures/entity/cookie_creeper/creeper.png");
}
```

with the file being stored at
resources/assets/wiki-entity/textures/entity/cookie\_creeper/creeper.png.

Finally, you'll need to connect your entity to your renderer. As
rendering only happens client-side, you should always do this kind of
work in a ClientModInitializer:

```java
EntityRendererRegistry.INSTANCE.register(CookieCreeperEntity.class, (entityRenderDispatcher, context) -> new CookieCreeperRenderer(entityRenderDispatcher));
```

This links our entity to our new renderer class. If you load into the
game, you should see our new friend:

<https://i.imgur.com/8Gfc2sV.jpg>

If you wanted to use your own model, you could create a new class that
extends EntityModel and exchange the Creeper model in our renderer for
it. This is fairly complex and will be covered in a separate tutorial.

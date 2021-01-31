## Creating an Entity

*The source code for this project can be found
[here](https://github.com/Draylar/entity-testing) on the entity branch.*

Entities are a movable object in a world with logic attached to them. A
few examples include:

- Minecarts
- Arrows
- Boats

Living Entities are Entities that have health and can deal damage. There
are various classes that branch off \`LivingEntity\` for different
purposes, including:

- `HostileEntity` for Zombies, Creepers, and Skeletons
- `AnimalEntity` for Sheep, Cows, and Pigs
- `WaterCreatureEntity` for things that swim
- `FishEntity` for fishies

What you extend depends on your needs and goals are. As you get further
down the chain, the entity logic becomes more specific and curated to
certain tasks. The two generic entity classes that come after
`LivingEntity` are:

- `MobEntity`
- `PathAwareEntity`

`MobEntity` has AI logic and movement controls. `PathAwareEntity`
provides extra capabilities for pathfinding favor, and various AI tasks
require this to operate.

In this tutorial, we will look at creating a cube entity that extends
`PathAwareEntity`. This entity will have a model & texture. Movement and
mechanics will be covered in a future tutorial.

## Creating & Registering an Entity

Create a class that extends `PathAwareEntity`. This class serves as the
brains and main hub for our custom entity.

```java
/*
 * Our Cube Entity extends PathAwareEntity, which extends MobEntity, which extends LivingEntity.
 *
 * LivingEntity has health and can deal damage.
 * MobEntity has movement controls and AI capabilities.
 * PathAwareEntity has pathfinding favor and slightly tweaked leash behavior.
 */
public class CubeEntity extends PathAwareEntity {

    public CubeEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }
}
```

You can register this entity under the `ENTITY_TYPE` registry category.
Fabric provides a `FabricEntityTypeBuilder` class, which is an extension
of the vanilla `EntityType.Builder` class. The Fabric builder class
provides extra methods for configuring your entities' tracking values.

```java
public class EntityTesting implements ModInitializer {

    /*
     * Registers our Cube Entity under the ID "entitytesting:cube".
     *
     * The entity is registered under the SpawnGroup#CREATURE category, which is what most animals and passive/neutral mobs use.
     * It has a hitbox size of .75x.75, or 12 "pixels" wide (3/4ths of a block).
     */
    public static final EntityType<CubeEntity> CUBE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("entitytesting", "cube"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CubeEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );

    @Override
    public void onInitialize() {
        
    }
}
```

Entities need *Attributes*, and a *Renderer* to function.

## Registering Entity Attributes

**Attributes** define the properties of the mob: how much health does it
have? How much damage does it do? Does it have any default armor points?

Most vanilla entities have a static method that returns their attributes
(such as `ZombieEntity#createZombieAttributes`). Our custom entity
doesn't have any unique properties, for now, so we can use
`MobEntity#createMobAttributes`.

Vanilla has a `DefaultAttributeRegistry` class for registering these
properties. It isn't publicly exposed or easily available, so Fabric
provides a `FabricDefaultAttributeRegistry` class. The registration of
default attributes should occur somewhere in your mod's initialization
phase:

```java
public class EntityTesting implements ModInitializer {
    
    public static final EntityType<CubeEntity> CUBE = [...];

    @Override
    public void onInitialize() {
        /*
      * Register our Cube Entity's default attributes.
      * Attributes are properties or stats of the mobs, including things like attack damage and health.
      * The game will crash if the entity doesn't have the proper attributes registered in time.
      *
      * In 1.15, this was done by a method override inside the entity class.
      * Most vanilla entities have a static method (eg. ZombieEntity#createZombieAttributes) for initializing their attributes.
      */
        FabricDefaultAttributeRegistry.register(CUBE, CubeEntity.createMobAttributes());
    }
}
```

## Registering Entity Renderer

The last requirement of an entity is a **Renderer**. Renderers define
\*what\* the entity looks like, generally by providing a model.
`MobEntityRenderer` is the best choice for MobEntities. The class has
one required method override for providing a texture, and wants 3
parameters for the super constructor:

- `EntityRenderDispatcher` instance
- `Model` of our entity
- shadow size of our entity as a `float`

The following code showcases a simple entity renderer with a shadow size
of 0.5f and texture at
`resources/assets/entitytesting/textures/entity/cube/cube.png`. Note
that the texture and model class will be created in the next step.

```java
/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class CubeEntityRenderer extends MobEntityRenderer<CubeEntity, CubeEntityModel> {

    public CubeEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new CubeEntityModel(), 0.5f);
    }

    @Override
    public Identifier getTexture(CubeEntity entity) {
        return new Identifier("entitytesting", "textures/entity/cube/cube.png");
    }
}
```

To register this renderer, use `EntityRendererRegistry` in a **client
initializer**:

```java
@Environment(EnvType.CLIENT)
public class EntityTestingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        /*
      * Registers our Cube Entity's renderer, which provides a model and texture for the entity.
      *
      * Entity Renderers can also manipulate the model before it renders based on entity context (EndermanEntityRenderer#render).
      */
        EntityRendererRegistry.INSTANCE.register(EntityTesting.CUBE, (dispatcher, context) -> {
            return new CubeEntityRenderer(dispatcher);
        });
    }
}
```

## Creating a Model and Texture

The final step to finishing our entity is creating a model and texture.
Models define the *structure* of the entity, while the texture provides
the color.

Standard models define "parts", or `ModelPart` instances at the top of
the class, initialize them in the constructor, and render them in the
`render` method. Note that `setAngles` and `render` are both required
overrides of the `EntityModel` class.

`ModelPart` has several constructors, with the most simple one taking
in:

- the current model instance
- textureOffsetU as an `int`
- textureOffsetV as an `int`

Texture offsets provide the location of the model's texture within your
texture file. Our entity is a single cube, so the base `ModelPart`
texture starts at 0, 0.

```java
public class CubeEntityModel extends EntityModel<CubeEntity> {

    private final ModelPart base;

    public CubeEntityModel() {
        base = new ModelPart(this, 0, 0);
    }
    
    [...]
}
```

After creating a part, we need to add a shape to it. Note that the
origin of a model starts at the corner, so you will need to offset the
part to center it:

```java
public class CubeEntityModel extends EntityModel<CubeEntity> {

    private final ModelPart base;

    public CubeEntityModel() {
        base = new ModelPart(this, 0, 0);
        base.addCuboid(-6, -6, -6, 12, 12, 12);
    }
    
    [...]
}
```

Our entity model now has a single cube that is 12x12x12 wide (75% of a
block) centered around 0, 0, 0. `setAngles` is used for animating the
model, but we will keep it empty for now. `render` is where we tell our
cube to show up. Note that standard vanilla models appear *higher* than
the entity hitbox, so we translate the model down to account for this.

```java
public class CubeEntityModel extends EntityModel<CubeEntity> {
    
    private final ModelPart base;
    
    public CubeEntityModel() [...]

    @Override
    public void setAngles(CubeEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        // translate model down
        matrices.translate(0, 1.125, 0);

        // render cube
        base.render(matrices, vertices, light, overlay);
    }
}
```

To complete our model, we need to add a texture file. The default
texture size is 64 wide and 32 tall; you can change this by changing
`textureWidth` and `textureHeight` in your model constructor. We will
set it to 64x64 for our texture:

![](https://i.imgur.com/JdF9zjf.png)

```java
public class CubeEntityModel extends EntityModel<CubeEntity> {

    private final ModelPart base;

    public CubeEntityModel() {
        this.textureHeight = 64;
        this.textureWidth = 64;
   
        [...]
    }

    [...]
} 
```

## Spawning your Entity

You can spawn your entity by typing `/summon entitytesting:cube`
in-game. Press f3+b to view hitboxes:
![](https://i.imgur.com/MmQvluB.png)

**NOTE:** If your entity does not extend `LivingEntity` you have to
create your own spawn packet handler. Either do this through the
networking API, or mixin to `ClientPlayNetworkHandler#onEntitySpawn`

## Adding tasks & activities

To add activities see [here](../villager_activities.md).

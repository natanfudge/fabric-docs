# Pixel raycast

Suppose you want to know what block or entity corresponds to a pixel on
the screen. This can be done with pixel raycast.

All of this is client side.

There are two cases, center pixel (crosshair) and arbitrary pixel.

## Special case: Center pixel

This can be done with:

```java
MinecraftClient client = MinecraftClient.getInstance();
HitResult hit = client.crosshairTarget;

switch(hit.getType()) {
    case Type.MISS:
        //nothing near enough
        break; 
    case Type.BLOCK:
        BlockHitResult blockHit = (BlockHitResult) hit;
        BlockPos blockPos = blockHit.getBlockPos();
        BlockState blockState = client.world.getBlockState(blockPos);
    Block block = blockState.getBlock();
        break; 
    case Type.ENTITY:
        EntityHitResult entityHit = (EntityHitResult) hit;
        Entity entity = entityHit.getEntity();
        break; 
}
```

### For arbitrary reach

The code above allows for the normal reach, 3 blocks for survival and
4.5 in creative. If you want the raycast to reach farther you need to
use the general case below.

## General case: Arbitrary pixel

Example [here](https://github.com/EmmanuelMess/BoundingBoxMinecraftMod).

For this one we need to precalculate a few things:

```java
MinecraftClient client = MinecraftClient.getInstance();
int width = client.getWindow().getScaledWidth();
int height = client.getWindow().getScaledHeight();
Vec3d cameraDirection = client.cameraEntity.getRotationVec(tickDelta);
double fov = client.options.fov;
double angleSize = fov/height;
Vector3f verticalRotationAxis = new Vector3f(cameraDirection);
verticalRotationAxis.cross(Vector3f.POSITIVE_Y);
if(!verticalRotationAxis.normalize()) {
    return;//The camera is pointing directly up or down, you'll have to fix this one
}

Vector3f horizontalRotationAxis = new Vector3f(cameraDirection);
horizontalRotationAxis.cross(verticalRotationAxis);
horizontalRotationAxis.normalize();

verticalRotationAxis = new Vector3f(cameraDirection);
verticalRotationAxis.cross(horizontalRotationAxis);
```

Once you have that, you can use this function to calculate the direction
vector that corresponds to the pixel:

```java
private static Vec3d map(float anglePerPixel, Vec3d center, Vector3f horizontalRotationAxis,
    Vector3f verticalRotationAxis, int x, int y, int width, int height) {
    float horizontalRotation = (x - width/2f) * anglePerPixel;
    float verticalRotation = (y - height/2f) * anglePerPixel;
    
    final Vector3f temp2 = new Vector3f(center);
    temp2.rotate(verticalRotationAxis.getDegreesQuaternion(verticalRotation));
    temp2.rotate(horizontalRotationAxis.getDegreesQuaternion(horizontalRotation));
    return new Vec3d(temp2);
}
```

Then you have this reimplementation of the code at
GameRenderer\#updateTargetedEntity:

```java
private static HitResult raycastInDirection(MinecraftClient client, float tickDelta, Vec3d direction) {
    Entity entity = client.getCameraEntity();
    if (entity == null || client.world == null) {
        return null;
    }

    double reachDistance = client.interactionManager.getReachDistance();//Change this to extend the reach
    HitResult target = raycast(entity, reachDistance, tickDelta, false, direction);
    boolean tooFar = false;
    double extendedReach = reachDistance;
    if (client.interactionManager.hasExtendedReach()) {
        extendedReach = 6.0D;//Change this to extend the reach
        reachDistance = extendedReach;
    } else {
        if (reachDistance > 3.0D) {
            tooFar = true;
        }
    }

    Vec3d cameraPos = entity.getCameraPosVec(tickDelta);

    extendedReach = extendedReach * extendedReach;
    if (target != null) {
        extendedReach = target.getPos().squaredDistanceTo(cameraPos);
    }

    Vec3d vec3d3 = cameraPos.add(direction.multiply(reachDistance));
    Box box = entity
            .getBoundingBox()
            .stretch(entity.getRotationVec(1.0F).multiply(reachDistance))
            .expand(1.0D, 1.0D, 1.0D);
    EntityHitResult entityHitResult = ProjectileUtil.raycast(
            entity,
            cameraPos,
            vec3d3,
            box,
            (entityx) -> !entityx.isSpectator() && entityx.collides(),
            extendedReach
    );

    if (entityHitResult == null) {
        return target;
    }

    Entity entity2 = entityHitResult.getEntity();
    Vec3d vec3d4 = entityHitResult.getPos();
    double g = cameraPos.squaredDistanceTo(vec3d4);
    if (tooFar && g > 9.0D) {
        return null;
    } else if (g < extendedReach || target == null) {
        target = entityHitResult;
        if (entity2 instanceof LivingEntity || entity2 instanceof ItemFrameEntity) {
            client.targetedEntity = entity2;
        }
    }

    return target;
}

private static HitResult raycast(
        Entity entity,
        double maxDistance,
        float tickDelta,
        boolean includeFluids,
        Vec3d direction
) {
    Vec3d end = entity.getCameraPosVec(tickDelta).add(direction.multiply(maxDistance));
    return entity.world.raycast(new RaycastContext(
            entity.getCameraPosVec(tickDelta),
            end,
            RaycastContext.ShapeType.OUTLINE,
            includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE,
            entity
    ));
}
```

Once you have the direction and the raycaster, you can put them
together:

```java
Vec3d direction = map(
        (float) angleSize,
        cameraDirection,
        horizontalRotationAxis,
        verticalRotationAxis,
        x,
        y,
        width,
        height
);
HitResult hit = raycastInDirection(client, tickDelta, direction);

switch(hit.getType()) {
    case Type.MISS:
        //nothing near enough
        break; 
    case Type.BLOCK:
        BlockHitResult blockHit = (BlockHitResult) hit;
        BlockPos blockPos = blockHit.getBlockPos();
        BlockState blockState = client.world.getBlockState(blockPos);
    Block block = blockState.getBlock();
        break; 
    case Type.ENTITY:
        EntityHitResult entityHit = (EntityHitResult) hit;
        Entity entity = entityHit.getEntity();
        break; 
}
```

Here x and y are your pixel coordinates.

### Performance considerations

This is EXPENSIVE, if you do it too many times, it WILL get slow.
Especially for long reaches. If you \*need\* to do many raycasts in a
single frame, [this](https://stackoverflow.com/q/777997/3124150) link
might be helpful.

# Rendering Blocks and Items Dynamically

There are two main ways to render blocks and items dynamically,
depending on what you want to do.

- [Custom Models](https://fabricmc.net/wiki/tutorial:custom_model) can
  be used to dynamically render the block when the chunk mesh is
  rebuilt. This means that the model will be re-generated only when
  the chunk is updated, or when you force it to update.

<!-- --->- [Block Entity Renderers](https://fabricmc.net/wiki/tutorial:blockentityrenderers)
  are called every frame to draw whatever you want, but the model will
  still be rendered. This can be used on top of the Custom Models for
  animation, e.g. a chest opening animation.


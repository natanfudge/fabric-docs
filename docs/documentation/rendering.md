# Rendering in Fabric (DRAFT)

**IMPORTANT:** This documentation describes upcoming features in the
process of being added to Fabric. These feature are not yet available
and subject to change. The documentation is being posted now to help the
developer community evaluate and improve the proposed features before
adoption.

## Introduction

### The Fabric Rendering API

The Rendering API specifies interfaces and creates hooks for the
implementation of a `Renderer`. An implementation will provide the
following features:

- Enhanced block model rendering: emissive lighting, control over
  diffuse and ambient occlusions lighting, and multiple blend modes
  (solid, cutout, translucent) in the same model.
- Dynamic block models: Some or all of a block model can be generated
  or modified during chunk rebuild based on world state, with or
  without a BlockEntity.
- Enhanced item model rendering: Item models have similar options for
  enhanced appearance and model output can be dynamic based on
  ItemStack state.

The API is flexible so that `Renderer` implementations can introduce
novel lighting, special effects and performance optimization with
excellent compatibility for models that depend on the API. Some renderer
implementations may focus on aesthetics and some may focus on
performance, and others may try to balance both.

This freedom is achieved through two key design decisions:

First, Fabric delegates most of the functionality to the `Renderer`
implementation itself - Fabric includes very few patches as part of the
API.

Second, the API specification hides vertex formats, vertex data
structures, and other implementation details from models using the API -
models are not expected to provide or manipulate raw vertex data.
Instead, the API defines lightweight interfaces for obtaining materials
and building/outputting model content. Mod authors who use these
interfaces can be assured their content will render well across a
diverse range of implementation approaches.

### Audience

##### Mod Developers

Many mod authors will use a 3rd-party library to create or load models.
However, many features of the API are likely to be useful or even
necessary when using a library. If this describes you, this wiki should
be a useful reference, but you don't need to read and understand every
part of it.

##### Model Loader Developers

The Fabric Rendering API was designed to be a suitable back-end for
practically any type of model loader but does not specify or implement
any model formats. Creating a model loader is a great way to contribute
to Fabric development\! If you want to create a model loader, you should
review the entire API and all of the sections below.

##### Model Library Developers

The Rendering API provides only the most basic primitives for model
creation. Creating a library for procedural model generation and
transformation is another good way to contribute to Fabric development.
If you want to create a model library, you'll need to review the entire
API and all of the sections below.

**Caution:** If your library will be used to generate models a run time
(after Minecraft is loaded) then you should also have a good working
understanding of the potential negative impacts on game performance and
be familiar with techniques to avoid them.

##### Renderer Developers

The Rendering API is only as good as the available implementations.
Creating and maintaining a new Renderer is likely to be a large and
complex effort. Before you begin, consider the following questions:

- What are you trying to achieve? Will your renderer be very fast?
  Will it try to conserve memory? Will it offer novel features?
- Your renderer should support all the features defined in the core
  API. Do you understand the entire Rendering API and how it is used?
- Are there any API extensions you intend to support or introduce?
- Do you know how to write and debug Mixins?
- How will your renderer modify the Minecraft rendering pipeline?
  Where will you need to make patches?
- How will your approach avoid excessive memory allocation?
- How will you ensure thread-safety?

Still want to create a renderer? This wiki and the links below will help
you get started. And please post in Discord so we know to include your
implementation in the links section\!

### Getting Started

You'll need to include the Fabric API in your development environment -
the Rendering API is part of it. See [Setting up a mod development
environment](../tutorial/setup.md) for step-by-step instructions.

Fabric is distributed with a default `Renderer` implementation providing
most of the features defined in the API. (It doesn't handle emissive
item rendering,). The default renderer offers excellent compatibility,
vanilla aesthetics and good performance for most use cases.

If the features provided in the core API are sufficient, the default
renderer is all you need. Players or pack makers may render your content
with other render implementations to get better performance or a
different look (ie. shader packs).

Only one Renderer implementation can be active in a game session. When a
non-default `Renderer` implementation mod is present, the default
implementation will deactivate itself automatically. If two
(non-default) implementations are present, Minecraft will crash during
startup when the second implementation tries to register itself.

Some renderer implementations may offer an expanded feature set. If you
create mods with a hard dependency on those features, be sure to state
that clearly for anyone using your mod. In that case, your audience will
be limited to players and pack makers who use renderers supporting those
expanded features.

#### Getting the Renderer Instance

To use the features of this API, you'll need a reference to the
`Renderer` instance. Getting the instance is easy:

```java
RendererAccess.INSTANCE.getRenderer()
```

It is safe to retain a reference to the `Renderer` instance.

The renderer instance will be reliably non-null unless Fabric was
somehow distributed without the default renderer or with the default
renderer disabled. (This would not be normal.) If your mod depends on
features defined in the API, then your mod should simply crash when no
`Renderer` is active.

## Materials

Every quad sent through the rending API has an associated
`RenderMaterial`. Materials give you control over texture blending and
lighting. In future API extensions, materials will be the attachment
point for other behaviors. Many of the effects you will want to achieve
can be accomplished by material selection.

### Obtaining Materials

Use `Renderer.materialFinder()` to get a `MaterialFinder` reference.
It's safe to retain the resulting reference. Use the finder to specify
the properties you want for your material and then use
MaterialFinder.find() to get a `RenderMaterial` instance that will
communicate those choices to the renderer.

**Tip:** Calling `find()` doesn't change any attributes of the finder.
This behavior is useful when you want several materials with similar
properties. If instead you want very different materials, call
`MaterialFinder.clear()` to reset the finder instance to default values
before specifying your next material.

Material instances are immutable and opaque once retrieved. Materials
with identical properties may or *may not* pass == and .equals() tests.
They exist only to efficiently communicate material properties to the
renderer - they *aren't* meant to be analyzed by models. This affords
renderer implementations maximum flexibility for material handling.

### Named Materials

Materials can be registered with a name-spaced identifier using
`Renderer.registerMaterial()` and retrieved using
`Renderer.materialById()`. This can be useful if you publish a mod that
allows third-party extensions and you want to give those extensions a
supported way to use consistent materials. In that case, your mod should
register materials with identifiers that are visible to extensions.

Named materials can also be used by `Renderer` implementations to expose
custom materials that provide special effects not possible with the
standard materials. Renderers that provide custom materials are
responsible for declaring and exposing the identifiers used to retrieve
them.

Named materials will not be present unless a mod/renderer that registers
them is also present. If your mod relies on named materials, you should
either make your mod explicitly depend on the providing
implementation(s) or check for the presence of the material at run time
and use a fallback material when the named material is not available.

### Material Properties

When selecting a material, you can choose from materials with the
following attributes:

#### Blend Mode

- Allows the effect of multiple render layers in the same block/model.
- Set via `MaterialFinder.blendMode()`
- Accepts `BlockRenderLayer` enum values.
- Does NOT necessarily mean the material actually renders in the given
  pass - only that it will appear to do so. (Some renderers may
  combine passes or do other optimizations. Don't write MixIns that
  are tied to `BlockRenderLayer` passes.)
- If null (the default) then terrain renders will use the value of
  `Block.getRenderLayer()`. Other render contexts will use
  `BlockRenderLayer.TRANSLUCENT` as the default.

#### Diffuse Shading On/Off

- Controls color modification for diffuse lighting.
- On by default.
- Disable via `MaterialFinder.disableDiffuse()`.
- In vanilla Minecraft this causes sides and bottoms of blocks to be
  darker, creating visual distinction.
- Renderer implementations may use a different diffuse lighting model
  but should still honor this setting.

#### Ambient Occlusion Shading On/Off

- Controls color modification for ambient occlusion lighting.
- On by default.
- Disable via `MaterialFinder.disableAo()`.
- In vanilla Minecraft this causes interior corners to be darker,
  creating visual distinction.
- Renderer implementations may use a different AO lighting model but
  should still honor this setting.

#### Emissive Rendering On/Off

- When enabled, causes quad to be rendered at full brightness.
- Disabled by default.
- Enable via `MaterialFinder.emissive()`
- Does not require a custom per-vertex light map and it is recommended
  you don't provide one.
- Diffuse and Ambient Occlusion shading *will* apply unless also
  disabled.

#### Sprite Depth

- Default value is 1 and 1 is the only value currently supported in
  the API.
- Values \> 1 are reserved for future use and for use by extensions.
- Extensions can use this to accept multiple sprites on the same quad
  to create overlay effects.

#### Color Index On/Off

- Controls application of block color index to vertex colors.
- If block color index \!= -1 it will be applied by default. (Set
  using `MutableQuadView.colorIndex()`).
- Use `MaterialFinder.disableColorIndex()` to prevent color
  application.
- Generally only useful when an extension is active the supports a
  material sprite depth \> 1, In that case it allows block color to
  apply only to specific sprite layers.

## Meshes

## Render Contexts

## Dynamic Rendering

## How To

- Static Models
- Compound Models
- Dynamic Models
- Emissive Rendering
- Transformation
- Selective Visibility
- Mirroring
- Wrapping
- Coloring
- Item Models
- Damage Models
- Moving Blocks

## Implementation Advice

Rendertime Mesh Generation

Necessary Hooks

Optimization Opportunities

- Chunk Builds
- Low Allocation
- Compact Formats
- Hardware Transforms

## Future Direction

- Batched BlockEntity Rendering
- Shaders
- Custom Materials
- Sprite Overlay Layers
- Fancy Lighting Models

## Links

Implementations

- Indigo
- Canvas

Extensions

- FREx

Sample Usage

- RenderBender

Model Libraries

- Brocade

Model Loaders

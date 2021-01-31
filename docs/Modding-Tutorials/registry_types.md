# Standard Registries

Minecraft has registries for many kinds of objects, such as blocks,
items or entities. These are the registries in vanilla:

## General registries

These registries are useful for many types of mods.

- `Registry.REGISTRIES`
  - Contains all the registries themselves.
- `Registry.BLOCK`
  - Contains all [blocks](../Modding-Tutorials/Blocks-and-Block-Entities/block.md).
- `Registry.ITEM`
  - Contains all [items](../Modding-Tutorials/Items/item.md) (including block items).
- `Registry.BLOCK_ENTITY_TYPE`
  - Contains the `BlockEntityType` of each [block   entity](../Modding-Tutorials/Blocks-and-Block-Entities/blockentity.md). Block entity types are used to
    deserialize block entities and store compatible blocks.
- `Registry.STATUS_EFFECT`
  - Contains all [status   effects](https://minecraft.gamepedia.com/Status_effect), like
    invisibility or night vision.
- `Registry.PARTICLE_TYPE`
  - Contains all [particle   types](https://minecraft.gamepedia.com/Particles).
- `Registry.FLUID`
  - Contains all [fluids](../Modding-Tutorials/Fluids/fluid.md).
- `Registry.ENCHANTMENT`
  - Contains all enchantments.
- `Registry.POTION`
  - Contains all different potion types, such as "long potion of
    night vision", "water" or "potion of luck".
- `Registry.DIMENSION_TYPE`
  - Contains all dimension types.
- `Registry.SCREEN_HANDLER`
  - Contains all screen handlers. Screen handlers are used to
    synchronize GUI state between the server and the client.
- `Registry.RECIPE_TYPE`
  - Contains all [recipe](https://minecraft.gamepedia.com/Recipe)
    types.
- `Registry.RECIPE_SERIALIZER`
  - Contains all recipe serializers. Recipe serializers are used to
    load recipes, and there can be many serializers for a single
    type (such as both shaped and shapeless crafting recipes).
- `Registry.PAINTING_MOTIVE`
  - Contains all painting motives, or the different types of
    paintings.
- `Registry.SOUND_EVENT`
  - Contains all different [sound events](../Modding-Tutorials/Miscellaneous/sounds.md) like
    `entity.item.pickup`.
- `Registry.STAT`
  - Contains all different stat types. Stats use a generic type,
    `<T>`, to get different data for different `T` values.
  - Stats that don't depend on outside objects (eg. `walk_one_cm`)
    use the `custom` stat type, which uses registered `Identifier`s
    as the `T` type.
  - Stats that aren't `custom` stats might depend on a specific
    block or item type, like `Stats.MINED`.
- `Registry.CUSTOM_STAT`
  - Contains all "custom stats", which are identifiers for stats
    that don't depend on outside objects.

## Entities

Most of these registries, apart from `ENTITY_TYPE` itself, are related
to entity AI.

- `Registry.ENTITY_TYPE`
  - Contains the `EntityType` of each
    [entity](../Modding-Tutorials/entity-old.md). Like block entity types, entity
    types are used for syncing and deserialization.
- `Registry.SCHEDULE`
  - Contains schedules for entities (usually villagers). Schedules
    control their activities based on the time of day.
- `Registry.ACTIVITY`
  - Controls activities for entities (usually villagers).
    Activities, such as `play` or `work`, control the behavior of
    entities.
- `Registry.SENSOR_TYPE`
  - Contains the type of each entity sensor. Sensors allow entities
    to "sense" different things and store them in their memory.
  - Like schedules and activities, this is mostly used by villagers.
- `Registry.MEMORY_MODULE_TYPE`
  - Contains all types of memory modules. Memory module types
    describe different things entities can remember.
- `Registry.VILLAGER_TYPE`
  - Contains all villager biome types.
- `Registry.VILLAGER_PROFESSION`
  - Contains all villager professions.
- `Registry.POINT_OF_INTEREST_TYPE`
  - Contains all types of points of interest. Points of interest
    allow entities to search for different blocks in the world, such
    as villager job sites. They are also used to locate nether
    portals.

## World generation

World generation in Minecraft is complex, and there are a lot of
different registries for world gen.

- `Registry.BIOME`
  - Contains all [biomes](../Modding-Tutorials/biomes.md).
- `Registry.FEATURE`
  - Contains all [world generation features](../Modding-Tutorials/World-Generation/features.md),
    including structures.
- `Registry.STRUCTURE_FEATURE`
  - Contains all [structure features](../Modding-Tutorials/World-Generation/structures.md).
- `Registry.CARVER`
  - Contains all carvers. Carvers are used to create caves and
    ravines.
- `Registry.DECORATOR`
  - Contains all decorators. Decorators are used to place features.
- `Registry.BIOME_SOURCE_TYPE`
  - Contains all biome source types. They determine which biome is
    placed where during world generation.
- `Registry.TREE_DECORATOR_TYPE`
  - Contains all tree decorator types. Tree decorators can add
    additional blocks, such as beehives or vines, to trees.
- `Registry.FOLIAGE_PLACER_TYPE`
  - Contains all foliage placer types. Foliage placers are used to
    generate the leaves of trees.
- `Registry.BLOCK_STATE_PROVIDER_TYPE`
  - Contains all block state provider types. They are used to select
    a possibly random block state based on a position.
- `Registry.BLOCK_PLACER_TYPE`
  - Contains all block placer types. Block placers are used to place
    blocks in features like `RandomPatchFeature`.
- `Registry.CHUNK_GENERATOR_TYPE`
  - Contains all chunk generator types. Chunk generators define the
    basic shape of the terrain, such as `surface` for
    overworld-like, `caves` for nether-like and `floating_islands`
    for end-like terrain.
- `Registry.CHUNK_STATUS`
  - Contains all chunk status types. They describe the current
    progress of world generation in a chunk.
- `Registry.STRUCTURE_PIECE`
  - Contains all structure piece types. Structure pieces are smaller
    parts of a structure, such as a single room.
- `Registry.RULE_TEST`
  - Contains all rule test types. They are used to match blocks in
    structure generation.
- `Registry.STRUCTURE_PROCESSOR`
  - Contains all structure processor types. Structure processors
    modify structures after they have been generated.
- `Registry.STRUCTURE_POOL_ELEMENT`
  - Contains all structure pool elements. Structure pool elements
    are even smaller parts of structures, and they are contained
    within pool structure pieces. They are used for generating
    structures that are based on datapacks (such as structures using
    jigsaws).
- `Registry.SURFACE_BUILDER`
  - Contains all surface builders. Surface builders are used to
    place the surface blocks of a biome, such as grass in many
    overworld biomes.


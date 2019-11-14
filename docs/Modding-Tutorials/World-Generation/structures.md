# Generating Structures

## Features

All code used in this tutorial is available here:
[fabric-structure-example-repo](https://github.com/Draylar/fabric-structure-example-repo)

### Introduction

Weâ€™re going to look at registering and placing structures in your world.

To view examples of 1.14 vanilla structures in action, IglooGenerator &
IglooFeature are a good place to start.

You are going to need a Feature and Generator for the most basic
structure. The feature handles the process of registering the structure
and loading it in when the world is generating-- it answers questions
such as â€˜should I spawn here?â€™ and â€˜what is my name?â€™ The generator
handles the placement of blocks or loading in a structure file if you
choose to do so.

### Creating a Feature

To create a basic feature, we recommend creating a class that extends
AbstractTempleFeature\<DefaultFeatureConfig\>. Various vanilla
structures, such as Shipwrecks, Igloos, and Temples, use
AbstractTempleFeature as a base. You will have to override the following
methods:

- shouldStartAt: return true for testing purposes.
- getName: name of your structure
- getRadius: radius of your structure, used for placement
- getSeeedModifier

You can pass DefaultFeatureConfig::deserialize into your constructor for
testing.

For getStructureStartFactory, most vanilla structures make a class that
extends StructureStart inside their Feature class:

```java
public static class MyStructureStart extends StructureStart {
    public MyStructureStart (StructureFeature<?> structureFeature_1, int int_1, int int_2, Biome biome_1, MutableIntBoundingBox mutableIntBoundingBox_1, int int_3, long long_1) {
        super(structureFeature_1, int_1, int_2, biome_1, mutableIntBoundingBox_1, int_3, long_1);
    }
    @Override
    public void initialize(ChunkGenerator<?> chunkGenerator, StructureManager structureManager, int chunkX, int chunkZ, Biome biome) {
        DefaultFeatureConfig defaultFeatureConfig = chunkGenerator.getStructureConfig(biome, MyMainclass.myFeature);
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos startingPos = new BlockPos(x, 0, z);
        Rotation rotation = Rotation.values()[this.random.nextInt(Rotation.values().length)];
        MyGenerator.addParts(structureManager, startingPos, rotation, this.children, this.random, defaultFeatureConfig);
        this.setBoundingBoxFromChildren();
    }
}
```

This is called when the world attempts to spawn in a new structure, and
is the gap between your Feature and Generator. The reference to the
variable in your main class doesn't exist yet, but we'll create it at
the end. You can also just set the config equal to a new
DefaultFeatureConfig. You can return this in getStructureStartFactory
with return MyStructureStart::new.

This is where structure files and generating straight from a generate
method part ways. There are two ways to go about this:

- If you want, you can simply override generate in your Feature class
  and use setBlockState to place blocks directly in the world. This is
  a valid option and was popular pre-1.13.
- Use structure files and a Generator. These are rather powerful at
  this point and are highly recommended.

### Creating a Generator

As you have probably noticed, we need to create a generator. We'll name
it MyGenerator, and it's referenced in the initialize method of your
StructureStart class. It doesn't need to override anything, but does
require the following:

- An Identifier that points to your structure file; use "igloo/top" if
  you need an example.
- Some sort of setup method - addParts is a good name:

<!-- end list --->

```java
public static void addParts(StructureManager structureManager_1, BlockPos blockPos_1, Rotation rotation_1, 
    List<StructurePiece> list_1, Random random_1, DefaultFeatureConfig featureConfig)
    
}
```

In your addParts method, you can choose which structure pieces are added
to your generation process. You can add a piece like this:

```java
list_1.add(new MyGenerator.Piece(structureManager_1, identifier, blockPos, rotation_1));
```

where the identifier is the path we created recently.

We're now going to create the Piece we just referenced; make a class
called Piece that extends SimpleStructurePiece *within your generator
class*.

Override required methods, and add a constructor that takes in a
StructureManager, Identifier, BlockPos, and Rotation. **toNbt isn't
required but is available if you need it**. We're also implementing our
own setStructureData with different arguments, so it's not an override.
We also have 2 constructors: 1 for our own pieces, and one for registry.
A basic template would be:

```java
public static class Piece extends SimpleStructurePiece {
    private Rotation rotation;
    private Identifier template;
    
    public Piece(StructureManager structureManager_1, Identifier identifier_1, BlockPos blockPos_1, Rotation rotation_1) {
        super(MyModClass.myStructurePieceType, 0);
        
        this.pos = blockPos_1;
        this.rotation = rotation_1;
        this.template = identifier_1;
        
        this.setStructureData(structureManager_1);
    }
    
    public Piece(StructureManager structureManager_1, CompoundTag compoundTag_1) {
        super(MyModClass.myStructurePieceType, compoundTag_1);
        this.identifier = new Identifier(compoundTag_1.getString("Template"));
        this.rotation = Rotation.valueOf(compoundTag_1.getString("Rot"));
        this.setStructureData(structureManager_1);
    }
    
    @Override
    protected void toNbt(CompoundTag compoundTag_1) {
        super.toNbt(compoundTag_1);
        compoundTag_1.putString("Template", this.template.toString());
        compoundTag_1.putString("Rot", this.rotation.name());
    }
    
    public void setStructureData(StructureManager structureManager) {
        Structure structure_1 = structureManager.getStructureOrBlank(this.identifier);
        StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setRotation(this.rotation).setMirrored(Mirror.NONE).setPosition(pos).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        this.setStructureData(structure_1, this.pos, structurePlacementData_1);
    }
    
    @Override
    protected void handleMetadata(String string_1, BlockPos blockPos_1, IWorld iWorld_1, Random random_1, MutableIntBoundingBox mutableIntBoundingBox_1) {
        
    }
    
    @Override
    public boolean generate(IWorld iWorld_1, Random random_1, MutableIntBoundingBox mutableIntBoundingBox_1, ChunkPos chunkPos_1) {
      
    }
}
```

handleMetadata is where you look at data blocks within your structure
and do tasks based on what you find. In vanilla structures, data blocks
are placed above chests so they can be filled with loot in this method.

We set the StructurePieceType to MyModClass.myStructurePiece type; this
is the variable that holds your registered structure piece. We'll handle
that after we finish the generate function, which sets the position of
your structure and generates it:

```java
@Override
public boolean generate(IWorld iWorld_1, Random random_1, MutableIntBoundingBox mutableIntBoundingBox_1, ChunkPos chunkPos_1) {
    int yHeight = iWorld_1.getTop(Heightmap.Type.WORLD_SURFACE_WG, this.pos.getX() + 8, this.pos.getZ() + 8);
    this.pos = this.pos.add(0, yHeight - 1, 0);
    return super.generate(iWorld_1, random_1, mutableIntBoundingBox_1, chunkPos_1);
}
```

In this case, we simply get the y position of the highest block in the
middle of our chunk and generate the structure off that position.

### Registering Features

The last step is to register our features. We're going to need to
register:

- StructurePieceType
- StructureFeature\<DefaultFeatureConfig\>
- StructureFeature\<?\>

We're also going to need to add the structure to the STRUCTURES list and
add it to each biome as a feature and generation step.

Registering piece type:

```java
public static final StructurePieceType myStructurePieceType = Registry.register(Registry.STRUCTURE_PIECE, "my_piece", MyGenerator.Piece::new);
```

Registering feature:

```java
public static final StructureFeature<DefaultFeatureConfig> myFeature = Registry.register(Registry.FEATURE, "my_feature", new MyFeature());
```

Registering structure:

```java
public static final StructureFeature<?> myStructure = Registry.register(Registry.STRUCTURE_FEATURE, "my_structure", myFeature);
```

To put your feature in the features list, you can use:

```java
Feature.STRUCTURES.put("My Awesome Feature", myFeature);
```

For testing, it's a good idea to register your feature to every biome
and set the spawn rate to 100% so you can be sure it's spawning and
working. You probably don't want your structure floating in the water,
so we'll also filter that out. Add it to every biome by iterating over
the biome list and adding it as a feature and generation step:

```java
for(Biome biome : Registry.BIOME) {
    if(biome.getCategory() != Biome.Category.OCEAN && biome.getCategory() != Biome.Category.RIVER) {
        biome.addStructureFeature(myFeature, new DefaultFeatureConfig());
        biome.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Biome.configureFeature(myFeature, new DefaultFeatureConfig(), Decorator.CHANCE_PASSTHROUGH, new ChanceDecoratorConfig(0)));
    }
}
```

ChanceDecoratorConfig's argument is basically how many chunks it will
skip over before generating. 0 is every chunk, 1 is every other, and 100
is every 100.

You need to add your structure as a feature so your biome knows it
exists, and then as a generation step so it's actually generated.

Load into your world, and if all went well, you should be met with a
*lot* of Igloos.

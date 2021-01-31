# Access Wideners

Access wideners provide a way to loosen the access limits of classes,
methods or fields. Access wideners are similar to the commonly known
Access Transformers.

Access wideners should **only** be used where mixin does not currently
provide a means to do so. There are currently only 2 cases where mixins
are not sufficient:

- Needing to access a (package) private class, especially for the
  purpose of shadowing or accessing a field or method in a mixin.
- Being able to override final methods or subclass final classes.
  - Before you consider overriding final methods, try mixin
    injecting in final methods first!
  - If you want to subclass a class with only (package) private
    constructors, wideners are a good choice.

In order for access widener changes to show up in the decompiled source,
run the `genSources` gradle task.

## Requirements

- Fabric-loader 0.8.0 or higher
- Loom 0.2.7 or higher

## File format

A specific file format is used to define the access changes included in
your mod. To aid IDE's you should use the `.accesswidener` file
extension.

The file must start with the following header, `namespace` should match
the mappings namespace of your mod's source code, this is usually
`named`. Loom will remap the access widener file for you into
`intermediary` along with your mod. If you use a custom `RemapJarTask`,
set `remapAccessWidener` property on it to `true` to ensure this
happens.

```[enable_line_numbers="true"]
accessWidener   v1  <namespace>
```

Access widener files can have blank lines and comments starting with \#

```[enable_line_numbers="true"]
# Comments like this are supported, as well as at the end of the line
```

Any whitespace can be used to separate in the access widener file, tab
is recommended.

Class names are separated with a / and not .

For inner classes, you should use `$` instead of `/`

##### Classes

Class access can be changed by specifying the access and the class name
as named the mappings namespace defined in the header.

```[enable_line_numbers="true"]
<access>   class   <className>
```

1. access can be *accessible* or *extendable*

##### Methods

Method access can be changed by specifying the access, class name,
method name and method descriptor as named the mappings namespace
defined in the header.

```[enable_line_numbers="true"]
<access>   method   <className>   <methodName>   <methodDesc>
```

1. access can be *accessible* or *extendable*
2. classname is the owner class
3. methodName is the method name
4. methodDesc is the method descriptior

##### Fields

Field access can be changed by specifying the access, class name, field
name and field descriptor as named the mappings namespace defined in the
header.

```[enable_line_numbers="true"]
<access>   field   <className>   <fieldName>   <fieldDesc>
```

1. access can be *accessible* or *mutable*
2. className is the owner class
3. fieldName is the field name
4. fieldDesc is the field descriptor

## Access Changes

#### Extendable

Extendable should be used where you want to extend a class or override a
method.

- Classes are made public and final is removed
- Methods are made protected and final is removed

Making a method extendable also makes the class extendable.

#### Accessible

Accessible should be used when you want to access a class, field or
method from another class.

- Classes are made public
- Methods are made public and final if private
- Fields are made public

Making a method or field accessible also makes the class accessible.

#### Mutable

Mutable should be used when you want to mutate a final field

- Fields have final removed

## Specifying file location

The access widener file location must be specified in your build.gradle
and in your fabric.mod.json file. It should be stored in the resources
as it needs to be included in the exported jar file.

```groovy
loom {
    accessWidener "src/main/resources/modid.accesswidener"
}
```

```json
...

"accessWidener" : "modid.accesswidener",

...
```


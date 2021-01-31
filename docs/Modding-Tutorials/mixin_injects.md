# Mixin Injects

## Introduction

Injects allows you to place custom code at a specified position inside
an existing method. For a working example, view the [Practical
Example](https://fabricmc.net/wiki/tutorial:mixin_injects#practical_example)
category at the bottom of this page. The standard form of an inject is
as shown:

```java
@Inject(method = "", at = @At("INJECTION POINT REFERENCE"))
private void injectMethod(METHOD ARGS, CallbackInfo info) {

}
```

The [Injection Point
Reference](https://github.com/SpongePowered/Mixin/wiki/Injection-Point-Reference)
defines where the code inside the method body is injected inside the
target method. The following table describes a few of the options:

| Name   | Description                       |
|--------|-----------------------------------|
| HEAD   | Top of the method                 |
| RETURN | Before every return statement     |
| INVOKE | At a method call                  |
| TAIL   | Before the final return statement |

In the case of injection points that reference statements or members,
the target value can be set inside *@At*. Target value is specified
using JVM bytecode descriptors.

Oracle defines the following [field
descriptors](https://docs.oracle.com/javase/specs/jvms/se14/html/jvms-4.html#jvms-4.3.2):

| Descriptor    | Primitive | Description                                                                       |
|---------------|-----------|-----------------------------------------------------------------------------------|
| B             | byte      | signed byte                                                                       |
| C             | char      | Unicode character code point in the Basic Multilingual Plane, encoded with UTF-16 |
| D             | double    | double-precision floating-point value                                             |
| F             | float     | single-precision floating-point value                                             |
| I             | int       | integer                                                                           |
| J             | long      | long integer                                                                      |
| L*ClassName*; | reference | an instance of *ClassName*                                                        |
| S             | short     | signed short                                                                      |
| Z             | boolean   | `true` or `false`                                                                 |
| \[            | reference | one array dimension                                                               |

A method descriptor is comprised of the method name, followed by a set
of parentheses containing the input types, followed by the output type.
A method defined in Java as `Object m(int i, double[] d, Thread t)`
would have the method descriptor
`m(I[DLjava/lang/Thread;)Ljava/lang/Object;`.

*@Inject* methods always have a void return type. The method name does
not matter; using something that describes what the inject does is best.
The target method's arguments are placed first in the method's header,
followed by a `CallbackInfo` object. If the target method has a return
type (T), `CallbackInfoReturnable<T>` is used instead of `CallbackInfo`.

#### Returning & Cancelling from Inject

To cancel or return early inside a method, use `CallbackInfo#cancel` or
`CallbackInfoReturnable<T>#setReturnValue(T)`. Note that `cancel` does
not have to be called after `setReturnValue`. In both instances,
`cancellable` will have to be set to true in the inject annotation:

```java
@Inject(method = "...", at = @At("..."), cancellable = true)
```

#### Injecting into Constructors

To inject into a constructor, use `<init>()V` as the method target, with
`()` containing the constructor argument descriptors. When injecting
into constructors, `@At` must be set to either `TAIL` or `RETURN`. No
other forms of injection are officially supported. Note that some
classes have methods named `init` which are different from `<init>`.
Don't get confused!

To inject into a static constructor, use `<clinit>` as the method name.

## Practical Example

The following example injects a print statement at the top of
`TitleScreen#init` (note: the method `init` is a normal method and not a
constructor).

```java
@Mixin(TitleScreen.class)
public class ExampleMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        System.out.println("This line is printed by an example mod mixin!");
    }
}
```

For more information on this particular example, view its usage in the
[Fabric Example Mod
repo](https://github.com/FabricMC/fabric-example-mod/blob/master/src/main/java/net/fabricmc/example/mixin/ExampleMixin.java).

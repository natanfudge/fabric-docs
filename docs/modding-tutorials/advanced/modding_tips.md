# Modding Tips

Here's a collection of assorted Fabric-related modding tips gleaned from experiences on advising users of the API.

## Basics \(API\)

* Due to the injection-based approach of Fabric's API development, we

  don't tend to patch classes outright in a way visible to the end

  user. As such, you may occasionally find Fabric extensions of

  vanilla classes when you run into something you cannot do. For

  example:

  * Block.Settings -&gt; FabricBlockSettings
  * EntityType.Builder -&gt; FabricEntityTypeBuilder

* While an official configuration system is being worked on, one

  replacement for now is to use Java .properties or JSON.

* For a built-in resource pack or data pack, please ensure that an

  "assets/\[mod id\]" or "data/\[mod id\]" directory path is present,

  respectively! IDEA users might find themselves accidentally

  creating an "assets.\[mod id\]" directory - this won't work.

## Mixins

* To cast a class to an interface it doesn't implement, or cast a

  final class, or cast the mixin to your target class, you can use the

  "\(TargetClass\) \(Object\) sourceClassObject" trick.

* To modify a constructor, use "\" \(or "\" for static

  constructors\) as the method name. Please note that @Inject on

  constructors only works with @At\("RETURN"\) - no other forms of

  injection are officially supported!

* @Redirect and @ModifyConstant mixins cannot currently be nested

  \(applied by more than one mod in the same area at the same time\).

  This might change later in development - however, for now, alongside

  @Overwrite, please avoid them if possible \(or discuss bringing the

  hook over to Fabric's API, or - for more niche things - consider

  putting it in a JAR-in-JAR once that's out\).

* If you're adding custom fields or methods, especially if they're not

  attached to an interface - prefix them with "\[modid\]\_" or another

  unique string. Essentially, "mymod\_secretValue" instead of

  "secretValue". This is to avoid conflicts between mods adding a

  field or method named the same way!

## Networking

* Packets always begin execution on the **network thread**, however

  accesses to most Minecraft things are not thread-safe. In general,

  if you're not exactly sure what you're doing, you want to parse the

  packet on the network thread \(read all the values out\), then use the

  **task queue** to perform additional operations on the \*\*main

  server/client thread\*\*.

## Pitfalls

* Avoid using the `java.awt` package and its subpackages. AWT does not

  work well on all systems. Several users have reported that it tends

  to hang Minecraft.


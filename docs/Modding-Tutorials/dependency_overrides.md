# Dependency Overrides

Fabric Loader version `0.11.1` introduced a feature named "Dependency
Overrides".

**Before you use it: This feature is designed to give modpack developers
more control over mod dependenies.**

As a regular player, you shouldn't use this, but ask the mod developers
to do whatever is that you want to change.

## Requirements

- Fabric Loader `0.11.1` or later
- Be familiar with the `fabric.mod.json` dependency syntax and the 5
  types of dependencies (`depends`, `recommends`, `suggests`,
  `conflicts`, `breaks`).

## Starting

First, create a file named `fabric_loader_dependencies.json` inside the
`config` folder (which should be inside your `.minecraft` folder).

Next, we fill in the file with the following boilerplate content:

```JavaScript
{
  "version": 1,
  "overrides": {
    
  }
}
```

Let's go over it line-by-line.

First, we have `version`, which specifies the dependency override spec
version we would like to use. At the time of writing this page, the
latest version is version `1`.

Secondly, we have `overrides` (which is currently empty). This Json
object will contain all of our dependency overrides to various mods.

## How to Override

Inside the `overrides` object, we may add entries with a key of a loaded
mod's ID and a value of a Json object. For example, if a loaded mod's ID
is `mymod`, We can do the following:

```JavaScript
{
  "version": 1,
  "overrides": {
    "mymod": {}
  }
}
```

As mentioned above, `mymod` has the value of a Json object. Inside that
object, we can add dependency overrides.

Keys inside the mod object can be one of the 5 dependency types
(`depends`, `recommends`, `suggests`, `conflicts`, `breaks`).

The key may be optionally prefixed with `+` or `-` (e.g. `"+depends"`,
`"-breaks"`).

The value of any one of those keys must be a Json object. This Json
object follows the exact same structure as a `fabric.mod.json`
dependency object.

If the key is prefixed with `+`, the entries inside that Json object
will be added (or overridden if already exist) to the mod.

If the key is prefixed with `-`, the value of each entry is ignored
completely and Fabric Loader will remove those entries from the
resulting dependency map.

If the key isn't prefixed, the dependency object will be replaced
completely. **Be careful to prefix your keys!**

## Practical Example

Let's assume that a mod with ID `specificmod` depends on Minecraft
version `1.16.4` **exactly**, but we want it to work on other 1.16
versions. Let's see how we can do that:

```JavaScript
{
  "version": 1,
  "overrides": {
    "specificmod": {
      "+depends": {
        "minecraft": "1.16.x"
      }
    }
  }
}
```

A `"minecraft"` dependency will now be overridden if specified (and we
know it is). There is another way to do this:

```JavaScript
{
  "version": 1,
  "overrides": {
    "specificmod": {
      "-depends": {
        "minecraft": "IGNORED"
      }
    }
  }
}
```

As specified above, the value of key `"minecraft"` will be ignored when
removing dependencies. If a dependency with a mod ID requirement of
`minecraft` is found, it will be removed from our target mod
`specificmod`.

We can also override the entire `depends` block, but with great power
comes great responsibility. Be careful.

Let's assume that `specificmod`'s dependency specification (inside
`fabric.mod.json`) looks something like this:

```JavaScript
{
  "depends": {
    "fabricloader": ">=0.11.1",
    "fabric": ">=0.28.0",
    "minecraft": "1.16.4"
  },
  "breaks": {
    "optifabric": "*"
  },
  "suggests": {
    "anothermod": "*",
    "flamingo": "*",
    "modupdater": "*"
  }
}
```

Aside from changing the `minecraft` dependency, we also want to remove
all `suggests` dependencies. We can do that like so:

```JavaScript
{
  "version": 1,
  "overrides": {
    "specificmod": {
      "-depends": {
        "minecraft": ""
      },
      "suggests": {}
    }
  }
}
```

Because the `suggests` key was not prefixed, it was completely replaced
with an empty object, essentially clearing it.

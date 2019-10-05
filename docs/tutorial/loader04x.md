# Loader 0.4.x and You\!

This tutorial lists what you need to know to update your mods to
**Loader 0.4.x** (and beyond\!?).

## Schema changes

The schema has changed somewhat. Throughout 0.4.x, the schemaVersion 0
will be supported, but no such promises are made for 0.5.x and beyond.

Useful sources:

- [fabric.mod.json format](../documentation/fabric_mod_json.md)
- [Example mod](https://github.com/FabricMC/fabric-example-mod/),
  which should be updated by now ([update
  commit](https://github.com/FabricMC/fabric-example-mod/commit/d6e85e22192c7d824572668f54a5bf81eec3bc78))

## Nested JARs

There has been a lot of confusion about what nested JARs are meant to be
used for.

Nested JARs **are**:

- a solution to provide dependencies which are in the form of *Fabric
  mods*, allowing the loader to pick the best version matching a given
  modpack's sets of dependencies,
- a solution that allows you turning libraries into *Fabric mods* and
  avoiding conflicts where they are not cleanly shadowable or where
  there is good reason for a Fabric mod developer to be the
  authoritative versioning source,
- a potential solution to cleanly package subprojects/submodules of a
  mod in one "combined" JAR, while also allowing using them
  separately.

Nested JARs **are not**:

- meant to be used on non-mod Java libraries,
- always the best solution for libraries which can be safely shadowed
  under a different package. Keep in mind that a Fabric mod ID can
  only exist once, meaning that potential version conflicts could
  prevent a pack from loading - shadowed libraries do not have this
  problem.

In doubt, refer to this helpful chart:

![](../images/tutorial/nested_jar_chart.png)

## Incompatibilities

- If your mod is using plugin-loader, please abandon it and adopt the
  entrypoints system instead.


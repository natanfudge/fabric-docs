# Matcher

[Matcher](https://github.com/FabricMC/Matcher) is a GUI tool to match
APIs from two jars by comparing entries, performing mapping analysis
between the two and then saving the matches which are then uploaded to
[FabricMC/intermediary](https://github.com/FabricMC/intermediary/tree/master/matches)
to be used as part of Yarn's build process.

## Overview

Matcher present's a split panel view, with the input A jar on the left
and input B jar on the right. Input A's classes are always visible,
where as input B's classes are visible when not matched to any from
input A.

Class entries are color coded to represent the matched status, red
denotes unmatched, amber for partially matched and green for 100%
matched (including methods and fields).

## Using Matcher

### Creating a New Project

Clicking `File` -\> `New project`, a dialog window will appear with the
following fields:

- Inputs A/B
  - Specify the two (maybe more) jars of the game, where
  - A is the already mapped version of the jar with an existing
    intermediary mapping
  - B is the new version with obfuscated class names that have
    changed.
- Class path A/B
  - Specify all the libraries that are used by the game versions
    respectively.
- Shared class path
  - Specifiy libraries used by both versions
- Non-obfuscated class name pattern A/B (regex)
  - Supply a regular expression to fully match jar class entry names
    (using `/` instead of `.`)
  - Example: With StarMade (as of `0.201.364`) obfuscated classes
    are inside the `obfuscated` package.
    `(org\/|schine\/|PolygonStatsInterface\/).*` would match any
    class under the `org`, `schine` and `PolygonInterface` packages
    as they are already public APIs that do not need to be matched.

Note: If any of the jars in the input/class path is invalid, clicking
creating a new project button will have no response\!

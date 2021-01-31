# Updating Yarn and intermediary to a new Minecraft version

Last updated 2020-05-20 by 2Xsaiko.

## Definitions/Notes

- **A** refers to the starting version of the game; **B** refers to
  the target version. They should always use the official Mojang
  identifier for a version!

## Requirements

- At least **six gigabytes** of free RAM. (I am not joking. Matcher is
  not optimized for RAM usage at all. Swap memory might be enough.)
  (6GB actually works on liach's device)
  - Note that Matcher doesn't actually need this much - its constant
    memory usage seems to peak at about 4GB heap during the first
    auto match, and after that is done it seems to use under 1GB
    heap; the thing is, the auto matching process generates an
    insane amount of object churn (again, optimized for code clarity
    rather than memory performance) and as such benefits from a high
    ceiling of memory.
- A cloned repository of [Yarn](https://github.com/FabricMC/yarn), on
  the **A** version. (Not required, but convenient for getting merged
  jars)
- A cloned repository of the [Intermediary mappings](https://github.com/FabricMC/intermediary/).
- A compiled copy of Matcher. (Only runs on java 11 or later!)
  - Consider using [the Fabric fork of   Matcher](https://github.com/FabricMC/Matcher) for easy building
    of fat jars that ships openjfx.
  - The Fabric fork differs solely in its inclusion of a Gradle
    build script for easy use - it generally tracks sfPlayer1's
    upstream.
  - A local installation of Gradle (preferably the latest, as of
    writing 6.3) is required to compile the Fabric fork. The Gradle
    major version should be at least 5.
- A compiled copy of [Stitch](https://github.com/FabricMC/stitch).
  - Allows for intermediary generation, the core of updating to a
    new Minecraft version.

## The Process

### Preparations

1. Enter the current Yarn version at **A**.
2. Run `gradlew mergeJars downloadMcLibs`. This will create
   **A**-merged.jar and .gradle/minecraft/libraries. You will need
   these for Matcher.
3. Edit build.gradle to point to the **B** Minecraft version.
4. Run `gradlew mergeJars downloadMcLibs` again. This will create
   **B**-merged.jar and grab new libraries (if there is any)
5. Launch Matcher, with -Xmx6G at the very least and -Xmx8G
   recommended, depending on your amount of RAM.
6. Create a new project and configure its **inputs** (the -merged JARs)
   and **class paths** (the libraries).
   - The shared class path can be used for libraries whose versions
     have not changed between **A** and **B**.
7. Wait for it to match. It takes quite a while.
8. In Matcher, `View -> Sort by Matched Status`.
9. Run `Auto Match All` to perform the initial matches.

### Matching

#### Notes

You can use mojang's offical mappings, or offical -&gt; named yarn
mappings as an input for side A (and B if using mojang mappings) to aid
with the matching process. To generate these yarn mappings clone yarn,
and run \`gradlew.bat exportMappingsOfficial\`

Matching is a process that cannot be easily described. Essentially,
there are three states:

- **red** - not matched,
- **yellow** - unconfidently matched,
- **green** - matched.

Your goal is to make as many red entries yellow or green as possible.
Matcher will generally get most things auto-matched, but occasional
false positives (in the single digit) can happen.

The way asie did it is as follows, roughly:

- Match all the unmatched (red) classes first.
- Run `Auto Match All` again.
- Match as many fields and methods as you can.

Some advice:

- The "match 100%" button will match all fields and methods which have
  not changed from version **A** to **B** - it is generally safe to
  use when there are no visible changes in the class, but **ALWAYS**
  make sure of that.
- The tabs in the matching menu expose the full heuristics of Matcher.
  You should use them to gather more information (for instance, it can
  help you check if the code changes were significant).
- Matcher currently doesn't take synthetic methods into account well,
  with many uncertainties between, say, ten synthetic methods with a
  near-identical body. If you do a few updates, you'll notice which
  classes are exceedingly unlikely to change in this regard - just use
  "match 100%" on them.

To not have to read obfuscated names, you can follow the initial parts
of the "Updating Yarn" phase - that is, loading **A** Yarn mappings into
Matcher - early.

### Updating Intermediary

1. Save the matches as `matches/A-B.match` in the Intermediary
   repository.
2. Run the following command (assuming you run from the root directory
   of intermediary repo and you have the stitch jar there):
   `java -Dstitch.counter=counter.txt -jar <path to stitch jar> updateIntermediary <path to A-merged.jar> <path to B-merged.jar> mappings/A.tiny mappings/B.tiny matches/A-B.match`.
   This will use the match information to update the intermediary
   mappings, preserving mod call compatibility where possible, and will
   use the global counter so when two versions branching off the same
   version won't have intermediary conflicts (e.g. 20w14infinite and
   20w14a)
   - This might find duplicate mappings, when Mojang combines
     multiple old method calls into one method call. In this case,
     your best bet is to try to find the most "common" option and
     tell Intermediary to settle on that. (Often, it's the one
     provided by an interface)

### Updating Yarn

1. Open the Enigma mappings from `mappings_official`, with **Type:
   Names**, **Target: A (left)** and **"Replace" checked**.
2. Save the Enigma mappings **TO A NEW DIRECTORY**, with **Environment:
   B (right)**, **Source name type: PLAIN**, **Target name type:
   MAPPED\_PLAIN** and **Verbosity: ROOTS**.
3. If everything went fine, delete the old directory's and rename the
   new directory to `mappings_official`.
4. Run `gradlew importMappingsOfficial` to import the mappings from
   `mappings_official` to `mappings`
5. Run `gradlew yarn`. If Enigma works correctly, **save the mappings
   in Enigma**, then close Enigma.

### Finishing touches

1. Create a new branch, named **B** - matching Mojang's name (it is
   permissible to replace spaces with underscores).
2. Add the new mappings/ and build.gradle, then push the branch.
3. You're done!

## Bonus

### Creating the initial mapping intermediaries

Essentially, instead of stitch's updateIntermediary command, use
generateIntermediary.

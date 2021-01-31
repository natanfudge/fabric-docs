# The Fabric Feature Procedure

So you want a feature to enter Fabric? Well then - here's a brief guide
on how to best propose a feature in a way which won't get lost in noise
and give *some* measurable results.

## Friendly Advice

- We don't tend to ban people just for having ideas, no matter how
  outlandish. However, "no" can mean "no", and unconstructive
  responses to constructive arguments can get on our nerves after some
  time. We are generally unpaid volunteers who dedicate a lot of free
  time to the tiring job of social interaction - please don't make our
  work harder than it already is.
- We're here to help each other learn, after all. Don't hesitate to
  talk to us at any point!
- Fabric isn't easy to get your content into, as we uphold standards
  of quality and are very paranoid about making a bad precedent. You
  will be scrutinized again, and again, and again; you will be asked
  questions about more-or-less every line in your pull request (even
  the imports!). Don't believe us? ~~Ask Grondag!~~

## When should I aim for Fabric?

There are many projects in the Fabric toolchain, and all of them welcome
different types of content (when in doubt, ask us in chat!):

- Projects like **Enigma**, **Stitch** and **tiny-remapper** have a
  well-defined scope - they're tools dedicated for specific purposes.
- Projects like **Matcher**, **Procyon**, **Fernflower**, etc. are
  forks of existing upstreams - try getting your changes accepted
  there first.
- Language shims, such as **fabric-language-kotlin** and
  **fabric-language-scala**, are always welcome! Standardizing on them
  is generally a good idea for everyone involved.
- **fabric-loader** is the mod loader. Features added to it generally
  revolve around the scope of mod loading - configuration, versioning,
  code modification mechanisms, security, the scope of supported
  environments, etc. - and *never* impose a dependency on specific
  game version code.
- **fabric** is the API module. Features added to it can be put into
  one of two categories:
  - *Hooks*, which expose ways to utilize functionality hardcoded,
    package-private or otherwise not exposed in vanilla code,
  - and *Interop*, which adds mechanisms to improve mod
    interoperation (within the scope of vanilla constructs - so no
    things like custom power systems, etc).
- In any case, **fabric** features are encouraged to follow the
  "majority rule". As we wish to track versions quickly, we are not
  keen on maintaining features only necessary by a small fraction of
  mods as part of mainline. We are somewhat more lenient with regards
  to *patches which cannot co-exist* - that is, a hook or patch which
  can only be applied by one mod at the same time.

However, if your idea doesn't fit on the above list, fear not! There are
other ways to share your standard with the world:

- For helper code (which won't always end up in Fabric), shadowing is
  always an option for interested parties.
- Nested JARs! They remove the friction from depending on or utilizing
  a small external hook or API - as each mod can bundle them, and
  Fabric's Loader will resolve the best applicable version
  automatically.

## The Procedure

### Make sure it's worth it

In short: Ask someone more knowledgeable in chat if the idea is even
worth pursuing for Fabric. This can be skipped, of course - if you don't
mind wasting a bit extra time in exchange for wider insights.

### Open an issue

Yes. **Open an issue.** Like, on the relevant GitHub project. We do
often miss chat messages, etc. - while they're fine for initial
surveying and discussion, they're not fine for keeping a track record.

### Wait for input

(cue all developers arguing for 100 comments on the meaning of a 5-line
patch)

Just wait until a clear direction/design is decided upon.

### Create a pull request

Now that you know what to do, put it into code! (Of course, the issue
creator and the pull request developer do not have to be the same
person. If you're lucky, one of the devs might even do the ground work
for you!)

Make sure your code can compile. Also make sure `checkstyle` is happy
and run a `licenseFormat`.

### Wait for more input

(have you met Player yet?)

### Last Call

You are almost there! Now your pull request goes into last call where
extra scrutiny may be applied before accepting the pull request.

### Get merged

Rejoice. Your feature is now a part of the Fabric hype train.

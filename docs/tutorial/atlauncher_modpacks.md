# Publishing Fabric modpacks on ATLauncher

ATLauncher's modpack publishing system is a bit complicated. Here are
some references which might help:

- [ATLauncher Wiki](https://wiki.atlauncher.com/) (incomplete in key
  areas)
- [Resonant Rise's modpack
  definition](https://github.com/Resonant-Rise/ResonantRise/blob/master/ResonantRise.xml)
- [ATLauncher Discord](https://discordapp.com/invite/qNnamR)

## The Fabric Bits

ATLauncher has introduced [official
support](https://github.com/ATLauncher/ATLauncher/issues/338#issuecomment-489320686)
for Fabric as of May 4th, 2019. Here is a relevant XML snippet, showing
how to use it in action:

```xml
<version>
    <pack>
        <version>1.0.0</version>
        <minecraft>1.14</minecraft>
        <memory>2048</memory>
    </pack>
    <loader type="fabric" yarn="1.14+build.21" loader="0.4.6+build.144"/>
</version>
```


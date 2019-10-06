# Publier des modpacks Fabric sur ATLauncher

Le système de publication de modpack d'ATLauncher est un peu compliqué.
Voici quelques références qui pourraient vous aider :

- [Wiki ATLauncher](https://wiki.atlauncher.com/) (incomplet dans les
  domaines clés) (en)
- [Définition de modpack de Resonant Rise](https://github.com/Resonant-Rise/ResonantRise/blob/master/ResonantRise.xml)
- [Discord ATLauncher](https://discordapp.com/invite/qNnamR)

## La partie Fabric

ATLauncher a introduit un [support
officiel](https://github.com/ATLauncher/ATLauncher/issues/338#issuecomment-489320686)
pour Fabric depuis le 4 mai 2019. Voici un extrait de code XML
pertinent, montrant comment l'utiliser :

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


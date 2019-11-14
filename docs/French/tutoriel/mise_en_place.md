# Mettre en place un environnement de dÃ©veloppement

## PrÃ©-requis

```
* Un kit de dÃ©veloppement Java (JDK) pour Java 8 ou plus rÃ©cent [[https://adoptopenjdk.net/]]
* N'importe quel IDE, par exemple [[https://www.jetbrains.com/idea/download/#section=windows|Intellij IDEA]]
```

## Ã‰tapes

```
- Copiez les fichiers de dÃ©part Ã  partir du [[https://github.com/FabricMC/fabric-example-mod/|fabric-example-mod]] (ou Ã  partir de la [[https://github.com/natanfudge/fabric-example-mod-kotlin|version Kotlin]] si vous souhaitez utiliser Kotlin), Ã  l'exception des fichiers ''LICENSE'' et ''README.md'' - puisque ceux-ci s'appliquent au modÃ¨le lui-mÃªme, pas nÃ©cessairement Ã  votre mod.
- Modifiez ''gradle.properties'' :
    * Assurez-vous de dÃ©finir ''archives_base_name'' et ''maven_group'' aux valeurs que vous voulez.
    * Assurez-vous de mettre Ã  jour les versions de Minecraft, les mappings et le loader - qui peuvent tous Ãªtre vÃ©rifiÃ©s sur [[https://modmuss50.me/fabric.html|ce site web]] - pour correspondre aux versions que vous souhaitez cibler.
    * Ajoutez les autres dÃ©pendances que vous prÃ©voyez d'utiliser dans ''build.gradle''.
- Importez le projet dans votre IDE. Suivez [[fr:tutoriel:mise_en_place_vscode|ces instructions]] [[tutorial:vscode_setup|(en)]] pour importer le projet dans Visual Studio Code.
- ExÃ©cutez la tÃ¢che Gradle ''genSources''. Si votre IDE n'a pas d'intÃ©gration Gradle, exÃ©cutez la commande suivante dans le terminal : ''./gradlew genSources''.
- Si vous voulez que l'IDE exÃ©cute les configurations, vous pouvez exÃ©cuter les commandes suivantes :
    * Pour IntelliJ IDEA : ''./gradlew idea''.
    * Pour Eclipse : ''./gradlew eclipse''.
    * Si vous utilisez VS Code, les configurations ont Ã©tÃ© gÃ©nÃ©rÃ©es Ã  l'Ã©tape 3.
- Bon modding !
```

## Pour commencer

Essayez d'[ajouter un objet](../../French/tutoriel/objets.md) ou [un
bloc](../../French/tutoriel/blocs.md). Il est Ã©galement conseillÃ© de lire la page
[Appliquer des modifications sans redÃ©marrer
Minecraft](../../French/tutoriel/appliquer_modifications.md).

## Conseils

- Bien que l'API Fabric ne soit pas strictement nÃ©cessaire pour
  dÃ©velopper des mods, son objectif principal est de fournir une
  inter-compatibilitÃ© et des points d'ancrage lÃ  oÃ¹ le moteur de jeu
  ne le fait pas. Elle est donc vivement recommandÃ©e \!
- Ã‰tant donnÃ© que Fabric est en dÃ©but de dÃ©veloppement,
  occasionnellement, avec le dÃ©veloppement de fabric-loom (notre
  plugin de build Gradle), des problÃ¨mes nÃ©cessitant un nettoyage
  (suppression) manuel du cache (qui peut Ãªtre trouvÃ© dans
  `.gradle/caches/fabric-loom`) peuvent survenir. Ceux-ci seront
  gÃ©nÃ©ralement annoncÃ©s Ã  mesure qu'ils seront identifiÃ©s.
- N'hÃ©sitez pas Ã  poser des questions \! Nous sommes lÃ  pour vous
  aider et travailler avec vous, afin de faire de votre mod rÃªvÃ© une
  rÃ©alitÃ©.

## ProblÃ¨mes

### Sons manquants

Parfois, en important le projet Gradle dans un IDE, les *assets* peuvent
ne pas Ãªtre tÃ©lÃ©chargÃ©s correctement. Dans ce cas, exÃ©cutez la tÃ¢che
`downloadAssets` manuellement - soit en utilisant le menu intÃ©grÃ© de
l'IDE ou en exÃ©cutant simplement `gradlew downloadAssets`.

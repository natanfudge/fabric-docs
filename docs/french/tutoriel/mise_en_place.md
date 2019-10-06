# Mettre en place un environnement de développement

## Pré-requis

```text
* Un kit de développement Java (JDK) pour Java 8 ou plus récent [[https://adoptopenjdk.net/]]
* N'importe quel IDE, par exemple [[https://www.jetbrains.com/idea/download/#section=windows|Intellij IDEA]]
```

## Étapes

```text
- Copiez les fichiers de départ à partir du [[https://github.com/FabricMC/fabric-example-mod/|fabric-example-mod]] (ou à partir de la [[https://github.com/natanfudge/fabric-example-mod-kotlin|version Kotlin]] si vous souhaitez utiliser Kotlin), à l'exception des fichiers ''LICENSE'' et ''README.md'' - puisque ceux-ci s'appliquent au modèle lui-même, pas nécessairement à votre mod.
- Modifiez ''gradle.properties'' :
    * Assurez-vous de définir ''archives_base_name'' et ''maven_group'' aux valeurs que vous voulez.
    * Assurez-vous de mettre à jour les versions de Minecraft, les mappings et le loader - qui peuvent tous être vérifiés sur [[https://modmuss50.me/fabric.html|ce site web]] - pour correspondre aux versions que vous souhaitez cibler.
    * Ajoutez les autres dépendances que vous prévoyez d'utiliser dans ''build.gradle''.
- Importez le projet dans votre IDE. Suivez [[fr:tutoriel:mise_en_place_vscode|ces instructions]] [[tutorial:vscode_setup|(en)]] pour importer le projet dans Visual Studio Code.
- Exécutez la tâche Gradle ''genSources''. Si votre IDE n'a pas d'intégration Gradle, exécutez la commande suivante dans le terminal : ''./gradlew genSources''.
- Si vous voulez que l'IDE exécute les configurations, vous pouvez exécuter les commandes suivantes :
    * Pour IntelliJ IDEA : ''./gradlew idea''.
    * Pour Eclipse : ''./gradlew eclipse''.
    * Si vous utilisez VS Code, les configurations ont été générées à l'étape 3.
- Bon modding !
```

## Pour commencer

Essayez d'[ajouter un objet](https://github.com/natanfudge/fabric-docs/tree/14ef4ec35beb42aa82da4d0c6a5e40f2806428b6/docs/French/tutoriel/objets.md) ou [un bloc](https://github.com/natanfudge/fabric-docs/tree/14ef4ec35beb42aa82da4d0c6a5e40f2806428b6/docs/French/tutoriel/blocs.md). Il est également conseillé de lire la page [Appliquer des modifications sans redémarrer Minecraft](https://github.com/natanfudge/fabric-docs/tree/14ef4ec35beb42aa82da4d0c6a5e40f2806428b6/docs/French/tutoriel/appliquer_modifications.md).

## Conseils

* Bien que l'API Fabric ne soit pas strictement nécessaire pour

  développer des mods, son objectif principal est de fournir une

  inter-compatibilité et des points d'ancrage là où le moteur de jeu

  ne le fait pas. Elle est donc vivement recommandée !

* Étant donné que Fabric est en début de développement,

  occasionnellement, avec le développement de fabric-loom \(notre

  plugin de build Gradle\), des problèmes nécessitant un nettoyage

  \(suppression\) manuel du cache \(qui peut être trouvé dans

  `.gradle/caches/fabric-loom`\) peuvent survenir. Ceux-ci seront

  généralement annoncés à mesure qu'ils seront identifiés.

* N'hésitez pas à poser des questions ! Nous sommes là pour vous

  aider et travailler avec vous, afin de faire de votre mod rêvé une

  réalité.

## Problèmes

### Sons manquants

Parfois, en important le projet Gradle dans un IDE, les _assets_ peuvent ne pas être téléchargés correctement. Dans ce cas, exécutez la tâche `downloadAssets` manuellement - soit en utilisant le menu intégré de l'IDE ou en exécutant simplement `gradlew downloadAssets`.


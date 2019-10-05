# Appliquer des modifications sans redémarrer Minecraft

Redémarrer Minecraft peut prendre un temps considérable. Heureusement,
il y a des outils qui vous permettent d'appliquer certaines
modifications pendant que le jeu est en cours d'exécution.

### Recharger les classes modifiées

Dans Eclipse ou Intellij IDEA, exécutez Minecraft en mode débogage. Pour
appliquer des modifications dans le code, faites `run -> reload changes
classes` dans Intellij ou sauvegardez dans Eclipse. Note : cela vous
permet seulement de modifier les corps des méthodes. Si vous faites un
autre type de modification, vous devrez redémarrer. Cependant, si vous
utilisez [DCEVM](http://dcevm.github.io/), vous pourrez faire la plupart
des modifications, y compris l'ajout et la suppression de méthodes et de
classes.

### Recharger les textures

Après que vous ayez modifié un fichier de texture (`.png`), vous pouvez
recharger les classes modifiées et appuyez sur `F3 + T` pour appliquer
la modification sans redémarrer Minecraft.

### Recharger les recettes et les tables de butin

Vous pouvez appliquer toute modification faite dans le répertoire
`data/` (notamment les recettes et les tables de butin) en rechargeant
les classes modifiées, puis en utilisant la commande `/reload` du jeu.

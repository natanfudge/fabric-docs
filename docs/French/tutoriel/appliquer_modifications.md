# Appliquer des modifications sans redÃ©marrer Minecraft

RedÃ©marrer Minecraft peut prendre un temps considÃ©rable. Heureusement,
il y a des outils qui vous permettent d'appliquer certaines
modifications pendant que le jeu est en cours d'exÃ©cution.

### Recharger les classes modifiÃ©es

Dans Eclipse ou Intellij IDEA, exÃ©cutez Minecraft en mode dÃ©bogage. Pour
appliquer des modifications dans le code, faites `run -> reload changes
classes` dans Intellij ou sauvegardez dans Eclipse. Note : cela vous
permet seulement de modifier les corps des mÃ©thodes. Si vous faites un
autre type de modification, vous devrez redÃ©marrer. Cependant, si vous
utilisez [DCEVM](http://dcevm.github.io/), vous pourrez faire la plupart
des modifications, y compris l'ajout et la suppression de mÃ©thodes et de
classes.

### Recharger les textures

AprÃ¨s que vous ayez modifiÃ© un fichier de texture (`.png`), vous pouvez
recharger les classes modifiÃ©es et appuyez sur `F3 + T` pour appliquer
la modification sans redÃ©marrer Minecraft.

### Recharger les recettes et les tables de butin

Vous pouvez appliquer toute modification faite dans le rÃ©pertoire
`data/` (notamment les recettes et les tables de butin) en rechargeant
les classes modifiÃ©es, puis en utilisant la commande `/reload` du jeu.

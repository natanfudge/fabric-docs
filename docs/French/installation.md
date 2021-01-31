# Installer Fabric

## Installation rÃ©guliÃ¨re

### MultiMC

**NOTE** MultiMC est un lanceur alternatif convivial pour Minecraft, qui
est recommandÃ© pour travailler avec Fabric et peut Ãªtre trouvÃ© ici. Si
vous ne souhaitez pas utiliser MultiMC ou si vous ne savez pas quel
lanceur vous utilisez, suivez les instructions [regular
client](../.md#mojang_s_minecraft_launcher).

1. CrÃ©ez une nouvelle instance de jeu.
2. SÃ©lectionnez "Modifier l'instance" dans le menu contextuel de
   l'instance ou sur le cÃ´tÃ© droit de la fenÃªtre.
3. Dans l'onglet Version de la fenÃªtre de configuration, cliquez sur
   "Installer Fabric" pour afficher une boÃ®te de dialogue avec toutes
   les versions disponibles de Fabric Loader. Choisissez-en un et
   cliquez sur OK pour l'ajouter Ã  l'instance.
   - Il est gÃ©nÃ©ralement recommandÃ© d'utiliser la derniÃ¨re version de
     Loader disponible.
   - Le chargeur doit Ãªtre principalement indÃ©pendant de la version
     du jeu. Si cette situation change, cela sera signalÃ©, alors ne
     vous inquiÃ©tez pas!
4. Appuyer sur OK. Votre instance Fabric est prÃªte Ã  l'emploi -
   n'hÃ©sitez pas Ã  y ajouter des mods!

Pour des instructions plus dÃ©taillÃ©es, visitez ce guide: [Installation
de Fabric Ã  l'aide du lanceur MultiMC](../Setup/install_with_multimc.md)

### Mojang's Minecraft Launcher

1. TÃ©lÃ©chargez le programme d'installation de Fabric depuis [la page de  tÃ©lÃ©chargement](https://fabricmc.net/use/).
   - Vous pouvez Ã©galement tÃ©lÃ©charger le programme d'installation
     directement Ã  partir de [Jenkins de    Modmuss50](https://jenkins.modmuss50.me/job/FabricMC/job/fabric-installer/job/master/).
2. Ouvrez l'installateur. Dans la fenÃªtre, vous devez configurer le
   mappage et la version du chargeur (selon les conseils de la [section  MultiMC](../.md#multimc)) et l'emplacement d'installation (la valeur par
   dÃ©faut devrait convenir Ã  la plupart des plates-formes).  
   \*\* REMARQUE \*\* Vous devez activer les snapshots afin que le
   programme d'installation affiche les options de mappage pour les
   snapshots Minecraft.
3. Appuyez sur Installer. Une nouvelle version du jeu et un nouveau
   profil seront crÃ©Ã©s dans le menu du lanceur, que vous pouvez
   maintenant utiliser pour lancer Fabric.

Pour des instructions plus dÃ©taillÃ©es, visitez ce guide: [Installer Ã 
l'aide du lanceur Minecraft](../Setup/install_with_minecraft_launcher.md)

### Serveur

**NOTE** \*\* Ã€ jour Ã  partir de Loader 0.4.4 +. \*\* Les versions plus
anciennes choisissent diffÃ©rentes techniques. Installateur 0.4.0+
requis!

1. TÃ©lÃ©chargez le programme d'installation de Fabric depuis [la page de  tÃ©lÃ©chargement](https://fabricmc.net/use/).
   - Vous pouvez Ã©galement tÃ©lÃ©charger le programme d'installation
     directement Ã  partir de [Jenkins de    Modmuss50](https://jenkins.modmuss50.me/job/FabricMC/job/fabric-installer/job/master/).
2. Ouvrez l'installateur. SÃ©lectionnez l'onglet "Serveur" en haut de la
   fenÃªtre.
3. Dans la fenÃªtre, vous devez configurer la version du mappage et du
   chargeur et l'emplacement d'installation.  
   \*\* REMARQUE \*\* Vous devez activer les snapshots afin que le
   programme d'installation affiche les options de mappage pour les
   snapshots Minecraft.
4. Appuyez sur Installer. Dans le rÃ©pertoire de sortie, un ''
   fabric-server-launch.jar '' sera crÃ©Ã©. Il attend un '' server.jar
   '', qui est le JAR du serveur vanilla, gÃ©nÃ©ralement trouvÃ© sur les
   articles du blog d'annonce de Mojang des versions, Ã  cÃ´tÃ©, mais
   gÃ©nÃ©rera Ã©galement un fichier de propriÃ©tÃ©s au premier lancement oÃ¹
   vous pourrez modifier le chemin d'accÃ¨s.  
   \*\* REMARQUE \*\* Si vous mettez Ã  niveau votre serveur existant,
   n'oubliez pas de supprimer le dossier '' .fabric '' si le programme
   d'installation ne l'a pas supprimÃ© pour vous! Ou vous obtiendrez des
   erreurs de chargement de classe.

Pour des instructions plus dÃ©taillÃ©es, visitez ce guide: [Installer un
serveur Minecraft /
Fabric](../Setup/installing_minecraft_fabric_server.md)

## Installation CLI

Le programme d'installation de Fabric prend entiÃ¨rement en charge
l'installation du client et du serveur Ã  partir de la ligne de commande.
Cela pourrait Ãªtre utilisÃ© pour automatiser l'installation. Le programme
d'installation dispose d'un certain nombre de commandes qui peuvent Ãªtre
utilisÃ©es pour une installation sans prise de tÃªte.

### Options disponibles

- **-snapshot** Permet l'utilisation de versions instantanÃ©es de
  Minecraft.
- **-dir** UtilisÃ© pour sÃ©lectionner le rÃ©pertoire d'installation, par
  dÃ©faut le rÃ©pertoire de travail actuel.
- **-mcversion** UtilisÃ© pour sÃ©lectionner la version minecraft, par
  dÃ©faut la derniÃ¨re version stable.
- **-loader** UtilisÃ© pour sÃ©lectionner la version du chargeur, par
  dÃ©faut la derniÃ¨re.
- **-downloadMinecraft** UtilisÃ© pour tÃ©lÃ©charger automatiquement le
  jar du serveur Minecraft
- **-noprofile** Ignorer la crÃ©ation d'un profil client
- **-mavenurl** (avancÃ©) Utilisez une URL maven personnalisÃ©e lors de
  l'installation
- **-metaurl** (avancÃ©) Utiliser une URL de mÃ©ta-serveur personnalisÃ©e

### Commandes disponibles

- **help** Imprime toutes les commandes disponibles ainsi que les
  derniÃ¨res mises en correspondance et versions de chargeur. Ignore
  les options. Exemple:

<!-- --->

    java -jar fabric-installer.jar help

- **server** UtilisÃ© pour crÃ©er les fichiers requis pour un serveur
  Fabric. Accepte toutes les options, aucune n'est requise. Exemple:

<!-- --->

    java -jar fabric-installer.jar server

- **client** UtilisÃ© pour crÃ©er les fichiers requis pour un client
  Fabric. Accepte toutes les options, \*\* - dir \*\* est requis.
  Exemple:

<!-- --->

    java -jar fabric-installer.jar client -dir "~/Games/.minecraft"


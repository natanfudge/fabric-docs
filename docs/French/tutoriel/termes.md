# Apprendre les conventions et la terminologie

Avant de commencer le modding Fabric, il est important de comprendre
certains termes et expressions clÃ©s utilisÃ©s dans les pages de tutoriel
suivantes. Il est Ã©galement utile de connaÃ®tre les conventions de base
pour des Ã©lÃ©ments tels que la structure des *packages* et le nommage de
l'ID du mod. Le fait de les connaÃ®tre tÃ´t vous aidera Ã  mieux comprendre
les tutoriels et Ã  poser de meilleures questions au besoin.

### ID du mod

Tout au long de la documentation, nous ferons souvent rÃ©fÃ©rence Ã  un ID
de mod, ou `modid` dans le code. Le terme ID de mod signifie
"Identifiant de mod". C'est une chaÃ®ne de caractÃ¨res qui devrait
identifier de maniÃ¨re unique votre mod. Un ID de mod est gÃ©nÃ©ralement
associÃ© Ã  l'espace de noms d'identifiant du mÃªme nom et suit donc les
mÃªmes restrictions. Il ne peut contenir que les caractÃ¨res `a-z` en
minuscule, les nombres `0-9` et les symboles `_-`. Par exemple,
Minecraft utilise l'espace de noms `minecraft`. De plus, un ID de mod
doit contenir au moins deux caractÃ¨res.

Un ID de mod est souvent une version compacte du nom du mod, ce qui le
rend court mais reconnaissable, et Ã©vite les conflits de noms.
Conventionnellement, un projet nommÃ© "Mon projet--" pourrait Ãªtre appelÃ©
`monprojet`, `mon_projet`, ou dans certains cas, `mon-projet` fonctionne
Ã©galement, mais les tirets dans les IDs de mod peuvent Ãªtre parfois un
peu compliquÃ©s Ã  traiter. Ce mod enregistrerait des objets et des blocs
avec cet ID de mod comme espace de noms de registre.

Certains des premiers tutoriels utiliseront un ID de mod gÃ©nÃ©rique et
enregistreront des objets et des blocs sous un espace de noms gÃ©nÃ©rique,
et vous pouvez considÃ©rer cela comme un modÃ¨le de dÃ©part -- alors que
laisser cela inchangÃ© n'est pas risquÃ© pour des tests, n'oubliez pas de
le changer si vous avez l'intention de publier votre projet.

### Tags

La convention pour l'espace de noms des tags est `c`.

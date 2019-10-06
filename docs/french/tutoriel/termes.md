# Apprendre les conventions et la terminologie

Avant de commencer le modding Fabric, il est important de comprendre
certains termes et expressions clés utilisés dans les pages de tutoriel
suivantes. Il est également utile de connaître les conventions de base
pour des éléments tels que la structure des *packages* et le nommage de
l'ID du mod. Le fait de les connaître tôt vous aidera à mieux comprendre
les tutoriels et à poser de meilleures questions au besoin.

### ID du mod

Tout au long de la documentation, nous ferons souvent référence à un ID
de mod, ou `modid` dans le code. Le terme ID de mod signifie
"Identifiant de mod". C'est une chaîne de caractères qui devrait
identifier de manière unique votre mod. Un ID de mod est généralement
associé à l'espace de noms d'identifiant du même nom et suit donc les
mêmes restrictions. Il ne peut contenir que les caractères `a-z` en
minuscule, les nombres `0-9` et les symboles `_-`. Par exemple,
Minecraft utilise l'espace de noms `minecraft`. De plus, un ID de mod
doit contenir au moins deux caractères.

Un ID de mod est souvent une version compacte du nom du mod, ce qui le
rend court mais reconnaissable, et évite les conflits de noms.
Conventionnellement, un projet nommé "Mon projet--" pourrait être appelé
`monprojet`, `mon_projet`, ou dans certains cas, `mon-projet` fonctionne
également, mais les tirets dans les IDs de mod peuvent être parfois un
peu compliqués à traiter. Ce mod enregistrerait des objets et des blocs
avec cet ID de mod comme espace de noms de registre.

Certains des premiers tutoriels utiliseront un ID de mod générique et
enregistreront des objets et des blocs sous un espace de noms générique,
et vous pouvez considérer cela comme un modèle de départ -- alors que
laisser cela inchangé n'est pas risqué pour des tests, n'oubliez pas de
le changer si vous avez l'intention de publier votre projet.

### Tags

La convention pour l'espace de noms des tags est `c`.

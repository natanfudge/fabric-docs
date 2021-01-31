# Recettes de fabrication

## Ajouter une recette de fabrication de base

Assurez-vous d'avoir [ajoutÃ© un objet](../../French/tutoriel/objets.md) avant de
lire ce tutoriel, nous allons l'utiliser.

Jusqu'Ã  prÃ©sent, notre objet est obtenable uniquement via le menu
crÃ©atif ou des commandes. Pour le rendre disponible Ã  des joueurs
survie, nous allons lui ajouter une recette de fabrication.

CrÃ©ez un fichier nommÃ© `fabric_item.json` dans le dossier
`resources/data/tutorial/recipes/` (remplacez *tutorial* par votre ID de
mod si cela est appropriÃ©). Voici un exemple de recette pour le
`fabric_item` que nous avons fait :

```javascript
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "WWW",
    "WR ",
    "WWW"
  ],
  "key": {
    "W": {
      "tag": "minecraft:logs"
    },
    "R": {
      "item": "minecraft:redstone"
    }
  },
  "result": {
    "item": "tutorial:fabric_item",
    "count": 4
  }
}
```

DÃ©composition de la recette :

- **type** : C'est une recette de fabrication en forme.
- **result** : C'est une recette de fabrication pour 4
  `tutorial:fabric_item`. Le champ `count` est optionnel. Si vous ne
  spÃ©cifiez pas un `count`, il sera Ã  1 par dÃ©faut.
- **pattern** : Un motif qui reprÃ©sente la recette de fabrication.
  Chaque lettre reprÃ©sente un objet. Un espace vide signifie qu'aucun
  objet n'est requis dans cet emplacement. Ce que chaque lettre
  reprÃ©sente est dÃ©fini dans **key**.
- **key** : Ce que chaque lettre dans le motif reprÃ©sente. `W`
  reprÃ©sente n'importe quel objet avec le tag `minecraft:logs` (toutes
  les bÃ»ches). `R` reprÃ©sente la redstone. Pour plus d'informations
  sur les tags, voir [ici](https://minecraft-fr.gamepedia.com/Tag).

Au final, la recette de fabrication ressemblerait Ã  cela :

| Recette pour 4 fabric\_item |             |             |
|-----------------------------|-------------|-------------|
| Toute bÃ»che                 | Toute bÃ»che | Toute bÃ»che |
| Toute bÃ»che                 | Redstone    | Rien        |
| Toute bÃ»che                 | Toute bÃ»che | Toute bÃ»che |

Pour plus d'informations sur le format des recettes de base, voir
[ici](https://minecraft-fr.gamepedia.com/Recette).

## Ajouter une recette de fabrication personnalisÃ©e

La valeur `type` peut Ãªtre changÃ©e pour supporter une recette
personnalisÃ©e.

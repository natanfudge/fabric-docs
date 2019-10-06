# Recettes de fabrication

## Ajouter une recette de fabrication de base

Assurez-vous d'avoir [ajouté un objet](../../French/tutoriel/objets.md) avant de
lire ce tutoriel, nous allons l'utiliser.

Jusqu'à présent, notre objet est obtenable uniquement via le menu
créatif ou des commandes. Pour le rendre disponible à des joueurs
survie, nous allons lui ajouter une recette de fabrication.

Créez un fichier nommé `fabric_item.json` dans le dossier
`resources/data/tutorial/recipes/` (remplacez *tutorial* par votre ID de
mod si cela est approprié). Voici un exemple de recette pour le
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

Décomposition de la recette :

- **type** : C'est une recette de fabrication en forme.
- **result** : C'est une recette de fabrication pour 4
  `tutorial:fabric_item`. Le champ `count` est optionnel. Si vous ne
  spécifiez pas un `count`, il sera à 1 par défaut.
- **pattern** : Un motif qui représente la recette de fabrication.
  Chaque lettre représente un objet. Un espace vide signifie qu'aucun
  objet n'est requis dans cet emplacement. Ce que chaque lettre
  représente est défini dans **key**.
- **key** : Ce que chaque lettre dans le motif représente. `W`
  représente n'importe quel objet avec le tag `minecraft:logs`
  (toutes les bûches). `R` représente la redstone. Pour plus
  d'informations sur les tags, voir
  [ici](https://minecraft-fr.gamepedia.com/Tag).

Au final, la recette de fabrication ressemblerait à cela :

| Recette pour 4 fabric\_item |             |             |
| --------------------------- | ----------- | ----------- |
| Toute bûche                 | Toute bûche | Toute bûche |
| Toute bûche                 | Redstone    | Rien        |
| Toute bûche                 | Toute bûche | Toute bûche |

Pour plus d'informations sur le format des recettes de base, voir
[ici](https://minecraft-fr.gamepedia.com/Recette).

## Ajouter une recette de fabrication personnalisée

La valeur `type` peut être changée pour supporter une recette
personnalisée.

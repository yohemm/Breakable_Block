# Commands

## block-breakable

**Création de blocs cassables**

Formes :

- `/block-breakable timeInSecToRegen idRecomp:data min-maxQuantity idBlockAfterBreak:data`
- `/block-breakable timeInSecToRegen idRecomp quantity idBlockAfterBreak`
- `/block-breakable timeInSecToRegen idRecomp min-maxQuantity`
- `/block-breakable timeInSecToRegen idRecomp quantity`

Le bloc visé correspond au bloc sous forme cassable.

- **idBlockAfterBreak** : Correspond au bloc une fois cassé (ex : `0` pour le vide).
- **timeInSecToRegen** : Temps en secondes avant régénération du bloc sous forme cassable.
- **idRecomp** : Item qui sera déposé au sol lors du cassage du bloc.
- **min-maxQuantity / quantity** : Nombre de récompenses qui seront déposées. Le nombre est généré aléatoirement entre 2 valeurs à chaque cassage du bloc. `64` est équivalent à `64-64` et permet d'avoir un nombre fixe de récompenses.
- **idBlockAfterBreak** : Permet de préciser le bloc de remplacement lors de l'état cassé/en cours de régénération. Si ce paramètre n'est pas précisé, la bedrock sera utilisée par défaut.

## no-block-breakable

**Suppression de blocs cassables**

- `no-block-breakable`

Régénère le bloc à son état initial avant la suppression.

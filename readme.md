# Commands

## block-breakable

    creation de block cassable

Formes :

- `/block-breakable timeInSecToRegen idRecomp:data min-maxQuantity idBlockAfterBreak:data`
- `/block-breakable timeInSecToRegen idRecomp quantity idBlockAfterBreak`
- `/block-breakable timeInSecToRegen idRecomp min-maxQuantity`
- `/block-breakable timeInSecToRegen idRecomp quantity`

Le block visé correspond au block sous forme cassable

idBlockAfterBreak : correspond au block une fois cassé (ex: `0` pour le vide ).

timeInSecToRegen : temps en secondes avant regeneration de block sous forme cassable.

idRecomp : item qui seras drop au sol lors du cassage de block.

min-maxQuantity / quantity : nombre de recompenses qui seront drop, le nombre est generer aléatoirement entre 2 nombres à chaque cassage du block .`64` est exactement comme `64-64` est permet de d'avoir un nombre fix de recompense.

idBlockAfterBreak : Permet de précisser le block de remplacement lors de l'état cassé/en cours de regenration, si il n'est pas précissé, la bedrock seras utilisé par défaults.

## no-block-breakable

    Suppression de block cassable

- `no-block-breakable`

regenere le block à son état initial avant la suppression

# A ajouté

Systeme de d'outils nessécaire pour casser un block

Avoir plusieur trésor possible

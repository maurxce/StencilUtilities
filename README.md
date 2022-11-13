# StencilUtilities

### Commands

| Name     | Arguments     | Alias         | Description              |
| -------- | ------------- | ------------- | ------------------------ |
| /reload  |               | stencilreload | Reloads the config files |
| /setrank | [name/random] |               | Sets a players rank      |
| /delrank | [player]      |               | Remove a players rank    |


### Permissions

| Node                   | Default  | Description                                |
| ---------------------- | -------- | ------------------------------------------ |
| stencil.reload         | op       | Permission for the /reload command         |
| stencil.setrank        | everyone | Permission for the base /setrank command   |
| stencil.setrank.random | everyone | Permission for the random /setrank command |
| stencil.delrank        | op       | Permission to remove player ranks          |

### Dependencies
| Name           | Type       |
| -------------- | ---------- |
| LuckPerms      | dependency | 
| PlaceholderAPI | dependency |

### Placeholders

| Name              | Description                           |
|-------------------| ------------------------------------- |
| %stencil_rank%    | Displays the current rank of a player |
| %stencil_player%  | Display a players name                |
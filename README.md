# StencilUtilities

### Commands

| Name     | Arguments     | Alias         | Description                                                                        |
| -------- | ------------- | ------------- | ---------------------------------------------------------------------------------- |
| /setrank | [name|random] |               | Sets a players rank either to the name provided or to a random one from the config |
| /reload  |               | stencilreload | Reloads the config files                                                           |


### Permissions

| Node                   | Default  | Description                                |
| ---------------------- | -------- | ------------------------------------------ |
| stencil.reload         | op       | Permission for the /reload command         |
| stencil.setrank        | everyone | Permission for the base /setrank command   |
| stencil.setrank.random | everyone | Permission for the random /setrank command |

### Dependencies
| Name           | Type       |
| -------------- | ---------- |
| LuckPerms      | dependency | 
| PlaceholderAPI | dependency |

### Placeholders

| Name           | Description                           |
| -------------- | ------------------------------------- |
| %stencil_rank% | Displays the current rank of a player |
# vue

## Project setup

```
npm install
```

### Compiles and hot-reloads for development against running backend

```
npm run serve
```

### Compiles and hot-reloads for development using mocks instead of backend

```
npm run serve:mocked
```

### Compiles and minifies for production

```
npm run build
```

### Run your unit tests

```
npm run test:unit
```

### Run your end-to-end tests
```
npm run test:e2e
```

### Lints and fixes files

```
npm run lint
```

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).

# routes

* / -> default page if user is logged in
* /start -> two buttons either create a game or join a game
* /configure-game -> configure a game (e.g. add player, remove player etc.)
* /games/{joinCode} -> enter rounds, tichus etc.; active until game is finished
* /statistics -> some interesting stuff

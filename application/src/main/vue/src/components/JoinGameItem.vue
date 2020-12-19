<template>
  <v-list-item class="join-game-item" to="/join-game">
    <v-list-item-icon>
      <v-icon>mdi-gamepad</v-icon>
    </v-list-item-icon>
    <v-list-item-title>Join Game</v-list-item-title>
  </v-list-item>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class JoinGameItem extends Vue {}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.join-game-item {
}
</style>

<template>
  <v-list-item class="create-game-item" to="/create-game">
    <v-list-item-icon>
      <v-icon>mdi-new-box</v-icon>
    </v-list-item-icon>
    <v-list-item-title>Create Game</v-list-item-title>
  </v-list-item>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";

@Component
export default class CreateGameItem extends Vue {
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.create-game-item {
}
</style>

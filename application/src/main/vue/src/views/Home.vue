<template>
  <div class="container">
    <div></div>
    <button class="button" @click="createGame">
      Spiel Erstellen
    </button>
    <button class="button">
      Spiel Beitreten
    </button>
  </div>
</template> 

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import router from 'vue-router'

@Component
export default class CreateGameView extends Vue {
  public createGame(): void {
    this.$router.push('Lobby')
  }
}
</script>

<style>
  .container {
    background-color: var(--color-main);
    display: grid;
    width: 100vw;
    height: 100vh;
    gap: 10px;

    grid-template-rows: 3fr 5em 5em 0px;
  }

  button {
    margin: 0 10px;
    background-color: var(--color-dark);
    color: var(--color-main);
    border: 0;
    border-radius: 20px;
    font-size: 2em;
  }
</style>
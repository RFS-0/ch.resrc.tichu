<script setup lang="ts">
import {useGameStore} from '@/stores/game-store';
import {onMounted, ref} from 'vue';
import {v4 as uuid} from 'uuid';

const gameStore = useGameStore();
const currentGame = gameStore.currentGame;

let displayedCode = ref("");
let generateRandomCode = true;

const animateCode = () => {
  if (!generateRandomCode) {
    return;
  } else {
    displayedCode.value = uuid();
  }
  window.requestAnimationFrame(animateCode);
}

onMounted(() => {
  setTimeout(() => {
    generateRandomCode = false
    displayedCode.value = currentGame.joinCode.value;
  }, 1000);
  window.requestAnimationFrame(animateCode);
})

</script>

<template>
  <div class="container--column">
    <h1 class=" text--md margin--md">join code</h1>
    <div class="text--sm">{{ displayedCode }}</div>
  </div>
</template>

<style scoped>
</style>

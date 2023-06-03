<script setup lang="ts">

import {computed} from 'vue';
import {useGameStore} from '@/stores/game-store';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
  roundNumber: number;
}>();

const gameStore = useGameStore();

const tichu = computed(() => {
  const player = gameStore.currentGame.getPlayerOfTeam(props.teamIndex, props.playerIndex);
  if (!player) {
    throw new Error('Implementation defect: player not found');
  }
  const tichu = gameStore.currentGame.round(props.roundNumber).tichus.get(player.value);
  if (tichu === undefined) {
    return -1;
  }
  return tichu.valueOf();
});

</script>

<template>
  <div v-if="tichu === -1"
       class="tichu text--sm">
    -
  </div>
  <div v-else-if="tichu === 0"
       class="tichu called text--sm">
    T. called
  </div>
  <div v-else-if="tichu === 1"
       class="tichu called text--sm">
    Gr. T. called
  </div>
  <div v-else-if="tichu === -100"
       class="failed text--sm">
    T.
  </div>
  <div v-else-if="tichu === 100"
       class="success text--sm">
    T.
  </div>
  <div v-else-if="tichu === 200"
       class="success text--sm">
    Gr. T.
  </div>
  <div v-else-if="tichu === -200" class="failed text--sm">
    Gr. T.
  </div>

</template>

<style scoped>
.failed {
  text-decoration: underline red;
}

.success {
  text-decoration: underline green;
}
</style>

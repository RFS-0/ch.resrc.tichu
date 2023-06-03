<script setup lang="ts">
import {computed, ref} from 'vue';
import {useGameStore} from '@/stores/game-store';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
}>();

const gameStore = useGameStore();

const cardPoints = ref(0);

const round = computed(() => {
  let selectedRound = gameStore.currentGame.round(gameStore.selectedRoundNumber);
  if (!selectedRound) {
    throw new Error('Implementation defect: round not found');
  }
  return selectedRound;
});

const tichu = computed(() => {
  const playerId = gameStore.currentGame.getPlayerOfTeam(props.teamIndex, props.playerIndex);
  if (!playerId) {
    throw new Error('Implementation defect: player not found');
  }
  return  round.value.tichus.get(playerId.value) ?? -1;
});

const toggleTichu = async () => {
  const playerId = gameStore.currentGame.getPlayerOfTeam(props.teamIndex, props.playerIndex);
  if (!playerId) {
    throw new Error('Implementation defect: player not found');
  }
  const updatedRound = round.value.toggleTichu(playerId.value);
  const updatedGame = gameStore.currentGame.updateRound(updatedRound);
  await gameStore.updateGame(updatedGame);
}
</script>

<template>
  <div v-if="tichu.valueOf() === -1"
       @click="toggleTichu"
       class="tichu none text--sm">
    No Tichu
  </div>
  <div v-else-if="tichu.valueOf() === 0"
       @click="toggleTichu"
       class="tichu called text--sm">
    Tichu called
  </div>
  <div v-else-if="tichu.valueOf() === 1"
       @click="toggleTichu"
       class="tichu called text--sm">
    Grand Tichu called
  </div>
  <div v-else-if="tichu.valueOf() === -100"
       @click="toggleTichu"
       class="tichu failed text--sm">
    Tichu failed
  </div>
  <div v-else-if="tichu.valueOf() === 100"
       @click="toggleTichu"
       class="tichu success text--sm">
    Tichu succeeded
  </div>
  <div v-else-if="tichu.valueOf() === 200"
       @click="toggleTichu"
       class="tichu success text--sm">
    Grand Tichu succeeded
  </div>
  <div v-else-if="tichu.valueOf() === -200"
       @click="toggleTichu"
       class="tichu failed text--sm">
    Grand Tichu failed
  </div>
</template>

<style scoped>
.tichu {
  cursor: pointer;
  width: 100%;
  text-align: center;
}
</style>

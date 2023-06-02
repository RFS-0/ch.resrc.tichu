<script setup lang="ts">


import {computed} from 'vue';
import {useGameStore} from '@/stores/game-store';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
}>();

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;

const gameStore = useGameStore();

const round = computed(() => {
  return gameStore.currentGame.round(gameStore.selectedRoundNumber);
});

const rank = computed(() => {
  const playerId = gameStore.currentGame.getPlayerOfTeam(props.teamIndex, props.playerIndex);
  if (!playerId) {
    throw new Error('Implementation defect: player not found');
  }
  return round.value.ranks.get(playerId.value);
});

const finishRound = async () => {
  const game = gameStore.currentGame;
  const playerId = game.getPlayerOfTeam(props.teamIndex, props.playerIndex);
  if (!playerId) {
    throw new Error('Implementation defect: player not found');
  }
  const updatedGame = game.rankPlayer(round.value.roundNumber, playerId)
  await gameStore.updateGame(updatedGame);
}

const resetRank = async () => {
  const game = gameStore.currentGame;
  const playerId = game.getPlayerOfTeam(props.teamIndex, props.playerIndex);
  const updatedGame = game.resetRank(round.value.roundNumber, playerId)
  await gameStore.updateGame(updatedGame);
}

</script>

<template>
  <div class="text--lg" :class="{'left-player-rank': isLeftTeam, 'right-player-rank': isRightTeam}">
    <div v-if="rank?.valueOf() === 0"
         @click="finishRound"
         class="finish-round">
      ?
    </div>
    <div v-else @click="resetRank" class="rank">
      {{ rank }}.
    </div>
  </div>
</template>

<style scoped>
.left-player-rank {
  grid-area: rank;
  place-self: center;
}

.right-player-rank {
  grid-area: rank;
  place-self: center;
}

.finish-round {
  cursor: pointer;
  background: transparent;
}

.rank {
  cursor: pointer;
}
</style>

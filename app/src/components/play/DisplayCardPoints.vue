<script setup lang="ts">

import {computed} from 'vue';
import {useGameStore} from '@/stores/game-store';

const props = defineProps<{
  teamIndex: number;
  roundNumber: number;
}>();

const gameStore  = useGameStore();

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;

const pointsOfRound = computed(() => {
  const pointsOfRound = gameStore.currentGame.totalPointsOfRound(
      props.roundNumber,
      props.teamIndex
  );
  if (pointsOfRound > 0) {
    return "+" + pointsOfRound;
  } else if (pointsOfRound < 0) {
    return "-" + pointsOfRound;
  } else {
    return pointsOfRound.toString();
  }
})

const pointsUpToRound = computed(() => {
  return gameStore.currentGame.totalPointsUpToRound(
      props.roundNumber,
      props.teamIndex
  );
})

</script>

<template>
  <div class="display-card-points-container">
    <div class="text--sm">
      {{ pointsOfRound }}
    </div>
    <div class="text--md"
         :class="{'left': isLeftTeam, 'right': isRightTeam}">
      {{ pointsUpToRound }}
    </div>
  </div>
</template>

<style scoped>
.display-card-points-container {
  display: grid;
  grid-template-rows: 50% 50%;
  justify-items: center;
  align-items: center;
  height: 100%;
  width: 100%;
}

.left {
  color: var(--color-left-team);
}

.right {
  color: var(--color-right-team);
}
</style>

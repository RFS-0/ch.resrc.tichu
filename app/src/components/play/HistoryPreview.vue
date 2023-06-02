<script setup lang="ts">

import {computed} from 'vue';
import {useGameStore} from '@/stores/game-store';

const gameStore = useGameStore();

const leftTeam = computed(() => {
  return gameStore.currentGame.teams[0];
});

const rightTeam = computed(() => {
  return gameStore.currentGame.teams[1];
});

const totalPointsOfLeftTeam = computed(() => {
  return 100;
});

const totalPointsOfRightTeam = computed(() => {
  return 200;
});

</script>

<template>
  <div class="summary-container">
    <div class="left-team-name text--md">
      <div>
        {{ leftTeam.name }}
      </div>
    </div>
    <div class="left-total-points text--lg">
      <div>
        {{ totalPointsOfLeftTeam }}
      </div>
    </div>
    <div class="colon text--lg">
      <div>:</div>
    </div>
    <div class="right-total-points text--lg">
      <div>
        {{ totalPointsOfRightTeam }}
      </div>
    </div>
    <div class="right-team-name text--md">
      {{ rightTeam.name }}
    </div>
  </div>
</template>

<style scoped>
.summary-container {
  display: grid;
  grid-template-rows: 100%;
  grid-template-columns: 32.5% 17.5% 5% 17.5% 32.5%;
  grid-template-areas:
    "left-team-name left-total-points colon right-total-points right-team-name";
  cursor: pointer;
  background: var(--sys-outline);
  color: var(--sys-on-outline);
  width: 100%;
  height: 100%;
  border-bottom-left-radius: 10vw;
  border-bottom-right-radius: 10vw;
}

.left-team-name {
  grid-area: left-team-name;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 90%;
  color: var(--sys-primary);
  border-radius: 5vh;
}

.left-total-points {
  grid-area: left-total-points;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: var(--sys-tertiary);
}

.colon {
  grid-area: colon;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: var(--sys-primary);
}

.right-total-points {
  grid-area: right-total-points;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: var(--sys-quaternary);
}

.right-team-name {
  grid-area: right-team-name;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 90%;
  color: var(--sys-primary);
}


.even {
  background: var(--sys-outline);
  color: var(--sys-on-outline);
}

.odd {
  background: var(--sys-on-outline);
  color: var(--sys-outline);
}
</style>

<script setup lang="ts">

import {useGameStore} from '@/stores/game-store';
import JoinCode from '@/components/setup/JoinCode.vue';
import SetupTeam from '@/components/setup/SetupTeam.vue';

const gameStore = useGameStore();
const currentGame = gameStore.currentGame;
</script>

<template>
  <div class="configure-game-container">
    <JoinCode/>
    <div class="configure-teams-container">
      <SetupTeam :team-index="0" class="left-team-background left-slide-in"/>
      <SetupTeam :team-index="1" class="right-team-background right-slide-in"/>

      <!--      <configure-team class="left-team-background left-slide-in" :game-id="gameId" :team-id="leftTeam.id"/>-->
      <!--      <configure-team class="right-team-background right-slide-in" :game-id="gameId" :team-id="rightTeam.id"/>-->
      <!--    </div>-->
      <!--    <div class="container&#45;&#45;column">-->
      <!--      <button class="button&#45;&#45;lg-dark"-->
      <!--              v-show="gameConfigured"-->
      <!--              :disabled="!gameConfigured"-->
      <!--              @click="navigateToPlayGame">-->
      <!--        Start-->
      <!--      </button>-->
    </div>
  </div>
</template>

<style scoped>
.configure-game-container {
  display: grid;
  grid-template-rows: 20% 70% 10%;
  grid-template-columns: 100%;
  height: 100%;
  width: 100%;
}

.configure-teams-container {
  display: grid;
  grid-template-columns: 50% 50%;
  grid-template-rows: 100%;
  grid-gap: 2vw;
  height: 100%;
  width: 100%;
}

@keyframes slide-in-team {
  from {
    transform: translateX(var(--slide-in-offset));
  }
  to {
    transform: translateX(0);
  }
}

.left-slide-in {
  --slide-in-offset: -50vw;
  animation-name: slide-in-team;
  animation-duration: 1s;
}

.right-slide-in {
  /* background-color: var(--color-right-team); */
  --slide-in-offset: 50vw;
  animation-name: slide-in-team;
  animation-duration: 1s;
}

</style>

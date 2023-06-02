<script setup lang="ts">

import {useGameStore} from '@/stores/game-store';
import JoinCode from '@/components/setup/JoinCode.vue';
import SetupTeam from '@/components/setup/SetupTeam.vue';
import {useRoute} from 'vue-router';
import {GameId} from 'pointchu.domain';
import {computed} from 'vue';
import router from '@/router';

const route = useRoute();
const gameStore = useGameStore();

await gameStore.loadGame(new GameId({value: route.params.game_id.toString()}));

const gameConfigured = computed(() => {
  const game = gameStore.currentGame;
  return game.numberOfPlayersInGame() === 4;
});

const startGame = async () => {
  await router.push('/play/' + gameStore.currentGame.id.value);
}

</script>

<template>
  <div class="configure-game-container">
    <JoinCode/>
    <div class="configure-teams-container">
      <SetupTeam :team-index="0" class="left-team-background left-slide-in"/>
      <SetupTeam :team-index="1" class="right-team-background right-slide-in"/>
    </div>
    <div class="setup-game-actions">
      <button class="start-game-button text--sm"
              :disabled="!gameConfigured"
              @click="startGame">
        Start
      </button>
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

.start-game-button {
  background-color: var(--sys-outline);
  color: var(--sys-on-outline);
  width: 30vw;
  height: 5vh;
  border-radius: 2vh;
  margin: 1vh
}

.start-game-button:disabled {
  background-color: var(--sys-secondary);
  color: var(--sys-on-secondary);
  cursor: not-allowed;
}

.setup-game-actions {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 1rem;
}

</style>

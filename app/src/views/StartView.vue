<script setup lang="ts">
import {useRouter} from 'vue-router';
import {useGameStore} from '@/stores/game-store';
import {usePlayerStore} from '@/stores/player-store';
import {PlayerId} from 'pointchu.domain';

const router = useRouter()
const gameStore = useGameStore();
const playerStore = usePlayerStore();

async function createGame(): Promise<void> {
  await gameStore.createGame(playerStore.loggedInPlayer.id as PlayerId);
  await router.push('/setup/' + gameStore.currentGame.id.value);
}

const onJoinGame = () => {
  console.log('onJoinGame');
}
</script>

<template>
  <div class="start-view-container">
    <button
        class="button--xl-dark text--lg"
        :disabled="!playerStore.loggedInPlayer.userId"
        @click="createGame"
    >
      Create Game
    </button>
    <button class="button--xl-dark text--lg" @click="onJoinGame">
      Join Game
    </button>
  </div>

</template>

<style scoped>
.start-view-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}
</style>

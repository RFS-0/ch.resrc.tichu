<script setup lang="ts">
import {computed, ref} from 'vue';
import {useGameStore} from '@/stores/game-store';
import HistoryPreview from '@/components/play/HistoryPreview.vue';
import DisplayRound from '@/components/play/DisplayRound.vue';
import UpdateRound from '@/components/play/UpdateRound.vue';
import {GameId} from 'pointchu.domain';
import {useRoute} from 'vue-router';
import router from '@/router';

const route = useRoute();
const gameStore = useGameStore();
await gameStore.loadGame(new GameId({value: route.params.game_id.toString()}));

gameStore.setSelectRoundNumber(gameStore.currentGame.rounds.length);

const historyShown = ref(false);

const gameComplete = computed(() => {
  return gameStore.currentGame.isComplete();
});

const round = computed(() => {
  return gameStore.currentGame.round(gameStore.selectedRoundNumber);
});

const selectedRoundComplete = computed(() => {
  return round.value.isComplete();
});

const latestRoundSelected = computed(() => {
  return gameStore.selectedRoundNumber === gameStore.currentGame.rounds.length;
});

const updateRound = async () => {
  const updatedGame = gameStore.currentGame.finishRound(gameStore.selectedRoundNumber);
  await gameStore.updateGame(updatedGame);
  gameStore.selectedRoundNumber = updatedGame.rounds.length;
}

const finishRound = async () => {
  console.log('finishRound');
  let updatedGame = gameStore.currentGame.finishRound(gameStore.selectedRoundNumber);
  if (!updatedGame.isComplete()) {
    const nextRoundNumber = updatedGame.rounds.length + 1;
    updatedGame = updatedGame.createRound(nextRoundNumber);
  }
  await gameStore.updateGame(updatedGame);
  gameStore.setSelectRoundNumber(gameStore.currentGame.rounds.length);
}

const finishGame = async () => {
  console.log('finishGame');
  await router.push('/statistics/' + gameStore.currentGame.id.value);
}

const showHistory = () => {
  historyShown.value = true;
}

const closeHistory = () => {
  historyShown.value = false;
}

const rounds = computed(() => {
  return gameStore.currentGame.rounds;
});

</script>

<template>
  <div v-if="!historyShown" class="update-container">
    <HistoryPreview class="history-preview"
                    @click.native="showHistory"/>
    <UpdateRound class="update-round"/>
    <div class="container--column update-round-buttons">
      <button v-if="gameComplete"
              @click="finishGame()"
              class="button--lg-dark">
        Finish game
      </button>
      <button v-else-if="selectedRoundComplete && latestRoundSelected"
              @click="finishRound()"
              class="button--lg-dark">
        Finish round
      </button>
      <button v-else-if="selectedRoundComplete"
              @click="updateRound()"
              class="button--lg-dark">
        Update round
      </button>
    </div>
  </div>
  <div v-else class="display-container">
    <div class="display-rounds slide-in-displayed-rounds">
      <div v-for="round in rounds" v-bind:key="round.roundNumber">
        <DisplayRound :round-number="round.roundNumber" @closeHistory="closeHistory()"/>
      </div>
    </div>
    <div class="container--column">
      <button class="button--lg-dark" @click="closeHistory">
        Close history
      </button>
    </div>
  </div>
</template>

<style scoped>
@keyframes slide-in {
  0% {
    transform: translateY(-100%);
  }

  100% {
    transform: translateY(0%);
  }
}

.slide-in-displayed-rounds {
  animation-name: slide-in;
  animation-duration: 0.5s;
}

.update-container {
  display: grid;
  grid-gap: 1vh;
  grid-template-rows: 2fr 13fr 2fr;
  grid-template-areas:
    "history-preview"
    "update-round"
    "update-round-buttons";
  height: 100%;
  width: 100%;
}

.history-preview {
  grid-area: history-preview;
  height: 100%;
  width: 100%;
  border-bottom-left-radius: 10vw;
  border-bottom-right-radius: 10vw;
}

.update-round {
  grid-area: update-round;
  height: 100%;
  width: 100%;
}

.update-round-buttons {
  grid-area: update-round-buttons;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  background-color: var(--color-main);
}

.display-container {
  display: grid;
  grid-template-rows: 13fr 2fr;
  grid-template-areas:
    "display-rounds"
    "display-rounds-buttons";
  height: 100%;
  width: 100%;
}

.display-rounds {
  grid-area: display-rounds;
  height: 100%;
  width: 100%;
  overflow-y: scroll;
}
</style>

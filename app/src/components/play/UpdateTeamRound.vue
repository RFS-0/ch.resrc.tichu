<script setup lang="ts">
import {computed, ref} from 'vue';
import {useGameStore} from '@/stores/game-store';
import {usePlayerStore} from '@/stores/player-store';
import UpdateTichu from '@/components/play/UpdateTichu.vue';
import UpdatePlayerRank from '@/components/play/UpdatePlayerRank.vue';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
}>();

const gameStore = useGameStore();
const playerStore = usePlayerStore();

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;

const cardPoints = ref(0);

const round = computed(() => {
  const game = gameStore.currentGame;
  const round = game.round(gameStore.selectedRoundNumber);
  cardPoints.value = round.cardPointsOfTeam(props.teamIndex);
  return round;
});

const firstPlayer = computed(() => {
  const game = gameStore.currentGame;
  const playerId = game.getPlayerOfTeam(props.teamIndex, 0);
  if (!playerId) {
    throw new Error('Implementation defect: player not found');
  }
  let playerById = playerStore.getPlayerById(playerId);
  if (!playerById) {
    throw new Error('Implementation defect: player not found');
  }
  return playerById;
});

const secondPlayer = computed(() => {
  const game = gameStore.currentGame;
  const playerId = game.getPlayerOfTeam(props.teamIndex, 1);
  if (!playerId) {
    throw new Error('Implementation defect: player not found');
  }
  return playerStore.getPlayerById(playerId);
});

const trackUpdatedCardPoints = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (!target.value) {
    return;
  }
  cardPoints.value = parseInt(target.value);
}

const updateCardPoints = async () => {
  const updatedRound = round.value.updateCardPointsOfTeam(props.teamIndex, cardPoints.value);
  const updatedGame = gameStore.currentGame.updateRound(updatedRound);
  await gameStore.updateGame(updatedGame);
}

const anyMatch = computed(() => {
  const playersOfLeftTeam = Array.from(gameStore.currentGame.teams[0].players.values());
  const playersORightTeam = Array.from(gameStore.currentGame.teams[1].players.values());
  return round.value.isMatch(playersOfLeftTeam) || round.value.isMatch(playersORightTeam);
});
</script>

<template>
  <div class="update-team-round-container"
       :class="{'left-team-areas': isLeftTeam, 'right-team-areas': isRightTeam}">
    <div class="first-player-name text--sm"
         :class="{'left-dark top-right-border': isLeftTeam, 'right-dark top-left-border': isRightTeam}">
      <div>{{ firstPlayer?.value?.name }}</div>
    </div>
    <div class="first-player-tichu">
      <UpdateTichu :team-index="props.teamIndex" :player-index="0"/>
    </div>
    <div class="first-player-rank">
      <UpdatePlayerRank :team-index="props.teamIndex" :player-index="0"/>
    </div>
    <div class="second-player-tichu">
      <UpdateTichu :team-index="props.teamIndex" :player-index="1"/>
    </div>
    <div class="second-player-rank">
      <UpdatePlayerRank :team-index="props.teamIndex" :player-index="1"/>
    </div>
    <div class="second-player-name text--sm"
         :class="{'left-dark bottom-right-border': isLeftTeam, 'right-dark bottom-left-border': isRightTeam}">
      <div>{{ secondPlayer?.value?.name }}</div>
    </div>
    <div class="card-points text--lg">
      <input v-if="round"
             v-on:blur="updateCardPoints"
             :value="cardPoints"
             @input="trackUpdatedCardPoints"
             :disabled="anyMatch"
             class="card-points-input"
             :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': isRightTeam}"
             type="number"
      />
    </div>
  </div>
</template>

<style scoped>
.update-team-round-container {
  display: grid;
  grid-template-rows: 1fr 3fr 3fr 1fr;
  grid-template-columns: 1fr 1fr 1fr;
  color: var(--color-main);
  width: 100%;
  height: 100%;
}

.left-team-areas {
  grid-template-areas:
    "first-player-name first-player-name first-player-name"
    "first-player-tichu first-player-rank card-points"
    "second-player-tichu second-player-rank card-points"
    "second-player-name second-player-name second-player-name";
}

.right-team-areas {
  grid-template-areas:
    "first-player-name first-player-name first-player-name"
    "card-points first-player-rank first-player-tichu"
    "card-points second-player-rank second-player-tichu"
    "second-player-name second-player-name second-player-name";
}

.first-player-name {
  grid-area: first-player-name;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.first-player-tichu {
  place-self: center;
  grid-area: first-player-tichu;
}

.first-player-rank {
  place-self: center;
  grid-area: first-player-rank;
}

.second-player-name {
  grid-area: second-player-name;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.top-left-border {
  border-radius: 5vh 0 0 0;
}

.top-right-border {
  border-radius: 0 5vh 0 0;
}

.bottom-right-border {
  border-radius: 0 0 5vh 0;
}

.bottom-left-border {
  border-radius: 0 0 0 5vh;
}

.second-player-tichu {
  place-self: center;
  grid-area: second-player-tichu;
}

.second-player-rank {
  place-self: center;
  grid-area: second-player-rank;
}

.card-points {
  place-self: center;
  grid-area: card-points;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.left-dark {
  color: var(--sys-primary);
  background: var(--sys-tertiary);
}

.right-dark {
  color: var(--sys-primary);
  background: var(--sys-quaternary);
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type=number] {
  -moz-appearance: textfield;
}

.card-points-input {
  height: 10vh;
  width: 100%;
  margin: auto;
  outline: none;
  text-align: center;
  border: none;
  border-radius: 0.5em;
}
</style>

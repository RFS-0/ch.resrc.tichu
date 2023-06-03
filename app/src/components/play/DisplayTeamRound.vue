<script setup lang="ts">

import {computed} from 'vue';
import {useGameStore} from '@/stores/game-store';
import {usePlayerStore} from '@/stores/player-store';
import {Rank} from 'pointchu.domain';
import DisplayTichu from '@/components/play/DisplayTichu.vue';
import DisplayCardPoints from '@/components/play/DisplayCardPoints.vue';

const props = defineProps<{
  teamIndex: number;
  roundNumber: number;
}>();

const gameStore = useGameStore();
const playerStore = usePlayerStore();

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;

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
  let playerById = playerStore.getPlayerById(playerId);
  if (!playerById) {
    throw new Error('Implementation defect: player not found');
  }
  return playerById;
});

const rank = (playerId: string | undefined) => {
  if (playerId === undefined) {
    return "?";
  }
  const rank = gameStore.currentGame.rounds.find(round => round.roundNumber === props.roundNumber)?.ranks.get(playerId);
  if (rank === undefined || rank.valueOf() === Rank.NONE.valueOf()) {
    return "?";
  }
  return rank.valueOf() + ".";
}
</script>

<template>
  <div class="display-team-round-container"
       :class="{'left-team-areas': isLeftTeam, 'right-team-areas': isRightTeam}">
    <div class="first-player-name text--sm">
      <div>{{ firstPlayer.value?.name }}</div>
    </div>
    <div class="first-player-rank">
      {{ rank(firstPlayer.value?.id?.value) }}
    </div>
    <div class="first-player-tichu text--sm">
      <DisplayTichu :team-index="props.teamIndex" :player-index="0" :round-number="props.roundNumber"/>
    </div>
    <div class="second-player-rank">
      {{ rank(secondPlayer.value?.id?.value) }}
    </div>
    <div class="second-player-tichu text--sm">
      <DisplayTichu :team-index="props.teamIndex" :player-index="1" :round-number="props.roundNumber"/>
    </div>
    <div class="second-player-name text--sm">
      <div>{{ secondPlayer.value?.name }}</div>
    </div>
    <div class="total-points">
      <DisplayCardPoints :round-number="props.roundNumber" :team-index="props.teamIndex"/>
    </div>
  </div>
</template>

<style scoped>
.display-team-round-container {
  cursor: pointer;
  display: grid;
  grid-template-rows: 50% 50%;
  width: 100%;
  height: 100%;
}

.left-team-areas {
  grid-template-columns: 40% 10% 20% 30%;
  grid-template-areas:
    "first-player-name first-player-rank first-player-tichu total-points"
    "second-player-name second-player-rank second-player-tichu total-points";
}

.right-team-areas {
  grid-template-columns: 30% 20% 10% 40%;
  grid-template-areas:
    "total-points first-player-tichu first-player-rank first-player-name"
    "total-points second-player-tichu second-player-rank second-player-name";
}

.first-player-name {
  grid-area: first-player-name;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  word-wrap: break-word;
}

.first-player-tichu {
  place-self: center;
  grid-area: first-player-tichu;
}

.first-player-rank {
  color: var(--color-main);
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
  word-wrap: break-word;
}

.second-player-tichu {
  place-self: center;
  grid-area: second-player-tichu;
}

.second-player-rank {
  color: var(--color-main);
  place-self: center;
  grid-area: second-player-rank;
}

.total-points {
  place-self: center;
  grid-area: total-points;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
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
  margin: auto;
  outline: none;
  text-align: center;
  border: none;
  border-radius: 0.5em;
  width: 100%;
}
</style>

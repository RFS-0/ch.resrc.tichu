<script setup lang="ts">
import {useGameStore} from '@/stores/game-store';
import {computed} from 'vue';
import DisplayTeamRound from '@/components/play/DisplayTeamRound.vue';

const props = defineProps<{
  roundNumber: number;
}>();

const gameStore = useGameStore();
const round = computed(() => {
  return gameStore.currentGame.round(props.roundNumber);
});

const selectRoundNumber = () => {
  gameStore.setSelectRoundNumber(round.value.roundNumber);
}

</script>

<template>
  <div class="display-game-round-container" :class="{'even': round.roundNumberIsEven(), 'odd': !round.roundNumberIsEven()}"
       @click="selectRoundNumber()">
    <DisplayTeamRound :team-index="0" :round-number="props.roundNumber" />
    <div class="colon text--lg">
      :
    </div>
    <DisplayTeamRound :team-index="1" :round-number="props.roundNumber" />
  </div>
</template>

<style scoped>
.display-game-round-container {
  height: 15vh;
  display: grid;
  grid-template-rows: 1fr;
  grid-template-columns: 13fr 1fr 13fr;
}

.colon {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: var(--color-main);
}
</style>

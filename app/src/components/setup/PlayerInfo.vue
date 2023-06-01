<script setup lang="ts">
import {ref} from 'vue';
import PlayerAvatar from '@/components/setup/PlayerAvatar.vue';
import {useGameStore} from '@/stores/game-store';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
}>()

const gameStore = useGameStore();

const isLeftTeam = props.teamIndex === 0;
const team =  gameStore.currentGame.teams[props.teamIndex]
const playerName = ref(team.playerIds[props.playerIndex]);

const removePlayer = () => {
  team.removePlayer(props.playerIndex);
}
</script>

<template>
  <div class="player-info-container">
    <div class="player-name">{{ playerName }}</div>
    <div class="player-avatar">
      <PlayerAvatar/>
    </div>
    <div class="button-container">
      <div class="remove button"
           @click="removePlayer">
        <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 24 24" fill="none">
          <path
              d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z"
              fill="currentColor"/>
        </svg>
      </div>
    </div>
  </div>

</template>

<style scoped>
.player-info-container {
  display: flex;
}

.player-name {
  margin: auto;
}

.player-avatar {
  height: 100%;
}

.button.remove {
  color: var(--sys-primary);
  background: transparent;
  outline: none;
  border: none;
}
</style>

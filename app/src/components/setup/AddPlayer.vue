<script setup lang="ts">
import {ref} from 'vue';
import PlayerAvatar from '@/components/setup/PlayerAvatar.vue';
import {useGameStore} from '@/stores/game-store';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
}>();

const gameStore = useGameStore();

const editingPlayerName = ref(false);
const playerDefined = ref(false);
const playerNameInput = ref<HTMLInputElement | null>(null);
const playerName = ref('click to to add player');

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;

const onAddPlayer = () => {
  editingPlayerName.value = true;
  if (playerName.value === 'click to to add player') {
    playerName.value = '';
  }
  // TODO: fix typing issue
  // @ts-ignore
  playerNameInput.value?.focus()
}

const updatePlayerName = (event: Event) => {
  const target = event.target as HTMLInputElement;
  playerName.value = target.value;
  if (playerName.value === '') {
    playerName.value = 'click to to add player';
  }
}

const onConfirm = () => {
  editingPlayerName.value = false;
  if (playerName.value === '') {
    playerName.value = 'click to to add player';
  }
}
</script>

<template>
  <div class="add-player-container">
    <div v-if="editingPlayerName">
      <input type="text"
             ref="input"
             :class="{'left-team-background': isLeftTeam, 'right-team-background': !isLeftTeam}"
             class="input--text narrow"
             @input="updatePlayerName"/>
      <div class="confirm button"
           @click="onConfirm">
        <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 24 24" fill="none">
          <path d="M9.00016 16.17L4.83016 12L3.41016 13.41L9.00016 19L21.0002 7L19.5902 5.59L9.00016 16.17Z"
                fill="currentColor"/>
        </svg>
      </div>
    </div>
    <div v-else>
      <PlayerAvatar/>
      <div class="button-lg"
           @click="onAddPlayer">
        {{ playerName }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.add-player-container {
  display: flex;
  flex-direction: row;
}
</style>

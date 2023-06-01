<script setup lang="ts">
import {ref} from 'vue';
import {useGameStore} from '@/stores/game-store';
import {Game} from 'pointchu.domain';

const props = defineProps<{
  teamIndex: number;
}>();

const gameStore = useGameStore();
const editingTeamName = ref(false)
const teamNameInput = ref<HTMLInputElement | null>(null);

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;

const onChangeTeamName = () => {
  editingTeamName.value = true;
  // TODO: fix typing issue
  // @ts-ignore
  teamNameInput.value?.focus()
}

const updateTeamName = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  gameStore.currentGame.teams[props.teamIndex].name = target.value;
}

const onConfirm = async () => {
  editingTeamName.value = false;
  await gameStore.updateGame(gameStore.currentGame as Game);
}
</script>

<template>
  <div class="container--column">
    <div v-if="!editingTeamName"
         class="button--sm text--md"
         @click="onChangeTeamName">
      {{ gameStore.currentGame.teams[props.teamIndex].name }}
    </div>
    <div v-else :class="{'input-container--left': isLeftTeam, 'input-container--right': isRightTeam}">
      <input type="text"
             ref="teamNameInput"
             :class="{'left-team-background': isLeftTeam, 'right-team-background': isRightTeam}"
             class="text--sm input--text"
             :value="gameStore.currentGame.teams[props.teamIndex].name"
             @input="updateTeamName"/>
      <div class="confirm button"
           @click="onConfirm">
        <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 24 24" fill="none">
          <path d="M9.00016 16.17L4.83016 12L3.41016 13.41L9.00016 19L21.0002 7L19.5902 5.59L9.00016 16.17Z"
                fill="currentColor"/>
        </svg>
      </div>
    </div>
  </div>
</template>

<style scoped>
.confirm.button {
  background: transparent;
  color: var(--color-main);
  outline: none;
  border: none;
  border-bottom: 1vh;
}
</style>

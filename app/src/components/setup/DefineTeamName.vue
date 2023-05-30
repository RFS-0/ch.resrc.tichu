<script setup lang="ts">
import {ref} from 'vue';

const props = defineProps<{
  teamIndex: number;
}>();

const editingTeamName = ref(false);
const teamNameInput = ref<HTMLInputElement | null>(null);
const teamName = ref('click to edit');

const onChangeTeamName = () => {
  editingTeamName.value = true;
  // TODO: fix typing issue
  // @ts-ignore
  if (teamName.value === 'click to edit') {
    teamName.value = '';
  }
  teamNameInput.value?.focus()
}

const updateTeamName = (event: Event) => {
  const target = event.target as HTMLInputElement;
  teamName.value = target.value;
  if (teamName.value === '') {
    teamName.value = 'click to edit';
  }
}

const onConfirm = () => {
  editingTeamName.value = false;
  if (teamName.value === '') {
    teamName.value = 'click to edit';
  }
}

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;
</script>

<template>
  <div class="container--column">
    <div v-if="!editingTeamName"
         class="button--sm text--md"
         @click="onChangeTeamName">
      {{ teamName }}
    </div>
    <div v-else :class="{'input-container--left': isLeftTeam, 'input-container--right': isRightTeam}">
      <input type="text"
             ref="teamNameInput"
             :class="{'left-team-background': isLeftTeam, 'right-team-background': isRightTeam}"
             class="text--sm input--text"
             :value="teamName"
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
</style>

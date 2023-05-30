<script setup lang="ts">
import {ref} from 'vue';
import PlayerAvatar from '@/components/setup/PlayerAvatar.vue';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
}>()

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;

const playerName = ref('specify player name');

const removePlayer = () => {
  console.log('remove player');
}
</script>

<template>
  <div class="player-info-container">
    <div class="player-name">{{ playerName }}</div>
    <div class="player-avatar">
      <PlayerAvatar />
    </div>
    <div class="button-container" :class="{'reversed': isRightTeam}">
      <v-icon class="button remove"
              @click="removePlayer" x-large>
        mdi-close-outline
      </v-icon>
    </div>
  </div>

</template>

<style scoped>
.player-info-container {
  display: grid;
  grid-template-rows: 30% 40% 30%;
  grid-template-columns: 100%;
  height: 100%;
  width: 100%;
}

.player-name {
  margin: auto;
}

.player-avatar {
  height: 100%;
}

.button.remove { /* increase specifity to override v-icon classes of vuetify by using two classes */
  color: var(--color-main);
  background: transparent;
  outline: none;
  border: none;
}
</style>

<script setup lang="ts">
import PlayerInfo from '@/components/setup/PlayerInfo.vue';
import {type Ref, ref, type UnwrapRef} from 'vue';
import AddPlayer from '@/components/setup/AddPlayer.vue';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
}>()

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;


const shouldAddUserAsPlayer = true;

const playerDefined: Ref<UnwrapRef<boolean>> = ref(false);
</script>

<template>
  <div :class="{'left-team-background': isLeftTeam, 'right-team-background': isRightTeam}">
    <PlayerInfo
        v-if="playerDefined"
        :team-index="props.teamIndex"
        :player-index="props.playerIndex"
    />
    <div v-else
         class="add-player-container">
      <AddPlayer :team-index="props.teamIndex" :player-index="props.playerIndex"/>
    </div>
  </div>
</template>

<style scoped>
.add-player-container {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>

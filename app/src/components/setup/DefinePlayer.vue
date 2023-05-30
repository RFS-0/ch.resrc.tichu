<script setup lang="ts">
import PlayerAvatar from '@/components/setup/PlayerAvatar.vue';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
}>()

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;


const shouldAddUserAsPlayer = true;

</script>

<template>
  <div :class="{'left-team-background': isLeftTeam, 'right-team-background': isRightTeam}">
    <PlayerAvatar :team-index="props.teamIndex" :player-index="props.playerIndex"/>
<!--    <div v-else-if="shouldAddUserAsPlayer" class="container&#45;&#45;column">-->
<!--      <add-user :team-id="teamId"-->
<!--                :position="playerId"-->
<!--                :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': !isLeftTeam}"-->
<!--                class="button&#45;&#45;lg"/>-->
<!--    </div>-->
<!--    <div v-else class="container&#45;&#45;column">-->
<!--      <add-player :is-left-team="isLeftTeam"-->
<!--                  :team-id="teamId"-->
<!--                  :player-id="playerId"-->
<!--                  :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': !isLeftTeam}"-->
<!--                  class="button&#45;&#45;lg">-->
<!--      </add-player>-->
<!--    </div>-->
  </div>
</template>

<style scoped>

</style>

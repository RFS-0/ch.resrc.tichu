<script setup lang="ts">
import DefineTeamName from '@/components/setup/DefineTeamName.vue';
import DefinePlayer from '@/components/setup/DefinePlayer.vue';

const props = defineProps<{
  teamIndex: number;
}>()

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;
</script>

<template>
  <div class="team-container border-radius">
    <DefineTeamName
        :team-index="props.teamIndex"
        :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': isRightTeam}"
        class="border-radius"
    />
    <DefinePlayer
        :team-index="props.teamIndex"
        :player-index="0"
    />
    <DefinePlayer
        :team-index="props.teamIndex"
        :player-index="1"
        class="border-radius"/>
    <!--    <configure-player :team-id="teamId" :player-id="leftPlayerId"/>-->
    <!--    <configure-player class="border-radius" :team-id="teamId" :player-id="rightPlayerId"/>-->
  </div>
</template>

<style scoped>
.team-container {
  display: grid;
  grid-template-rows: 20% 40% 40%;
  grid-template-columns: 100%;
  height: 100%;
  width: 100%;
}
</style>

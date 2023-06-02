<script setup lang="ts">

import UpdateTeamRound from '@/components/play/UpdateTeamRound.vue';
</script>

<template>
  <div class="update-game-round-container">
    <UpdateTeamRound
        class="team left"
        :team-index="0"
        :player-index="0"/>
    <UpdateTeamRound
        :team-index="1"
        :player-index="1"
        class="team right"
    />
  </div>
</template>

<style scoped>
@keyframes slide-in-team {
  from {
    transform: translateX(var(--slide-in-offset));
  }
  to {
    transform: translateX(0);
  }
}

.update-game-round-container {
  height: 100%;
  display: grid;
  grid-gap: 1em;
  grid-template-rows: 1fr;
  grid-template-columns: 1fr 1fr;
  --corner-radius: 5vh;
}

.left {
  border-top-right-radius: var(--corner-radius);
  border-bottom-right-radius: var(--corner-radius);
  --slide-in-offset: -50vw;
  animation-name: slide-in-team;
  animation-duration: 1s;
  background: var(--sys-tertiary);
}

.right {
  border-top-left-radius: var(--corner-radius);
  border-bottom-left-radius: var(--corner-radius);
  /* background-color: var(--color-right-team); */
  --slide-in-offset: 50vw;
  animation-name: slide-in-team;
  animation-duration: 1s;
  background: var(--sys-quaternary);
}
</style>

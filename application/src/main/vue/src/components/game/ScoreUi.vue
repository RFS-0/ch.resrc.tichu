<template>
  <div class="score" :style="colorVars">
    Score
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref, Prop, Emit } from "vue-property-decorator";
import { Round, CardPoints } from '../../types';

@Component
export default class ScoreUi extends Vue {
  @Prop() teamColor!: string;
  @Prop() teamColorDark!: string;
  @Prop() backgroundColor!: string;

  get colorVars() {
    return {
      '--bg-color':this.backgroundColor,
      '--team-color':this.teamColor,
      '--team-color-dark':this.teamColorDark,
      }
  }
}
</script>

<style scoped>
  .score {
    height: 100%;
    width: 100%;
    background-color: var(--team-color-dark)
  }
</style>
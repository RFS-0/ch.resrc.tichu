<template>
  <div class="score" :style="colorVars">
    <ScoreInput
      :color="backgroundColor"
      :backgroundColor="teamColorDark"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref, Prop, Emit } from "vue-property-decorator";
import { Round, CardPoints } from '../../types';
import ScoreInput from './ScoreInput.vue';

@Component({
  components: {
    ScoreInput
  }
})
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
    background-color: var(--team-color-dark);
    display: flex;
    justify-content: center;
  }
</style>
<template>
  <div class="score" :style="colorVars" :class="{left:isLeft}">
    <div class="triangles">
      <svg width="15" viewBox="0 0 30 20">
          <path d="M15 0 l -15 20 l 30 0" fill="black" stroke="transparent" stroke-width="0"/>
      </svg> 
      <svg width="15" viewBox="0 0 30 20">
          <path d="M0 0 l 15 20 l 15 -20" fill="black" stroke="transparent" stroke-width="0"/>
      </svg> 
    </div>
    <ScoreInput
      :color="backgroundColor"
      :backgroundColor="teamColorDark"
      :round="round"
      :teamId="teamId"
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
  @Prop() side!: string;
  @Prop() gameId!: string;
  @Prop() round!: Round;
  @Prop() teamId!: string;

  get isLeft() {
    return this.side == 'left';
  }

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
    justify-content: flex-start;
    flex-direction: row-reverse;
    align-items: center;
  }

  .left {
    flex-direction: row;
  }

  .triangles {
    margin: 0 15px;
    height: 100%;
    display: flex;
    flex-flow: column nowrap;
    justify-content: center;
    align-items: center;
  }
  .triangles>svg {
    margin: 5px 0;
  }

  .triangles>svg>path {
    fill: var(--team-color);
  }


</style>
<template>
  <div class="summary" :style="fgColor">
    <div class="number-team-1">
      <h1
      v-if="!collapsed"
        class="score-change-left"
      >
        {{leftPoints>0?'+':''}}{{leftPoints}}
      </h1>
      <h1 class="score team-1">
        {{leftPoints+leftPointsPrev}}
      </h1>
    </div>
    <div>
      <h1 class="score dots">:</h1>
    </div>
    <div class="number-team-2">
      <h1
        v-if="!collapsed"
        class="score-change-right"
      >
        {{rightPoints>0?'+':''}}{{rightPoints}}
      </h1>
      <h1 class="score team-2">
        {{rightPoints+rightPointsPrev}}
      </h1>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref, Prop, Emit } from "vue-property-decorator";
import { Round, CardPoints } from '../../types';

@Component
export default class RoundScore extends Vue {
  @Prop() round!: Round;
  @Prop() leftTeam!: string;
  @Prop() rightTeam!: string;

  @Prop() leftPointsPrev!: number;
  @Prop() rightPointsPrev!: number;
  @Prop() focused!: boolean;
  @Prop() collapsed!: boolean;
  @Prop() color!: string;
  @Prop() dotsColor!: string;

  get leftPoints(): number {
    const points = this.round.cardPoints.find(
      (points) => points.teamId == this.leftTeam
    )?.value

    if (points)
      return points
    else
      return 0;
  }
  get rightPoints(): number {
    const points = this.round.cardPoints.find(
      (points) => points.teamId == this.rightTeam
    )?.value

    if (points)
      return points
    else
      return 0;
  }

  get fgColor() {
    return {
      '--fg':this.color,
      '--dots-color':this.dotsColor
      }
  }
}
</script>

<style scoped>
  .summary {
    height: 85px;
    /* height: var(--tab-height); */
    width : calc(var(--number) * 2 + var(--dots) + 10px);
    display: grid;
    margin:auto;
    grid-template-columns: var(--number) var(--dots) var(--number);
    gap: 5px;
    /* background-color: crimson; */
  }

  .number-team-1 {
    display:flex;
    flex-direction: column;
    align-items: flex-end;
    justify-content: center;
  }

  .number-team-2 {
    display:flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: center;
    /* background-color: chocolate; */
  }
  .score-change-left {
    color: var(--fg);
    margin-right: 10px;
    /* margin-top: auto; */
    font-size: 20px;
    transition: color 1s;
  }
  .score-change-right {
    color: var(--fg);
    margin-left: 10px;
    /* margin-top: auto; */
    font-size: 20px;
    transition: color 1s;
  }

  .score-change-focus {
    color: var(--color-dark-light)
  }

  .score {
    /* margin: 0 5px auto 5px; */
    /* margin: auto; */
    color: var(--color-main);
    font-size: 50px;
    user-select: none;
    /* margin: auto 5px; */
    /* background-color: cornflowerblue; */
  }

  .dots {
    color: var(--dots-color);
    transform: translateY(19px);
  }

  .team-1 {
    color: var(--color-team-1)
  }

  .team-2 {
    color: var(--color-team-2)
  }
</style>
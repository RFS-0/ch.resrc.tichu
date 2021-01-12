<template>
  <div @click="focus" class="round" :class="{'round-hide':hidden,'round-focus':focused, 'round-collapses':collapsed}">
    <div class="details" :class="{'details-focus':focused}">
      Details
    </div>
    <div class="summary">
      <!-- <h1>{{round.roundNumber}}</h1> -->
      <div></div>
      <div></div>
      <div class="number-team-1">
        <h1
        v-if="!collapsed"
          class="score-change-left"
          :class="{'score-change-focus':focused}"
        >{{pointsLeftTeam>0?'+':''}}{{pointsLeftTeam}}</h1>
        <h1 class="score team-1">{{pointsLeftTeam}}</h1>
      </div>
      <div>
        <h1 class="score dots">:</h1>
      </div>
      <div class="number-team-2">
        <h1
          v-if="!collapsed"
          class="score-change-right"
          :class="{'score-change-focus':focused}"
        >{{pointsLeftTeam>0?'+':''}}{{pointsRightTeam}}</h1>
        <h1 class="score team-2">{{pointsRightTeam}}</h1>
      </div>
      <div></div>
      <div></div>
      <!-- <h1>{{`H:${hidden}, F:${focused}`}}</h1> -->
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref, Prop, Emit } from "vue-property-decorator";
import { Round, CardPoints } from '../../types';

@Component
export default class RoundInfo extends Vue {
  @Prop() leftTeamId!: string;
  @Prop() rightTeamId!: string;
  @Prop() round!: Round;
  @Prop() hidden!: boolean;
  @Prop() focused!: boolean;
  @Prop() collapsed!: boolean;

  @Emit()
  focus() {
    console.log('focus click')
    return 3;
  }

    get pointsLeftTeam() {
      return this.round.cardPoints.find(cp => cp.teamId == this.leftTeamId)?.value;
    }
  get pointsRightTeam() {
    return this.round.cardPoints.find(cp => cp.teamId == this.rightTeamId)?.value;
  }

  // @Emit('focus')
  // @Prop() focus!: boolean;
  //focus = false;
  hide = false;
}
</script>

<style scoped>
  /* .round:nth-child(even)
  {
    --background: var(--color-dark-light);
  } */

  .round {
    height: var(--tab-height);
    flex-shrink: 0;
    transition: all 1s;
    flex-shrink: 0;
    --bg: var(--color-dark);
    --fg: var(--color-dark-light);
    background-color: var(--bg);
    display:flex;
    flex-direction: column;
    justify-content: center;
    /* transition: background-color 1s; */
  }
  
  .round-collapses {
    height: 75px;
    /* background-color: crimson; */
  }

  .round:nth-last-child(even) {
    --bg: var(--color-dark-light);
    --fg: var(--color-dark);
  }

  .round-focus {
    height: calc(var(--window-height));
    background-color: var(--color-dark);
  }

  .round-hide {
    height: 0;
  }

  .summary {
    height: var(--tab-height);
    /* background-color: aqua; */
    width : auto;
    /* display: flex;
    flex-flow: row;
    justify-content: center; */
    display: grid;
    grid-template-columns: 1fr 1fr var(--number) var(--dots) var(--number) 1fr 1fr;
    gap: 5px;
    margin: 0 10px;
    flex-shrink: 0;
    /* margin-top: 5px; */
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

  .details {
    height: 0;
    /* background-color:blueviolet; */
    width : 100%;
    transition: height 1s;
  }

  .details-focus {
    height: calc(var(--window-height) - var(--tab-height))
    /* height: 100%; */
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
    transform: translateY(19px);
  }

  .team-1 {
    color: var(--color-team-1)
  }

  .team-2 {
    color: var(--color-team-2)
  }
</style>
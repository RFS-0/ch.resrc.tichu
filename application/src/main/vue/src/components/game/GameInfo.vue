<template>
  <div @click="$emit('click')" class="info" :class="{'hide':hidden}">
    <!-- <div></div> -->
    <div class="name">
      <h1>
        LUKAS
      </h1>
    </div>
    <div class="name">
      <h1>
        KILIAN
      </h1>
    </div>
    <div class="team team-1">
      <h1>
        DIE KALKÜHLEN KÜHNEN
      </h1>
    </div>
    <div>
    </div>
    <div class="team team-2">
      <h1>
        DIE ??
      </h1>
    </div>
    <div class="name">
      <h1>
        MERLIN
      </h1>
    </div>
    <div class="name">
      <h1>
        SLIVANA
      </h1>
    </div>
    <!-- <div></div> -->
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref, Prop, Emit } from "vue-property-decorator";
import { Round, CardPoints } from '../../types';

@Component
export default class GameInfo extends Vue {
  @Prop() leftTeamId!: string;
  @Prop() rightTeamId!: string;
  @Prop() round!: Round;
  @Prop() hidden!: boolean;
}
</script>

<style scoped>
  .info {
    display: grid;
    height: var(--info-height);
    height: var(--info-height);
    min-height: auto;
    /* height: 200px; */
    /* background-color: cadetblue; */

    transition: height 1s;
    flex-shrink: 0;

    display: grid;
    /* grid-template-columns: 1fr 30px 30px 1fr 30px 30px 1fr; */
    grid-template-columns: 1fr 1fr var(--number) var(--dots) var(--number) 1fr 1fr;
    gap: 5px;
    margin: 10px 15px 10px 15px;
  }
  
  .info>div {
    /* background-color: crimson; */
  }

  .name {
    /* height: 100%; */
    /* background-color: chocolate; */
    display: flex;
    justify-content: center;
    align-content: flex-start;
    align-items: flex-start;
  }

  .name>h1 {
    /* transform: rotate(90deg); */
    writing-mode: vertical-lr;
    /* background-color: cornflowerblue; */
    color: var(--color-dark-light);
    font-size: 18px;
    /* width: 100px; */
    /* height: 100%; */
    /* width: 20px; */
  }

  .team {
    text-align: end;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    font-size: 13px;
  }

  .team>h1 {

  }

  .team-1 {
    text-align: end;
    color: var(--color-team-1-dark);
  }
  .team-2 {
    text-align: start;
    color: var(--color-team-2-dark);
  }

  .hide {
    height: 0;
    margin: 0;
  }
</style>
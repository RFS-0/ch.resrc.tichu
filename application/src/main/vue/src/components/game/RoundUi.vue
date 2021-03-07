<template>
  <div class="round" :style="colorVars">
    <!-- <div class="left">

    </div> -->
    <!-- <div class="right">

    </div> -->
    <RoundTeamUi
      :gameId="gameId"
      :round="round"
      :teamId="leftTeam"
      :teamColor="'var(--color-team-1)'"
      :teamColorDark="'var(--color-team-1-dark)'"
      :backgroundColor="'var(--color-main)'"
      :side="'left'"
    />
    <RoundTeamUi
      :gameId="gameId"
      :round="round"
      :teamId="rightTeam"
      :teamColor="'var(--color-team-2)'"
      :teamColorDark="'var(--color-team-2-dark)'"
      :backgroundColor="'var(--color-main)'"
      :side="'right'"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref, Prop, Emit } from "vue-property-decorator";
import { Round, CardPoints } from '../../types';
import RoundTeamUi from './RoundTeamUi.vue'

@Component({
  components: {
    RoundTeamUi
  }
})
export default class RoundUi extends Vue {
  @Prop() gameId!: string;
  @Prop() round!: Round;
  @Prop() leftTeam!: string;
  @Prop() rightTeam!: string;

  @Prop() bgColor!: string;
  @Prop() inactiveColor!: string;
  @Prop() leftColor!: string;
  @Prop() leftColorDark!: string;
  @Prop() rightColor!: string;
  @Prop() rightColorDark!: string;


  get colorVars() {
    return {
      '--bg-color':this.bgColor,
      '--inactive-color':this.bgColor,
      '--left-color':this.bgColor,
      '--left-color-dark':this.bgColor,
      '--right-color':this.bgColor,
      '--right-color-dark':this.bgColor,
      }
  }
}
</script>

<style scoped>
  .round {
    height:100%;
    width: 100%;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 10px;
  }

  .left {
    background-color: var(--color-team-1);
    height: 100%;
    width: 100%;
  }

  .right {
    background-color: var(--color-team-2);
    height: 100%;
    width: 100%;
  }
</style>
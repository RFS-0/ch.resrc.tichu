<template>
  <div
    class="team"
    :class="{
      'team-left':isLeft,
      'team-right':isRight
    }"
  >
    <PlayerUi
      :round="round"
      :playerId="team.firstPlayer"
      :teamColor="teamColor"
      :teamColorDark="teamColorDark"
      :backgroundColor="backgroundColor"
      :side="side"
    />
    <PlayerUi
      :round="round"
      :playerId="team.secondPlayer"
      :teamColor="teamColor"
      :teamColorDark="teamColorDark"
      :backgroundColor="backgroundColor"
      :side="side"
    />
    <ScoreUi
      :teamColor="teamColor"
      :teamColorDark="teamColorDark"
      :backgroundColor="backgroundColor"
    />
    <!-- <ScoreUi/> -->
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref, Prop, Emit } from "vue-property-decorator";
import { Round, Team } from '../../types';
import PlayerUi from './PlayerUi.vue'
import ScoreUi from './ScoreUi.vue'

@Component({
  components: {
    PlayerUi,
    ScoreUi
  }
})
export default class RoundTeamUi extends Vue {
  @Prop() round!: Round;
  @Prop() teamId!: string;

  @Prop() teamColor!: string;
  @Prop() teamColorDark!: string;
  @Prop() backgroundColor!: string;
  @Prop() side!: string;

  get team() {
    return this.$store.getters.teamById(this.teamId) as Team;
  }
  get isLeft() {
    return this.side == 'left';
  }
  get isRight() {
    return this.side == 'right';
  }
}
</script>

<style scoped>
  .team {
    height: 100%;
    width: 100%;
    display: grid;
    grid-template-rows: 1fr 1fr 85px;
    gap: 10px;

    --border-radius: 40px;

    animation-name: slide-in-team;
    animation-duration: 1s;
  }

  @keyframes slide-in-team {
    from {transform: translateX(var(--slide-in-offset));}
    to {transform: translateX(0);}
  }

  .team-left {
    border-top-right-radius: var(--border-radius);
    border-bottom-right-radius: var(--border-radius);
    
    --slide-in-offset: -50vw;
  }

  .team-right {
    border-top-left-radius: var(--border-radius);
    border-bottom-left-radius: var(--border-radius);

    --slide-in-offset: 50vw;
  }
</style>
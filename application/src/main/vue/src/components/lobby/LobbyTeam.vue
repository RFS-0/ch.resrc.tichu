<template>
  <div class="team-container" :class="{'team-1': isTeam1, 'team-2': isTeam2}">
    <div class="team-info">
      <div class="team-name">
        <h1>{{team.name.toUpperCase()}}</h1>
      </div>
    </div>
    <PlayerSlot
      :playerId="team.firstPlayer"
      :teamId="team.id"
      :left="teamIndex==1"
    />
    <PlayerSlot 
      :playerId="team.secondPlayer"
      :teamId="team.id"
      :left="teamIndex==1"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Emit, Watch } from "vue-property-decorator";
import { Player, Team } from '../../types'
import PlayerAvatar from './PlayerAvatar.vue'
import PlayerSlot from './PlayerSlot.vue'
import 'vue-class-component/hooks'

@Component({
  components: {
    PlayerAvatar,
    PlayerSlot
  }
})
export default class LobbyTeam extends Vue {
  @Prop(String) readonly teamId!: string
  @Prop(Boolean) readonly overlay!: boolean
  get team() {
    return this.$store.getters.teamById(this.teamId) as Team;
  }
  // to determine the color
  @Prop(Number) readonly teamIndex!: number
  get isTeam1() {
    return this.teamIndex <= 1
  }
  get isTeam2() {
    return !this.isTeam1
  }
  private player = {id: 'test-id', name: 'test-player'} as Player
}
</script>

<style scoped>
  .team-container {
    display: grid;
    grid-template-rows: 1fr 3fr 3fr;
    gap: 5px;
    height: 100%;
    max-height: 100%;
    --margin: 15px;
  }

  .team-1 {
    --color-primary: var(--color-team-1);
    --color-secondary: var(--color-team-1-dark);
    --title-margin: var(--margin) 30px var(--margin) var(--margin);
    --title-align: center;
    --flex-direction: row;
  }

  .team-2 {
    --color-primary: var(--color-team-2);
    --color-secondary: var(--color-team-2-dark);
    --title-margin: var(--margin) var(--margin) var(--margin) 30px;
    --title-align: center;
    --flex-direction: row-reverse;
  }

  .team-info {
    background-color: var(--color-secondary);
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .team-name {
    font-size: 23px;
    margin: var(--title-margin);
    text-align: var(--title-align);
    color: var(--color-primary);
  }
</style>
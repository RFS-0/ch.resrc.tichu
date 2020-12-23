<template>
  <div class="team-info">
    <h1 class="team-name">{{team.name}}</h1>
    <PlayerAvatar v-if="team.players.length >= 1" :player="team.players[0]"/>
    <PlayerAvatar v-if="team.players.length >= 2" :player="team.players[1]"/>
    <div  v-if="team.players.length < 2" class="join-slot">
      <div class="team-join-button">
        <h1 class="plus">+</h1>
      </div>
    </div>
    <div  v-if="team.players.length < 1" class="join-slot">
      <div class="team-join-button">
        <h1 class="plus">+</h1>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import { Player, Team } from '../../types'
import PlayerAvatar from './PlayerAvatar.vue'
import 'vue-class-component/hooks'

@Component({
  components: {
    PlayerAvatar,
  }
})
export default class LobbyTeam extends Vue {
  @Prop(Object) readonly team!: Team
  private player = {id: 'test-id', name: 'test-player'} as Player
}
</script>

<style scoped>
  .team-info {
    display: grid;
    grid-template-rows: auto 1fr 1fr;
    height: 100%;
  }

  .team-name {
    margin: auto;
    font-size: 23px;
    margin-top: 20px;
    text-align: center;
  }
  
  .join-slot {
    display: flex;
    flex-flow: column;
    grid-template-rows: min-content;
  }

  .join-slot>h1{
    margin: auto;
    font-size: 23px;
  }

  .team-join-button {
    background-color: var(--color-dark);
    width: 25vw;
    height: 25vw;
    margin: auto;
    border-radius: 12.5vw;
    display: flex;
  }

  .plus {
    color: var(--color-main);
    margin: auto;
    font-size: 90px;
  }
</style>
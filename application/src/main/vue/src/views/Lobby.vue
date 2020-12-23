<template>
  <div class="lobby">
    <JoinCode :code="game.joinCode"/>
    <!-- <div>blah</div> -->
    <div class="teams">
      <div class="team left">
        <LobbyTeam :team="game.teams[0]"/>
      </div>
      <div class="team right">
        <LobbyTeam :team="game.teams[1]"/>
      </div>
    </div>
    <div class="start">
      <button>Start</button>
    </div>
  </div>
</template> 

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import router from 'vue-router'
import JoinCode from '../components/lobby/JoinCode.vue'
import LobbyTeam from '../components/lobby/LobbyTeam.vue'
import { Game, Team, Player } from "../types";

@Component({
  components: {
    JoinCode,
    LobbyTeam
  }
})
export default class CreateGameView extends Vue {

  // Hardcoded Game Data for Testing
  private player1 = {id: 'test-id-player-1', name: 'player-1'} as Player
  private player2 = {id: 'test-id-player-2', name: 'player-2'} as Player
  private player3 = {id: 'test-id-player-3', name: 'player-3'} as Player
  private player4 = {id: 'test-id-player-4', name: 'player-4'} as Player
  private playersTeam1 = [this.player1, this.player2]
  // private playersTeam2 = [this.player3, this.player4]
  private playersTeam2 = [this.player3]
  private team1 = {id: 'test_id-team-1', name: 'team-1', players: this.playersTeam1} as Team
  private team2 = {id: 'test_id-team-2', name: 'team-2', players: this.playersTeam2} as Team
  private teams = [this.team1, this.team2]
  private game = {id: 'test-id-game', teams: this.teams, joinCode:'ABCD'} as Game



  public createGame(): void {
    this.$router.push({})
  }
}
</script>

<style>
  .lobby {
    /* background-color: var(--color-main); */
    display: grid;
    width: 100vw;
    height: 100vh;

    grid-template-rows: auto 1fr 15vh;
    gap: 10px;
  }

  .start {
    /* background-color: aqua; */
    padding: 10px;
    padding-top: 0;
  }

  .teams {
    /* background-color: darkcyan; */
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-gap: 10px;
  }

  .team {
    background-color: darkgoldenrod;
  }

  @keyframes slide-in {
    from {transform: translateX(var(--slide-in-offset));}
    to {transform: translateX(0);}
  }

  .left {
    border-top-right-radius: 40px;
    border-bottom-right-radius: 40px;
    background-color: var(--color-team-1);
    --slide-in-offset: -50vw;
    animation-name: slide-in;
    animation-duration: 1s;
  }

  .right {
    border-top-left-radius: 40px;
    border-bottom-left-radius: 40px;
    background-color: var(--color-team-2);
    --slide-in-offset: 50vw;
    animation-name: slide-in;
    animation-duration: 1s;
  }

  button {
    background-color: var(--color-dark);
    color: var(--color-main);
    border: 0;
    margin: 0;
    border-radius: 20px;
    font-size: 2em;
    width: 100%;
    height: 100%;
  }
</style>
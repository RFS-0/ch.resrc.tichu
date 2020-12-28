<template>
  <div class="lobby">
    <JoinCode :code="game.joinCode"/>
    <!-- <div>joincode</div> -->
    <!-- <div>blah</div> -->
    <div class="teams">
      <div class="team left" :class="{ hide: overlayActive}" @click="closeOverlay">
        <LobbyTeam :teamId="game.leftTeam" :teamIndex="1"/>
      </div>
      <div class="team right" :class="{ hide: overlayActive}" @click="closeOverlay">
        <LobbyTeam :teamId="game.rightTeam" :teamIndex="2"/>
      </div>
      <div class="overlay-container">
        <div class="overlay" :class="{ visible: overlayActive}">
          <!-- <AddPlayerOverlay :team="game.teams[0]" :teamIndex="0" v-on:on-player-added="addPlayer"/> -->
        </div>
      </div>
    </div>
    <div class="start">
      <button @click="toggleOverlay">SPIEL STARTEN</button>
    </div>
  </div>
</template> 

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import router from 'vue-router'
import JoinCode from '../components/lobby/JoinCode.vue'
import LobbyTeam from '../components/lobby/LobbyTeam.vue'
import AddPlayerOverlay from '../components/lobby/AddPlayerOverlay.vue'
import { Game, Team, Player } from "../types";
import {
  State,
  Getter,
  Action,
  Mutation,
  namespace
} from 'vuex-class'


@Component({
  components: {
    JoinCode,
    LobbyTeam,
    AddPlayerOverlay
  }
})
export default class CreateGameView extends Vue {

  // Hardcoded Game Data for Testing
  // private player1 = {id: 'test-id-player-1', name: 'player-1'} as Player
  // private player2 = {id: 'test-id-player-2', name: 'player-2'} as Player
  // private player3 = {id: 'test-id-player-3', name: 'player-3'} as Player
  // private player4 = {id: 'test-id-player-4', name: 'player-4'} as Player
  // private playersTeam1 = [this.player1, this.player2]
  // // private playersTeam2 = [this.player3, this.player4]
  // private playersTeam2 = [this.player3]
  // private team1 = {
  //   id: 'test_id-team-1',
  //   name: 'team grün',
  //   firstPlayer: this.player1.id,
  //   secondPlayer: this.player2.id
  // } as Team
  // private team2 = {id: 'test_id-team-2', name: 'team rot', players: this.playersTeam2} as Team
  // private teams = [this.team1, this.team2]
  // // private game = {
  // //   id: 'test-id-game',
  // //   leftTeam: this.team1.id
  // //   joinCode:'ABCD'
  // // } as Game

  get game(){
    return this.$store.getters.gameById('3466b1bc-821c-42f6-9f83-1d29cb58699e') as Game;
  }

  mounted() {
    console.log(this.game);
  }



  private overlayActive = false;

  public createGame(): void {
    this.$router.push({})
  }

  private toggleOverlay(): void {
    this.overlayActive = !this.overlayActive;
  }

  private closeOverlay(): void {
    this.overlayActive = false;
  }

  private showOverlay(): void {
    this.overlayActive = true;
  }

  private addPlayer(playerName: string): void {
    console.log(playerName);
    // const player = {id:'test-id-new-player', name:playerName} as Player;
    // const players = this.game.teams[1].players;
    // players[1] = player;
    // this.$set(this.game.teams[1], 'players', players);
    // console.log(this.game);
    this.closeOverlay();
  }
}
</script>

<style>
  .lobby {
    /* background-color: var(--color-main); */
    display: grid;
    width: 100%;
    height: 100%;

    grid-template-rows: 2.5fr 12fr 2fr;
    gap: 10px;
  }

  .start {
    /* background-color: aqua; */
    /* padding: 10px; */
    padding-top: 0;
  }

  .teams {
    /* background-color: darkcyan; */
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-gap: 10px;
    max-height: 100%;
    position: relative;
    --corner-radius: 18vw;
  }

  .overlay-container {
    position: absolute;
    display: flex;
    justify-content: center;
    width: 100%;
    height: 100%;
    pointer-events: none;
  }

  .overlay {
    width: 0%;
    height: 100%;
    /* background-color: azure; */
    border-radius: var(--corner-radius);
    /* animation-name: show-overlay;
    animation-duration: 1s; */
    transition: width 1s;
    pointer-events: all;
  }

  .visible {
    width: 70%;
  }

  .team {
    /* background-color: darkgoldenrod; */
    overflow: hidden;
    transition: transform 1s;
  }

  @keyframes slide-in-team {
    from {transform: translateX(var(--slide-in-offset));}
    to {transform: translateX(0);}
  }

  /* @keyframes slide-in-team {
    from {transform: translateX(0);}
    to {transform: translateX(calc(var(--slide-in-offset) * 0.75));}
  } */

  .left {
    border-top-right-radius: var(--corner-radius);
    border-bottom-right-radius: var(--corner-radius);
    /* background-color: var(--color-team-1); */
    --slide-in-offset: -50vw;
    animation-name: slide-in-team;
    animation-duration: 1s;
  }

  .right {
    border-top-left-radius: var(--corner-radius);
    border-bottom-left-radius: var(--corner-radius);
    /* background-color: var(--color-team-2); */
    --slide-in-offset: 50vw;
    animation-name: slide-in-team;
    animation-duration: 1s;
  }

  .hide {
    transform: translateX(calc(var(--slide-in-offset) * 0.75));
  }

  button {
    background-color: var(--color-main-dark);
    color: var(--color-main);
    border: 0;
    margin: 0;
    border-radius: 20px;
    font-family: Lato;
    border-radius: 40px 40px 0 0;
    /* font-size: 2em; */
    font-size: minmax(12vw,8vh);
    width: 100%;
    height: 100%;
  }
</style>
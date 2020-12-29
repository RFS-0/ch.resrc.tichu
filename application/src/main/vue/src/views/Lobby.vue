<template>
  <div class="lobby">
    <JoinCode :code="game.joinCode"/>
    <div class="teams">
      <div class="team left" :class="{ hide: overlayActive}">
        <LobbyTeam
          :teamId="game.leftTeam"
          :teamIndex="1"
        />
      </div>
      <div class="team right" :class="{ hide: overlayActive}">
        <LobbyTeam
          :teamId="game.rightTeam"
          :teamIndex="2"
          :overlay="overlayActive"
        />
      </div>
      <div class="overlay-container">
        <div class="overlay" :class="{ visible: overlayActive}">
          <AddPlayerOverlay :teamIndex="overlayTeam"/>
        </div>
      </div>
    </div>
    <div class="start">
      <button 
        :class="{ready:lobbyFull}"
        @click="startGame"
      >SPIEL STARTEN</button>
    </div>
  </div>
</template> 

<script lang="ts">
import { Component, Prop, PropSync, Vue, Watch } from "vue-property-decorator";
import router from 'vue-router'
import JoinCode from '../components/lobby/JoinCode.vue'
import LobbyTeam from '../components/lobby/LobbyTeam.vue'
import AddPlayerOverlay from '../components/lobby/AddPlayerOverlay.vue'
import { Game, Team, Player } from "../types";
import { State, Getter, Action, Mutation, namespace } from 'vuex-class'


@Component({
  components: {
    JoinCode,
    LobbyTeam,
    AddPlayerOverlay
  }
})
export default class CreateGameView extends Vue {
  get game(){
    return this.$store.getters.gameById('3466b1bc-821c-42f6-9f83-1d29cb58699e') as Game;
  }

  get leftTeam() {
    return this.$store.getters.teamById(this.game.leftTeam) as Team;
  }
  get rightTeam() {
    return this.$store.getters.teamById(this.game.rightTeam) as Team; 
  }

  get lobbyFull() {
    return (
      this.leftTeam.firstPlayer
      && this.leftTeam.secondPlayer
      && this.rightTeam.firstPlayer
      && this.rightTeam.secondPlayer
    );
  }

  mounted() {
    console.log(this.game);
  }

  public startGame(): void {
    if(!this.lobbyFull)
      return
    this.$router.push({name: 'Home'})
  }

  overlayTeam = 0;
  overlayActive = false;

  private toggleOverlay(): void {
    this.overlayActive = !this.overlayActive;
  }
}
</script>

<style>
  .lobby {
    display: grid;
    width: 100%;
    height: 100%;

    grid-template-rows: 2.5fr 12fr 2fr;
    gap: 10px;
  }

  .start {
    padding-top: 0;
  }

  .teams {
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
    border-radius: var(--corner-radius);
    transition: width 1s;
    pointer-events: all;
  }

  .visible {
    width: 70%;
  }

  .team {
    overflow: hidden;
    transition: transform 1s;
  }

  @keyframes slide-in-team {
    from {transform: translateX(var(--slide-in-offset));}
    to {transform: translateX(0);}
  }

  .left {
    border-top-right-radius: var(--corner-radius);
    border-bottom-right-radius: var(--corner-radius);
    --slide-in-offset: -50vw;
    animation-name: slide-in-team;
    animation-duration: 1s;
  }

  .right {
    border-top-left-radius: var(--corner-radius);
    border-bottom-left-radius: var(--corner-radius);
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
    font-size: minmax(12vw,8vh);
    width: 100%;
    height: 100%;
  }

  .ready {
    background-color: var(--color-dark);
  }
</style>
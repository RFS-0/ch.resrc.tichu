<template>
  <div class="player-slot">
    <!-- <div v-if="team.players.length > playerIndex" class="player-info"> -->
    <div v-if="player" class="player-info">
      <div class="player-name">
        <h1>{{player.name.toUpperCase()}}</h1>
      </div>
      <div class="player-info-2">
        <div class="player-buttons">
          <div @click="kick">+</div>
        </div>
        <div class="player-avatar">
          <PlayerAvatar :player="player"/>
        </div>
      </div>
    </div>
    <div v-if="!player" class="join-slot" ref="slot">
      <div @click="toggle" class="team-join-button">
        <h1 class="plus">+</h1>
      </div>
      <div
        :style="height"
        class="add-player"
        :class="{left:left, hidden:hidden}"
      >
        <div class="add-buttons">
          <h1 @click="toggle" class="plus rotate">+</h1>
          <div @click="add" class="add-submit">
             <svg width="60" viewBox="0 0 50 40">
               <path d="M10 20 l 10 10 l 20 -20" fill="transparent" stroke="black" stroke-width="9" stroke-linejoin="round"/>
            </svg> 
          </div>
        </div>
        <div class="add-content">
          <h1 class="add-header">FÜGE EINEN SPIELER<br>HINZU, DER KEINEN<br>ACCOUNT BESITZT</h1>
          <form action="" v-on:submit.prevent="add">
            <input
              :value="nameInput.toUpperCase()"
              @input="nameInput = $event.target.value.toUpperCase()"
              type="text"
              placeholder="NAME"
              class="add-input"
              ref="input"
            >
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Emit, Ref } from "vue-property-decorator";
import { Player, AddPlayerInfo, RemovePlayerInfo } from "../../types";
import PlayerAvatar from './PlayerAvatar.vue'

@Component({
  components: {
    PlayerAvatar
  }
})
export default class PlayerSlot extends Vue {
  @Prop(String) readonly playerId!: string
  @Prop(String) readonly teamId!: string
  @Prop(Boolean) readonly left!: boolean
  @Ref('slot') readonly slot: HTMLDivElement | undefined
  @Ref('overlay') readonly overlay: HTMLDivElement | undefined
  @Ref('input') readonly input: HTMLInputElement | undefined


  nameInput = ""
  height = '--height: 100px'

  hidden = true;

  toggle() {
    this.hidden = !this.hidden
    this.nameInput = ""
  }

  public add() {
    console.log("click")
    if(this.nameInput != "")
    {
      const info = {teamId: this.teamId, name:this.nameInput} as AddPlayerInfo
      this.$store.commit('addplayertoteam', info);
      this.toggle();
    }
    return
  }

  public kick() {
    const info = {teamId: this.teamId, playerId:this.playerId} as RemovePlayerInfo
    this.$store.commit('removeplayerfromteam', info);
  }

  get player() {
    return this.$store.getters.playerById(this.playerId) as Player;
  }

  mounted(){
    this.calculateHeight();
    window.addEventListener('resize', this.calculateHeight);
    window.addEventListener('orientationchange', this.calculateHeight);
  }

  private calculateHeight() {
    const height = (((window.innerHeight - 20) / (2.5 + 12 + 2) * 12) - 10) / (1 + 3 + 3) * 3;
    this.height = `--height: ${height}px`
  }
}
</script>

<style scoped>
 .player-slot {
    background-color: var(--color-primary);
  }

  .player-info {
    width: 100%;
    height: 100%;
    display: grid;
    grid-template-rows: auto 1fr 0px;
    gap: 10px;
  }
  .player-name {
    font-size: 25px;
    color: var(--color-secondary);
    margin: 10px 10px 0 10px;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
  }

  .player-info-2 {
    display: flex;
    flex-direction: var(--flex-direction);
  }

  .player-buttons {
    height: 100%;
    width: 40px;
    display: flex;
    flex-direction: column-reverse;
    transform: translateY(10px);
  }

  .player-buttons>div {
    transform: rotate(45deg);
    color: var(--color-main);
    font-size: 50px;
    user-select: none;
    cursor: pointer;
  }
  .player-buttons>div:hover {
    color: var(--color-dark);
  }

  .player-avatar {
    width: calc(100% - 40px - 30px);
    height: 100%
  }

  .join-slot {
    display: flex;
    flex-flow: column;
    height: 100%;
    --height: 100%;
  }

  .team-join-button {
    background-color: var(--color-secondary);
    width: 25vw;
    height: 25vw;
    margin: auto;
    border-radius: 12.5vw;
    display: flex;
    cursor: pointer;
  }

  .plus {
    color: var(--color-main);
    margin: auto;
    font-size: 60px;
    user-select: none;
    transform:
   translatey(-2px);
  }

  .add-player {
    position:absolute;
    background-color: var(--color-secondary);
    height: var(--height);
    --width: 85vw;
    width: var(--width);
    --offset: calc(calc(var(--width) - 50vw) * -1 - 5px);
    transform: translateX(var(--offset));
    border-top-left-radius: 30px;
    border-bottom-left-radius: 30px;
    display: grid;
    grid-template-columns: 80px 1fr;
    z-index: 10;
    --hidden-offset: 50vw;
    transition: all 0.5s;
  }
  .left {
    --offset: 0;
    --hidden-offset: calc(var(--width) * -1);
    border-radius: 0;
    border-top-right-radius: 30px;
    border-bottom-right-radius: 30px;
  }

  .add-content {
    margin: 20px 20px 20px 0;
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    grid-template-rows: 1fr auto;
    transition: all 1s;
  }

  .add-header {
    color: var(--color-main);
    font-size: 20px;
  }

  .add-input {
    background-color: var(--color-primary);
    border: none;
    font-size: 29px;
    width: 80%;
    font-family: Lato;
    padding: 6px;
    border-radius: 10px;
    text-align: center;
    color: var(--color-main);
  }

  .add-input::placeholder {
    color: var(--color-secondary);
  }

  .add-buttons {
    display: grid;
    grid-template-rows: auto auto;
    margin-left: 20px;
    margin-bottom: 10px;

    transition: all 1s;
  }

  .add-buttons>div {
    cursor: pointer;
    user-select: none;
  }

  .add-submit{
    height: auto;
    cursor: pointer;
    transition: all 1s;
  }

  .rotate {
    transform: rotate(45deg);
    font-size: 80px;
    cursor: pointer;

    transition: all 1s;
  }

  svg>path {
    stroke:var(--color-main);
  }

  .hidden {
    transform: translateX(var(--hidden-offset));
    border-radius: 50px;
    pointer-events: all;
    z-index: 1;
  }
  .hidden>.add-content {
    opacity: 0%;
  }
  .hidden>.add-buttons {
    margin-bottom: 0;
    margin-left: 0;
  }
  .hidden>.add-buttons>.add-submit {
    opacity: 0%;
  }
  .hidden>.add-buttons>.rotate {
    transform: none;
    font-size: 60px;
  }

</style>
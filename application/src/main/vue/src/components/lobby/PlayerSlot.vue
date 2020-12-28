<template>
  <div class="player-slot">
    <!-- <div v-if="team.players.length > playerIndex" class="player-info"> -->
    <div v-if="player" class="player-info">
      <div class="player-name">
        <h1>{{player.name.toUpperCase()}}</h1>
      </div>
      <div class="player-info-2">
        <div class="player-buttons">
          <div>+</div>
        </div>
        <div class="player-avatar">
          <PlayerAvatar :player="player"/>
        </div>
      </div>
    </div>
    <div  v-if="!player" class="join-slot">
      <div @click="add" class="team-join-button add-player">
        <h1 class="plus">+</h1>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Emit } from "vue-property-decorator";
import { Player } from "../../types";
import PlayerAvatar from './PlayerAvatar.vue'

@Component({
  components: {
    PlayerAvatar
  }
})
export default class PlayerSlot extends Vue {
  @Prop(String) readonly playerId!: string

  @Emit('add-player')
  public add() {
    console.log('slot.emit addPlayer')
  }

  get player() {
    return this.$store.getters.playerById(this.playerId) as Player;
  }
}
</script>

<style scoped>
 .player-slot {
    background-color: var(--color-primary);
  }

  .player-info {
    /* background-color:azure ; */
    width: 100%;
    height: 100%;
    display: grid;
    grid-template-rows: auto 1fr 0px;
    gap: 10px;
    /* padding-bottom: 10px; */
  }
  .player-name {
    font-size: 25px;
    color: var(--color-secondary);
    margin: 10px 10px 0 10px;
    /* background-color: burlywood; */
    height: 100%;
    /* overflow: hidden; */
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
  }

  .player-info-2 {
    /* background-color: cadetblue; */
    display: flex;
    flex-direction: var(--flex-direction);
    /* grid-template-columns: 1fr 5fr; */
  }

  .player-buttons {
    /* background-color:chartreuse; */
    height: 100%;
    width: 40px;
    display: flex;
    flex-direction: column-reverse;
    transform: translateY(10px);
  }
  /* X */
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
    /* background-color: coral; */
    width: calc(100% - 40px - 30px);
    height: 100%
  }

  .join-slot {
    display: flex;
    flex-flow: column;
    height: 100%;
    /* position: relative; */
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

  .team-join-button:hover {
    background-color: var(--color-dark);
  }

  .plus {
    color: var(--color-main);
    margin: auto;
    font-size: 60px;
    user-select: none;
    transform: translatey(-2px);
  }
</style>
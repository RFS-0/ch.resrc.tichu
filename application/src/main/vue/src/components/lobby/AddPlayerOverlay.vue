<template>
  <div
    :class="{'team-1':teamIndex == 0, 'team-2':teamIndex == 1}"
    class=" overlay"
  >
    <div class="info">
      <!-- Füge einen Spieler<br>
      hinzu, der keinen<br>
      Account besitzt. -->
      Füge einen<br>
      Spieler hinzu,<br>
      der keinen<br>
      Account besitzt.
    </div>
    <form>
      <input v-model="playerName" placeholder="Name">
    </form>
    <div class="buttons">
      <div class="button" :class="{invalid:playerName == ''}" @click="addPlayer">
        <h1>OK</h1>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Emit} from "vue-property-decorator";
import { Team, Player } from "../../types";

@Component
export default class AddPlayerOverlay extends Vue {
  @Prop(Object) private readonly team!: Team;
  @Prop(Number) private readonly teamIndex!: number;
  private playerName = "";
  private addPlayer(): void {
    if (this.playerName != "") {
      this.onPlayerAdded();
    }
  }

  @Emit()
  onPlayerAdded() {
    return this.playerName;
  }
}
</script>

<style scoped>
  .team-1 {
    --col: var(--color-team-1);
    --col-dark: var(--color-team-1-dark);
  }

  .team-2 {
    --col: var(--color-team-2);
    --col-dark: var(--color-team-2-dark);
  }

  .overlay{
    width: 100%;
    height: 100%;
    background-color: var(--col);
    display: grid;
    grid-template-rows: 2fr 1fr 1fr;
  }

  .info {
    font-size: 25px;
    color: var(--color-main);
    margin-top: 30px;
    text-align: center;
    width: 75vw;
  }

  input {
    background-color: var(--col-dark);
    font-size: 40px;
    width: calc(100% - 60px);
    margin:auto;
    border: none;
    border-radius: 20px;
    font-family: Lato;
    text-align: center;
    color: var(--color-main);
  }

  ::placeholder { /* Chrome, Firefox, Opera, Safari 10.1+ */
    color: var(--col);
    opacity: 1; /* Firefox */
  }

  .buttons {
    /* background-color: cadetblue; */
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-content: center;
  }

  .button {
    /* background-color: coral; */
    margin: 10px;
    display: flex;
    justify-content: center;
    cursor:pointer;
  }

  .button>h1 {
    text-align: center;
    font-size: 30px;
    color: var(--color-main);
  }

  .invalid {
    cursor: default;
  }
  .invalid>h1 {
    color: var(--col-dark);
  }
</style>
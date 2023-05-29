<template>
  <div class="display-game-round-container" :class="{'even': round.roundNumberIsEven(), 'odd': !round.roundNumberIsEven()}"
       @click="selectRoundNumber()">
    <display-team-round :team-id="leftTeamId" :round-number="roundNumber"/>
    <div class="colon text--lg">
      :
    </div>
    <display-team-round :team-id="rightTeamId" :round-number="roundNumber"/>
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Round } from "@/domain/value_objects/round";
import DisplayTeamRound from "@/use_cases/play_game/DisplayTeamRound.vue";
import { Some } from "fp-ts/Option";
import { Component, Prop, Vue } from "vue-property-decorator";
import { mapGetters, mapMutations } from "vuex";

@Component({
  components: {
    DisplayTeamRound,
  },
  computed: {
    ...mapGetters("gameState", {
      game: "game",
    }),
  },
  methods: {
    ...mapMutations("gameState", {
      updateGame: "updateGame",
      updateSelectedRoundNumber: "updateSelectedRoundNumber",
    }),
  },
})
export default class DisplayGameRound extends Vue {
  @Prop() roundNumber!: number;

  game!: Some<Game>;

  updateGame!: (game: Game) => void;
  updateSelectedRoundNumber!: (roundNumber: number) => void;

  get round(): Round {
    const round = this.game.value.rounds.find(
      round => round.roundNumber === this.roundNumber,
    );
    if (!round) {
      throw Error("Invariant violated: selected round cannot be undefined");
    }
    return round;
  }

  get leftTeamId(): string {
    return this.game.value.leftTeam.id;
  }

  get rightTeamId() {
    return this.game.value.rightTeam.id;
  }

  selectRoundNumber() {
    this.updateSelectedRoundNumber(this.roundNumber);
    this.$emit("closeHistory");
  }
}
</script>

<style scoped>
.display-game-round-container {
  height: 15vh;
  display: grid;
  grid-template-rows: 1fr;
  grid-template-columns: 13fr 1fr 13fr;
}

.colon {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: var(--color-main);
}
</style>

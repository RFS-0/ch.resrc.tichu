<template>
  <div v-if="tichu === -1"
       class="tichu text--sm">
    -
  </div>
  <div v-else-if="tichu === 0"
       class="tichu called text--sm">
    T. called
  </div>
  <div v-else-if="tichu === 1"
       class="tichu called text--sm">
    Gr. T. called
  </div>
  <div v-else-if="tichu === -100"
       class="failed text--sm">
    T.
  </div>
  <div v-else-if="tichu === 100"
       class="success text--sm">
    T.
  </div>
  <div v-else-if="tichu === 200"
       class="success text--sm">
    Gr. T.
  </div>
  <div v-else-if="tichu === -200" class="failed text--sm">
    Gr. T.
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Some } from "fp-ts/Option";
import { Component, Prop, Vue } from "vue-property-decorator";
import { mapGetters } from "vuex";

@Component({
  computed: {
    ...mapGetters("gameState", {
      game: "game",
      selectedRound: "selectedRound",
    }),
  },
})
export default class DisplayTichu extends Vue {
  @Prop() teamId!: string;
  @Prop() playerId!: string;
  @Prop() roundNumber!: number;

  game!: Some<Game>;

  get tichu(): number {
    const tichu = this.game.value.rounds.find(round => round.roundNumber === this.roundNumber)?.tichus.get(this.playerId);
    if (tichu === undefined) {
      return -1;
    }
    return tichu.valueOf();
  }
}
</script>

<style scoped>
.failed {
  text-decoration: underline red;
}

.success {
  text-decoration: underline green;
}
</style>

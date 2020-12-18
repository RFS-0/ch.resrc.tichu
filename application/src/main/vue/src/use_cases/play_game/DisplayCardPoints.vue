<template>
  <div class="display-card-points-container">
    <div class="text--sm">
      {{ pointsOfRound }}
    </div>
    <div class="text--md"
         :class="{'left': isLeftTeam, 'right': !isLeftTeam}">
      {{ pointsUpToRound }}
    </div>
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
    }),
  },
})
export default class DisplayCardPoints extends Vue {
  @Prop() teamId!: string;
  @Prop() roundNumber!: number;

  game!: Some<Game>;

  get isLeftTeam(): boolean {
    return this.game.value.isLeftTeam(this.teamId);
  }

  get pointsOfRound(): string {
    const firstPlayerId = this.game.value.teamById(this.teamId).firstPlayer?.id;
    const secondPlayerId = this.game.value.teamById(this.teamId).secondPlayer?.id;
    if (!firstPlayerId) {
      throw new Error("Invariant violated: First player id cannot be undefined");
    }
    if (!secondPlayerId) {
      throw new Error("Invariant violated: Second player id cannot be undefined");
    }
    const pointsOfRound = this.game.value.totalPointsOfRound(
      this.roundNumber,
      this.teamId,
      firstPlayerId,
      secondPlayerId,
    );
    if (pointsOfRound > 0) {
      return "+" + pointsOfRound;
    }
    return pointsOfRound.toString();
  }

  get pointsUpToRound(): number {
    const firstPlayerId = this.game.value.teamById(this.teamId).firstPlayer?.id;
    const secondPlayerId = this.game.value.teamById(this.teamId).secondPlayer?.id;
    if (!firstPlayerId) {
      throw new Error("Invariant violated: First player id cannot be undefined");
    }
    if (!secondPlayerId) {
      throw new Error("Invariant violated: Second player id cannot be undefined");
    }
    return this.game.value.totalPointsUpToRound(
      this.roundNumber,
      this.teamId,
      firstPlayerId,
      secondPlayerId,
    );
  }
}
</script>

<!--suppress CssUnusedSymbol -->
<style scoped>
.display-card-points-container {
  display: grid;
  grid-template-rows: 50% 50%;
  justify-items: center;
  align-items: center;
  height: 100%;
  width: 100%;
}

.left {
  color: var(--color-left-team);
}

.right {
  color: var(--color-right-team);
}
</style>

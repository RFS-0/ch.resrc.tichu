<template>
  <div class="summary-container">
    <div class="left-team-name text--md">
      <div>
        {{ leftTeam.name }}
      </div>
    </div>
    <div class="left-total-points text--lg">
      <div>
        {{ leftTotalPoints }}
      </div>
    </div>
    <div class="colon text--lg">
      <div>:</div>
    </div>
    <div class="right-total-points text--lg">
      <div>
        {{ rightTotalPoints }}
      </div>
    </div>
    <div class="right-team-name text--md">
      {{ rightTeam.name }}
    </div>
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Team } from "@/domain/entities/team";
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
export default class HistoryPreview extends Vue {
  game!: Some<Game>;

  @Prop() historyShown!: boolean;

  get leftTeam(): Team {
    return this.game.value.leftTeam;
  }

  get rightTeam(): Team {
    return this.game.value.rightTeam;
  }

  get leftTotalPoints(): number {
    return this.game.value.totalPointsOfTeam(this.leftTeam.id);
  }

  get rightTotalPoints(): number {
    return this.game.value.totalPointsOfTeam(this.rightTeam.id);
  }
}
</script>

<!--suppress CssUnusedSymbol -->
<style scoped>

.summary-container {
  display: grid;
  grid-template-rows: 100%;
  grid-template-columns: 32.5% 17.5% 5% 17.5% 32.5%;
  grid-template-areas:
    "left-team-name left-total-points colon right-total-points right-team-name";
  cursor: pointer;
  background: var(--color-dark);
  color: var(--color-dark-light);
  width: 100%;
  height: 100%;
  border-bottom-left-radius: 10vw;
  border-bottom-right-radius: 10vw;
}

.left-team-name {
  grid-area: left-team-name;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 90%;
  color: var(--color-main);
  border-radius: 5vh;
}

.left-total-points {
  grid-area: left-total-points;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: var(--color-left-team);
}

.colon {
  grid-area: colon;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: var(--color-main);
}

.right-total-points {
  grid-area: right-total-points;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: var(--color-right-team);
}

.right-team-name {
  grid-area: right-team-name;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 90%;
  color: var(--color-main);
}


.even {
  background: var(--color-dark);
  color: var(--color-dark-light);
}

.odd {
  background: var(--color-dark-light);
  color: var(--color-dark);
}
</style>

<template>
  <div class="display-team-round-container"
       :class="{'left-team-areas': isLeftTeam, 'right-team-areas': !isLeftTeam}">
    <div class="first-player-name text--sm">
      <div>{{ team.firstPlayer.name }}</div>
    </div>
    <div class="first-player-rank">
      {{ rank(team.firstPlayer.id) }}
    </div>
    <div class="first-player-tichu text--sm">
      <display-tichu :team-id="teamId" :player-id="team.firstPlayer.id" :round-number="roundNumber"/>
    </div>
    <div class="second-player-rank">
      {{ rank(team.secondPlayer.id) }}
    </div>
    <div class="second-player-tichu text--sm">
      <display-tichu :team-id="teamId" :player-id="team.secondPlayer.id" :round-number="roundNumber"/>
    </div>
    <div class="second-player-name text--sm">
      <div>{{ team.secondPlayer.name }}</div>
    </div>
    <div class="total-points">
      <display-card-points :team-id="teamId" :round-number="roundNumber"/>
    </div>
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Team } from "@/domain/entities/team";
import { Rank } from "@/domain/value_objects/rank";
import DisplayCardPoints from "@/use_cases/play_game/DisplayCardPoints.vue";
import DisplayTichu from "@/use_cases/play_game/DisplayTichu.vue";
import { Some } from "fp-ts/Option";
import "vue-class-component/hooks";
import { Component, Prop, Vue } from "vue-property-decorator";
import { mapGetters } from "vuex";

@Component({
  components: { DisplayCardPoints, DisplayTichu },
  computed: {
    ...mapGetters("gameState", {
      game: "game",
    }),
  },
})
export default class DisplayTeamRound extends Vue {
  @Prop() teamId!: string;
  @Prop() roundNumber!: number;

  game!: Some<Game>;

  get team(): Team {
    return this.game.value.teamById(this.teamId);
  }

  get isLeftTeam(): boolean {
    return this.game.value.isLeftTeam(this.teamId);
  }

  rank(playerId: string): string {
    const rank = this.game.value.rounds.find(round => round.roundNumber === this.roundNumber)?.ranks.get(playerId);
    if (rank === undefined || rank.valueOf() === Rank.NONE.valueOf()) {
      return "?";
    }
    return rank.valueOf() + ".";
  }
};
</script>

<!--suppress CssUnusedSymbol -->
<style scoped>
.display-team-round-container {
  cursor: pointer;
  display: grid;
  grid-template-rows: 50% 50%;
  width: 100%;
  height: 100%;
}

.left-team-areas {
  grid-template-columns: 40% 10% 20% 30%;
  grid-template-areas:
    "first-player-name first-player-rank first-player-tichu total-points"
    "second-player-name second-player-rank second-player-tichu total-points";
}

.right-team-areas {
  grid-template-columns: 30% 20% 10% 40%;
  grid-template-areas:
    "total-points first-player-tichu first-player-rank first-player-name"
    "total-points second-player-tichu second-player-rank second-player-name";
}

.first-player-name {
  grid-area: first-player-name;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  word-wrap: break-word;
}

.first-player-tichu {
  place-self: center;
  grid-area: first-player-tichu;
}

.first-player-rank {
  color: var(--color-main);
  place-self: center;
  grid-area: first-player-rank;
}

.second-player-name {
  grid-area: second-player-name;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  word-wrap: break-word;
}

.second-player-tichu {
  place-self: center;
  grid-area: second-player-tichu;
}

.second-player-rank {
  color: var(--color-main);
  place-self: center;
  grid-area: second-player-rank;
}

.total-points {
  place-self: center;
  grid-area: total-points;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type=number] {
  -moz-appearance: textfield;
}

.card-points-input {
  height: 10vh;
  margin: auto;
  outline: none;
  text-align: center;
  border: none;
  border-radius: 0.5em;
  width: 100%;
}
</style>

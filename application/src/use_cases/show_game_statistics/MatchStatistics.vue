<template>
  <table>
    <caption>Matches</caption>
    <thead>
    <tr>
      <th>Name</th>
      <th class="math">&num;</th>
      <th class="math">P(match)</th>
      <th class="math">E(pts)</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="matchStatistic in matchStatistics" :key="matchStatistic.teamName">
      <td>{{ matchStatistic.teamName }}</td>
      <td>{{ matchStatistic.numberOfMatches }}</td>
      <td>{{ matchStatistic.probabilityOfMatch }}</td>
      <td>{{ matchStatistic.expectedPointsPerRound }}</td>
    </tr>
    </tbody>
  </table>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Some } from "fp-ts/Option";
import { Bar } from "vue-chartjs";
import { Component, Vue } from "vue-property-decorator";
import { mapGetters } from "vuex";

@Component({
  extends: Bar,
  components: {},
  computed: {
    ...mapGetters("gameState", {
      game: "game",
    }),
  },
})
export default class MatchStatistics extends Vue {
  game!: Some<Game>;

  get matchStatistics(): MatchStatistic[] {
    const matchStatistics = [] as MatchStatistic[];
    this.game.value.teams.forEach(team => {
      const firstPlayerId = team.firstPlayer?.id;
      const secondPlayerId = team.secondPlayer?.id;
      if (!firstPlayerId || !secondPlayerId) {
        throw new Error("Invariant violated: Player id cannot be undefined");
      }
      const totalNumberOfRounds = this.game.value.rounds.length;
      const numberOfMatches = this.game.value.rounds
        .filter(round => round.isMatch(firstPlayerId, secondPlayerId))
        .length;
      const teamName = this.game.value.teamById(team.id).name || "";
      const probabilityOfMatch = Math.round(numberOfMatches / totalNumberOfRounds * 100) / 100;
      const expectedPointsPerRound = Math.round(numberOfMatches * 100 / totalNumberOfRounds * 100) / 100;
      matchStatistics.push(
        {
          teamName,
          numberOfMatches,
          probabilityOfMatch,
          expectedPointsPerRound,
        },
      );
    });
    return matchStatistics;
  }
}

interface MatchStatistic {
  teamName: string;
  numberOfMatches: number;
  probabilityOfMatch: number;
  expectedPointsPerRound: number;
}

</script>

<style scoped>
table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0.2vh;
  margin-left: 2em;
  box-shadow: 0 0 0.5vh var(--color-dark);
  position: relative;
  z-index: 1;
}

th, td {
  padding: 0.5vh;
  text-align: center;
  box-shadow: inset 0.5vh 0.5vh 0.5vh -0.4vh var(--color-dark);
}

thead th {
  background-color: var(--color-dark);
  color: var(--color-main);
}

td:empty {
  box-shadow: none;
}

tbody tr:nth-child(even) td {
  background-color: var(--color-dark);
  color: var(--color-main);
}

caption {
  background-color: var(--color-dark);
  color: var(--color-main);
  padding: 1vh;
  width: 100%;
}

.math {
  font-family: times new roman, serif;
  font-style: italic;
}
</style>

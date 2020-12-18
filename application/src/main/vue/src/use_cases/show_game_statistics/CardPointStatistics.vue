<template>
  <table>
    <caption>Card Points</caption>
    <thead>
    <tr>
      <th>Name</th>
      <th class="math">&Sigma;</th>
      <th class="math">E(pts)</th>
      <th class="math">min</th>
      <th class="math">max</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="cardPointsStatistic in cardPointsStatistics" :key="cardPointsStatistic.teamName">
      <td>{{ cardPointsStatistic.teamName }}</td>
      <td>{{ cardPointsStatistic.totalCardPoints }}</td>
      <td>{{ cardPointsStatistic.expectedCardPointsPerRound }}</td>
      <td>{{ cardPointsStatistic.minCardPoints }}</td>
      <td>{{ cardPointsStatistic.maxCardPoints }}</td>
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
export default class CardPointsStatistics extends Vue {
  game!: Some<Game>;

  get cardPointsStatistics(): CardPointsStatistic[] {
    const matchStatistics = [] as CardPointsStatistic[];
    this.game.value.teams.forEach(team => {
      const firstPlayerId = team.firstPlayer?.id;
      const secondPlayerId = team.secondPlayer?.id;
      if (!firstPlayerId || !secondPlayerId) {
        throw new Error("Invariant violated: Player id cannot be undefined");
      }
      const totalNumberOfRounds = this.game.value.rounds.length;
      const cardPoints = this.game.value.rounds
        .map(round => round.cardPointsOfTeam(team.id));
      const totalCardPoints = cardPoints.reduce((prev, current) => prev + current);
      const teamName = this.game.value.teamById(team.id).name || "";
      const expectedCardPointsPerRound = Math.round(totalCardPoints / totalNumberOfRounds * 100) / 100;
      const minCardPoints = cardPoints.reduce((prev, current) => prev <= current ? prev : current);
      const maxCardPoints = cardPoints.reduce((prev, current) => prev >= current ? prev : current);
      matchStatistics.push(
        {
          teamName,
          totalCardPoints,
          expectedCardPointsPerRound,
          minCardPoints,
          maxCardPoints,
        },
      );
    });
    return matchStatistics;
  }
}

interface CardPointsStatistic {
  teamName: string;
  totalCardPoints: number;
  expectedCardPointsPerRound: number;
  minCardPoints: number;
  maxCardPoints: number;
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

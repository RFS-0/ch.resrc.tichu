<template>
  <table>
    <caption>Tichus</caption>
    <colgroup>
      <col style="width:40%; text-align: left">
      <col style="width:15%">
      <col style="width:15%">
      <col style="width:15%">
      <col style="width:15%">
    </colgroup>
    <thead>
    <tr>
      <th>Name</th>
      <th class="math">&Sigma;</th>
      <th class="math success">&Sigma;</th>
      <th class="math">P(succ.)</th>
      <th class="math">E(pts)</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="tichuStatistic in tichuStatistics" :key="tichuStatistic.name">
      <td>{{ tichuStatistic.name }}</td>
      <td>{{ tichuStatistic.count }}</td>
      <td>{{ tichuStatistic.succeededCount }}</td>
      <td>{{ tichuStatistic.probabilitySuccessful }}</td>
      <td>{{ tichuStatistic.expectedPointsPerRound }}</td>
    </tr>
    </tbody>
  </table>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Tichu } from "@/domain/value_objects/tichu";
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
export default class TichuStatistics extends Vue {
  game!: Some<Game>;

  get tichuStatistics(): TichuStatistic[] {
    const tichuStatistics = [] as TichuStatistic[];
    this.game.value.teams.forEach(team => {
      const firstPlayerId = team.firstPlayer?.id;
      const secondPlayerId = team.secondPlayer?.id;
      if (!firstPlayerId || !secondPlayerId) {
        throw new Error("Invariant violated: Player id cannot be undefined");
      }
      const teamName = this.game.value.teamById(team.id).name || "";
      const totalNumberOfRounds = this.game.value.rounds.length;
      const allTichusOfTeam = this.game.value.rounds
        .map(round => round.tichus.get(firstPlayerId))
        .concat(this.game.value.rounds.map(round => round.tichus.get(secondPlayerId)))
        .filter(
          tichu => !!tichu &&
            (tichu.valueOf() === Tichu.TICHU_FAILED.valueOf() || tichu.valueOf() === Tichu.TICHU_SUCCEEDED.valueOf()),
        ) as Tichu[];
      const succeededCount = allTichusOfTeam.filter(tichu => tichu.valueOf() === Tichu.TICHU_SUCCEEDED).length || 0;
      const failedCound = allTichusOfTeam.filter(tichu => tichu.valueOf() === Tichu.TICHU_FAILED).length || 0;
      const probabilitySuccessful = Math.round((succeededCount / allTichusOfTeam.length || 0) * 100) / 100;
      const expectedPointsPerRound = Math.round(((succeededCount * 100) - (failedCound * -100) / totalNumberOfRounds) * 100) / 100;
      tichuStatistics.push(
        {
          name: teamName,
          count: allTichusOfTeam.length,
          succeededCount,
          probabilitySuccessful,
          expectedPointsPerRound,
        },
      );
    });
    [
      this.game.value.leftTeam.firstPlayer?.id,
      this.game.value.leftTeam.secondPlayer?.id,
      this.game.value.rightTeam.firstPlayer?.id,
      this.game.value.rightTeam.firstPlayer?.id,
    ].forEach(playerId => {
      if (!playerId) {
        return;
      }
      const playerName = this.game.value.playerById(playerId).name || "";
      const totalNumberOfRounds = this.game.value.rounds.length;
      const allTichusOfPlayer = this.game.value.rounds
        .map(round => round.tichus.get(playerId))
        .filter(
          tichu => !!tichu &&
            (tichu.valueOf() === Tichu.TICHU_FAILED.valueOf() || tichu.valueOf() === Tichu.TICHU_SUCCEEDED.valueOf()),
        ) as Tichu[];
      const succeededCount = allTichusOfPlayer.filter(tichu => tichu.valueOf() === Tichu.TICHU_SUCCEEDED).length || 0;
      const probabilitySuccessful = Math.round((succeededCount / allTichusOfPlayer.length || 0) * 100) / 100;
      const expectedPointsPerRound = Math.round((succeededCount * 100 / totalNumberOfRounds) * 100) / 100;
      tichuStatistics.push(
        {
          name: playerName,
          count: allTichusOfPlayer.length,
          succeededCount,
          probabilitySuccessful,
          expectedPointsPerRound,
        },
      );
    });
    return tichuStatistics;
  }
}

interface TichuStatistic {
  name: string;
  count: number;
  succeededCount: number;
  probabilitySuccessful: number;
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
</style>

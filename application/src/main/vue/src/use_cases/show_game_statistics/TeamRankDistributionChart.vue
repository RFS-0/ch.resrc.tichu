<script lang="ts">
import { Game } from "@/domain/entities/game";
import { ChartData, ChartOptions } from "chart.js";
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
export default class TeamRankDistributionChart extends Vue {
  private readonly gameId = this.$route.params.game_id;

  game!: Some<Game>;

  private dataCollection!: ChartData;
  private options = {
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: true,
        },
        gridLines: {
          display: true,
        },
      }],
    },
    responsive: true,
    maintainAspectRatio: false,
    height: 100,
  } as ChartOptions;

  renderChart!: (chartData: Chart.ChartData, options?: Chart.ChartOptions) => void;

  mounted() {
    this.dataCollection = {
      labels: ["1. Rank", "2. Rank", "3. Rank", "4. Rank"],
      datasets: [
        {
          label: this.game.value.leftTeam.name || "Team name missing",
          backgroundColor: "#3EC591",
          pointBackgroundColor: "white",
          borderWidth: 2,
          pointBorderColor: "#3EC591",
          data: this.createHistogramOfRanks(this.game.value.leftTeam.id),
        },
        {
          label: this.game.value.rightTeam.name || "Team name missing",
          backgroundColor: "#D65661",
          pointBackgroundColor: "white",
          borderWidth: 1,
          pointBorderColor: "#D65661",
          data: this.createHistogramOfRanks(this.game.value.rightTeam.id),
        },
      ],
    };
    this.renderChart(this.dataCollection, this.options);
  }

  createHistogramOfRanks(teamId: string): number[] {
    const histogram = [0, 0, 0, 0];
    const firstPlayerId = this.game.value.teamById(teamId).firstPlayer?.id;
    const secondPlayerId = this.game.value.teamById(teamId).secondPlayer?.id;
    if (!firstPlayerId || !secondPlayerId) {
      throw new Error("Invariant violated: Player id cannot be null");
    }
    this.game.value.rounds.forEach(round => {
      const firstPlayerRank = round.ranks.get(firstPlayerId);
      const secondPlayerRank = round.ranks.get(secondPlayerId);
      switch (firstPlayerRank?.valueOf()) {
        case 1:
          histogram[0] = histogram[0] + 1;
          break;
        case 2:
          histogram[1] = histogram[1] + 1;
          break;
        case 3:
          histogram[2] = histogram[2] + 1;
          break;
        case 4:
          histogram[3] = histogram[3] + 1;
          break;
      }
      switch (secondPlayerRank?.valueOf()) {
        case 1:
          histogram[0] = histogram[0] + 1;
          break;
        case 2:
          histogram[1] = histogram[1] + 1;
          break;
        case 3:
          histogram[2] = histogram[2] + 1;
          break;
        case 4:
          histogram[3] = histogram[3] + 1;
          break;
      }
    });
    return histogram;
  }
}
</script>

<style scoped>

</style>

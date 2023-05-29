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
export default class PlayerRankDistributionChart extends Vue {
  private readonly gameId = this.$route.params.game_id;

  game!: Some<Game>;

  private dataCollection!: ChartData;
  private options = {
    legend: {
      position: "bottom",
    },
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
    const leftFirstPlayer = this.game.value.leftTeam.firstPlayer;
    const leftSecondPlayer = this.game.value.leftTeam.secondPlayer;
    const rightFirstPlayer = this.game.value.rightTeam.firstPlayer;
    const rightSecondPlayer = this.game.value.rightTeam.secondPlayer;
    if (!leftFirstPlayer || !leftSecondPlayer || !rightFirstPlayer || !rightSecondPlayer) {
      throw new Error("Invariant violated: Player id cannot be null");
    }
    this.dataCollection = {
      labels: ["1. Rank", "2. Rank", "3. Rank", "4. Rank"],
      datasets: [
        {
          label: leftFirstPlayer.name,
          backgroundColor: "#3EC591",
          borderWidth: 2,
          pointBorderColor: "#3EC591",
          data: this.createHistogramOfRanks(leftFirstPlayer.id),
        },
        {
          label: leftSecondPlayer.name,
          backgroundColor: "#2D8F69",
          borderWidth: 2,
          pointBorderColor: "#2D8F69",
          data: this.createHistogramOfRanks(leftSecondPlayer.id),
        },
        {
          label: rightFirstPlayer.name,
          backgroundColor: "#D65661",
          borderWidth: 1,
          pointBorderColor: "#D65661",
          data: this.createHistogramOfRanks(rightFirstPlayer.id),
        },
        {
          label: rightSecondPlayer.name,
          backgroundColor: "#A03F47",
          borderWidth: 1,
          pointBorderColor: "#A03F47",
          data: this.createHistogramOfRanks(rightFirstPlayer.id),
        },
      ],
    };
    this.renderChart(this.dataCollection, this.options);
  }

  createHistogramOfRanks(playerId: string): number[] {
    const histogram = [0, 0, 0, 0];
    this.game.value.rounds.forEach(round => {
      const playerRank = round.ranks.get(playerId);
      switch (playerRank?.valueOf()) {
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

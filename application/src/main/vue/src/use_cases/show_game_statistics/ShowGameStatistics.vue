<template>
  <div class="game-statistics-container">
    <div class="team-rank-distribution container--column">
      <team-rank-distribution-chart></team-rank-distribution-chart>
    </div>
    <div class="player-rank-distribution container--column">
      <player-rank-distribution-chart></player-rank-distribution-chart>
    </div>
    <div class="game-statistics">
      <div class="container--column-start text--sm">
        <match-statistics class="statistic"/>
        <card-point-statistics class="statistic"/>
        <tichu-statistics class="statistic"/>
        <grand-tichu-statistics class="statistic"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import CardPointStatistics from "@/use_cases/show_game_statistics/CardPointStatistics.vue";
import GrandTichuStatistics from "@/use_cases/show_game_statistics/GrandTichuStatistics.vue";
import MatchStatistics from "@/use_cases/show_game_statistics/MatchStatistics.vue";
import PlayerRankDistributionChart from "@/use_cases/show_game_statistics/PlayerRankDistributionChart.vue";
import TeamRankDistributionChart from "@/use_cases/show_game_statistics/TeamRankDistributionChart.vue";
import TichuStatistics from "@/use_cases/show_game_statistics/TichuStatistics.vue";
import { Component, Vue } from "vue-property-decorator";

@Component({
  components: {
    GrandTichuStatistics,
    TichuStatistics,
    CardPointStatistics,
    MatchStatistics,
    PlayerRankDistributionChart,
    TeamRankDistributionChart,
  },
})
export default class ShowGameStatistics extends Vue {
  private readonly gameId = this.$route.params.game_id;
}
</script>

<style scoped>
.game-statistics-container {
  display: grid;
  grid-template-columns: 100%;
  grid-template-rows: 30% 30% auto;
  width: 100%;
  overflow-y: scroll;
  grid-template-areas:
    "team-rank-distribution"
    "player-rank-distribution"
    "game-statistics";
}

.team-rank-distribution {
  grid-area: team-rank-distribution;
}

.player-rank-distribution {
  grid-area: player-rank-distribution;
}

div {
  height: 100%;
  width: 100%;
}

.statistic {
  margin: 1vh 0 1vh 0;
  width: 100%;
}
</style>

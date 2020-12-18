<template>
  <div class="text--lg" :class="{'left-player-rank': isLeftTeam, 'right-player-rank': !isLeftTeam}">
    <div v-if="rank.valueOf() === 0"
         @click="finishRound"
         class="finish-round">
      ?
    </div>
    <div v-else @click="resetRank" class="rank">
      {{rank}}.
    </div>
  </div>
</template>

<script lang="ts">
import { EndpointRegistry } from '@/configuration/application-configuration';
import { Game, ResetRankEvent, UpdateCardPointsEvent, UpdateRankEvent } from '@/domain/entities/game';
import { Rank } from '@/domain/value_objects/rank';
import { Round } from '@/domain/value_objects/round';
import { Some } from 'fp-ts/Option';
import { merge } from 'rxjs';
import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import { mapGetters, mapMutations } from 'vuex';

@Component({
  computed: {
    ...mapGetters("gameState", {
      game: "game",
      selectedRoundNumber: "selectedRoundNumber",
      selectedRound: "selectedRound",
    }),
  },
  methods: {
    ...mapMutations("gameState", {
      updateGame: "updateGame",
    }),
  },
})
export default class UpdatePlayerRank extends Vue {
  @Inject("endpoints")
  private endpoints!: EndpointRegistry;

  @Prop() gameId!: string;
  @Prop() teamId!: string;
  @Prop() playerId!: string;

  game!: Some<Game>;
  selectedRound!: Round;
  updateGame!: (game: Game) => void;

  get rank(): number {
    const rank = this.selectedRound.ranks.get(this.playerId);
    if (rank === undefined) {
      return Rank.NONE;
    }
    return rank;
  }

  get isLeftTeam(): boolean {
    return this.game.value.isLeftTeam(this.teamId);
  }

  finishRound() {
    this.endpoints.updateRank.send(
      UpdateRankEvent.of(
        this.gameId,
        this.playerId,
        this.selectedRound.roundNumber,
      ),
    ).subscribe(game => {
      this.updateGame(game);
      const firstPlayerId = game.teamById(this.teamId).firstPlayer?.id;
      const secondPlayerId = game.teamById(this.teamId).secondPlayer?.id;
      if (!firstPlayerId || !secondPlayerId) {
        throw new Error("Invariant violated: Player id cannot be null");
      }
      if (this.selectedRound.isMatch(firstPlayerId, secondPlayerId)) {
        this.endpoints.updateCardPoints.send(UpdateCardPointsEvent.of(
          this.gameId,
          this.teamId,
          this.selectedRound.roundNumber,
          100,
          ),
        ).subscribe(game => this.updateGame(game));
      }
    });
  }

  resetRank() {
    merge(
      this.endpoints.updateCardPoints.send(UpdateCardPointsEvent.of(this.gameId, this.teamId, this.selectedRound.roundNumber, 50)),
      this.endpoints.resetRank.send(ResetRankEvent.of(this.gameId, this.playerId, this.selectedRound.roundNumber)),
    ).subscribe(game => this.updateGame(game));
  }
};
</script>

<!--suppress CssUnusedSymbol -->
<style scoped>
.left-player-rank {
  grid-area: rank;
  place-self: center;
}

.right-player-rank {
  grid-area: rank;
  place-self: center;
}

.finish-round {
  cursor: pointer;
  background: transparent;
}

.rank {
  cursor: pointer;
}
</style>

<template>
  <div v-if="!historyShown" class="update-container">
    <history-preview class="history-preview"
                     @click.native="showHistory"/>
    <update-round class="update-round"/>
    <div class="container--column">
      <button v-if="game.value.isComplete()"
              @click="finishGame()"
              class="button--lg-dark">
        Finish game
      </button>
      <button v-else-if="selectedRound.isComplete() && latestRoundSelected"
              @click="finishRound()"
              class="button--lg-dark">
        Finish round
      </button>
      <button v-else-if="selectedRound.isComplete()"
              @click="updateRound()"
              class="button--lg-dark">
        Update round
      </button>
    </div>
  </div>
  <div v-else class="display-container">
    <div class="display-rounds slide-in-displayed-rounds">
      <div v-for="round in game.value.rounds" v-bind:key="round.roundNumber">
        <display-game-round :round-number="round.roundNumber" @closeHistory="closeHistory()"/>
      </div>
    </div>
    <div class="container--column">
      <button class="button--lg-dark" @click="closeHistory">
        Close history
      </button>
    </div>
  </div>
</template>

<script lang="ts">
import { EndpointRegistry } from '@/configuration/application-configuration';
import { FinishGameEvent, FinishRoundEvent, Game, UpdateRoundEvent } from '@/domain/entities/game';
import { Round } from '@/domain/value_objects/round';
import DisplayGameRound from '@/use_cases/play_game/DisplayGameRound.vue';
import HistoryPreview from '@/use_cases/play_game/HistoryPreview.vue';
import UpdateRound from '@/use_cases/play_game/UpdateRound.vue';
import { Some } from 'fp-ts/Option';
import { Component, Inject, Vue } from 'vue-property-decorator';
import { mapGetters, mapMutations } from 'vuex';

@Component({
  components: {
    DisplayGameRound,
    UpdateRound,
    HistoryPreview,
  },
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
      updateSelectedRoundNumber: "updateSelectedRoundNumber",
    }),
  },
})
export default class PlayGame extends Vue {
  @Inject("endpoints")
  private endpoints!: EndpointRegistry;

  private readonly gameId = this.$route.params.game_id;

  game!: Some<Game>;
  selectedRoundNumber!: number;
  selectedRound!: Round;

  updateGame!: (game: Game) => void;
  updateSelectedRoundNumber!: (roundNumber: number) => void;

  private historyShown = false;

  get latestRoundSelected(): boolean {
    return this.selectedRoundNumber === this.game.value.rounds.length;
  }

  showHistory() {
    this.historyShown = true;
  }

  finishRound(): void {
    this.endpoints.finishRound.send(FinishRoundEvent.of(this.gameId, this.selectedRoundNumber)).subscribe(game => {
      this.updateGame(game);
      if (!game.isComplete()) {
        this.updateSelectedRoundNumber(this.selectedRoundNumber + 1);
      }
    });
  }

  updateRound(): void {
    this.endpoints.updateRound.send(UpdateRoundEvent.of(this.gameId, this.selectedRoundNumber)).subscribe(game => {
      this.updateGame(game);
      this.updateSelectedRoundNumber(this.game.value.rounds.length);
    });
  }

  finishGame(): void {
    this.endpoints.finishGame.send(FinishGameEvent.of(this.gameId)).subscribe(
      () => this.$router.replace("/show-game-statistics/" + this.gameId),
    );
  }

  closeHistory() {
    this.historyShown = false;
  }
}
</script>

<!--suppress CssUnusedSymbol -->
<style scoped>
@keyframes slide-in {
  0% {

    -webkit-transform: translateY(-100%);
    transform: translateY(-100%);
  }

  100% {
    -webkit-transform: translateY(0%);
    transform: translateY(0%);
  }
}

.slide-in-displayed-rounds {
  animation-name: slide-in;
  animation-duration: 0.5s;
}

.update-container {
  display: grid;
  grid-gap: 1vh;
  grid-template-rows: 2fr 13fr 2fr;
  grid-template-areas:
    "history-preview"
    "update-round"
    "update-round-buttons";
  height: 100%;
  width: 100%;
}

.history-preview {
  grid-area: history-preview;
  height: 100%;
  width: 100%;
  border-bottom-left-radius: 10vw;
  border-bottom-right-radius: 10vw;
}

.update-round {
  grid-area: update-round;
  height: 100%;
  width: 100%;
}

.update-round-buttons {
  grid-area: update-round-buttons;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  background-color: var(--color-main);
}

.display-container {
  display: grid;
  grid-template-rows: 13fr 2fr;
  grid-template-areas:
    "display-rounds"
    "display-rounds-buttons";
  height: 100%;
  width: 100%;
}

.display-rounds {
  grid-area: display-rounds;
  height: 100%;
  width: 100%;
  overflow-y: scroll;
}
</style>

<template>
  <div class="update-team-round-container"
       :class="{'left-team-areas': isLeftTeam, 'right-team-areas': !isLeftTeam}">
    <div class="first-player-name text--md"
         :class="{'left-dark top-right-border': isLeftTeam, 'right-dark top-left-border': !isLeftTeam}">
      <div>{{ team.firstPlayer.name }}</div>
    </div>
    <div class="first-player-tichu">
      <update-tichu :team-id="teamId" :player-id="team.firstPlayer.id"/>
    </div>
    <div class="first-player-rank">
      <update-player-rank :gameId="gameId" :team-id="teamId" :player-id="team.firstPlayer.id"/>
    </div>
    <div class="second-player-tichu">
      <update-tichu :team-id="teamId" :player-id="team.secondPlayer.id"/>
    </div>
    <div class="second-player-rank">
      <update-player-rank :gameId="gameId" :team-id="teamId" :player-id="team.secondPlayer.id"/>
    </div>
    <div class="second-player-name text--md"
         :class="{'left-dark bottom-right-border': isLeftTeam, 'right-dark bottom-left-border': !isLeftTeam}">
      <div>{{ team.secondPlayer.name }}</div>
    </div>
    <div class="card-points text--lg">
      <input v-model.number="cardPoints"
             v-on:input="updateCardPoints(teamId, parseInt($event.target.value))"
             :disabled="anyMatch"
             class="card-points-input"
             :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': !isLeftTeam}"
             type="number"
             step="5"
             min="-25"
             max="100"/>
    </div>
  </div>
</template>

<script lang="ts">
import { EndpointRegistry } from '@/configuration/application-configuration';
import { Game, UpdateCardPointsEvent } from '@/domain/entities/game';
import { Team } from '@/domain/entities/team';
import { Rank } from '@/domain/value_objects/rank';
import { Round } from '@/domain/value_objects/round';
import UpdatePlayerRank from '@/use_cases/play_game/UpdatePlayerRank.vue';
import UpdateTichu from '@/use_cases/play_game/UpdateTichu.vue';
import { Some } from 'fp-ts/Option';
import 'vue-class-component/hooks';
import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import { mapGetters, mapMutations } from 'vuex';

@Component({
  components: { UpdateTichu, UpdatePlayerRank },
  computed: {
    ...mapGetters("gameState", {
      game: "game",
      selectedRound: "selectedRound",
    }),
  },
  methods: {
    ...mapMutations("gameState", {
      updateGame: "updateGame",
    }),
  },
})
export default class UpdateTeamRound extends Vue {
  @Inject("endpoints")
  private endpoints!: EndpointRegistry;

  @Prop() gameId!: string;
  @Prop() teamId!: string;

  game!: Some<Game>;
  updateGame!: (game: Game) => void;
  selectedRound!: Round;

  get team(): Team {
    return this.game.value.teamById(this.teamId);
  }

  get isLeftTeam(): boolean {
    return this.game.value.isLeftTeam(this.teamId);
  }

  get cardPoints(): number {
    const cardPoints = this.selectedRound.cardPoints.get(this.teamId);
    if (cardPoints === undefined) {
      throw new Error("Invariant violated: card points cannot be undefined");
    }
    return cardPoints;
  }

  set cardPoints(cardPoints: number) {
    // nop
  }

  get anyMatch(): boolean {
    const leftFirstId = this.game.value.leftTeam.firstPlayer?.id;
    const leftSecondId = this.game.value.leftTeam.secondPlayer?.id;
    const rightFirstId = this.game.value.rightTeam.firstPlayer?.id;
    const rightSecondId = this.game.value.rightTeam.secondPlayer?.id;
    if (!leftFirstId || !leftSecondId || !rightFirstId || !rightSecondId) {
      throw new Error("Invariant violated: Player id cannot be null");
    }
    return this.selectedRound.isMatch(leftFirstId, leftSecondId) || this.selectedRound.isMatch(rightFirstId, rightSecondId);
  }

  rank(playerId: string): number {
    const rank = this.selectedRound.ranks.get(playerId);
    if (rank === undefined) {
      return Rank.NONE.valueOf();
    }
    return rank.valueOf();
  }

  updateCardPoints(teamId: string, cardPoints: number) {
    this.endpoints.updateCardPoints.send(
      UpdateCardPointsEvent.of(
        this.game.value.id,
        teamId,
        this.selectedRound.roundNumber,
        cardPoints,
      ),
    ).subscribe(game => this.updateGame(game));
  }
};
</script>

<!--suppress CssUnusedSymbol -->
<style scoped>
.update-team-round-container {
  display: grid;
  grid-template-rows: 1fr 3fr 3fr 1fr;
  grid-template-columns: 1fr 1fr 1fr;
  color: var(--color-main);
  width: 100%;
  height: 100%;
}

.left-team-areas {
  grid-template-areas:
    "first-player-name first-player-name first-player-name"
    "first-player-tichu first-player-rank card-points"
    "second-player-tichu second-player-rank card-points"
    "second-player-name second-player-name second-player-name";
}

.right-team-areas {
  grid-template-areas:
    "first-player-name first-player-name first-player-name"
    "card-points first-player-rank first-player-tichu"
    "card-points second-player-rank second-player-tichu"
    "second-player-name second-player-name second-player-name";
}

.first-player-name {
  grid-area: first-player-name;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.first-player-tichu {
  place-self: center;
  grid-area: first-player-tichu;
}

.first-player-rank {
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
}

.top-left-border {
  border-radius: 5vh 0 0 0;
}

.top-right-border {
  border-radius: 0 5vh 0 0;
}

.bottom-right-border {
  border-radius: 0 0 5vh 0;
}

.bottom-left-border {
  border-radius: 0 0 0 5vh;
}

.second-player-tichu {
  place-self: center;
  grid-area: second-player-tichu;
}

.second-player-rank {
  place-self: center;
  grid-area: second-player-rank;
}

.card-points {
  place-self: center;
  grid-area: card-points;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.left-dark {
  color: var(--color-main);
  background: var(--color-left-team-dark);
}

.right-dark {
  color: var(--color-main);
  background: var(--color-right-team-dark);
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
  width: 100%;
  margin: auto;
  outline: none;
  text-align: center;
  border: none;
  border-radius: 0.5em;
}
</style>

<template>
  <div class="player-info-container">
    <div class="player-name">{{ playerName }}</div>
    <div class="player-avatar">
      <player-avatar playerId="playerId"/>
    </div>
    <div class="button-container" :class="{'reversed': playerIsInRightTeam}">
      <v-icon class="button remove"
              @click="removePlayer" x-large>
        mdi-close-outline
      </v-icon>
    </div>
  </div>
</template>

<script lang="ts">
import { EndpointRegistry } from '@/configuration/application-configuration';
import { Game } from '@/domain/entities/game';
import PlayerAvatar from '@/use_cases/configure_game/PlayerAvatar.vue';
import { Some } from 'fp-ts/Option';
import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import { mapGetters, mapMutations } from 'vuex';
import { RemoveFirstPlayerEvent, RemoveSecondPlayerEvent } from '@/domain/entities/team';

@Component({
  components: {
    PlayerAvatar,
  },
  computed: {
    ...mapGetters("gameState", {
      game: "game",
    }),
  },
  methods: {
    ...mapMutations("gameState", {
      updateGame: "updateGame",
    }),
  },
})
export default class PlayerInfo extends Vue {
  @Inject("endpoints")
  private endpoints!: EndpointRegistry;

  @Prop() readonly playerId!: string;
  @Prop() readonly teamId!: string;

  game!: Some<Game>;
  updateGame!: (game: Game) => void;

  removePlayer() {
    if (this.game.value.teamById(this.teamId).isFirstPlayer(this.playerId)) {
      this.endpoints.removeFirstPlayerFromTeam.send(RemoveFirstPlayerEvent.of(this.teamId)).subscribe(
        updatedGame => this.updateGame(updatedGame),
      );
    } else if (this.game.value.teamById(this.teamId).isSecondPlayer(this.playerId)) {
      this.endpoints.removeSecondPlayerFromTeam.send(RemoveSecondPlayerEvent.of(this.teamId)).subscribe(
        updatedGame => this.updateGame(updatedGame),
      );
    } else {
      throw new Error("Invariant violated: Cannot remove player from unknown position");
    }
  }

  get playerIsInRightTeam(): boolean {
    return !this.game.value.isLeftTeam(this.teamId);
  }

  get playerName() {
    return this.game.value.playerById(this.playerId).name;
  }
};
</script>

<style scoped>
.player-info-container {
  display: grid;
  grid-template-rows: 30% 40% 30%;
  grid-template-columns: 100%;
  height: 100%;
  width: 100%;
}

.player-name {
  margin: auto;
}

.player-avatar {
  height: 100%;
}

.button.remove { /* increase specifity to override v-icon classes of vuetify by using two classes */
  color: var(--color-main);
  background: transparent;
  outline: none;
  border: none;
}
</style>

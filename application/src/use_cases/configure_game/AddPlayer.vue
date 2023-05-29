<template>
  <div class="container--column">
    <div v-if="!showInput"
         class="button-lg"
         @click="onAddPlayer">
      {{ playerName }}
    </div>
    <div v-else :class="{'input-container--left': isLeftTeam, 'input-container--right': !isLeftTeam}">
      <input type="text"
             ref="input"
             :class="{'left-team-background': isLeftTeam, 'right-team-background': !isLeftTeam}"
             class="input--text narrow"
             @input="playerNameInput = $event.target.value"/>
      <v-icon class="confirm button"
              :disabled="playerNameInput.length < 1"
              @click="onConfirm">
        mdi-check-outline
      </v-icon>
    </div>
  </div>
</template>

<script lang="ts">
import { EndpointRegistry } from '@/configuration/application-configuration';
import { Game } from '@/domain/entities/game';
import { AddPlayerEvent } from '@/domain/entities/team';
import { Some } from 'fp-ts/Option';
import { Component, Inject, Prop, Ref, Vue } from 'vue-property-decorator';
import { mapGetters, mapMutations } from 'vuex';

@Component({
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
export default class AddPlayer extends Vue {
  @Inject("endpoints")
  private endpoints!: EndpointRegistry;

  @Ref("input") input!: HTMLInputElement;

  @Prop() readonly teamId!: string;
  @Prop() readonly playerId!: string;

  game!: Some<Game>;
  updateGame!: (game: Game) => void;

  private showInput = false;
  private playerNameInput = "";

  get isLeftTeam(): boolean {
    return this.game.value.isLeftTeam(this.teamId);
  }

  get playerName() {
    return this.game.value.findPlayerById(this.playerId) || "Click to add player";
  }

  onAddPlayer() {
    this.showInput = true;
    setTimeout(() => this.input.focus(), 0);
  }

  onConfirm() {
    this.showInput = false;
    if (this.playerId === "add-first-player") {
      this.endpoints.addFirstPlayerToTeam.send(AddPlayerEvent.ofPlayer(this.teamId, this.playerNameInput)).subscribe(
        updatedGame => this.updateGame(updatedGame),
      );
    } else if (this.playerId === "add-second-player") {
      this.endpoints.addSecondPlayerToTeam.send(AddPlayerEvent.ofPlayer(this.teamId, this.playerNameInput)).subscribe(
        updatedGame => this.updateGame(updatedGame),
      );
    } else {
      throw new Error("Invariant violated: Cannot add player to unknown position");
    }
  }
};
</script>

<style scoped>
</style>

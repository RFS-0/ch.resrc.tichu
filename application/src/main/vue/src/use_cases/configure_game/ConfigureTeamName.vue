<template>
  <div class="container--column">
    <div v-if="!showInput"
         class="button-sm text--md"
         @click="onChangeTeamName">
      {{ team.name || "click to specify name" }}
    </div>
    <div v-else :class="{'input-container--left': isLeftTeam, 'input-container--right': !isLeftTeam}">
      <input type="text"
             ref="input"
             :class="{'left-team-background': isLeftTeam, 'right-team-background': !isLeftTeam}"
             class="input--text"
             @input="teamNameInput = $event.target.value"/>
      <v-icon class="confirm button"
              @click="onConfirm">
        mdi-check-outline
      </v-icon>
    </div>
  </div>
</template>

<script lang="ts">
import { EndpointRegistry } from '@/configuration/application-configuration';
import { Game } from '@/domain/entities/game';
import { Team, UpdateTeamNameEvent } from '@/domain/entities/team';
import { Some } from 'fp-ts/Option';
import 'vue-class-component/hooks';
import { Component, Inject, Prop, Ref, Vue } from 'vue-property-decorator';
import { mapGetters, mapMutations } from 'vuex';

@Component({
  computed: {
    ...mapGetters('gameState', {
      game: 'game',
    }),
  },
  methods: {
    ...mapMutations('gameState', {
      updateGame: 'updateGame',
    }),
  },
})
export default class ConfigureTeamName extends Vue {
  @Inject('endpoints')
  private endpoints!: EndpointRegistry;

  @Ref('input') input!: HTMLInputElement;

  @Prop() teamId!: string;

  game!: Some<Game>;
  updateGame!: (game: Game) => void;

  private showInput = false;
  private teamNameInput = '';

  get team(): Team {
    return this.game.value.teamById(this.teamId);
  }

  get isLeftTeam(): boolean {
    return this.game.value.isLeftTeam(this.teamId);
  }

  onChangeTeamName() {
    this.showInput = true;
    setTimeout(() => this.input.focus(), 0);
  }

  onConfirm() {
    this.showInput = false;
    this.endpoints.updateTeamName.send(
      UpdateTeamNameEvent.of(this.game.value.id, this.team.id, this.teamNameInput))
      .subscribe(updatedGame => this.updateGame(updatedGame));
  }
};
</script>

<style scoped>
</style>

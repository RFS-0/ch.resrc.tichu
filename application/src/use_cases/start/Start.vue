<template>
  <div class="container--column">
    <button class="button--xl-dark text--lg" @click="onCreateGame">
      Create Game
    </button>
    <button class="button--xl-dark text--lg">
      Join Game
    </button>
  </div>
</template>

<script lang="ts">
import { EndpointRegistry } from '@/configuration/application-configuration';
import { CreateGameEvent, Game } from '@/domain/entities/game';
import { User } from '@/domain/entities/user';
import { Some } from 'fp-ts/Option';
import { Component, Inject, Vue } from 'vue-property-decorator';
import { mapGetters, mapMutations } from 'vuex';

@Component({
    methods: {
      ...mapMutations('gameState', {
        updateGame: 'updateGame',
      }),
    },
    computed: {
      ...mapGetters('userState', {
        user: 'user',
      }),
    },
  },
)
export default class Start extends Vue {
  @Inject('endpoints')
  private endpoints!: EndpointRegistry;

  updateGame!: (game: Game) => void;
  user!: Some<User>;

  onCreateGame(): void {
    this.endpoints.createGame.send(CreateGameEvent.of(this.user.value.id)).subscribe(gameDto => {
        this.updateGame(Game.fromDto(gameDto));
        this.$router.replace('/configure-game/' + gameDto.id);
      },
      () => console.error('Could not create game'),
    );
  }
}
</script>

<style scoped>
</style>

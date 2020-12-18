<template>
  <div class="configure-game-container">
    <join-code :gameId="gameId"/>
    <div class="configure-teams-container">
      <configure-team class="left-team-background left-slide-in" :game-id="gameId" :team-id="leftTeam.id"/>
      <configure-team class="right-team-background right-slide-in" :game-id="gameId" :team-id="rightTeam.id"/>
    </div>
    <div class="container--column">
      <button class="button--lg-dark"
              v-show="gameConfigured"
              :disabled="!gameConfigured"
              @click="navigateToPlayGame">
        Start
      </button>
    </div>
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Team } from "@/domain/entities/team";
import ConfigureTeam from "@/use_cases/configure_game/ConfigureTeam.vue";
import JoinCode from "@/use_cases/configure_game/JoinCode.vue";
import { Some } from "fp-ts/Option";
import { Component, Vue } from "vue-property-decorator";
import { mapGetters, mapMutations } from "vuex";

@Component({
  components: {
    JoinCode,
    ConfigureTeam,
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
export default class ConfigureGame extends Vue {
  private readonly gameId = this.$route.params.game_id;

  game!: Some<Game>;
  updateGame!: (game: Game) => void;

  get leftTeam(): Team {
    return this.game.value.leftTeam;
  }

  get rightTeam(): Team {
    return this.game.value.rightTeam;
  }

  get gameConfigured() {
    return this.leftTeam.isComplete() && this.rightTeam.isComplete();
  }

  navigateToPlayGame(): void {
    this.updateGame(this.game.value.createRound(1));
    this.$router.replace("/play-game/" + this.gameId);
  }
}
</script>
<style scoped>
@keyframes slide-in-team {
  from {
    transform: translateX(var(--slide-in-offset));
  }
  to {
    transform: translateX(0);
  }
}

.left-slide-in {
  --slide-in-offset: -50vw;
  animation-name: slide-in-team;
  animation-duration: 1s;
}

.right-slide-in {
  /* background-color: var(--color-right-team); */
  --slide-in-offset: 50vw;
  animation-name: slide-in-team;
  animation-duration: 1s;
}

.configure-game-container {
  display: grid;
  grid-template-rows: 20% 70% 10%;
  grid-template-columns: 100%;
  height: 100%;
  width: 100%;
}

.configure-teams-container {
  display: grid;
  grid-template-columns: 50% 50%;
  grid-template-rows: 100%;
  grid-gap: 2vw;
  height: 100%;
  width: 100%;
}
</style>

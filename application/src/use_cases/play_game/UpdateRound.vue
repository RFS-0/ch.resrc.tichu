<template>
  <div class="update-game-round-container">
    <update-team-round :gameId="gameId" :team-id="leftTeamId" class="team left"/>
    <update-team-round :gameId="gameId" :team-id="rightTeamId" class="team right"/>
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import UpdateTeamRound from "@/use_cases/play_game/UpdateTeamRound.vue";
import { Some } from "fp-ts/Option";
import { Component, Vue } from "vue-property-decorator";
import { mapGetters } from "vuex";

@Component({
  components: {
    UpdateTeamRound,
  },
  computed: {
    ...mapGetters("gameState", {
      game: "game",
    }),
  },
})
export default class UpdateRound extends Vue {
  private readonly gameId = this.$route.params.game_id;

  game!: Some<Game>;

  get leftTeamId(): string {
    return this.game.value.leftTeam.id;
  }

  get rightTeamId() {
    return this.game.value.rightTeam.id;
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

.update-game-round-container {
  height: 100%;
  display: grid;
  grid-gap: 1em;
  grid-template-rows: 1fr;
  grid-template-columns: 1fr 1fr;
  --corner-radius: 5vh;
}

.left {
  border-top-right-radius: var(--corner-radius);
  border-bottom-right-radius: var(--corner-radius);
  --slide-in-offset: -50vw;
  animation-name: slide-in-team;
  animation-duration: 1s;
  background: var(--color-left-team);
}

.right {
  border-top-left-radius: var(--corner-radius);
  border-bottom-left-radius: var(--corner-radius);
  /* background-color: var(--color-right-team); */
  --slide-in-offset: 50vw;
  animation-name: slide-in-team;
  animation-duration: 1s;
  background: var(--color-right-team);
}
</style>

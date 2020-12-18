<template>
  <div class="team-container border-radius">
    <configure-team-name :team-id="teamId"
                         :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': !isLeftTeam}"
                         class="border-radius"/>
    <configure-player :team-id="teamId" :player-id="leftPlayerId"/>
    <configure-player class="border-radius" :team-id="teamId" :player-id="rightPlayerId"/>
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Team } from "@/domain/entities/team";
import ConfigurePlayer from "@/use_cases/configure_game/ConfigurePlayer.vue";
import ConfigureTeamName from "@/use_cases/configure_game/ConfigureTeamName.vue";
import { Some } from "fp-ts/Option";
import "vue-class-component/hooks";
import { Component, Prop, Vue } from "vue-property-decorator";
import { mapGetters } from "vuex";

@Component({
  components: { ConfigureTeamName, ConfigurePlayer },
  computed: {
    ...mapGetters("gameState", {
      game: "game",
    }),
  },
})
export default class ConfigureTeam extends Vue {
  @Prop() readonly gameId!: string;
  @Prop() readonly teamId!: string;

  game!: Some<Game>;

  get team(): Team {
    return this.game.value.teamById(this.teamId);
  }

  get isLeftTeam(): boolean {
    return this.game.value.isLeftTeam(this.teamId);
  }

  get leftPlayerId(): string {
    return this.team.firstPlayer?.id || "add-first-player";
  }

  get rightPlayerId(): string {
    return this.team.secondPlayer?.id || "add-second-player";
  }
};
</script>

<style scoped>
.team-container {
  display: grid;
  grid-template-rows: 20% 40% 40%;
  grid-template-columns: 100%;
  height: 100%;
  width: 100%;
}
</style>

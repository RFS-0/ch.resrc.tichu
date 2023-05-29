<template>
  <div :class="{'left-team-background': isLeftTeam, 'right-team-background': !isLeftTeam}">
    <player-info v-if="player" :team-id="teamId" :player-id="player.id"/>
    <div v-else-if="shouldAddUserAsPlayer" class="container--column">
      <add-user :team-id="teamId"
                :position="playerId"
                :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': !isLeftTeam}"
                class="button--lg"/>
    </div>
    <div v-else class="container--column">
      <add-player :is-left-team="isLeftTeam"
                  :team-id="teamId"
                  :player-id="playerId"
                  :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': !isLeftTeam}"
                  class="button--lg">
      </add-player>
    </div>
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Player } from "@/domain/entities/player";
import { User } from "@/domain/entities/user";
import AddPlayer from "@/use_cases/configure_game/AddPlayer.vue";
import AddUser from "@/use_cases/configure_game/AddUser.vue";
import PlayerInfo from "@/use_cases/configure_game/PlayerInfo.vue";
import { isNone, Option, Some } from "fp-ts/Option";
import { Component, Prop, Vue } from "vue-property-decorator";
import { mapGetters, mapMutations } from "vuex";

@Component({
  components: {
    AddPlayer,
    AddUser,
    PlayerInfo,
  },
  computed: {
    ...mapGetters("userState", {
      user: "user",
    }),
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
export default class ConfigurePlayer extends Vue {
  @Prop() readonly teamId!: string;
  @Prop() readonly playerId!: string;

  user!: Option<User>;
  game!: Some<Game>;
  updateGame!: (game: Game) => void;

  get player(): Player | undefined {
    return this.game.value.findPlayerById(this.playerId);
  }

  get isLeftTeam(): boolean {
    return this.game.value.isLeftTeam(this.teamId);
  }

  get shouldAddUserAsPlayer(): boolean {
    if (isNone(this.user)) {
      return false;
    }
    if (this.game.value.players.length === 0) {
      return true;
    }
    const userId = this.user.value.id;
    return !this.game.value.players
      .map(player => player.id)
      .includes(userId);
  }
};
</script>

<style scoped>
</style>

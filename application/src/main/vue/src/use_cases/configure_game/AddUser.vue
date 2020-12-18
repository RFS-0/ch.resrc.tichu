<template>
  <div class="container--column text--lg" @click="addUser">
    <player-avatar class="button--lg"/>
    &checkmark;
  </div>
</template>

<script lang="ts">
import { EndpointRegistry } from '@/configuration/application-configuration';
import { Game } from '@/domain/entities/game';
import { AddPlayerEvent, Team } from '@/domain/entities/team';
import { User } from '@/domain/entities/user';
import PlayerAvatar from '@/use_cases/configure_game/PlayerAvatar.vue';
import { isNone, Option, Some } from 'fp-ts/Option';
import { Component, Inject, Prop, Vue } from 'vue-property-decorator';
import { mapGetters, mapMutations } from 'vuex';

@Component({
  components: {
    PlayerAvatar,
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
export default class AddUser extends Vue {
  @Inject("endpoints")
  private endpoints!: EndpointRegistry;

  @Prop() readonly teamId!: string;
  @Prop() readonly position!: string;

  user!: Option<User>;
  game!: Some<Game>;
  updateGame!: (game: Game) => void;

  get team(): Team {
    return this.game.value.teamById(this.teamId);
  }

  addUser() {
    if (isNone(this.user)) {
      throw Error("Invariant violated: Cannot add non existing user to team");
    }
    const userId = this.user.value.id;
    const userName = this.user.value.name.toUpperCase();
    if (this.position === "add-first-player") {
      this.endpoints.addFirstPlayerToTeam.send(AddPlayerEvent.ofUser(this.teamId, userId, userName)).subscribe(
        updatedGame => this.updateGame(updatedGame),
      );
    } else if (this.position === "add-second-player") {
      this.endpoints.addSecondPlayerToTeam.send(AddPlayerEvent.ofUser(this.teamId, userId, userName)).subscribe(
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

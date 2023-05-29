<template>
  <div v-if="tichu === -1"
       @click="toggleTichu"
       class="tichu none text--sm">
    No Tichu
  </div>
  <div v-else-if="tichu === 0"
       @click="toggleTichu"
       class="tichu called text--sm">
    Tichu called
  </div>
  <div v-else-if="tichu === 1"
       @click="toggleTichu"
       class="tichu called text--sm">
    Grand Tichu called
  </div>
  <div v-else-if="tichu === -100"
       @click="toggleTichu"
       class="tichu failed text--sm">
    Tichu failed
  </div>
  <div v-else-if="tichu === 100"
       @click="toggleTichu"
       class="tichu success text--sm">
    Tichu succeeded
  </div>
  <div v-else-if="tichu === 200"
       @click="toggleTichu"
       class="tichu success text--sm">
    Grand Tichu succeeded
  </div>
  <div v-else-if="tichu === -200"
       @click="toggleTichu"
       class="tichu failed text--sm">
    Grand Tichu failed
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Round } from "@/domain/value_objects/round";
import { Some } from "fp-ts/Option";
import { Component, Prop, Vue } from "vue-property-decorator";
import { mapGetters, mapMutations } from "vuex";

@Component({
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
export default class UpdateTichu extends Vue {
  @Prop() teamId!: string;
  @Prop() playerId!: string;

  game!: Some<Game>;
  selectedRound!: Round;
  updateGame!: (game: Game) => void;

  get tichu(): number {
    const tichu = this.selectedRound.tichus.get(this.playerId);
    if (tichu === undefined) {
      return -1;
    }
    return tichu;
  }

  toggleTichu() {
    this.updateGame(
      this.game.value.butRound(this.selectedRound.toggleTichu(this.playerId)),
    );
  }
}
</script>

<style scoped>
.tichu {
  cursor: pointer;
  width: 100%;
  text-align: center;
}
</style>

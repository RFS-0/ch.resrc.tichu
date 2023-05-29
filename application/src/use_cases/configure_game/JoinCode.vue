<template>
  <div class="container--column">
    <h1 class=" text--md margin--md">join code</h1>
    <div class="text--xl">{{displayedCode}}</div>
  </div>
</template>

<script lang="ts">
import { Game } from "@/domain/entities/game";
import { Some } from "fp-ts/Option";
import "vue-class-component/hooks";
import { Component, Prop, Vue } from "vue-property-decorator";
import { mapGetters } from "vuex";

@Component({
    computed: {
      ...mapGetters("gameState", {
        game: "game",
      }),
    },
  },
)
export default class JoinCode extends Vue {
  @Prop(String) readonly gameId!: string;

  game!: Some<Game>;

  private displayedCode = "";
  private generateRandomCode = true;

  mounted() {
    setTimeout(() => this.generateRandomCode = false, 1000);
    window.requestAnimationFrame(this.animateCode);
  }

  animateCode(): void {
    if (!this.generateRandomCode) {
      this.displayedCode = this.game.value.joinCode;
      return;
    }
    let randomCode = "";
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    for (let i = 0; i < this.game.value.joinCode.length; i++) {
      randomCode += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    this.displayedCode = randomCode;
    window.requestAnimationFrame(this.animateCode);
  }
}
</script>

<style scoped>
</style>

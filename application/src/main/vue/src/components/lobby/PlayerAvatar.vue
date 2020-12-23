<template>
  <div class="align-center">
    <div class="avatar-container">
      <img src="@/assets/avatar-1.svg" alt="avatar" class="avatar">
      <!-- <div class="avatar">
      </div> -->
      <h1 class="name">
        {{player.name}}
      </h1>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import 'vue-class-component/hooks'
import { Player } from "../../types";

@Component
export default class PlayerAvatar extends Vue {
  @Prop() readonly player!: Player
}
</script>

<style scoped>
  .align-center {
    display: flex;
    flex-flow: column;
    justify-content: center;
  }

  .avatar-container
  {
    display: grid;
    /* gap: 20px; */
    grid-template-rows: min(20vh, 40vw) min-content;
  }

  .avatar {
    height: 100%;
    margin: auto;
  } 

  .name {
    font-size: 20px;
    margin: auto;
  }
</style>
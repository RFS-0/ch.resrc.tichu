<template>
  <div class="align-center">
      <img src="@/assets/avatar-1.svg" alt="avatar" class="avatar">
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
    align-content: center;
    width: 100%;
    height: 100%;
    overflow: hidden;
  }

  .avatar {
    height: 13vh;
    /* max-width: 100%;
    max-height: 100%; */
  } 
</style>
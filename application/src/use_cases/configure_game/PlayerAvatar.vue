<template>
  <img src="../../assets/avatar-1.svg" alt="avatar" class="avatar">
</template>

<script lang="ts">
import "vue-class-component/hooks";
import { Component, Vue } from "vue-property-decorator";

@Component
export default class PlayerAvatar extends Vue {
}
</script>

<style scoped>
.avatar {
  max-height: 70%;
  max-width: 70%;
}
</style>

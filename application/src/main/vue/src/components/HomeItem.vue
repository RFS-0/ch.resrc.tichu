<template>
  <v-list-item class="home-item" to="/">
    <v-list-item-icon>
      <v-icon>mdi-home</v-icon>
    </v-list-item-icon>
    <v-list-item-title>Home</v-list-item-title>
  </v-list-item>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";

@Component
export default class HomeItem extends Vue {
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.home-item {
}
</style>

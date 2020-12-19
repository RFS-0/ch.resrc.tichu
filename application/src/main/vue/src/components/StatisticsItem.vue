<template>
  <v-list-item class="statistics-item" to="/statistics">
    <v-list-item-icon>
      <v-icon>mdi-trending-up</v-icon>
    </v-list-item-icon>
    <v-list-item-title>Statistics</v-list-item-title>
  </v-list-item>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";

@Component
export default class StatisticsItem extends Vue {
}
</script>

<style scoped>
.statistics-item {
}
</style>

<script setup lang="ts">
import {ref} from 'vue'

const count = ref(0)

const increment = () => {
  count.value++
}</script>

<template>
  <div class="first-try">
    <h1>This is my first try</h1>
    <button @click="increment">count is: {{ count }}</button>
  </div>
</template>

<style scoped>

</style>

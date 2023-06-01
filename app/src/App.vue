<script setup lang="ts">
import {RouterView} from 'vue-router'
import LoadingView from '@/views/LoadingView.vue';
</script>

<template>
  <RouterView v-slot="{ Component }">
    <template v-if="Component">
      <Transition mode="out-in">
        <KeepAlive>
          <Suspense>
            <component :is="Component"></component>
            <template #fallback>
              <LoadingView/>
            </template>
          </Suspense>
        </KeepAlive>
      </Transition>
    </template>
  </RouterView>
</template>

<style>
</style>

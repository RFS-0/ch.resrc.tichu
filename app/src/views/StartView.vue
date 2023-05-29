<script setup lang="ts">
import {inject} from 'vue';
import {createGameUseCaseProviderKey} from '@/dependency-injection';
import {GameViewPresenter} from '@/presenters/game-view-presenter';
import {UserId} from 'pointchu.domain';

const createGameUseCase = inject(createGameUseCaseProviderKey)

async function onCreateGame(): Promise<void> {
  if (!createGameUseCase) {
    throw new Error('createGameUseCase is not defined');
  }
  console.log('onCreateGame');
  const presenter = new GameViewPresenter();
  await createGameUseCase.execute(
      {
        createdBy: new UserId({value: 'a0ada946-fe29-11ed-be56-0242ac120002'}),
      },
      presenter
  );
  console.log('game created', JSON.stringify(presenter.view, null, 2));
}

const onJoinGame = () => {
  console.log('onJoinGame');
}
</script>

<template>
  <div class="container--column">
    <button class="button--xl-dark text--lg" @click="onCreateGame">
      Create Game
    </button>
    <button class="button--xl-dark text--lg" @click="onJoinGame">
      Join Game
    </button>
  </div>

</template>

<style scoped>

</style>

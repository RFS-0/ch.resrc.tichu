<script setup lang="ts">
import {inject} from 'vue';
import {createGameUseCaseProviderKey} from '@/dependency-injection';
import {GameViewPresenter} from '@/presenters/game-view-presenter';
import {Game, GameSchema, PlayerId, safeParseEntity,} from 'pointchu.domain';
import {mapToRawGame} from 'pointchu.use-cases';
import {useRouter} from 'vue-router';
import {useGameStore} from '@/stores/game-store';

const router = useRouter()
const gameStore = useGameStore();

async function onCreateGame(): Promise<void> {
  const createGameUseCase = inject(createGameUseCaseProviderKey)
  if (!createGameUseCase) {
    throw new Error('createGameUseCase is not defined');
  }
  const presenter = new GameViewPresenter();
  await createGameUseCase.execute(
      {
        createdBy: new PlayerId({value: 'a0ada946-fe29-11ed-be56-0242ac120002'}),
      },
      presenter
  );
  if (!presenter.view) {
    throw new Error('Either error or defect occurred. TODO: handle this gracefully');
  }
  const parseError = () => new Error('Implementation defect: failed to parse game');
  const rawGame = mapToRawGame(presenter.view);
  const newGame = safeParseEntity(rawGame, GameSchema, Game).getOrThrow(parseError);
  gameStore.currentGame = newGame;
  console.log('game created', JSON.stringify(presenter.view, null, 2));
  await router.push('/setup/' + newGame.id.value);
}

const onJoinGame = () => {
  console.log('onJoinGame');
}
</script>

<template>
  <div class="start-view-container">
    <button class="button--xl-dark text--lg" @click="onCreateGame">
      Create Game
    </button>
    <button class="button--xl-dark text--lg" @click="onJoinGame">
      Join Game
    </button>
  </div>

</template>

<style scoped>
.start-view-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}
</style>

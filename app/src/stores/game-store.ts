import {defineStore} from 'pinia';
import {
    inject,
    type InjectionKey,
    type Ref,
    ref,
    type UnwrapRef
} from 'vue';
import {
    createIdSequence,
    EntityIdSchema,
    Game,
    GameId,
    GameSchema,
    JoinCode,
    PlayerId,
    type RawGame,
    safeParseEntity
} from 'pointchu.domain';
import {
    createGameUseCaseProviderKey, databaseProviderKey, findGameUseCaseProviderKey, updateGameUseCaseProviderKey
} from '@/dependency-injection';
import {GameViewPresenter} from '@/presenters/game-view-presenter';
import {mapToRawGame} from 'pointchu.use-cases';
import {doc, onSnapshot} from "firebase/firestore";

function injectOrThrow<T>(injectionKey: InjectionKey<T>): T {
    const dependency = inject(injectionKey);
    if (!dependency) {
        throw new Error(`Could not find dependency for ${injectionKey.description}`);
    }
    return dependency;
}

export const useGameStore = defineStore('games', () => {
    const database = injectOrThrow(databaseProviderKey);
    const createGameUseCase = injectOrThrow(createGameUseCaseProviderKey)
    const findGameUseCase = injectOrThrow(findGameUseCaseProviderKey)
    const updateGameUseCase = injectOrThrow(updateGameUseCaseProviderKey);
    const gameIdSequence = createIdSequence(EntityIdSchema, GameId)
    const initialGame = new Game({
        id: gameIdSequence.next().value,
        createdBy: null,
        joinCode: JoinCode.create().value,
        teams: [
            {
                index: 0,
                name: 'Team 1',
                playerIds: [],
            },
            {
                index: 1,
                name: 'Team 2',
                playerIds: [],
            }
        ],
        rounds: [],
    });

    const currentGame: Ref<UnwrapRef<Game>> = ref(initialGame);

    const listenToChangesOfGame = (gameId: GameId) => {
        const gameRef = doc(
            database,
            'games',
            gameId.value
        );
        return onSnapshot(
            gameRef,
            (snapshot) => {
                if (snapshot.exists()) {
                    const rawUpdatedGame = mapToRawGame(snapshot.data() as RawGame);
                    const parseError = () => new Error('Implementation defect: failed to parse game');
                    const updatedGame = safeParseEntity(rawUpdatedGame, GameSchema, Game).getOrThrow(parseError);
                    console.log('Current data: ', JSON.stringify(updatedGame, null, 2));
                    setGame(updatedGame);
                } else {
                    console.log('No such document!');
                }
            },
            (error) => {
                console.log('Encountered error:', error);
            }
        );
    };

    let unsubscribeToChangesOfGame = listenToChangesOfGame(currentGame.value.id as GameId);

    const setGame = (game: Game) => {
        if (currentGame.value.id.value !== game.id.value) {
            console.log('Unsubscribing from changes of game', currentGame.value.id);
            unsubscribeToChangesOfGame && unsubscribeToChangesOfGame();
            unsubscribeToChangesOfGame = listenToChangesOfGame(game.id);
        }
        currentGame.value = game;
    };

    async function createGame(createdBy: PlayerId) {
        const presenter = new GameViewPresenter();
        await createGameUseCase.execute(
            {
                createdBy,
            },
            presenter
        );
        if (!presenter.view) {
            throw new Error('Either system error or defect occurred. TODO: handle this gracefully');
        }
        const rawGame = mapToRawGame(presenter.view);
        const parseError = () => new Error('Implementation defect: failed to parse game');
        const game = safeParseEntity(rawGame, GameSchema, Game).getOrThrow(parseError);
        setGame(game);
    }

    async function loadGame(gameId: GameId) {
        if (currentGame.value.id.value === gameId.value) {
            return;
        }
        unsubscribeToChangesOfGame && unsubscribeToChangesOfGame();
        unsubscribeToChangesOfGame = listenToChangesOfGame(gameId);
        const presenter = new GameViewPresenter();
        await findGameUseCase.execute({gameId}, presenter);
        if (!presenter.view) {
            throw new Error('Either system error or defect occurred. TODO: handle this gracefully');
        }
    }

    async function updateGame(updatedGame: Game) {
        await updateGameUseCase.execute({updatedGame}, new GameViewPresenter());
    }

    return {
        currentGame,
        createGame,
        loadGame,
        updateGame,
    }
});

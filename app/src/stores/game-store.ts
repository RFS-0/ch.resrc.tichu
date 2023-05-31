import {defineStore} from 'pinia';
import {type Ref, ref, type UnwrapRef} from 'vue';
import {createIdSequence, EntityIdSchema, Game, GameId, JoinCode} from 'pointchu.domain';

export const useGameStore = defineStore('games', () => {
    const gameIdSequence = createIdSequence(EntityIdSchema, GameId)
    const initialGame = new Game({
        id: gameIdSequence.next().value,
        createdBy: null,
        joinCode: JoinCode.create().value,
        teams: [],
        rounds: [],
    });

    let currentGame: Ref<UnwrapRef<Game>> = ref(initialGame);

    const setGame = (game: Game) => {
        currentGame.value = game;
    };

    return {
        currentGame,
        setGame,
    }
});

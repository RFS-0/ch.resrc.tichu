import {defineStore} from 'pinia';
import {type Ref, ref, type UnwrapRef} from 'vue';
import {createIdSequence, EntityIdSchema, Player, PlayerId,} from 'pointchu.domain';

export const usePlayerStore = defineStore('players', () => {
    const userIdSequence = createIdSequence(EntityIdSchema, PlayerId)
    const initialPlayer = new Player({
        id: userIdSequence.next().value,
        userId: null,
        name: 'anonymous',
    });

    let currentPlayer: Ref<UnwrapRef<Player>> = ref(initialPlayer);

    const setPlayer = (player: Player) => {
        currentPlayer.value = player;
    };

    return {
        currentPlayer,
        setPlayer,
    }
});

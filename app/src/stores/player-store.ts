import {defineStore} from 'pinia';
import {computed, type Ref, ref, type UnwrapRef} from 'vue';
import {createIdSequence, EntityIdSchema, Player, PlayerId,} from 'pointchu.domain';

export const usePlayerStore = defineStore('players', () => {
    const playerIdSequence = createIdSequence(EntityIdSchema, PlayerId)
    const initialPlayer = new Player({
        id: playerIdSequence.next().value,
        userId: null,
        name: 'Anonymous',
    });

    let currentPlayer: Ref<UnwrapRef<Player>> = ref(initialPlayer);
    const currentPlayerId = computed(() => currentPlayer.value.id as PlayerId);

    const setPlayer = (player: Player) => {
        currentPlayer.value = player;
    };

    return {
        currentPlayer,
        currentPlayerId,
        setPlayer,
    }
});

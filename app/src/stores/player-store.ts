import {defineStore} from 'pinia';
import {computed, inject, type InjectionKey, type Ref, ref, type UnwrapRef} from 'vue';
import {
    createIdSequence, EntityIdSchema, Player, PlayerId, PlayerSchema, type RawPlayer, safeParseEntity,
} from 'pointchu.domain';
import {
    createPlayerUseCaseProviderKey, databaseProviderKey, findPlayerUseCaseProviderKey, updatePlayerUseCaseProviderKey
} from '@/dependency-injection';
import {type CreatePlayerRequest, mapToRawPlayer} from 'pointchu.use-cases';
import {PlayerViewPresenter} from '@/presenters/player-view-presenter';
import {doc, onSnapshot, type Unsubscribe} from 'firebase/firestore';

function injectOrThrow<T>(injectionKey: InjectionKey<T>): T {
    const dependency = inject(injectionKey);
    if (!dependency) {
        throw new Error(`Could not find dependency for ${injectionKey.description}`);
    }
    return dependency;
}

export const usePlayerStore = defineStore('players', () => {
    const COLLECTION = 'players';
    const database = injectOrThrow(databaseProviderKey);
    const createPlayerUseCase = injectOrThrow(createPlayerUseCaseProviderKey)
    const updatePlayerUseCase = injectOrThrow(updatePlayerUseCaseProviderKey)
    const findPlayerUseCase = injectOrThrow(findPlayerUseCaseProviderKey)
    const playerIdSequence = createIdSequence(EntityIdSchema, PlayerId)
    const initialPlayer = new Player({
        id: playerIdSequence.next().value,
        userId: null,
        name: 'Anonymous',
    });


    let loggedInPlayer: Ref<UnwrapRef<Player>> = ref(initialPlayer);
    const allPlayers: Ref<UnwrapRef<Player>[]> = ref([]);
    const playerToSubscription: Map<string, Unsubscribe> = new Map();
    const getPlayerById = (playerId: PlayerId) => computed(
        () => allPlayers.value.find(player => player.id.value === playerId.value)
    );
    const setLoggedInPlayer = (player: Player) => {
        loggedInPlayer.value = player;
    };

    async function createPlayer(request: CreatePlayerRequest) {
        const presenter = new PlayerViewPresenter();
        await createPlayerUseCase.execute(
            request,
            presenter
        );
        if (!presenter.view) {
            throw new Error('Either system error or defect occurred. TODO: handle this gracefully');
        }
        const rawPlayer = mapToRawPlayer(presenter.view);
        const parseError = () => new Error('Implementation defect: failed to parse game');
        const player = safeParseEntity(rawPlayer, PlayerSchema, Player).getOrThrow(parseError);
        addPlayerToStore(player);
        return player;
    }

    async function loadPlayer(playerId: PlayerId) {
        if (playerToSubscription.has(playerId.value)) {
            return;
        }
        const presenter = new PlayerViewPresenter();
        await findPlayerUseCase.execute({playerId}, presenter);
        if (!presenter.view) {
            throw new Error('Either system error or defect occurred. TODO: handle this gracefully');
        }
        const rawPlayer = mapToRawPlayer(presenter.view);
        const parseError = () => new Error('Implementation defect: failed to parse game');
        const player = safeParseEntity(rawPlayer, PlayerSchema, Player).getOrThrow(parseError);
        addPlayerToStore(player);
    }

    async function loadPlayers(playerIds: PlayerId[]) {
        for (const playerId of playerIds) {
            await loadPlayer(playerId);
        }
    }

    async function updatePlayer(updatedPlayer: Player) {
        await updatePlayerUseCase.execute({updatedPlayer}, new PlayerViewPresenter());
    }

    const listenToChangesOfPlayer = (playerId: PlayerId) => {
        const gameRef = doc(
            database,
            COLLECTION,
            playerId.value
        );
        return onSnapshot(
            gameRef,
            (snapshot) => {
                if (snapshot.exists()) {
                    const rawUpdatedPlayer = mapToRawPlayer(snapshot.data() as RawPlayer);
                    const parseError = () => new Error('Implementation defect: failed to parse game');
                    const updatedPlayer = safeParseEntity(rawUpdatedPlayer, PlayerSchema, Player).getOrThrow(parseError);
                    updatePlayerInStore(updatedPlayer);
                } else {
                    console.log('No such document!');
                }
            },
            (error) => {
                console.log('Encountered error:', error);
            }
        );
    };

    const subscribeToChangesOfPlayer = (playerId: PlayerId) => {
        playerToSubscription.set(playerId.value, listenToChangesOfPlayer(playerId));
    }

    const unsubscribeFromChangesOfPlayer = (playerId: PlayerId) => {
        const unsubscribe = playerToSubscription.get(playerId.value);
        if (unsubscribe) {
            unsubscribe();
            playerToSubscription.delete(playerId.value);
            removePlayerFromStore(playerId);
        }
    }

    const addPlayerToStore = (playerToAdd: Player) => {
        const indexOfPlayer = allPlayers.value.findIndex(player => playerToAdd.id.value === player.id.value)
        if (indexOfPlayer === -1) {
            allPlayers.value.push(playerToAdd);
            subscribeToChangesOfPlayer(playerToAdd.id);
        }
    }

    const updatePlayerInStore = (updatedPlayer: Player) => {
        const indexOfPlayer = allPlayers.value.findIndex(player => player.id.value === updatedPlayer.id.value);
        if (indexOfPlayer !== -1) {
            allPlayers.value = [...allPlayers.value].map((player, index) => index === indexOfPlayer ? updatedPlayer : player);
        } else {
            throw new Error('Implementation defect: player not found in store');
        }
    }

    const removePlayerFromStore = (playerId: PlayerId) => {
        if (playerId.value === loggedInPlayer.value.id.value) {
            return;
        }
        const indexOfPlayer = allPlayers.value.findIndex(player => player.id.value === playerId.value);
        if (indexOfPlayer !== -1) {
            unsubscribeFromChangesOfPlayer(playerId);
            allPlayers.value.splice(indexOfPlayer, 1);
        }
    }

    return {
        loggedInPlayer,
        getPlayerById,
        allPlayers,
        setLoggedInPlayer,
        addPlayerToStore,
        removePlayerFromStore,
        createPlayer,
        loadPlayer,
        loadPlayers,
        updatePlayer,
    }
});

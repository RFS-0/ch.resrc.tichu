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
import {doc, onSnapshot} from 'firebase/firestore';

function injectOrThrow<T>(injectionKey: InjectionKey<T>): T {
    const dependency = inject(injectionKey);
    if (!dependency) {
        throw new Error(`Could not find dependency for ${injectionKey.description}`);
    }
    return dependency;
}

export const usePlayerStore = defineStore('players', () => {
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
    const loggedInPlayerId = computed(() => loggedInPlayer.value.id as PlayerId);
    const getPlayerById = (id: PlayerId) => computed(
        () => allPlayers.value.find(player => player.id.value === id.value)
    );

    const setLoggedInPlayer = (player: Player) => {
        const indexOfPreviouslyLoggedInPlayer = allPlayers.value.indexOf(loggedInPlayer.value);
        allPlayers.value.splice(indexOfPreviouslyLoggedInPlayer, 1, player);
        listenToChangesOfPlayer(player.id);
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
        allPlayers.value.push(player);
        listenToChangesOfPlayer(player.id);
        return player;
    }

    // TODO: implement
    async function loadPlayer(playerId: PlayerId) {
        const presenter = new PlayerViewPresenter();
        await findPlayerUseCase.execute({playerId}, presenter);
        if (!presenter.view) {
            throw new Error('Either system error or defect occurred. TODO: handle this gracefully');
        }
    }

    async function updatePlayer(updatedPlayer: Player) {
        await updatePlayerUseCase.execute({updatedPlayer}, new PlayerViewPresenter());
    }

    const setPlayer = (player: Player) => {
        const indexOfPlayer = allPlayers.value.indexOf(player);
        if (indexOfPlayer === -1) {
            allPlayers.value.push(player);
        } else {
            allPlayers.value.splice(indexOfPlayer, 1, player);
        }
    }


    const listenToChangesOfPlayer = (playerId: PlayerId) => {
        const gameRef = doc(
            database,
            'games',
            playerId.value
        );
        return onSnapshot(
            gameRef,
            (snapshot) => {
                if (snapshot.exists()) {
                    const rawUpdatedPlayer = mapToRawPlayer(snapshot.data() as RawPlayer);
                    const parseError = () => new Error('Implementation defect: failed to parse game');
                    const updatedPlayer = safeParseEntity(rawUpdatedPlayer, PlayerSchema, Player).getOrThrow(parseError);
                    setPlayer(updatedPlayer);
                    console.log('Current data: ', JSON.stringify(allPlayers.value, null, 2));
                } else {
                    console.log('No such document!');
                }
            },
            (error) => {
                console.log('Encountered error:', error);
            }
        );
    };

    return {
        loggedInPlayer,
        loggedInPlayerId,
        getPlayerById,
        allPlayers,
        setLoggedInPlayer,
        createPlayer,
        updatePlayer,
    }
});

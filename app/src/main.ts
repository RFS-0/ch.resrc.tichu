import {type FirebaseApp, initializeApp} from "firebase/app";
import {getFirestore} from "firebase/firestore";
import {type Auth, getAuth, signInAnonymously} from "firebase/auth";
import {createApp} from 'vue'
import {createPinia} from 'pinia'
import App from './App.vue'
import router from './router'
import {GameRepositoryImpl, PlayerRepositoryImpl} from 'pointchu.database-adapter';
import {CreateGameUseCaseImpl} from 'pointchu.use-cases/src/create-game-use-case';
import {
    createIdSequence, EntityIdSchema, GameId, Player, PlayerId, PlayerSchema, safeParseEntity,
} from 'pointchu.domain';
import {
    authProviderKey, createGameUseCaseProviderKey, createPlayerUseCaseProviderKey, databaseProviderKey,
    findGameUseCaseProviderKey, findPlayerUseCaseProviderKey,
    updateGameUseCaseProviderKey, updatePlayerUseCaseProviderKey,
} from '@/dependency-injection';
import './assets/main.css'
import {
    CreatePlayerUseCaseImpl,
    FindGameUseCaseImpl, FindOrCreatePlayerUseCaseImpl, FindPlayerUseCaseImpl, mapToRawPlayer, UpdateGameUseCaseImpl,
    UpdatePlayerUseCaseImpl
} from 'pointchu.use-cases';
import {PlayerViewPresenter} from '@/presenters/player-view-presenter';
import {usePlayerStore} from '@/stores/player-store';

const firebaseConfig = {
    apiKey: "AIzaSyC3_BvK56Mzs4GyEiZivB6zODpK_YEbygg",
    authDomain: "pointchu.firebaseapp.com",
    projectId: "pointchu",
    storageBucket: "pointchu.appspot.com",
    messagingSenderId: "1087484042555",
    appId: "1:1087484042555:web:9104a410450180881c6b1e",
    measurementId: "G-BZC149F502"
};

const firebaseApp: FirebaseApp = initializeApp(firebaseConfig);

const database = getFirestore(firebaseApp);

const gameRepository = new GameRepositoryImpl(database);
const playerRepository = new PlayerRepositoryImpl(database);

const createGameUseCase = new CreateGameUseCaseImpl({
    inbound: {
        gameIdSequence: createIdSequence(EntityIdSchema, GameId)
    },
    outbound: {
        gameRepository
    }
});
const createPlayerUseCase = new CreatePlayerUseCaseImpl({
    inbound: {
        playerIdSequence: createIdSequence(EntityIdSchema, PlayerId)
    },
    outbound: {
        playerRepository
    }
});
const findGameUseCase = new FindGameUseCaseImpl({
    inbound: {},
    outbound: {
        gameRepository
    }
});
const findPlayerUseCase = new FindPlayerUseCaseImpl({
    inbound: {},
    outbound: {
        playerRepository
    }
});
const updateGameUseCase = new UpdateGameUseCaseImpl({
    inbound: {},
    outbound: {
        gameRepository
    }
});

const updatePlayerUseCase = new UpdatePlayerUseCaseImpl({
    inbound: {},
    outbound: {
        playerRepository
    }
});
const findOrCreatePlayerUseCase = new FindOrCreatePlayerUseCaseImpl({
    inbound: {
        playerIdSequence: createIdSequence(EntityIdSchema, PlayerId)
    },
    outbound: {
        playerRepository
    }
});

const authorization: Auth = getAuth();

// TODO: fix typing issue
// @ts-ignore
createApp(App)
    .use(createPinia())
    .use(router)
    .provide(authProviderKey, authorization)
    .provide(databaseProviderKey, database)
    .provide(createGameUseCaseProviderKey, createGameUseCase)
    .provide(createPlayerUseCaseProviderKey, createPlayerUseCase)
    .provide(findGameUseCaseProviderKey, findGameUseCase)
    .provide(findPlayerUseCaseProviderKey, findPlayerUseCase)
    .provide(updateGameUseCaseProviderKey, updateGameUseCase)
    .provide(updatePlayerUseCaseProviderKey, updatePlayerUseCase)
    .mount('#app');

const playerStore = usePlayerStore();
authorization.onAuthStateChanged(async (user) => {
    if (!user) {
        console.log('User logged out');
        return;
    }
    const presenter = new PlayerViewPresenter();
    await findOrCreatePlayerUseCase.execute(
        {
            userId: user.uid,
        },
        presenter
    );
    if (!presenter.view) {
        throw new Error('Either system error or defect occurred. TODO: handle this gracefully');
    }
    const parseError = () => new Error('Implementation defect: failed to parse game');
    const player = safeParseEntity(mapToRawPlayer(presenter.view), PlayerSchema, Player).getOrThrow(parseError);
    playerStore.setLoggedInPlayer(player);
    playerStore.addPlayerToStore(player)
});

signInAnonymously(authorization)
    .then((credential) => {
        console.log('Signed in anonymously');
    })
    .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        window.alert(`Could not sign in anonymously because of error ${errorCode} with message ${errorMessage}. Please try later`);
    });

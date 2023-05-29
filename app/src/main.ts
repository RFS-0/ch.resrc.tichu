import {type FirebaseApp, initializeApp} from "firebase/app";
import {getFirestore} from "firebase/firestore";
import {type Auth, getAuth, signInAnonymously} from "firebase/auth";
import {createApp} from 'vue'
import {createPinia} from 'pinia'
import App from './App.vue'
import router from './router'
import {GameRepositoryImpl} from 'pointchu.database-adapter';
import {CreateGameUseCaseImpl} from 'pointchu.use-cases/src/create-game-use-case';
import {createIdSequence, EntityIdSchema, GameId,} from 'pointchu.domain';
import {createGameUseCaseProviderKey,} from '@/dependency-injection';
import './assets/main.css'

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
const authorization: Auth = getAuth();
signInAnonymously(authorization)
    .then(() => {
        console.log("Signed in");
        // Signed in..
    })
    .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;
        // ...
    });

const app = createApp(App);


const gameRepository = new GameRepositoryImpl(database);
const createGameUseCase = new CreateGameUseCaseImpl({
        inbound: {
            gameIdSequence: createIdSequence(EntityIdSchema, GameId)
        },
        outbound: {
            gameRepository
        }
    }
);

app.provide(createGameUseCaseProviderKey, createGameUseCase);

app.use(createPinia());
app.use(router);

app.mount('#app');

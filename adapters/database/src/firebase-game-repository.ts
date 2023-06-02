import {type GameRepository} from 'pointchu.use-cases';
import {Game, GameId, type RawGame,} from 'pointchu.domain';
import {AsyncResult} from 'pointchu.capabilities';
import {doc, type Firestore, getDoc, setDoc} from "firebase/firestore";
import {gameConverter} from './data-types/firebase-data-types';

export class GameRepositoryImpl implements GameRepository {
    private readonly COLLECTION = 'games';

    constructor(private readonly database: Firestore) {
    }

    createMultiple(games: Game[]): AsyncResult<Game[]> {
        return AsyncResult
            .fromValue(games)
            .doAsyncEffect(async games => {
                for (const game of games) {
                    const gameRef = doc(
                        this.database,
                        this.COLLECTION,
                        game.id.value
                    )
                        .withConverter(gameConverter);
                    await setDoc(
                        gameRef,
                        game
                    );
                }
            });
    }

    create(game: Game): AsyncResult<Game> {
        return this.createMultiple([game])
                   .map(games => games[0]);
    }

    findById(gameId: GameId): AsyncResult<Game | undefined> {
        const gameRef = doc(
            this.database,
            this.COLLECTION,
            gameId.value
        )
            .withConverter(gameConverter);

        return AsyncResult
            .fromPromise(() => getDoc(gameRef))
            .map(snapshot => {
                if (!snapshot.exists()) {
                    return undefined;
                }
                return snapshot.data();
            })
    }

    updateMultiple(updatedGames: Game[]): AsyncResult<Game[]> {
        return AsyncResult
            .fromValue(updatedGames)
            .doAsyncEffect(async games => {
                for (const game of games) {
                    const gameRef = doc(
                        this.database,
                        this.COLLECTION,
                        game.id.value
                    )
                        .withConverter(gameConverter);
                    await setDoc(
                        gameRef,
                        game
                    );
                }
            });
    }

    update(updatedGame: Game): AsyncResult<Game> {
        return this.updateMultiple([updatedGame])
                   .map(games => games[0]);
    }
}

import {type GameRepository} from 'pointchu.use-cases';
import {Game,} from 'pointchu.domain';
import {AsyncResult} from 'pointchu.capabilities';
import {doc, type Firestore, setDoc} from "firebase/firestore";


export class GameRepositoryImpl implements GameRepository {
    private readonly COLLECTION = 'games';

    constructor(private readonly database: Firestore) {
    }

    createMultiple(games: Game[]): AsyncResult<Game[]> {
        return AsyncResult
            .fromValue(games)
            .doAsyncEffect(async games => {
                for (const game of games) {
                    await setDoc(
                        doc(
                            this.database,
                            this.COLLECTION,
                            game.id.value
                        ),
                        game.toRaw()
                    );
                }
            })
    }

    create(game: Game): AsyncResult<Game> {
        return this.createMultiple([game])
                   .map(games => games[0]);
    }
}

import {PlayerRepository} from 'pointchu.use-cases';
import {Player, PlayerId, RawPlayer} from 'pointchu.domain';
import {AsyncResult} from 'pointchu.capabilities';
import {doc, query, where, collection, Firestore, setDoc, getDoc, getDocs} from 'firebase/firestore';

export class PlayerRepositoryImpl implements PlayerRepository {
    private readonly COLLECTION = 'players';

    constructor(private readonly database: Firestore) {
    }

    createMultiple(players: Player[]): AsyncResult<Player[]> {
        return AsyncResult
            .fromValue(players)
            .doAsyncEffect(async players => {
                for (const player of players) {
                    await setDoc(
                        doc(
                            this.database,
                            this.COLLECTION,
                            player.id.value
                        ),
                        player.toRaw()
                    );
                }
            })
    }

    create(player: Player): AsyncResult<Player> {
        return this.createMultiple([player])
                   .map(player => player[0]);
    }

    findById(id: PlayerId): AsyncResult<Player | undefined> {
        const playerRef = doc(
            this.database,
            this.COLLECTION,
            id.value
        );
        return AsyncResult
            .fromPromise(() => getDoc(playerRef))
            .map(snapshot => {
                if (!snapshot.exists()) {
                    return undefined;
                }
                return new Player(snapshot.data() as RawPlayer);
            })
    }


    findByUserId(userId: string): AsyncResult<Player | undefined> {
        const players = collection(this.database, this.COLLECTION);
        const queryPlayerByUserId = query(
            players,
            where("userId", "==", userId)
        );

        return AsyncResult
            .fromPromise(() => getDocs(queryPlayerByUserId))
            .map(snapshots => {
                if (snapshots.empty) {
                    return undefined;
                }
                const snapshot = snapshots.docs[0]
                return new Player(snapshot.data() as RawPlayer);
            })

    }
}

import {Game, rankOf, tichuOf} from 'pointchu.domain';
import {type FirestoreDataConverter, QueryDocumentSnapshot, type SnapshotOptions} from "firebase/firestore";

export type PlayerInDB = {
    id: string;
    userId: string | null;
    name: string;
}

export type PlayersInDB = {
    [key: number]: string;
}

export type TeamInDB = {
    index: number;
    name: string;
    players: PlayersInDB[];
}

export type GameInDB = {
    id: string;
    createdBy: string | null;
    joinCode: string;
    teams: TeamInDB[];
    rounds: RoundInDB[];
}

export type CardPointsInDB = {
    [key: number]: number;
}

export type RanksInDB = {
    [key: string]: number;
}

export type TichusInDB = {
    [key: string]: number;
}

export type RoundInDB = {
    roundNumber: number,
    cardPoints: CardPointsInDB[],
    ranks: RanksInDB[],
    tichus: TichusInDB[],
}

export const gameConverter: FirestoreDataConverter<Game> = {
    toFirestore: (game: Game) => {
        return {
            id: game.id.value,
            createdBy: game.createdBy?.value || null,
            joinCode: game.joinCode.value,
            teams: game.teams.map(team => ({
                index: team.index,
                name: team.name,
                players: Array.from(team.players.entries())
                              .map(([playerIndex, playerId]) => ({
                                  [playerIndex]: playerId.value,
                              }))
            })),
            rounds: game.rounds.map(round => ({
                roundNumber: round.roundNumber,
                cardPoints: Array.from(round.cardPoints.entries())
                                 .map(
                                     ([teamIndex, cardPoints]) => ({
                                         [teamIndex]: cardPoints,
                                     })),
                ranks: Array.from(round.ranks.entries())
                            .map(
                                ([playerId, rank]) => ({
                                    [playerId]: rank.valueOf(),
                                })
                            ),
                tichus: Array.from(round.tichus.entries())
                             .map(
                                 ([playerId, tichu]) => ({
                                     [playerId]: tichu.valueOf(),
                                 }))
            }))
        }
    },
    fromFirestore(snapshot: QueryDocumentSnapshot<GameInDB>, options?: SnapshotOptions): Game {
        const data = snapshot.data(options);
        return new Game({
            id: data.id,
            createdBy: data.createdBy,
            joinCode: data.joinCode,
            teams: data.teams.map(team => ({
                index: team.index,
                name: team.name,
                players: new Map<number, string>(
                    team.players.map(
                        player => [
                            Number(Object.keys(player)[0]),
                            Object.values(player)[0]
                        ]
                    )
                )
            })),
            rounds: data.rounds.map(round => ({
                roundNumber: round.roundNumber,
                cardPoints: new Map<number, number>(
                    round.cardPoints.map(
                        points => [
                            Number(Object.keys(points)[0]),
                            Object.values(points)[0]
                        ]
                    )
                ),
                ranks: new Map(round.ranks.map(rank => [Object.keys(rank)[0], rankOf(Object.values(rank)[0])])),
                tichus: new Map(round.tichus.map(tichu => [Object.keys(tichu)[0], tichuOf(Object.values(tichu)[0])]))
            }))
        });
    }
}

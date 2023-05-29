import {AsyncResult} from 'pointchu.capabilities';
import { Game} from 'pointchu.domain';

export interface GameRepository {
    create(game: Game): AsyncResult<Game>
    createMultiple(games: Game[]): AsyncResult<Game[]>
    // findAll(): AsyncResult<Game[]>
    // findById(gameId: GameId): AsyncResult<Game>
    // findByIds(gameIds: GameId[]): AsyncResult<Game[]>
    // update(updatedGame: Game): AsyncResult<Game>
    // updateMultiple(updatedGames: Game[]): AsyncResult<Game[]>
    // delete(gameId: GameId): AsyncResult<GameId>
    // deleteMultiple(gameIds: GameId[]): AsyncResult<GameId[]>
}

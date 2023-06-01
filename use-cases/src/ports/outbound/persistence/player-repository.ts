import {Player, PlayerId} from 'pointchu.domain';
import {AsyncResult} from 'pointchu.capabilities';

export interface PlayerRepository {
    create(player: Player): AsyncResult<Player>;

    createMultiple(players: Player[]): AsyncResult<Player[]>;

    update(updatedPlayer: Player): AsyncResult<Player>

    updateMultiple(updatedPlayers: Player[]): AsyncResult<Player[]>

    findById(id: PlayerId): AsyncResult<Player | undefined>;

    findByUserId(userId: string): AsyncResult<Player | undefined>;
}

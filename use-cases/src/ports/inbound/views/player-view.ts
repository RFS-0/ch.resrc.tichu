import {type RawPlayer} from 'pointchu.domain';

export interface PlayerView {
    id: string,
    userId: string | null,
    name: string
}

export function mapToRawPlayer(player: PlayerView): RawPlayer {
    return {
        id: player.id,
        userId: player.userId,
        name: player.name
    }
}

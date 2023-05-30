import {mapToRawPlayer, type PlayerView} from './player-view';
import {type RawTeam} from 'pointchu.domain';

export interface TeamView {
    id: string
    name: string
    firstPlayer: PlayerView
    secondPlayer: PlayerView
}

export function mapToRawTeam(team: TeamView): RawTeam {
    return {
        id: team.id,
        name: team.name,
        firstPlayer: mapToRawPlayer(team.firstPlayer),
        secondPlayer: mapToRawPlayer(team.secondPlayer),
    }
}

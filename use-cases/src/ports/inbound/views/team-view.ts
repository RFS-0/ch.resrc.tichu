import {type RawTeam} from 'pointchu.domain';

export interface TeamView {
    index: number
    name: string
    players: Map<number, string>
}

export function mapToRawTeam(team: TeamView): RawTeam {
    return {
        index: team.index,
        name: team.name,
        players: team.players
    }
}

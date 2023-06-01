import {type RawTeam} from 'pointchu.domain';

export interface TeamView {
    index: number
    name: string
    playerIds: string[]
}

export function mapToRawTeam(team: TeamView): RawTeam {
    return {
        index: team.index,
        name: team.name,
        playerIds: team.playerIds
    }
}

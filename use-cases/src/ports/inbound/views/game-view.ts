import {type RoundView} from './round-view';
import {type TeamView} from './team-view';

export interface GameView {
    id: string
    createdBy: string
    joinCode: string
    teams: TeamView[]
    rounds: RoundView[]

}

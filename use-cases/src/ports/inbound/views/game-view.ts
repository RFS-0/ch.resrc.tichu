import {mapToRawRound, type RoundView} from './round-view';
import {mapToRawTeam, type TeamView} from './team-view';
import {type RawGame} from 'pointchu.domain';

export interface GameView {
    id: string;
    createdBy: string | null;
    joinCode: string;
    teams: TeamView[];
    rounds: RoundView[];
}

export function mapToRawGame(view: GameView): RawGame {
    return {
        id: view.id,
        createdBy: view.createdBy,
        joinCode: view.joinCode,
        teams: view.teams.map(team => mapToRawTeam(team)),
        rounds: view.rounds.map(round => mapToRawRound(round))
    } as RawGame;
}

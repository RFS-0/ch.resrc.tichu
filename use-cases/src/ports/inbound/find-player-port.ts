import {type PlayerView} from './views';
import {type RawProblem} from 'pointchu.capabilities';
import {PlayerId} from 'pointchu.domain';

export interface FindPlayerRequest {
    playerId: PlayerId
}

export interface FindPlayerResponse {
    player: PlayerView | undefined;
    problems: RawProblem[];
}

export interface FindPlayerPresenter {
    present(response: FindPlayerResponse): Promise<void>
}

export interface FindPlayerUseCase {
    execute(request: FindPlayerRequest, presenter: FindPlayerPresenter): Promise<void>
}

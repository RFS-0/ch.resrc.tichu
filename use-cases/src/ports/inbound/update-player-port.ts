import {type PlayerView} from './views';
import {type RawProblem} from 'pointchu.capabilities';
import {Player,} from 'pointchu.domain';

export interface UpdatePlayerRequest {
    updatedPlayer: Player
}

export interface UpdatePlayerResponse {
    player: PlayerView | undefined;
    problems: RawProblem[];
}

export interface UpdatePlayerPresenter {
    present(response: UpdatePlayerResponse): Promise<void>
}

export interface UpdatePlayerUseCase {
    execute(request: UpdatePlayerRequest, presenter: UpdatePlayerPresenter): Promise<void>
}

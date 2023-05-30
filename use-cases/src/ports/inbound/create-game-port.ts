import {type GameView} from './views';
import {type RawProblem} from 'pointchu.capabilities';
import {PlayerId} from 'pointchu.domain';

export interface CreateGameRequest {
    createdBy: PlayerId;
}

export interface CreateGameResponse {
    game: GameView | undefined;
    problems: RawProblem[];
}

export interface CreateGamePresenter {
    present(response: CreateGameResponse): Promise<void>
}

export interface CreateGameUseCase {
    execute(request: CreateGameRequest, presenter: CreateGamePresenter): Promise<void>
}

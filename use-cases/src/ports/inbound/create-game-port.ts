import {UserId} from 'pointchu.domain';
import {type GameView} from './views';
import {type RawProblem} from 'pointchu.capabilities';

export interface CreateGameRequest {
    createdBy: UserId;
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

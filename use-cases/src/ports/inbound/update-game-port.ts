import {type GameView} from './views';
import {type RawProblem} from 'pointchu.capabilities';
import {Game,} from 'pointchu.domain';

export interface UpdateGameRequest {
    updatedGame: Game
}

export interface UpdateGameResponse {
    game: GameView | undefined;
    problems: RawProblem[];
}

export interface UpdateGamePresenter {
    present(response: UpdateGameResponse): Promise<void>
}

export interface UpdateGameUseCase {
    execute(request: UpdateGameRequest, presenter: UpdateGamePresenter): Promise<void>
}

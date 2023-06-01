import {type GameView} from './views';
import {type RawProblem} from 'pointchu.capabilities';
import {GameId} from 'pointchu.domain';

export interface FindGameRequest {
    gameId: GameId
}

export interface FindGameResponse {
    game: GameView | undefined;
    problems: RawProblem[];
}

export interface FindGamePresenter {
    present(response: FindGameResponse): Promise<void>
}

export interface FindGameUseCase {
    execute(request: FindGameRequest, presenter: FindGamePresenter): Promise<void>
}

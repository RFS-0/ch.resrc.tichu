import {type PlayerView} from './views';
import {type RawProblem} from 'pointchu.capabilities';

export interface CreatePlayerRequest {
    userId: string | null;
    name: string;
}

export interface CreatePlayerResponse {
    player: PlayerView | undefined;
    problems: RawProblem[];
}

export interface CreatePlayerPresenter {
    present(response: CreatePlayerResponse): Promise<void>
}

export interface CreatePlayerUseCase {
    execute(request: CreatePlayerRequest, presenter: CreatePlayerPresenter): Promise<void>
}

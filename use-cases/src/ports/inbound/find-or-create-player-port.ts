import {type PlayerView} from './views';
import {type RawProblem} from 'pointchu.capabilities';

export interface FindOrCreatePlayerRequest {
    userId: string
}

export interface FindOrCreatePlayerResponse {
    player: PlayerView | undefined;
    problems: RawProblem[];
}

export interface FindOrCreatePlayerPresenter {
    present(response: FindOrCreatePlayerResponse): Promise<void>
}

export interface FindOrCreatePlayerUseCase {
    execute(request: FindOrCreatePlayerRequest, presenter: FindOrCreatePlayerPresenter): Promise<void>
}

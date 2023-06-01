import type {
    CreateGamePresenter,
    FindGamePresenter,
    CreateGameResponse,
    FindGameResponse,
    UpdateGameResponse,
    GameView,
} from 'pointchu.use-cases';
import {
} from 'pointchu.use-cases';

export class GameViewPresenter implements CreateGamePresenter, FindGamePresenter {

    private _view: GameView | undefined;

    async present(response: FindGameResponse): Promise<void>
    async present(response: UpdateGameResponse): Promise<void>
    async present(response: CreateGameResponse): Promise<void> {
        if (response.problems.length > 0) {
            // TODO: Handle problems
            console.log(`Problems: ${JSON.stringify(response.problems)}`);
            return;
        }
        this._view = response.game;
    }

    get view(): GameView | undefined {
        return this._view;
    }
}

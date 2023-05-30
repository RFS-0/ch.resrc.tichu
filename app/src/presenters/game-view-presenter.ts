import type {
    CreateGamePresenter,
    CreateGameResponse,
    GameView
} from 'pointchu.use-cases';

export class GameViewPresenter implements CreateGamePresenter {

    private _view: GameView | undefined;

    async present(response: CreateGameResponse): Promise<void> {
        console.log(`Presenting ${JSON.stringify(response)}`);
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

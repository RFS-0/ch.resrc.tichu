import type {
    CreateGamePresenter,
    CreateGameResponse,
    GameView
} from 'pointchu.use-cases';

export class GameViewPresenter implements CreateGamePresenter {

    private _view: GameView | undefined;

    async present(response: CreateGameResponse): Promise<void> {
        console.log(`Presenting ${JSON.stringify(response)}`);
        this._view = response.game;
    }

    get view(): GameView | undefined {
        return this._view;
    }
}

import type {FindOrCreatePlayerPresenter, FindOrCreatePlayerResponse, PlayerView} from 'pointchu.use-cases';

export class PlayerViewPresenter implements FindOrCreatePlayerPresenter {

    private _view: PlayerView | undefined;

    async present(response: FindOrCreatePlayerResponse): Promise<void> {
        console.log(`Presenting ${JSON.stringify(response)}`);
        if (response.problems.length > 0) {
            // TODO: Handle problems
            console.log(`Problems: ${JSON.stringify(response.problems)}`);
            return;
        }
        this._view = response.player
    }

    get view(): PlayerView | undefined {
        return this._view;
    }
}

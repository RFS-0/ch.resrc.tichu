import {type GameRepository, type GameView, UpdateGamePresenter, UpdateGameRequest, UpdateGameUseCase} from './ports';
import {AsyncResult, Problem} from 'pointchu.capabilities';
import {Game} from 'pointchu.domain';

export type UpdateGameUseCasePorts = {
    inbound: {
    },
    outbound: {
        gameRepository: GameRepository,
    }
}

export class UpdateGameUseCaseImpl implements UpdateGameUseCase {

    constructor(private readonly ports: UpdateGameUseCasePorts) {
    }

    async execute(request: UpdateGameRequest, presenter: UpdateGamePresenter): Promise<void> {
        await AsyncResult
            .fromValue(request.updatedGame)
            .doAsyncEffect(async game => await this.updateEntity(game).get())
            .doAsyncEffect(async game => await this.presentEntity(presenter, game))
            .doFailureEffect(async problems => await this.presentProblems(presenter, problems))
            .get();
    }


    private updateEntity(game: Game): AsyncResult<Game> {
        return this.ports.outbound.gameRepository.update(game);
    }

    private presentEntity(presenter: UpdateGamePresenter, game: Game): Promise<void> {
        return presenter.present({
            game: this.mapToView(game),
            problems: []
        })
    }

    private presentProblems(presenter: UpdateGamePresenter, problems: Problem[]): Promise<void> {
        return presenter.present({
            game: undefined,
            problems: problems.map(problem => problem.toRaw())
        })
    }

    private mapToView(game: Game): GameView {
        return {
            ...game.toRaw(),
        }
    }
}

import {type GameRepository, type GameView} from './ports';
import {type FindGamePresenter, type FindGameRequest, type FindGameUseCase} from './ports';
import {AsyncResult, Problem} from 'pointchu.capabilities';
import {Game, GameId} from 'pointchu.domain';

export type FindGameUseCasePorts = {
    inbound: {
    },
    outbound: {
        gameRepository: GameRepository,
    }
}

export class FindGameUseCaseImpl implements FindGameUseCase {

    constructor(private readonly PORTS: FindGameUseCasePorts) {
    }

    async execute(request: FindGameRequest, presenter: FindGamePresenter): Promise<void> {
        await AsyncResult
            .fromValue(request.gameId)
            .flatMap(gameId => this.findGameById(gameId))
            .doAsyncEffect(async game => await this.presentEntity(presenter, game))
            .doFailureEffect(async problems => await this.presentProblems(presenter, problems))
            .get();
    }

    private findGameById(gameId: GameId): AsyncResult<Game | undefined> {
        return this.PORTS.outbound.gameRepository.findById(gameId);
    }

    private mapToView(game: Game): GameView {
        return {
            ...game.toRaw()
        }
    }

    private presentEntity(presenter: FindGamePresenter, game: Game | undefined): Promise<void> {
        if (!game) {
            return Promise.reject(new Error('Game not found'));
        }
        return presenter.present({
            game: this.mapToView(game),
            problems: []
        })
    }

    private presentProblems(presenter: FindGamePresenter, problems: Problem[]): Promise<void> {
        return presenter.present({
            game: undefined,
            problems: problems.map(problem => problem.toRaw())
        })
    }
}

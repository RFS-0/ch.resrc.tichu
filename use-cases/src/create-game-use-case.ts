import {
    type CreateGamePresenter,
    type CreateGameRequest,
    type CreateGameUseCase,
    type GameRepository,
    type GameView
} from './ports';
import {AsyncResult, Problem} from 'pointchu.capabilities';
import {
    Game,
    GameId,
    type IdSequence,
    JoinCode, Team
} from 'pointchu.domain';

export type CreateGameUseCasePorts = {
    inbound: {
        gameIdSequence: IdSequence<GameId>
    },
    outbound: {
        gameRepository: GameRepository,
    }
}

export class CreateGameUseCaseImpl implements CreateGameUseCase {

    constructor(private readonly ports: CreateGameUseCasePorts) {
    }

    async execute(request: CreateGameRequest, presenter: CreateGamePresenter): Promise<void> {
        await AsyncResult
            .fromValue(this.createEntity(request))
            .doAsyncEffect(async game => await this.storeEntity(game).get())
            .doAsyncEffect(async game => await this.presentEntity(presenter, game))
            .doFailureEffect(async problems => await this.presentProblems(presenter, problems))
            .get();
    }

    private createEntity(request: CreateGameRequest): Game {
        return new Game({
            id: this.ports.inbound.gameIdSequence.next().value,
            createdBy: request.createdBy.value,
            joinCode: JoinCode.create().value,
            teams: [
                {
                    index: 0,
                    name: 'Team 1',
                    players: new Map(),
                },
                {
                    index: 1,
                    name: 'Team 2',
                    players: new Map(),
                }
            ],
            rounds: [],
        });
    }

    private storeEntity(game: Game): AsyncResult<Game> {
        return this.ports.outbound.gameRepository.create(game);
    }

    private presentEntity(presenter: CreateGamePresenter, game: Game): Promise<void> {
        return presenter.present({
            game: this.mapToView(game),
            problems: []
        })
    }

    private presentProblems(presenter: CreateGamePresenter, problems: Problem[]): Promise<void> {
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

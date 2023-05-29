import {
    type CreateGamePresenter,
    type CreateGameRequest,
    type CreateGameUseCase,
    type GameRepository,
    type GameView
} from './ports';
import {AsyncResult} from 'pointchu.capabilities';
import {
    Game,
    GameId,
    type IdSequence,
    JoinCode
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
            .fromValue(this.mapToEntity(request))
            .doAsyncEffect(async game => await this.ports.outbound.gameRepository.create(game).get())
            .doAsyncEffect(async game => await presenter.present({
                game: this.mapToView(game),
                problems: []
            }))
            .doFailureEffect(errors => presenter.present({
                game: undefined,
                problems: errors.map(error => error.toRaw())
            }))
            .get();
    }

    private mapToEntity(request: CreateGameRequest): Game {
        return new Game({
            id: this.ports.inbound.gameIdSequence.next().value,
            createdBy: request.createdBy.value,
            joinCode: JoinCode.create().value,
            teams: [],
            rounds: [],
        });
    }

    private mapToView(game: Game): GameView {
        return {
            ...game.toRaw(),
        }
    }
}

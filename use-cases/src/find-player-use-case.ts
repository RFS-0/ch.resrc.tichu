import {Player, PlayerId} from 'pointchu.domain';
import {
    type  FindPlayerPresenter, type FindPlayerRequest, type FindPlayerUseCase, type PlayerRepository, type PlayerView
} from './ports';
import {AsyncResult, Problem} from 'pointchu.capabilities';


export type FindPlayerUseCasePorts = {
    inbound: {
    },
    outbound: {
        playerRepository: PlayerRepository,
    }
}

export class FindPlayerUseCaseImpl implements FindPlayerUseCase {

    constructor(private readonly ports: FindPlayerUseCasePorts) {
    }

    async execute(request: FindPlayerRequest, presenter: FindPlayerPresenter): Promise<void> {
        await AsyncResult
            .fromValue(request.playerId)
            .flatMap(playerId => this.findPlayerByUserId(playerId))
            .doAsyncEffect(async player => await this.presentEntity(presenter, player))
            .doFailureEffect(async problems => await this.presentProblems(presenter, problems))
            .get();
    }

    private findPlayerByUserId(playerId: PlayerId): AsyncResult<Player | undefined> {
        return this.ports.outbound.playerRepository.findById(playerId);
    }

    private mapToView(player: Player): PlayerView {
        return {
            ...player.toRaw()
        }
    }

    private presentEntity(presenter: FindPlayerPresenter, player: Player | undefined): Promise<void> {
        if (!player) {
            return Promise.reject(new Error('Player not found'));
        }
        return presenter.present({
            player: this.mapToView(player),
            problems: []
        })
    }

    private presentProblems(presenter: FindPlayerPresenter, problems: Problem[]): Promise<void> {
        return presenter.present({
            player: undefined,
            problems: problems.map(problem => problem.toRaw())
        })
    }
}

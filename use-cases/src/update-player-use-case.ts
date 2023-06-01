import {type PlayerRepository, type PlayerView, UpdatePlayerPresenter, UpdatePlayerRequest, UpdatePlayerUseCase} from './ports';
import {AsyncResult, Problem} from 'pointchu.capabilities';
import {Player} from 'pointchu.domain';

export type UpdatePlayerUseCasePorts = {
    inbound: {
    },
    outbound: {
        playerRepository: PlayerRepository,
    }
}

export class UpdatePlayerUseCaseImpl implements UpdatePlayerUseCase {

    constructor(private readonly ports: UpdatePlayerUseCasePorts) {
    }

    async execute(request: UpdatePlayerRequest, presenter: UpdatePlayerPresenter): Promise<void> {
        await AsyncResult
            .fromValue(request.updatedPlayer)
            .doAsyncEffect(async player => await this.updateEntity(player).get())
            .doAsyncEffect(async player => await this.presentEntity(presenter, player))
            .doFailureEffect(async problems => await this.presentProblems(presenter, problems))
            .get();
    }


    private updateEntity(player: Player): AsyncResult<Player> {
        return this.ports.outbound.playerRepository.update(player);
    }

    private presentEntity(presenter: UpdatePlayerPresenter, player: Player): Promise<void> {
        return presenter.present({
            player: this.mapToView(player),
            problems: []
        })
    }

    private presentProblems(presenter: UpdatePlayerPresenter, problems: Problem[]): Promise<void> {
        return presenter.present({
            player: undefined,
            problems: problems.map(problem => problem.toRaw())
        })
    }

    private mapToView(player: Player): PlayerView {
        return {
            ...player.toRaw(),
        }
    }
}

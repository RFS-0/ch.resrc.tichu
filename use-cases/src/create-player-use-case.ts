import {type CreatePlayerPresenter, type CreatePlayerRequest, type CreatePlayerUseCase, type PlayerRepository, type PlayerView} from './ports';
import {type IdSequence, Player, PlayerId} from 'pointchu.domain';
import {AsyncResult, Problem} from 'pointchu.capabilities';

import {faker} from '@faker-js/faker';

export type CreatePlayerUseCasePorts = {
    inbound: {
        playerIdSequence: IdSequence<PlayerId>
    },
    outbound: {
        playerRepository: PlayerRepository,
    }
}

export class CreatePlayerUseCaseImpl implements CreatePlayerUseCase {

    constructor(private readonly ports: CreatePlayerUseCasePorts) {
    }

    async execute(request: CreatePlayerRequest, presenter: CreatePlayerPresenter): Promise<void> {
        await AsyncResult
            .fromValue(this.createEntity(request))
            .doAsyncEffect(async game => await this.storeEntity(game).get())
            .doAsyncEffect(async game => await this.presentEntity(presenter, game))
            .doFailureEffect(async problems => await this.presentProblems(presenter, problems))
            .get();
    }

    private createEntity(request: CreatePlayerRequest): Player {
        return new Player({
            id: this.ports.inbound.playerIdSequence.next().value,
            userId: request.userId,
            name: `${faker.word.adjective()}_${faker.word.noun()}`,
        });
    }

    private storeEntity(player: Player): AsyncResult<Player> {
        return this.ports.outbound.playerRepository.create(player);
    }

    private presentEntity(presenter: CreatePlayerPresenter, player: Player): Promise<void> {
        return presenter.present({
            player: this.mapToView(player),
            problems: []
        })
    }

    private presentProblems(presenter: CreatePlayerPresenter, problems: Problem[]): Promise<void> {
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

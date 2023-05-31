import {IdSequence, Player, PlayerId} from 'pointchu.domain';
import {PlayerRepository, PlayerView} from './ports';
import {
    FindOrCreatePlayerPresenter, FindOrCreatePlayerRequest, FindOrCreatePlayerUseCase
} from './ports';
import {AsyncResult, Problem} from 'pointchu.capabilities';
import {faker} from '@faker-js/faker';


export type FindOrCreatePlayerUseCasePorts = {
    inbound: {
        playerIdSequence: IdSequence<PlayerId>
    },
    outbound: {
        playerRepository: PlayerRepository,
    }
}

export class FindOrCreatePlayerUseCaseImpl implements FindOrCreatePlayerUseCase {

    constructor(private readonly ports: FindOrCreatePlayerUseCasePorts) {
    }

    async execute(request: FindOrCreatePlayerRequest, presenter: FindOrCreatePlayerPresenter): Promise<void> {
        await AsyncResult
            .fromValue(request.userId)
            .flatMap(userId => this.findPlayerByUserId(userId))
            .flatMap(player => {
                if (player) {
                    return AsyncResult.fromValue(player);
                }
                return this.createPlayer(request.userId);
            })
            .doAsyncEffect(async player => await this.presentEntity(presenter, player))
            .doFailureEffect(async problems => await this.presentProblems(presenter, problems))
            .get();
    }

    private findPlayerByUserId(userId: string): AsyncResult<Player | undefined> {
        return this.ports.outbound.playerRepository.findByUserId(userId);
    }

    private createPlayer(userId: string): AsyncResult<Player> {
        return AsyncResult
            .fromValue(new Player({
                id: this.ports.inbound.playerIdSequence.next().value,
                userId: userId,
                name: `${faker.word.adjective()}_${faker.word.noun()}`,
            }))
            .doAsyncEffect(async player => await this.ports.outbound.playerRepository.create(player).get())
    }

    private mapToView(player: Player): PlayerView {
        return {
            ...player.toRaw()
        }
    }

    private presentEntity(presenter: FindOrCreatePlayerPresenter, player: Player): Promise<void> {
        return presenter.present({
            player: this.mapToView(player),
            problems: []
        })
    }

    private presentProblems(presenter: FindOrCreatePlayerPresenter, problems: Problem[]): Promise<void> {
        return presenter.present({
            player: undefined,
            problems: problems.map(problem => problem.toRaw())
        })
    }
}

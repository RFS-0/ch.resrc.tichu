import type {InjectionKey} from 'vue';
import type {CreateGameUseCase} from 'pointchu.use-cases';

export const createGameUseCaseProviderKey = Symbol('InjectionKey for the create game use case object') as InjectionKey<CreateGameUseCase>;


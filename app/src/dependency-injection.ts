import type {InjectionKey} from 'vue';
import type {CreateGameUseCase} from 'pointchu.use-cases';
import type {Auth} from 'firebase/auth';

export const authProviderKey = Symbol('InjectionKey for the auth object') as InjectionKey<Auth>;
export const createGameUseCaseProviderKey = Symbol('InjectionKey for the create game use case object') as InjectionKey<CreateGameUseCase>;

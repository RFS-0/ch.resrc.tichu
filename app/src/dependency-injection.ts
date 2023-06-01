import type {InjectionKey} from 'vue';
import type {
    CreateGameUseCase, CreatePlayerUseCase, FindGameUseCase, FindPlayerUseCase, UpdateGameUseCase, UpdatePlayerUseCase
} from 'pointchu.use-cases';
import type {Auth} from 'firebase/auth';
import {Firestore} from "firebase/firestore";

export const authProviderKey = Symbol('InjectionKey for the auth object') as InjectionKey<Auth>;
export const databaseProviderKey = Symbol('InjectionKey for the database object') as InjectionKey<Firestore>;
export const createGameUseCaseProviderKey = Symbol('InjectionKey for the create game use case object') as InjectionKey<CreateGameUseCase>;
export const createPlayerUseCaseProviderKey = Symbol('InjectionKey for the create player use case object') as InjectionKey<CreatePlayerUseCase>;
export const updateGameUseCaseProviderKey = Symbol('InjectionKey for the update game use case object') as InjectionKey<UpdateGameUseCase>;
export const updatePlayerUseCaseProviderKey = Symbol('InjectionKey for the update player use case object') as InjectionKey<UpdatePlayerUseCase>;
export const findPlayerUseCaseProviderKey = Symbol('InjectionKey for the find player use case object') as InjectionKey<FindPlayerUseCase>;
export const findGameUseCaseProviderKey = Symbol('InjectionKey for the find game use case object') as InjectionKey<FindGameUseCase>;

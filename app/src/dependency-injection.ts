import type {InjectionKey} from 'vue';
import type {CreateGameUseCase, FindGameUseCase, UpdateGameUseCase} from 'pointchu.use-cases';
import type {Auth} from 'firebase/auth';
import {Firestore} from "firebase/firestore";

export const authProviderKey = Symbol('InjectionKey for the auth object') as InjectionKey<Auth>;
export const databaseProviderKey = Symbol('InjectionKey for the database object') as InjectionKey<Firestore>;
export const createGameUseCaseProviderKey = Symbol('InjectionKey for the create game use case object') as InjectionKey<CreateGameUseCase>;
export const updateGameUseCaseProviderKey = Symbol('InjectionKey for the find game use case object') as InjectionKey<UpdateGameUseCase>;
export const findGameUseCaseProviderKey = Symbol('InjectionKey for the find game use case object') as InjectionKey<FindGameUseCase>;

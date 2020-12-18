import { endpointRegistry } from '@/configuration/application-configuration';
import { Game } from '@/domain/entities/game';
import { Player } from '@/domain/entities/player';
import { Team } from '@/domain/entities/team';
import { User } from '@/domain/entities/user';
import { Round } from '@/domain/value_objects/round';
import { isNone, none, Option, some } from 'fp-ts/Option';
import Vue from 'vue';
import Vuex, { ActionTree, GetterTree, Module, MutationTree, StoreOptions } from 'vuex';

Vue.use(Vuex);

const endpoints = endpointRegistry();

export interface RootState {
  version: string;
}

///// USERS /////

export interface UserState {
  user: Option<User>;
}

export const userState: UserState = {
  user: none,
};

export const userGetters: GetterTree<UserState, RootState> = {
  user(state): Option<User> {
    return state.user;
  },
};

export const userMutations: MutationTree<UserState> = {
  updateUser(state, user: User) {
    state.user = some(user);
  },
};

export const userActions: ActionTree<UserState, RootState> = {};

export const userModule: Module<UserState, RootState> = {
  namespaced: true,
  state: userState,
  getters: userGetters,
  actions: userActions,
  mutations: userMutations,
};

///// GAMES /////

export interface GameState {
  game: Option<Game>;
  selectedRoundNumber: number,
}

export const gameState: GameState = {
  game: none,
  selectedRoundNumber: 1,
};

export const gameGetters: GetterTree<GameState, RootState> = {
  game(state): Option<Game> {
    return state.game;
  },
  selectedRoundNumber(state): number {
    return state.selectedRoundNumber;
  },
  selectedRound(state): Round {
    if (isNone(state.game)) {
      throw Error('Invariant violated: game cannot be undefined');
    }
    const game = state.game.value;
    const selectedRound = game.rounds.find(
      round => round.roundNumber === state.selectedRoundNumber,
    );
    if (!selectedRound) {
      throw Error('Invariant violated: selected round cannot be undefined');
    }
    return selectedRound;
  },
};

export const gameMutations: MutationTree<GameState> = {
  updateGame(state, game: Game) {
    state.game = some(game);
  },
  updateSelectedRoundNumber(state, selectedRoundNumber) {
    state.selectedRoundNumber = selectedRoundNumber;
  },
};

export const gameActions: ActionTree<GameState, RootState> = {};

export const gameModule: Module<GameState, RootState> = {
  namespaced: true,
  state: gameState,
  getters: gameGetters,
  mutations: gameMutations,
  actions: gameActions,
};

///// TEAMS /////

export interface TeamsState {
  teams: Team[];
}

export const teamsState: TeamsState = {
  teams: [],
};

export const teamsGetters: GetterTree<TeamsState, RootState> = {
  teamById: (state: TeamsState) => (id: string): Team | undefined => {
    return state.teams.find((team) => team.id == id);
  },
};

export const teamsMutations: MutationTree<TeamsState> = {
  addTeam(state, team: Team) {
    state.teams.push(team);
  },
  updateTeam(state, updated: Team) {
    const indexOfTeamToUpdate = state.teams.map(team => team.id).indexOf(updated.id);
    state.teams[indexOfTeamToUpdate] = updated;
  },
  addOrUpdateTeam(state, updated: Team) {
    const exists = state.teams.find(team => team.id === updated.id);
    if (exists) {
      const indexOfTeamToUpdate = state.teams.map(team => team.id).indexOf(updated.id);
      state.teams[indexOfTeamToUpdate] = updated;
    } else {
      state.teams.push(updated);
    }
  },
};

export const teamsActions: ActionTree<TeamsState, RootState> = {};

export const teamsModule: Module<TeamsState, RootState> = {
  namespaced: true,
  state: teamsState,
  getters: teamsGetters,
  mutations: teamsMutations,
  actions: teamsActions,
};

///// PLAYERS /////

export interface PlayersState {
  players: Player[];
}

export const playersState: PlayersState = {
  players: [],
};

export const playersGetters: GetterTree<PlayersState, RootState> = {
  playerById: (state: PlayersState) => (id: string): Player | undefined => {
    return state.players.find(player => player.id == id);
  },
};

export const playersMutations: MutationTree<PlayersState> = {
  addPlayer(state, player: Player) {
    state.players.push(player);
  },
  updatePlayer(state, updated: Player) {
    const indexOfPlayerToUpdate = state.players.map(player => player.id).indexOf(updated.id);
    state.players[indexOfPlayerToUpdate] = updated;
  },
  addOrUpdatePlayer(state, updated: Player) {
    const exists = state.players.find(player => player.id === updated.id);
    if (exists) {
      const indexOfPlayerToUpdate = state.players.map(player => player.id).indexOf(updated.id);
      state.players[indexOfPlayerToUpdate] = updated;
    } else {
      state.players.push(updated);
    }
  },
};

export const playersActions: ActionTree<PlayersState, RootState> = {};

export const playersModule: Module<PlayersState, RootState> = {
  namespaced: true,
  state: playersState,
  getters: playersGetters,
  mutations: playersMutations,
  actions: playersActions,
};

const store: StoreOptions<RootState> = {
  state: {
    version: '0.0.1',
  },
  mutations: {},
  actions: {},
  modules: {
    userState: userModule,
    gameState: gameModule,
    teamsState: teamsModule,
    playersState: playersModule,
  },
};

export default new Vuex.Store<RootState>(store);

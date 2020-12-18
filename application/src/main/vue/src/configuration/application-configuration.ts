import {
  AddFirstPlayerToTeamFake,
  AddSecondPlayerToTeamFake,
  CreateGameFake,
  FindOrCreateUserFake,
  FinishGameFake,
  FinishRoundFake,
  RemoveFirstPlayerFromTeamFake,
  RemoveSecondPlayerFromTeamFake,
  ResetRankFake,
  UpdateCardPointsFake,
  UpdateRankFake,
  UpdateRoundFake,
  UpdateTeamNameFake,
} from '@/adapters/endpoints/fake-endpoints/fake-endpoints';
import { FindOrCreateUserRest } from '@/adapters/endpoints/rest/find-or-create-user-rest';
import { CreateGameWebSocket } from '@/adapters/endpoints/websocket/create-game-web-socket';
import {
  AddFirstPlayerToTeam,
  AddSecondPlayerToTeam,
  CreateGame,
  FindOrCreateUser,
  FinishGame,
  FinishRound,
  RemoveFirstPlayerFromTeam,
  RemoveSecondPlayerFromTeam,
  ResetRank,
  UpdateCardPoints,
  UpdateRank,
  UpdateRound,
  UpdateTeamName,
} from '@/endpoints/endpoints';

export interface EndpointRegistry {
  findOrCreateUser: FindOrCreateUser;
  createGame: CreateGame;
  updateTeamName: UpdateTeamName;
  addFirstPlayerToTeam: AddFirstPlayerToTeam;
  removeFirstPlayerFromTeam: RemoveFirstPlayerFromTeam;
  addSecondPlayerToTeam: AddSecondPlayerToTeam;
  removeSecondPlayerFromTeam: RemoveSecondPlayerFromTeam;
  updateRank: UpdateRank;
  resetRank: ResetRank;
  updateCardPoints: UpdateCardPoints;
  finishRound: FinishRound;
  updateRound: UpdateRound;
  finishGame: FinishGame;
}

export interface ApplicationConfiguration {
  backendHost: string;
  backendPort: string;
  webSocketAddresses: WebSocketAddresses
  restPaths: RestPaths
  endpoints: EndpointRegistry;
}

export enum Endpoints {
  FAKED = 'FAKED',
  TEST = 'TEST',
  DEV = 'DEV',
  PROD = 'PROD',
}

export interface WebSocketAddresses {
  games: GameSockets
  teams: TeamSockets
}

export interface RestPaths {
  user: UserPaths
}

export interface UserPaths {
  users: string;
  findOrCreate: string;
  me: string
}

export interface GameSockets {
  create: string;
  created: string;
}

export interface TeamSockets {
  findById: string;
  foundById: string;
  updateTeamName: string;
  updatedTeamName: string;
  addFirstPlayerToTeam: string;
  addedFirstPlayerToTeam: string;
  addSecondPlayerToTeam: string;
  addedSecondPlayerToTeam: string;
  removeFirstPlayerFromTeam: string;
  removedFirstPlayerFromTeam: string;
  removeSecondPlayerFromTeam: string;
  removedSecondPlayerFromTeam: string;
}

const WEB_SOCKET_ADDRESSES: WebSocketAddresses = {
  games: {
    create: "/events/games/create",
    created: "/events/games/created",
  },
  teams: {
    findById: "/events/teams/find-by-id",
    foundById: "/events/teams/found/{teamId}",
    updateTeamName: "/events/teams/update-team-name",
    updatedTeamName: "/events/teams/updated-team-name/{teamId}",
    addFirstPlayerToTeam: "/events/teams/add-first-player-to-team",
    addedFirstPlayerToTeam: "/events/teams/added-first-player-to-team/{teamId}",
    addSecondPlayerToTeam: "/events/teams/add-second-player-to-team",
    addedSecondPlayerToTeam: "/events/teams/added-second-player-to-team/{teamId}",
    removeFirstPlayerFromTeam: "/events/teams/remove-first-player-from-team",
    removedFirstPlayerFromTeam: "/events/teams/removed-first-player-from-team/{teamId}",
    removeSecondPlayerFromTeam: "/events/teams/remove-second-player-from-team",
    removedSecondPlayerFromTeam: "/events/teams/removed-second-player-from-team/{teamId}",
  },
};

const REST_PATHS: RestPaths = {
  user: {
    users: "/api/users",
    findOrCreate: "/api/users/find-or-create",
    me: "/api/me",
  },
};

export const FAKED_ENDPOINT_REGISTRY: EndpointRegistry = {
  findOrCreateUser: new FindOrCreateUserFake(),
  createGame: new CreateGameFake(),
  updateTeamName: new UpdateTeamNameFake(),
  addFirstPlayerToTeam: new AddFirstPlayerToTeamFake(),
  removeFirstPlayerFromTeam: new RemoveFirstPlayerFromTeamFake(),
  addSecondPlayerToTeam: new AddSecondPlayerToTeamFake(),
  removeSecondPlayerFromTeam: new RemoveSecondPlayerFromTeamFake(),
  updateRank: new UpdateRankFake(),
  resetRank: new ResetRankFake(),
  updateCardPoints: new UpdateCardPointsFake(),
  finishRound: new FinishRoundFake(),
  updateRound: new UpdateRoundFake(),
  finishGame: new FinishGameFake(),
};

export const LOCAL_HOST_ENDPOINT_REGISTRY: EndpointRegistry = {
  findOrCreateUser: new FindOrCreateUserRest(REST_PATHS.user.findOrCreate),
  createGame: new CreateGameWebSocket(
    "ws://" + process.env.VUE_APP_API_HOST + WEB_SOCKET_ADDRESSES.games.create,
    "ws://" + process.env.VUE_APP_API_HOST + WEB_SOCKET_ADDRESSES.games.created,
  ),
  updateTeamName: new UpdateTeamNameFake(),
  addFirstPlayerToTeam: new AddFirstPlayerToTeamFake(),
  removeFirstPlayerFromTeam: new RemoveFirstPlayerFromTeamFake(),
  addSecondPlayerToTeam: new AddSecondPlayerToTeamFake(),
  removeSecondPlayerFromTeam: new RemoveSecondPlayerFromTeamFake(),
  updateRank: new UpdateRankFake(),
  resetRank: new ResetRankFake(),
  updateCardPoints: new UpdateCardPointsFake(),
  finishRound: new FinishRoundFake(),
  updateRound: new UpdateRoundFake(),
  finishGame: new FinishGameFake(),
};

export function endpointRegistry(): EndpointRegistry {
  switch (process.env.VUE_APP_ENDPOINTS) {
    case Endpoints.FAKED:
      return FAKED_ENDPOINT_REGISTRY;
    case Endpoints.DEV:
      return LOCAL_HOST_ENDPOINT_REGISTRY;
    case Endpoints.PROD:
      return LOCAL_HOST_ENDPOINT_REGISTRY; // this will require another implementation
    default:
      throw new Error("Invariant violated: Cannot create registry for unknown endpoints type");
  }
}

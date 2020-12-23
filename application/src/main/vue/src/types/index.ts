export interface User {
  id: string
}

export interface Player {
  id: string
  name: string
  user?: User
}

export interface Team {
  id: string
  name: string
  players: Player[]
}

export enum TichuType {
  Tichu, GrandTichu
}

export enum TichuState {
  Called, Successful, Failed
}

export interface Tichu {
  type: TichuType
  state: TichuState
  caler: Player
}

export interface Round {
  tichus: { [playerId: number]: Tichu }
  points: { [teamId: number]: number }
  rankedPlayers: Player[];
}

export enum GameState {
  Created, Active, Finished
}

export interface Game {
  id: string
  joinCode: string
  state: GameState
  teams: Team[]
  rounds: Round[]
}

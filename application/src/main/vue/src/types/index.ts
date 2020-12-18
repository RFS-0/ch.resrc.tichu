export interface User {
  id: string
}

export interface Player {
  id: string
  name: string
  avatar?: string
}

export interface Team {
  id: string
  name: string
  firstPlayer?: string
  secondPlayer?: string
}

export interface Game {
  id: string
  joinCode: string
  leftTeam: string
  rightTeam: string
  rounds: Round[]
  finishedAt?: Date
}

export interface Tichu {
  playerId: string
  // null if round is not finished yet
  successful?: boolean;
  value: number;
}

export interface GrandTichu {
  playerId: string
  // null if round is not finished yet
  successful?: boolean;
  value: number;
}

export interface CardPoints {
  teamId: string
  value: number
}

export interface Ranks {
  firstPlayer: string
  secondPlayer: string
  thirdPlayer: string
  fourthPlayer: string
}

export interface Round {
  roundNumber: number
  cardPoints: CardPoints[]
  tichus?: Tichu[]
  grandTichus?: Tichu[]
  ranks: Ranks
}

export interface AddPlayerInfo {
  teamId: string;
  name: string;
}

export interface RemovePlayerInfo {
  teamId: string;
  playerId: string;
}

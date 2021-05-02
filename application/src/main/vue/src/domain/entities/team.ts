import { Player, PlayerDto } from '@/domain/entities/player';

export class Team {

  private constructor(
    private readonly _id: string,
    private _name: string | null,
    private _firstPlayer: Player | null,
    private _secondPlayer: Player | null,
  ) {
  }

  static of(id: string, name: string | null, firstPlayer: Player | null, secondPlayer: Player | null): Team {
    return new Team(
      id,
      name,
      firstPlayer,
      secondPlayer,
    );
  }

  static fromDto(dto: TeamDto): Team {
    return Team.of(dto.id, dto.name, Player.fromDto(dto.firstPlayer), Player.fromDto(dto.secondPlayer));
  }

  static fromDtos(dtos: TeamDto[]): Team[] {
    return dtos.map(Team.fromDto)
  }

  static copy(other: Team): Team {
    return new Team(
      other._id,
      other._name,
      other._firstPlayer,
      other._secondPlayer,
    );
  }

  butName(name: string): Team {
    const copy = Team.copy(this);
    copy._name = name;
    return copy;
  }

  butFirstPlayer(firstPlayer: Player | null): Team {
    const copy = Team.copy(this);
    copy._firstPlayer = firstPlayer;
    return copy;
  }

  butSecondPlayer(secondPlayer: Player | null): Team {
    const copy = Team.copy(this);
    copy._secondPlayer = secondPlayer;
    return copy;
  }

  players(): Player[] {
    const players = [];
    if (this.firstPlayer) {
      players.push(this.firstPlayer);
    }
    if (this.secondPlayer) {
      players.push(this.secondPlayer);
    }
    return players;
  }

  isFirstPlayer(playerId: string): boolean {
    return this.firstPlayer?.id === playerId;
  }

  isSecondPlayer(playerId: string): boolean {
    return this.secondPlayer?.id === playerId;
  }

  isComplete(): boolean {
    return !!this.firstPlayer && !!this.secondPlayer;
  }

  get id(): string {
    return this._id;
  }

  get name(): string | null {
    return this._name;
  }

  get firstPlayer(): Player | null {
    return this._firstPlayer;
  }

  get secondPlayer(): Player | null {
    return this._secondPlayer;
  }
}

export interface TeamDto {
  readonly id: string,
  readonly name: string | null,
  readonly firstPlayer: PlayerDto | null,
  readonly secondPlayer: PlayerDto | null,
}

export class UpdateTeamNameEvent {
  private constructor(public gameId: string, public teamId: string, public teamName: string) {
  }

  static of(gameId: string, teamId: string, teamName: string): UpdateTeamNameEvent {
    return new UpdateTeamNameEvent(
      gameId,
      teamId,
      teamName,
    );
  }
}

export class AddPlayerEvent {
  private constructor(
    public teamId: string,
    public userId: string | null,
    public playerName: string,
  ) {
  }

  static ofUser(teamId: string, userId: string, playerName: string): AddPlayerEvent {
    return new AddPlayerEvent(
      teamId,
      userId,
      playerName,
    );
  }

  static ofPlayer(teamId: string, playerName: string): AddPlayerEvent {
    return new AddPlayerEvent(
      teamId,
      null,
      playerName,
    );
  }
}

export class RemoveFirstPlayerEvent {
  private constructor(public teamId: string) {
  }

  static of(teamId: string): RemoveFirstPlayerEvent {
    return new RemoveFirstPlayerEvent(teamId);
  }
}

export class RemoveSecondPlayerEvent {
  private constructor(public teamId: string) {
  }

  static of(teamId: string): RemoveSecondPlayerEvent {
    return new RemoveSecondPlayerEvent(teamId);
  }
}

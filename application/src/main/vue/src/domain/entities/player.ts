export class Player {

  constructor(
    private readonly _id: string,
    private readonly _name: string,
  ) {
  }

  static of(id: string, name: string): Player {
    return new Player(
      id,
      name,
    );
  }

  static fromDto(dto: PlayerDto | null): Player | null {
    if (!dto) {
      return null;
    }
    return this.of(dto.id, dto.name,);
  }

  static copy(other: Player) {
    return new Player(
      other._id,
      other._name
    );
  }

  get id(): string {
    return this._id;
  }

  get name(): string {
    return this._name;
  }
}

export interface PlayerDto {
  readonly id: string,
  readonly name: string,
}

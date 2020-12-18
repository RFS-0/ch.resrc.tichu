export class User {
  private constructor(
    private readonly _id: string,
    private readonly _email: string | null,
    private readonly _name: string,
    private readonly _createdAt: string | null) {
  }

  static of(id: string, email: string, name: string, createdAt: string | null): User {
    return new User(id, email, name, createdAt);
  }

  static copy(other: User): User {
    return new User(other._id, other._email, other._name, other._createdAt);
  }

  get id(): string {
    return this._id;
  }

  get email(): string | null {
    return this._email;
  }

  get name(): string {
    return this._name;
  }

  get createdAt(): string | null {
    return this._createdAt;
  }
}

export class IntendedUserDto {
  private constructor(
    private readonly name: string,
    private readonly email: string) {
  }

  static of(name: string, email: string): IntendedUserDto {
    return new IntendedUserDto(name, email);
  }
}

export abstract class Result<T, E> {
  static success<T, E>(value: T): Result<T, E> {
    return new Success(value)
  }

  static emptySuccess<T, E>(): Result<T, E> {
    return new EmptySuccess<T, E>()
  }

  static failure<T, E>(errors: E[]): Result<T, E> {
    return new Failure<T, E>(errors)
  }

  static emptyFailure<T, E>(errors: E[]): Result<T, E> {
    return new EmptyFailure<T, E>(errors)
  }

  static emptyResult<T, E>(errors: E[]): Result<T, E> {
    if (errors.length === 0) {
      return new EmptySuccess<T, E>()
    } else {
      return new EmptyFailure<T, E>(errors)
    }
  }

  abstract isSuccess(): boolean
  abstract isFailure(): boolean
  abstract isEmpty(): boolean
  abstract value(): T
  abstract errors(): E[]
  abstract map<U>(f: (value: T) => U): Result<U, E>
  abstract flatMap<U>(f: (value: T) => Result<U, E>): Result<U, E>
  abstract mapErrors<EE>(f: (error: E) => EE): Result<T, EE>
  abstract handleErrors<EE>(f: (errors: E[]) => Result<T, EE>): Result<T, EE>
  abstract getOrThrow(f: (errors: E[]) => Error): T
  abstract ifFailureThrow(f: (errors: E[]) => void): Result<T, E>
  abstract recover<U extends T>(alternative: U): Result<U | T, E>
  abstract ifEmptySwitch(alternative: () => Result<T, E>): Result<T, E>
  abstract thenEmptySuccess(): Result<void, E>
  abstract thenResultOf<U>(f: () => Result<U, E>): Result<U, E>
  abstract doEffect(f: () => void): Result<T, E>
  abstract doEffect(f: (value: T) => void): Result<T, E>
  mapValue<U>(f: (value: T) => U): U {
    return f(this.value())
  }
  doFailureEffect(f: (errors: E[]) => void): Result<T, E> {
    if (this.isFailure()) {
      f(this.errors())
    }
    return this
  }
  thenValueOf<U>(f: () => U): Result<U, E> {
    return this.thenResultOf(() => Result.success(f()))
  }
}

export class Success<T, E> extends Result<T, E> {

  private readonly _value: T

  constructor(value: T) {
    super()
    this._value = value
  }


  isSuccess(): boolean {
    return true
  }

  isFailure(): boolean {
    return false
  }

  isEmpty(): boolean {
    return false
  }

  value(): T {
    return this._value
  }

  errors(): E[] {
    return []
  }

  map<U>(f: (value: T) => U): Result<U, E> {
    return Result.success(f(this._value))
  }

  flatMap<U>(f: (value: T) => Result<U, E>): Result<U, E> {
    return f(this._value)
  }

  mapErrors<EE>(_: (error: E) => EE): Result<T, EE> {
    return Result.success(this._value)
  }

  handleErrors<EE>(_: (error: E[]) => Result<T, EE>): Result<T, EE> {
    return Result.success(this._value)
  }

  getOrThrow(_: (errors: E[]) => void): T {
    return this._value
  }

  ifFailureThrow(_: (errors: E[]) => void): Result<T, E> {
    return Result.success(this._value)
  }

  recover<U extends T>(_: U): Result<T, E> {
    return Result.success(this._value)
  }

  ifEmptySwitch(_: () => Result<T, E>): Result<T, E> {
    return Result.success(this._value)
  }

  thenEmptySuccess(): Result<void, E> {
    return Result.success(undefined)
  }

  thenResultOf<U>(f: () => Result<U, E>): Result<U, E> {
    return f()
  }

  doEffect(f: (value: T) => void): Result<T, E> {
    f(this._value)
    return Result.success(this._value)
  }

  doFailureEffect(_: (errors: E[]) => void): Result<T, E> {
    return Result.success(this._value)
  }
}

export class EmptySuccess<T, E> extends Result<T, E> {

  isSuccess(): boolean {
    return true
  }

  isFailure(): boolean {
    return false
  }

  isEmpty(): boolean {
    return false
  }

  value(): T {
    throw new Error('EmptySuccess has no value')
  }

  errors(): E[] {
    return []
  }

  map<U>(_: (value: T) => U): Result<U, E> {
    return Result.emptySuccess()
  }

  flatMap<U>(_: (value: T) => Result<U, E>): Result<U, E> {
    return Result.emptySuccess()
  }

  mapErrors<EE>(_: (error: E) => EE): Result<T, EE> {
    return Result.emptySuccess()
  }

  handleErrors<EE>(_: (error: E[]) => Result<T, EE>): Result<T, EE> {
    return Result.emptySuccess()
  }

  getOrThrow(_: (errors: E[]) => void): T {
    throw new Error('EmptySuccess has no value')
  }

  ifFailureThrow(_: (errors: E[]) => void): Result<T, E> {
    return Result.emptySuccess()
  }

  recover<U extends T>(alternative: U): Result<T, E> {
    return Result.success(alternative)
  }

  ifEmptySwitch(alternative: () => Result<T, E>): Result<T, E> {
    return alternative()
  }

  thenEmptySuccess(): Result<void, E> {
    return Result.emptySuccess()
  }

  thenResultOf<U>(f: () => Result<U, E>): Result<U, E> {
    return f()
  }

  doEffect(f: ((value?: T) => void) | (() => void)): Result<T, E> {
    if (f.length > 0) {
      throw new Error('EmptySuccess has no value')
    } else {
      f()
    }
    return Result.emptySuccess()
  }

  doFailureEffect(_: (errors: E[]) => void): Result<T, E> {
    return Result.emptySuccess()
  }
}

export class Failure<T, E> extends Result<T, E> {

  private readonly _errors: E[]

  constructor(errors: E[]) {
    super()
    this._errors = errors
  }

  isSuccess(): boolean {
    return false
  }

  isFailure(): boolean {
    return true
  }

  isEmpty(): boolean {
    return false
  }

  value(): T {
    throw new Error('Failure has no value')
  }

  errors(): E[] {
    return this._errors
  }

  map<U>(_: (value: T) => U): Result<U, E> {
    return Result.failure(this._errors)
  }

  flatMap<U>(_: (value: T) => Result<U, E>): Result<U, E> {
    return Result.failure(this._errors)
  }

  mapErrors<EE>(f: (error: E) => EE): Result<T, EE> {
    const mappedErrors = this._errors.map(f)
    return Result.failure(mappedErrors)
  }

  handleErrors<EE>(f: (errors: E[]) => Result<T, EE>): Result<T, EE> {
    return f(this._errors)
  }

  getOrThrow(f: (errors: E[]) => Error): T {
    throw f(this._errors)
  }

  ifFailureThrow(f: (errors: E[]) => void): Result<T, E> {
    f(this._errors)
    return Result.failure(this._errors)
  }

  recover<U extends T>(alternative: U): Result<T, E> {
    return Result.success(alternative)
  }

  ifEmptySwitch(_: () => Result<T, E>): Result<T, E> {
    return Result.failure(this._errors)
  }

  thenEmptySuccess(): Result<void, E> {
    return Result.failure(this._errors)
  }

  thenResultOf<U>(_: () => Result<U, E>): Result<U, E> {
    return Result.failure(this._errors)
  }

  doEffect(_: (value: T) => void): Result<T, E> {
    return Result.failure(this._errors)
  }

  doFailureEffect(f: (errors: E[]) => void): Result<T, E> {
    f(this._errors)
    return Result.failure(this._errors)
  }
}

export class EmptyFailure<T, E> extends Result<T, E> {

  private readonly _failure: Failure<undefined, E>

  constructor(errors: E[]) {
    super()
    this._failure = new Failure(errors)
  }

  isSuccess(): boolean {
    return false
  }

  isFailure(): boolean {
    return true
  }

  isEmpty(): boolean {
    return true
  }

  value(): T {
    throw new Error('EmptyFailure has no value')
  }

  errors(): E[] {
    return this._failure.errors()
  }

  map<U>(_: (value: T) => U): Result<U, E> {
    throw new Error('EmptyFailure has no value')
  }

  flatMap<U>(_: (value: T) => Result<U, E>): Result<U, E> {
    throw new Error('EmptyFailure has no value')
  }

  mapErrors<EE>(f: (error: E) => EE): Result<T, EE> {
    const mappedErrors = this._failure.mapErrors(f).errors()
    return Result.emptyFailure(mappedErrors)
  }

  handleErrors<EE>(f: (errors: E[]) => Result<T, EE>): Result<T, EE> {
    return f(this.errors())
  }

  getOrThrow(_: (errors: E[]) => Error): T {
    throw new Error('EmptyFailure has no value')
  }

  ifFailureThrow(f: (errors: E[]) => void): Result<T, E> {
    this._failure.ifFailureThrow(f)
    return this
  }

  recover<U extends T>(_: U): Result<T, E> {
    throw new Error('EmptyFailure has no value')
  }

  ifEmptySwitch(_: () => Result<T, E>): Result<T, E> {
    return this;
  }

  thenEmptySuccess(): Result<void, E> {
    return Result.emptySuccess()
  }

  thenResultOf<U>(f: () => Result<U, E>): Result<U, E> {
    return this._failure.thenResultOf(f)
  }


  doEffect(_: ((value?: T) => void) | (() => void)): Result<T, E> {
    return this
  }
}

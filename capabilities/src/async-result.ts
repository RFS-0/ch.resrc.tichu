import { Problem } from './problem';
import { Result } from './result';

type InitialOperation<T> = {
  type: 'initial',
  initial: () => Promise<T>
}

type MapChain<T, U> = {
  type: 'mapChain',
  previous: AsyncResult<T>,
  map: (value: T) => U
}

type MapErrorsChain = {
  type: 'mapErrorsChain',
  previous: AsyncResult<any>,
  chainOnFailure: (value: Problem[]) => Problem[]
}

type Operation =
  InitialOperation<any> |
  MapErrorsChain |
  MapChain<any, any>

export class AsyncResult<T> {

  static fromPromise<T>(initial: () => Promise<T>): AsyncResult<T> {
    return new AsyncResult<T>({
      type: 'initial',
      initial
    } as InitialOperation<T>)
  }

  static fromValue<T>(value: T): AsyncResult<T> {
    return AsyncResult.fromPromise(() => Promise.resolve(value))
  }

  static empty(): AsyncResult<void> {
    return AsyncResult.fromValue(undefined)
  }

  static fromAsyncResult<T>(result: AsyncResult<T>): AsyncResult<T> {
    return result;
  }

  private _successChain: Promise<T>;
  private _failureChain: Promise<Problem[]>

  constructor(operation: Operation) {
    switch (operation.type) {
      case 'initial':
        this._successChain = operation.initial()
        this._failureChain = this._successChain.then(
          () => [],
          error => this.handleErrors(error)
        )
        break;
      case 'mapChain':
        this._successChain = operation.previous.value().then(value => operation.map(value));
        this._failureChain = this._successChain.then(
          () => [],
          error => this.handleErrors(error)
        );
        break;
      case 'mapErrorsChain':
        this._successChain = operation.previous.value().catch(error => this.handleErrors(error))
        this._failureChain = operation.previous.errors().then(error => operation.chainOnFailure(error))
        this._failureChain = this._failureChain.catch(error => this.handleErrors(error))
        break;
    }
  }

  private value(): Promise<T> {
    return this._successChain;
  }

  private errors(): Promise<Problem[]> {
    return this._failureChain;
  }

  async get(): Promise<Result<T, Problem>> {
    const [value, errors] = await Promise.allSettled([this._successChain, this._failureChain])
    if (errors.status === 'fulfilled' && errors.value.length > 0) {
      return Result.failure(errors.value)
    }
    else if (value.status === 'fulfilled') {
      return Result.success(value.value)
    }
    else {
      return Result.failure([Problem.withMessage("Unknown error")])
    }
  }

  map<U>(f: (value: T) => U): AsyncResult<U> {
    return new AsyncResult<U>({
      type: 'mapChain',
      previous: this,
      map: f
    })
  }

  flatMap<U>(f: (value: T) => AsyncResult<U>): AsyncResult<U> {
    return AsyncResult.fromPromise(
      () => this._successChain.then(
        value => f(value).value()
      )
    )
  }

  tryRecoverWithPromise(f: () => Promise<T>): AsyncResult<T> {
    this._successChain = this.value().catch(f)
    this._failureChain = Promise.resolve([])
    return this
  }

  tryRecoverWithResult(f: () => AsyncResult<T>): AsyncResult<T> {
    return AsyncResult.fromPromise(
      () => this._successChain.catch(
        () => f().value()
      )
    )
  }

  mapErrors(f: (error: Problem[]) => Problem[]): AsyncResult<T> {
    return new AsyncResult<T>({
      type: 'mapErrorsChain',
      previous: this,
      chainOnFailure: f
    })
  }

  doEffect(f: (value: T) => void): AsyncResult<T> {
    this._successChain = this._successChain.then(value => {
      f(value)
      return value
    })
    return this
  }

  doAsyncEffect(f: (value: T) => Promise<any>): AsyncResult<T> {
    this._successChain = this._successChain.then(async value => {
      await f(value)
      return value
    })
    return this
  }

  doFailureEffect(f: (error: Problem[]) => void): AsyncResult<T> {
    this._failureChain = this.errors().then(error => {
      if (error.length > 0) {
        f(error)
      }
      return error
    })
    return this
  }

  private handleErrors(errors: any): Problem[] {
    if (errors instanceof Error) {
      return [Problem.fromError(errors)]
    } else if (Array.isArray(errors)) {
      return errors
    } else if (errors instanceof Problem) {
      return [errors]
    } else {
      return [Problem.withMessage("Unknown error")]
    }
  }
}


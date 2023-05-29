export type ProblemCause =
  'input-invalid' |
  'implementation-defect' |
  'system-error'

export type ProblemParams = {
  [key: string]: string
}

export interface RawProblem {
  cause: ProblemCause;
  template: string;
  params?: ProblemParams;
}

export class Problem {

  static withMessage(message: string): Problem {
    return new Problem({
      cause: 'system-error',
      template: message,
    })
  }

  static fromError(error: Error): Problem {
    return new Problem({
      cause: 'system-error',
      template: error.message,
    });
  }

  _cause: ProblemCause
  _template: string;
  _parameters: Record<string, string>;

  constructor(input: RawProblem) {
    this._cause = input.cause;
    this._template = input.template;
    this._parameters = input.params ?? {};
  }

  get cause() {
    return this._cause;
  }

  get message() {
    return this._template.replace(/\$\{(\w+)}/g, (_, key) => this._parameters[key]);
  }

  toRaw(): RawProblem {
    return {
      cause: this._cause,
      template: this._template,
      params: this._parameters,
    }
  }
}


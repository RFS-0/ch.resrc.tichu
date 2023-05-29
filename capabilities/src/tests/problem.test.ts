import { Problem, ProblemCause, RawProblem } from '../problem';

describe(`Testing the 'Problem'`, () => {
  describe("creating this object", () => {
    const validInputs: RawProblem[] = [
      {
        cause: 'input-invalid' as ProblemCause,
        template: 'Some input the client provided is invalid',
      },
      {
        cause: 'implementation-defect' as ProblemCause,
        template: 'Some code is not working as expected',
      },
      {
        cause: 'system-error' as ProblemCause,
        template: 'Some system is not working as expected',
      }
    ]
    describe("with valid input", () => {
      validInputs.forEach(validInput => {
        it(`is ensured that valid input '${JSON.stringify(validInput)}' results in the successful creation of the object`, () => {
          expect(() => new Problem(validInput)).not.toThrow()
        })
      })
    })

    describe("wiht a valid template and params", () => {
      describe("converting the input into a message", () => {
        const validTemplatesWithParams: RawProblem[] = [
          {
            cause: 'input-invalid' as ProblemCause,
            template: 'invalid input is ${value}',
            params: {
              value: 'invalid-value'
            }
          },
          {
            cause: 'implementation-defect' as ProblemCause,
            template: 'the function ${function} is not working as expected',
            params: {
              function: 'some-function'
            }
          },
          {
            cause: 'system-error' as ProblemCause,
            template: '${system} is not working as expected',
            params: {
              system: 'some-system'
            }
          }
        ]
        validTemplatesWithParams.forEach(validTemplate => {
          it(`is ensured that the valid template '${JSON.stringify(validTemplate)}' can be parsed into a message which replaces all params in the template with the provided values`, () => {
            const problem = new Problem(validTemplate)
            if (!validTemplate.params) {
              throw new Error(`Expected '${JSON.stringify(validTemplate)}' to have params`)
            }
            for (const key of Object.keys(validTemplate.params)) {
              expect(problem.message).toContain(validTemplate.params[key])
            }
          })
        })
      })
    })
  })
})

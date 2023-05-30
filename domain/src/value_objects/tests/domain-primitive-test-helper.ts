import { Result } from 'pointchu.capabilities';
import { Schema, ZodError } from 'zod';
import {
  CompositeValueObject,
  NumberValueObject,
  RawCompositeValueObject,
  RawNumberValueObject,
  RawStringValueObject,
  safeParseNumber,
  safeParseObject,
  safeParseString,
  StringValueObject,
} from '../value-object';

export type StringValueObjectTestsuiteParams<R extends RawStringValueObject, S extends Schema<R>, T extends StringValueObject> = {
  ctor: { new(input: R): T },
  schema: S,
  validInputs: string[],
  invalidInputs: string[]
}

export function createStringValueObjectCreationTests<R extends RawStringValueObject, S extends Schema<R>, T extends StringValueObject>(params: StringValueObjectTestsuiteParams<R, S, T>) {
  describe(`Testing the string domain primitive '${params.ctor.name}'`, () => {

    describe("creating this object", () => {

      describe("with valid input", () => {

        params.validInputs.forEach(validInput => {

          it(`is ensured that safely parsing the valid input '${validInput}' results in the successful creation of the object`, () => {
            const result = safeParseString({ value: validInput } as R, params.schema, params.ctor)
            result
              .doEffect((value: T) => {
                expect(value.value).toBe(validInput)
              })
              .doFailureEffect(errors => {
                throw new Error(`The creation of the object failed with the following errors: ${errors.map(error => error.message).join(', ')}`)
              })
          })

          it(`is ensured that parsing the valid input '${validInput}' results in the successful creation of the object`, () => {
            const domainPrimitive = new params.ctor({ value: validInput } as R)
            expect(domainPrimitive.value).toEqual(validInput)
          })
        })
      })

      describe("with invalid input", () => {

        params.invalidInputs.forEach(invalidInput => {

          it(`is ensured that safely parsing the invalid input '${invalidInput}' results in a failure`, () => {
            const result: Result<T, ZodError<R>> = safeParseString({ value: invalidInput } as R, params.schema, params.ctor)
            result
              .doEffect(() => {
                throw new Error(`Expected '${invalidInput}' to be an invalid '${params.ctor.name}'`)
              })
              .doFailureEffect(errors => {
                expect(errors.length).toEqual(1)
                const errorMessages = errors.map(error => error.message).join(', ')
                expect(errorMessages).toContain(`Invalid `)
              })
          })

          it(`is ensured that parsing the invalid input '${invalidInput}' results in an exception`, () => {
            const parse = () => new params.ctor({ value: invalidInput } as R)
            expect(parse).toThrow(`Preconditions to create ${params.ctor.name} not met`)
          })
        })
      })
    })
  })
}

export type NumberPrimitiveTestsuiteParams<R extends RawNumberValueObject, S extends Schema<R>, T extends NumberValueObject> = {
  ctor: { new(input: R): T },
  schema: S,
  validInputs: number[],
  invalidInputs: number[]
}

export function createNumberPrimitiveCreationTests<R extends RawNumberValueObject, S extends Schema<R>, T extends NumberValueObject>(params: NumberPrimitiveTestsuiteParams<R, S, T>) {
  describe(`Testing the string domain primitive '${params.ctor.name}'`, () => {

    describe("creating this object", () => {

      describe("with valid input", () => {

        params.validInputs.forEach(validInput => {

          it(`is ensured that safely parsing the valid input '${validInput}' results in the successful creation of the object`, () => {
            const result = safeParseNumber({ value: validInput } as R, params.schema, params.ctor)
            result
              .doEffect(() => {
                expect(result.value().value).toBe(validInput)
              })
              .doFailureEffect(errors => {
                throw new Error(`The creation of the object failed with the following errors: ${errors.map(error => error.message).join(', ')}`)
              })
          })

          it(`is ensured that parsing the valid input '${validInput}' results in the successful creation of the object`, () => {
            const domainPrimitive = new params.ctor({ value: validInput } as R)
            expect(domainPrimitive.value).toEqual(validInput)
          })
        })
      })

      describe("with invalid input", () => {

        params.invalidInputs.forEach(invalidInput => {

          it(`is ensured that safely parsing the invalid input '${invalidInput}' results in a failure`, () => {
            const result: Result<T, ZodError<R>> = safeParseNumber({ value: invalidInput } as R, params.schema, params.ctor)
            result
              .doEffect(() => {
                throw new Error(`Expected '${invalidInput}' to be an invalid '${params.ctor.name}'`)
              })
              .doFailureEffect(errors => {
                expect(errors.length).toEqual(1)
                const errorMessages = errors.map(error => error.message).join(', ')
                expect(errorMessages).toContain(`Number must be `)
              })
          })

          it(`is ensured that parsing the invalid input '${invalidInput}' results in an exception`, () => {
            const parse = () => new params.ctor({ value: invalidInput } as R)
            expect(parse).toThrow(`Preconditions to create ${params.ctor.name} not met`)
          })
        })
      })
    })
  })
}

export type ObjectTestsuiteParams<R extends RawCompositeValueObject, S extends Schema<R>, T extends CompositeValueObject> = {
  ctor: { new(input: R): T },
  schema: S,
  validInputs: R[],
  invalidInputs: R[]
}

export function createObjectPrimitiveCreationTests<R extends RawCompositeValueObject, S extends Schema<R>, T extends CompositeValueObject>(params: ObjectTestsuiteParams<R, S, T>) {
  describe(`Testing the domain object primitive '${params.ctor.name}'`, () => {

    describe("creating this object", () => {

      describe("with valid input", () => {

        params.validInputs.forEach(validInput => {

          it(`is ensured that safely parsing the valid input '${JSON.stringify(validInput)}' results in the successful creation of the object`, () => {
            const result = safeParseObject(validInput, params.schema, params.ctor)
            result
              .doEffect(() => {
                expect(result.value()).toEqual(new params.ctor(validInput))
              })
              .doFailureEffect(errors => {
                throw new Error(`The creation of the object failed with the following errors: ${errors.map(error => error.message).join(', ')}`)
              })
          })

          it(`is ensured that parsing the valid input '${JSON.stringify(validInput)}' results in the successful creation of the object`, () => {
            const intendedEntity = new params.ctor(validInput)
            expect(intendedEntity).toEqual(new params.ctor(validInput))
          })
        })
      })

      describe("with invalid input", () => {

        params.invalidInputs.forEach(invalidInput => {

          it(`is ensured that safely parsing the invalid input '${JSON.stringify(invalidInput)}' results in a failure`, () => {
            const result: Result<T, ZodError<R>> = safeParseObject(invalidInput, params.schema, params.ctor)
            result
              .doEffect(() => {
                throw new Error(`Expected '${JSON.stringify(invalidInput)}' to be an invalid '${params.ctor.name}'`)
              })
              .doFailureEffect(errors => {
                expect(errors.length).toBeGreaterThan(0)
              })
          })

          it(`is ensured that parsing the invalid input '${JSON.stringify(invalidInput)}' results in an exception`, () => {
            const parse = () => new params.ctor(invalidInput)
            expect(parse).toThrow()
          })
        })
      })
    })
  })
}

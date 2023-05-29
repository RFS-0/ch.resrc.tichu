import { Result } from 'pointchu.capabilities';
import { Schema, ZodError } from 'zod';
import { Entity, IntendedEntity, RawEntity, RawIntendedEntity, safeParseEntity, safeParseIntention } from '../entity';

export type IntendedEntityTestsuiteParams<R extends RawIntendedEntity, S extends Schema<R>, T extends IntendedEntity> = {
  ctor: { new(input: R): T },
  schema: S,
  validInputs: R[],
  invalidInputs: R[]
}

export function createIntendedEntityCreationTests<R extends RawIntendedEntity, S extends Schema<R>, T extends IntendedEntity>(params: IntendedEntityTestsuiteParams<R, S, T>) {
  describe(`Testing the intended entity '${params.ctor.name}'`, () => {

    describe("creating this object", () => {

      describe("with valid input", () => {

        params.validInputs.forEach(validInput => {

          it(`is ensured that safely parsing the valid input '${JSON.stringify(validInput)}' results in the successful creation of the object`, () => {
            safeParseIntention(validInput, params.schema, params.ctor)
              .doEffect((value: T) => {
                expect(value).toEqual(new params.ctor(validInput))
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
            safeParseIntention(invalidInput, params.schema, params.ctor)
              .doEffect(() => {
                throw new Error(`Expected '${JSON.stringify(invalidInput)}' to be an invalid '${params.ctor.name}'`)
              })
              .doFailureEffect(errors => {
                expect(errors.length).toBeGreaterThan(0)
              })
          })

          it(`is ensured that parsing the invalid input '${JSON.stringify(invalidInput)}' results in an exception`, () => {
            const parse = () => new params.ctor(invalidInput)
            expect(parse).toThrow(`Precondtions to create ${params.ctor.name} not met`)
          })
        })
      })
    })
  })
}

export type EntityTestsuiteParams<R extends RawEntity, S extends Schema<R>, T extends Entity> = {
  ctor: { new(input: R): T },
  schema: S,
  validInputs: R[],
  invalidInputs: R[]
}

export function createEntityCreationTests<R extends RawEntity, S extends Schema<R>, T extends Entity>(params: EntityTestsuiteParams<R, S, T>) {
  describe(`Testing the entity '${params.ctor.name}'`, () => {

    describe("creating this object", () => {

      describe("with valid input", () => {

        params.validInputs.forEach(validInput => {

          it(`is ensured that safely parsing the valid input '${JSON.stringify(validInput)}' results in the successful creation of the object`, () => {
            safeParseEntity(validInput, params.schema, params.ctor)
              .doEffect((value: T) => {
                expect(value).toEqual(new params.ctor(validInput))
              })
              .doFailureEffect(errors => {
                throw new Error(`The creation of the object failed with the following errors: ${errors.map(error => error.message).join(', ')}`)
              })
          })

          it(`is ensured that parsing the valid input '${JSON.stringify(validInput)}' results in the successful creation of the object`, () => {
            const entity = new params.ctor(validInput)
            expect(entity).toEqual(new params.ctor(validInput))
          })
        })
      })

      describe("with invalid input", () => {

        params.invalidInputs.forEach(invalidInput => {

          it(`is ensured that safely parsing the invalid input '${JSON.stringify(invalidInput)}' results in a failure`, () => {
            const result: Result<T, ZodError<R>> = safeParseEntity(invalidInput, params.schema, params.ctor)
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
            expect(parse).toThrow(`Precondtions to create ${params.ctor.name} not met`)
          })
        })
      })
    })
  })
}

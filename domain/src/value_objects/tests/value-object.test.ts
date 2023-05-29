import {
  NumberValueObject,
  NumberValueObjectSchema,
  safeParseNumber,
  safeParseString,
  StringValueObject,
  StringValueObjectSchema,
} from '../value-object';

describe("Testing the domain primitives", () => {

  describe("creating a string based domain primitive object", () => {

    it(`is ensured that safely parsing the valid input 'test' results in the successful creation of the object`, () => {
      const value = "Hello World"
      safeParseString({ value }, StringValueObjectSchema, StringValueObject)
        .doEffect((value: StringValueObject) => {
          expect(value).toBe(value)
        })
        .doFailureEffect((errors) => {
          throw new Error(`Expected '${value}' to be a valid '${StringValueObject.name}' but got errors: ${errors}`)
        })
    })

    it(`is ensured that parsing the invalid input '1' results in an exception`, () => {
      const parse = () => new StringValueObject({ value: 1 as any })
      expect(parse).toThrowError(`Precondtions to create ${StringValueObject.name} not met`)
    })
  })

  describe("creating a number based domain primitive object", () => {

    it(`is ensured that safely parsing the valid input '0' results in the successful creation of the object`, () => {
      const value = 0
      safeParseNumber({ value }, NumberValueObjectSchema, NumberValueObject)
        .doEffect((value: NumberValueObject) => {
          expect(value).toBe(value)
        })
        .doFailureEffect(() => {
          throw new Error(`Expected '${value}' to be a valid '${NumberValueObject.name}'`)
        })
    })

    it(`is ensured that parsing the invalid input '1' results in an exception`, () => {
      const value = "1" as any
      const parse = () => new NumberValueObject({ value })
      expect(parse).toThrowError(`Precondtions to create ${NumberValueObject.name} not met`)
    })
  })
})


import { Result } from 'pointchu.capabilities';
import { Schema, z, ZodError } from 'zod';
import { implement } from '../validation';

export interface RawStringValueObject {
  value: string
}

export const StringValueObjectSchema = implement<RawStringValueObject>().with({
  value: z.string(),
});

export class StringValueObject {
  protected _value: string

  constructor(input: RawStringValueObject) {
    const result = StringValueObjectSchema.safeParse(input)
    if (!result.success) {
      throw new Error(`Precondtions to create ${StringValueObject.name} not met`)
    }
    this._value = result.data.value
  }

  get value() {
    return this._value
  }

  toRaw(): RawStringValueObject {
    return {
      value: this.value,
    }
  }
}

export function safeParseString<R extends RawStringValueObject, S extends Schema<R>, T extends StringValueObject>(input: R, schema: S, c: { new(input: R): T }): Result<T, ZodError<R>> {
  const result = schema.safeParse(input)
  if (!result.success) {
    return Result.failure([result.error])
  }
  return Result.success(new c(result.data))
}

export interface RawNumberValueObject {
  value: number
}

export const NumberValueObjectSchema = implement<RawNumberValueObject>().with({
  value: z.number(),
});

export class NumberValueObject {
  protected _value: number

  constructor(input: RawNumberValueObject) {
    const result = NumberValueObjectSchema.safeParse(input)
    if (!result.success) {
      throw new Error(`Precondtions to create ${NumberValueObject.name} not met`)
    }
    this._value = result.data.value
  }

  get value() {
    return this._value
  }

  toRaw(): RawNumberValueObject {
    return {
      value: this.value,
    }
  }
}

export function safeParseNumber<R extends RawNumberValueObject, S extends Schema<R>, T extends NumberValueObject>(input: R, schema: S, c: { new(input: R): T }): Result<T, ZodError<R>> {
  const result = schema.safeParse(input)
  if (!result.success) {
    return Result.failure([result.error])
  }
  return Result.success(new c(result.data))
}

export type DomainValueObject = RawStringValueObject | RawNumberValueObject

export interface RawCompositeValueObject {
  values: Record<string, DomainValueObject>
}

export const CompositeValueObjectSchema = implement<RawCompositeValueObject>().with({
  values: z.record(z.string(), z.union([StringValueObjectSchema, NumberValueObjectSchema]))
});

export class CompositeValueObject {
  protected _values: Map<string, DomainValueObject>

  constructor(input: RawCompositeValueObject) {
    const result = CompositeValueObjectSchema.safeParse(input)
    if (!result.success) {
      throw new Error(`Precondtions to create ${NumberValueObject.name} not met`)
    }
    this._values = new Map<string, DomainValueObject>(Object.entries(result.data.values).map(([key, value]) => {
      if (typeof value.value === 'number') {
        return [key, new NumberValueObject({ value: value.value })]
      } else if (typeof value.value === 'string') {
        return [key, new StringValueObject({ value: value.value })]
      } else {
        throw new Error(`Unknown type of value ${value}`)
      }
    }))
  }

  get values() {
    return this._values
  }
}

export function safeParseObject<R extends RawCompositeValueObject, S extends Schema<R>, T extends CompositeValueObject>(input: R, schema: S, c: { new(input: R): T }): Result<T, ZodError<R>> {
  const result = schema.safeParse(input)
  if (!result.success) {
    return Result.failure([result.error])
  }
  return Result.success(new c(result.data))
}

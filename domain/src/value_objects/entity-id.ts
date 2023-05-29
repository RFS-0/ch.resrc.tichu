import { Schema, z } from 'zod';
import { implement } from '../validation';
import { type RawStringValueObject, StringValueObject } from './value-object';
import { v4 as uuid } from 'uuid';

export interface RawEntityId extends RawStringValueObject { }

const uuidv4Regex = /^([a-f0-9]{8}-[a-f0-9]{4}-[1-5][a-f0-9]{3}-[a-f0-9]{4}-[a-f0-9]{12}|00000000-0000-0000-0000-000000000000)$/i

export const EntityIdSchema = implement<RawEntityId>().with({
  value: z.string().regex(uuidv4Regex, 'Invalid EntityId')
});

export class EntityId extends StringValueObject {
  protected _value: string

  constructor(input: RawEntityId) {
    super(input)
    const result = EntityIdSchema.safeParse(input)
    if (!result.success) {
      throw new Error('Precondtions to create EntityId not met')
    }
    this._value = result.data.value
  }

  get value(): string {
    return this._value
  }
}

export interface IdSequence<T extends EntityId> {
  next(): T
}

export function createIdSequence<R extends RawEntityId, S extends Schema<R>, T extends EntityId>(schema: S, c: { new(input: R): T }): IdSequence<T> {
  return {
    next(): T {
      const result = schema.safeParse({ value: uuid() })
      if (!result.success) {
        throw new Error('Precondtions to create EntityId not met')
      }
      return new c(result.data)
    }
  }
}

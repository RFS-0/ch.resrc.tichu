import {Result} from 'pointchu.capabilities';
import {z, ZodError, type ZodTypeDef} from 'zod';
import {implement} from '../validation';

export interface RawIntendedEntity {
}

export const IntendedEntitySchema = implement<RawIntendedEntity>().with({});

export class IntendedEntity {
    constructor(input: RawIntendedEntity) {
        const result = IntendedEntitySchema.safeParse(input)
        if (!result.success) {
            throw new Error('Preconditions to create IntendedEntity not met')
        }
    }
}

export function safeParseIntention<R extends RawIntendedEntity, S extends z.ZodType<R, ZodTypeDef, R>, T extends IntendedEntity>(
    input: R,
    schema: S,
    c: { new(input: R): T }
): Result<T, ZodError<R>> {
    const result = schema.safeParse(input)
    if (!result.success) {
        return Result.failure([result.error])
    }
    return Result.success(new c(result.data))
}

export interface RawEntity {
}

export const EntitySchema = implement<RawEntity>().with({});

export abstract class Entity {
    protected constructor(input: RawEntity) {
        const result = EntitySchema.safeParse(input)
        if (!result.success) {
            throw new Error(`Preconditions to create ${Entity.name} not met`)
        }
    }
}

export function safeParseEntity<R extends RawEntity, S extends z.ZodType<R, ZodTypeDef, R>, T extends Entity>(
    input: R,
    schema: S,
    c: { new(input: R): T }
): Result<T, ZodError<R>> {
    const result = schema.safeParse(input)
    if (!result.success) {
        return Result.failure([result.error])
    }
    return Result.success(new c(input))
}

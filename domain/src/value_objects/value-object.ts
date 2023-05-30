import {Result} from 'pointchu.capabilities';
import {z, ZodError, type ZodTypeDef} from 'zod';
import {implement} from '../validation';

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
            throw new Error(`Preconditions to create ${StringValueObject.name} not met`)
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

export function safeParseString<R extends RawStringValueObject, S extends z.ZodType<R, ZodTypeDef, R>, T extends StringValueObject>(input: R, schema: S, c: {
    new(input: R): T
}): Result<T, ZodError<R>> {
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
            throw new Error(`Preconditions to create ${NumberValueObject.name} not met`)
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

export function safeParseNumber<R extends RawNumberValueObject, S extends z.ZodType<R, ZodTypeDef, R>, T extends NumberValueObject>(
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

export interface RawCompositeValueObject {
}

export const CompositeValueObjectSchema = implement<RawCompositeValueObject>().with({
});

export class CompositeValueObject {
    constructor(input: RawCompositeValueObject) {
        const result = CompositeValueObjectSchema.safeParse(input)
        if (!result.success) {
            throw new Error(`Preconditions to create ${CompositeValueObject .name} not met`)
        }
    }
}

export function safeParseObject<R extends RawCompositeValueObject, S extends z.ZodType<R, ZodTypeDef, R>, T extends CompositeValueObject>(
    input: R,
    schema: S, c: { new(input: R): T }
): Result<T, ZodError<R>> {
    const result = schema.safeParse(input)
    if (!result.success) {
        return Result.failure([result.error])
    }
    return Result.success(new c(result.data))
}

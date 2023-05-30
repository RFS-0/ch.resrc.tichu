import {type RawStringValueObject, StringValueObject} from './value-object';
import {implement} from '../validation';
import {z} from 'zod';
import {v4 as uuid} from 'uuid';

export interface RawJoinCode extends RawStringValueObject {
}

const uuidv4Regex = /^([a-f0-9]{8}-[a-f0-9]{4}-[1-5][a-f0-9]{3}-[a-f0-9]{4}-[a-f0-9]{12}|00000000-0000-0000-0000-000000000000)$/i

export const JoinCodeSchema = implement<RawJoinCode>().with({
    value: z.string().regex(uuidv4Regex, `Invalid JoinCode`)
});

export class JoinCode extends StringValueObject {
    constructor(input: RawJoinCode) {
        super(input);
        const result = JoinCodeSchema.safeParse(input);
        if (!result.success) {
            throw new Error(`Preconditions to create ${JoinCode.name} not met`);
        }
        this._value = result.data.value;
    }

    static create(): JoinCode {
        return new JoinCode({value: uuid()});
    }
}

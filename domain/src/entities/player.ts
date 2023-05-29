import {type RawEntity} from './entity';
import {implement} from '../validation';
import {EntityIdSchema, PlayerId} from '../value_objects';
import {z} from 'zod';

export interface RawPlayer extends RawEntity {
    id: string,
    name: string
}

export const PlayerSchema = implement<RawPlayer>().with({
    id: EntityIdSchema.shape.value,
    name: z.string(),
});

export class Player {

    private _id: PlayerId;
    private _name: string;

    constructor(
        input: RawPlayer
    ) {
        const result = PlayerSchema.safeParse(input);
        if (!result.success) {
            throw new Error(`Preconditions to create ${Player.name} not met because ${result.error.message}`)
        }
        this._id = new PlayerId({value: result.data.id});
        this._name = result.data.name;
    }

    get id(): PlayerId {
        return this._id;
    }

    get name(): string {
        return this._name;
    }

    toRaw(): RawPlayer {
        return {
            id: this.id.value,
            name: this.name
        }
    }
}

import {EntitySchema, type RawEntity} from './entity';
import {implement} from '../validation';
import {EntityIdSchema, PlayerId} from '../value_objects';
import {z, type ZodTypeDef} from 'zod';
import {Game, GameSchema} from './game';

export interface RawPlayer extends RawEntity {
    id: string;
    userId: string | null;
    name: string;
}

export const PlayerSchema: z.ZodType<RawPlayer, ZodTypeDef, RawPlayer> = implement<RawPlayer>()
    .extend(EntitySchema)
    .with({
        id: EntityIdSchema.shape.value,
        userId: z.string().nullable(),
        name: z.string(),
    });

export class Player {

    private _id: PlayerId;
    private _userId: string | null;
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
        this._userId = result.data.userId;
    }

    private mutate(mutate: (player: Player) => void): Player {
        const validate = (player: Player) => {
            const result = PlayerSchema.safeParse(player.toRaw());
            if (!result.success) {
                throw new Error(`Preconditions to mutate ${Player.name} not met because ${result.error.message}`)
            }
            return player;
        }
        const copy = new Player(this.toRaw());
        mutate(copy)
        return validate(copy);
    }

    changeName(name: string): Player {
        return this.mutate(player => player._name = name);
    }

    get id(): PlayerId {
        return this._id;
    }

    get name(): string {
        return this._name;
    }

    get userId(): string | null {
        return this._userId;
    }

    toRaw(): RawPlayer {
        return {
            id: this.id.value,
            userId: this.userId,
            name: this.name
        }
    }
}

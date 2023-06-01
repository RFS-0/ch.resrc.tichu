import {implement} from '../validation';
import {CompositeValueObject, CompositeValueObjectSchema, type RawCompositeValueObject} from './value-object';
import {z, type ZodTypeDef} from 'zod';
import {PlayerId} from './player-id';

export interface RawTeam extends RawCompositeValueObject {
    index: number
    name: string
    playerIds: string[]
}

export const TeamSchema: z.ZodType<RawTeam, ZodTypeDef, RawTeam> = implement<RawTeam>()
    .extend(CompositeValueObjectSchema)
    .with({
        index: z.number().min(0).max(1),
        name: z.string(),
        playerIds: z.array(z.string()).max(2),
    });

export class Team extends CompositeValueObject {

    private _index: number;
    private _name: string;
    private _playerIds: PlayerId[];

    constructor(
        input: RawTeam,
    ) {
        super(input);
        const result = TeamSchema.safeParse(input);
        if (!result.success) {
            throw new Error(`Preconditions to create ${Team.name} not met because ${result.error.message}`)
        }
        this._index = result.data.index;
        this._name = result.data.name;
        this._playerIds = result.data.playerIds.map(playerId => new PlayerId({value: playerId}));
    }

    removePlayer(index: number): Team {
        this._playerIds.splice(index, 1);
        return this;
    }

    get index(): number {
        return this._index;
    }

    get name(): string {
        return this._name;
    }

    set name(name: string) {
        this._name = name;
    }

    get playerIds(): PlayerId[] {
        return this._playerIds;
    }

    toRaw(): RawTeam {
        return {
            index: this.index,
            name: this.name,
            playerIds: this.playerIds.map(playerId => playerId.value),
        }
    }
}

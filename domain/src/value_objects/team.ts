import {implement} from '../validation';
import {CompositeValueObject, CompositeValueObjectSchema, type RawCompositeValueObject} from './value-object';
import {z, type ZodTypeDef} from 'zod';
import {PlayerId} from './player-id';

export interface RawTeam extends RawCompositeValueObject {
    index: number
    name: string
    players: Map<number, string>
}

export const TeamSchema: z.ZodType<RawTeam, ZodTypeDef, RawTeam> = implement<RawTeam>()
    .extend(CompositeValueObjectSchema)
    .with({
        index: z.number().min(0).max(1),
        name: z.string(),
        players: z.map(z.number(), z.string()),
    });

export class Team extends CompositeValueObject {

    private _index: number;
    private _name: string;
    private _playerIds: Map<number, PlayerId>;

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
        this._playerIds = Array.from(result.data.players.entries())
                               .reduce(
                                 (players, [index, playerId]) => {
                                     players.set(index, new PlayerId({value: playerId}));
                                     return players;
                                 },
                                 new Map<number, PlayerId>()
                             );
    }

    removePlayer(index: number): Team {
        this._playerIds.delete(index);
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

    get players(): Map<number, PlayerId> {
        return this._playerIds;
    }

    get playerIds(): PlayerId[] {
        return Array.from(this._playerIds.values());
    }

    toRaw(): RawTeam {
        return {
            index: this.index,
            name: this.name,
            players: Array.from(this._playerIds)
                          .reduce(
                              (players, [index, playerId]) => {
                                  players.set(index, playerId.value);
                                  return players;
                              },
                              new Map<number, string>()
                          )
        }
    }
}

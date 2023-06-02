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

    private mutate(mutate: (team: Team) => void): Team {
        const validate = (team: Team) => {
            const result = TeamSchema.safeParse(team.toRaw());
            if (!result.success) {
                throw new Error(`Preconditions to mutate ${Team.name} not met because ${result.error.message}`)
            }
            return team;
        }
        const copy = new Team(this.toRaw());
        mutate(copy)
        return validate(copy);
    }

    hasPlayer(playerId: PlayerId) {
        return Array.from(this._playerIds.values()).some(id => id.value === playerId.value);
    }

    addPlayer(playerIndex: number, playerId: PlayerId) {
        if (playerIndex < 0 || playerIndex > 1) {
            throw new Error('Implementation defect: playerIndex must be 0 or 1');
        }
        return this.mutate(team => team._playerIds.set(playerIndex, playerId));
    }

    getPlayer(playerIndex: number) {
        if (playerIndex < 0 || playerIndex > 1) {
            throw new Error('Implementation defect: playerIndex must be 0 or 1');
        }
        return this._playerIds.get(playerIndex);
    }

    removePlayer(index: number): Team {
        if (index < 0 || index > 1) {
            throw new Error('Implementation defect: index must be 0 or 1');
        }
        return this.mutate(team => team._playerIds.delete(index));
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

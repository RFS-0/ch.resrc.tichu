import {RawTeam, Team, TeamSchema} from '../team';
import {createObjectPrimitiveCreationTests} from './domain-primitive-test-helper';

const validInputs = [
    {
        index: 0,
        name: 'John & Doe',
        players: new Map([
                [0, '8e3255a4-fe43-11ed-be56-0242ac120002'],
                [1, '934d1056-fe43-11ed-be56-0242ac120002'],
            ]
        )
    }
] as RawTeam[]

const invalidInputs = [
    {
        index: -1,
        name: 'Friedli',
        players: new Map([
                [0, '8e3255a4-fe43-11ed-be56-0242ac120002'],
                [1, '934d1056-fe43-11ed-be56-0242ac120002'],
            ]
        )
    },
    {
        index: 0,
        name: null, // invalid name,
        players: new Map([
                [0, '8e3255a4-fe43-11ed-be56-0242ac120002'],
                [1, '934d1056-fe43-11ed-be56-0242ac120002'],
            ]
        )
    }
]

createObjectPrimitiveCreationTests({
    ctor: Team,
    schema: TeamSchema,
    validInputs,
    // @ts-ignore
    invalidInputs
})

describe(`Testing the value object '${Team.name}'`, () => {
    describe("creating this object", () => {
        describe("with valid input", () => {
            validInputs.forEach(validInput => {
                it(`is ensured that the properties of '${JSON.stringify(validInput)}' are parsed correctly`, () => {
                    const team = new Team(validInput)
                    expect(team.name).toEqual(validInput.name);
                    expect(team.getPlayer(0)?.value).toEqual(validInput.players.get(0));
                    expect(team.getPlayer(1)?.value).toEqual(validInput.players.get(1));
                })
            })
        })
    })
})

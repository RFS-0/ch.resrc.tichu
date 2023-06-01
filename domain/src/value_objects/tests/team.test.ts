import {RawTeam, Team, TeamSchema} from '../team';
import {createObjectPrimitiveCreationTests} from './domain-primitive-test-helper';

const validInputs = [
    {
        index: 0,
        name: 'John & Doe',
        playerIds: [
            '8e3255a4-fe43-11ed-be56-0242ac120002',
            '934d1056-fe43-11ed-be56-0242ac120002',
        ]
    }
] as RawTeam[]

const invalidInputs = [
    {
        index: -1,
        name: 'Friedli',
        players: [
            '8e3255a4-fe43-11ed-be56-0242ac120002',
            '934d1056-fe43-11ed-be56-0242ac120002',
        ]
    },
    {
        index: 0,
        name: null, // invalid name,
        players: [
            '8e3255a4-fe43-11ed-be56-0242ac120002',
            '934d1056-fe43-11ed-be56-0242ac120002',
        ]
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
                    expect(team.playerIds[0].value).toEqual(validInput.playerIds[0]);
                    expect(team.playerIds[1].value).toEqual(validInput.playerIds[1]);
                })
            })
        })
    })
})

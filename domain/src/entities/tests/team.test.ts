import {createEntityCreationTests} from "./entity-test-helper"
import {RawTeam, Team, TeamSchema} from '../team';

const validInputs = [
    {
        id: '81d80a38-fe43-11ed-be56-0242ac120002',
        name: 'John & Doe',
        firstPlayer: {
            id: '8e3255a4-fe43-11ed-be56-0242ac120002',
            userId: 'user1',
            name: 'John',
        },
        secondPlayer: {
            id: '934d1056-fe43-11ed-be56-0242ac120002',
            userId: 'user2',
            name: 'Doe',
        }
    }
] as RawTeam[]

const invalidInputs = [
    {
        id: '8a70353b-baf3-4bed-be24-06020a62691z',// invalid id
        name: 'Friedli',
        firstPlayer: {
            id: '8a70353b-baf3-4bed-be24-06020a62691c',
            name: 'John',
        }

    },
    {
        id: '8a70353b-baf3-4bed-be24-06020a62691z',
        name: null, // invalid name,
        firstPlayer: {
            id: '8e3255a4-fe43-11ed-be56-0242ac120002',
            name: 'John',
        },
        secondPlayer: {
            id: '934d1056-fe43-11ed-be56-0242ac120002',
            name: 'John',
        }
    }
]

createEntityCreationTests({
    ctor: Team,
    schema: TeamSchema,
    validInputs,
    // @ts-ignore
    invalidInputs
})

describe(`Testing the entity '${Team.name}'`, () => {
    describe("creating this object", () => {
        describe("with valid input", () => {
            validInputs.forEach(validInput => {
                it(`is ensured that the properties of '${JSON.stringify(validInput)}' are parsed correctly`, () => {
                    const team = new Team(validInput)
                    expect(team.id.value).toEqual(validInput.id);
                    expect(team.name).toEqual(validInput.name);
                    expect(team.firstPlayer.toRaw()).toEqual(validInput.firstPlayer);
                    expect(team.secondPlayer.toRaw()).toEqual(validInput.secondPlayer);
                })
            })
        })
    })
})

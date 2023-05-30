import {createEntityCreationTests} from "./entity-test-helper"
import {Player, PlayerSchema, RawPlayer} from '../player';
import {safeParseEntity} from '../entity';

const validInputs = [
    {
        id: '8a70353b-baf3-4bed-be24-06020a62691c',
        userId: 'user1',
        name: 'John',
    }
] as RawPlayer[]

const invalidInputs = [
    {
        id: '8a70353b-baf3-4bed-be24-06020a62691z', // invalid id
        userId: 'user1',
        name: 'John',
    },
    {
        id: '8a70353b-baf3-4bed-be24-06020a62691a',
        userId: undefined, // invalid userId
        name: 'Joey'
    },
    {
        id: '8a70353b-baf3-4bed-be24-06020a62691a',
        userId: 'user1',
        name: null, // invalid name
    }
] as RawPlayer[]

createEntityCreationTests({
    ctor: Player,
    schema: PlayerSchema,
    validInputs,
    // @ts-ignore
    invalidInputs
})

describe(`Testing the entity ${Player.name}}`, () => {
    describe("creating this object", () => {
        describe("with valid input", () => {
            validInputs.forEach(validInput => {
                it(`is ensured that the properties of '${JSON.stringify(validInput)}' are parsed correctly`, () => {
                    const player = new Player(validInput)
                    expect(player.id.value).toEqual(validInput.id);
                    expect(player.name).toEqual(validInput.name);
                })
                it(`is ensured that safe parsing valid input results in success`, () => {
                    const result = safeParseEntity(validInput, PlayerSchema, Player);
                    expect(result.isSuccess()).toBe(true);
                })
            })
        })
    })
})

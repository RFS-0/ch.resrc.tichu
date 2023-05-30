import {Game, GameSchema, RawGame} from '../game';
import {safeParseEntity} from '../entity';
import {Rank, Tichu} from '../../value_objects';
import {createEntityCreationTests} from './entity-test-helper';

const validInputs: RawGame[] = [
    {
        id: '8510d73a-fe51-11ed-be56-0242ac120002',
        createdBy: '8dd99fa0-fe51-11ed-be56-0242ac120002',
        joinCode: '979b4782-fe51-11ed-be56-0242ac120002',
        teams: [
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
            },
            {
                id: 'c5cbe652-fe51-11ed-be56-0242ac120002',
                name: 'Fizz & Buzz',
                firstPlayer: {
                    id: 'd676be28-fe51-11ed-be56-0242ac120002',
                    userId: 'user3',
                    name: 'Fizz',
                },
                secondPlayer: {
                    id: 'dab96120-fe51-11ed-be56-0242ac120002',
                    userId: 'user4',
                    name: 'Buzz',
                }
            }
        ],
        rounds: [
            {
                roundNumber: 1,
                cardPoints: new Map<string, number>([
                    ['b919996e-9070-41d9-82be-a908e07c5952', 10],
                    ['53305f-b1aa-4ac6-b976-715c8af41e8d', 20],
                    ['b5e382-0e39-4983-b206-ae025d05eeba', 30],
                    ['480427-049e-4d50-afdd-0f87bafabf7a', 40],
                ]),
                ranks: new Map<string, Rank>([
                    ['b919996e-9070-41d9-82be-a908e07c5952', Rank.NONE],
                    ['53305f-b1aa-4ac6-b976-715c8af41e8d', Rank.NONE],
                    ['b5e382-0e39-4983-b206-ae025d05eeba', Rank.NONE],
                    ['480427-049e-4d50-afdd-0f87bafabf7a', Rank.NONE],
                ]),
                tichus: new Map<string, Tichu>([
                    ['b919996e-9070-41d9-82be-a908e07c5952', Tichu.NONE],
                    ['53305f-b1aa-4ac6-b976-715c8af41e8d', Tichu.NONE],
                    ['b5e382-0e39-4983-b206-ae025d05eeba', Tichu.NONE],
                    ['480427-049e-4d50-afdd-0f87bafabf7a', Tichu.NONE],
                ]),
            }
        ]
    }
];

const invalidInputs = [
    {
        id: '8510d73a-fe51-11ed-be56-0242ac12000z', // invalid id
        createdBy: '8dd99fa0-fe51-11ed-be56-0242ac120002',
        joinCode: '979b4782-fe51-11ed-be56-0242ac120002',
        teams: [
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
            },
            {
                id: 'c5cbe652-fe51-11ed-be56-0242ac120002',
                name: 'Fizz & Buzz',
                firstPlayer: {
                    id: 'd676be28-fe51-11ed-be56-0242ac120002',
                    userId: 'user3',
                    name: 'Fizz',
                },
                secondPlayer: {
                    id: 'dab96120-fe51-11ed-be56-0242ac120002',
                    userId: 'user4',
                    name: 'Buzz',
                }
            }
        ],
        rounds: []
    },
    {
        id: '8510d73a-fe51-11ed-be56-0242ac120002',
        createdBy: '8dd99fa0-fe51-11ed-be56-0242ac12000z', // invalid id
        joinCode: '979b4782-fe51-11ed-be56-0242ac120002',
        teams: [
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
            },
            {
                id: 'c5cbe652-fe51-11ed-be56-0242ac120002',
                name: 'Fizz & Buzz',
                firstPlayer: {
                    id: 'd676be28-fe51-11ed-be56-0242ac120002',
                    userId: 'user3',
                    name: 'Fizz',
                },
                secondPlayer: {
                    id: 'dab96120-fe51-11ed-be56-0242ac120002',
                    userId: 'user4',
                    name: 'Buzz',
                }
            }
        ],
        rounds: []
    }
] as RawGame[]

createEntityCreationTests({
    ctor: Game,
    schema: GameSchema,
    validInputs,
    invalidInputs
})

describe(`Testing the entity '${Game.name}'`, () => {
    describe("creating this object", () => {
        describe("with valid input", () => {
            validInputs.forEach(validInput => {
                it(`is ensured that the properties of '${JSON.stringify(validInput)}' are parsed correctly`, () => {
                    const game = new Game(validInput)
                    expect(game.id.value).toEqual(validInput.id);
                    expect(game.createdBy.value).toEqual(validInput.createdBy);
                    expect(game.joinCode.value).toEqual(validInput.joinCode);
                    expect(game.leftTeam.toRaw()).toEqual(validInput.teams[0]);
                    expect(game.rightTeam.toRaw()).toEqual(validInput.teams[1]);
                    expect(game.rounds.map(round => round.toRaw())).toEqual(validInput.rounds);
                })
                it(`is ensured that safe parsing valid input results in success`, () => {
                    const result = safeParseEntity(validInput, GameSchema, Game);
                    expect(result.isSuccess()).toBe(true);
                })
            })
        })
    })
})

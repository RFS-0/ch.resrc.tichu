import {Rank, Round, Tichu} from '../../value_objects';

const validInputs = [
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

const invalidInputs = [
    {
        roundNumber: 0, // invalid round number
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
    },
    {
        roundNumber: 1,
        cardPoints: null, // invalid card points
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
    },
    {
        roundNumber: 1,
        cardPoints: new Map<string, number>([
            ['b919996e-9070-41d9-82be-a908e07c5952', 10],
            ['53305f-b1aa-4ac6-b976-715c8af41e8d', 20],
            ['b5e382-0e39-4983-b206-ae025d05eeba', 30],
            ['480427-049e-4d50-afdd-0f87bafabf7a', 40],
        ]),
        ranks: null, // invalid ranks
        tichus: new Map<string, Tichu>([
            ['b919996e-9070-41d9-82be-a908e07c5952', Tichu.NONE],
            ['53305f-b1aa-4ac6-b976-715c8af41e8d', Tichu.NONE],
            ['b5e382-0e39-4983-b206-ae025d05eeba', Tichu.NONE],
            ['480427-049e-4d50-afdd-0f87bafabf7a', Tichu.NONE],
        ]),
    },
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
        tichus: null, // invalid tichus
    }
]

describe(`Testing the value object ${Round.name}}`, () => {
    describe("creating this object", () => {
        describe("with valid input", () => {
            validInputs.forEach(validInput => {
                it(`is ensured that the properties of '${JSON.stringify(validInput)}' are parsed correctly`, () => {
                    const round = new Round(validInput)
                    expect(round.roundNumber).toEqual(validInput.roundNumber);
                    expect(round.cardPoints).toEqual(validInput.cardPoints);
                    expect(round.ranks).toEqual(validInput.ranks);
                    expect(round.tichus).toEqual(validInput.tichus);
                })
            })
        })

        describe("with invalid input", () => {
            invalidInputs.forEach(invalidInput => {
                it(`is ensured that the properties of '${JSON.stringify(invalidInput)}' are parsed correctly`, () => {
                    // @ts-ignore
                    expect(() => new Round(invalidInput)).toThrowError();
                })
            })
        })
    })
})

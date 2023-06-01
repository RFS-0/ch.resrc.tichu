import {Rank, type RawRound, Tichu} from 'pointchu.domain';

export interface RoundView {
    roundNumber: number,
    cardPoints: Map<number, number>,
    ranks: Map<string, Rank>,
    tichus: Map<string, Tichu>,
}

export function mapToRawRound(round: RoundView): RawRound {
    return {
        roundNumber: round.roundNumber,
        cardPoints: round.cardPoints,
        ranks: round.ranks,
        tichus: round.tichus,
    }
}

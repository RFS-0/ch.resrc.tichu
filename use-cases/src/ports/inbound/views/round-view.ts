import {
    Rank,
    Tichu
} from 'pointchu.domain';

export interface RoundView {
    readonly  roundNumber: number,
    readonly cardPoints: Map<string, number>,
    readonly ranks: Map<string, Rank>,
    readonly tichus: Map<string, Tichu>,
}

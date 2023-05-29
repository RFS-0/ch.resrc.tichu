import {type PlayerView} from './player-view';

export interface TeamView {
    id: string
    name: string
    firstPlayer: PlayerView
    secondPlayer: PlayerView
}

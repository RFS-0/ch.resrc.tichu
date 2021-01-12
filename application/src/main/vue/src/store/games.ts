import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators'
import { Game, Round, CardPoints, Tichu, GrandTichu, Ranks } from '../types'

@Module({ namespaced: false, name: 'games' })
class Games extends VuexModule {
  public games = [
    {
      id: '3466b1bc-821c-42f6-9f83-1d29cb58699e',
      leftTeam: '71a0b577-8f64-412c-8da6-835eb88b77e0',
      rightTeam: 'd6c7fddca8ba-4509-95e2-27b428ba21b4',
      joinCode: 'ABCD',
      rounds: [
        {
          roundNumber: 0,
          cardPoints: [
            {
              teamId: '71a0b577-8f64-412c-8da6-835eb88b77e0',
              value: 60
            } as CardPoints,
            {
              teamId: 'd6c7fddca8ba-4509-95e2-27b428ba21b4',
              value: 40
            } as CardPoints,
          ],
          tichus: [
            {
              playerId: '094667f2-c967-476a-933d-1106ba61af51',
              successful: true,
              value: 100
            } as Tichu
          ],
          ranks: {
            firstPlayer: '094667f2-c967-476a-933d-1106ba61af51',
            secondPlayer: '91584076-4929-11eb-b378-0242ac130002',
            thirdPlayer: '9bbce77e-4929-11eb-b378-0242ac130002',
            fourthPlayer: '8329001b-591e-4496-96ca-c71a331c29f8'
          } as Ranks
        } as Round,
        {
          roundNumber: 1,
          cardPoints: [
            {
              teamId: '71a0b577-8f64-412c-8da6-835eb88b77e0',
              value: 15
            } as CardPoints,
            {
              teamId: 'd6c7fddca8ba-4509-95e2-27b428ba21b4',
              value: 85
            } as CardPoints,
          ],
          grandTichus: [
            {
              playerId: '91584076-4929-11eb-b378-0242ac130002',
              successful: true,
              value: 200
            } as GrandTichu
          ],
          ranks: {
            firstPlayer: '91584076-4929-11eb-b378-0242ac130002',
            secondPlayer: '094667f2-c967-476a-933d-1106ba61af51',
            thirdPlayer: '8329001b-591e-4496-96ca-c71a331c29f8',
            fourthPlayer: '9bbce77e-4929-11eb-b378-0242ac130002',
          } as Ranks
        } as Round,
      ]
    } as Game
  ]

  get gameById() {
    return (id: string) => {
      return this.games.find(
        (game) => game.id == id
      )
    }
  }
}

export default Games

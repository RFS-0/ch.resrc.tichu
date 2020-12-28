import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators'
import { Game } from '../types'

@Module({ namespaced: false, name: 'games' })
class Games extends VuexModule {
  public games = [
    {
      id: '3466b1bc-821c-42f6-9f83-1d29cb58699e',
      leftTeam: '71a0b577-8f64-412c-8da6-835eb88b77e0',
      rightTeam: 'd6c7fddca8ba-4509-95e2-27b428ba21b4',
      joinCode: 'ABCD',
      rounds: []
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

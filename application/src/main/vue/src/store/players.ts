import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators'
import { Player } from '../types'

@Module({ namespaced: false, name: 'games' })
class Players extends VuexModule {
  public players = [
    {
      id: '094667f2-c967-476a-933d-1106ba61af51',
      name: 'player 1'
    } as Player,
    {
      id: '8329001b-591e-4496-96ca-c71a331c29f8',
      name: 'player 2'
    } as Player,
    {
      id: '91584076-4929-11eb-b378-0242ac130002',
      name: 'player 3'
    } as Player,
    {
      id: '9bbce77e-4929-11eb-b378-0242ac130002',
      name: 'player 4'
    } as Player,
    {
      id: '24b173d0-49be-11eb-b378-0242ac130002',
      name: 'test player'
    } as Player
  ]

  get playerById() {
    return (id: string) => {
      return this.players.find(
        (player) => player.id == id
      )
    }
  }
}

export default Players

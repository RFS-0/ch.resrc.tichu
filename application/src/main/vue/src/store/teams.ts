import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators'
import { Team, AddPlayerInfo, RemovePlayerInfo } from '../types'

@Module({ namespaced: false, name: 'games' })
class Teams extends VuexModule {
  public teams = [
    {
      id: '71a0b577-8f64-412c-8da6-835eb88b77e0',
      name: 'team grün',
      firstPlayer: '094667f2-c967-476a-933d-1106ba61af51',
      // secondPlayer: undefined
      secondPlayer: '8329001b-591e-4496-96ca-c71a331c29f8'
    } as Team,
    {
      id: 'd6c7fddca8ba-4509-95e2-27b428ba21b4',
      name: 'team grot',
      firstPlayer: '91584076-4929-11eb-b378-0242ac130002',
      // secondPlayer: undefined
      secondPlayer: '9bbce77e-4929-11eb-b378-0242ac130002'
    } as Team
  ]

  // @Mutation
  // addplayertoteam(value: AddPlayerInfo) {
  //   console.log('added player')
  //   this.teams[1].secondPlayer = '9bbce77e-4929-11eb-b378-0242ac130002';
  // }

  @Mutation
  addplayertoteam(info: AddPlayerInfo) {
    console.log('adding player to team')
    const index = this.teams.findIndex(
      (team) => team.id == info.teamId
    )
    console.log(`index: ${index}`)
    if (index >= 0) {
      if (!this.teams[index].firstPlayer)
        this.teams[index].firstPlayer = '24b173d0-49be-11eb-b378-0242ac130002';
      else if (!this.teams[index].secondPlayer)
        this.teams[index].secondPlayer = '24b173d0-49be-11eb-b378-0242ac130002';
    }
  }

  @Mutation
  removeplayerfromteam(info: RemovePlayerInfo) {
    console.log('removing player from team')
    const index = this.teams.findIndex(
      (team) => team.id == info.teamId
    )
    console.log(`index: ${index}`)
    if (index >= 0) {
      if (this.teams[index].firstPlayer == info.playerId)
        this.teams[index].firstPlayer = undefined;
      else if (this.teams[index].secondPlayer == info.playerId)
        this.teams[index].secondPlayer = undefined;
    }
  }

  get teamById() {
    return (id: string) => {
      return this.teams.find(
        (team) => team.id == id
      )
    }
  }
}

export default Teams

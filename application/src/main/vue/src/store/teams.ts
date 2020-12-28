import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators'
import { Team } from '../types'

@Module({ namespaced: false, name: 'games' })
class Teams extends VuexModule {
  public teams = [
    {
      id: '71a0b577-8f64-412c-8da6-835eb88b77e0',
      name: 'team grün',
      firstPlayer: '094667f2-c967-476a-933d-1106ba61af51',
      secondPlayer: '8329001b-591e-4496-96ca-c71a331c29f8'
    } as Team,
    {
      id: 'd6c7fddca8ba-4509-95e2-27b428ba21b4',
      name: 'team grot',
      firstPlayer: '91584076-4929-11eb-b378-0242ac130002',
      // secondPlayer: '9bbce77e-4929-11eb-b378-0242ac130002'
    } as Team
  ]

  get teamById() {
    return (id: string) => {
      return this.teams.find(
        (team) => team.id == id
      )
    }
  }
}

export default Teams

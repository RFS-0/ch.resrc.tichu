//https://blog.logrocket.com/how-to-write-a-vue-js-app-completely-in-typescript/

import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators'

@Module({ namespaced: true, name: 'test' })
class Game extends VuexModule {
  public name = ''
  public id = 0
  
  @Mutation
  public setName(newName: string): void {
    this.name = newName;
  }

  @Action
  public updateName(newName: string): void {
    this.context.commit('setName', newName)
  }
}

export default Game

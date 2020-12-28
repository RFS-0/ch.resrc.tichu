import Vue from "vue";
import Vuex from "vuex";
import Games from "@/store/games"
import Teams from "@/store/teams"
import Players from "@/store/players"

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: {
    Players,
    Teams,
    Games
  }
});

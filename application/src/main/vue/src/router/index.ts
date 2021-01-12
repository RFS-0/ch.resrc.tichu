import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from '@/views/Home.vue'
import Lobby from '@/views/Lobby.vue'
import Game from '@/views/Game.vue'
import Fullscreen from '@/views/Fullscreen.vue'

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/Lobby',
    name: 'Lobby',
    component: Lobby
  },
  {
    path: '/Game',
    name: 'Game',
    component: Game
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;

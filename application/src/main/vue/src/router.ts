import { userState } from "@/store";
import ConfigureGame from "@/use_cases/configure_game/ConfigureGame.vue";
import Login from "@/use_cases/login/Login.vue";
import PlayGame from "@/use_cases/play_game/PlayGame.vue";
import ShowGameStatistics from "@/use_cases/show_game_statistics/ShowGameStatistics.vue";
import Start from "@/use_cases/start/Start.vue";
import { isNone } from "fp-ts/Option";
import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    redirect: "/start",
  },
  {
    path: "/login",
    name: "login",
    component: Login,
  },
  {
    path: "/start",
    name: "start",
    component: Start,
  },
  {
    path: "/configure-game/:game_id",
    name: "configure-game",
    component: ConfigureGame,
  },
  {
    path: "/play-game/:game_id",
    name: "play-game",
    component: PlayGame,
  },
  {
    path: "/show-game-statistics/:game_id",
    name: "show-game-statistics",
    component: ShowGameStatistics,
  },
];

const router = new VueRouter({
  mode: "hash",
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.name !== "login" && isNone(userState.user)) {
    next({ name: "login" });
  } else {
    next();
  }
});

export default router;

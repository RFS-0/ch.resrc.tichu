import Vue from "vue";
import VueRouter, {RouteConfig} from "vue-router";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "Home",
    component: () => import("../views/HomeView.vue")
  },
  {
    path: "/create-game",
    name: "Create game",
    component: () => import("../views/CreateGameView.vue")
  },
  {
    path: "/join-game",
    name: "Join game",
    component: () => import("../views/JoinGameView.vue")
  },
  {
    path: "/statistics",
    name: "Statistics",
    component: () => import("../views/StatisticsView.vue")
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;

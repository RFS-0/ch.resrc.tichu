import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/start',
    },
    {
      path: "/start",
      name: "start",
      component: () => import('../views/StartView.vue')
    },
    {
      path: "/setup/:game_id",
      name: "setup",
      component: () => import('../views/SetupGameView.vue')
    },
    {
      path: "/play/:game_id",
      name: "play",
      component: () => import('../views/PlayView.vue')
    },
  ]
})

export default router

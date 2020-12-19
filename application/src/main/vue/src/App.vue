<template>
  <v-app>
    <v-navigation-drawer app
                         v-model="drawer"
                         absolute
                         temporary
    >
      <v-list
        nav
        dense
      >
        <v-list-item-group
          v-model="group"
          @click="drawer = false"
        >
          <home-item></home-item>
          <create-game-item></create-game-item>
          <join-game-item></join-game-item>
          <statistics-item></statistics-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar app>
      <v-app-bar
        absolute
        color="green darken-3"
      >
        <v-app-bar-nav-icon @click="drawer = true"
                            color="white"
        >
        </v-app-bar-nav-icon>
        <v-spacer></v-spacer>
      </v-app-bar>
    </v-app-bar>

    <v-main>

      <!-- Provides the application the proper gutter -->
      <v-container fluid>

        <!-- If using vue-router -->
        <router-view></router-view>
      </v-container>
    </v-main>

    <v-footer app>
      <!-- -->
    </v-footer>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";
import 'vuetify/dist/vuetify.min.css';
import CreateGameItem from "@/components/CreateGameItem.vue";
import JoinGameItem from "@/components/JoinGameItem.vue";
import StatisticsItem from "@/components/StatisticsItem.vue";
import HomeItem from "@/components/HomeItem.vue";

export default Vue.extend({
  name: "App",

  components: {
    HomeItem,
    StatisticsItem,
    CreateGameItem,
    JoinGameItem,
  },

  data: () => ({
    drawer: false,
    group: null,
  })
});
</script>

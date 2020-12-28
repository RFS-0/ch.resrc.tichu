<template>
  <div class="con">
    <div class="top">
    </div>
    <div class="mid">
      Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
    </div>
    <div class="bottom">
    </div>
  </div>
</template> 

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class CreateGameView extends Vue {
}
</script>

<style>
  .con {
    background-color: darkkhaki;
    width: 100%;
    height: 100%;

    display: grid;
    grid-template-rows: 80px 1fr 80px;
    /* padding: 10px */
  }
  
  .top {
    background-color: blanchedalmond;
    height: 100%;
    width: 100%;
    /* margin:10px; */
  }

  .mid {
    height: 100%;
    width: 100%;
    font-size: 3em;
    /* overflow: hidden; */
  }

  .bottom {
    background-color: crimson;
    height: 100%;
    width: 100%;
  }
</style>
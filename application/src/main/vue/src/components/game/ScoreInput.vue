<template>
  <div :style="colorVars">
    <div class="scroll-container">
      <div class="scroll-wheel" ref="scroll1" @scroll="onScroll1">
        <div class="space"></div>
        <div
          class="item"
          v-for="value in values" :key="value"
        >
          <h1 class="value left">
            {{value}}
          </h1>
        </div>
        <div class="space"></div>
        <div class="overlay">
          <div class="top"></div>
          <div></div>
          <div class="bottom"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref, Prop } from "vue-property-decorator";

Component.registerHooks([
  'mounted'
])

@Component
export default class ScoreInput extends Vue {

  @Ref('scroll1') readonly scrollWheel1!: HTMLElement;
  @Ref('scroll2') readonly scrollWheel2!: HTMLElement;

  @Prop() color!: string;
  @Prop() backgroundColor!: string;

  mounted() {
    this.scrollToValue(this.scrollWheel2, 0);
    this.scrollToValue(this.scrollWheel1, 0);
  }

  get colorVars() {
    return {
      '--bg-color':this.backgroundColor,
      '--color':this.color,
      }
  }

  private score1 = 0;
  private score2 = 0;

  public onScroll1(): void {
    if(this.scrollWheel1 == null)
      return

    const scroll = this.scrollWheel1.scrollTop;
    const index = Math.round(scroll / 66);
    if(index < 0 || index >= this.values.length)
      return;

    const score = this.values[index];
    if (this.score1 == score)
      return;

    this.score1 = score;
    if(this.score1 == 0 && this.score2 == 200)
      this.score2 = 200;
    else if(this.score1 == 200)
      this.score2 = 0;
    else
      this.score2 = 100 - this.score1;
    this.scrollToValue(this.scrollWheel2, this.score2);
    // console.log(this.selectedValue);
  }

  public onScroll2(): void {
    if(this.scrollWheel1 == null)
      return

    const scroll = this.scrollWheel2.scrollTop;
    const index = Math.round(scroll / 66);
    if(index < 0 || index >= this.values.length)
      return;

    const score = this.values[index];
    
    if(this.score2 == score)
      return;

    this.score2 = score;
    if(this.score2 == 0 && this.score1 == 200)
      this.score1 = 200;
    else if(this.score2 == 200)
      this.score1 = 0;
    else
      this.score1 = 100 - this.score2;
    this.scrollToValue(this.scrollWheel1, this.score1);
  }

  public scrollTo50(): void {
    const index = this.values.indexOf(50);
    const scroll = index * 66;
    this.scrollWheel2.scrollTop = scroll;
  }

  public scrollToValue(scrollWheel: HTMLElement, score: number): void
  {
    const index = this.values.indexOf(score);
    const scroll = index * 66;
    scrollWheel.scrollTop = scroll;
  }

  private values = 
    [-25,-20,-15,-10,-5,
    0,5,10,15,20,
    25,30,35,40,45,
    50,55,60,65,70,
    75,80,85,90,95,
    100,105,110,115,120,
    125,200]
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.scroll-container {
  display: grid;
  grid-template-columns: auto min-content auto;
  width: 110px;
  position: relative;
  /* background-color: cornflowerblue; */
}

.scroll-wheel {
  /* position: relative; */
  overflow-x: hidden;
  overflow-y: scroll;
  scroll-snap-type: y mandatory;
  /* background-color: aqua; */
  margin: auto;

  --height: 80px;
  --item-height: 66px;
  --space: calc((var(--height) - var(--item-height)) / 2);

  height: var(--height);
  display: grid;
  /* gap: 1px; */
  grid-template-rows: var(--space) repeat(32, var(--item-height)) var(--space);
}

.space {
  scroll-snap-align: center;
  display: block;
  /* background-color: chocolate; */
  height: 100%;
}

.item {
  scroll-snap-align: center;
  display: block;
  /* background-color:crimson; */
  height: 100%;
  max-height: var(--item-height);
  overflow: hidden;
}

.value {
  font-size: 60px;
  font-family: Lato;
  color: var(--color);
}

.left {
  margin-left: auto;
}

.right {
  margin-right: auto;
}

.overlay {
  z-index: 1;
  position: absolute;
  width: 110px;
  height: var(--height);
  /* height: calc(var(--height) / 4); */
  /* background-image: linear-gradient(var(--color-main), var(--color-main-transparent)); */
  /* background-color: orange; */
  pointer-events: none;
  display: grid;
  grid-template-rows: 1fr 2fr 1fr;
}

.top {
  /* background-color: black; */
  background-image: linear-gradient(var(--bg-color), var(--color-main-transparent));
}
.bottom {
  /* background-color: black; */
  background-image: linear-gradient(var(--color-main-transparent), var(--bg-color));
}

/* .child {
  background-color:red;
  font-size: 1em;
  display: block;
  widows: 100%;
  margin: auto;
} */
</style>
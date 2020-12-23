<template>
  <div id="app" ref="app">
    <router-view></router-view>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref } from "vue-property-decorator";

Component.registerHooks([
  'mounted'
])

@Component
export default class ScoreInput extends Vue {
  @Ref('app') app: HTMLElement | undefined

  mounted(){
    this.app?.style.setProperty('--window-height', `${window.innerHeight}px`);
    // this.$refs.app.style.setProperty('--window-height', `10px`);
  }
}
</script>

<style>
/* Reset Css */
/* http://meyerweb.com/eric/tools/css/reset/ 
   v2.0 | 20110126
   License: none (public domain)
*/

html, body, div, span, applet, object, iframe,
h1, h2, h3, h4, h5, h6, p, blockquote, pre,
a, abbr, acronym, address, big, cite, code,
del, dfn, em, img, ins, kbd, q, s, samp,
small, strike, strong, sub, sup, tt, var,
b, u, i, center,
dl, dt, dd, ol, ul, li,
fieldset, form, label, legend,
table, caption, tbody, tfoot, thead, tr, th, td,
article, aside, canvas, details, embed, 
figure, figcaption, footer, header, hgroup, 
menu, nav, output, ruby, section, summary,
time, mark, audio, video {
	margin: 0;
	padding: 0;
	border: 0;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
}
/* HTML5 display-role reset for older browsers */
article, aside, details, figcaption, figure, 
footer, header, hgroup, menu, nav, section {
	display: block;
}
body {
	line-height: 1;
}
ol, ul {
	list-style: none;
}
blockquote, q {
	quotes: none;
}
blockquote:before, blockquote:after,
q:before, q:after {
	content: '';
	content: none;
}
table {
	border-collapse: collapse;
	border-spacing: 0;
}


/* global Color Variables */
:root {
  --color-main: rgb(221, 215, 181);
  --color-main-transparent: rgba(221, 215, 181, 0);
  --color-team-1: rgb(62, 197, 145);
  --color-team-2: rgb(214, 86, 97);
  --color-dark: rgb(25, 29, 49);
  --color-light: rgb(247, 242, 216);

  font-family: Arial, Helvetica, sans-serif;
}

#app {
  --window-height: 100vh;
  overflow: hidden;
  width: 100vw;
  height: var(--window-height);
  /* height: -webkit-fill-available; */
  background-color: var(--color-main);
}

/* App Style */
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>

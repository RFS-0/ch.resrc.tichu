<script src="router.ts"></script>
<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script lang="ts">
import {endpointRegistry} from '@/configuration/application-configuration';
import 'vue-meta';
import {Component, Provide, Vue} from 'vue-property-decorator';

Component.registerHooks(['mounted']);

@Component
export default class App extends Vue {
  @Provide('endpoints')
  private endpoints = endpointRegistry();
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Lato:wght@900&display=swap');

@keyframes slide-in-input {
  from {
    width: 0;
  }
  to {
    width: var(--input-width);
  }
}

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
menu, nav, response, ruby, section, summary,
time, mark, audio, video {
  margin: 0;
  padding: 0;
  border: 0;
  vertical-align: baseline;
}

/* HTML5 display-role reset for older browsers */
article, aside, details, figcaption, figure,
footer, header, hgroup, menu, nav, section {
  display: block;
}

body {
  line-height: 1;
  font-size: clamp(0.1em, 0.75em + 0.75vw + 1vh, 2em);
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
  --color-main-dark: rgb(170, 166, 141);
  --color-left-team: rgb(62, 197, 145);
  --color-left-team-dark: rgb(45, 143, 105);
  --color-right-team: rgb(214, 86, 97);
  --color-right-team-dark: rgb(160, 63, 71);
  --color-dark: rgb(25, 29, 49);
  --sys-on-surface: #191D317F;

  --color-dark-light: rgb(118, 128, 236);
  --color-light: rgb(247, 242, 216);

  /* font-family: Arial, Helvetica, sans-serif; */
  font-family: 'Lato', sans-serif;
}

.light-background {
  color: var(--color-dark);
  background: var(--color-light);
}

.left-team-background {
  color: var(--color-main);
  background: var(--color-left-team);
}

.left-team-background-dark {
  color: var(--color-main);
  background: var(--color-left-team-dark);
}

.right-team-background {
  color: var(--color-main);
  background: var(--color-right-team);
}

.right-team-background-dark {
  color: var(--color-main);
  background: var(--color-right-team-dark);
}

/* App Style */
#app {
  /* font-family: Avenir, Helvetica, Arial, sans-serif; */
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  overflow: hidden;
  width: 100vw;
  height: 100vh;
  background-color: var(--color-main);
}

.text--xl {
  font-size: clamp(0.1em, 1em + 2vw + 2vh, 5em);
}

.text--lg {
  font-size: clamp(0.1em, 0.9em + 0.9vw + 1.5vh, 4em);
}

.text--md {
  font-size: clamp(0.1em, 0.8em + 0.8vw + 1.25vh, 3em);
}

.text--sm {
  font-size: clamp(0.1em, 0.5em + 0.5vw + 0.5vh, 0.8em);
}

.container--row {
  display: flex;
  flex-direction: row;
  align-items: center;
  height: 100%;
  width: 100%;
}

.container--column {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
}

.container--column-start {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 100%;
  width: 100%;
}

.button--xl-dark {
  background-color: var(--color-dark);
  color: var(--color-main);
  width: 75vw;
  height: 20vh;
  border-radius: 2vh;
  margin: 1vh
}

.button--lg-dark {
  background-color: var(--color-dark);
  color: var(--color-main);
  width: 80%;
  height: 80%;
  border-radius: 2vh;
  margin: 1vh
}

.button--lg {
  cursor: pointer;
  color: var(--color-main);
  width: 80%;
  height: 80%;
  border-radius: 2vh;
  margin: 1vh
}

.button--md {
  width: 15vh;
  height: 15vh;
  border-radius: 2vh;
  margin: 1vh
}

.button--md-dark {
  background-color: var(--color-dark);
  color: var(--color-main);
  width: 15vh;
  height: 15vh;
  border-radius: 2vh;
  margin: 1vh
}

.button--sm {
  width: 10vh;
  height: 10vh;
  border-radius: 2vh;
  margin: 1vh
}

.button--sm-dark {
  background-color: var(--color-dark);
  color: var(--color-main);
  width: 10vh;
  height: 10vh;
  border-radius: 2vh;
  margin: 1vh
}

.margin--xl {
  margin: 3vh;
}

.margin--lg {
  margin: 2vh;
}

.margin--md {
  margin: 1vh;
}

.margin--sm {
  margin: 0.5vh;
}

.border-radius {
  border-radius: 3vh;
}

.input-container--center {
  display: flex;
  align-items: center;
  flex-direction: row;
  height: 100%;
  width: 100%;
  border-radius: 2vh;
}

.input-container--left ,.input-container--right{
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 4vw;
  border-radius: 5vh;
  margin-left: 2vw;
}

.input-container--right {
  flex-direction: row-reverse;
}

.input--text {
  animation-name: slide-in-input;
  animation-duration: 1s;
  width: var(--input-width);
  padding: 2vw;
  border: none;
  outline: none;
  margin: 2vw;
}

.input--text.narrow {
  width: 70%;
  padding: 0 1vw 0 1vw;
  margin: 0 1vw 0 1vw;
}


.even {
  background: var(--color-dark);
  color: var(--color-dark-light);
}

.odd {
  background: var(--color-dark-light);
  color: var(--color-dark);
}

.none {
  text-decoration: underline black;
}

.called {
  text-decoration: underline #ffdd00;
}

.failed {
  text-decoration: underline red;
}

.success {
  text-decoration: underline green;
}

.math {
  font-family: times new roman, serif;
  font-style: italic;
  text-align: center;
}
</style>

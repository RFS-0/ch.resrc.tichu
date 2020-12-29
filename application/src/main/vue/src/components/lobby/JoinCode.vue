<template>
  <div class="code-container">
    <h1 class="code">join code</h1>
    <div class="join-code">{{animatedCode}}</div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from "vue-property-decorator";
import 'vue-class-component/hooks'
import { Player } from "../../types";

@Component
export default class JoinCode extends Vue {
  @Prop(String) readonly code!: string
  private animatedCode = 'BA6KD'
  private readonly duration = 30
  private counter = 0
  mounted(){
    window.requestAnimationFrame(this.animateCode)
  }
  animateCode(): void {
    if(this.counter >= this.duration){
      this.animatedCode = this.code;
      return;
    }
    this.counter++
    
    let result             = '';
    const characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    const charactersLength = characters.length;
    for ( let i = 0; i < this.code.length; i++ ) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    this.animatedCode = result;

    window.requestAnimationFrame(this.animateCode)
  }
}
</script>

<style scoped>
  .code-container
  {
    display: grid;
    grid-template-rows: auto auto;
  }

  .code {
    margin: auto;
    margin-top: 15px;
    font-size: 16px;
  }

  .join-code {
    font-size: 50px;
    margin: auto;
    margin-top: 5px;
  }
</style>
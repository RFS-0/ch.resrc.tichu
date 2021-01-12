<template>
  <div
    class="player"
    :style="style"
    ref="container"
  >
    <div class="grid"
      :class="{
      'left-tall tall':isLeft && isTall,
      'left-wide':isLeft && isWide,
      'right-tall tall':isRight && isTall,
      'right-wide':isRight && isWide,
      }"
    >
      <div class="name">
        <h1>
          {{player.name.toUpperCase()}}
        </h1>
      </div>
      <div class="avatar">
        <PlayerAvatar/>
      </div>
      <div class="buttons" ref="buttons">
        <div class="tichu-overlay"
          :class="{
            'overlay-left':isLeft,
            'overlay-right':isRight,
            'overlay-tall':isTall,
            'overlay-hide':!selectingTichu
          }"
        >
          <div class="cancel" @click="removeTichuCall">
            <div>+</div>
          </div>
          <div class="select-tichu">
            <h1 @click="callTichu">TICHU</h1>
            <h1 class="gr">GR.</h1>
            <h1 @click="callGrandTichu">TICHU</h1>
          </div>
        </div>
        <div class="btn-tichu" @click="toggleSelectTichu">
          <div class="plus" v-if="isNoTichuCalled">+</div>
          <div class="tichu" v-if="isTichuCalled">TICHU</div>
          <div class="tichu grand" v-if="isGrandTichuCalled">GR.<br>TICHU</div>
        </div>
        <div class="btn-finish">
          <svg width="35" viewBox="0 0 50 40">
              <path d="M10 20 l 10 10 l 20 -20" fill="transparent" stroke="black" stroke-width="9" stroke-linejoin="round"/>
          </svg> 
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Ref, Prop, Emit } from "vue-property-decorator";
import { Round, Player } from '../../types';
import PlayerAvatar from '../lobby/PlayerAvatar.vue'

@Component({
  components: {
    PlayerAvatar
  }
})
export default class PlayerUi extends Vue {
  @Prop() round!: Round;
  @Prop() playerId!: string;

  @Prop() teamColor!: string;
  @Prop() teamColorDark!: string;
  @Prop() backgroundColor!: string;
  @Prop() side!: string;

  @Ref('container') readonly container!: HTMLDivElement;
  @Ref('buttons') readonly buttons!: HTMLDivElement;

  height = 1;
  width = 1;
  buttonsHeight = 1;
  selectingTichu = false;
  // calledTichu = '';

  get calledTichu(): string {
    const tichu = this.round.tichus?.find(
      (tichu) => tichu.playerId == this.playerId
    );
    if (tichu)
      return 'tichu';

    const grandTichu = this.round.grandTichus?.find(
      (grandTichu) => grandTichu.playerId == this.playerId
    );
    if (grandTichu)
      return 'grand';

    return '';
  }

  get player() {
    return this.$store.getters.playerById(this.playerId) as Player;
  }

  get style() {
    return {
      '--bg-color':this.backgroundColor,
      '--team-color':this.teamColor,
      '--team-color-dark':this.teamColorDark,
      '--buttons-height':this.buttonsHeight,
      }
  }

  get aspectRatio() {
    return this.width/this.height;
  }

  get isWide() {
    return this.aspectRatio > 1.2
  }
  get isTall() {
    return !this.isWide;
  }
  get isLeft() {
    return this.side == 'left';
  }
  get isRight() {
    return this.side == 'right';
  }
  get isTichuCalled() {
    return this.calledTichu == 'tichu'
  }
  get isGrandTichuCalled() {
    return this.calledTichu == 'grand'
  }
  get isNoTichuCalled() {
    return this.calledTichu == ''
  }
  


  mounted(){
    this.calculateDimentions();
    window.addEventListener('resize', this.calculateDimentions);
    window.addEventListener('orientationchange', this.calculateDimentions);
  }

  private calculateDimentions() {
    this.height = this.container.clientHeight;
    this.width = this.container.clientWidth;
    this.buttonsHeight = this.buttons.clientHeight;
  }

  private removeTichuCall() {
    this.selectingTichu = false;
    // this.calledTichu = ''
  }

  private callTichu() {
    this.selectingTichu = false;
    // this.calledTichu = 'tichu'
  }

  private callGrandTichu() {
    this.selectingTichu = false;
    // this.calledTichu = 'grand'
  }

  private toggleSelectTichu() {
    this.selectingTichu = !this.selectingTichu;
  }
}
</script>

<style scoped>
  .player {
    height: 100%;
    width: 100%;
    background-color: var(--team-color);
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
  }

  .grid {
    display: grid;
    gap: 10px;
    height: calc(100% - 20px);
    width: calc(100% - 20px);
    /* background-color: lavender; */
    grid-template-rows: 1fr auto;
  }

  .tall {
    font-size: 40px;
  }

  .left-tall {
    grid-template-columns: 40px 1fr;
    grid-template-areas: 
    "buttons avatar"
    "name name";
  }

  .left-wide {
    grid-template-columns: 40px 1fr;
    grid-template-areas: 
    "buttons avatar"
    "buttons name";
  }

  .right-tall {
    grid-template-columns: 1fr 40px;
    grid-template-areas: 
    "avatar buttons"
    "name name";
  }

  .right-wide {
    grid-template-columns: 1fr 40px;
    grid-template-areas: 
    "avatar buttons"
    "name buttons";
  }


  .name {
    grid-area: name;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    font-size: 40px;
    color: var(--team-color-dark);
  }

  .name>h1 {
    /* background-color: indigo; */
    transform: translateY(2.5px);
    /* text-align: end; */
    font-size: 23px;
  }

  .tall>.name>h1 {
    font-size: 30px;
  }

  .avatar {
    grid-area: avatar;
  }

  .buttons {
    grid-area: buttons;
    display: flex;
    flex-flow: column nowrap;
    justify-content: space-between;
    align-items: stretch;
  }

  .tichu-overlay {
    background-color: var(--bg-color);
    height: calc(100% - 20px);
    /* height: 239px; */
    /* height: calc(var(--buttons-height) + 10px); */
    width: calc(100%);
    position: absolute;
    /* transform: translateY(px); */
    display: grid;
    gap: 20px;
    transform: translateX(0);
    transition: transform 0.5s;
    z-index: 10;
  }

  .overlay-tall {
    height: calc(100% - 50px);
  }

  .overlay-left {
    left: -10px;
    border-top-right-radius: 32px;
    border-bottom-right-radius: 32px;
    grid-template-columns: 1fr 2fr;
    grid-template-areas: "cancel select";
    --select-align: flex-start;
    --cancel-align: flex-end;
    --translation: -50vw;
  }
  .overlay-right {
    right: -10px;
    border-top-left-radius: 32px;
    border-bottom-left-radius: 32px;
    grid-template-columns: 2fr 1fr;
    grid-template-areas: "select cancel";
    --select-align: flex-end;
    --cancel-align: flex-start;
    --translation: 50vw;
  }

  .overlay-hide {
    transform: translateX(var(--translation));
  }

  .cancel {
    grid-area: cancel;
    display: flex;
    justify-content: var(--cancel-align);
    align-items: center;
  }

  .cancel>div {
    transform: rotate(45deg);
    color: var(--team-color);
    font-size: 65px;
  }

  .select-tichu {
    grid-area: select;
    display: flex;
    flex-flow: column nowrap;
    justify-content: center;
    align-items: var(--select-align);
  }

  .select-tichu>h1 {
    font-size: 30px;
    text-align: start;
    color: var(--team-color);
  }

  .select-tichu>.gr {
    font-size: 15px;
    margin-top: 10px;
  }


  .demo {
    background-color: darkkhaki;
  }

  .btn-tichu {
    height: auto;
    height: 80px;
    color: var(--bg-color);
    /* background-color: indigo; */
    display: flex;
    align-items: flex-start;
  }

  .tichu {
    color: var(--bg-color);
    /* color: indigo; */
    font-size: 22px;
    margin: auto;
    writing-mode: vertical-rl;
    text-align: start;
    /* background-color: indigo; */
  }

  .grand {
    font-size: 20px;
  }

  .plus {
    transform: translate(-1px,-2px);
    font-size: 70px;
  }

  .btn-finish {
    height: 40px;
    width: 40px;
    background-color: var(--bg-color);
    border-top-right-radius: 50%;
    display: flex;
    justify-content: center;
  }

  svg>path {
    stroke: var(--team-color);
    transform: translate(-2px, 2px);
  }
</style>
<template>
  <div class="game">
    <div class="game-info" :class="{'no-corners':focusActive}">
      <div
        class="round-list"
        :class="{'fill':focusActive}"
        ref="round-list"
      >
        <!-- <div class="spacer"></div> -->
        <RoundInfo
          :round="initialRound"
          :leftTeamId="leftTeam"
          :rightTeamId="rightTeam"
          :hidden="this.rounds.length > 1"
          :focused="false"
        />
        <RoundInfo
          v-for="(round, index) in finishedRounds"
          :key="index"
          :round="round"
          :leftTeamId="leftTeam"
          :rightTeamId="rightTeam"
          :hidden="hidden(index)"
          :focused="inFocus(index)"
          :collapsed="collapsed"
          @focus="toggleFocus(index)"
        />
      </div>
      <GameInfo
        :hidden="focusActive||collapsed"
        @click="toggleCollapse"
      />

    </div>
    <div class="active-round">
      <div class="spacer">
      </div>
      <RoundUi
        :round="currentRound"
        :leftTeam="leftTeam"
        :rightTeam="rightTeam"
      />
      <RoundScore
        :round="currentRound"
        :leftTeam="leftTeam"
        :rightTeam="rightTeam"
        :leftPointsPrev="0"
        :rightPointsPrev="0"
        :color="'var(--color-dark)'"
        :dotsColor="'var(--color-dark)'"
      />
    </div>
  </div>
</template> 

<script lang="ts">
import { Component, Vue, Ref } from "vue-property-decorator";
import RoundInfo from "../components/game/RoundInfo.vue"
import GameInfo from "../components/game/GameInfo.vue"
import RoundScore from "../components/game/RoundScore.vue"
import RoundUi from "../components/game/RoundUi.vue"
import { Round, CardPoints } from '../types'


@Component({
  components: {
    RoundInfo,
    GameInfo,
    RoundScore,
    RoundUi
  }
})
export default class Game extends Vue {
  @Ref('round-list') readonly roundList!: HTMLDivElement

  numbers = [1,2,3,4,5,6,7,8,9];

  // leftTeam = 'leftTeamId'
  // rightTeam = 'rightTeamId'

  initialRound = {
    roundNumber: 0,
    cardPoints: [
      { teamId: this.leftTeam, value: 0 } as CardPoints,
      { teamId: this.rightTeam, value: 0 } as CardPoints
    ]
  } as Round;

  // rounds = [
  //   {
  //     roundNumber: 0,
  //     cardPoints: [
  //       { teamId: this.leftTeam, value: 120 } as CardPoints,
  //       { teamId: this.rightTeam, value: -20 } as CardPoints
  //     ]
  //   } as Round,
  //   {
  //     roundNumber: 1,
  //     cardPoints: [
  //       { teamId: this.leftTeam, value: 30 } as CardPoints,
  //       { teamId: this.rightTeam, value: 70 } as CardPoints
  //     ]
  //   } as Round,
  //   {
  //     roundNumber: 1,
  //     cardPoints: [
  //       { teamId: this.leftTeam, value: 50 } as CardPoints,
  //       { teamId: this.rightTeam, value: 50 } as CardPoints
  //     ]
  //   } as Round,
  //   {
  //     roundNumber: 1,
  //     cardPoints: [
  //       { teamId: this.leftTeam, value: 10 } as CardPoints,
  //       { teamId: this.rightTeam, value: 90 } as CardPoints
  //     ]
  //   } as Round,
  //   {
  //     roundNumber: 1,
  //     cardPoints: [
  //       { teamId: this.leftTeam, value: 75 } as CardPoints,
  //       { teamId: this.rightTeam, value: 25 } as CardPoints
  //     ]
  //   } as Round,
  //   {
  //     roundNumber: 1,
  //     cardPoints: [
  //       { teamId: this.leftTeam, value: 50 } as CardPoints,
  //       { teamId: this.rightTeam, value: 50 } as CardPoints
  //     ]
  //   } as Round,
  //   {
  //     roundNumber: 1,
  //     cardPoints: [
  //       { teamId: this.leftTeam, value: 95 } as CardPoints,
  //       { teamId: this.rightTeam, value: 5 } as CardPoints
  //     ]
  //   } as Round,
  // ];

  get game(): Game {
    return this.$store.getters.gameById('3466b1bc-821c-42f6-9f83-1d29cb58699e') as Game;
  }

  get rounds(): Round[] {
    return this.game.rounds;
  }

  get currentRound(): Round {
    return this.game.rounds[this.game.rounds.length-1];
  }

  get leftTeam(): string {
    return this.game.leftTeam;
  }

  get rightTeam(): string {
    return this.game.rightTeam;
  }

  get finishedRounds() {
    return this.rounds.slice(0, -1);
  }

  collapsed = true;
  focusActive = false;
  focusedRound = this.rounds.length-2;
  inFocus(roundIndex: number) {
    return this.focusActive && roundIndex == this.focusedRound;
  }
  hidden(roundIndex: number) {
    // return false;
    if(this.collapsed){
      return roundIndex != this.rounds.length - 2;
    }

    if(!this.focusActive)
      return false;
    // else if(this.inFocus(roundIndex) || this.inFocus(roundIndex + 1))
    else if(this.inFocus(roundIndex))
      return false;
    else
      return true;
 }
  toggleFocus(roundIndex: number) {
    if(this.collapsed){
      this.toggleCollapse();
      return;
    }

    this.focusActive = !this.focusActive;
    this.focusedRound = roundIndex;
  }
  scrollTobottom = false;
  adjustScroll() {
    this.roundList.scrollTop = this.roundList.scrollHeight
    if(this.scrollTobottom)
      requestAnimationFrame(this.adjustScroll)
  }
  toggleCollapse() {
    this.collapsed = !this.collapsed;
    // scroll to latest round
    if(!this.collapsed){
      this.scrollTobottom = true
      this.adjustScroll();
      new Promise(res => setTimeout(res, 1000)).then(() => this.scrollTobottom = false);
    }
  }
}
</script>

<style>
  .game {
    height: 100%;
    position: relative;

    /* display: flex;
    flex-direction: column;
    flex-wrap:  nowrap;
    justify-content: flex-end;
    align-items: stretch; */

    /* table widths */
    --number: 100px;
    --dots: 30px;
    --name: 30px;
  }

  .game-info {
    position: absolute;
    width: 100%;
    z-index: 10;

    --info-height: 80px;
    height: auto;
    max-height: 100%;
    background-color: var(--color-dark);

    display: flex;
    flex-direction: column;
    flex-wrap:  nowrap;
    justify-content: flex-end;
    flex-shrink: 0;
    align-items: stretch;
    /* transform: translateY(85px - var(--window-height)); */

    /* display: grid;
    grid-template-rows: 1f auto; */

    border-bottom-left-radius: 60px;
    border-bottom-right-radius: 60px;

    transition: border-radius 0.5s;
  }

  .no-corners {
    border-bottom-left-radius: 0;
    border-bottom-right-radius: 0;
  }

  .round-list {
    --tab-height: 85px;
    --offset-y: 0;
    --background: var(--color-dark);

    width: 100%;
    max-height: calc(var(--window-height) - var(--info-height));
    flex-shrink: 0;
    /* background-color: darkgoldenrod; */
    /* height: calc(100% - var(--info-height)); */

    display: flex;
    flex-flow: column nowrap;
    /* justify-content: flex-end; */
    align-items: stretch;

    overflow-y: scroll;

    transition: height 1s max-height 1s;

    /* transform: translateY(var(--tab-height));
    transition: transform 1s; */
  }

  .fill {
    max-height: calc(var(--window-height));
  }

  .spacer {
    height: 100px;
  }

  .active-round {
    /* background-color: cadetblue; */
    display: grid;
    grid-template-rows: 85px 1fr 80px;
    height: 100%;
  }
</style>
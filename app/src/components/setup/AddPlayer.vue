<script setup lang="ts">
import {computed, ref} from 'vue';
import PlayerAvatar from '@/components/setup/PlayerAvatar.vue';
import {useGameStore} from '@/stores/game-store';
import {Game, Player, PlayerId} from 'pointchu.domain';
import {usePlayerStore} from '@/stores/player-store';

const props = defineProps<{
  teamIndex: number;
  playerIndex: number;
}>();

const gameStore = useGameStore();
const playerStore = usePlayerStore();

const editingPlayerName = ref(false);
const playerNameInput = ref<HTMLInputElement | null>(null);
const updatedPlayerName = ref('');

const isLeftTeam = props.teamIndex === 0;
const isRightTeam = props.teamIndex === 1;

const player = computed(() => {
      const playerId = gameStore.currentGame.teams[props.teamIndex].playerIds[props.playerIndex];
      if (!playerId) {
        return null;
      }
      return playerStore.allPlayers.find(player => player.id.value === playerId.value);
    },
);

const addPlayer = async () => {
  const loggedInPlayerAlreadyInTeam = gameStore.currentGame.players.find(
      playerId => playerId.value === playerStore.loggedInPlayerId.value
  );
  if (loggedInPlayerAlreadyInTeam) {
    const numberONextPlayer = gameStore.currentGame.players.length;
    const newPlayer = await playerStore.createPlayer({
      userId: null,
      name: `Player ${numberONextPlayer}`,
    });
    gameStore.currentGame.teams[props.teamIndex].playerIds[props.playerIndex] = newPlayer.id;
  } else {
    gameStore.currentGame.teams[props.teamIndex].playerIds[props.playerIndex] = playerStore.loggedInPlayerId;
  }
  await gameStore.updateGame(gameStore.currentGame as Game);
  editingPlayerName.value = true;
  // TODO: fix typing issue
  // @ts-ignore
  playerNameInput.value?.focus()
}
const editPlayerName = () => {
  const name = player.value?.name;
  if (!name) {
    throw new Error('Implementation defect: player name input not found');
  }
  updatedPlayerName.value = name;
  editingPlayerName.value = true;
}

const updatePlayerName = (event: Event) => {
  const target = event.target as HTMLInputElement;
  updatedPlayerName.value = target.value;
}

const confirm = () => {
  editingPlayerName.value = false;
  const playerToUpdate = player.value;
  if (!playerToUpdate) {
    throw new Error('Implementation defect: Player not found');
  }
  playerToUpdate.name = updatedPlayerName.value;
  playerStore.updatePlayer(playerToUpdate as Player);
  console.log(updatedPlayerName.value);
  console.log(playerToUpdate);
}

const removePlayer = () => {
  const teamToUpdate = gameStore.currentGame.teams[props.teamIndex]
  const playerToRemove = teamToUpdate.playerIds[props.playerIndex];
  gameStore.updateGame(gameStore.currentGame as Game);
  playerStore.removePlayerFromStore(playerToRemove as PlayerId);
}
</script>

<template>
  <div>
    <div
        v-if="editingPlayerName"
        class="edit-player"
    >
      <input type="text"
             ref="input"
             :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': isRightTeam}"
             class="text--sm input--text"
             :value="updatedPlayerName"
             @input="updatePlayerName"/>
      <div class="confirm button"
           @click="confirm">
        <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 24 24" fill="none">
          <path d="M9.00016 16.17L4.83016 12L3.41016 13.41L9.00016 19L21.0002 7L19.5902 5.59L9.00016 16.17Z"
                fill="currentColor"/>
        </svg>
      </div>
    </div>
    <div
        v-else-if="player"
        class="remove-player"
        :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': isRightTeam}"
    >
      <div>{{ player.name }}</div>
      <div class="edit-player-action">
        <div
            class="button--lg"
            @click="removePlayer"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 24 24" fill="none">
            <path
                d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z"
                fill="currentColor"/>
          </svg>
        </div>
        <div
            class="button--lg"
            @click="editPlayerName"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 24 24" fill="none">
            <path fill-rule="evenodd" clip-rule="evenodd"
                  d="M19.06 3.59L20.41 4.94C21.2 5.72 21.2 6.99 20.41 7.77L7.18 21H3V16.82L13.4 6.41L16.23 3.59C17.01 2.81 18.28 2.81 19.06 3.59ZM5 19L6.41 19.06L16.23 9.23L14.82 7.82L5 17.64V19Z"
                  fill="currentColor"/>
          </svg>
        </div>
      </div>
    </div>
    <div
        v-else
        class="add-player"
        :class="{'left-team-background-dark': isLeftTeam, 'right-team-background-dark': isRightTeam}"
        @click="addPlayer"
    >
      <div>Specify player</div>
      <PlayerAvatar/>
      <div class="button-lg">
        <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 24 24" fill="none">
          <path d="M20 13H13V20H11V13H4V11H11V4H13V11H20V13Z" fill="currentColor"/>
        </svg>
      </div>
    </div>
  </div>
</template>

<style scoped>
.add-player {
  padding: 1rem;
  border-radius: 3rem;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.remove-player {
  padding: 1rem;
  border-radius: 3rem;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.edit-player {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.edit-player-action {
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 1rem;
}
</style>

<template>
  <div class="login-container">
    <div></div>
    <div class="container--column">
      <input class="input--text-login light-background"
             id="name"
             type="text"
             ref="input"
             placeholder="Name"
             @input="name = $event.target.value"/>
    </div>
    <div class="container--column ">
      <input class="input--text-login light-background"
             id="email"
             type="text"
             ref="input"
             placeholder="E-Mail"
             @input="email = $event.target.value"/>
    </div>
    <div class="container--column">
      <button class="button--md-dark"
              :disabled="nameOrMailNotDefined"
              :class="{'disabled': nameOrMailNotDefined}"
              @click="findOrCreateUser">
        Login
      </button>
    </div>
    <div></div>
  </div>
</template>

<script lang="ts">
import { EndpointRegistry } from '@/configuration/application-configuration';
import { IntendedUserDto, User } from '@/domain/entities/user';
import { Option } from 'fp-ts/Option';
import Vue from 'vue';
import { Component, Inject } from 'vue-property-decorator';
import { mapGetters, mapMutations } from 'vuex';

@Component({
  methods: {
    ...mapMutations('userState', {
      updateUser: 'updateUser',
    }),
    ...mapGetters('userState', {
      user: 'user',
    }),
  },
})
export default class Login extends Vue {
  @Inject('endpoints')
  private endpoints!: EndpointRegistry;

  user!: () => Option<User>;
  updateUser!: (user: User) => void;

  private name = '';
  private email = '';

  findOrCreateUser() {
    const intendedUserDto = IntendedUserDto.of(this.name, this.email);
    this.endpoints.findOrCreateUser.send(intendedUserDto).then(
      (user: User) => {
        this.updateUser(user);
        this.$router.replace('start');
      },
      () => console.error('Could not find or create user'), // TODO: use toaster service to notify user
    );
  }

  get nameOrMailNotDefined(): boolean {
    return !this.name || this.name.length < 1 || !this.email || this.email.length < 1;
  }
}
</script>

<style scoped>
.login-container {
  display: grid;
  grid-template-rows: 35% 10% 10% 10% 35%;
  height: 100%;
  width: 100%;
}

.input--text-login {
  display: flex;
  align-items: center;
  flex-direction: row;
  text-align: center;
  height: 80%;
  width: 50%;
  border-radius: 2vh;
}

.disabled {
  opacity: 0.5;
}
</style>

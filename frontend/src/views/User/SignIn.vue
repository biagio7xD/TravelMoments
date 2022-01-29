<template>
  <div class="grid items-center justify-center min-h-screen">

    <Toast position="center"/>

    <div class="col-11 w-auto h-auto">
      <div class="px-8 py-4 text-left bg-white shadow-lg relative fadein animation-duration-900">
        <h3 class="text-2xl font-bold text-center">Login</h3>
        <form id="sign_in">
          <div class="mt-2">
            <label class="block py-2" for="username">Username</label>
            <InputText id="username" v-model="user.username" v-tooltip.right="'Required'" class="w-full"
                       placeholder="Rossi123" v-bind:class="usernameExist? 'p-invalid' : ''" v-on:blur="checkUsername"/>
            <small v-if="usernameExist" id="user-exist" class="block p-error">Username Not exist.</small>
          </div>
          <div class="mt-2">
            <label class="block py-2" for="password">Password</label>
            <Password id="password" v-model="user.password" v-tooltip.right="'Required'" :feedback="false"
                      toggleMask></Password>
          </div>
          <div class="flex justify-content-center mt-4 ">
            <Button :disabled="checkButtonEnabled()" :loading="isloading"
                    label="Sign In"
                    @click.prevent="handleLogin"></Button>
          </div>
        </form>
      </div>
    </div>

  </div>
</template>

<script>

import User from "@/model/user";
import axios from "axios";

export default {
  name: 'SignIn',

  data() {
    return {
      user: new User("", "", ""),
      loading: false,
      message: "",
      isloading: false,
      usernameExist: false,
    };
  },

  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  created() {
    if (this.loggedIn) {
      this.$toast.add({
        severity: 'success',
        summary: 'Login Succesful',
        detail: 'User already authenticated',
        life: 3000
      });
      this.$router.push("/");
    }
  },

  methods: {
    handleLogin() {
      this.loading = true;
      this.isloading = true;
      this.$store.dispatch("auth/login", this.user)
          .then(() => {
                this.isloading = false;
                this.$toast.add({
                  severity: 'success',
                  summary: 'Login Succesful',
                  detail: 'User authenticated',
                  life: 3000
                });
                setTimeout(() => this.$router.push("/"), 1500);
              },
              (error) => {
                this.isloading = false;
                this.loading = false;
                this.message = (error.response && error.response.data && error.response.data.message) || error.message || error.toString();
                this.$toast.add({
                  severity: 'error',
                  summary: 'Login Failed',
                  detail: 'User not authenticated',
                  life: 3000
                });
              }
          )
    },

    checkUsername() {
      axios.get(`http://localhost:8080/api/auth/user/${this.user.username}`)
          .then(response => {
            if (response.status === 200) {
              this.usernameExist = false;
            }
            if (response.status === 204) {
              this.usernameExist = true;
            }
          })
          .catch(console.log)
    },

    checkButtonEnabled() {
      return (this.user.password === '' || (this.user.username === '' || this.usernameExist)) ? true : false;
    }
  },
}
</script>

<style scoped>

</style>
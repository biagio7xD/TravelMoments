<template>
  <div id="container" class="grid items-center justify-center min-h-screen">

    <Dialog id="succesful-registration" v-model:visible="showConfirmationDialog" :breakpoints="{ '960px': '60vw' }"
            :modal="true"
            :style="{ width: '30vw' }">
      <div class="flex align-items-center justify-content-center">
        <i :style="{fontSize: '5rem', color: 'var(--green-500)' }" class="pi pi-check-circle"></i>
        <h5 class="px-2">Registration Successful!</h5>
      </div>
      <template #footer>
        <div class="p-d-flex p-jc-center">
          <Button class="p-button-text" label="OK" @click.prevent="showConfirmDialog"/>
        </div>
      </template>
    </Dialog>

    <Dialog id="reject-registration" v-model:visible="showRejectDialog" :breakpoints="{ '960px': '60vw' }" :modal="true"
            :style="{ width: '30vw' }">
      <div class="flex align-items-center justify-content-center">
        <i :style="{fontSize: '5rem', color: 'var(--pink-900)' }" class="pi pi-times-circle"></i>
        <h5 class="px-2">User already exist. <br> Try to Login!</h5>
      </div>
      <template #footer>
        <div class="p-d-flex p-jc-center">
          <Button class="p-button-text" label="OK" @click.prevent="showReject"/>
        </div>
      </template>
    </Dialog>

    <div class="col-11 w-auto h-auto items-center justify-center">
      <div class="px-8 py-4 text-left bg-white shadow-lg relative fadein animation-duration-900">
        <h3 class="text-2xl font-bold text-center">New User?</h3>
        <form id="sign_up">
          <div class="mt-4">
            <label class="block py-2" for="email">Email</label>
            <InputText id="email" v-model="user.email" v-tooltip.right="'Required'" class="w-full"
                       placeholder="xyz@hello.com" v-bind:class="emailExist? 'p-invalid' : ''"
                       v-on:blur="checkEmailFormat"/>
            <small v-if="emailExist" id="email-exist" class="block p-error">{{ emailExist }}</small>
          </div>
          <div class="mt-2">
            <label class="block py-2" for="username">Username</label>
            <InputText id="username" v-model="user.username" v-tooltip.right="'Required'" class="w-full"
                       placeholder="Rossi123" v-bind:class="usernameExist? 'p-invalid' : ''"
                       v-on:blur="checkUsernameExist"/>
            <small v-if="usernameExist" id="user-exist" class="block p-error">Username already exist.</small>
          </div>
          <div class="mt-2">
            <label class="block py-2" for="password-signup">Password</label>
            <Password id="password-signup" v-model="user.password" v-tooltip.right="'Required'"
                      toggleMask v-bind:class="passwdInvalidFormat? 'p-invalid' : ''"
                      v-on:focusout="checkPasswdNotValid"/>
            <small v-if="passwdInvalidFormat" class="block p-error">{{ passwdInvalidFormat }}.</small>
          </div>
          <div class="flex justify-content-center mt-4">
            <Button :disabled="checkButtonEnabled()"
                    :loading="isloading" label="Sign Up"
                    @click.prevent="handleUserSignUp"></Button>
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
  name: "SignUp",
  data() {
    return {
      user: new User("", "", ""),
      accept: undefined,
      submitted: false,
      showConfirmationDialog: false,
      showRejectDialog: false,
      status: false,
      isloading: false,
      usernameExist: false,
      emailExist: '',
      passwdInvalidFormat: '',
    }
  },

  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },

  mounted() {
    if (this.loggedIn) {
      this.$router.push("/");
    }
  },

  methods: {

    handleUserSignUp() {
      this.isloading = true;
      this.message = "";
      this.loading = true;
      this.$store.dispatch("auth/register", this.user).then(
          (data) => {
            this.status = true;
            this.message = data.message;
            this.loading = false;
          },
          (error) => {
            this.message = (error.response && error.response.data && error.response.data.message) || error.message || error.toString();
            this.loading = false;
            this.status = false;
          },
      ).then(() => {
        this.isloading = false;
        if (this.status === true) {
          this.showConfirmationDialog = true
        } else {
          this.showRejectDialog = true;
        }
      })
    },

    showConfirmDialog() {
      this.showConfirmationDialog = !this.showConfirmationDialog;
      if (!this.showConfirmationDialog) {
        this.resetForm();
      }
      this.$router.push("/sign_in")
    },

    showReject() {
      this.showRejectDialog = !this.showRejectDialog;
      if (!this.showRejectDialog) {
        this.resetForm();
      }
      this.$router.push("/sign_in");
    },

    checkButtonEnabled() {
      return !((this.user.username !== "" && this.usernameExist === false) && (this.user.email !== "" && this.emailExist === "") && (this.user.password !== "" && this.passwdInvalidFormat === ""));
    },

    resetForm() {
      this.accept = null;
      this.submitted = false;
      this.showRejectDialog = false;
      this.showConfirmationDialog = true;
      this.isloading = false;
    },

    checkUsernameExist() {
      axios.get(`http://localhost:8080/api/auth/user/${this.user.username}`)
          .then(response => {
            if (response.status === 200) {
              this.usernameExist = true;
              this.user.username = ""
            }
            if (response.status === 204) {
              this.usernameExist = false;
            }
          })
          .catch(console.log)
    },

    checkEmailFormat() {
      const validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
      if (this.user.email.match(validRegex)) {
        this.emailExist = ""
      } else {
        this.emailExist = "Email not valid."
      }
    },

    checkPasswdNotValid() {
      return this.user.password.length < 4 ? this.passwdInvalidFormat = "Use more than 4 characters for password" : "";
    }


  },
}

</script>

<style scoped>

</style>

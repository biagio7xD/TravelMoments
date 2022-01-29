<template>
  <div class="grid justify-center items-center mt-4 mb-2">
    <div class="col-11 py-2.5">
      <!--Nav-->
    <nav id="navbar" class="flex justify-between">

      <div class="flex items-center">
        <router-link class="hidden md:flex" to="/">
          <div class="flex">
            <img alt="Logo" class="h-8 w-auto" src="https://tailwindui.com/img/logos/workflow-mark-indigo-600.svg">
            <h1 id="LogoName" class="text-xl text-gray-800 font-light z-5">Travel<span
                class="text-indigo-600">Moments</span></h1>
          </div>
        </router-link>
        <Button class="p-button-plain p-button-text md:hidden"
                icon='pi pi-home'
                type="button" @click.prevent="this.$router.push('/')"/>
        <div v-if="currentUser" class="ml-3 flex scalein animation-duration-1000">
          <Button v-tooltip.bottom="'Show my travel'" class="p-button-sm p-button-plain p-button-text"
                  icon='pi pi-map'
                  type="button" @click.prevent="this.$router.push('/travel/get')"/>
          <Button v-tooltip.bottom="'Add new travel'" class="p-button-sm p-button-plain p-button-text" icon='pi pi-plus'
                  type="button" @click.prevent="this.$router.push('/travel/add')"/>
          <Button v-tooltip.bottom="'Modify a travel'" class="p-button-sm p-button-plain p-button-text"
                  icon='pi pi-user-edit'
                  type="button" @click.prevent="this.$router.push('/travel/modify')"/>
        </div>
      </div>

      <ul class="flex items-center space-x-2">
        <li v-if="!currentUser" class="font-semibold text-gray-700">
          <Button class="p-button-text p-button-sm" icon='pi pi-sign-in' icon-pos="right" label="SignIn"
                  @click.prevent="$router.push('/sign_in')"/>
        </li>
        <Divider v-if="!currentUser" layout="vertical"/>
        <li v-if="!currentUser" class="font-semibold text-gray-700">
          <Button class="p-button-text p-button-secondary p-button-sm" icon='pi pi-user-plus' label="SignUp"
                  @click.prevent="$router.push('/sign_up')"/>
        </li>
        <li v-if="currentUser" class="hidden font-extralight text-gray-700 sm:inline">
          <p v-tooltip.bottom="'Ehi, You! Welcome :)'" class="font-light">Welcome <span
              class="text-xl fadeinup animation-duration-900">{{ currentUser.username }}</span>
          </p>
        </li>
        <Divider v-if="currentUser" layout="vertical" class="hidden sm:inline"/>
        <li v-if="currentUser" class="font-semibold text-gray-700">
          <Button class="p-button-text p-button-danger p-button-sm" icon='pi pi-sign-out' icon-pos="right"
                  label="LogOut" @click.prevent="logOut"/>
        </li>
      </ul>

    </nav>

    </div>
  </div>

  <router-view/>
</template>

<script>

export default {
  name: "Navigation",

  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },

  data() {
    return {
      userJWT: true,
    }
  },

  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/sign_in');
    }
  }

}
</script>

<style scoped>

</style>


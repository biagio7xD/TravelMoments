<template>

  <div class="grid items-start justify-center min-h-screen min-w-screen">
    <div
        class="col-11 flex mt-4 items-center text-left bg-white shadow-1 relative rounded-md fadeinleft animation-duration-900">
      <div id="homepage_slogan" class="mx-8 sm:text-center lg:text-left">
        <h1 class="text-4xl tracking-tight font-extrabold text-gray-900 sm:text-5xl md:text-6xl text-center">
          <span class="block">A New Way to</span>
          <span class="block text-indigo-600">Remember Moments.</span>
        </h1>
        <p class="mt-3 text-sm text-gray-500 sm:mt-5  sm:mx-auto md:mt-5 lg:text-base">
          Record your trip and relive it whenever and wherever you want. Collect your best moments and relive the
          emotions of your adventure. </p>
      </div>
      <div id="home_image" class="hidden bg-cover w-auto h-auto lg:block">
        <img alt="homepage background" src="../assets/img/home_background.jpg">
      </div>
    </div>

    <div class="col-11 mt-4 mb-8 relative fadeinleft animation-duration-900 align-items-center">
      <div
          class="flex-col flex lg:flex-row lg:justify-between gap-4 justify-center items-center text-center scalein animation-duration-900">

        <Card style="width: 25em">
          <template #header>
            <img
                alt=""
                src="https://previews.123rf.com/images/moloko88/moloko881701/moloko88170100004/69842165-carte-des-ic%C3%B4nes-du-monde-et-de-voyages-.jpg"
                style="height: 15rem"/>
          </template>
          <template #title>
            Add a Travel
          </template>
          <template #content>
            <p class="text-sm text-gray-500">Add a travel to your diary and save all places visited during your
              travel</p>
          </template>
          <template #footer>
            <Button class="p-button-text" icon="pi pi-plus" label="Add" @click.prevent="$router.push('/travel/add')"/>
          </template>
        </Card>

        <Card style="width: 25em">
          <template #header>
            <img
                src="https://media.istockphoto.com/photos/world-map-with-visas-stamps-seals-travel-concept-picture-id1071294112"
                style="height: 15rem"/>
          </template>
          <template #title>
            Modify Travel
          </template>
          <template #content>
            <p class="text-sm text-gray-500">Change all information about a travel or place in your diary.</p>
          </template>
          <template #footer>
            <Button class="p-button-text" icon="pi pi-pencil" label="Edit"
                    @click.prevent="$router.push('/travel/modify')"/>
          </template>
        </Card>

        <Card style="width: 25em">
          <template #header>
            <img src="https://thumbs.dreamstime.com/b/web-148145829.jpg" style="height: 15rem"/>
          </template>
          <template #title>
            Visualize Travels
          </template>
          <template #content>
            <p class="text-sm text-gray-500">Take a look to all your travels on map and get all information about it</p>
          </template>
          <template #footer>
            <Button class="p-button-text" icon="pi pi-map-marker" label="Show Map"
                    @click.prevent="$router.push('/travel/get')"/>
          </template>
        </Card>
      </div>
    </div>

  </div>
</template>

<script>
// @ is an alias to /src
export default {
  name: 'Home',
}
</script>

<style>

</style>

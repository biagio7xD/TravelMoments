<template>
  <div id="maps">

    <GoogleMap
        id="gmaps"
        :center="center"
        :zoom="8"
        api-key="AIzaSyD_L001FEb9q8w8XJoNn1yOvIfb3nEv3Fs"
    >
      <Marker
          v-for="(gmp, index) in this.value"
          :key="index"
          :options="{position : gmp}"
          @click="showLocationDialog(gmp)"/>

      <Polyline v-for="(gmp, index) in this.path"
                v-if="showPath"
                :key="index"
                :options="gmp"
                @click="showRouteDialog(index)"/>

      <Dialog v-model:visible="displayLocationDialog" :header="place" :modal="true">
        <p><span class="font-semibold">City:  </span> {{ this.city }}</p>
        <p><span class="font-semibold">Country:  </span> {{ this.country }}</p>
        <p><span class="font-semibold">Latitude:  </span> {{ this.latitude }}</p>
        <p><span class="font-semibold">Longitude:  </span> {{ this.longitude }}</p>
        <p><span class="font-semibold">Description:  </span> {{ this.description }}</p>
      </Dialog>

      <Dialog v-model:visible="displayRouteDialog" :header="date" :modal="true">
        <p><span class="font-semibold">Transport Used:  </span> {{ this.transport }}</p>
        <p><span class="font-semibold">Description:  </span> {{ this.description }}</p>
      </Dialog>

    </GoogleMap>

    <div class="w-full h-9 surface-50 flex justify-content-end">
      <div id="pathSwitcherContainer" class="py-1 items-center px-2 flex">
        <p class="font-light text-sm mr-2">Do you want the route to be shown? </p>
        <InputSwitch v-model="this.showPathChecked" @change="getRoutes"/>
      </div>
    </div>

  </div>
</template>

<script>
import {defineComponent} from 'vue'
import {GoogleMap, Marker, Polyline} from 'vue3-google-map'
import TravelsService from "@/services/travels.service";

export default defineComponent({
  components: {GoogleMap, Marker, Polyline},
  name: 'GMaps',
  props: {
    value: Object,
    dateSelected: Boolean,
    date: String,
  },

  data() {
    return {
      center: {lat: this.value[0].lat, lng: this.value[0].lng},
      displayLocationDialog: false,
      displayRouteDialog: false,
      place: "",
      description: "",
      transport: "",
      city: "",
      country: "",
      latitude: "",
      longitude: "",
      showPath: false,
      path: [],
      showPathChecked: false,
      test: undefined,
      travelService: undefined,
      routeList: undefined,
    }
  },

  created() {
    this.travelService = new TravelsService();
  },


  watch: {
    date(value, oldValue) {
      if (value !== oldValue) {
        this.showPath = false;
        this.path = [];
        this.showPathChecked = false;
      }
    },

    value(value, oldValue) {
      if (value !== oldValue) {
        this.center = {lat: value[0].lat, lng: value[0].lng};
      }
    }
  },

  methods: {

    showLocationDialog(data) {
      this.center = data;
      this.displayLocationDialog = true;
      this.description = data["description"];
      this.latitude = data["lat"];
      this.longitude = data["lng"];
      this.place = data["address"];
      this.city = data["city"]
      this.country = data["country"]
    },

    showRouteDialog(index) {
      this.displayRouteDialog = true;
      this.transport = this.routeList[index].transport;
      this.description = this.routeList[index].routeInfo;
    },

    getRoutes() {
      this.travelService.getTravelsByDate(this.date)
          .then(data => this.routeList = data)
          .then(() => {
            return this.dateSelected ? this.setPathCoordinate() : this.getWarning()
          });
    },

    setPathCoordinate() {
      if (this.showPathChecked) {
        for (let route of this.routeList) {
          const lineSymbol = {
            path: "M 0,-1 0,1",
            strokeOpacity: 0.9,
            scale: 4,
          };

          this.path.push({
            path: this.getLatLngFromArray(route.latAndLng),
            geodesic: true,
            strokeColor: '#f63e3e',
            strokeOpacity: 0,
            icons: [
              {
                icon: lineSymbol,
                offset: "0",
                repeat: "20px",
              }]
          });
        }
        console.log(this.path)
        this.showPath = true;
      } else {
        this.path = [];
        this.showPath = false;
      }

    },

    getWarning() {
      if (this.showPathChecked) {
        this.$toast.add({severity: 'warn', summary: 'Warning Message', detail: 'Select a date and retry', life: 3000});
      }
    },

    getLatLngFromArray(coordinates) {
      let latLng = [];
      coordinates.map(value => (
          latLng.push({
            lat: value[1],
            lng: value[0]
          })
      ))
      return latLng;
    }
  },

})
</script>

<style scoped>

#gmaps {
  width: 800px;
  height: 400px;
}

@media only screen and (max-width: 768px) {
  #gmaps {
    width: 600px;
    height: 300px;
  }
}

</style>
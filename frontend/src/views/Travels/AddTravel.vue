<template>
  <div class="grid items-center justify-center min-h-screen">

    <Toast position="center"/>

    <div class="col-11 w-auto h-auto">
      <div v-if="display==='AddDate'" id="date-container"
           class="px-8 py-4 mb-8 text-left bg-white shadow-xl relative rounded-lg fadeinleft animation-duration-900">
        <h3 class="text-2xl font-bold text-center">Travel Date</h3>
        <div class="mt-4">
          <label class="block py-2" for="date">Date of Travel</label>
          <Calendar id="date" v-model="travel.date" v-tooltip.right="'Required'" dateFormat="dd-mm-yy"/>
        </div>
        <div class="flex justify-content-center mt-4">
          <Button :disabled="checkButtonDateEnabled()" class="p-button-outlined p-button-sm" label="Add Address"
                  @click.prevent="display='AddRoute'"></Button>
        </div>
      </div>

      <div v-if="display==='AddRoute'" id="route-container"
           class="px-8 py-4 mb-8 text-left bg-white shadow-xl relative rounded-lg fadeinright animation-duration-900">
        <h3 class="text-2xl font-bold text-center">Travel Route</h3>
        <div class="mt-4">
          <label class="block py-2" for="routeinfo">Route info</label>
          <InputText id="routeinfo" v-model="travel.routeInfo" v-tooltip.right="'Required'" class="w-full"
                     placeholder="Christmas Holiday" type="text"
                     v-bind:class="{'p-invalid' : this.travel.routeInfo !== '' && this.travel.routeInfo.length < 5}"/>
        </div>
        <div class="mt-2">
          <label class="block py-2" for="transport">Means Of Transport</label>
          <Dropdown id="transport" v-model="travel.transport" :options="transport" class="w-full"
                    placeholder="Select a means"/>
        </div>
        <div class="mt-2">
          <label class="block py-2" for="geocoord">GeoCoordinates of route</label>
          <Textarea id="geocoord" v-model="travel.latAndLng" v-tooltip.right="'Add list of lat and lng'" class="w-full"
                    cols="30"
                    placeholder="e.g [ [lat,lng], [lat,lng] ]" rows="2"/>
          <Divider align="center">Or</Divider>
          <input id="geojsoncoord" ref="doc" v-tooltip.right="'Upload a GeoJson file'" accept="application/geo+json"
                 class="text-gray-500 px-3 py-2 border-1 rounded"
                 type="file" @change="readFile()">
        </div>
        <div class="flex justify-content-between mt-4">
          <Button class="p-button-rounded p-button-text p-button-sm" icon="pi pi-arrow-left"
                  @click.prevent="display='AddDate'"/>
          <Button class="p-button-outlined p-button-sm" label="Add Places"
                  @click.prevent="display='AddPlaces'"></Button>
        </div>
      </div>

      <div v-if="display==='AddPlaces'" id="places-container"
           class="px-8 py-4 mb-8 text-left bg-white shadow-xl relative rounded-lg fadeinright animation-duration-900">
        <h3 class="text-2xl font-bold text-center">Travel Places</h3>
        <div class="mt-4">
          <label class="block py-2" for="address">Address / Place</label>
          <InputText id="address" v-model="address" v-tooltip.right="'Required'" placeholder="Rue du Temple, 86"
                     type="text"
                     v-bind:class="{'p-invalid' : this.address !== '' && this.address.length < 4}"/>
        </div>
        <div class="mt-2">
          <label class="block py-2" for="city">City</label>
          <InputText id="city" v-model="city" v-tooltip.right="'Required'" placeholder="Geneva"
                     type="text"
                     v-bind:class="{'p-invalid' : this.city !== '' && this.city.length < 3}"/>
        </div>
        <div class="mt-2">
          <InputText id="country" v-model="country" v-tooltip.right="'Required'" placeholder="Switzerland"
                     type="text"
                     v-bind:class="{'p-invalid' : this.country !== '' && this.country.length < 5}"/>
        </div>
        <div class="mt-2">
          <label class="block py-2" for="info">Description</label>
          <InputText id="info" v-model="description" v-tooltip.right="'Optional'" placeholder="Lake Geneva."
                     type="text"/>
        </div>
        <div class="flex justify-content-between mt-4">
          <Button class="p-button-rounded p-button-text p-button-sm" icon="pi pi-arrow-left"
                  @click.prevent="display='AddRoute'"/>
          <Button v-tooltip.bottom="'Add new place'" :loading="loadingAddPlace"
                  class="p-button-rounded p-button-text p-button-sm"
                  icon="pi pi-plus" v-bind:disabled="checkAddPlaceEnabled()"
                  @click.prevent="pushPlace"/>
          <Button :disabled="checkConfirmButtonEnabled()" :loading="confirmLoading" class="p-button-sm" label="Confirm"
                  @click.prevent="addTravel"></Button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import Place from "@/model/place";
import axios from "axios";
import authHeader from "@/services/auth.header";
import getUserId from "@/services/user.auth.details";
import Travel from "@/model/travel";

export default {
  name: "AddTravel",

  data() {
    return {
      display: "AddDate",
      places: [],
      travel: new Travel(),
      file: undefined,
      geojson: undefined,
      address: "",
      city: "",
      country: "",
      description: "",
      selectedMeans: undefined,
      transport: [
        'Auto',
        'Moto',
        'Walk',
        'Airplane',
        'Metro',
        'Train',
        'Other'
      ],
      confirmLoading: false,
      loadingAddPlace: false
    }
  },
  methods: {

    checkButtonDateEnabled() {
      return this.travel.date === "";
    },

    readFile() {
      this.file = this.$refs.doc.files[0];
      const reader = new FileReader();
      reader.onload = (res) => {
        this.geojson = JSON.parse(res.target.result);
      };
      reader.onerror = (err) => console.log(err);
      reader.readAsText(this.file);
    },

    async pushPlace() {
      this.loadingAddPlace = true;
      try {
        const place = await this.getPlace();
        this.places.push(place);
        this.loadingAddPlace = false;
        this.$toast.add({severity: 'success', summary: 'Success Message', detail: 'Place Added', life: 3000});
        this.reset();
      } catch (e) {
        this.$toast.add({
          severity: 'warn',
          summary: 'Warning Message',
          detail: e,
          life: 3000
        });
        this.loadingAddPlace = false;
      }
    },

    async getPlace() {
      const features = await this.getPlaceCoordinates();
      return new Place(
          this.travel.date,
          this.description,
          features.properties["address_line1"],
          features.properties["city"],
          features.properties["country"],
          features.geometry.coordinates[0], //lng
          features.geometry.coordinates[1], //lat
      );
    },

    async getPlaceCoordinates() {
      //Code to geocode the address
      const API_KEY = "36323b8c882b4f6bb1f6edb419bef016";
      const ACCEPT_LEVEL = 0.65;
      const address = `${this.address}, ${this.city}, ${this.country}`;
      const geocodingUrl = `https://api.geoapify.com/v1/geocode/search?text=${encodeURIComponent(address)}&apiKey=${API_KEY}`;
      let geocoding = await axios.get(geocodingUrl);
      const data = geocoding.data.features;
      if (data.length === 0 || data[0].properties.rank.confidence < ACCEPT_LEVEL) {
        throw "Place Not Found"
      }
      return data[0];
    },

    async addTravel() {
      this.confirmLoading = true;
      try {
        if (this.checkPlaceFilled()) {
          const place = await this.getPlace();
          this.places.push(place);
        }
        const ADD_TRAVEL_URL = `http://localhost:8080/api/travel/add/${getUserId()}`;
        this.travel.latAndLng = this.setLatAndLngTravel();
        const response = await axios.post(ADD_TRAVEL_URL, JSON.stringify(this.travel), {headers: authHeader()});
        if (response.status !== 200) {
          throw 'An error occurred, retry'
        }
        const ADD_PLACE_URL = `http://localhost:8080/api/travel/add/${getUserId()}/${response.data}`;
        const responsePlaces = await axios.post(ADD_PLACE_URL, JSON.stringify(this.places), {headers: authHeader()});
        if (responsePlaces.status !== 200) {
          throw 'An error occurred, retry'
        }
        this.$toast.add({severity: 'success', summary: 'Success Message', detail: 'Travel Added', life: 3000});
        setTimeout(() => this.$router.push("/travel/get"), 1500);
        this.confirmLoading = false;
      } catch (e) {
        this.$toast.add({
          severity: 'warn',
          summary: 'Warning Message',
          detail: e,
          life: 3000
        });
        this.confirmLoading = false;
      }
    },

    checkPlaceFilled() {
      return (this.address !== "" || this.city !== "" || this.country !== "")
    },

    setLatAndLngTravel() {
      return this.geojson !== undefined ? this.geojson.features[0].geometry.coordinates : (this.travel.latAndLng === undefined ? [[]] : JSON.parse(this.travel.latAndLng))
    },

    reset() {
      this.city = "";
      this.address = "";
      this.transport = "";
      this.country = "";
      this.description = "";
      this.confirmLoading = false;
    },

    checkConfirmButtonEnabled() {
      return this.address === "" && this.places.length === 0;
    },

    checkAddPlaceEnabled() {
      return this.address === "";
    }

  }
}

</script>

<style scoped>

</style>
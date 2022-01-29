<template>
  <div class="grid items-center justify-center min-h-screen">

    <Toast position="center"/>

    <div class="col-11 w-auto h-auto">
      <div v-if="display==='SelectTravelDate'" id="mod-date-container"
           class="px-8 py-4 mb-8 text-left bg-white shadow-xl relative rounded-lg fadeinleft animation-duration-900">
        <h3 class="text-2xl font-bold text-center">Travel to Modify</h3>
        <div class="mt-4">
          <label class="block py-2" for="mod-date">Date of Travel</label>
          <Dropdown id="mod-date" v-model="selectDate" :options="dateItems" :virtualScrollerOptions="{ itemSize: 10 }"
                    placeholder="Select Date"/>
        </div>
        <div class="flex justify-content-center mt-4">
          <Button :disabled="selectDate === undefined ? true : false" class="p-button-outlined" label="Modify"
                  @click.prevent="getTableTravelsByDate"/>
        </div>
      </div>

      <div
          class="mb-8 mt-4 text-left bg-white rounded-lg shadow-xl relative fadeindown animation-duration-900">
        <DataTable v-if="display==='LocationsTable'" id="locations" v-model="selectPlace" :value="placeItems"
                   data-key="id"
                   showGridlines>
          <Column field="date" header="Date" style="font-size: small"/>
          <Column field="address" header="Address" style="font-size: small"/>
          <Column field="city" header="City" style="font-size: small"/>
          <Column field="description" header="Description" style="font-size: small"/>
          <Column field="lat" header="Latitude" style="font-size: small"/>
          <Column field="lng" header="Longitude" style="font-size: small"/>
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
              <Button class="p-button-rounded p-button-text p-mr-2" icon="pi pi-pencil"
                      @click.prevent="editTravel(slotProps.data)"/>
              <Button class="p-button-rounded p-button-text p-button-danger" icon="pi pi-trash"
                      @click.prevent="deletePlace(slotProps.data)"/>
            </template>
          </Column>
          <template #footer>
            <div class="justify-content-between flex">
              <Button class="p-button-rounded p-button-text p-button-sm" icon="pi pi-arrow-left"
                      @click.prevent="display='SelectTravelDate'"/>
              <Button class="p-button-link p-button-sm" label="Modify Travel information"
                      @click.prevent="display='TravelTable'"/>
            </div>
          </template>
        </DataTable>
      </div>

      <div v-if="display==='LocationInfoDate'" id="mod-place-info"
           class="px-8 py-4 mb-8 text-left bg-white shadow-xl relative fadeinleft animation-duration-900">
        <h3 class="text-2xl font-bold text-center">Change Information</h3>
        <div class="mt-4">
          <label class="block py-2" for="date">Date</label>
          <Calendar id="date" v-model="place.date" v-tooltip.right="'Required'" dateFormat="dd-mm-yy"/>
        </div>
        <div class="flex justify-content-between mt-4">
          <Button class="p-button-rounded p-button-text p-button-sm" icon="pi pi-arrow-left"
                  @click.prevent="display='LocationsTable'"/>
          <Button class="p-button p-button-outlined p-button-sm" label="Continue"
                  @click.prevent="this.display='LocationInfoAddress'"></Button>
        </div>
      </div>

      <div v-if="display==='LocationInfoAddress'" id="mod-place-address"
           class="px-8 py-4 mb-8 text-left bg-white shadow-xl relative fadeinright animation-duration-900">
        <h3 class="text-2xl font-bold text-center">Modify Address</h3>
        <div class="mt-2">
          <label class="block py-2" for="mod-address">Address / Place</label>
          <InputText id="mod-address" v-model="this.address" v-tooltip.right="'Required'"
                     placeholder="Rue du Temple, 86" type="text"
                     v-bind:class="{'p-invalid' : this.address !== '' && this.address.length < 5}"/>
        </div>
        <div class="mt-2">
          <label class="block py-2" for="mod-city">City</label>
          <InputText id="mod-city" v-model="this.city" v-tooltip.right="'Required'" placeholder="Geneva" type="text"
                     v-bind:class="{'p-invalid' : this.city !== '' && this.city.length < 3}"/>
        </div>
        <div class="mt-2">
          <InputText id="mod-country" v-model="this.country" v-tooltip.right="'Required'" placeholder="Switzerland"
                     type="text"
                     v-bind:class="{'p-invalid' : this.country !== '' && this.country.length < 5}"/>
        </div>
        <div class="mt-2">
          <label class="block py-2" for="mod-info">Description</label>
          <InputText id="mod-info" v-model="place.description" v-tooltip.right="'Optional'" placeholder="Lake Geneva."
                     type="text"/>
        </div>
        <div class="flex justify-content-between mt-4">
          <Button class="p-button-rounded p-button-outlined" icon="pi pi-arrow-left"
                  @click.prevent="display='LocationInfoDate'"/>
          <Button :loading="isloading" label="Confirm" @click.prevent="updateTravel"></Button>
        </div>
      </div>

      <EditTravelRoute v-if="display==='TravelTable'" id="travel" :date="this.selectDate"/>


      <Dialog v-model:visible="deletePlaceDialog" :modal="true" :style="{width: '450px'}" header="Confirm">
        <div class="confirmation-content">
          <i class="pi pi-exclamation-triangle p-mr-3"/>
          <span class="px-2"> Are you sure you want to delete this travel?</span>
        </div>
        <template #footer>
          <Button class="p-button-text p-button-sm p-button-danger" icon="pi pi-times" label="No"
                  @click="deletePlaceDialog = false"/>
          <Button class="p-button-text p-button-sm " icon="pi pi-check" label="Yes" @click="doDeleteRequest"/>
        </template>
      </Dialog>
      <router-view/>
    </div>
  </div>
</template>

<script>
import Place from "@/model/place";
import axios from "axios";
import authHeader from "@/services/auth.header";
import getUserId from "@/services/user.auth.details";
import TravelsService from "@/services/travels.service";
import EditTravelRoute from "@/components/EditTravelRoute";

export default {
  name: "ModifyTravel",
  components: {EditTravelRoute},
  data() {
    return {
      place: new Place("", "", "", "", "", "", "", ""),
      city: "",
      address: "",
      country: "",
      display: "SelectTravelDate",
      description: undefined,
      transport: [
        'Auto',
        'Moto',
        'Walk',
        'Airplane',
        'Metro',
        'Train',
        'Other'
      ],
      selectDate: undefined,
      selectPlace: undefined,
      dateItems: undefined,
      placeItems: undefined,
      deletePlaceDialog: false,
      isloading: false,
      showTravelRoute: false,
    }
  },
  travelService: null,
  created() {
    this.travelService = new TravelsService();
  },
  mounted() {
    this.travelService.getAllTravelDates().then(data => this.dateItems = data.sort((a, b) => {
      return new Date(a) - new Date(b)
    }));
  },
  methods: {
    getTableTravelsByDate() {
      this.travelService.getPlacesByDate(this.selectDate)
          .then(data => this.placeItems = data)
          .then(() => {
            this.display = "LocationsTable";
          })
          .catch(() => this.display = "TravelTable");
    },

    populateTravelForm() {
      this.place.date = this.selectPlace["date"];
      this.place.description = this.selectPlace["description"];
      this.place.lng = this.selectPlace["lng"];
      this.place.lat = this.selectPlace["lat"];
      this.address = this.place.address = this.selectPlace["address"];
      this.city = this.place.city = this.selectPlace["city"];
      this.country = this.place.country = this.selectPlace["country"];
    },

    editTravel(product) {
      this.selectPlace = {...product};
      this.populateTravelForm();
      this.display = "LocationInfoDate"
    },

    async updateTravel() {
      this.isloading = true;
      if (this.checkGeocodingChange()) {
        const response = await this.doPutRequest();
        if (response.status === 200) {
          this.isloading = false;
          this.$toast.add({
            severity: 'success',
            summary: 'Success Message',
            detail: 'Place modified correctly',
            life: 3000
          });
          this.isloading = false;
          setTimeout(() => this.$router.push("/travel/get"), 1500);
        }
      } else {
        try {
          const responseFeature = await this.getPlaceCoordinates();
          let features = responseFeature[0];
          this.place.address = features.properties["address_line1"];
          this.place.city = features.properties["city"];
          this.place.country = features.properties["country"];
          this.place.lng = features.geometry.coordinates[0]; //lng
          this.place.lat = features.geometry.coordinates[1]; //lat
          const response = await this.doPutRequest();
          if (response.status !== 200) {
            throw 'An error occurred, retry';
          }
          this.isloading = false;
          this.$toast.add({
            severity: 'success',
            summary: 'Success Message',
            detail: 'Place modified correctly',
            life: 3000
          });
          this.isloading = false;
          setTimeout(() => this.$router.push("/travel/get"), 1500);
        } catch (e) {
          this.$toast.add({
            severity: 'error',
            summary: 'Error Message',
            detail: e,
            life: 3000
          });
        }
      }
    },

    async doPutRequest() {
      return await axios.put(
          `http://localhost:8080/api/travel/place/${getUserId()}/${this.selectPlace["_id"]}`,
          JSON.stringify(this.place),
          {headers: authHeader()});
    },

    checkGeocodingChange() {
      return this.address === this.place.address && (this.city === this.place.city && this.country === this.place.country);
    },

    async getPlaceCoordinates() {
      //Code to geocode the address
      const API_KEY = "36323b8c882b4f6bb1f6edb419bef016";
      const ACCEPT_LEVEL = 0.65;
      const address = `${this.address}, ${this.city}, ${this.country}`;
      const geocodingUrl = `https://api.geoapify.com/v1/geocode/search?text=${encodeURIComponent(address)}&apiKey=${API_KEY}`;
      let geocoding = await axios.get(geocodingUrl);
      const data = await geocoding.data.features;
      if (data.length === 0 || data[0].properties.rank.confidence < ACCEPT_LEVEL) {
        throw 'Place not found, retry';
      }
      return data;
    },

    deletePlace(product) {
      this.selectPlace = {...product};
      this.deletePlaceDialog = true;
    },

    doDeleteRequest() {
      axios.delete(
          `http://localhost:8080/api/travel/place/${getUserId()}/${this.selectPlace["_id"]}`,
          {headers: authHeader()})
          .then(response => {
            if (response.status === 204) {
              this.placeItems = this.placeItems.filter(val => val["_id"] !== this.selectPlace["_id"]);
              this.travelService.getAllTravelDates().then(data => this.dateItems = data.sort((a, b) => {
                return new Date(a) - new Date(b)
              }));
              this.deletePlaceDialog = false;
              this.selectPlace = {};
              this.$toast.add({
                severity: 'success',
                summary: 'Success Message',
                detail: 'Place deleted',
                life: 3000
              });
            }
          })
          .catch(error => {
            this.$toast.add({
              severity: 'error',
              summary: 'Error Message',
              detail: 'An error occurred, retry',
              life: 3000
            });
          });
    },

    reset() {
      this.city = "";
      this.address = "";
      this.country = "";
      this.isloading = false;
    }

  },
}

</script>

<style scoped>
@media only screen and (max-width: 768px) {
  #locations {
    object-fit: scale-down
  }
}
</style>
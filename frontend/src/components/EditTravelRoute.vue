<template>
  <div>

    <div
        class="mb-8 text-left bg-white rounded-lg shadow-xl relative fadeindown animation-duration-900">
      <DataTable v-if="showType==='TravelTable'" v-model="selectTravel" :value="travelItems" data-key="id"
                 showGridlines>
        <Column field="date" header="Date" style="font-size: small"/>
        <Column field="routeInfo" header="Place route description" style="font-size: small"/>
        <Column field="transport" header="Transport" style="font-size: small"/>
        <Column :exportable="false" style="min-width:8rem">
          <template #body="slotProps">
            <Button class="p-button-rounded p-button-text p-mr-2" icon="pi pi-pencil"
                    @click.prevent="editRoute(slotProps.data)"/>
            <Button class="p-button-rounded p-button-text p-button-danger" icon="pi pi-trash"
                    @click.prevent="deleteTravel(slotProps.data)"/>
          </template>
        </Column>
        <template #footer>
          <div class="justify-content-between flex">
            <Button class="p-button-rounded p-button-text p-button-sm" icon="pi pi-arrow-left" @click="goBack"/>
          </div>
        </template>
      </DataTable>
    </div>

    <div v-if="showType==='Date'"
         class="px-8 py-4 mb-8 text-left bg-white shadow-xl relative fadeinleft animation-duration-900">
      <h3 class="text-2xl font-bold text-center">Modify Travel</h3>
      <div class="mt-4">
        <label class="block py-2">Date of Travel</label>
        <Calendar id="date" v-model="travel.date" v-tooltip.right="'Required'" dateFormat="dd-mm-yy"/>
      </div>
      <div class="flex justify-content-between mt-4">
        <Button class="p-button-rounded p-button-text p-button-sm" icon="pi pi-arrow-left"
                @click.prevent="showType='TravelTable'"/>
        <Button class="p-button p-button-outlined p-button-sm" label="Continue"
                @click.prevent="this.showType='TravelInfo'"></Button>
      </div>
    </div>

    <div v-if="showType==='TravelInfo'"
         class="px-8 py-4 mb-8 text-left bg-white shadow-xl relative rounded-lg fadeinright animation-duration-900">
      <h3 class="text-2xl font-bold text-center">Travel Route</h3>
      <div class="mt-2">
        <label class="block py-2">Means Of Transport</label>
        <Dropdown v-model="travel.transport" :options="transport" class="w-full" placeholder="Select a means"/>
      </div>
      <div class="mt-2">
        <label class="block py-2">Description</label>
        <InputText v-model="travel.routeInfo" v-tooltip.right="'Required'" class="w-full"
                   placeholder="Christmas Holiday" type="text"
                   v-bind:class="{'p-invalid' : this.travel.routeInfo !== '' && this.travel.routeInfo.length < 5}"/>
      </div>
      <div class="mt-2">
        <label class="block py-2">GeoCoordinates</label>
        <Textarea v-model="latAndLng" v-tooltip.right="'Add list of lat and lng'" class="w-full" cols="30"
                  placeholder="e.g [ [lat,lng], [lat,lng] ]" rows="2"/>
        <Divider align="center">Or</Divider>
        <input ref="doc" v-tooltip.right="'Upload a GeoJson file'" accept="application/geo+json"
               class="text-gray-500 px-3 py-2 border-1 rounded"
               type="file" @change="readFile()">
      </div>
      <div class="flex justify-content-center mt-4">
        <Button :loading="confirmLoading" class="p-button p-button-sm" label="Confirm"
                @click.prevent="updateTravelRoute"></Button>
      </div>
    </div>

    <Dialog v-model:visible="deleteTravelDialog" :modal="true" :style="{width: '450px'}" header="Confirm">
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle p-mr-3"/>
        <span class="px-2"> Are you sure you want to delete this travel?<br></span>
        <span class="px-2"> If 'YES', all places of this travel will be deleted.</span>
      </div>
      <template #footer>
        <Button class="p-button-text p-button-sm p-button-danger" icon="pi pi-times" label="No"
                @click="deleteTravelDialog = false"/>
        <Button class="p-button-text p-button-sm " icon="pi pi-check" label="Yes" @click="doDeleteRequest"/>
      </template>
    </Dialog>

  </div>
</template>

<script>
import TravelsService from "@/services/travels.service";
import Travel from "@/model/travel";
import axios from "axios";
import authHeader from "@/services/auth.header";
import getUserId from "@/services/user.auth.details";

export default {
  props: {
    date: String,
  },
  name: "EditTravelRoute",
  data() {
    return {
      travel: new Travel(),
      latAndLng: "",
      file: undefined,
      geojson: undefined,
      showType: 'TravelTable',
      selectTravel: undefined,
      travelItems: undefined,
      transport: [
        'Auto',
        'Moto',
        'Walk',
        'Airplane',
        'Other'
      ],
      confirmLoading: false,
      deleteTravelDialog: false,
      travelService: undefined,
    }
  },
  created() {
    this.travelService = new TravelsService();
  },
  mounted() {
    this.travelService.getTravelsByDate(this.date).then(data => this.travelItems = data);
  },
  methods: {

    goBack() {
      this.showType = "";
      this.$parent.$data.display = "SelectTravelDate";
    },

    editRoute(data) {
      this.selectTravel = {...data};
      this.populateForm();
      this.showType = 'Date';
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

    populateForm() {
      this.travel.date = this.selectTravel["date"];
      this.travel.transport = this.selectTravel["transport"];
      this.travel.routeInfo = this.selectTravel["routeInfo"];
    },

    async updateTravelRoute() {
      this.confirmLoading = true;
      const UPDATE_ROUTE_URL = `http://localhost:8080/api/travel/${getUserId()}/${this.selectTravel["id"]}`;
      this.travel.latAndLng = this.setLatAndLng();
      const response = await axios.put(UPDATE_ROUTE_URL, JSON.stringify(this.travel), {headers: authHeader()});
      if (response.status !== 200) {
        this.$toast.add({
          severity: 'error',
          summary: 'Error Message',
          detail: 'An error occurred, retry',
          life: 3000
        });
        setTimeout(() => this.$router.push("/"), 1500);
      } else {
        this.confirmLoading = false;
        this.$toast.add({
          severity: 'success',
          summary: 'Success Message',
          detail: 'Place updated!',
          life: 3000
        });
        setTimeout(() => this.$router.push("/"), 1500);
      }
    },

    setLatAndLng() {
      return this.latAndLng === "" && this.geojson === undefined ? this.selectTravel["latAndLng"] : (this.geojson === undefined ? this.latAndLng : this.geojson.features[0].geometry.coordinates)
    },

    deleteTravel(data) {
      this.selectTravel = {...data};
      this.deleteTravelDialog = true;
    },

    async doDeleteRequest() {
      const DELETE_TRAVEL_URL = `http://localhost:8080/api/travel/${getUserId()}/${this.selectTravel["id"]}`
      try {
        const response = await axios.delete(DELETE_TRAVEL_URL, {headers: authHeader()});
        console.log(response)
        if (response.status === 204) {
          this.travelItems = this.travelItems.filter(val => val["id"] !== this.selectTravel["id"]);
          this.travelService.getAllTravelDates().then(data => this.$parent.$data.dateItems = data);
          this.$parent.$data.dateItems = await this.travelService.getAllTravelDates();
          this.deleteTravelDialog = false;
          this.selectTravel = {};
          this.$toast.add({
            severity: 'success',
            summary: 'Success Message',
            detail: 'Place deleted',
            life: 3000
          });
        }
      } catch (e) {
        this.$toast.add({
          severity: 'error',
          summary: 'Error Message',
          detail: 'An error occurred, retry',
          life: 3000
        });
      }
    },
  }
}
</script>

<style scoped>

</style>
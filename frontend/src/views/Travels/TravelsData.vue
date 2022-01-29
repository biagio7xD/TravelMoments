<template>
  <div class="grid items-center justify-center min-h-screen">

    <Toast/>
    <div class="col-11 w-auto h-auto">
      <div class="mb-8 mt-4 text-left bg-white rounded-lg shadow-xl relative scalein animation-duration-900">
        <div class="w-full px-4 py-4 surface-50 justify-content-between flex">
          <Dropdown id="date-maps" v-model="selectDate" :options="dateItems" :virtualScrollerOptions="{ itemSize: 10 }"
                    placeholder="Select Date" @change="getTravelsByDate"/>
          <SelectButton id="mode-selector" v-model="selectMode" v-tooltip.top="'Change visualization mode'"
                        :disabled="this.selectDate === undefined ? true : false"
                        :options="modeOptions"
                        dataKey="value" option-value="value"
                        @click.prevent="modeChanger">
            <template #option="slotProps">
              <i :class="slotProps.option.icon"></i>
            </template>
          </SelectButton>
        </div>

        <DataTable v-if="display==='TravelTable'" id="travel-table" :value="listOfTravel"
                   class="scalein animation-duration-900"
                   showGridlines>
          <Column field="date" header="Date" style="font-size: small"></Column>
          <Column field="routeInfo" header="Description of travel" style="font-size: small"></Column>
          <Column field="transport" header="Transport" style="font-size: small"></Column>
          <template #footer>
          </template>
        </DataTable>

        <DataTable v-if="display==='LocationTable'" id="place-table" :value="listOfPlaces"
                   class="scalein animation-duration-900"
                   showGridlines>
          <Column field="date" header="Date" style="font-size: small"></Column>
          <Column field="address" header="Address" style="font-size: small"></Column>
          <Column field="city" header="City" style="font-size: small"></Column>
          <Column field="description" header="Description" style="font-size: small"></Column>
          <Column field="lat" header="Latitude" style="font-size: small"></Column>
          <Column field="lng" header="Longitude" style="font-size: small"></Column>
          <template #footer>
          </template>
        </DataTable>

        <GMaps v-if="display==='Maps'" id="maps" :date="this.selectDate" :dateSelected="this.selectDate !== undefined"
               :value="listOfPlaces"
               class="scalein animation-duration-900"/>

      </div>
    </div>
  </div>
</template>

<script>
import TravelsService from "@/services/travels.service";
import GMaps from "@/components/GMaps";

export default {
  name: "TravelsData",
  components: {GMaps},
  data() {
    return {
      listOfTravel: undefined,
      listOfPlaces: undefined,
      selectDate: undefined,
      display: "TravelTable",
      selectMode: 'Table',
      dateItems: undefined,
      modeOptions: [
        {icon: 'pi pi-table', value: 'Table'},
        {icon: 'pi pi-map-marker', value: 'Maps'}],
      travelService: undefined,
    }
  },
  created() {
    this.travelService = new TravelsService();
  },
  mounted() {
    this.travelService.getAllTravels().then(data => this.listOfTravel = data.sort((a, b) => {
      return new Date(a.date) - new Date(b.date)
    }))
    this.travelService.getAllTravelDates().then(data => this.dateItems = data.sort((a, b) => {
      return new Date(a) - new Date(b)
    }));

  },

  methods: {

    modeChanger() {
      if (this.selectMode === 'Maps') {
        this.display = 'Maps'
      }

      if (this.selectMode === "Table") {
        this.display = this.getTableMode();
      }
    },

    getTravelsByDate() {
      this.travelService.getPlacesByDate(this.selectDate).then(data => this.listOfPlaces = data);
      this.modeChanger();
    },

    getTableMode() {
      return this.selectDate === undefined ? "TravelTable" : "LocationTable";

    },

  },
}
</script>


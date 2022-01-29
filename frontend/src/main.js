import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from "./store";

import PrimeVue from 'primevue/config';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import Password from 'primevue/password';
import Dialog from 'primevue/dialog';
import Checkbox from "primevue/checkbox";
import ToastService from 'primevue/toastservice';
import Calendar from 'primevue/calendar';
import Dropdown from 'primevue/dropdown';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import './assets/css/tailwind.css'
import Steps from 'primevue/steps';
import 'primevue/resources/themes/tailwind-light/theme.css'
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';
import Divider from "primevue/divider";
import Toast from "primevue/toast";
import TabMenu from 'primevue/tabmenu';
import Tooltip from 'primevue/tooltip';
import Card from 'primevue/card';
import SelectButton from 'primevue/selectbutton';
import InputSwitch from 'primevue/inputswitch';
import Textarea from 'primevue/textarea';


const app = createApp(App);

app.use(PrimeVue, {ripple: true});
app.use(store);

app.directive('tooltip', Tooltip);


app.component('InputText', InputText);
app.component('Button', Button);
app.use(ToastService);
app.component('Password', Password);
app.component('Dialog', Dialog);
app.component('Checkbox', Checkbox);
app.component('Calendar', Calendar);
app.component('Divider', Divider);
app.component("Dropdown", Dropdown);
app.component('Steps', Steps);
app.component('Card', Card);
app.component('Toast', Toast);
app.component('TabMenu', TabMenu);
app.component('DataTable', DataTable);
app.component('Column', Column)
app.component('SelectButton', SelectButton);
app.component('InputSwitch', InputSwitch);
app.component('Textarea', Textarea);


app.use(router).mount('#app')

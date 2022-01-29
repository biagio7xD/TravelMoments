import axios from "axios";
import getUserId from "@/services/user.auth.details";
import authHeader from "@/services/auth.header";

export default class TravelsService {

    getAllTravels() {
        return axios.get(`http://localhost:8080/api/travel/${getUserId()}`, {headers: authHeader()}).then(response => response.data);
    }

    getAllTravelDates() {
        return axios.get(`http://localhost:8080/api/travel/${getUserId()}/dates`, {headers: authHeader()}).then(response => response.data);
    }

    getPlacesByDate(date) {
        return axios.get(`http://localhost:8080/api/travel/${getUserId()}/${date}/places`, {headers: authHeader()}).then(response => response.data);
    }

    getTravelsByDate(date) {
        return axios.get(`http://localhost:8080/api/travel/${getUserId()}/${date}`, {headers: authHeader()}).then(response => response.data);
    }
}
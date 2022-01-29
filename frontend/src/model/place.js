export default class Place {
    constructor(date, description = "", address, city, country, lng, lat) {
        this.date = date;
        this.description = description;
        this.address = address;
        this.city = city;
        this.country = country;
        this.lng = lng;
        this.lat = lat;
    }
}
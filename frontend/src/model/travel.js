export default class Travel {
    constructor(routeInfo = "", date = "", latAndLng, transport = "") {
        this.routeInfo = routeInfo;
        this.date = date;
        this.latAndLng = latAndLng
        this.transport = transport;
    }
}
export class Point {

    lng: number;
    lat: number;

    constructor(lng: number, lat: number) {
        this.lng = lng;
        this.lat = lat;
    }

    public equals(p: Point) {
        return this.lng == p.lng && this.lat == p.lat;
    }

}
export class PointENObj {

	lng: {
		h: number,
		m: number,
		s: number,
	};
	lat: {
		h: number,
		m: number,
		s: number,
	};

	constructor(lng: { h: number, m: number, s: number }, lat: { h: number, m: number, s: number }) {
		this.lng = {
			h: lng.h,
			m: lng.m,
			s: lng.s,
		};
		this.lat = {
			h: lat.h,
			m: lat.m,
			s: lat.s,
		};
	}

	valid(): boolean {
		let isNotNull = (
			this.lng.h != null &&
			this.lng.m != null &&
			this.lng.s != null &&
			this.lat.h != null &&
			this.lat.m != null &&
			this.lat.s != null
		);
		if (!isNotNull) {
			return false;
		}
		return (
			this.lng.h > -180 && this.lng.h < 180 &&
			this.lng.m >= 0 && this.lng.m < 60 &&
			this.lng.s >= 0 && this.lng.s < 60 &&
			this.lat.h > -90 && this.lat.h < 90 &&
			this.lat.m >= 0 && this.lat.m < 60 &&
			this.lat.s >= 0 && this.lat.s < 60
		);
	}

}

package acs.boundary;

import java.util.Objects;

public class LocationBoundery {
	
	private Float lat;
	private Float lng;

	public LocationBoundery() {
	}

	public LocationBoundery(Float lat, Float lng) {
		
		super();
		this.lat = lat;
		this.lng = lng;
		
	}
	

	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLng() {
		return lng;
	}
	public void setLng(Float lng) {
		this.lng = lng;
	}

	@Override
	public boolean equals(Object o) {
		// self check
		if (this == o)
			return true;
		// null check
		if (o == null)
			return false;
		// type check and cast
		if (getClass() != o.getClass())
			return false;
		LocationBoundery location = (LocationBoundery) o;
		// field comparison
		return Objects.equals(this.lng, location.lng) && Objects.equals(this.lat, location.lat);
	}

	@Override
	public int hashCode() {
		return (int) (this.lat + this.lng);
	}
}

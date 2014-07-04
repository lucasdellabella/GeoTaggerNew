package com.hci.geotagger.Objects;

import java.io.Serializable;

public class GeoLocation implements Serializable {

	private double latitude, longitude;
	
	public GeoLocation(double lat, double lon)
	{
		this.setLatitude(lat);
		this.setLongitude(lon);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}

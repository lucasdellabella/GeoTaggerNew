/*
 * Location handler class is used to handle the GPS Location
 * It contains methods to return the current location as well
 * as show a prompt to enable GPS if it is disabled.
 */
package com.hci.geotagger.common;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.hci.geotagger.Objects.GeoLocation;

public class LocationHandler {
	
	Context context;
	AlertHandler alertHandler;
	LocationManager locationManager;
	GeoLocation geo;
	
	public LocationHandler(Context c)
	{
		this.context = c;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}
	/*
	 * Return the current location of the device, if available
	 * If not, show prompt to enable GPS
	 */
	public GeoLocation getCurrentLocation()
	{
		boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (gpsEnabled)
		{
			Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			geo = new GeoLocation(loc.getLatitude(), loc.getLongitude());
		}
		else
		{
			alertHandler.showGpsDisabledAlert(context);
		}
		return geo;
	}
	   
}

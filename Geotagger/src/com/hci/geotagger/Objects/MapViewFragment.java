//package com.teampterodactyl.fragments;
package com.hci.geotagger.Objects;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.hci.geotagger.Objects.GeoLocation;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
//import com.teampterodactyl.mobsciinterface.R;
import com.hci.geotagger.R;

/**
 * MapViewFragment implements the fragment that allows for the viewing of a tag's
 * position on a map that can be interacted with. When the user clicks on the map button
 * in the TagViewActivity, this view will be displayed.
 * 
 * @author: Spencer Kordecki
 * @version: January 20, 2014
 */
//public class MapViewFragment extends SherlockFragment
@SuppressLint("ValidFragment")
public class MapViewFragment extends SupportMapFragment
{	
	private GeoLocation geo;
	private LatLng location;
	private String locationName;
	
	/*
	 * Creates the view that the user will see when the fragment is created, similar 
	 * to onCreate methods seen in activities.
	 */
	/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		
		
		//if the view hasn't been created, create it, otherwise, do nothing
	    if (view != null) 
	    {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	        {
	        	parent.removeView(view);
	        }
	    }
	    try 
	    {
	    	
	        view = inflater.inflate(R.layout.fragment_map_view, container, false);
	     
	        
	    } 
	    catch (InflateException e) 
	    {
	        //map is already inflated, view map as it was prior to switching fragments
	    }
	    return view;
	}
	
	*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("MapViewFragment", "MapViewFragment on create called");
	/*	
		 GoogleMap mMap = super.getMap();
	        //add the markers
	        if (mMap != null) 
	        {
	            mMap.setMyLocationEnabled(true);
	        }
	        */
	}
	
	 @Override
	    public void onResume() 
	 {
	        super.onResume();
	        GoogleMap mMap = super.getMap();
	        if (mMap != null) 
	        {
	        	mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	        	CameraUpdate update= CameraUpdateFactory.newLatLngZoom(location,16);
	        	mMap.addMarker(new MarkerOptions()
	            .position(location)
	            .title(""));
	        	mMap.animateCamera(update);
	            
	        }
	    }
	 
	 public MapViewFragment(GeoLocation geo)
	 {
		 this.geo = geo;
		 location = new LatLng(geo.getLatitude(), geo.getLongitude());
	 }
	 
	 public MapViewFragment()
	 {
		 
	 }
	 
	 public void setLocationName(String locationName)
	 {
		 this.locationName = locationName;
		 
	 }
}



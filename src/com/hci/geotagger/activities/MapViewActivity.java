package com.hci.geotagger.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import com.hci.geotagger.R;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.MapViewHandler;

public class MapViewActivity extends FragmentActivity
{
	private SupportMapFragment smf;
	private GoogleMap mv;
	private Runnable viewTags;
	private int userID;
	private ArrayList<Tag> tags = null;
	private MapViewHandler mapViewHandler;
  
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
				
        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mv = smf.getMap();
        mv.getUiSettings().setZoomControlsEnabled(false);
        mv.getUiSettings().setMyLocationButtonEnabled(true);
        mv.setMapType(GoogleMap.MAP_TYPE_NORMAL); //sets satellite to off by default
        mv.setMyLocationEnabled(true);
        
        userID = UserSession.CURRENTUSER_ID;
                        
        //find the user's tags
        retrieveTags();
        
        //populate the map using the tag array list
	}
	
	// setup separate thread to retrieve tags
	private void retrieveTags() 
	{
		// retrieve the tags in separate thread
		viewTags = new Runnable() 
		{
			@Override
			public void run() 
			{
				//userName = accountHandler.GetUsernameFromId(userID);
				Log.d("TagListActivity", "ID: " + userID);

				getTags();
			}
		};
		
		Thread thread = new Thread(null, viewTags, "GetTagThread");
		thread.start();
		ProgressDialog PD = ProgressDialog.show(MapViewActivity.this, "Please Wait",
				"Retrieving tags...", true);
	}
	
	private void getTags() 
	{	
		tags = new ArrayList<Tag>();
		JSONObject obj;
		JSONArray tagData = mapViewHandler.GetTagsById(userID);
		
		if (tagData != null) 
		{
			// loop through each entry in the json array (each tag encoded as
			// JSON)
			for (int i = 0; i < tagData.length(); i++) 
			{
				obj = null;
				try 
				{
					obj = tagData.getJSONObject(i);
				} 
				catch (JSONException e) 
				{
					Log.d("TagList GetTags",
							"Error getting JSON Object from array.");
					e.printStackTrace();
				}

				if (obj != null) 
				{
					Tag t = mapViewHandler.createTagFromJSON(obj);
					tags.add(t);
				}
			}
		}	
	}	
}
package com.hci.geotagger.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.hci.geotagger.R;
import com.hci.geotagger.Objects.GeoLocation;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AccountHandler;
import com.hci.geotagger.connectors.JSONParser;
import com.hci.geotagger.connectors.TagHandler;

/**
 * This class shows a Map view. Currently not used in this iteration of GeoTagger.
 * Separate MapViewViewFragment used instead in TagViewActivity(please see com.hci.geotagger.Objects package
 * for Fragments used and their implementations).
 * 
 */
public class MapViewActivity extends FragmentActivity
{
	private GoogleMap map;
	private Context context;
	private SupportMapFragment smf;
	private GoogleMap mv;
	private CameraUpdate zoom;
	private Runnable viewTags;
	private String userName;
	private int userID;
	private AccountHandler accountHandler;
	private ProgressDialog PD = null;
	private ArrayList<Tag> tags = null;
	private TagHandler tagHandler;
	private static String tagOpURL = Constants.TAGOP_URL;
	private JSONParser jsonParser;

  
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		context = this;
		
        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mv = smf.getMap();
        mv.getUiSettings().setZoomControlsEnabled(false);
        mv.getUiSettings().setMyLocationButtonEnabled(true);
        mv.setMapType(GoogleMap.MAP_TYPE_NORMAL); //sets satellite to off by default
        mv.setMyLocationEnabled(true);
        
        userID = UserSession.CURRENTUSER_ID;
        
        jsonParser = new JSONParser();
                
        //find the user's tags
        retrieveTags();
        
        //populate the map using the tag array list
	}
	
	
	private JSONArray GetTagsById(int oId)
	{
		 // Building Parameters
        List<NameValuePair> getTagsParams = new ArrayList<NameValuePair>();
        getTagsParams.add(new BasicNameValuePair("operation", Constants.OP_GETTAGS_BYID));
        getTagsParams.add(new BasicNameValuePair("oId", Integer.toString(oId)));
        
		try
		{
			 //make webservice call to get tags
			JSONArray resultsArray = jsonParser.getJSONArrayFromUrl(tagOpURL, getTagsParams);
			if(resultsArray != null)
			{
				//Log.d("TagHandler GetTagsByID", "JSON Response from PHP: " + resultsArray.toString());
				//parse result into array of jsonobjects and return it
				return resultsArray;
			}
			else
			{
				Log.d("TagHandler GetTagsByID", "No Results");
				//parse result into array of jsonobjects and return it
				return null;
			}
			
		}
		catch (Exception ex)
		{
			Log.d("GetTagsById", "Exception occurred getting tags, returning null.");
			return null;
		}
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
		PD = ProgressDialog.show(MapViewActivity.this, "Please Wait",
				"Retrieving tags...", true);
	}
	
	
	public Tag createTagFromJSON(JSONObject json)
	{
		Date d = new Date();
    	try 
    	{
    		//format the date
    		SimpleDateFormat ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d = ts.parse(json.getString("CreatedDateTime"));
			double lat, lon;
			if(! json.getString("Latitude").equalsIgnoreCase("null") && ! json.getString("Longitude").equalsIgnoreCase("null"))
			{
				lat = json.getDouble("Latitude");
				lon = json.getDouble("Longitude");
			}
			else 
			{
				lat = 0;
				lon = 0;
			}
			
			GeoLocation geo = new GeoLocation(lat, lon);
			//instantiate the tag object with properties from JSON
			Tag t = new Tag(json.getLong("TagID"), json.getString("Name"), json.getString("Description"), json.getString("ImageUrl"),
					json.getString("Location"), json.getString("Category"), json.getInt("RatingScore"),
					json.getInt("OwnerID"), json.getString("Username"), geo, json.getInt("Visibility"), d);
			return t;
			
			
		} 
    	catch (JSONException e) 
    	{
    		Log.d("TagHandler", "CreateTag from JSONObject failed");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.d("TagHandler", "Problem parsing timestamp from JSON");
			e.printStackTrace();
		}
    	return null;
	}
	
	private void getTags() 
	{	
		tags = new ArrayList<Tag>();
		JSONObject obj;
		JSONArray tagData = GetTagsById(userID);
		
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
					Tag t = createTagFromJSON(obj);
					tags.add(t);
				}
			}
		}	
	}	
}
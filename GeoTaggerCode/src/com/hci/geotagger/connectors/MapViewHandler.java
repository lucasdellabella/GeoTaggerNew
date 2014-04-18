package com.hci.geotagger.connectors;

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

import android.util.Log;

import com.hci.geotagger.Objects.GeoLocation;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.common.Constants;

public class MapViewHandler 
{
	private JSONParser jsonParser;
	private static String tagOpURL = Constants.TAGOP_URL;
	
	public JSONArray GetTagsById(int oId)
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
}

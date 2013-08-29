/*
 * Tag handler class is responsible for making calls to the webservice 
 * for tag operations, such as adding and retrieving tags.
 * 
 * Chris Loeschorn
 * Spring 2013
 */
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

import com.hci.geotagger.Objects.Comment;
import com.hci.geotagger.Objects.GeoLocation;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.Objects.UserAccount;
import com.hci.geotagger.common.Constants;

public class TagHandler {

	private JSONParser jsonParser;
	private static String tagOpURL = Constants.TAGOP_URL;
	
	public TagHandler()
	{
        jsonParser = new JSONParser();
    }
	/*
	 * Add a tag to the database
	 */
	public JSONObject AddTag(Tag t)
	{
		 // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_ADD_TAG));
        params.add(new BasicNameValuePair("oId", Integer.toString(t.getOwner().getId())));
        params.add(new BasicNameValuePair("vis", Integer.toString(t.getVisibility())));
        params.add(new BasicNameValuePair("name", t.getName()));
        params.add(new BasicNameValuePair("desc", t.getDescription()));
        params.add(new BasicNameValuePair("imgUrl", t.getImageUrl()));
        params.add(new BasicNameValuePair("loc", t.getLocationString()));
        
        String lat, lon;
        if(t.getLocation() != null)
        {
        	 lat = Double.toString(t.getLocation().getLatitude());
        	 lon = Double.toString(t.getLocation().getLongitude());
        }
        else { lat = "null"; lon = "null"; }
        params.add(new BasicNameValuePair("lat", lat));
        params.add(new BasicNameValuePair("lon", lon));
        params.add(new BasicNameValuePair("cat", t.getCategory()));
        
        //make webservice call to add tag to db
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(tagOpURL, params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return json;
		}
		catch (Exception ex)
		{
			Log.d("TagHandler AddTag", "Exception occurred adding tag, returning null.");
			return null;
		}
	}
	/*
	 * Return a JSONArray (array of JSON Objects) containing
	 * all the tags for the given user ID
	 */
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
	//delete a tag from the db
	public boolean deleteTag(long tagId)
	{
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_TAG));
        params.add(new BasicNameValuePair("tId", Long.toString(tagId)));
        
        //make webservice call to remove tag from db
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(tagOpURL, params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return true;
		}
		catch (Exception ex)
		{
			Log.d("TagHandler AddTag", "Exception occurred deleting tag, returning error.");
			return false;
		}
	}
	/*
	 * Add a tag comment to the database
	 */
	public JSONObject AddTagComment(Long tagID, String text, String username)
	{
		 // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_ADD_TAGCOMMENT));
        params.add(new BasicNameValuePair("tagId", Long.toString(tagID)));
        params.add(new BasicNameValuePair("comment", text ));
        params.add(new BasicNameValuePair("uName", username));
       
        //make webservice call to add tag comment to db
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(tagOpURL, params);
			Log.d("TagHandler AddTagComment", "JSON Response from PHP: " + json.toString());
			return json;
		}
		catch (Exception ex)
		{
			Log.d("TagHandler AddTagComment", "Exception occurred adding comment, returning null.");
			return null;
		}
	}
	
	public Comment CreateCommentFromJson(JSONObject json)
	{
		Date d = new Date();
    	try 
    	{
    		//format the date
    		SimpleDateFormat ts = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			d = ts.parse(json.getString("CreatedDateTime"));
			//instantiate the comment object with properties from JSON
			Comment c = new Comment(json.getLong("ID"),json.getLong("ParentTagID"),
					json.getString("Text"), json.getString("Username"),  d);
			return c;			
		} 
    	catch (JSONException e) 
    	{
    		Log.d("TagHandler", "CreateComment from JSONObject failed");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.d("TagHandler", "Create Comment: Problem parsing timestamp from JSON");
			e.printStackTrace();
		}
    	return null;
	}
	//retrieve all comments from a tag
	public JSONArray getTagComments(long tagID) {
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_GET_TAGCOMMENTS));
        params.add(new BasicNameValuePair("tagId", Long.toString(tagID)));
        
    	try
		{
			 //make webservice call to get tags
			JSONArray resultsArray = jsonParser.getJSONArrayFromUrl(tagOpURL, params);
			if(resultsArray != null)
			{
				Log.d("TagHandler GetTagComments", "JSON Response from PHP: " + resultsArray.toString());
				//parse result into array of jsonobjects and return it
				return resultsArray;
			}
			else
			{
				Log.d("TagHandler GetTagComments", "No Results");
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
	
	//delete a tag comment from the db
		public boolean deleteTagComment(long commentId)
		{
			// Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_TAGCOMMENT));
	        params.add(new BasicNameValuePair("cId", Long.toString(commentId)));
	        
	        //make webservice call to remove tag from db
			try
			{
				JSONObject json = jsonParser.getJSONFromUrl(tagOpURL, params);
				Log.d("TagHandler Delete Tag", "JSON Response from PHP: " + json.toString());
				return true;
			}
			catch (Exception ex)
			{
				Log.d("TagHandler AddTag", "Exception occurred deleting tag, returning error.");
				return false;
			}
		}
}

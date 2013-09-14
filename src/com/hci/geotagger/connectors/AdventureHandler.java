/*
 * Adventure handler class is responsible for making calls to the webservice 
 * for adventure operations, such as adding and retrieving adventures.
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

import com.hci.geotagger.Objects.Adventure;
import com.hci.geotagger.common.Constants;

public class AdventureHandler 
{
	private JSONParser jsonParser;
	private static String advOpURL = Constants.ADV_URL;		
	
	public AdventureHandler()
	{
        jsonParser = new JSONParser();               
    }
	/*
	 * Add an Adventure to the database
	 */
	public JSONObject AddAdventure(Adventure a)
	{
		 // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_ADD_ADV));
        params.add(new BasicNameValuePair("uId", Integer.toString(a.getCreatorID())));
        params.add(new BasicNameValuePair("name", a.getName()));
        params.add(new BasicNameValuePair("desc", a.getDescription()));              
        
        //make webservice call to add adventure to db
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return json;
		}
		catch (Exception ex)
		{
			Log.d("AdventureHandler AddAdventure", "Exception occurred adding adventure, returning null.");
			return null;
		}
	}
	
	//delete an adventure from the db
	public boolean deleteAdventure(long id)
	{
		// Building Parameters
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_ADV));
	    params.add(new BasicNameValuePair("id", Long.toString(id)));
	      
	    //make webservice call to remove tag from db
	    try
		{
			JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return true;
		}
		catch (Exception ex)
		{
			Log.d("AdventureHandler deleteAdventure", "Exception occurred deleting adventure, returning error.");
			return false;
		}
	}
	
	/*
	 * Return a JSONArray (array of JSON Objects) containing
	 * all the adventures for the given user ID
	 */
	public JSONArray getAllAdventuresUserPartOf(int id)
	{
		 // Building Parameters
        List<NameValuePair> getAdvsParams = new ArrayList<NameValuePair>();
        getAdvsParams.add(new BasicNameValuePair("operation", Constants.OP_GETADVS_BYID));
        getAdvsParams.add(new BasicNameValuePair("id", Integer.toString(id)));
        
		try
		{
			 //make webservice call to get tags
			JSONArray resultsArray = jsonParser.getJSONArrayFromUrl(advOpURL, getAdvsParams);
			if(resultsArray != null)
			{
				//Log.d("TagHandler GetTagsByID", "JSON Response from PHP: " + resultsArray.toString());
				//parse result into array of jsonobjects and return it
				return resultsArray;
			}
			else
			{
				Log.d("AdventureHandler GetAdventuresByID", "No Results");
				//parse result into array of jsonobjects and return it
				return null;
			}
			
		}
		catch (Exception ex)
		{
			Log.d("GetAdventuresById", "Exception occurred getting adventures, returning null.");
			return null;
		}
	}
	
	public Adventure createAdventureFromJSON(JSONObject json)
	{
		Date d = new Date();
    	try 
    	{
    		//format the date
    		SimpleDateFormat ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d = ts.parse(json.getString("CreatedDateTime"));			
			//instantiate the tag object with properties from JSON
			Adventure a = new Adventure(json.getLong("ID"), 
					json.getInt("Visibility"), json.getInt("OwnerID"),
					json.getString("Name"), json.getString("Description"), 
					/*json.getString("Creatorname"),*/ d);				
			return a;			
		} 
    	catch (JSONException e) 
    	{
    		Log.d("AdventureHandler", "CreateTag from JSONObject failed");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.d("AdventureHandler", "Problem parsing timestamp from JSON");
			e.printStackTrace();
		}
    	return null;
	}
	
	/*
	 * Add tags to the Adventure.
	 */
	public boolean addTagToAdventure(long tagId, long advId)
	{
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_ADD_TAG));
        params.add(new BasicNameValuePair("advId", Long.toString(advId)));
        params.add(new BasicNameValuePair("tagId", Long.toString(tagId)));
               
        //make webservice call to add tag to adventure
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return true;
		}
		catch (Exception ex)
		{
			Log.d("AdventureHandler AddTag", "Exception occurred adding tag, returning null.");
			return false;
		}
	}
	
	/*
	 * Return a JSONArray (array of JSON Objects) containing
	 * all the tags for the given adventure ID.
	 */	
	public JSONArray getAllAdventureTags(long id)
	{
		List<NameValuePair> getTagsParams = new ArrayList<NameValuePair>();
		getTagsParams.add(new BasicNameValuePair("operation", Constants.OP_GETTAGS_BYID));
		getTagsParams.add(new BasicNameValuePair("oId", Long.toString(id)));
		
		try
		{
			 //make webservice call to get tags
			JSONArray resultsArray = jsonParser.getJSONArrayFromUrl(advOpURL, getTagsParams);
			if(resultsArray != null)
			{
				//Log.d("TagHandler GetTagsByID", "JSON Response from PHP: " + resultsArray.toString());
				//parse result into array of jsonobjects and return it
				return resultsArray;
			}
			else
			{
				Log.d("AdventureHandler GetTagsInAdventure", "No Results");
				//parse result into array of jsonobjects and return it
				return null;
			}
			
		}
		catch (Exception ex)
		{
			Log.d("GetTagsInAdventure", "Exception occurred getting tags, returning null.");
			return null;
		}
	}	
	
	//delete a tag from the adventure
	public boolean removeTagFromAdventure(long tagId, long advId)
	{
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_TAG));
        params.add(new BasicNameValuePair("advId", Long.toString(advId)));
        params.add(new BasicNameValuePair("tagId", Long.toString(tagId)));
        
        //make webservice call to remove tag from adventure
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return true;
		}
		catch (Exception ex)
		{
			Log.d("AdventureHandler deleteTag", "Exception occurred deleting tag, returning error.");
			return false;
		}
	}
	
	/*
	 * Add people to the Adventure.
	 */
	public boolean addUserToAdventureById(int uId, long advId)
	{
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_ADD_PERSON));
        params.add(new BasicNameValuePair("advId", Long.toString(advId))); 
        params.add(new BasicNameValuePair("uId", Integer.toString(uId)));
                
        //make webservice call to add tag to adventure
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return true;
		}
		catch (Exception ex)
		{
			Log.d("AdventureHandler AddPeople", "Exception occurred adding people, returning null.");
			return false;
		}
	}
	
	/*
	 * Return a JSONArray (array of JSON Objects) containing
	 * all the tags for the given adventure ID.
	 */	
	public JSONArray GetPeopleInAdventure(long aId)
	{
		List<NameValuePair> getTagsParams = new ArrayList<NameValuePair>();
		getTagsParams.add(new BasicNameValuePair("operation", Constants.OP_GETPEOPLE_BYID));
		getTagsParams.add(new BasicNameValuePair("id", Long.toString(aId))); // might need to be oId
		
		try
		{
			 //make webservice call to get tags
			JSONArray resultsArray = jsonParser.getJSONArrayFromUrl(advOpURL, getTagsParams);
			if(resultsArray != null)
			{
				//Log.d("TagHandler GetTagsByID", "JSON Response from PHP: " + resultsArray.toString());
				//parse result into array of jsonobjects and return it
				return resultsArray;
			}
			else
			{
				Log.d("AdventureHandler GetPeopleInAdventure", "No Results");
				//parse result into array of jsonobjects and return it
				return null;
			}
			
		}
		catch (Exception ex)
		{
			Log.d("GetTagsInAdventure", "Exception occurred getting tags, returning null.");
			return null;
		}
	}	
	
	//delete a person from the adventure
	public boolean removeUserFromAdventure(int uId, long advId)
	{
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_TAG));
        params.add(new BasicNameValuePair("advId", Long.toString(advId)));
        params.add(new BasicNameValuePair("personId", Integer.toString(uId)));
        
        //make webservice call to remove tag from adventure
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return true;
		}
		catch (Exception ex)
		{
			Log.d("AdventureHandler deletePeople", "Exception occurred deleting people, returning error.");
			return false;
		}
	}	
}
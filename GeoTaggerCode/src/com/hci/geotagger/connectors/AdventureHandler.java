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

import android.content.Context;
import android.util.Log;

import com.hci.geotagger.Objects.Adventure;
import com.hci.geotagger.Objects.Comment;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.Objects.UserAccount;
import com.hci.geotagger.cache.CacheHandler;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.NetworkUtils;

public class AdventureHandler 
{
	private static final String TAG = "AdventureHandler";
	
	private JSONParser jsonParser;
	private static String advOpURL = Constants.ADV_URL;
	private Context context;
	private CacheHandler cache;

	public AdventureHandler(Context context)
	{
		this.context = context;
        jsonParser = new JSONParser();
        cache = new CacheHandler(context);
    }
	
	/**
	 * This method will add an Adventure record to the database.  If the network
	 * is NOT available then the operation will be cached.
	 * TODO: Add caching of the operation.
	 * @param a
	 * @return
	 */
	public ReturnInfo addAdventure(Adventure a) {
		ReturnInfo result;
		
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			JSONObject json = addAdventureToServer(a);
			result =  new ReturnInfo(json);
			if (result.success) {
				// Get the Adventure record from the JSON response
				Adventure ra = createAdventureFromJSON(json);
						
				// TODO: add the record to the Cache
				cache.addAdventure(ra);
			}
		} else {
			// TODO: add the add adventure operation to the cache
			// TODO: add the record to the Cache
			
			result = new ReturnInfo(ReturnInfo.FAIL_NONETWORK);
		}
		return result;
	}
	
	/**
	 * Add an Adventure to the server database
	 * INSERT INTO adventures(OwnerID, Name, Description)
	 * if successful, returns:
	 * array('AdventureID'=>$last_id, 'OwnerID'=>$uId, 'Name'=>$name, 'Description'=>$desc);
	 */
	private JSONObject addAdventureToServer(Adventure a)
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
			Log.d(TAG, "JSON Response from PHP: " + json.toString());
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
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			// Building Parameters
			
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_ADV));
		    params.add(new BasicNameValuePair("id", Long.toString(id)));
		      
		    //make webservice call to remove tag from db
		    try
			{
				JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
				Log.d(TAG,"JSON Response from PHP: " + json.toString());

				// Tell the cache to delete the Adventure record
			    cache.deleteAdventure(id);
				return true;
			}
			catch (Exception ex)
			{
				Log.d(TAG, "deleteAdventure: Exception occurred deleting adventure, returning error.");
				return false;
			}
		} else {
			//TODO: Add the delete adventure operation to the cache
			//TODO: Delete the adventure from the cache
		}
		return false;
	}
	
	/**
	 * Return an array list with all the adventures for the given user ID 
	 * The array could come from the cache or from the Server database.
	 * @param id the adventure ID to use
	 * @return
	 */
	public ArrayList<Adventure> getAllAdventuresUserPartOf(int id)
	{
		ArrayList<Adventure> adventures;

		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			adventures = new ArrayList<Adventure>();

			JSONArray adventureData = getAllAdventuresUserPartOfFromServer(id);
			JSONObject obj;
			if (adventureData != null) {
				// loop through each JSON entry in the JSON array (tags encoded as JSON)
				for (int i = 0; i < adventureData.length(); i++) {
					obj = null;
					try {
						obj = adventureData.getJSONObject(i);
					} catch (JSONException e) {
						Log.d(TAG, "Error getting JSON Object from array.");
						e.printStackTrace();
					}
	
					if (obj != null) {
						Adventure a = createAdventureFromJSON(obj); 
						adventures.add(a);

						// TODO: add/update the record in the cache
					}
				}
			}
		} else {
			// If the network was not up then lets check the cache for the records
			adventures = cache.getAllUserAdventures(id);
		}
		return adventures;
	}


	
	/*
	 * Return a JSONArray (array of JSON Objects) containing
	 * all the adventures for the given user ID
	 */
	private JSONArray getAllAdventuresUserPartOfFromServer(int id)
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
				Log.d(TAG, "getAllAdventuresUserPartOff: No Results");
				//parse result into array of jsonobjects and return it
				return null;
			}
			
		}
		catch (Exception ex)
		{
			Log.d(TAG, "getAllAdventuresUserPartOff: Exception occurred getting adventures, returning null.");
			return null;
		}
	}
	
	private Adventure createAdventureFromJSON(JSONObject json)
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
    		Log.d(TAG, "CreateTag from JSONObject failed");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.d(TAG, "Problem parsing timestamp from JSON");
			e.printStackTrace();
		}
    	return null;
	}
	
	/*
	 * Add tags to the Adventure.
	 */
	public boolean addTagToAdventure(long tagId, long advId)
	{
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			// Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("operation", Constants.OP_ADD_TAG2ADV));
	        params.add(new BasicNameValuePair("advId", Long.toString(advId)));
	        params.add(new BasicNameValuePair("tagId", Long.toString(tagId)));
	               
	        //make webservice call to add tag to adventure
			try
			{
				JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
				Log.d(TAG, "JSON Response from PHP: " + json.toString());
				
				// Add to the cache
				cache.addTag2Adventure(tagId, advId);
				return true;
			}
			catch (Exception ex)
			{
				Log.e(TAG, "addTagToAdventure: Exception occurred adding tag, returning null.");
				return false;
			}
		} else {
			// TODO: Cache the add tag to adventure operation
			return false;
		}
	}

	/**
	 * Return an array list with all of the Tags for a given adventure ID.
	 * The array could come from the cache or from the Server database.
	 * @param id the adventure ID to use
	 * @return
	 */
	public ArrayList<Tag> getAllAdventureTags(long id)
	{
		ArrayList<Tag> tags;

		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			tags = new ArrayList<Tag>();

			JSONArray tagData = getAllAdventureTagsFromServer(id);
			JSONObject obj;
			if (tagData != null) {
				// loop through each JSON entry in the JSON array (tags encoded as JSON)
				for (int i = 0; i < tagData.length(); i++) {
					obj = null;
					try {
						obj = tagData.getJSONObject(i);
					} catch (JSONException e) {
						Log.e(TAG, "Error getting JSON Object from array.");
						e.printStackTrace();
					}
	
					if (obj != null) {
						Tag t = TagHandler.createTagFromJSON(obj);
						tags.add(t);

						// TODO: add/update the record in the cache
					}
				}
			}
		} else {
			// If the network was not up then lets check the cache for the records
			tags = cache.getAdventureTags(id);
		}
		return tags;
	}

	/*
	 * Return a JSONArray (array of JSON Objects) containing
	 * all the tags for the given adventure ID.
	 */	
	private JSONArray getAllAdventureTagsFromServer(long id)
	{
		List<NameValuePair> getTagsParams = new ArrayList<NameValuePair>();
		getTagsParams.add(new BasicNameValuePair("operation", Constants.OP_GETTAGS_BYADVID));
		getTagsParams.add(new BasicNameValuePair("id", Long.toString(id)));
		
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
				Log.d(TAG, "getAllAdventureTagsFromServer: No Results");
				//parse result into array of jsonobjects and return it
				return null;
			}
			
		}
		catch (Exception ex)
		{
			Log.e(TAG, "Exception occurred getting tags, returning null.");
			return null;
		}
	}	
	
	//delete a tag from the adventure
	public boolean removeTagFromAdventure(long tagId, long advId)
	{
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			// Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_TAG));
	        params.add(new BasicNameValuePair("advId", Long.toString(advId)));
	        params.add(new BasicNameValuePair("tagId", Long.toString(tagId)));
	        
	        //make webservice call to remove tag from adventure
			try
			{
				JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
				Log.d(TAG, "JSON Response from PHP: " + json.toString());
				return true;
			}
			catch (Exception ex)
			{
				Log.e(TAG, "removeTagFromAdventure: Exception occurred deleting tag, returning error.");
				return false;
			}
		} else {
			// TODO: Add remove tag from adventure operation to cache
			return false;
		}
	}
	
	/*
	 * Add people to the Adventure.
	 */
	public boolean addUserToAdventureById(int uId, long advId)
	{
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			// Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("operation", Constants.OP_ADD_PERSON));
	        params.add(new BasicNameValuePair("advId", Long.toString(advId))); 
	        params.add(new BasicNameValuePair("uId", Integer.toString(uId)));
	                
	        //make webservice call to add tag to adventure
			try
			{
				JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
				Log.d(TAG, "JSON Response from PHP: " + json.toString());
				
				// TODO: Add caching function
				
				return true;
			}
			catch (Exception ex)
			{
				Log.e(TAG, "addUserToaAdventureById: Exception occurred adding people, returning null.");
				return false;
			}
		} else {
			// TODO: add caching function
			return false;
		}
	}

	public ArrayList<UserAccount> getPeopleInAdventure(long aId) {
		ArrayList<UserAccount> uaList = new ArrayList<UserAccount>();

		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			JSONArray uaData = getPeopleInAdventureFromServer(aId);
			JSONObject obj;
			if (uaData != null) {
				// loop through each JSON entry in the JSON array (tags encoded as JSON)
				for (int i = 0; i < uaData.length(); i++) {
					obj = null;
					try {
						obj = uaData.getJSONObject(i);
					} catch (JSONException e) {
						Log.e(TAG, "Error getting JSON Object from array.");
						e.printStackTrace();
					}
	
					if (obj != null) {
						UserAccount u = AccountHandler.createAccountFromJSON(obj);
						uaList.add(u);
						
						// TODO: add/update the record in the cache
					}
				}
			}
		} else {
			// TODO: If the network was not up then lets check the cache for the records
		}
		return uaList;
	}

	/*
	 * Return a JSONArray (array of JSON Objects) containing
	 * all the tags for the given adventure ID.
	 */	
	private JSONArray getPeopleInAdventureFromServer(long aId)
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
				Log.d(TAG, "getPeopleInAdventureFromServer: No Results");
				//parse result into array of jsonobjects and return it
				return null;
			}
			
		}
		catch (Exception ex)
		{
			Log.e(TAG, "Exception occurred getting tags, returning null.");
			return null;
		}
	}	
	
	//delete a person from the adventure
	public boolean removeUserFromAdventure(int uId, long advId)
	{
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			// Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_TAG));
	        params.add(new BasicNameValuePair("advId", Long.toString(advId)));
	        params.add(new BasicNameValuePair("personId", Integer.toString(uId)));
	        
	        //make webservice call to remove tag from adventure
			try
			{
				JSONObject json = jsonParser.getJSONFromUrl(advOpURL, params);
				Log.d(TAG, "JSON Response from PHP: " + json.toString());
				
				// TODO: remove the user from the adventure in the cache
				
				return true;
			}
			catch (Exception ex)
			{
				Log.e(TAG, "removeUserFromAdventure: Exception occurred deleting people, returning error.");
				return false;
			}
		} else {
			// TODO: add cache operation for the action
			return false;
		}
	}	
}
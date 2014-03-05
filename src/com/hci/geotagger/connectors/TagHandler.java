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

import android.content.Context;
import android.util.Log;

import com.hci.geotagger.Objects.Comment;
import com.hci.geotagger.Objects.GeoLocation;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.cache.CacheAction;
import com.hci.geotagger.cache.CacheHandler;
import com.hci.geotagger.common.Constants;

public class TagHandler {

	private static String TAG = "TagHandler";
	private JSONParser jsonParser;
	private static String tagOpURL = Constants.TAGOP_URL;
	private Context context;
	private CacheHandler cache;
	
	/**
	 * Constructor for the TagHandler class.  The TagHandler class requires a context to make
	 * subsequent calls for caching.
	 * @param context current context
	 */
	public TagHandler(Context context)
	{
        jsonParser = new JSONParser();
        this.context = context;
        cache = new CacheHandler(context);
    }
	
	/**
	 * This method will add a Tag to the database.  The input Tag object contains the
	 * necessary fields to be added to the database.
	 * @param t this is the Tag object to add to the database
	 * @return Returns a ReturnInfo object which identifies the success of the Add operation
	 */
	public ReturnInfo AddTag(Tag t)
	{
		ReturnInfo dbresponse;
		Log.d(TAG, "Entering AddTag");
		
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

		// perform cached actions before this action, also returns false if network is down
		if (cache.performCachedActions()) {
			dbresponse = addTagToServer(params);
			if (dbresponse.success) {
				if (dbresponse.object instanceof Tag) {
					// Add the Tag object to the cache
					if (! cache.addTag((Tag)(dbresponse.object)))
						dbresponse = new ReturnInfo(ReturnInfo.FAIL_NOCACHE);
				} else {
					// TODO: if NOT an instance of a Comment then what?
					dbresponse = new ReturnInfo(ReturnInfo.FAIL_GENERAL);
				}
			} else {
				// TODO: if a failure then what?
				dbresponse = new ReturnInfo(ReturnInfo.FAIL_GENERAL);
			}
		} else {
			// Since record was not added we need a Tag ID, so we use the next Cache Tag ID
			t.setId(cache.getnextTagCacheID());
			
			// Add the Tag object to the cache
			if (cache.addTag(t)) {
				dbresponse = new ReturnInfo();
				dbresponse.object = t;
			} else {
				dbresponse = new ReturnInfo(ReturnInfo.FAIL_NONETWORK);
			}
			
			// TODO: Create post commands for the Action cache record.
	        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
	        postParams.add(new BasicNameValuePair(CacheHandler.ACTION_OP_POSTID, CacheHandler.ACTION_UPDATE_POSTOP));
	        postParams.add(new BasicNameValuePair(CacheHandler.ACTION_TAGID_POSTID, Long.toString(t.getId())));
			
			// add the AddTag request to the cached list of DB transactions
			cache.cacheAction(tagOpURL, params, postParams);
		}
		Log.d(TAG, "Leaving AddTag");
		return dbresponse;
	}
	
	/**
	 * Internal method to add a Tag to the server.  This method makes the specific JSON calls
	 * to add the record to the database.
	 * @param params
	 * @return
	 */
	private ReturnInfo addTagToServer(List<NameValuePair> params)
	{
		ReturnInfo result;
		Log.d(TAG, "Entering addTagToServer");

        //make webservice call to add tag comment to db
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(tagOpURL, params);
			Log.d(TAG, "addTagToServer: JSON Response from PHP: " + json.toString());
			result = new ReturnInfo(json);
			if (result.success)
				result.object = createTagFromJSON(json);
		}
		catch (Exception ex)
		{
			Log.d(TAG, "addTagToServer: Exception occurred adding comment, returning null.");
			result = new ReturnInfo(ReturnInfo.FAIL_JSONERROR);
		}
		
		Log.d(TAG, "Leaving addTagToServer");
		return result;
	}

	/**
	 * Return all of the Tags associated with a specific Owner ID.  The Tag objects returned
	 * are contained in the returned ArrayList.
	 * @param oId The Owner ID to use for the request.
	 * @return The ArrayList containing the associated Tags is returned
	 */
	public ArrayList<Tag> getTagsById(int oId) {
		ArrayList<Tag> tags;
		Log.d(TAG, "Entering getTagsById");

		// perform cached actions before this action, also returns false if network is down
		if (cache.performCachedActions()) {
			tags = new ArrayList<Tag>();
			
			JSONArray tagData = getTagsByIdFromServer(oId);
			JSONObject obj;
			if (tagData != null) {
				// loop through each JSON entry in the JSON array (tags encoded as JSON)
				for (int i = 0; i < tagData.length(); i++) {
					obj = null;
					try {
						obj = tagData.getJSONObject(i);
					} catch (JSONException e) {
						Log.d(TAG, "Error getting JSON Object from array.");
						e.printStackTrace();
					}
	
					if (obj != null) {
						Tag t = createTagFromJSON(obj);
						tags.add(t);
						
						// add/update the record in the cache
						cache.addTag(t);
					}
				}
			}
		} else {
			// If the network was not up then lets check the cache for the records
			tags = cache.getTags(oId);
		}
		
		Log.d(TAG, "Leaving getTagsById");
		return tags;
	}

	/**
	 * Static function to create a Tag object from a JSON Object.  All JSON functions
	 * are meant to be contained within the connectors are of the project.
	 * @param json The JSONObject to convert to a Tag object
	 * @return a Tag object is returned
	 */
	public static Tag createTagFromJSON(JSONObject json)
	{
		Tag t = null;
		Log.d(TAG, "Entering createTagFromJSON");

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
//			commenting out because don't really need to pass username but hopefully doesn't break everything
//			//instantiate the tag object with properties from JSON
//			Tag t = new Tag(json.getLong("TagID"), json.getString("Name"), json.getString("Description"), json.getString("ImageUrl"),
//					json.getString("Location"), json.getString("Category"), json.getInt("RatingScore"),
//					json.getInt("OwnerID"), json.getString("Username"), geo, json.getInt("Visibility"), d);
			//instantiate the tag object with properties from JSON
			t = new Tag(json.getLong("TagID"), json.getString("Name"), json.getString("Description"), json.getString("ImageUrl"),
					json.getString("Location"), json.getString("Category"), json.getInt("RatingScore"),
					json.getInt("OwnerID"), geo, json.getInt("Visibility"), d);
		} 
    	catch (JSONException e) 
    	{
    		Log.d("TagHandler", "CreateTag from JSONObject failed");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.d("TagHandler", "Problem parsing timestamp from JSON");
			e.printStackTrace();
		}
    	
		Log.d(TAG, "Leaving createTagFromJSON");
    	return t;
	}
	
	/**
	 * Private method to query the server for the Tags associated Owner ID.  This method returns
	 * a JSONArray (array of JSON Objects) containing all the tags for the given user ID
	 * @param oId The Owner ID to use for the query
	 * @return A JSONArray with the results of the query
	 */
	private JSONArray getTagsByIdFromServer(int oId)
	{
		JSONArray resultsArray = null;
		Log.d(TAG, "Entering getTagsByIdFromServer");

		 // Building Parameters
        List<NameValuePair> getTagsParams = new ArrayList<NameValuePair>();
        getTagsParams.add(new BasicNameValuePair("operation", Constants.OP_GETTAGS_BYID));
        getTagsParams.add(new BasicNameValuePair("oId", Integer.toString(oId)));
        
		try
		{
			 //make webservice call to get tags
			resultsArray = jsonParser.getJSONArrayFromUrl(tagOpURL, getTagsParams);
			if(resultsArray == null)
			{
				Log.d("TagHandler GetTagsByID", "No Results");
			}
		}
		catch (Exception ex)
		{
			Log.d("GetTagsById", "Exception occurred getting tags, returning null.");
		}

		Log.d(TAG, "Leaving getTagsByIdFromServer");
		return resultsArray;
	}

	/**
	 * This method will delete a tag from the database
	 * @param tagId ID of the tag to remove
	 * @return ReturnInfo object returned which identifies operation success
	 */
	public ReturnInfo deleteTag(long tagId)
	{
		ReturnInfo dbResponse;
		Log.d(TAG, "Entering deleteTag");

		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_TAG));
        params.add(new BasicNameValuePair("tId", Long.toString(tagId)));

		// perform cached actions before this action, also returns false if network is down
		if (cache.performCachedActions()) {
	        //make webservice call to remove tag from db
			try
			{
				JSONObject json = jsonParser.getJSONFromUrl(tagOpURL, params);
				Log.d(TAG, "deleteTag: JSON Response from PHP: " + json.toString());
				dbResponse = new ReturnInfo();
				
				// Delete the record from the cache
				cache.deleteTag(tagId);
			}
			catch (Exception ex)
			{
				Log.d(TAG, "deleteTag: Exception occurred deleting tag, returning error.");
				dbResponse = new ReturnInfo(ReturnInfo.FAIL_JSONERROR);
			}
		} else {
			// If the network is down are we going to handle delete operations?
			cache.deleteTag(tagId);
			
			// add the deleteTag request to the cached list of DB transactions
			cache.cacheAction(tagOpURL, params);
			
			// TODO: SHould we return more information??
			dbResponse = new ReturnInfo();
		}
		
		Log.d(TAG, "Leaving deleteTag");
		return dbResponse;
	}

	
	/****************************************************************************************
	 * TAG COMMENT METHODS
	 ****************************************************************************************/	

	/**
	 * Add a Tag Comment to the database.  A ReturnInfo object is returned with result 
	 * information and the Comment object.
	 * @param tagID ID of the Tag to add the comment to
	 * @param text Text of the comment to add
	 * @param username User name of the person adding the comment
	 * @return a ReturnInfo object with operation status and the new Comment object
	 */
	public ReturnInfo addTagComment(Long tagID, String text, String username)
	{
		ReturnInfo dbresponse;
		Log.d(TAG, "Entering addTagComment");
		
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_ADD_TAGCOMMENT));
        params.add(new BasicNameValuePair("tagId", Long.toString(tagID)));
        params.add(new BasicNameValuePair("comment", text ));
        params.add(new BasicNameValuePair("uName", username));

		// perform cached actions before this action, also returns false if network is down
		if (cache.performCachedActions()) {
			dbresponse = addTagCommentToServer(params);
			if (dbresponse.success) {
				if (dbresponse.object instanceof Comment) {
					// Add to the cache
					if (! cache.addTagComment((Comment)(dbresponse.object)))
						dbresponse = new ReturnInfo(ReturnInfo.FAIL_NOCACHE);
				} else {
					// TODO: if NOT an instance of a Comment then what?
					dbresponse = new ReturnInfo(ReturnInfo.FAIL_GENERAL);
				}
			} else {
				// TODO: if a failure then what?
			}
		} else {
			/*
			 *  Create Tag Comment object based on what we know.  Since the record cannot
			 *  be added to the database we will generate a temporary CommentId value.
			 *  TODO: Convert from cached CommentID to the database's CommentID
			 */
			Date d = new Date();
			long cacheID = cache.getnextTagCommentCacheID();
			Comment comment = new Comment(cacheID, tagID, text, username, d);
			
			// Add the record to the cache
			if (cache.addTagComment(comment)) {
				dbresponse = new ReturnInfo();
				dbresponse.object = comment;
			} else {
				dbresponse = new ReturnInfo(ReturnInfo.FAIL_NONETWORK);
			}
			
			// TODO: Create post commands for the Action cache record.
	        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
	        postParams.add(new BasicNameValuePair(CacheHandler.ACTION_OP_POSTID, CacheHandler.ACTION_UPDATE_POSTOP));
	        postParams.add(new BasicNameValuePair(CacheHandler.ACTION_COMMENTID_POSTID, Long.toString(cacheID)));
			
			// TODO: add the AddTag request to the cached list of DB transactions
			cache.cacheAction(tagOpURL, params, postParams);
		}

		Log.d(TAG, "Leaving addTagComment");
		return dbresponse;
	}

	/**
	 * Add a Tag Comment to the database with a picture.  A ReturnInfo object is returned
	 * with result information and the Comment object.
	 * @param tagID ID of the Tag to add the comment to
	 * @param text Text of the comment to add
	 * @param username User name of the person adding the comment
	 * @param imgURL URL of the image to be added to the comment
	 * @return a ReturnInfo object with operation status and the new Comment object
	 */
	public ReturnInfo addTagComment(Long tagID, String text, String username, String imgURL)
	{
		ReturnInfo dbresponse;
		Log.d(TAG, "Entering addTagComment");
		
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_ADD_TAGCOMMENT));
        params.add(new BasicNameValuePair("tagId", Long.toString(tagID)));
        params.add(new BasicNameValuePair("comment", text ));
        params.add(new BasicNameValuePair("uName", username));
        params.add(new BasicNameValuePair("imgUrl", imgURL));
        
		// perform cached actions before this action, also returns false if network is down
		if (cache.performCachedActions()) {
			dbresponse = addTagCommentToServer(params);
			if (dbresponse.success) {
				if (dbresponse.object instanceof Comment) {
					if (! cache.addTagComment((Comment)(dbresponse.object))) {
						Log.d(TAG, "addTagComment: cache error");
//						return new ReturnInfo(ReturnInfo.FAIL_NOCACHE);
					}
				} else {
					// TODO: if NOT an instance of a Comment then what?
					dbresponse = new ReturnInfo(ReturnInfo.FAIL_GENERAL);
				}
			} else {
				// TODO: If the add failed then what?
			}
		} else {
			/*
			 *  Create Tag Comment object based on what we know.  Since the record cannot
			 *  be added to the database we will generate a temporary CommentId value.
			 *  TODO: Convert from cached CommentID to the database's CommentID
			 */
			Date d = new Date();
			long cacheID = cache.getnextTagCommentCacheID();
			Comment comment = new Comment(cacheID, tagID, text, username, d, imgURL);
			
			// Add the record to the cache
			if (cache.addTagComment(comment)) {
				dbresponse = new ReturnInfo();
				dbresponse.object = comment;
			} else {
				dbresponse = new ReturnInfo(ReturnInfo.FAIL_NONETWORK);
			}
			
			// TODO: Create post commands for the Action cache record.
	        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
	        postParams.add(new BasicNameValuePair(CacheHandler.ACTION_OP_POSTID, CacheHandler.ACTION_UPDATE_POSTOP));
	        postParams.add(new BasicNameValuePair(CacheHandler.ACTION_COMMENTID_POSTID, Long.toString(cacheID)));
			
			// add the AddTag request to the cached list of DB transactions
			cache.cacheAction(tagOpURL, params, postParams);
		}

		Log.d(TAG, "Leaving addTagComment");
		return dbresponse;
	}

	private ReturnInfo addTagCommentToServer(List<NameValuePair> params)
	{
		ReturnInfo result = null;
		Log.d(TAG, "Entering addTagCommentToServer");

        //make webservice call to add tag comment to db
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(tagOpURL, params);
			Log.d("TagHandler AddTagComment", "JSON Response from PHP: " + json.toString());
			result = new ReturnInfo(json);
			if (result.success)
				result.object = createCommentFromJSON(json);
		}
		catch (Exception ex)
		{
			Log.d("TagHandler AddTagComment", "Exception occurred adding comment, returning null.");
			result = new ReturnInfo(ReturnInfo.FAIL_JSONERROR);
		}
		
		Log.d(TAG, "Leaving addTagCommentToServer");
		return result;
	}
	
	/**
	 * This method generates a Comment object from the input JSON object.
	 * @param json input JSONObject object
	 * @return Comment object is returned
	 */
	private Comment createCommentFromJSON(JSONObject json)
	{
		Comment c = null;
		Log.d(TAG, "Entering createCommentFromJSON");

		Date d = new Date();
    	try 
    	{
    		//format the date
    		SimpleDateFormat ts = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			d = ts.parse(json.getString("CreatedDateTime"));
			
			//below code keeps tags working until server has imageURL field for comments
			if(!json.getString("ImageUrl").equals(""))
			{
				c = new Comment(json.getLong("ID"),json.getLong("ParentTagID"),
						json.getString("Text"), json.getString("Username"),  d,
						json.getString("ImageUrl"));
			}
			else
			{
				c = new Comment(json.getLong("ID"),json.getLong("ParentTagID"),
						json.getString("Text"), json.getString("Username"),  d);
			}
		} 
    	catch (JSONException e) 
    	{
    		Log.d("TagHandler", "CreateComment from JSONObject failed");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.d("TagHandler", "Create Comment: Problem parsing timestamp from JSON");
			e.printStackTrace();
		}
    	
		Log.d(TAG, "Leaving createCommentFromJSON");
    	return c;
	}

	/**
	 * This method returns an ArrayList of Comment objects, that are associated with the
	 * input tagID value.
	 * @param tagID the Tag ID to use to retrieve the Comment records
	 * @return The ArrayList containing the Comment objects
	 */
	public ArrayList<Comment> getTagComments(long tagID) {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Log.d(TAG, "Entering getTagComments");

		// perform cached actions before this action, also returns false if network is down
		if (cache.performCachedActions()) {
			JSONArray commentData = getTagCommentsFromServer(tagID);
			JSONObject obj;
			if (commentData != null) {
				// loop through each JSON entry in the JSON array (tags encoded as JSON)
				for (int i = 0; i < commentData.length(); i++) {
					obj = null;
					try {
						obj = commentData.getJSONObject(i);
					} catch (JSONException e) {
						Log.d(TAG, "Error getting JSON Object from array.");
						e.printStackTrace();
					}
	
					if (obj != null) {
						Comment c = createCommentFromJSON(obj);
						comments.add(c);
						
						// add/update the record in the cache
						cache.addTagComment(c);
					}
				}
			}
		} else {
			// If the network was not up then lets check the cache for the records
			comments = cache.getTagComments(tagID);
		}

		Log.d(TAG, "Leaving getTagComments");
		return comments;
	}
	
	/**
	 * This method will retrieve all comments related to a specific tag from the server.
	 * The returned value is a JSONArray of Comment records.
	 * @param tagID The Tag ID used to retrieve the Comments
	 * @return The JSONArray object that contains the retrieved Comments
	 */
	private JSONArray getTagCommentsFromServer(long tagID) {
		JSONArray resultsArray = null;
		Log.d(TAG, "Entering getTagCommentsFromServer");

		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_GET_TAGCOMMENTS));
        params.add(new BasicNameValuePair("tagId", Long.toString(tagID)));
        
    	try
		{
			 //make webservice call to get tags
			resultsArray = jsonParser.getJSONArrayFromUrl(tagOpURL, params);
			if(resultsArray != null)
			{
				Log.d("TagHandler GetTagComments", "JSON Response from PHP: " + resultsArray.toString());
				//parse result into array of jsonobjects and return it
			}
			else
			{
				Log.d("TagHandler GetTagComments", "No Results");
				//parse result into array of jsonobjects and return it
			}		
		}
		catch (Exception ex)
		{
			Log.d("GetTagsById", "Exception occurred getting tags, returning null.");
		}
    	
		Log.d(TAG, "Leaving getTagCommentsFromServer");
		return resultsArray;
	}
	
	/**
	 * This method will delete a Tag Comment from the database
	 * @param commentId The ID of the Comment record to delete
	 * @return A ReturnInfo object is returned to identify how the operation proceeded.
	 */
	public ReturnInfo deleteTagComment(long commentId)
	{
		ReturnInfo dbResponse;
		Log.d(TAG, "Entering deleteTagComment");
		
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_TAGCOMMENT));
		params.add(new BasicNameValuePair("cId", Long.toString(commentId)));
        
		// perform cached actions before this action, also returns false if network is down
		if (cache.performCachedActions()) {
			//make webservice call to remove tag from db
			try
			{
				JSONObject json = jsonParser.getJSONFromUrl(tagOpURL, params);
				Log.d(TAG, "deleteTagComment: JSON Response from PHP: " + json.toString());
				dbResponse = new ReturnInfo();
				
				// Delete the record from the cache
				cache.deleteTagComment(commentId);
			}
			catch (Exception ex)
			{
				Log.d(TAG, "deleteTagComment: Exception occurred deleting tag, returning error.");
				dbResponse = new ReturnInfo(ReturnInfo.FAIL_JSONERROR);
			}
		} else {
			// If the network is down are we going to handle delete operations?
			cache.deleteTagComment(commentId);
			
			// add the deleteTag request to the cached list of DB transactions
			cache.cacheAction(tagOpURL, params);
			
			dbResponse = new ReturnInfo();
		}

		Log.d(TAG, "Leaving deleteTagComment");
		return dbResponse;
	}
	
}

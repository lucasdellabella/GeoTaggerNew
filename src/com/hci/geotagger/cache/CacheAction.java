package com.hci.geotagger.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * The CacheAction class is used to maintain information about Action caching.
 * Specifically, information associated with each cached action.
 * @author Paul Cushman
 *
 */
public class CacheAction {
	private static final String TAG = "CacheAction";
	String actionParams;
	String url;
	long id;
	String postActions;

	public final static int UNKNOWN_ID = -1;
	
	/**
	 * CacheAction constructor that creates a CacheAction object using the input
	 * Action parameters NameValuePair List.
	 * @param params The action parameters associated with the action to be cached
	 */
	public CacheAction(List<NameValuePair> params) {
		actionParams = toString(params);
		url = null;
		id = UNKNOWN_ID;
		postActions = null;
	}

	/**
	 * CacheAction constructor that creates a CacheAction object using the input URL
	 * string and the Action parameters NameValuePair List
	 * @param url The URL associated with the action to be cached
	 * @param params The action parameters associated with the action to be cached
	 */
	public CacheAction(String url, List<NameValuePair> params) {
		actionParams = toString(params);
		this.url = url;
		id = UNKNOWN_ID;
		postActions = null;
	}
	
	/**
	 * CacheAction constructor that creates a CacheAction object using the input
	 * string representation of the Action parameters NameValuePair List
	 * @param action A string version of the action parameters associated with the action to be cached
	 */
	public CacheAction(String action) {
		actionParams = action;
		url = null;
		id = UNKNOWN_ID;
		postActions = null;
	}

	/**
	 * CacheAction constructor that creates a CacheAction object using the input URL
	 * string and the string representation of the Action parameters NameValuePair List
	 * @param url The URL associated with the action to be cached
	 * @param action A string version of the action parameters associated with the action to be cached
	 */
	public CacheAction(String url, String action) {
		actionParams = action;
		this.url = url;
		id = UNKNOWN_ID;
		postActions = null;
	}
	
	/**
	 * CacheAction constructor that creates a CacheAction object using the input URL
	 * string and the string representation of the Action parameters NameValuePair List and
	 * the record ID associated with the cache action
	 * @param url The URL associated with the action to be cached
	 * @param action A string version of the action parameters associated with the action to be cached
	 * @param id The record ID associated with the action to be cached
	 */
	public CacheAction(String url, String action, long id) {
		this.url = url;
		actionParams = action;
		this.id = id;
		postActions = null;
	}

	/**
	 * This method will print the parameters associated with the cached action object
	 */
	public void print() {
		Log.d(TAG, "Params="+actionParams);
	}

	/**
	 * This static method will convert the input List<NameValuePair> to a JSONObject
	 * and then convert the JSONObject to a string.  This string can be used to 
	 * save the database requests for caching.
	 * @param param The List<NameValuePair> object to convert
	 * @return The string representation of the input param
	 */
	static public String toString(List<NameValuePair> param) {
		if (param.size() <= 0)
			return null;
		
		JSONObject json;
		
		json = new JSONObject();
		
		for (int i=0; i<param.size(); i++) {
			NameValuePair nvp = param.get(i);
			
			try {
				json.put(nvp.getName(), nvp.getValue());
			} catch (Exception e) {
				Log.e(TAG, "Error during JSON Put");
			}
		}
		
		return json.toString();
	}
	
	/**
	 * This method will return the List<NameValuePair> parameter saved by
	 * this CacheAction object.
	 * @return
	 */
	public List<NameValuePair> getArray() {
		return getArray(actionParams);
	}
	
	/**
	 * This method will return the List<NameValuePair> value for the Post Actions
	 * that are set for this Action Cache.
	 * @return The List<NameValuePair> for the post action
	 */
	public List<NameValuePair> getPostArray() {
		if (postActions == null)
			return null;
		return getArray(postActions);
	}
	
	/**
	 * This static method will convert the input JSON string into the associated
	 * List<NameValuePair> object.  The string should have been formed by a
	 * previous call to the CacheAction.toString call, or by the constructor
	 * method of this class. 
	 * @param jsonString The input JSON string
	 * @return The converted List<NameValuePair> object
	 */
	static public List<NameValuePair> getArray(String jsonString) {
		JSONObject json;
		try {
			json = new JSONObject(jsonString);
		} catch (Exception e) {
			Log.d(TAG, "Error parsing string to JSONObject");
			return null;
		}

		if (json == null)
			return null;
		
		Iterator iterate = json.keys();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		while (iterate.hasNext()) {
			String key = (String)iterate.next();
			try {
				String string = json.getString(key);
		        params.add(new BasicNameValuePair(key, string));
				Log.d(TAG, "key="+key+", string = "+string);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
        return params;
	}
}

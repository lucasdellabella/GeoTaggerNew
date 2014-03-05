/*
 * Account Handler class handles the calls to the webservice that deal
 * with user account operations, such as adding accounts, adding friends,
 * logging in, etc.
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

import com.hci.geotagger.Objects.UserAccount;
import com.hci.geotagger.cache.CacheHandler;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.MyUserAccount;
import com.hci.geotagger.common.NetworkUtils;

import android.content.Context;
import android.util.Log;

 
public class AccountHandler {
    private static final String TAG = "AccountHandler";
    
    private JSONParser jsonParser;
    private Context context;
	private CacheHandler cache;

    // constructor
    public AccountHandler(Context context){
        jsonParser = new JSONParser();
        this.context = context;
        cache = new CacheHandler(context);
    }
 
    /**
     * This method will perform a login operation to the server.  If the network
     * is down then the login operation will be performed against the cache database.
     * @param uName The username used to login
     * @param password The password used to login
     * @return A ReturnInfo structure is used to identify the result of the login
     */
    public ReturnInfo login(String uName, String password) {
    	ReturnInfo retValue;
    	
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			JSONObject response;
			response = loginFromServer(uName, password);
			
			retValue = new ReturnInfo(response);
			if (retValue.success) {
				retValue.object = createMyAccountFromJSON(response);
				cache.addAccount((MyUserAccount)retValue.object);
			}
		} else {
			MyUserAccount mua = cache.loginByName(uName, password);
			if (mua != null) {
				retValue = new ReturnInfo();
				retValue.object = mua;
			} else
				retValue = new ReturnInfo(ReturnInfo.FAIL_NONETWORK);
		}
		return retValue;
    }
    
    /**
     * Attempt to login user with username/pw
     * If successful, returns JSON account object
     * Performs the following query on the server database:
     * SELECT AccountID, Username, EmailAddress, Password, Image, Description, Location, Quote, Type, Visibility, 
     * 		CreatedDateTime, RatingScore FROM accounts WHERE Username='".$username."' AND Password=MD5('".$password."')
     * */
    private JSONObject loginFromServer(String uName, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_LOGIN));
        params.add(new BasicNameValuePair("username", uName));
        params.add(new BasicNameValuePair("password", password));
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
			Log.d(TAG, "JSON Response from PHP: " + json.toString());
			return json;
		}
		catch (Exception ex)
		{
			Log.d(TAG, "Login: Exception occurred during login, returning null.");
			return null;
		}
    }

    /**
     * This method will perform a login operation to the server.  If the network
     * is down then the login operation will be performed against the cache database.
     * @param id The ID associated with this user
     * @param password The password used to login
     * @return A ReturnInfo structure is used to identify the result of the login
     */
    public ReturnInfo login(int id, String password) {
    	ReturnInfo retValue;
    	
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			JSONObject response;
			response = loginFromServer(id, password);
			
			retValue = new ReturnInfo(response);
			if (retValue.success) {
				retValue.object = createMyAccountFromJSON(response);
				cache.addAccount((MyUserAccount)retValue.object);
			}
		} else {
			MyUserAccount mua = cache.loginById(id, password);
			if (mua != null) {
					retValue = new ReturnInfo();
					retValue.object = mua;
			} else
				retValue = new ReturnInfo(ReturnInfo.FAIL_NONETWORK);
		}
		return retValue;
    }
    
    /**
     * Attempt to login user with ID/pw
     * If successful, returns JSON account object
     * Performs following DB request on server:
     * SELECT AccountID, Username, EmailAddress, Password, Image, Description, Location, Quote, Type, Visibility, 
     * 				CreatedDateTime, RatingScore FROM accounts WHERE AccountID=".$id." AND Password='".$password."'"
     * */
    public JSONObject loginFromServer(int id, String password){
    	//Create post params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_LOGIN_ID));
        params.add(new BasicNameValuePair("id", Integer.toString(id)));
        params.add(new BasicNameValuePair("password", password));
        //get json response
        JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
        Log.d(TAG, "loginFromServer: JSON Response from PHP: " + json.toString());
        return json;
    }

    /**
     * Get a UserAccount record based on a username from the server.  If the network is not up
     * then the record will be retrieved from the cache.
     * @param username The user name of the UserAccount
     * @return The UserAccount record retrieved from the database or cache
     */
    public UserAccount getUser(String username) {
    	UserAccount ua;
    	
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			ua = createAccountFromJSON(getUserFromServer(username));
			cache.addAccount(ua);
		} else {
			// TODO: If the network was not up then lets check the cache for the records
			ua = cache.getAccount(username);
		}
		return ua;
    }

    private JSONObject getUserFromServer(String username){
    	//Create post params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_GETUSER));
        params.add(new BasicNameValuePair("username", username));
        //get json response
        JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
        Log.d(TAG, "getUserFromServer: JSON Response from PHP: " + json.toString());
        return json;
    }
    
    //Attempt to register user. If successful, returns JSON Account object
    /**
     * This method will register the input username and password.  The result is
     * returned as a ReturnInfo object.  There is no object returned within the
     * ReturnInfo object.
     * @param name a string that represents the username
     * @param password a string that represents the user's password
     * @return a ReturnInfo object that identifies the status of the operation
     */
    public ReturnInfo registerUser(String name, String password){
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
	        // Create post params
	    	try
	    	{
	    		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    		params.add(new BasicNameValuePair("operation", Constants.OP_REGISTER));
	    		params.add(new BasicNameValuePair("username", name));
	    		params.add(new BasicNameValuePair("password", password));
	    		//get JSON response
	    		JSONObject json = jsonParser.getJSONFromUrl(Constants.REGISTER_URL, params);
	    		
	    		return new ReturnInfo(json);
	    	}
	    	catch (Exception ex)
	    	{
	    		ex.printStackTrace();
	    		return new ReturnInfo(ReturnInfo.FAIL_JSONERROR);
	    	}
		} else {
			//TODO: What to do if network is down???
			return new ReturnInfo(ReturnInfo.FAIL_NONETWORK);
		}
    }
    
    /*
     * Create a user account object from a JSON object
     * @in json: JSONObject
     * @out UserAccount
     */
    private MyUserAccount createMyAccountFromJSON(JSONObject json)
    {
    	Date d = new Date();
    	try 
    	{
    		//get the user object from the json object
    		JSONObject jUser = json;
    		//format the date
    		SimpleDateFormat ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d = ts.parse(jUser.getString("CreatedDateTime"));
			
			String pwd = "";
			 pwd = jUser.getString("Password");
			//instantiate the user account object with properties from JSON
			MyUserAccount ua = new MyUserAccount(jUser.getInt("AccountID"), jUser.getString("Username"), jUser.getString("EmailAddress"),
					pwd, jUser.getString("Image"), jUser.getString("Description"), jUser.getString("Location"),
					jUser.getString("Quote"), jUser.getInt("Type"), jUser.getInt("Visibility"), d , jUser.getInt("RatingScore"));
			return ua;
		} 
    	catch (JSONException e) 
    	{
    		Log.d(TAG, "CreateUserAccount from JSONObject failed");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.d(TAG, "Problem parsing timestamp from JSON");
			e.printStackTrace();
		}
    	return null;
    }
    
    public static UserAccount createAccountFromJSON(JSONObject json)
    {
    	Date d = new Date();
    	try 
    	{
    		//get the user object from the json object
    		JSONObject jUser = json;
    		//format the date
    		SimpleDateFormat ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			d = ts.parse(jUser.getString("CreatedDateTime"));
			
			//instantiate the user account object with properties from JSON
			UserAccount ua = new UserAccount(jUser.getInt("AccountID"), jUser.getString("Username"), jUser.getString("EmailAddress"),
					jUser.getString("Image"), jUser.getString("Description"), jUser.getString("Location"),
					jUser.getString("Quote"), jUser.getInt("Type"), jUser.getInt("Visibility"), d , jUser.getInt("RatingScore"));
			return ua;
		} 
    	catch (JSONException e) 
    	{
    		Log.d(TAG, "CreateUserAccount from JSONObject failed");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.d(TAG, "Problem parsing timestamp from JSON");
			e.printStackTrace();
		}
    	return null;
    }
 
    /**
     * String GetUsernameFromId
     * @param id
     * @return username
     * */
    public String getUsernameFromId(int id)
    {
    	String username = null;
    	
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
            // Create post params
        	try
        	{
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		params.add(new BasicNameValuePair("operation", Constants.OP_GETNAME_FROMID));
        		params.add(new BasicNameValuePair("id", Integer.toString(id)));
        		//get JSON response
        		JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
        		username = json.getString("username");
        		
        		//TODO: add the cached value to the database
        		
        	}
        	catch (Exception ex)
        	{
        		ex.printStackTrace();
        	}
		} else {
			//TODO: Get the cached value
			username = cache.getUsernameFromAccount(id);
		}
		return username;
    }
    
    //Attempt to add friend
    public ReturnInfo addFriend(int userId, String friendName) {
    	ReturnInfo result;

        // Create post params
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("operation", Constants.OP_ADD_FRIEND));
		params.add(new BasicNameValuePair("uId", Integer.toString(userId)));
		params.add(new BasicNameValuePair("fName", friendName));
    	
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			JSONObject json = addFriendToServer(params);
			result = new ReturnInfo(json);
			cache.addFriend(userId, friendName);
		} else {
			// Add the record to the Cache for now
			if (cache.addFriend(userId, friendName)) 
				result = new ReturnInfo();
			else
				result = new ReturnInfo(ReturnInfo.FAIL_NOCACHE);
			
			// add the deleteTag request to the cached list of DB transactions
			cache.cacheAction(Constants.LOGIN_URL, params);
		}
		return result;
    }
    
    private JSONObject addFriendToServer(List<NameValuePair> params) {
    	try
    	{
    		//get JSON response
    		JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
    		return json;
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
    	}
    }

	public ArrayList<UserAccount> getFriends(int friendListOwnerId) {
		ArrayList<UserAccount> friends = new ArrayList<UserAccount>();
		
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			JSONArray friendData = getFriendsFromServer(friendListOwnerId);
			JSONObject obj;
			if (friendData != null) {
				// loop through each JSON entry in the JSON array (tags encoded as JSON)
				for (int i = 0; i < friendData.length(); i++) {
					obj = null;
					try {
						obj = friendData.getJSONObject(i);
					} catch (JSONException e) {
						Log.d(TAG, "Error getting JSON Object from array.");
						e.printStackTrace();
					}
	
					if (obj != null) {
						UserAccount a = createAccountFromJSON(obj);
						friends.add(a);
						
						// TODO: add/update the record in the cache
						cache.addAccount(a);
					}
				}
			}
		} else {
			// TODO: If the network was not up then lets check the cache for the records
		}
		return friends;
	}
	
    //get jsonarray of user account objects
	private JSONArray getFriendsFromServer(int friendListOwnerId) {
		 // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_GETFRIENDS));
        params.add(new BasicNameValuePair("uId", Integer.toString(friendListOwnerId)));
        
		try
		{
			 //make webservice call to get tags
			JSONArray resultsArray = jsonParser.getJSONArrayFromUrl(Constants.LOGIN_URL, params);
			if(resultsArray != null)
			{
				Log.d(TAG, "getFriendsFromServer: JSON Response from PHP: " + resultsArray.toString());
				//parse result into array of jsonobjects and return it
				return resultsArray;
			}
			else
			{
				Log.d(TAG, "getFriendsFromServer: JSON Response from PHP: " + resultsArray.toString());
				return null;
			}
			
		}
		catch (Exception ex)
		{
			Log.d(TAG, "getFriendsFromServer: Exception occurred getting friends, returning null.");
			return null;
		}
	}
	
	//delete a friend association from the db
	public boolean deleteFriend(int uId, int fId)
	{
		boolean retVal = false;
		
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_FRIEND));
			params.add(new BasicNameValuePair("uId", Integer.toString(uId)));
			params.add(new BasicNameValuePair("fId", Integer.toString(fId)));
		       
			//make webservice call to remove friendassociation from db
			try
			{
				JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
				Log.d(TAG, "getFriendsFromServer: JSON Response from PHP: " + json.toString());
				retVal = true;
				
				//TODO: remove the entry in the cache
				cache.deleteFriend(uId, fId);
			}
			catch (Exception ex)
			{
				Log.d(TAG, "getFriendsFromServer: Exception occurred removing friend, returning error.");
			}
		} else {
			// TODO: cache the database operation to be done later when the database is accessible
		}
		return retVal;
	}
		
	/**
	 *  Edit a user profile
	 * @param uId
	 * @param imgUrl
	 * @param description
	 * @param location
	 * @param quote
	 * @return
	 */
	public boolean editProfile(int uId, String imgUrl, String description, String location, String quote) {
		boolean retValue = false;
		
		// If the network is up then try to get the record from the Server DB
		if (NetworkUtils.isNetworkUp(context)) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("operation", Constants.OP_EDIT_PROFILE));
			params.add(new BasicNameValuePair("uId", Integer.toString(uId)));
			params.add(new BasicNameValuePair("imgUrl", imgUrl));
			params.add(new BasicNameValuePair("description", description));
			params.add(new BasicNameValuePair("location", location));
			params.add(new BasicNameValuePair("quote", quote));
	
			// make webservice call to remove friendassociation from db
			try {
				JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
				Log.d(TAG, "editProfile: JSON Response from PHP: " + json.toString());
				
				//TODO: Update the user profile
				UserAccount ua = createAccountFromJSON(json);
				if (ua != null)
					cache.addAccount(ua);
				retValue = true;
			} catch (Exception ex) {
				Log.d(TAG, "editProfile: failed. Returning false");
			}
		} else {
			// TODO: cache the database operation to be done later when the database is accessible
			
			// TODO: Modify the cache database record
		}
		return retValue;
	}
    
}

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
import com.hci.geotagger.common.Constants;
 
import android.util.Log;

 
public class AccountHandler {
 
    private JSONParser jsonParser;
 
    // constructor
    public AccountHandler(){
        jsonParser = new JSONParser();
    }
 
    /**
     * Attempt to login user with username/pw
     * If successful, returns JSON account object
     * */
    public JSONObject login(String uName, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_LOGIN));
        params.add(new BasicNameValuePair("username", uName));
        params.add(new BasicNameValuePair("password", password));
		try
		{
			JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return json;
		}
		catch (Exception ex)
		{
			Log.d("AccountHandler Login", "Exception occurred during login, returning null.");
			return null;
		}
    }

    /**
     * Attempt to login user with ID/pw
     * If successful, returns JSON account object
     * */
    public JSONObject login(int id, String password){
    	//Create post params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_LOGIN_ID));
        params.add(new BasicNameValuePair("id", Integer.toString(id)));
        params.add(new BasicNameValuePair("password", password));
        //get json response
        JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
        Log.d("AccountHandler Login", "JSON Response from PHP: " + json.toString());
        return json;
    }

    public JSONObject getUser(String username){
    	//Create post params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_GETUSER));
        params.add(new BasicNameValuePair("username", username));
        //get json response
        JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
        Log.d("AccountHandler Login", "JSON Response from PHP: " + json.toString());
        return json;
    }
    //Attempt to register user. If successful, returns JSON Account object
    public JSONObject registerUser(String name, String password){
        // Create post params
    	try
    	{
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("operation", Constants.OP_REGISTER));
    		params.add(new BasicNameValuePair("username", name));
    		params.add(new BasicNameValuePair("password", password));
    		//get JSON response
    		JSONObject json = jsonParser.getJSONFromUrl(Constants.REGISTER_URL, params);
    		return json;
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		return null;
    	}
    }
    
    /*
     * Create a user account object from a JSON object
     * @in json: JSONObject
     * @out UserAccount
     */
    public UserAccount CreateAccountFromJSON(JSONObject json)
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
					jUser.getString("Password"), jUser.getString("Image"), jUser.getString("Description"), jUser.getString("Location"),
					jUser.getString("Quote"), jUser.getInt("Type"), jUser.getInt("Visibility"), d , jUser.getInt("RatingScore"));
			return ua;
		} 
    	catch (JSONException e) 
    	{
    		Log.d("AccountHandler", "CreateUserAccount from JSONObject failed");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.d("AccountHandler", "Problem parsing timestamp from JSON");
			e.printStackTrace();
		}
    	return null;
    }
 
    /**
     * String GetUsernameFromId
     * @param id
     * @return username
     * */
    public String GetUsernameFromId(int id)
    {
            // Create post params
        	try
        	{
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		params.add(new BasicNameValuePair("operation", Constants.OP_GETNAME_FROMID));
        		params.add(new BasicNameValuePair("id", Integer.toString(id)));
        		//get JSON response
        		JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
        		String username = json.getString("username");
        		return username;
        	}
        	catch (Exception ex)
        	{
        		ex.printStackTrace();
        		return null;
        	}
    }
    
    //Attempt to add friend
    public JSONObject AddFriend(int userId, String friendName){
        // Create post params
    	try
    	{
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		params.add(new BasicNameValuePair("operation", Constants.OP_ADD_FRIEND));
    		params.add(new BasicNameValuePair("uId", Integer.toString(userId)));
    		params.add(new BasicNameValuePair("fName", friendName));
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

    //get jsonarray of user account objects
	public JSONArray getFriends(int friendListOwnerId) {
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
				Log.d("AccountHandlerGetFriends", "JSON Response from PHP: " + resultsArray.toString());
				//parse result into array of jsonobjects and return it
				return resultsArray;
			}
			else
			{
				Log.d("AccountHandler GetFriends", "JSON Response from PHP: " + resultsArray.toString());
				return null;
			}
			
		}
		catch (Exception ex)
		{
			Log.d("AddTag GetFriends", "Exception occurred getting friends, returning null.");
			return null;
		}
	}
	
	//delete a friend association from the db
		public boolean deleteFriend(int uId, int fId)
		{
			// Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("operation", Constants.OP_DELETE_FRIEND));
	        params.add(new BasicNameValuePair("uId", Integer.toString(uId)));
	        params.add(new BasicNameValuePair("fId", Integer.toString(fId)));
	        
	        //make webservice call to remove friendassociation from db
			try
			{
				JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL, params);
				System.out.println("JSON Response from PHP: " + json.toString());
				return true;
			}
			catch (Exception ex)
			{
				Log.d("AccountHandler DeleteFriend", "Exception occurred removing friend, returning error.");
				return false;
			}
		}
		
	// delete a friend association from the db
	public boolean editProfile(int uId, String imgUrl, String description, String location, String quote) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("operation",
				Constants.OP_EDIT_PROFILE));
		params.add(new BasicNameValuePair("uId", Integer.toString(uId)));
		params.add(new BasicNameValuePair("imgUrl", imgUrl));
		params.add(new BasicNameValuePair("description", description));
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("quote", quote));

		// make webservice call to remove friendassociation from db
		try {
			JSONObject json = jsonParser.getJSONFromUrl(Constants.LOGIN_URL,
					params);
			System.out.println("JSON Response from PHP: " + json.toString());
			return true;
		} catch (Exception ex) {
			Log.d("AccountHandler editProfile",
					"EditProfile failed. Returning false");
			return false;
		}
	}
    
}

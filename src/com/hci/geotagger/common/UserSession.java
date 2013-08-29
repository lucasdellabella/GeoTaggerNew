package com.hci.geotagger.common;

import java.util.Map;

import com.hci.geotagger.Objects.GeoLocation;
import com.hci.geotagger.Objects.UserAccount;

import android.content.SharedPreferences;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
/*
 * UserSession class keeps track of the current session. It 
 * knows if a user is logged in, and also caches the user account
 * object of the logged in user. 
 * 
 * Chris Loeschorn
 * Spring 2013
 */
public final class UserSession {

	
	public static boolean LOGGED_IN = false;
	public static UserAccount CURRENT_USER;
	public static int CURRENTUSER_ID;
	/*
	 * Set LoggedIn flag to true, cache current user account object
	 * and set shared preferences so the login is saved even if
	 * the application is killed or crashes
	 */
	public static void login(Context c, UserAccount account)
	{
		LOGGED_IN = true;
		CURRENT_USER = account;
		CURRENTUSER_ID = account.getId();
		
		//set shared preferences (private)
		SharedPreferences app_settings = c.getApplicationContext().getSharedPreferences(Constants.LOGIN_DATAFILE, Constants.MODE_PRIVATE);
		SharedPreferences.Editor editor = app_settings.edit();
		editor.putBoolean(Constants.KEY_LOGGEDIN, true);
		editor.putInt(Constants.KEY_UID, account.getId());
		editor.putString(Constants.KEY_PASS, account.getPass());
		editor.commit();
		Log.d("UserSession Login", "Shared Preferences Set.");
	}
	/*
	 * Logout method clears static fields, sets LoggedIn to false, and clears shared preferences
	 */
	public static void logout(Context c)
	{
		LOGGED_IN = false;
		CURRENT_USER = null;
		CURRENTUSER_ID = -1;
		SharedPreferences app_settings = c.getApplicationContext().getSharedPreferences(Constants.LOGIN_DATAFILE, Constants.MODE_PRIVATE);
		app_settings.edit().clear().commit();
		
		Map<String,?> keys = app_settings.getAll();

		for(Map.Entry<String,?> entry : keys.entrySet()){
		            Log.d("map values",entry.getKey() + ": " + 
		                                   entry.getValue().toString());            
		 }
	}
	
}

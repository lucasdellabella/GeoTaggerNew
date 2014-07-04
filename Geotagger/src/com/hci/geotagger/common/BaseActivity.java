/**
 * Class BaseActivity is used to create an options menu that will be used in all 
 * other activities throughout the application. Any activity that wants to use
 * this options menu should extend BaseActivity instead of Activity. 
 */
package com.hci.geotagger.common;

import com.hci.geotagger.activities.HomeActivity;
import android.view.WindowManager;
import com.hci.geotagger.activities.LoginActivity;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

public class BaseActivity extends Activity
{
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//create logout menu item with ID = 1
		menu.add(1, 1, 1, "Logout");
		menu.add(1,2,2,"Home");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case 1: 
			//log out the user, then open the login screen
			UserSession.logout(this);
			
			Intent i = new Intent(getBaseContext(), LoginActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
						Intent.FLAG_ACTIVITY_CLEAR_TASK |
						Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			finish();
			return true;
		case 2:
			Intent homeIntent = new Intent(getBaseContext(), HomeActivity.class); 
			startActivity(homeIntent);
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
	/*
	public static boolean isTablet(Context context)
	{
		Boolean isTablet = false;
		
		DisplayMetrics metrics = new DisplayMetrics();
	    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		if (width > 1023 || height > 1023)
		{
			//tablet
			isTablet = true;
			Log.d("BaseActivity", "This is a tablet device");
			
		}else
		{
			//smartphone
			Log.d("BaseActivity", "This is a smartphone device");

		}
		return isTablet;
	}
	*/
	public static boolean isTablet(Context context) 
	{
		Boolean isTablet =  (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	            
	            if(isTablet)
	            {
	            	Log.d("BaseActivity", "Is Tablet Device");
	            }
	            else
	            {
	            	Log.d("BaseActivity", "Is Phone Device");

	            }
	            
	            return isTablet;
	}
}

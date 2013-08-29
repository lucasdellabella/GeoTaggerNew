/*
 * Class BaseActivity is used to create an options menu that will be used in all 
 * other activities throughout the application. Any activity that wants to use
 * this options menu should extend BaseActivity instead of Activity. 
 */
package com.hci.geotagger.common;

import com.hci.geotagger.activities.HomeActivity;
import com.hci.geotagger.activities.LoginActivity;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

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
}

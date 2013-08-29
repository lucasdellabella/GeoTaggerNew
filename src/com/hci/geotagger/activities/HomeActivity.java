package com.hci.geotagger.activities;

import com.hci.geotagger.R;
import com.hci.geotagger.common.UserSession;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends Activity {
	TextView homeLabel;
	Button btnAdd, btnMyTags, btnFriends, btnProfile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		homeLabel = (TextView) findViewById(R.id.home_homeLbl);
		homeLabel.setText(homeLabel.getText() + " " + UserSession.CURRENT_USER.getuName());
		
		btnAdd = (Button) findViewById(R.id.home_btnAddTag);
		btnMyTags = (Button) findViewById(R.id.home_btnMyTags);
		btnFriends = (Button) findViewById(R.id.home_btnFriends);
		btnProfile = (Button) findViewById(R.id.home_btnProfile);
		
		//go to add tags menu when add button is clicked
		btnAdd.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view0) 
			{
				// create link to add tag
				Intent i = new Intent(getBaseContext(), AddTagActivity.class);
				startActivity(i);
			}
		});
		
		btnMyTags.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view0) 
			{
				// create link to my tags
				Intent i = new Intent(getBaseContext(), TagListActivity.class);
				//pass the ID of the current user to ViewTag activity to load their tags 
				i.putExtra("id", UserSession.CURRENTUSER_ID);
				startActivity(i);
			}
		});
		
		//go to friends screen when friends button is clicked
		btnFriends.setOnClickListener(new OnClickListener() {
			public void onClick(View view0) {
				// create link to friends screen
				Intent i = new Intent(getBaseContext(), FriendListActivity.class);
				//pass the ID of the current user to Friends activity to load their friends 
				i.putExtra("id", UserSession.CURRENTUSER_ID);
				startActivity(i);
			}
		});
		
		// go to add tags menu when add button is clicked
		btnProfile.setOnClickListener(new OnClickListener() {
			public void onClick(View view0) {
				// open current user's profile
				Intent i = new Intent(getBaseContext(), UserProfileActivity.class);
				//tell the profile to open the current user's profile
				i.putExtra("LoggedInUser", true);
				startActivity(i);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//create logout menu item with ID = 1
		menu.add(1, 1, 1, "Logout");
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
		}
		
		return super.onOptionsItemSelected(item);
	}

}

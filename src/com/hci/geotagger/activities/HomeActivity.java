package com.hci.geotagger.activities;

<<<<<<< HEAD
import com.actionbarsherlock.app.SherlockActivity;
import com.hci.geotagger.R;
=======
>>>>>>> FETCH_HEAD
import com.hci.geotagger.cache.CacheHandler;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends SherlockActivity {
	TextView homeLabel;
	Button btnGroups, btnMyTags, btnFriends, btnMyProfile, btnSignOut1, btnSignOut2, btnAdventures, btnAddTag, btnComments;
	private Typeface titleFont; //font used for the title of the activity
	private Typeface font; //font used for everything but title in the activity
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		titleFont = Typeface.createFromAsset(getAssets(), "steelfish rg.ttf");
		font = Typeface.createFromAsset(getAssets(), "Gravity-Book.ttf");
		
		if (!Constants.LIMIT_HOME_TO_ADVENTURE){
			setContentView(R.layout.activity_home);
			btnAddTag = (Button) findViewById(R.id.home_btnAddTag);		
			btnMyTags = (Button) findViewById(R.id.home_btnMyTags);
			btnFriends = (Button) findViewById(R.id.home_btnFriends);

			//go to add tags menu when add button is clicked
			btnAddTag.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View view0) 
				{
					// create link to add tag
					Intent i = new Intent(getBaseContext(), AddTagActivity.class);
					startActivity(i);
				}
			});
			
			//go to my tags screen when my tags button is clicked
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
		}
		else
		{	setContentView(R.layout.activity_home_limited_adventures);
			btnSignOut2 = (Button) findViewById(R.id.home_btnSignOut);
			btnSignOut2.setTypeface(font);
			btnSignOut2.setOnClickListener(new OnClickListener()
			{	@Override
				public void onClick(View arg0) {
					logoutSession();
				}
			});
		}

		homeLabel = (TextView) findViewById(R.id.home_homeLbl);
		btnMyProfile = (Button) findViewById(R.id.home_btnMyProfile);		
		btnAdventures = (Button) findViewById(R.id.home_btnAdventures);	
		btnAddTag = (Button) findViewById(R.id.home_btnAddTag);	
		btnComments = (Button) findViewById(R.id.home_btnComments);
		
		homeLabel.setTypeface(titleFont);
		btnMyProfile.setTypeface(font);
		btnAdventures.setTypeface(font);
		btnAddTag.setTypeface(font);
		btnComments.setTypeface(font);
		
		String uname = UserSession.CURRENT_USER.getuName();
		if (uname.length() > 0)
		{	String homeLabelStr = "Welcome " + uname.substring(0, 1).toUpperCase() + uname.substring(1); 
			homeLabel.setText(homeLabelStr);
		}
		

		
		// go to profile screen when my profile button is clicked
		btnMyProfile.setOnClickListener(new OnClickListener() {
			public void onClick(View view0) {
				// open current user's profile
				Intent i = new Intent(getBaseContext(), UserProfileActivity.class);
				//tell the profile to open the current user's profile
				i.putExtra("LoggedInUser", true);
				startActivity(i);
			}
		});
		
		// go to adventures screen when adventures button is clicked
		btnAdventures.setOnClickListener(new OnClickListener() {
			public void onClick(View view0) {
				// open current user's profile
				Intent i = new Intent(getBaseContext(), AdventureListActivity.class);
				//tell the profile to open the current user's adventure list
				i.putExtra("id", UserSession.CURRENTUSER_ID);
				startActivity(i);
			}
		});
		
		btnAddTag.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view0) 
			{
				// create link to add tag
				Intent i = new Intent(getBaseContext(), AddTagActivity.class);
				startActivity(i);
			}
		});
		

		btnComments.setOnClickListener(new OnClickListener() {
			public void onClick(View view0) {
				// open the comments/suggestions webpage
				String strURL = "https://docs.google.com/forms/d/1CRSXqdzy3C98JgCbXEwHLj374sTHVnTMiA04blExXp8/viewform";
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(strURL));
				startActivity(i);
			}
		});	
		
		// Perform a cache garbage collection
		CacheHandler ch = new CacheHandler(this);
		ch.cleanup();
	}
	/*
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
			logoutSession();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}*/
	
	private void logoutSession()
	{
		//log out the user, then open the login screen
		UserSession.logout(this);
		
		Intent i = new Intent(getBaseContext(), LoginActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
					Intent.FLAG_ACTIVITY_CLEAR_TASK |
					Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}

}

package com.hci.geotagger.activities;

import org.json.JSONArray;
import org.json.JSONException;

import com.hci.geotagger.R;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AdventureHandler;
import com.hci.geotagger.Objects.Adventure;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TabHost;
import android.app.TabActivity;

public class AdventureViewActivity extends TabActivity 
{	
	private Button edit;
	private TextView name, description;
	private AdventureHandler advHandler;
	private Adventure adventure;
	private TabHost tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//hide title bar
		setContentView(R.layout.activity_adventure_view);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		adventure = (Adventure) bundle.getSerializable("adventure");
		
		//Set up tab view
		tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent in;
		//Tags tab
		in = new Intent(this, AdvViewTagTabActivity.class);
		Bundle bundle2 = new Bundle();
		bundle2.putSerializable("adventure", adventure);
		in.putExtras(bundle2);				
		spec = tabHost.newTabSpec("tags").setIndicator("Tags").setContent(in);
		tabHost.addTab(spec);
		//People tab
		in = new Intent(this, AdvViewPeopleTabActivity.class);
		Bundle bundle3 = new Bundle();
		bundle3.putSerializable("adventure", adventure);
		in.putExtras(bundle3);
		spec = tabHost.newTabSpec("people").setIndicator("People").setContent(in);
		tabHost.addTab(spec);				
		
		name = (TextView) findViewById(R.id.adventureView_nametxt);
		name.setText(adventure.getName());
		description = (TextView) findViewById(R.id.adventureView_desc);
		description.setText(adventure.getDescription());		
		
		edit = (Button) findViewById(R.id.adventureView_edit);
		edit.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent i = new Intent(v.getContext(), EditAdventureActivity.class);
				Bundle bundle1 = new Bundle();
				bundle1.putSerializable("adventure", adventure);
				i.putExtras(bundle1);				
				startActivity(i);
			}
		});

		if(UserSession.CURRENTUSER_ID == adventure.getCreatorID() && Constants.ADVENTURE_EDITABLE)
		{	//Button is visible
			edit.setVisibility(Button.VISIBLE);
		}
		else
		{	//Button is invisible
			edit.setVisibility(Button.INVISIBLE);
		}
	}//end onCreate	
	
	// add logout to options menu since this class can't inherit it from
	// BaseActivity
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
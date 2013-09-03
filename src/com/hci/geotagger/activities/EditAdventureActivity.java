package com.hci.geotagger.activities;

import java.util.Date;
import java.util.ArrayList;

import com.hci.geotagger.R;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AdventureHandler;
import com.hci.geotagger.Objects.Adventure;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.Objects.UserAccount;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.Toast;
import android.app.TabActivity;

public class EditAdventureActivity extends TabActivity 
{	
	private Button save, cancel;	
	private EditText nameE, descriptionE;
	private AdventureHandler advHandler;
	private Adventure adventure;	
	private TabHost tabHost;
	private Adventure newAdventure;
	private boolean isNewAdv = false;
	private Context context = EditAdventureActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//hide title bar
		setContentView(R.layout.activity_edit_adventure);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();		
		adventure = (Adventure) bundle.getSerializable("adventure");		
		if(intent.getBooleanExtra("newAdventure", false) == true)
		{			
			newAdventure = new Adventure(Constants.VISIBILITY_FULL, UserSession.CURRENTUSER_ID, null, null,
											UserSession.CURRENT_USER.getName(), new Date());
			//advHandler = new AdventureHandler();
			//advHandler.AddAdventure(newAdventure);
			adventure = newAdventure;
			isNewAdv = true;
		}		
		
		//Set up tab view
		tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent in;
		//Tags tab
		in = new Intent(this, AdvEditTagTabActivity.class);
		Bundle bundle2 = new Bundle();
		bundle2.putSerializable("adventure", adventure);
		in.putExtras(bundle2);
		spec = tabHost.newTabSpec("tags").setIndicator("Tags").setContent(in);
		tabHost.addTab(spec);
		//People tab
		in = new Intent(this, AdvEditPeopleTabActivity.class);
		Bundle bundle3 = new Bundle();
		bundle3.putSerializable("adventure", adventure);
		in.putExtras(bundle3);
		spec = tabHost.newTabSpec("people").setIndicator("People").setContent(in);
		tabHost.addTab(spec);			
		
		nameE = (EditText)findViewById(R.id.adventureEdit_name);
		descriptionE = (EditText) findViewById(R.id.adventureEdit_desc);
		if(isNewAdv == false)
		{			
			nameE.setText(adventure.getName());				
			descriptionE.setText(adventure.getDescription());
		}
				
		cancel = (Button)findViewById(R.id.adventureEdit_btnCancel);
		cancel.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				if(isNewAdv == false)
				{
					//advHandler.deleteAdventure(adventure.getID());
				}
				startActivity(new Intent(v.getContext(), AdventureListActivity.class));
			}
		});
		save = (Button) findViewById(R.id.adventureEdit_btnOk);
		save.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				if(nameE.getText().equals(null) || descriptionE.getText().equals(null))
				{
					Toast t = Toast.makeText(context, "Adventure needs name and description!", Toast.LENGTH_SHORT);
					t.show();
				}
				else
				{
					adventure.setName(nameE.getText().toString());
					adventure.setDescription(descriptionE.getText().toString());
					doStoreAddTagList();
					doStoreRemoveTagList();
					doStoreAddUserList();
					doStoreRemoveUserList();				
					startActivity(new Intent(v.getContext(), AdventureListActivity.class));
				}
			}
		});			
	}//end onCreate	
	
	/**
	 * These methods loop through the adventure's store lists for adding/removing tags and users and actually performs 
	 * the add and remove calls.	
	 */
	private void doStoreAddTagList()
	{
		ArrayList<Tag> tempList = adventure.getStoreAddTagList();
		for(int i = 0; i < tempList.size(); i++)
		{
			Tag t = tempList.get(i);
			adventure.addTag(t);
			//advHandler.addTagToAdventure(t.getId(), adventure.getID());
			adventure.emptyStoreTagList(tempList);
		}
	}
	
	private void doStoreRemoveTagList()
	{
		ArrayList<Tag> tempList = adventure.getStoreRemoveTagList();
		for(int i = 0; i < tempList.size(); i++)
		{
			Tag t = tempList.get(i);
			adventure.removeTag(t.getId());
			//advHandler.removeTagFromAdventure(t.getId(), adventure.getID());
			adventure.emptyStoreTagList(tempList);
		}
	}
	
	private void doStoreAddUserList()
	{
		ArrayList<UserAccount> tempList = adventure.getStoreAddUserList();
		for(int i = 0; i < tempList.size(); i++)
		{
			UserAccount u = tempList.get(i);
			adventure.addPerson(u);
			//advHandler.addUserToAdventureById(u.getId(), adventure.getID());
			adventure.emptyStoreUserList(tempList);
		}
	}
	
	private void doStoreRemoveUserList()
	{
		ArrayList<UserAccount> tempList = adventure.getStoreRemoveUserList();
		for(int i = 0; i < tempList.size(); i++)
		{
			UserAccount u = tempList.get(i);
			adventure.removePerson(u.getId());
			//advHandler.removeUserFromAdventure(u.getId(), adventure.getID());
			adventure.emptyStoreUserList(tempList);
		}
	}
	
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
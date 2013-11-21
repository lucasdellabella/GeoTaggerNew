package com.hci.geotagger.activities;

import java.util.Date;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hci.geotagger.R;
import com.hci.geotagger.activities.AddTagActivity.AddTagTask;
import com.hci.geotagger.common.AlertHandler;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AdventureHandler;
import com.hci.geotagger.connectors.TagHandler;
import com.hci.geotagger.Objects.Adventure;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.exceptions.UnknownErrorException;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.Objects.UserAccount;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.app.TabActivity;

public class EditAdventureActivity extends TabActivity 
{	
	private Button save, cancel;	
	private EditText nameE, descriptionE;
	private AdventureHandler advHandler;
	private Adventure adventure;	
	private TabHost tabHost;	
	private boolean isNewAdv = false;
	private Context context = EditAdventureActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//hide title bar
		setContentView(R.layout.activity_edit_adventure);
		
		advHandler = new AdventureHandler();
		
		Intent intent = getIntent();				
		if(intent.getBooleanExtra("newAdventure", false) == true)
		{			
			adventure = new Adventure(Constants.VISIBILITY_FULL, UserSession.CURRENTUSER_ID, null, null,
										/*UserSession.CURRENT_USER.getName(),*/ new Date());			
			isNewAdv = true;
		}
		else
		{
			Bundle bundle = intent.getExtras();		
			adventure = (Adventure) bundle.getSerializable("adventure");
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
				Intent i = new Intent(getBaseContext(), AdventureListActivity.class);
				i.putExtra("id", UserSession.CURRENTUSER_ID);
				startActivity(i);
			}
		});
		save = (Button) findViewById(R.id.adventureEdit_btnOk);
		save.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{				
				if(nameE.getText().toString().isEmpty() || descriptionE.getText().toString().isEmpty())
				{
					Toast t = Toast.makeText(context, "Adventure needs name and description!", Toast.LENGTH_SHORT);
					t.show();
					
				
					
					
				}
				else
				{
					adventure.setName(nameE.getText().toString());
					adventure.setDescription(descriptionE.getText().toString());
					if(isNewAdv == true)
					{
						new AddAdvTask(context).execute(adventure);
					}
					doStoreAddTagList();
					doStoreRemoveTagList();
					doStoreAddUserList();
					doStoreRemoveUserList();
					
					
					//startActivity(new Intent(v.getContext(), AdventureListActivity.class));		
					
					
					
					// create link to my tags
					//Intent i = new Intent(getBaseContext(), TagListActivity.class);
					//pass the ID of the current user to ViewTag activity to load their tags 
					Intent i = new Intent(getBaseContext(), AdventureListActivity.class);
					i.putExtra("id", UserSession.CURRENTUSER_ID);
					startActivity(i);
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
			advHandler.addTagToAdventure(t.getId(), adventure.getId());
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
			advHandler.removeTagFromAdventure(t.getId(), adventure.getId());
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
			advHandler.addUserToAdventureById(u.getId(), adventure.getId());
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
			advHandler.removeUserFromAdventure(u.getId(), adventure.getId());
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
	
	/*
	 * This class extends AsyncTask and provides the methods to add an adventure via an
	 * asynchronous task
	 */
	class AddAdvTask extends AsyncTask<Adventure, Void, JSONObject> 
	{
		ProgressDialog progressDialog;
		Context c;
		//get context from the parent activity for opening dialogs
		public AddAdvTask(Context context)
		{
			this.c = context;		
		}
		
		//Setup progress dialog before execution
		@Override
		public void onPreExecute() 
		{
			progressDialog = new ProgressDialog(c);
			progressDialog.setMessage("Creating adventure...");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}
		
		/*
		 * After login task is finished, get response and
		 * determine if the login was successful. If so, close dialog 
		 * and move to next activity, if not show error. 
		 */
		@Override
		protected void onPostExecute(JSONObject response) 
		{	
			if(response != null)
			{
				try
				{
					String returnCode = response.getString(Constants.SUCCESS);
					//if success = 1, create a user account object from the JSON returned from the database,
					//set the loggedin flag to true, and open the adventure list page
					if (returnCode != null)
					{
						if (Integer.parseInt(returnCode) == 1)
						{
								String msg = "Adventure added!";
								Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
								// return to adventure list screen
								Intent i = new Intent(getBaseContext(), AdventureListActivity.class);
								//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);
								progressDialog.dismiss();
								finish();
						}
						else
						{
							progressDialog.dismiss();
							throw new UnknownErrorException();
						}
					}
					else
					{	
						progressDialog.dismiss();
						Log.d("AddAdvPostExecute", "Null response, Logon Error.");
						throw new UnknownErrorException();
					}
				}
				catch (UnknownErrorException ex)
				{	
					//parser error
					AlertHandler alert = new AlertHandler();
					alert.showAlert(c, null, ex.getMessage());
					Log.d("RegisterPostExecute", "Parsing returned JSON object failed.");
					ex.printStackTrace();
				} 
				catch (JSONException e) 
				{
					progressDialog.dismiss();
					AlertHandler alert = new AlertHandler();
					alert.showAlert(c, null, getString(R.string.unknown_error));
					e.printStackTrace();
				}	
				
			}		
		}//end onPostExecute
		
		/*
		 * Create an account handler and attempt to log in with 
		 * the provided credentials. 
		 */
		@Override
		protected JSONObject doInBackground(Adventure...adventure) 
		{
			Adventure a = adventure[0];
			
			// attempt to add tag
			AdventureHandler handler = new AdventureHandler();
			JSONObject response;
			try 
			{				
				//add adventure to db
				response = handler.addAdventure(a);
				Log.d("AddAdvTask", "Got response, returncode = " + response.getString(Constants.SUCCESS));
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
				return null;
			}
			return response;

		}// end doInBackground
	}//end LoginTask
}
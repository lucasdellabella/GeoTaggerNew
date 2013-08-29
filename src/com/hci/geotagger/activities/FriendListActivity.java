package com.hci.geotagger.activities;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hci.geotagger.R;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.Objects.UserAccount;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AccountHandler;
import com.hci.geotagger.connectors.ImageHandler;
import com.hci.geotagger.connectors.TagHandler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FriendListActivity extends ListActivity {
	private ImageButton btnAddFriend;
	private ProgressDialog PD = null;
	private TextView nameTxt;
	
	private AccountHandler accountHandler;
	private Runnable addFriend, loadFriends, loadSingleFriend;
	private final Context c = this;
	
	private ArrayList<UserAccount> friends = null;
	private FriendAdapter FA;
	
	private String friendAdded = ""; //callback field used to pass data between threads when adding friend
	private UserAccount acctToAdd = null;
	private int friendListOwnerId; //id of the user account whose friends list is being shown

	/*
	 * Event Handlers
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		
		//initialize objects
		friends = new ArrayList<UserAccount>();
		accountHandler = new AccountHandler();
		friendListOwnerId = UserSession.CURRENTUSER_ID;
		
		this.FA = new FriendAdapter(this, R.layout.friendrow, friends);
		setListAdapter(this.FA);
		registerForContextMenu(getListView());
		
		//init ui elements
		nameTxt = (TextView) findViewById(R.id.friendlist_username);
		nameTxt.setText(UserSession.CURRENT_USER.getuName() + "'s Friends");
		btnAddFriend = (ImageButton) findViewById(R.id.friends_btnAdd);
		
		//call separate thread to retrieve friends list
		retrieveFriends();
		
		// Set onClick action for add friend button
		btnAddFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get the xml layout for the dialog
				LayoutInflater inflater = LayoutInflater.from(c);
				View searchView = inflater.inflate(R.layout.addfriend_dialog,
						null);
				// create dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
				// set the xml layout to the dialog
				builder.setView(searchView);
				// get the input box
				final EditText searchInput = (EditText) searchView
						.findViewById(R.id.searchdlg_txtusername);
				// set dialog message
				builder.setCancelable(true)
						.setPositiveButton("Add Friend",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// get the input from the search and
										// search the db for that username
										String searchName = searchInput
												.getText().toString();
										if (!searchName.isEmpty())
											addFriend(searchName);
										else
											Toast.makeText(
													c,
													"Please enter a username to search for!",
													Toast.LENGTH_SHORT).show();
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				// create the dialog and show
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			}
		});
		
		//when a friend item is clicked, open their profile
		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserAccount acct = (UserAccount) parent.getItemAtPosition(position);
				//Toast.makeText(FriendListActivity.this, "Position: " + position +  " User: " + acct.getuName(), Toast.LENGTH_SHORT).show();
				Intent i = new Intent(parent.getContext(),UserProfileActivity.class);
				i.putExtra("account", acct);
				startActivity(i);
			}
		});
	}

	
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
	
	//Context menu to allow user to delete tags
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) 
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		//show delete context menu only if user is viewing their own tag list
		if(this.friendListOwnerId == UserSession.CURRENTUSER_ID)
		{
			menu.setHeaderTitle("Remove Friend");
			menu.add(1,1,1,"Remove " + friends.get(info.position).getuName());
		}
	} 
	
	//actions for context menu items
		@Override
	    public boolean onContextItemSelected(MenuItem item) {
	        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
	                .getMenuInfo();
	        //delete the selected tag
	        if(item.getItemId() == 1)
	        {
	        	removeFriend(info.position);
	        }
	        return true;
	    }
	/*
	 * Functions
	 */
		
	//remove a friend from the friends list/db
	private void removeFriend(final int index)
	{
		final AccountHandler ah = new AccountHandler();
		Runnable deleteTag = new Runnable() 
		{
			@Override
			public void run() 
			{
				boolean success = ah.deleteFriend(friendListOwnerId, friends.get(index).getId());
				if (success)
				{
					   runOnUiThread(new Runnable() {
						    public void run() {
						    	PD.dismiss();
						    	//once the tag is removed from the db, remove it from the arraylist and update.
						    	FA.remove(friends.get(index));
								FA.notifyDataSetChanged();
								friends.remove(index);
								Toast.makeText(FriendListActivity.this, "Friend Removed", Toast.LENGTH_SHORT).show();	
						    }	    
					});	
				}
				else
					Toast.makeText(FriendListActivity.this, "Error Removing Friend..", Toast.LENGTH_SHORT).show();
			}
		};
		Thread thread = new Thread(null, deleteTag, "DeleteTagThread");
		thread.start();
		PD = ProgressDialog.show(FriendListActivity.this,
				"Please Wait", "Removing Friend...", true);
	}
	//Attempt to add a friend based on the given username
	private void addFriend(String userName)
	{
		final String uName = userName;
		
		// create new thread to add friend
		addFriend = new Runnable() {
			@Override
			public void run() {
				
				final StringBuilder sb = new StringBuilder();
				JSONObject response = accountHandler.AddFriend(UserSession.CURRENTUSER_ID, uName);
				try {
					String msg;
					int successCode = response.getInt(Constants.SUCCESS);
					//if friend was added successfully
					if(successCode == 1)
					{
						msg = "Added " + uName + " as a friend!";
						sb.append(msg);
						friendAdded = uName;
					}
					else //find out what went wrong
					{
						int errorCode = response.getInt(Constants.ERROR);
						switch (errorCode)
						{
							case Constants.ADDFRIEND_ALREADYFRIENDS: 
								msg = uName + " is already in your friends list!";
								sb.append(msg);
								break;
							case Constants.ADDFRIEND_USERNOTFOUND: 	
								msg = "Sorry! We can't find that user...please try another name!";
								sb.append(msg);
								break;
							case Constants.ADDFRIEND_ERROR: 	
								msg = "Oops! Something went wrong...Please search again!";
								sb.append(msg);
								break;
							default: 
								msg = "Please try again.";
								sb.append(msg);
								break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//show result on the UI thread and close the dialog
				runOnUiThread(new Runnable() {
					public void run() {
						PD.dismiss();
						if (! friendAdded.isEmpty())
						{
							//if a friend was added, update the list with their user account
							addSingleFriend();
						}		
						Toast.makeText(c, sb.toString(), Toast.LENGTH_LONG).show();
					}
				});

			}
		};
		Thread thread = new Thread(null, addFriend, "AddFriendThread");
		thread.start();
		PD = ProgressDialog.show(FriendListActivity.this,
				"Please Wait", "Adding Friend...", true);
		
	}
	
	// setup separate thread to retrieve Friends
	private void retrieveFriends() {
		// retrieve the tags in separate thread
		loadFriends = new Runnable() {
			@Override
			public void run() {
				// after we have the username, get the user's tags and display
				// them in the list
				GetFriends();
			}
		};
		Thread thread = new Thread(null, loadFriends, "GetFriendsThread");
		thread.start();
		PD = ProgressDialog.show(FriendListActivity.this, "Please Wait",
				"Retrieving friends...", true);
	}
	//get the friends from the database, then create UA objects for them and add them to the array list
	private void GetFriends()
	{
		friends = new ArrayList<UserAccount>();
		JSONObject obj;
		JSONArray friendData = accountHandler.getFriends(friendListOwnerId);
		
		if (friendData != null)
		{
			//loop through each entry in the json array (each tag encoded as JSON)
			for (int i = 0; i < friendData.length(); i ++)
			{
				obj = null;
				try 
				{
					obj = friendData.getJSONObject(i);
				} 
				catch (JSONException e) 
				{
					Log.d("FriendList GetFriends", "Error getting JSON Object from array.");
					e.printStackTrace();
				}
				
				if (obj != null)
				{
					UserAccount a = accountHandler.CreateAccountFromJSON(obj);
					friends.add(a);
				}
			}
			
		}
		runOnUiThread(returnRes);    
	}
	
	//when a friend is added, get their user account and add it to the list 
	//so the whole list does not need to be reloaded
	private void addSingleFriend()
	{
			//retrieve the account in separate thread
			loadSingleFriend = new Runnable() {
				@Override
				public void run() {
					//retrieve the UA as json from the db
					JSONObject json = accountHandler.getUser(friendAdded);
					//get the user account object
					UserAccount acct = accountHandler.CreateAccountFromJSON(json);
					acctToAdd = acct;
					runOnUiThread(new Runnable(){
						public void run(){
							if(acctToAdd != null)
							{
								friends.add(acctToAdd);
								FA.add(acctToAdd);
								FA.notifyDataSetChanged();
								//clear the callback fields after friend is added
								acctToAdd = null;
								friendAdded = "";
							}
							PD.dismiss();
						}
					});	
				}
			};
			Thread thread = new Thread(null, loadSingleFriend, "GetFriendThread");
			thread.start();
			PD = ProgressDialog.show(FriendListActivity.this,
					"Please Wait", "Updating List...", true);
	}
	//update list on UI thread
	private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            if(friends != null && friends.size() > 0){
                FA.notifyDataSetChanged();
                for(int i = 0; i < friends.size(); i++)
                	FA.add(friends.get(i));
            }
            PD.dismiss();
            FA.notifyDataSetChanged();
        }
      };
	 // ArrayAdapter to bind useraccounts to list view
	private class FriendAdapter extends ArrayAdapter<UserAccount> 
	{

		private ArrayList<UserAccount> friends;
		private Runnable loadImage;
		Context c;

		public FriendAdapter(Context context, int textViewResourceId, ArrayList<UserAccount> friends) {
			super(context, textViewResourceId, friends);
			this.friends = friends;
			this.c = context;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View row = convertView;
			if (row == null) 
			{
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = vi.inflate(R.layout.friendrow, null);
			}
			
			UserAccount account = friends.get(position);
			if (account != null) 
			{
				TextView nameTxt = (TextView) row.findViewById(R.id.friendrow_txtName);
				final ImageView imgView = (ImageView) row.findViewById(R.id.friendrow_thumbnail);

				if (nameTxt != null) 
					nameTxt.setText(account.getuName());                            
				/*
				 * Set thumbnail of tag image to imageview
				 */
					
				 if(imgView != null)
				 {

					 if(account.getImage() != null)
					 {
						 final String url = account.getImage();
						 //retrieve the tags in separate thread
						 loadImage = new Runnable() 
						 {
							 @Override
							 public void run() 
							 {
								 ImageHandler handler = new ImageHandler();
								 final Bitmap b = handler.getScaledBitmapFromUrl(url, R.dimen.thumbnail_width,
										 R.dimen.thumbnail_height);
								 //if the thumbnail gets returned, set it in the image view
								 if (b != null)
								 {
									 runOnUiThread(new Runnable()
									 {
										 public void run()
										 {
											 imgView.setImageBitmap(b);
										 }
									 });
								 }		
							 }
						 };
						 Thread thread = new Thread(null, loadImage, "LoadImageThread");
						 thread.start();
					 }
				 }       
			}
			return row;
		}//end getView
	}//end FriendAdapter
	
	
}//end addFriendActivity

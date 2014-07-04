package com.hci.geotagger.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.hci.geotagger.R;
import com.hci.geotagger.Objects.Adventure;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AccountHandler;
import com.hci.geotagger.connectors.AdventureHandler; 

import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

/**
 * Adventurelist activity displays a scrollable list of adventures for 
 * a given user account and gives the user the ability
 * to click on them to view the adventure. Currently, the ability to add
 * a new adventure has been temporarily disabled
 */

public class AdventureListActivity extends ListActivity {

	private ProgressDialog PD = null;
	private ArrayList<Adventure> adventures = null;
	private AdventureAdapter AA;
	private AdventureHandler adventureHandler; 
	private AccountHandler accountHandler;
	private Runnable viewAdventures;
	private int userID;
	private int CONTEXT_DELETE_ID = 1;
	private String userName;	
	private Adventure a;
	TextView nameTxt;
	private Button addAdv;

	/**
	 * Overidded onCreate method that retrieves list of adventures in background thread 
	 * from server via retrieveAdventures method and populates AdventureAdapter with this list of adventures.
	 * This method also sets up listview onClick listener so that when an single adventure is selected,
	 * it is passed to the AdventureViewActivity for viewing along with the entire adventure list for
	 * future functionality of scrolling sequentially thru each adventure in AdventureViewActivity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adventurelist);
		
		//removing add new adventure for now
		/*addAdv = (Button) findViewById(R.id.adventureList_btnNewAdv);
		addAdv.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{				
				Intent intent = new Intent(v.getContext(), EditAdventureActivity.class);
				intent.putExtra("newAdventure", true);
				startActivity(intent);
			}
		});	
		if (!Constants.ADVENTURE_EDITABLE){
			addAdv.setVisibility(Button.INVISIBLE);
			LinearLayout ll = (LinearLayout) findViewById(R.id.adventurelist);
			ll.removeView(addAdv);
			ll.requestLayout();
		}
			
*/
		// initialize objects				
		adventures = new ArrayList<Adventure>();
		this.AA = new AdventureAdapter(this, R.layout.row, adventures);
		setListAdapter(this.AA);
		registerForContextMenu(getListView());		

		adventureHandler = new AdventureHandler(this); 
		accountHandler = new AccountHandler(this);
		nameTxt = (TextView) findViewById(R.id.adventurelist_username);
		// action when a list item is clicked
		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				a = (Adventure) parent.getItemAtPosition(position);

				// Open adventure view, pass current position in adventurelist along with
				// whole adventurelist
				// this can be used to implement swipe through adventures without
				// downloading each time
				Intent i = new Intent(parent.getContext(),
						AdventureViewActivity.class);
				i.putExtra("startPos", position);
				i.putExtra("adventureList", adventures);
				Bundle bundle = new Bundle();
				bundle.putSerializable("adventure", a);
				i.putExtras(bundle);
				// if user is viewing their own adventure list, open the adventure view
				// expecting a result in case
				// the user deletes the adventure from the adventureview
				if (userID == UserSession.CURRENTUSER_ID)
					startActivityForResult(i, CONTEXT_DELETE_ID);
				else
					startActivity(i);
			}
		});		
		
		// get the UserID that was passed to this activity to determine whose
		// adventures to load
		Intent i = getIntent();
		int id = i.getIntExtra("id", -1);
		if (id != -1) {
			this.userID = id;
			retrieveAdventures();
		}
	}


	/**
	 *  Sets up separate thread to retrieve adventures from server. Called only in onCreate method currently
	 */
	private void retrieveAdventures() {
		// retrieve the adventures in separate thread
		viewAdventures = new Runnable() {
			@Override
			public void run() {
				// first get the username of the user whose adventures are being
				// viewed
				GetUsername();
				if (!userName.isEmpty() && userName != null) {
					// update the owner's name on the ui thread
					final String str = userName + "'s Adventures";
					runOnUiThread(new Runnable() {
						public void run() {
							nameTxt.setText(str);
						}
					});
				}
				// after we have the username, get the user's adventures and display
				// them in the list
				GetAdventures();
				
			}
		};
		Thread thread = new Thread(null, viewAdventures, "GetAdventureThread");
		thread.start();
		PD = ProgressDialog.show(AdventureListActivity.this, "Please Wait",
				"Retrieving adventures...", true);
	}

	/**
	 *  When the AdventureView activity is closed, this method checks to see if a delete call was sent back.
	 *  It is was, the adventure is deleted from the server
	 *  */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		if (requestCode == CONTEXT_DELETE_ID) {

			if (resultCode == RESULT_OK) {
				int removeIndex = data.getIntExtra("Delete", -1);
				// if so, delete adventure and update list (adventure was deleted from
				// database in other activity)
				if (removeIndex >= 0) {
					deleteAdventure(removeIndex);
				}
			}
		}
	}// onActivityResult

	/*
	 * Event Handlers
	 */

	/** Add logout to options menu since this class can't inherit it from
	 * 	 BaseActivity
	 */
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
	// create context menu when list item is long-pressed
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		// show delete context menu only if user is viewing their own adventure list
		if (this.userID == UserSession.CURRENTUSER_ID) {
			menu.setHeaderTitle("Adventure " + adventures.get(info.position).getName());
			menu.add("Delete");
		}
	}

	// actions for context menu items
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		// delete the selected adventure
		if (item.getTitle() == "Delete") {
			deleteAdventure(info.position);
		}
		return true;
	}

	/**
	 * Deletes the adventure listed at position. This method is called if selected in
	 * following AdventureViewActivity or if selected from the AdventureListActivity's context menu
	 * @param position Specifies the Adventure's position in the List to be deleted.
	 */
	private void deleteAdventure(final int position) {
		final AdventureHandler ah = new AdventureHandler(this); 		
		Runnable deleteAdventure = new Runnable() {
			@Override
			public void run() {
				boolean success = ah.deleteAdventure(adventures.get(position).getId());
				if (success) {
					runOnUiThread(new Runnable() {
						public void run() {
							PD.dismiss();
							// once the tag is removed from the db, remove it
							// from the arraylist and update.
							AA.remove(adventures.get(position));
							AA.notifyDataSetChanged();
							adventures.remove(position);
							Toast.makeText(AdventureListActivity.this,
									"Adventure Deleted!", Toast.LENGTH_SHORT).show();
						}
					});
				} else
					Toast.makeText(AdventureListActivity.this,
							"Error Deleting Adventure...", Toast.LENGTH_SHORT).show();
			}
		};
		Thread thread = new Thread(null, deleteAdventure, "DeleteAdventureThread");
		thread.start();
		PD = ProgressDialog.show(AdventureListActivity.this, "Please Wait",
				"Deleting adventure...", true);
	}

	/*
	 * Functions
	 */

	/**
	 *  Gets the username from the given ID
	 */
	private void GetUsername() {
		userName = accountHandler.getUsernameFromId(userID);
		Log.d("AdventureListActivity", "Username: " + userName + " ID: " + userID);
	}

	/**
	 *  Gets the adventures from the database, then creates adventure objects for them, and adds
	 *	 them to the member array list
	 */
	private void GetAdventures() {
		adventures = adventureHandler.getAllAdventuresUserPartOf(userID);
		runOnUiThread(returnRes);
	}	
	
	/*
	 * Bindings
	 */
	/**
	 *  Updates list with the results of getAdventure
	 */
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (adventures != null && adventures.size() > 0) {
				AA.notifyDataSetChanged();
				for (int i = 0; i < adventures.size(); i++)
					AA.add(adventures.get(i));
			}
			PD.dismiss();
			AA.notifyDataSetChanged();
		}
	};

	// arrayadapter to bind adventures to list view
	private class AdventureAdapter extends ArrayAdapter<Adventure> {

		private ArrayList<Adventure> adventures;
		Context c;

		public AdventureAdapter(Context context, int textViewResourceId,
				ArrayList<Adventure> adventures) {
			super(context, textViewResourceId, adventures);
			this.adventures = adventures;
			this.c = context;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = vi.inflate(R.layout.row, null);
			}
			Adventure a = adventures.get(position);
			if (a != null) {
				TextView nameTxt = (TextView) row
						.findViewById(R.id.row_txtName);
				TextView descTxt = (TextView) row
						.findViewById(R.id.row_txtdesc);
				TextView timeTxt = (TextView) row
						.findViewById(R.id.row_txtTime);
				final ImageView imgView = (ImageView) row
						.findViewById(R.id.row_thumbnail);
				

				if (nameTxt != null)
					nameTxt.setText(a.getName());
				if (descTxt != null)
					descTxt.setText(a.getDescription());
				if (timeTxt != null) {
					Date date = a.getCreatedDateTime();
					SimpleDateFormat df = new SimpleDateFormat(
							Constants.DATETIME_FORMAT);
					String formatted = df.format(date);
					timeTxt.setText(formatted);	
					
					Bitmap default_bitmap = BitmapFactory.decodeResource(
							c.getResources(), R.drawable.icon);
					imgView.setImageBitmap(default_bitmap);
				}				
			}// end if a
			return row;
		}// end getView

	}// end adventureadapter	
}
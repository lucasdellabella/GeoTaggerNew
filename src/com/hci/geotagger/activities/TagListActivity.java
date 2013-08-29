package com.hci.geotagger.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hci.geotagger.R;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AccountHandler;
import com.hci.geotagger.connectors.ImageHandler;
import com.hci.geotagger.connectors.TagHandler;

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
import android.widget.TextView;
import android.widget.Toast;

/*
 * Taglist activity displays a scrollable list of tags for 
 * a given user account and gives the user the ability
 * to click on them to view the tag. 
 */

public class TagListActivity extends ListActivity {

	private ProgressDialog PD = null;
	private ArrayList<Tag> tags = null;
	private TagAdapter TA;
	private TagHandler tagHandler;
	private AccountHandler accountHandler;
	private Runnable viewTags;
	private int userID;
	private int CONTEXT_DELETE_ID = 1;
	private String userName;
	private ImageHandler imageHandler;
	TextView nameTxt;

	HashMap<String, Bitmap> thumbCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taglist);

		// initialize objects
		imageHandler = new ImageHandler();
		thumbCache = new HashMap<String, Bitmap>();
		tags = new ArrayList<Tag>();
		this.TA = new TagAdapter(this, R.layout.row, tags);
		setListAdapter(this.TA);
		registerForContextMenu(getListView());

		tagHandler = new TagHandler();
		accountHandler = new AccountHandler();
		nameTxt = (TextView) findViewById(R.id.taglist_username);
		// action when a list item is clicked
		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Tag t = (Tag) parent.getItemAtPosition(position);

				// Open tag view, pass current position in taglist along with
				// whole taglist
				// this can be used to implement swype through tags without
				// downloading each time
				Intent i = new Intent(parent.getContext(),
						TagViewActivity.class);
				i.putExtra("startPos", position);
				i.putExtra("tagList", tags);
				// if user is viewing their own tag list, open the tag view
				// expecting a result incase
				// the user deletes the tag from the tagview
				if (userID == UserSession.CURRENTUSER_ID)
					startActivityForResult(i, CONTEXT_DELETE_ID);
				else
					startActivity(i);
			}
		});

		// get the UserID that was passed to this activity to determine whose
		// tags to load
		Intent i = getIntent();
		int id = i.getIntExtra("id", -1);
		if (id != -1) {
			this.userID = id;
			retrieveTags();
		}
	}

	// setup separate thread to retrieve tags
	private void retrieveTags() {
		// retrieve the tags in separate thread
		viewTags = new Runnable() {
			@Override
			public void run() {
				// first get the username of the user whose tags are being
				// viewed
				GetUsername();
				if (!userName.isEmpty() && userName != null) {
					// update the owner's name on the ui thread
					final String str = userName + "'s Tags";
					runOnUiThread(new Runnable() {
						public void run() {
							nameTxt.setText(str);
						}
					});
				}
				// after we have the username, get the user's tags and display
				// them in the list
				GetTags();
				// after getting tags, download the images to the cache and
				// update the ui
				loadImagesToCache();
			}
		};
		Thread thread = new Thread(null, viewTags, "GetTagThread");
		thread.start();
		PD = ProgressDialog.show(TagListActivity.this, "Please Wait",
				"Retrieving tags...", true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// when the tagview activity is closed, check to see if a delete call
		// was sent back
		if (requestCode == CONTEXT_DELETE_ID) {

			if (resultCode == RESULT_OK) {
				int removeIndex = data.getIntExtra("Delete", -1);
				// if so, delete tag and update list (tag was deleted from
				// database in other activity)
				if (removeIndex >= 0) {
					deleteTag(removeIndex);
				}
			}
		}
	}// onActivityResult

	/*
	 * Event Handlers
	 */

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
	// create context menu when list item is long-pressed
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		// show delete context menu only if user is viewing their own tag list
		if (this.userID == UserSession.CURRENTUSER_ID) {
			menu.setHeaderTitle("Tag " + tags.get(info.position).getName());
			menu.add("Delete");
		}
	}

	// actions for context menu items
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		// delete the selected tag
		if (item.getTitle() == "Delete") {
			deleteTag(info.position);
		}
		return true;
	}

	private void deleteTag(final int position) {
		final TagHandler th = new TagHandler();
		// Handler handler = new Handler();
		Runnable deleteTag = new Runnable() {
			@Override
			public void run() {
				boolean success = th.deleteTag(tags.get(position).getId());
				if (success) {
					runOnUiThread(new Runnable() {
						public void run() {
							PD.dismiss();
							// once the tag is removed from the db, remove it
							// from the arraylist and update.
							TA.remove(tags.get(position));
							TA.notifyDataSetChanged();
							tags.remove(position);
							Toast.makeText(TagListActivity.this,
									"Tag Deleted!", Toast.LENGTH_SHORT).show();
						}
					});
				} else
					Toast.makeText(TagListActivity.this,
							"Error Deleting Tag...", Toast.LENGTH_SHORT).show();
			}
		};
		Thread thread = new Thread(null, deleteTag, "DeleteTagThread");
		thread.start();
		PD = ProgressDialog.show(TagListActivity.this, "Please Wait",
				"Deleting tag...", true);
	}

	/*
	 * Functions
	 */

	// Get the username from the given ID
	private void GetUsername() {
		userName = accountHandler.GetUsernameFromId(userID);
		Log.d("TagListActivity", "Username: " + userName + " ID: " + userID);
	}

	// get the tags from the database, then create tag objects for them and add
	// them to the array list
	private void GetTags() {
		tags = new ArrayList<Tag>();
		JSONObject obj;
		JSONArray tagData = tagHandler.GetTagsById(userID);

		if (tagData != null) {
			// loop through each entry in the json array (each tag encoded as
			// JSON)
			for (int i = 0; i < tagData.length(); i++) {
				obj = null;
				try {
					obj = tagData.getJSONObject(i);
				} catch (JSONException e) {
					Log.d("TagList GetTags",
							"Error getting JSON Object from array.");
					e.printStackTrace();
				}

				if (obj != null) {
					Tag t = tagHandler.createTagFromJSON(obj);
					tags.add(t);
				}
			}
		}

	}

	private void loadImagesToCache() {
		// retrieve the tags in separate thread
		Runnable loadImages = new Runnable() {
			@Override
			public void run() {
				// loop through tags and cache their images if they have them
				for (Tag t : tags) {
					if(t != null)
					{
						String url = t.getImageUrl();
						// if tag has image url, download image and cache it
						if (!url.isEmpty()) {
							final Bitmap b = imageHandler.getScaledBitmapFromUrl(
									url, R.dimen.thumbnail_width,
									R.dimen.thumbnail_height);
							if (b != null)
								thumbCache.put(url, b);
						}
					}	
				}
				// update array adapter on ui thread
				runOnUiThread(returnRes);
			}
		};
		Thread thread = new Thread(null, loadImages, "LoadImageThread");
		thread.start();

	}

	/*
	 * Bindings
	 */
	// update list with the results of getTag
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (tags != null && tags.size() > 0) {
				TA.notifyDataSetChanged();
				for (int i = 0; i < tags.size(); i++)
					TA.add(tags.get(i));
			}
			PD.dismiss();
			TA.notifyDataSetChanged();
		}
	};

	// arrayadapter to bind tags to list view
	private class TagAdapter extends ArrayAdapter<Tag> {

		private ArrayList<Tag> tags;
		private Runnable loadImage;
		Context c;

		public TagAdapter(Context context, int textViewResourceId,
				ArrayList<Tag> tags) {
			super(context, textViewResourceId, tags);
			this.tags = tags;
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
			Tag t = tags.get(position);
			if (t != null) {
				TextView nameTxt = (TextView) row
						.findViewById(R.id.row_txtName);
				TextView descTxt = (TextView) row
						.findViewById(R.id.row_txtdesc);
				TextView timeTxt = (TextView) row
						.findViewById(R.id.row_txtTime);
				final ImageView imgView = (ImageView) row
						.findViewById(R.id.row_thumbnail);

				if (nameTxt != null)
					nameTxt.setText(t.getName());
				if (descTxt != null)
					descTxt.setText(t.getDescription());
				if (timeTxt != null) {
					Date date = t.getCreatedDateTime();
					SimpleDateFormat df = new SimpleDateFormat(
							Constants.DATETIME_FORMAT);
					String formatted = df.format(date);
					timeTxt.setText(formatted);
				}
				/*
				 * Set thumbnail of tag image to imageview
				 */
				if (imgView != null) {
					if (!t.getImageUrl().isEmpty()) {
						final String url = t.getImageUrl();
						// first try to get image from cache
						if (thumbCache.containsKey(url)) {
							imgView.setImageBitmap(thumbCache.get(url));
							Log.d("TagAdapter", "Got image from cache!");
						} else {
							Log.d("TagAdapter",
									"Tag has imageurl but it isnt in cache");

						}// end else
					}// end if imgurl
					else {
						Bitmap default_bitmap = BitmapFactory.decodeResource(
								c.getResources(), R.drawable.icon);
						imgView.setImageBitmap(default_bitmap);
					}
				} // end if imgview
			}// end if t
			return row;
		}// end getView

	}// end tagadapter
}

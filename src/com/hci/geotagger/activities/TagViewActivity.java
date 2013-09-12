package com.hci.geotagger.activities;

<<<<<<< HEAD
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
=======
>>>>>>> fa376da541230ebe8a60b839e301cf0753a0ee64
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hci.geotagger.R;
import com.hci.geotagger.Objects.Comment;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AccountHandler;
import com.hci.geotagger.connectors.ImageHandler;
import com.hci.geotagger.connectors.TagHandler;

import android.os.Bundle;
<<<<<<< HEAD
import android.os.Environment;
import android.provider.MediaStore;

=======
>>>>>>> fa376da541230ebe8a60b839e301cf0753a0ee64
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
<<<<<<< HEAD
import android.graphics.drawable.Drawable;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

=======
import android.util.Log;
>>>>>>> fa376da541230ebe8a60b839e301cf0753a0ee64
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class TagViewActivity extends Activity implements SensorEventListener 
{
	TextView txt_tagName, txt_ownerAndTime, txt_tagLocation, txt_tagDescription, 
			 txt_Rating, txt_currentLoc, txt_distance, txt_latLong;
	ImageView img_tagImage, img_commentImage, commentrow_thumbnail;
	private boolean HAS_IMAGE = false;
=======
public class TagViewActivity extends Activity {
	TextView txt_tagName, txt_ownerAndTime, txt_tagLocation, txt_tagDescription, txt_Rating;
	ImageView img_tagImage;
>>>>>>> fa376da541230ebe8a60b839e301cf0753a0ee64
	ImageView btnRating;
	Button commentBtn;
	EditText commentTxt;
	Dialog ratingDialog;
	RatingBar ratingBar;
	ProgressDialog PD;
	ListView commentList;
	
	private int currentTagIndex;
	private ArrayList<Tag> tagList;
	private Tag currentTag;
	private TagHandler tagHandler;
	private Runnable addComment, loadComments;
	
	private ArrayList<Comment> comments = null;
	private CommentAdapter CA;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//hide title bar
		setContentView(R.layout.activity_tag_view);
		//set up commentlist
		comments = new ArrayList<Comment>();
		this.CA = new CommentAdapter(this, R.layout.commentrow, comments);
		commentList = (ListView) findViewById(R.id.commentList);
		commentList.setAdapter(this.CA);
		registerForContextMenu(commentList);
		tagHandler = new TagHandler();
		commentBtn = (Button) findViewById(R.id.tagview_commentbtn);
		commentTxt = (EditText) findViewById(R.id.tagview_commenttxt);
			
		btnRating = (ImageView) findViewById(R.id.tagview_ratingbtn);
<<<<<<< HEAD
		img_commentImage = (ImageView) findViewById(R.id.tagview_commentimg);
		commentrow_thumbnail = (ImageView) findViewById(R.id.commentrow_thumbnail);
		
		//LOCATION INITIALIZATION
		
		//initialize components needed for location handling
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = (LocationListener) new MyLocationListener();
		//text that will be used for user's current location
		txt_currentLoc = (TextView) findViewById(R.id.tagview_currentlocationtxt);
		//text that will be used for the user's distance to tag
		txt_distance = (TextView) findViewById(R.id.tagview_distancetotxt);
		//request the latest location
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		//the location that is used for the location of the tag
		tagLocation = new Location("");
		
		//END LOCATION INITIALIZATION
		//COMPASS INITIALIZATION
		
		//create the compass defined in Compass.java
		myCompass = (Compass) findViewById(R.id.mycompass);
		//initialize the sensors for the compass
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	    sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    //the values that grab the direction of the device  
	    valuesAccelerometer = new float[3];
	    valuesMagneticField = new float[3];
	    //the values that update the direction of the device
	    matrixR = new float[9];
	    matrixI = new float[9];
	    matrixValues = new float[3];
	    
	    //END COMPASS INITIALIZATION
=======
>>>>>>> fa376da541230ebe8a60b839e301cf0753a0ee64
		
		//set dialog activity to fullscreen
		 LayoutParams params = getWindow().getAttributes();
	        params.height = LayoutParams.MATCH_PARENT;
	        params.width = LayoutParams.MATCH_PARENT;
	        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	        
		Intent i = getIntent();
		//if a taglist and start index were passed to the activity, load them
		if(i != null && i.hasExtra("startPos") && i.hasExtra("tagList")){
			currentTagIndex = i.getIntExtra("startPos", 0);
			try
			{
				tagList = (ArrayList<Tag>) i.getSerializableExtra("tagList");
			}
			catch(ClassCastException ex)
			{
				Log.d("TagView OnCreate", "Unable to deserialize taglist");
				ex.printStackTrace();
			}
		    //display the tag info in the activity
		    displayTag();
		    retrieveComments();
		   
		    
			//go to add tags menu when add button is clicked
			btnRating.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View view0) 
				{
					ratingDialog = new Dialog(TagViewActivity.this, R.style.ratingDialogStyle);
					ratingDialog.setContentView(R.layout.rating_dialog);
					ratingDialog.setCancelable(true);
					ratingBar = (RatingBar) ratingDialog.findViewById(R.id.dialog_ratingbar);
					ratingBar.setRating(2);

					Button updateButton = (Button) ratingDialog
							.findViewById(R.id.rank_dialog_button);
					updateButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							int r = (int) ratingBar.getRating();
							int curRating = currentTag.getRatingScore();
							switch (r)
							{
								case 1: if(curRating > 0)
									currentTag.setRatingScore(curRating - 1);
									break;
								case 2: //neutral, do nothing
									break;
								case 3:
									currentTag.setRatingScore(curRating + 1);
									break;
								case 4:
									currentTag.setRatingScore(curRating + 2);
									break;
							}
							updateRating();
							Toast t = Toast.makeText(getBaseContext(), "Thank you for rating!", Toast.LENGTH_SHORT);
							t.show();
							ratingDialog.dismiss();
						}
					});
					// now that the dialog is set up, it's time to show it
					ratingDialog.show();	
				}
			});
			//add comment when comment button is pressed
			commentBtn.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View view0) 
				{
					addComment();
					
				}
			});
		}	
	}//end onCreate

	//Context menu to allow user to delete tags
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) 
		{
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			//show delete context menu only if user created the comment, or if the comment is on their tag
			if(comments.get(info.position).getUsername().equalsIgnoreCase(UserSession.CURRENT_USER.getuName())
					|| currentTag.getOwnerName().equalsIgnoreCase(UserSession.CURRENT_USER.getuName()))
			{
				menu.setHeaderTitle("Comment");
				menu.add(1,1,1,"Remove Comment");
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
		        	removeComment(info.position);
		        }
		        return true;
		    }
		/*
		 * Functions
		 */
			
		//remove a friend from the friends list/db
		private void removeComment(final int index)
		{
			final TagHandler th = new TagHandler();
			Runnable deleteTag = new Runnable() 
			{
				@Override
				public void run() 
				{
					boolean success = th.deleteTagComment(comments.get(index).getId());
					if (success)
					{
						   runOnUiThread(new Runnable() {
							    public void run() {
							    	PD.dismiss();
							    	//once the tag is removed from the db, remove it from the arraylist and update.
							    	CA.remove(comments.get(index));
									CA.notifyDataSetChanged();
									comments.remove(index);
									Toast.makeText(TagViewActivity.this, "Comment Removed", Toast.LENGTH_SHORT).show();	
							    }	    
						});	
					}
					else
						Toast.makeText(TagViewActivity.this, "Error Removing Comment..", Toast.LENGTH_SHORT).show();
				}
			};
			Thread thread = new Thread(null, deleteTag, "DeleteTagThread");
			thread.start();
			PD = ProgressDialog.show(TagViewActivity.this,
					"Please Wait", "Removing Comment...", true);
		}
	/*
	 * Functions
	 */
	Integer position = null;
	private void addComment() {
		if (!commentTxt.getText().toString().isEmpty()) {
			final String comment = commentTxt.getText().toString();
			
			// create new thread to add friend
			addComment = new Runnable() {
				@Override
				public void run() {

					final StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
					JSONObject response;
					if(HAS_IMAGE == true)
					{
						response = tagHandler.AddTagComment(currentTag.getId(), comment,
								UserSession.CURRENT_USER.getuName(), url);
						Log.d("TagViewActivity", "Picture Added");
					}
					else
					{
						response = tagHandler.AddTagComment(currentTag.getId(), comment,
								UserSession.CURRENT_USER.getuName());
					}
					try 
					{
=======
					JSONObject response = tagHandler.AddTagComment(
							currentTag.getId(), comment,
							UserSession.CURRENT_USER.getuName());
					try {
>>>>>>> fa376da541230ebe8a60b839e301cf0753a0ee64
						String msg;
						int successCode = response.getInt(Constants.SUCCESS);
						// if friend was added successfully
						if (successCode == 1) {
							msg = "Comment added!";
							sb.append(msg);
							Comment comm = tagHandler.CreateCommentFromJson(response);
							comments.add(comm);
							position = comments.size()-1;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// show result on the UI thread and close the dialog
					runOnUiThread(new Runnable() {
						public void run() {
							if(position != null)
							{
								CA.add(comments.get(position));
								CA.notifyDataSetChanged();
							}
							position = null;
							commentTxt.setText("");
							PD.dismiss();			
							Toast.makeText(TagViewActivity.this, sb.toString(),
									Toast.LENGTH_LONG).show();
						}
					});

				}
			};
			Thread thread = new Thread(null, addComment, "AddCommentThread");
			thread.start();
			PD = ProgressDialog.show(TagViewActivity.this, "Please Wait",
					"Adding comment ...", true);
		} else {
			Toast.makeText(TagViewActivity.this, "Enter a comment!",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	// setup separate thread to retrieve comments
	private void retrieveComments() {
		// retrieve the tags in separate thread
		loadComments = new Runnable() {
			@Override
			public void run() {
				GetComments();
			}
		};
		Thread thread = new Thread(null, loadComments, "GetCommentsThread");
		thread.start();
		PD = ProgressDialog.show(TagViewActivity.this, "Please Wait",
				"Retrieving comments...", true);
	}

	// get the comments from the database,
	private void GetComments() {
		comments = new ArrayList<Comment>();
		JSONObject obj;
		JSONArray commentData = tagHandler.getTagComments(currentTag.getId());

		if (commentData != null) {
			// loop through each entry in the json array (each tag encoded as
			// JSON)
			for (int i = 0; i < commentData.length(); i++) {
				obj = null;
				try {
					obj = commentData.getJSONObject(i);
				} catch (JSONException e) {
					Log.d("FriendList GetFriends",
							"Error getting JSON Object from array.");
					e.printStackTrace();
				}

				if (obj != null) {
					Comment c = tagHandler.CreateCommentFromJson(obj);
					comments.add(c);
				}
			}

		}
		runOnUiThread(returnRes);
	}
		
	// update list on UI thread
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			if (comments != null && comments.size() > 0) {
				CA.notifyDataSetChanged();
				for (int i = 0; i < comments.size(); i++)
					CA.add(comments.get(i));
			}
			PD.dismiss();
			CA.notifyDataSetChanged();
		}
	};
	private void updateRating()
	{
		txt_Rating.setText(Integer.toString(currentTag.getRatingScore()));
	}
	private void displayTag()
	{
		//initialize controls
		txt_tagName = (TextView) findViewById(R.id.tagview_nametxt);
		txt_ownerAndTime = (TextView) findViewById(R.id.tagview_ownertimetxt);
		txt_tagLocation = (TextView) findViewById(R.id.tagview_locationtxt);
		txt_tagDescription = (TextView) findViewById(R.id.tagview_descriptiontxt);
		txt_Rating = (TextView) findViewById(R.id.tagview_ratingtxt);
		//if a taglist is available, display the tag from the current position
		if (tagList != null)
		{
			currentTag = tagList.get(currentTagIndex);
			//show tag name and underline
			txt_tagName.setText(currentTag.getName());
			txt_tagName.setPaintFlags(txt_tagName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			//show tag rating
			txt_Rating.setText(Integer.toString(currentTag.getRatingScore()));
			//show owner name and datetime of tag
			Date date = currentTag.getCreatedDateTime();
        	SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        	SimpleDateFormat tf = new SimpleDateFormat(Constants.TIME_FORMAT);
        	String fDate = df.format(date);
        	String fTime = tf.format(date);
        	String ownerTime = "By: " + currentTag.getOwnerName() + ". "
        			+ fTime + " on " + fDate + ".";
        	txt_ownerAndTime.setText(ownerTime);
        	//show tag location description
        	txt_tagLocation.setText(currentTag.getLocationString());
        	//show tag description
        	txt_tagDescription.setText(currentTag.getDescription());
        	//load image into image view
        	if(currentTag.getImageUrl() != null)
        	{
        		String url = currentTag.getImageUrl();
        		loadImage(url);
        	}
			
		}
	}//end displayTag
	
	//load the tag's image from the url into the image view
	private void loadImage(String imgUrl)
	{
		img_tagImage = (ImageView) findViewById(R.id.tagview_imageview);
		final String url = imgUrl;
		//retrieve the tags in separate thread
		Runnable loadImage = new Runnable() 
		{
			@Override
			public void run() 
			{
				ImageHandler handler = new ImageHandler();
				//get a scaled version of the image so we don't load the full size unnecessarily
				final Bitmap b = handler.getScaledBitmapFromUrl(url, R.dimen.image_width,
						R.dimen.image_height);
				//if the image gets returned, set it in the image view
				if (b != null)
				{
					runOnUiThread(new Runnable()
					{
					    public void run()
					    {
					        img_tagImage.setImageBitmap(b);
					    }
					});
				}		
			}
		};
		Thread thread = new Thread(null, loadImage, "LoadImageThread");
		thread.start();
	}
	
	
	/*
	 *	Event Handlers
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//create logout menu item with ID = 1
		menu.add(1, 1, 1, "Logout");
		menu.add(1,2,2, "Home");
		//if the current tag is owned by the logged in user, give option to delete it
		if(currentTag.getOwnerId() == UserSession.CURRENT_USER.getId())
			menu.add(1, 3, 3, "Delete Tag");
		
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
		case 2: //return to the list and delete this tag
			Intent homeIntent = new Intent(getBaseContext(), HomeActivity.class); 
			startActivity(homeIntent);
			finish();
		case 3: //return to the list and delete this tag
			Intent returnIntent = new Intent();
			returnIntent.putExtra("Delete", currentTagIndex);
			setResult(RESULT_OK, returnIntent); 
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
<<<<<<< HEAD
	private void openCamera()
	{
		Intent i_Cam = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//create a file to save an image in
		File f = imageHandler.makeImageFile();
		if (f != null)
		{
			TMP_IMGURI = Uri.fromFile(f);
			//if file was created, pass the URI to the camera app
			i_Cam.putExtra(MediaStore.EXTRA_OUTPUT, TMP_IMGURI);
		}
		//open camera to take pic when camera button is clicked				
        startActivityForResult(i_Cam, Constants.CAPTURE_IMG);
	}
	
	/*
	 * When the image is selected in the gallery, show it in the ImageView
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
        if (resultCode == RESULT_OK)
        {	
        	switch(requestCode)
            {
                //if new picture is taken, show that in the image view
        		case Constants.CAPTURE_IMG:
        			//if the image was saved to the device use the URI to populate image view and set the current image
        			if (TMP_IMGURI != null)
        			{        				
        				CUR_IMGURI = TMP_IMGURI;
        				CURRENT_IMAGE = new File(CUR_IMGURI.getPath());
        				TMP_IMGURI = null;
        				
        				img_commentImage.setImageURI(CUR_IMGURI);
        				HAS_IMAGE = true;
        			}
        			break;
            }
        }
        //if user backed out of the camera without saving picture, discard empty image file
        else
        {
        	if (TEMP_IMAGE != null)
        		TEMP_IMAGE.delete();
        	
        	TMP_IMGURI = null;
        }
    }
	
	/*
	 * Upload an image to the server and set the URL
	 */
	private String uploadImage(File f)
	{
		//first check the size of the image file without getting pixels
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
		
		int height = options.outHeight;
		int width = options.outWidth;
		Log.d("Image Size", "H, W = " + height + ", " + width);
		//resize image if it is very large to avoid out of memory exception
		if (height > 2048 || width > 2048)
			options.inSampleSize = 4;
		else if(height > 1024 || width > 1024)
			options.inSampleSize = 2;
		
		//get bitmap pixels
		options.inJustDecodeBounds = false;
		b = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
		height = b.getHeight();
		width = b.getWidth();
		Log.d("New Image Size", "H, W = " + height + ", " + width);
		if(height > 0 && width > 0)
=======
	 // ArrayAdapter to bind comments to list view
		private class CommentAdapter extends ArrayAdapter<Comment> 
>>>>>>> fa376da541230ebe8a60b839e301cf0753a0ee64
		{
			private ArrayList<Comment> comments;
			private Runnable loadImage;
			Context c;

			public CommentAdapter(Context context, int textViewResourceId, ArrayList<Comment> comments) {
				super(context, textViewResourceId, comments);
				this.comments = comments;
				this.c = context;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
<<<<<<< HEAD
				TextView nameTxt = (TextView) row.findViewById(R.id.commentrow_txtName);
				TextView timeTxt = (TextView) row.findViewById(R.id.commentrow_txtTime);
				TextView commentTxt = (TextView) row.findViewById(R.id.commentrow_txtdesc);
				ImageView commentImg = (ImageView) row.findViewById(R.id.commentrow_thumbnail);
				
				if (nameTxt != null) 
					nameTxt.setText(comment.getUsername()); 
				
				if(timeTxt != null)
=======
				View row = convertView;
				if (row == null) 
>>>>>>> fa376da541230ebe8a60b839e301cf0753a0ee64
				{
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					row = vi.inflate(R.layout.commentrow, null);
				}
				
<<<<<<< HEAD
				
				if(commentImg != null)
				{
					try
					{
						InputStream is = (InputStream) new URL(comment.getImageURL()).getContent();
						Drawable d = Drawable.createFromStream(is, "commentImage");
						commentImg.setBackground(d);
					}
					catch(Exception e) 
					{}
				}
			}
			return row;
		}
	}
	
	/*
	 * MyLocationListener implements LocationListener to request updates about the user's
	 * current location for use in determining the user's distance to the tag
	 */
	private class MyLocationListener implements LocationListener
	{
		/*
		 * Logs when the location has changed and then updates the user's current location
		 * and the user's distance to the tag whenever the user's current location
		 * changes
		 */
		@Override
		public void onLocationChanged(Location location) 
		{
			if(location != null)
			{
				Log.d("LOCATION CHANGED", location.getLatitude() + "");
				Log.d("LOCATION CHANGED", location.getLongitude() + "");
				String str = "Current Location: " + location.getLatitude() + ", "
						+ location.getLongitude();
				txt_currentLoc.setText(str);
				tagLocation.setLatitude(geo.getLatitude());
				tagLocation.setLongitude(geo.getLongitude());
				String distance = String.valueOf(location.distanceTo(tagLocation));
				txt_distance.setText(distance + " meters to tag");
			}
		}

		@Override
		public void onProviderDisabled(String arg0) 
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String arg0) 
		{
			// TODO Auto-generated method stub	
		}
=======
				Comment comment = comments.get(position);
				if (comment != null) 
				{
					TextView nameTxt = (TextView) row.findViewById(R.id.commentrow_txtName);
					TextView timeTxt = (TextView) row.findViewById(R.id.commentrow_txtTime);
					TextView commentTxt = (TextView) row.findViewById(R.id.commentrow_txtdesc);
					if (nameTxt != null) 
						nameTxt.setText(comment.getUsername());                            
					if(timeTxt != null)
					{
						Date date = comment.getCreatedDateTime();
						SimpleDateFormat df = new SimpleDateFormat(
								Constants.DATETIME_FORMAT);
						String formatted = df.format(date);
						timeTxt.setText(formatted);
					}
						
					if(commentTxt != null)
						commentTxt.setText(comment.getText().toString());
				}
				return row;
			}//end getView
		}//end FriendAdapter
		
>>>>>>> fa376da541230ebe8a60b839e301cf0753a0ee64

}

package com.hci.geotagger.activities;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hci.geotagger.R;
import com.hci.geotagger.Objects.Comment;
import com.hci.geotagger.Objects.GeoLocation;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.ImageHandler;
import com.hci.geotagger.connectors.TagHandler;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

public class TagViewActivity extends Activity implements SensorEventListener 
{
	TextView txt_tagName, txt_ownerAndTime, txt_tagLocation, txt_tagDescription, 
	txt_Rating, txt_currentLoc, txt_distance, txt_latLong;
	ImageView img_tagImage, commentrow_thumbnail, compassTriangle;
	ImageView img_commentImage;
	ImageView btnRating;
	Button commentBtn, navBtn;
	EditText commentTxt;
	Dialog ratingDialog;
	RatingBar ratingBar;
	ProgressDialog PD;
	ListView commentList;
	String url;
	
	private Drawable icon;

	private DecimalFormat ddf  = new DecimalFormat("#.00");
	private DecimalFormat lldf = new DecimalFormat("#.000000");

	private int currentTagIndex;
	private ArrayList<Tag> tagList;
	private Tag currentTag;
	private TagHandler tagHandler;
	private Runnable addComment, loadComments;

	//fields needed for the location on tag
	private LocationManager locationManager;
	private MyLocationListener locationListener;
	private Location tagLocation;
	private GeoLocation geo;

	//fields needed for the compass
	private SensorManager sensorManager;
	private Sensor sensorAccelerometer;
	private Sensor sensorMagneticField; 
	private float[] valuesAccelerometer;
	private float[] valuesMagneticField;
	private float[] matrixR;
	private float[] matrixI;
	private float[] matrixValues;
	//private float pivotX = 0;
	//private float pivotY = 0;
	private float currentDegree;
	//private Compass myCompass;

	private ArrayList<Comment> comments = null;
	private CommentAdapter CA;

	private ImageHandler imageHandler;
	private File camCurrImageFile, camTempImageFile;
	private Uri camCurrImageURI, camTempImageURI;
	private boolean commentHasImage = false;

	private HashMap<String, Bitmap> thumbCache;
	
	private Intent extendedComment;
	private Context c;


	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//hide title bar
		setContentView(R.layout.activity_tag_view);
		
		icon = getResources().getDrawable( R.drawable.icon );
		
		c = getBaseContext();
		extendedComment = new Intent(c, CommentViewActivity.class);

		//set up commentlist
		comments = new ArrayList<Comment>();
		this.CA = new CommentAdapter(this, R.layout.commentrow, comments);
		commentList = (ListView) findViewById(R.id.commentList);
		commentList.setAdapter(this.CA);
		registerForContextMenu(commentList);
		commentList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) 
			{
				Comment comment = comments.get(position);
				Log.d("Posi", Integer.toString(position));
				Log.d("Clicky", comment.getText().toString());
				extendedComment.putExtra("commentText", comment.getText().toString());

				if(comment.getImageURL() != null)
				{
					Log.d("getImage", comment.getImageURL());
					extendedComment.putExtra("imgURL", comment.getImageURL());
				}
				else
				{
					extendedComment.putExtra("imgURL", "");
				}
				startActivity(extendedComment);
			}
		});
		tagHandler = new TagHandler();
		imageHandler = new ImageHandler(this);
		commentBtn = (Button) findViewById(R.id.tagview_commentbtn);
		commentTxt = (EditText) findViewById(R.id.tagview_commenttxt);
		navBtn = (Button) findViewById(R.id.tagview_mapsnavbtn);
		navBtn.setOnClickListener(new OnClickListener()
		{	@Override
			public void onClick(View arg0) {
			if (currentTag == null)
				return;
			String gps = "geo:0,0?q=" + currentTag.getLocation().getLatitude() + ","
					+ currentTag.getLocation().getLongitude() + " (" 
					+ currentTag.getName() + ")";
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(gps));
			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
			startActivity(intent);
		}
		});

		btnRating = (ImageView) findViewById(R.id.tagview_ratingbtn);
		img_commentImage = (ImageView) findViewById(R.id.tagview_commentimg);
		commentrow_thumbnail = (ImageView) findViewById(R.id.commentrow_thumbnail);
		
		compassTriangle = (ImageView) findViewById(R.id.compassTriangle);
		thumbCache = new HashMap<String, Bitmap>();

		//pivotX = compassTriangle.getWidth()/2;
		//pivotY = compassTriangle.getHeight()/2;

		//LOCATION INITIALIZATION

		//initialize components needed for location handling
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		//text that will be used for user's current location
		txt_currentLoc = (TextView) findViewById(R.id.tagview_currentlocationtxt);
		//text that will be used for the user's distance to tag
		txt_distance = (TextView) findViewById(R.id.tagview_distancetotxt);
		txt_distance.bringToFront();			// had to do programmatically because I couldn't change the order in XML without it crashing
		//request the latest location
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L,500.0f, locationListener);
		//the location that is used for the location of the tag
		tagLocation = new Location("");

		//END LOCATION INITIALIZATION
		//COMPASS INITIALIZATION

		//create the compass defined in Compass.java
		//myCompass = (Compass) findViewById(R.id.mycompass);
		//initialize the sensors for the compass
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		// the values that grab the direction of the device  
		valuesAccelerometer = new float[3];
		valuesMagneticField = new float[3];
		//the values that update the direction of the device
		matrixR = new float[9];
		matrixI = new float[9];
		matrixValues = new float[3];
		currentDegree = 0;

		//END COMPASS INITIALIZATION

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
					updateButton.setOnClickListener(new View.OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
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
			img_commentImage.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view0)
				{
					openCamera();
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
		clearCameraVars();
	} //end onCreate

	/*
	 * Implemented so that the sensors for the compass are only working when the 
	 * application is active and not just running in the background which would affect
	 * battery life
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() 
	{
		//sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		//sensorManager.registerListener(this, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
				SensorManager.SENSOR_DELAY_GAME);
		locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		super.onResume();
	}

	/*
	 * Implemented so that the sensors for the compass are only working when the 
	 * application is active and not just running in the background which would affect
	 * battery life
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() 
	{
		//sensorManager.unregisterListener(this, sensorAccelerometer);
		//sensorManager.unregisterListener(this, sensorMagneticField);
		sensorManager.unregisterListener(this);
		locationManager.removeUpdates(locationListener);
		locationListener = null;
		super.onPause();
	}

	/*
	 * Method must be defined to implement SensorEventListener
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) 
	{
		// TODO Auto-generated method stub
	}

	/*
	 * This method detects whenever the sensors pick up a change
	 * in location and/or position. When a change is detected, the compass
	 * will be updated to point in regards to the current location.
	 * 
	 * Code modified from:
	 * http://sunil-android.blogspot.com/2013/02/create-our-android-compass.html
	 * 
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */

	/*
	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		  switch(event.sensor.getType()) //determine what sensor event has occured
		  {
		  	case Sensor.TYPE_MAGNETIC_FIELD:
		  		for(int i = 0; i < 3; i++)
		  		{
		  			valuesMagneticField[i] = event.values[i];
		  		}
		  		break;
		  	case Sensor.TYPE_ACCELEROMETER:
		  		for(int i = 0; i < 3; i++)
		  		{
		  			valuesAccelerometer[i] = event.values[i];
		  		}
		  		break;
		  }

		  boolean success = SensorManager.getRotationMatrix(matrixR, matrixI,
		       valuesAccelerometer, valuesMagneticField);

		  if(success) //if the rotation matrix was found above, update the compass
		  {  
			  SensorManager.getOrientation(matrixR, matrixValues);
			  //myCompass.update(matrixValues[0]);
			  compassTriangle.setRotation(matrixValues[0]);
		  }
	}*/


	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		//spencerOnSensorChanged(event);
		//emilyOnSensorChanged(event);
	}

	private void emilyOnSensorChanged(SensorEvent event) 
	{
		float degree = currentDegree;
		boolean success = false;
		double xo = 0;
		double yo = 0;
		double zo = 0;

		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			// assume flat, position is x & y
			xo = event.values[0];
			yo = event.values[1];
			zo = event.values[2];
			success = true;
		} else {
			switch(event.sensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:
				for(int i =0; i < 3; i++){
					valuesAccelerometer[i] = event.values[i];
				}
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				for(int i =0; i < 3; i++){
					valuesMagneticField[i] = event.values[i];
				}
				break;
			}

			boolean s = SensorManager.getRotationMatrix(
					matrixR,
					matrixI,
					valuesAccelerometer,
					valuesMagneticField);

			if(s){
				SensorManager.getOrientation(matrixR, matrixValues);
				xo = Math.toDegrees(matrixValues[0]);
				yo = Math.toDegrees(matrixValues[1]);
				zo = Math.toDegrees(matrixValues[2]);
				success = true;
			}
		}

		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null && success) { // success
			double x = location.getLatitude();
			double y = location.getLongitude();


			if (success) {
				double lat = currentTag.getLocation().getLatitude();
				double lon = currentTag.getLocation().getLongitude();

				//http://stackoverflow.com/questions/3309617/calculating-degrees-between-2-points-with-inverse-y-axis
				//Suppose you're at (a, b) and the object is at (c, d). 
				//The relative position of the object to you is (x, y) = (c - a, d - b).
				//var theta = Math.atan2(-y, x); b - d, c - a
				float toDegree = (float) Math.toDegrees(Math.atan2(y - lon, lat - x));
				// from device to tag

				// now need to account for orientation of device (xo, yo)
				float devDegree = (float) Math.toDegrees(Math.atan2(-yo, xo));
				degree = devDegree - toDegree;

				// for debugging the sensors - WHICH ISN'T WORKING
				//String msg = "dev loc: [" + x + ", " + y + "] (" + toDegree + ")\n";
				//msg += "tag loc: [" + lat + ", " + lon + "]\n";
				//msg += "dev o: [" + xo + ", " + yo + ", " + zo + "] (" + devDegree + ")\n";
				//msg += "degree: " + degree + "\n";
				//Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				//Log.d("LoginPostExecute", msg);

				// create a rotation animation (reverse turn degree degrees)
				RotateAnimation ra = 
						new RotateAnimation(currentDegree, degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				//new RotateAnimation(currentDegree, degree);

				// how long the animation will take place
				ra.setDuration(210);

				// set the animation after the end of the reservation status
				ra.setFillAfter(true);

				// Start the animation
				compassTriangle.startAnimation(ra);
				currentDegree = degree; 

				//myCompass.update(matrixValues[0]);

			} else {
				// TO DO some kind of error message or wait
				String msg = "Loading tag location...";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				Log.d("LoginPostExecute", msg);
			}
		}
	}

	private void spencerOnSensorChanged(SensorEvent event) 
	{
		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);

		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		compassTriangle.startAnimation(ra);
		currentDegree = -degree; 
	}


	/*
	 * Creates the context menu that allows the user to delete tags
	 */
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

	/*
	 * Implements the click listeners for selecting an item from the context menu
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
		final AdapterView.AdapterContextMenuInfo info = 
				(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		//delete the selected tag
		if(item.getItemId() == 1)
		{
			removeComment(info.position);
		}
		return true;
	}

	/*
	 * FUNCTIONS
	 */

	/*
	 * Removes a comment from a tag
	 */
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
					runOnUiThread(new Runnable() 
					{
						public void run() 
						{
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

	Integer position = null; //Is this necessary? -SK 9/2

	/*
	 * Adds a comment to a tag 
	 */
	private void addComment() 
	{
		if (!commentTxt.getText().toString().isEmpty()) 
		{
			final String comment = commentTxt.getText().toString();

			// create new thread to add comment
			addComment = new Runnable() 
			{
				@Override
				public void run() 
				{
					final StringBuilder sb = new StringBuilder();
					JSONObject response;
					if(commentHasImage == true)
					{
						String url = uploadImage(camCurrImageFile);
						Log.d("TagViewActivity", "url: " + url);
						response = tagHandler.AddTagComment(currentTag.getId(), comment,
								UserSession.CURRENT_USER.getuName(), url);
						Log.d("TagViewActivity", "Picture Added");
						
						//reset image
						clearCameraVars();
					}
					else
					{
						response = tagHandler.AddTagComment(currentTag.getId(), comment,
								UserSession.CURRENT_USER.getuName());
					}
					try 
					{
						String msg;
						int successCode = response.getInt(Constants.SUCCESS);
						// if comment was added successfully
						if (successCode == 1) 
						{
							msg = "Comment added!";
							sb.append(msg);
							Comment comm = new Comment(response.getInt("cID"), currentTag.getId(), comment, UserSession.CURRENT_USER.getuName(), new Date());//tagHandler.CreateCommentFromJson(response);
							comments.add(comm);
							position = comments.size()-1;
						}
					} 
					catch (JSONException e) 
					{
						e.printStackTrace();
					}
					// show result on the UI thread and close the dialog
					runOnUiThread(new Runnable() 
					{
						public void run() 
						{
							if(position != null)
							{
								CA.add(comments.get(position));
								CA.notifyDataSetChanged();
								commentList.setSelection(position);
							}
							position = null;
							commentTxt.setText("");
							PD.dismiss();			
							Toast.makeText(TagViewActivity.this, sb.toString(),
									Toast.LENGTH_LONG).show();
							img_commentImage.setImageDrawable(icon);
						}
					});

				}
			};
			Thread thread = new Thread(null, addComment, "AddCommentThread");
			thread.start();
			PD = ProgressDialog.show(TagViewActivity.this, "Please Wait",
					"Adding comment ...", true);
		} 
		else 
		{
			Toast.makeText(TagViewActivity.this, "Enter a comment!",
					Toast.LENGTH_SHORT).show();
		}
	}

	// setup separate thread to retrieve comments

	/*
	 * Retrieves the comment for the tag by creating a new thread
	 */
	private void retrieveComments() 
	{
		// retrieve the tags in separate thread
		loadComments = new Runnable() 
		{
			@Override
			public void run() 
			{
				GetComments();
			}
		};
		Thread thread = new Thread(null, loadComments, "GetCommentsThread");
		thread.start();
		PD = ProgressDialog.show(TagViewActivity.this, "Please Wait",
				"Retrieving comments...", true);
	}

	// get the comments from the database,
	private void GetComments() 
	{
		comments = new ArrayList<Comment>();
		JSONObject obj;
		JSONArray commentData = tagHandler.getTagComments(currentTag.getId());

		if (commentData != null) 
		{
			// loop through each JSON entry in the JSON array (tags encoded as JSON)
			for (int i = 0; i < commentData.length(); i++) 
			{
				obj = null;
				try 
				{
					obj = commentData.getJSONObject(i);
				} 
				catch (JSONException e) 
				{
					Log.d("FriendList GetFriends",
							"Error getting JSON Object from array.");
					e.printStackTrace();
				}

				if (obj != null) 
				{
					Comment c = tagHandler.CreateCommentFromJson(obj);
					comments.add(c);
				}
			}

			loadImagesToCache();
		}
	}

	private Runnable returnRes = new Runnable() 
	{
		@Override
		public void run()
		{
			if (comments != null && comments.size() > 0) 
			{
				CA.notifyDataSetChanged();
				for (int i = 0; i < comments.size(); i++)
					CA.add(comments.get(i));
				commentList.setSelection(comments.size()-1);
			}
			PD.dismiss();
			CA.notifyDataSetChanged();
		}
	};

	/*
	 * Updates the rating of a tag
	 */
	private void updateRating()
	{
		txt_Rating.setText(Integer.toString(currentTag.getRatingScore()));
	}

	/*
	 * Displays the tag for viewing by the user
	 */
	private void displayTag()
	{
		//initialize the TextViews to populate
		txt_tagName = (TextView) findViewById(R.id.tagview_nametxt);
		txt_ownerAndTime = (TextView) findViewById(R.id.tagview_ownertimetxt);
		txt_latLong = (TextView) findViewById(R.id.tagview_latlongtxt);
		txt_tagLocation = (TextView) findViewById(R.id.tagview_locationtxt);
		txt_tagDescription = (TextView) findViewById(R.id.tagview_descriptiontxt);
		txt_Rating = (TextView) findViewById(R.id.tagview_ratingtxt);
		
		//if a taglist is available, display the tag from the current position
		if (tagList != null)
		{
			currentTag = tagList.get(currentTagIndex);
			//show tag name and underline
			txt_tagName.setText(currentTag.getName());
			//txt_tagName.setPaintFlags(txt_tagName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
			//show tag rating
			txt_Rating.setText(Integer.toString(currentTag.getRatingScore()));
			//txt_currentLoc.setText((currentTag == null || currentTag.getLocationString() == null ? "Name Not Specified" : ""));
			//show owner name and date/time of tag
			Date date = currentTag.getCreatedDateTime();
			SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
			SimpleDateFormat tf = new SimpleDateFormat(Constants.TIME_FORMAT);
			String fDate = df.format(date); 
			String fTime = tf.format(date);
			String ownerTime = "";
			ownerTime += (currentTag.getOwnerName() != null ? currentTag.getOwnerName() + ": " : "") + 
					fTime + " on " + fDate;
			txt_ownerAndTime.setText(ownerTime);

			//get the location of the tag that was specified in the creation of the tag
			geo = currentTag.getLocation();
			double lat = geo.getLatitude();
			double lon = geo.getLongitude();
			//show tag location
			txt_latLong.setText(lldf.format(lat) + ", " + lldf.format(lon));
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
	}

	/*
	 * Load the tag's image from the URL and into the ImageView
	 */
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
				if (b != null) //if the image gets returned, set it in the image view
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
	 * EVENT HANDLERS
	 */

	/*
	 * Creates the options menu for the application allowing the user to logout or go
	 * back to the home screen
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//create logout menu item with ID = 1
		menu.add(1, 1, 1, "Logout");
		menu.add(1, 2, 2, "Home");
		//if the current tag is owned by the logged in user, give option to delete it
		if(currentTag.getOwnerId() == UserSession.CURRENT_USER.getId())
		{
			menu.add(1, 3, 3, "Delete Tag");
		}

		return true;
	}

	/*
	 * Handles the event of a user clicking on an item in the options menu
	 */
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

	private void clearCameraVars()
	{
		commentHasImage = false;
		imageHandler = new ImageHandler(this);
		camCurrImageFile = null;
		camTempImageFile = null;
		camCurrImageURI = null;
		camTempImageURI = null;
	}
	
	private void openCamera()
	{
		clearCameraVars();
		Intent i_Cam = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//create a file to save an image in
		File f = imageHandler.makeImageFile();
		if (f != null)
		{
			camTempImageURI = Uri.fromFile(f);
			//if file was created, pass the URI to the camera app
			i_Cam.putExtra(MediaStore.EXTRA_OUTPUT, camTempImageURI);
		}
		//open camera to take pic when camera button is clicked				
		startActivityForResult(i_Cam, Constants.CAPTURE_IMG);
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
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		b = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
		height = b.getHeight();
		width = b.getWidth();
		Log.d("New Image Size", "H, W = " + height + ", " + width);
		if(height > 0 && width > 0)
		{
			String url = imageHandler.UploadImageToServer(b);
			b.recycle();
			b = null;
			Log.d("AddImageTask", "Got response, img url = " + url);
			return url;
		}
		else
		{
			b.recycle();
			b = null;
			return null;
		}
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
				if (camTempImageURI != null)
				{        				
					camCurrImageURI = camTempImageURI;
					camCurrImageFile = new File(camCurrImageURI.getPath());
					camTempImageURI = null;

					img_commentImage.setImageURI(camCurrImageURI);
					commentHasImage = true;
				}
				break;
			}
		}
		//if user backed out of the camera without saving picture, discard empty image file
		else
		{
			if (camTempImageFile != null && camTempImageFile.exists())
				camTempImageFile.delete();

			camTempImageURI = null;
		}
	}

	private void loadImagesToCache() {
		// retrieve the tags in separate thread
		Runnable loadImages = new Runnable() 
		{
			@Override
			public void run() 
			{
				// loop through tags and cache their images if they have them
				for (Comment c : comments) 
				{
					Log.d("uploading", "Uploading: " + c);
					if(c != null)
					{
						String url = c.getImageURL();
						// if tag has image url, download image and cache it
						if (url != null && !url.equals("") && !thumbCache.containsKey(url)) 
						{
							final Bitmap b = imageHandler.getScaledBitmapFromUrl(
									url, R.dimen.thumbnail_width,
									R.dimen.thumbnail_height);
							Log.d("CacheTest", "Bitmap: " + b.toString() + " URL: " + url);
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
	 * This class extends ArrayAdapter to bind comments to a list view
	 */
	private class CommentAdapter extends ArrayAdapter<Comment> 
	{
		private ArrayList<Comment> comments;
		Context c;

		/*
		 * Constructor for the CommentAdapter
		 */
		public CommentAdapter(Context context, int textViewResourceId, ArrayList<Comment> comments) 
		{
			super(context, textViewResourceId, comments);
			this.comments = comments;
			this.c = context;
		}

		/*
		 * Returns the id of the item at a specified position
		 */
		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View row = convertView;
			if (row == null) 
			{
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = vi.inflate(R.layout.commentrow, null);
			}

			Comment comment = comments.get(position);
			if (comment != null) 
			{
				TextView nameTxt = (TextView) row.findViewById(R.id.commentrow_txtName);
				TextView timeTxt = (TextView) row.findViewById(R.id.commentrow_txtTime);
				TextView commentTxt = (TextView) row.findViewById(R.id.commentrow_txtdesc);
				ImageView commentImg = (ImageView) row.findViewById(R.id.commentrow_thumbnail);

				if (nameTxt != null) 
					nameTxt.setText(comment.getUsername()); 

				if(timeTxt != null)
				{
					Date date = comment.getCreatedDateTime();
					SimpleDateFormat df = new SimpleDateFormat(Constants.DATETIME_FORMAT);
					String formatted = df.format(date);
					timeTxt.setText(formatted);
				}

				if(commentTxt != null)
				{
					commentTxt.setText(comment.getText().toString());
				}


				if (commentImg != null) 
				{
					if (comment.getImageURL() != null) 
					{
						String url = comment.getImageURL();
						// first try to get image from cache
						Log.d("loadImageNull", "URL: " + url);

						if(!url.equals(""))
						{							
							if (thumbCache.containsKey(url)) 
							{
								commentImg.setImageBitmap(thumbCache.get(url));
								Log.d("TagAdapter", "Got image from cache!");
							} 
							else 
							{
								Log.d("TagAdapter",
										"Tag has imageurl but it isnt in cache");
							}	
						}
					}
					else {
						Bitmap default_bitmap = BitmapFactory.decodeResource(
								c.getResources(), R.drawable.icon);
						commentImg.setImageBitmap(default_bitmap);
					}
				} // end if imgview
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
		private double lat = 0;
		private double lon = 0;
		private Location lastBestLocation;

		public double getLatitude() {
			return lat;
		}

		private double getLongitude() {
			return lon;
		}

		/*
		 * Logs when the location has changed and then updates the user's current location
		 * and the user's distance to the tag whenever the user's current location
		 * changes
		 */
		@Override
		public void onLocationChanged(Location location) 
		{
			if (isBetterLocation(location, lastBestLocation))
				lastBestLocation = location;

			if (lastBestLocation != null)
			{
				//Log.d("LOCATION CHANGED", location.getLatitude() + "");
				//Log.d("LOCATION CHANGED", location.getLongitude() + "");
				String str = lldf.format(location.getLatitude()) + ", " + lldf.format(location.getLongitude());
				txt_currentLoc.setText(str);
				tagLocation.setLatitude(geo.getLatitude());
				tagLocation.setLongitude(geo.getLongitude());
				float dist = location.distanceTo(tagLocation);
				String unit = "m";
				if (dist >= 1000)
				{	unit = "km";
				dist /= 1000;
				}
				txt_distance.setText("Distance:\n" + ddf.format(dist) + " " + unit);
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

		@Override
		public void onStatusChanged(String arg0, int status, Bundle arg2)
		{
			if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE)
			{
				txt_currentLoc.setText("Location Not Available");
				txt_currentLoc.setTypeface(null, Typeface.ITALIC);
			}
			else txt_currentLoc.setTypeface(null, Typeface.NORMAL);
		}

		//http://developer.android.com/guide/topics/location/strategies.html
		private static final int TWO_MINUTES = 1000 * 60 * 2;

		/** Determines whether one Location reading is better than the current Location fix
		 * @param location  The new Location that you want to evaluate
		 * @param currentBestLocation  The current Location fix, to which you want to compare the new one
		 */
		protected boolean isBetterLocation(Location location, Location currentBestLocation) {
			if (currentBestLocation == null) {
				// A new location is always better than no location
				return true;
			}

			// Check whether the new location fix is newer or older
			long timeDelta = location.getTime() - currentBestLocation.getTime();
			boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
			boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
			boolean isNewer = timeDelta > 0;

			// If it's been more than two minutes since the current location, use the new location
			// because the user has likely moved
			if (isSignificantlyNewer) {
				return true;
				// If the new location is more than two minutes older, it must be worse
			} else if (isSignificantlyOlder) {
				return false;
			}

			// Check whether the new location fix is more or less accurate
			int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
			boolean isLessAccurate = accuracyDelta > 0;
			boolean isMoreAccurate = accuracyDelta < 0;
			boolean isSignificantlyLessAccurate = accuracyDelta > 200;

			// Check if the old and new location are from the same provider
			boolean isFromSameProvider = isSameProvider(location.getProvider(),
					currentBestLocation.getProvider());

			// Determine location quality using a combination of timeliness and accuracy
			if (isMoreAccurate) {
				return true;
			} else if (isNewer && !isLessAccurate) {
				return true;
			} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
				return true;
			}
			return false;
		}

		/** Checks whether two providers are the same */
		private boolean isSameProvider(String provider1, String provider2) {
			if (provider1 == null) {
				return provider2 == null;
			}
			return provider1.equals(provider2);
		}
	}
}

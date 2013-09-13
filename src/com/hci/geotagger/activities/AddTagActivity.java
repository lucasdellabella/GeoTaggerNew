/*
 * Addtag activity allows the user to add new tags to the database.
 * This includes setting fields such as name/description and also
 * setting an image for the tag and usinggeo-location.
 * 
 * Chris Loeschorn
 * Spring 2013
 */
package com.hci.geotagger.activities;

import java.io.File;

import java.text.SimpleDateFormat;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.provider.MediaStore;

import android.util.Log;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import android.app.AlertDialog;

import android.content.DialogInterface;

import com.hci.geotagger.R;
import com.hci.geotagger.Objects.Adventure;
import com.hci.geotagger.Objects.GeoLocation;
import com.hci.geotagger.Objects.Tag;
import com.hci.geotagger.common.AlertHandler;
import com.hci.geotagger.common.BaseActivity;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.LocationHandler;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AdventureHandler;
import com.hci.geotagger.connectors.ImageHandler;
import com.hci.geotagger.connectors.TagHandler;
import com.hci.geotagger.exceptions.UnknownErrorException;

public class AddTagActivity extends BaseActivity 
{
	private static final String LOGTAG = "EXPLORECA";
	private final int CONTEXT_DELETE_ID = 1;
	private boolean HAS_IMAGE = false, IMG_ERROR = false, URL_SET = false;
	private String IMG_URL;
	private File CURRENT_IMAGE, TEMP_IMAGE;
	private Uri CUR_IMGURI, TMP_IMGURI;
	final Context c = AddTagActivity.this;
	private AdventureHandler AH;
	private Adventure adventure;
	ImageHandler imageHandler;
	
	Button btnOk, btnCancel;
	ImageView imgView;
	EditText txtName, txtDesc, txtLoc;
	CheckBox chkGPS;
	ProgressDialog pDialog;
	
	private LocationListener listener;
	private boolean gpsEnabled = false;
	private boolean networkEnabled = false;
	private LocationManager lm;
	private Location location;
	
	/*
	 * FUNCTIONS
	 */
	
	/*
	 * Opens the camera to allow user to take a picture
	 */
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
	 * Opens the device's gallery to allow the user to select an image from the gallery
	 */
	private void openGallery()
	{
	   	Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(i, "Select Picture"), Constants.SELECT_IMG);
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
		{
			String url = imageHandler.UploadImageToServer(b);
			b.recycle();
			Log.d("AddImageTask", "Got response, img url = " + url);
			return url;
		}
		else
		{
			return null;
		}	
	}
	
	/*
	 * Event Handlers
	 */
	
	/*
	 * Defines what is initialized when the activity is created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tag);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		//adventure = (Adventure) bundle.getSerializable("adventure");
		AH = new AdventureHandler();

		//get form control IDs
		imgView = (ImageView) findViewById(R.id.addtag_imgView);
		registerForContextMenu(imgView);
		
		//buttons
		btnOk = (Button) findViewById(R.id.addtag_btnOk);
		btnOk.setEnabled(true);
		btnCancel = (Button) findViewById(R.id.addtag_btnCancel);
		btnCancel.setEnabled(true);
		
		//text fields
		txtName = (EditText) findViewById(R.id.addtag_name);
		txtDesc = (EditText) findViewById(R.id.addtag_desc);
		txtLoc = (EditText) findViewById(R.id.addtag_location);
		
		//Check box
		chkGPS = (CheckBox) findViewById(R.id.addtag_useGPS);
		
		imageHandler = new ImageHandler(c);
		
		//initialize location components
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
        listener = new LocationListener() //initialization of LocationListener
        {
			@Override
			public void onLocationChanged(Location locat) //update location when it changes
			{
				location = locat;
			}

			@Override
			public void onProviderDisabled(String provider) 
			{
				// TODO Auto-generated method stub	
			}

			@Override
			public void onProviderEnabled(String provider) 
			{
				// TODO Auto-generated method stub	
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) 
			{
				// TODO Auto-generated method stub	
			} 
        };
        
    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,500.0f, listener);
		
		//If the orientation was changed, reload the image
		if(savedInstanceState != null)
	    {
			String savedUri = (savedInstanceState.getString("imageUri"));
			if(savedUri != null)
			{
				//Set the layout of the image view to allow the image to expand up to max size
	        	LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	        	                        LayoutParams.WRAP_CONTENT);
	        	layout.gravity = Gravity.CENTER_HORIZONTAL;
	        	layout.leftMargin = R.dimen.Scrollview_Leftmargin;
	        	layout.rightMargin = R.dimen.Scrollview_Leftmargin;
	        	
				CUR_IMGURI = Uri.parse(savedUri);
				CURRENT_IMAGE = new File(CUR_IMGURI.getPath());

				imgView.setLayoutParams(layout);
				try
				{
					imgView.setImageURI(CUR_IMGURI);
					HAS_IMAGE = true;
				}
				catch(Exception ex)
				{
					Toast.makeText(c, "Problem loading image, please try again.", Toast.LENGTH_SHORT).show();
				}
			}
	    }
		
		// Image selection button action
		imgView.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view) 
			{
				//items for the dialog
				String[] items = new String[] {"Camera","Gallery"};
				//create dialog with onClick listener for the list items
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
			    builder.setTitle(R.string.dlg_tagimg_title)
			           .setItems(items, new DialogInterface.OnClickListener() 
			           {
			        	   public void onClick(DialogInterface dialog, int which) 
			        	   {
			            	   //if camera was clicked, open camera
			            	   if (which == 0)
			            	   {
			            		   openCamera();
			            	   }
			            	   //if gallery was clicked, open gallery. 
			            	   if (which == 1)
			            	   {
			            		   openGallery();
			            	   }
			        	   }
			           });
				builder.show();
			}
		});
		
		// Add button action
		btnOk.setOnClickListener(new OnClickListener() 
		{
			private int flag;

			public void onClick(View view0) 
			{
				Log.i(LOGTAG, "You reached add tag");
				String url = "";
				GeoLocation geo = new GeoLocation(0, 0);
				
				//if the user wants to use GPS coordinates, get the current location to store in tag
				if(chkGPS.isChecked())
				{
					if(location == null)
					{
						Toast.makeText(c, "Acquiring signal, please wait", Toast.LENGTH_SHORT).show();
						onClick(view0); //recursive call if location cannot be found
					}
					else
					{
						geo.setLatitude(location.getLatitude());
						geo.setLongitude(location.getLongitude());
					}
				}
				else
				{
					location = null;
				}
				
				//create a new tag from the given info
				String name = txtName.getText().toString();
				if (!name.isEmpty())
				{
					if(geo.getLatitude() != 0 || geo.getLongitude() != 0) //there is a location
					{
						Tag t = new Tag(name, txtDesc.getText().toString(), url, txtLoc.getText().toString(),
								Constants.CATEGORY_DEFAULT, Constants.RATING_DEFAULT, UserSession.CURRENT_USER,
								geo, Constants.VISIBILITY_FULL);	
												
						//attempt to add tag to db in an async task
						new AddTagTask(c).execute(t);
					}
					else //no location added to the tag
					{
						Tag t = new Tag(name, txtDesc.getText().toString(), url, txtLoc.getText().toString(),
								Constants.CATEGORY_DEFAULT, Constants.RATING_DEFAULT, UserSession.CURRENT_USER,
								null, Constants.VISIBILITY_FULL);
						
						//attempt to add tag to db in an async task
						new AddTagTask(c).execute(t);		
						
						if(flag == 1)
						{
							AH.addTagToAdventure(adventure.getId(), t.getId());
							adventure.addTag(t);
						}
						else if(flag == 2)
						{
							adventure.addStoreTagList(t);
						}

					}
					

			
					//will only reach here if something goes wrong adding tag
					//if so, re enable button
					btnOk.setEnabled(true);
					btnCancel.setEnabled(true);
				}
				else
				{
					Toast t = Toast.makeText(c, "The tag needs a name!", Toast.LENGTH_SHORT);
					t.show();
					btnOk.setEnabled(true);
					btnCancel.setEnabled(true);
				}
				
				//close the view
			}
		});
		
		//Return to previous activity if cancel is clicked
		btnCancel.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View view0) 
			{
				//finish();	
				Intent i = new Intent(getBaseContext(), HomeActivity.class);
				//pass the ID of the current user to ViewTag activity to load their tags 
				i.putExtra("id", UserSession.CURRENTUSER_ID);
				startActivity(i);
			}
		});
		
		//show context menu to delete image on long press of imgView
		imgView.setOnLongClickListener(new OnLongClickListener() 
		{	
		    @Override
		    public boolean onLongClick(View v) 
		    {
		    	openContextMenu(imgView);
		        return true;
		    }
		});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
	    super.onSaveInstanceState(outState);
	    if (CUR_IMGURI != null)
	    	outState.putString("imageUri", CUR_IMGURI.toString());
	}
	
	/*
	 * When the image is selected in the gallery, show it in the ImageView
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
        if (resultCode == RESULT_OK)
        {
        	//Set the layout of the image view to allow the image to expand up to max size
        	LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
        	                        LayoutParams.WRAP_CONTENT);
        	layout.gravity = Gravity.CENTER_HORIZONTAL;
        	layout.leftMargin = R.dimen.Scrollview_Leftmargin;
        	layout.rightMargin = R.dimen.Scrollview_Leftmargin;
        	
        	switch(requestCode)
            {
        		//if image is selected from gallery, show it in the image view
        		case Constants.SELECT_IMG:
        			Uri selectedImageUri = data.getData();
                
        			//update current image file from uri
        			String realImgPath = imageHandler.getRealPathFromURI(selectedImageUri);
        			CUR_IMGURI = Uri.parse(realImgPath);
        			CURRENT_IMAGE = new File(CUR_IMGURI.getPath());
				
        			//display image in imageview
        			imgView.setLayoutParams(layout); 
        			imgView.setImageURI(CUR_IMGURI);
        			HAS_IMAGE = true;
        			break;
                //if new picture is taken, show that in the image view
        		case Constants.CAPTURE_IMG:
        			//if the image was saved to the device use the URI to populate image view and set the current image
        			if (TMP_IMGURI != null)
        			{        				
        				CUR_IMGURI = TMP_IMGURI;
        				CURRENT_IMAGE = new File(CUR_IMGURI.getPath());
        				TMP_IMGURI = null;
        				
        				imgView.setLayoutParams(layout);
        				imgView.setImageURI(CUR_IMGURI);
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
	 * Creates the menu where the user can logout or return to the home screen
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//create logout menu item with ID = 1
		menu.add(1, 1, 1, "Logout");
		menu.add(1, 2, 2, "Home");
		return true;
	}
	
	/*
	 * Handles the event of a user selecting an item from the options menu
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
			case 2:
				Intent homeIntent = new Intent(getBaseContext(), HomeActivity.class); 
				startActivity(homeIntent);
				finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * Defines the context menu for when an image view is long pressed
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		if (v.getId() == R.id.addtag_imgView) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("Tag Image");
			menu.add(Menu.NONE, CONTEXT_DELETE_ID, Menu.NONE, "Clear");
		}
	}
	
	/*
	 * Context handler for deleting an image on long press
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			//if the user deletes the image, set the flag to false,
			//reset the imageview size and image to default
			case CONTEXT_DELETE_ID:
				if(HAS_IMAGE == true)
				{
					HAS_IMAGE = false;
					CURRENT_IMAGE = null;
					CUR_IMGURI = null;
					//reset default layout/image
					LayoutParams defaultParams =  (LayoutParams) imgView.getLayoutParams();
					defaultParams.width = 100;
					defaultParams.gravity = Gravity.CENTER_HORIZONTAL;
	        	
					imgView.setLayoutParams(defaultParams);
					imgView.setImageResource(R.drawable.icon);
				}	
				break;
		}	
		return true;	
	}
	
	/*
	 * What to do when the activity is destroyed
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.d("Orientation Change", "Destroying bitmap");
		ImageView imageView = (ImageView) findViewById(R.id.addtag_imgView);
		BitmapDrawable bd = (BitmapDrawable)imageView.getDrawable();
		bd.getBitmap().recycle();
		imageView.setImageBitmap(null);
	}
	
	/*
	 * ASYNC TASK
	 */
	
	/*
	 * This class extends AsyncTask and provides the methods to add a tag via an
	 * asynchronous task
	 */
	class AddTagTask extends AsyncTask<Tag, Void, JSONObject> 
	{
		ProgressDialog progressDialog;
		Context c;
		//get context from the parent activity for opening dialogs
		public AddTagTask(Context context)
		{
			this.c = context;		
		}
		
		//Setup progress dialog before execution
		@Override
		public void onPreExecute() 
		{
			progressDialog = new ProgressDialog(c);
			progressDialog.setMessage("Creating tag...");
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
					//set the loggedin flag to true, and open the Home page
					if (returnCode != null)
					{
						if (Integer.parseInt(returnCode) == 1)
						{
								String msg = "Tag added!";
								Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
								// return to home screen
								Intent i = new Intent(getBaseContext(), HomeActivity.class);
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
						Log.d("AddTagPostExecute", "Null response, Logon Error.");
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
		protected JSONObject doInBackground(Tag... tags) 
		{
			Tag t = tags[0];
			
			// attempt to add tag
			TagHandler handler = new TagHandler();
			JSONObject response;
			try 
			{
				//if there is an image, first try to upload it
				if(CURRENT_IMAGE != null)
				{
					String url = uploadImage(CURRENT_IMAGE);
					if(url != null)//if upload is successful, set the tag imgurl to the url
						t.setImageUrl(url);
				}
				//add tag to db
				response = handler.AddTag(t);
				Log.d("AddTagTask", "Got response, returncode = " + response.getString(Constants.SUCCESS));
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
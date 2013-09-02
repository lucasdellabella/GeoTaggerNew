package com.hci.geotagger.activities;

import java.io.File;

import org.json.JSONObject;

import com.hci.geotagger.R;
import com.hci.geotagger.R.layout;
import com.hci.geotagger.R.menu;
import com.hci.geotagger.Objects.UserAccount;
import com.hci.geotagger.common.BaseActivity;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.UserSession;
import com.hci.geotagger.connectors.AccountHandler;
import com.hci.geotagger.connectors.ImageHandler;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class EditProfileActivity extends BaseActivity {
	private EditText locTxt, descTxt, quoteTxt;
	private Button saveBtn, cancelBtn;
	private ProgressDialog PD;
	private ImageView imgView;
	private AccountHandler accountHandler;
	private ImageHandler imageHandler;
	
	private boolean HAS_IMAGE = false, IMG_CHANGED = false;
	private File CURRENT_IMAGE, TEMP_IMAGE;
	final File uploadImg = null;
	private Uri CUR_IMGURI, TMP_IMGURI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		accountHandler = new AccountHandler();
		imageHandler = new ImageHandler(EditProfileActivity.this);
		
		locTxt = (EditText) findViewById(R.id.editprofile_location);
		descTxt = (EditText) findViewById(R.id.editprofile_desc);
		quoteTxt= (EditText) findViewById(R.id.editprofile_quote);
		
		saveBtn = (Button) findViewById(R.id.editprofile_saveBtn);
		cancelBtn = (Button) findViewById(R.id.editprofile_cancelBtn);
		
		imgView = (ImageView) findViewById(R.id.editprofile_imageview);
		registerForContextMenu(imgView);
		
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
					Toast.makeText(EditProfileActivity.this, "Problem loading image, please try again.", Toast.LENGTH_SHORT).show();
				}
			}
	    }
		
		loadCurrentProfile();
		
		// go to add tags menu when add button is clicked
		saveBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view0) {
				saveChanges();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view0) {
				finish();
			}
		});
		
		// Image selection button action
		imgView.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// items for the dialog
				String[] items = new String[] { "Camera", "Gallery" };
				// create dialog with onClick listener for the list items
				AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
				builder.setTitle("Choose Profile Picture").setItems(items,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// if camera was clicked, open camera
								if (which == 0) {
									openCamera();
								}
								// if gallery was clicked, open gallery.
								if (which == 1) {
									openGallery();
								}
							}
						});
				builder.show();
			}
		});
		//show context menu to delete image on long press of imgView
		imgView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				openContextMenu(imgView);
				return true;
			}
		});
	}
	
	// define context menu for when image view is long-pressed
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.editprofile_imageview) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("Profile Picture");
			menu.add(1, 1, 1, "Clear");
		}
	}
	
	//Context handler for deleting image on long press
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		//if the user deletes the image, set the flag to false,
		//reset the imageview size and image to default
		case 1:
			if(HAS_IMAGE == true)
			{
				HAS_IMAGE = false;
				CURRENT_IMAGE = null;
				CUR_IMGURI = null;
				imgView.setImageResource(R.drawable.ic_addphoto);
			}
			break;
		}	
		return true;	
	}

	//when the image is captured or selected in the gallery, show it in the image view
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
					IMG_CHANGED = true;
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
						IMG_CHANGED = true;
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (CUR_IMGURI != null)
			outState.putString("imageUri", CUR_IMGURI.toString());
	}

	/*
	 * Functions
	 */
	//load current profile data into editable fields
	private void loadCurrentProfile() {
		//if the user has a profile image, load it to the image view
		String imageUrl = UserSession.CURRENT_USER.getImage();
		if((!imageUrl.equalsIgnoreCase("null")) && (!imageUrl.isEmpty()))
		{
			loadImage(imageUrl);
			HAS_IMAGE = true; 
		}
		String str_default = "";
		// set strings to default value if they are not set (to prevent
		// displaying 'null')
		String quote = (UserSession.CURRENT_USER.getQuote()
				.equalsIgnoreCase("null")) ? str_default
				: UserSession.CURRENT_USER.getQuote();
		String loc = (UserSession.CURRENT_USER.getLocation()
				.equalsIgnoreCase("null")) ? str_default
				: UserSession.CURRENT_USER.getLocation();
		String desc = (UserSession.CURRENT_USER.getDescription()
				.equalsIgnoreCase("null")) ? str_default
				: UserSession.CURRENT_USER.getDescription();

		locTxt.setText(loc);
		descTxt.setText(desc);
		quoteTxt.setText(quote);
	}
	
	// load the tag's image from the url into the image view
	private void loadImage(String imgUrl) {
		final String url = imgUrl;
		// retrieve the image in separate thread
		Runnable loadImage = new Runnable() {
			@Override
			public void run() {
				ImageHandler handler = new ImageHandler();
				// get a scaled version of the image so we don't load the full
				// size unnecessarily
				final Bitmap b = handler.getScaledBitmapFromUrl(url,
						R.dimen.image_width, R.dimen.image_height);
				// if the image gets returned, set it in the image view
				if (b != null) {
					runOnUiThread(new Runnable() {
						public void run() {
							imgView.setImageBitmap(b);
						}
					});
				}
			}
		};
		Thread thread = new Thread(null, loadImage, "LoadImageThread");
		thread.start();
	}
	private void saveChanges()
	{
		// editProfile(int uId, String imgUrl, String description, String
		// location, String quote)
		Runnable SaveChanges = new Runnable() {
			@Override
			public void run() {
				String url = UserSession.CURRENT_USER.getImage();
				//only upload a new image if the image was changed
				if(CURRENT_IMAGE != null && IMG_CHANGED)
				{
					url = uploadImage(CURRENT_IMAGE);
				}else if(CURRENT_IMAGE == null)
				{
					url = "";
				}
				boolean success = accountHandler.editProfile(UserSession.CURRENTUSER_ID, url, 
						descTxt.getText().toString(), locTxt.getText().toString(), 
						quoteTxt.getText().toString());
				if(success)
				{
					//if edit successful, update the currentuser object
					JSONObject json = accountHandler.getUser(UserSession.CURRENT_USER.getuName());
					UserAccount account = accountHandler.CreateAccountFromJSON(json);
					UserSession.CURRENT_USER = account;
				}
					runOnUiThread(new Runnable() {
						public void run() {
							PD.dismiss();
							Intent returnIntent = getIntent();
							setResult(RESULT_OK, returnIntent);        
							finish();
						}
					});
			}
		};
		Thread thread = new Thread(null, SaveChanges, "SaveChangesThread");
		thread.start();
		PD = ProgressDialog.show(EditProfileActivity.this, "Please Wait",
				"Saving Changes...", true);
	}

	/*
	 * Camera Functions
	 */
	// open the camera to allow user to take a picture
	private void openCamera() {
		Intent i_Cam = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// create a file to save an image in
		File f = imageHandler.makeImageFile();
		if (f != null) {
			TMP_IMGURI = Uri.fromFile(f);
			// if file was created, pass the URI to the camera app
			i_Cam.putExtra(MediaStore.EXTRA_OUTPUT, TMP_IMGURI);
		}
		// open camera to take pic when camera button is clicked
		startActivityForResult(i_Cam, Constants.CAPTURE_IMG);
	}

	// open the device's gallery to allow the user to select an image
	private void openGallery() {
		Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(i, "Select Picture"),
				Constants.SELECT_IMG);
	}
	
	//upload an image to the server and set the url
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
				Log.d("EditProfile uploadImage", "Got response, img url = " + url);
				return url;
			}
			else
			{
				return null;
			}
			
		}

}

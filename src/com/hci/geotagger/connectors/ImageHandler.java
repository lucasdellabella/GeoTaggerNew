/*
 * ImageHandler class takes care of actions relating to images
 * such as hosting them on the webserver and retreiving them
 * when needed.
 * 
 * Chris Loeschorn
 * Spring 2013
 */
package com.hci.geotagger.connectors;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.hci.geotagger.common.Constants;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

public class ImageHandler {
	JSONParser jsonParser;
	Context c;
	public ImageHandler()
	{
		jsonParser = new JSONParser();
	}
	public ImageHandler(Context context)
	{
		this.c = context;
		jsonParser = new JSONParser();
	}
	
	//@Return image url
	public String UploadImageToServer(Bitmap b)
	{
		//encode image to base64
		String encodedImg = EncodeImage(b);
		
		  // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("operation", Constants.OP_UPLOAD_IMG));
        params.add(new BasicNameValuePair("imageString", encodedImg));
		try
		{
			JSONObject response = jsonParser.getJSONFromUrl(Constants.IMAGE_URL, params);
			System.out.println("JSON Response from PHP: " + response.toString());
			
			String returnCode = response.getString(Constants.SUCCESS);
			//if the upload was successful, return image url
			if (returnCode != null)
			{
				if (Integer.parseInt(returnCode) == 1)
				{
					String imgUrl = response.getString("url");
					return imgUrl;	
				}	
			}
			else 
			{
				//if there was a problem, return null
				Log.d("ImageUpload", "Problem uploading image.");
				return null;
			}
		}
		catch (Exception ex)
		{
			Log.d("ImageHandler Upload", "Exception occurred during upload, returning null. ");
			ex.printStackTrace();
			return null;
		}
		
		return null;
	}
	//return image as base64 encoded string
	public String EncodeImage(Bitmap b)
	{
		// Compress the image to JPEG to make size smaller,
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// store bytes in output stream
		b.compress(Bitmap.CompressFormat.PNG, Constants.IMAGE_QUALITY, os);
		// encode image bytes to base 64
		byte[] bytes = os.toByteArray();
		String encodedImg = Base64.encodeToString(bytes, Base64.DEFAULT);
		
		return encodedImg;
	}
	//calculate the closest value to desired image size for inSampleSize option
	//this allows scaling of bitmaps before they are downloaded to save memory
	public int getSampleSize(BitmapFactory.Options o, int newWidth, int newHeight)
	{
		//get image width and height
		int sampleSize = 1;
		int imgWidth = o.outWidth;
		int imgHeight = o.outHeight;
		
		
		if(imgHeight > newHeight || imgWidth > newWidth)
		{
			if (imgWidth > imgHeight)
				sampleSize = Math.round((float) imgHeight / (float) newHeight);
			else
				sampleSize = Math.round((float) imgWidth / (float) newWidth);
		}
		
		return sampleSize;
	}
	
	//retrieve a bitmap from the given URL, scaled to fit the given max dimensions.
	public Bitmap getScaledBitmapFromUrl(String imgUrl, int maxWidth, int maxHeight)
	{
		
		try {
			//create a url for the image url
			URL url = new URL(imgUrl);
			//options for decoding the image
			BitmapFactory.Options o = new BitmapFactory.Options();
			//get the image dimensions (without the data) to calcualte samplesize
			o.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o);
		    o.inSampleSize = getSampleSize(o, maxWidth, maxHeight);
		    //reset options to decode whole image
		    o.inJustDecodeBounds = false;
		    Bitmap pic = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o);
		    return pic;
		} 
		catch (MalformedURLException e) {
			Log.d("ImageHandler GetScaledBitmap", "Error parsing image URL.");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("ImageHandler GetScaledBitmap", "Error decoding bitmap from URL.");
			e.printStackTrace();
		}
		
		return null;
	}
	
	// get absolute image path from MediaStore URI for gallery images
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = c.getContentResolver().query(contentUri, proj, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	// Return the path of the directory to store Images on the device (as a file)
	public File getImageAlbum() {
		File imageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				Constants.ALBUM_NAME);

		// create the dir tree if it does not exist
		if (!imageDir.exists()) {
			if (imageDir.mkdirs())
				return imageDir;
			else
				return null;
		}
		return imageDir;
	}
	
	//Make a file to save the captured image to the device
	public File makeImageFile() {
	    // Create an image file name
	    String timeStamp = 
	        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = Constants.IMAGE_PREFIX + timeStamp + Constants.IMAGE_EXT;
	    File image;

		if(getImageAlbum() != null)
		{
			image = new File(getImageAlbum(), imageFileName);
			return image;
		}
	    return null;   
	}
	
}

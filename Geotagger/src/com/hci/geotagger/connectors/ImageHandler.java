/**
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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.hci.geotagger.cache.CacheHandler;
import com.hci.geotagger.common.Constants;
import com.hci.geotagger.common.NetworkUtils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class ImageHandler {
	JSONParser jsonParser;
	Context c;
	CacheHandler cache;
	private static final String TAG = "ImageHandler";
	
	public ImageHandler(Context context)
	{
		this.c = context;
		jsonParser = new JSONParser();
		cache = new CacheHandler(context);
	}
	
	//@Return image url
	public ReturnInfo uploadImageToServer(Bitmap b)
	{
		ReturnInfo retValue = null;
		Log.d(TAG, "Entering uploadImageToServer");

		//encode image to base64
		String encodedImg = EncodeImage(b);
		JSONObject response = null;
		String returnCode = null;
			
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("operation", Constants.OP_UPLOAD_IMG));
		params.add(new BasicNameValuePair("imageString", encodedImg));
	        
		// perform cached actions before this action, also returns false if network is down
		if (cache.performCachedActions()) {
			try
			{
				response = jsonParser.getJSONFromUrl(Constants.IMAGE_URL, params);
				Log.d(TAG,"JSON Response from PHP: " + response.toString());
				
				returnCode = response.getString(Constants.SUCCESS);
				//if the upload was successful, return image url
				if (returnCode != null)
				{
					if (Integer.parseInt(returnCode) == 1)
					{
						retValue = new ReturnInfo();
						retValue.url = response.getString("url");
					} else {
						retValue = new ReturnInfo(ReturnInfo.FAIL_GENERAL);
					}
				}
				else 
				{
					//if there was a problem, return null
					Log.d(TAG, "UploadImageToServer: Problem uploading image.");
//					Toast.makeText(c, "Couldn't upload image!", Toast.LENGTH_SHORT).show();
					retValue = new ReturnInfo(ReturnInfo.FAIL_GENERAL);
				}
			}
			catch (Exception ex)
			{
				Log.e(TAG, "UploadImageToServer: Exception occurred during upload, returning null. ");
//				Toast.makeText(c, "Couldn't upload image!", Toast.LENGTH_SHORT).show();
				ex.printStackTrace();
				retValue = new ReturnInfo(ReturnInfo.FAIL_UPLOAD);
			}
		} else {
			String url = "";
			
			// Add the bitmap image to the cache, and use the temporary key returned
			url = cache.add2Cache(b);

			if (url != null) {
				retValue = new ReturnInfo();
				retValue.url = url;
				retValue.detail = ReturnInfo.FAIL_NONETWORK;

				// Create post commands for the Action cache record.
		        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		        postParams.add(new BasicNameValuePair(CacheHandler.ACTION_OP_POSTID, CacheHandler.ACTION_UPDATE_POSTOP));
		        postParams.add(new BasicNameValuePair(CacheHandler.ACTION_URL_POSTID, url));
				
				// TODO: add the AddTag request to the cached list of DB transactions
		        // TODO: saving the entire image in the params list is really big, need to reference and use the cached file
//				cache.cacheAction(Constants.IMAGE_URL, params, postParams);
			} else {
				retValue = new ReturnInfo(ReturnInfo.FAIL_NONETWORK);
			}
		}
		
		Log.d(TAG, "Leaving uploadImageToServer");
		return retValue;
	}
	
	//return image as base64 encoded string
	private String EncodeImage(Bitmap b)
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
	private int getSampleSize(BitmapFactory.Options o, int newWidth, int newHeight)
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
		Bitmap pic = null;
		Log.d(TAG, "Entering getScaledBitmapFromUrl");

		if (imgUrl == null || imgUrl.length() == 0) {
			Log.d(TAG, "Leaving getScaledBitmapFromUrl");
			return null;
		}
		
		try {
			//create a url for the image url
			URL url = new URL(imgUrl);
			InputStream stream;
			
			// If there is not cache then get the image from the server
			if (cache == null) {
				// If the Network is UP then upload
				if (NetworkUtils.isNetworkUp(c)) {
					//options for decoding the image
					BitmapFactory.Options o = new BitmapFactory.Options();
					//get the image dimensions (without the data) to calcualte samplesize
					o.inJustDecodeBounds = true;
				    BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o);
				    o.inSampleSize = getSampleSize(o, maxWidth, maxHeight);
				    //reset options to decode whole image
				    o.inJustDecodeBounds = false;
				    pic = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o);
				}
			} else {
//				String imageCacheKey = url.toString() + "." + maxWidth + "." + maxHeight;
				String imageCacheKey = url.toString();
				
				// If the image exists in the cache then decode and use it
				if (cache.imageExists(imageCacheKey)) {
					// TODO: need to compare image version number with that on the server
					
					BitmapFactory.Options o = new BitmapFactory.Options();
				    o.inSampleSize = getSampleSize(o, maxWidth, maxHeight);
				    o.inJustDecodeBounds = false;

					pic = cache.decodeCacheBitmap(imageCacheKey, o);
				} else {
					// If the URL is NOT cached then get the URL stream and cache it
					if (NetworkUtils.isNetworkUp(c)) {
						// Get the default sized image from the server
						Bitmap serverpic = BitmapFactory.decodeStream(url.openConnection().getInputStream());
						cache.add2Cache(imageCacheKey, serverpic);

						BitmapFactory.Options o = new BitmapFactory.Options();
					    o.inSampleSize = getSampleSize(o, maxWidth, maxHeight);
					    o.inJustDecodeBounds = false;

						pic = cache.decodeCacheBitmap(imageCacheKey, o);

						/*
						//options for decoding the image
						BitmapFactory.Options o = new BitmapFactory.Options();
						//get the image dimensions (without the data) to calcualte samplesize
						o.inJustDecodeBounds = true;
					    BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o);
	
					    o.inSampleSize = getSampleSize(o, maxWidth, maxHeight);
					    //reset options to decode whole image
					    o.inJustDecodeBounds = false;
					    
					    pic = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o);
						*/
					}
					
				}
				
				/*
				if (stream != null) {
					//options for decoding the image
					BitmapFactory.Options o = new BitmapFactory.Options();
					//get the image dimensions (without the data) to calcualte samplesize
					o.inJustDecodeBounds = true;
				    BitmapFactory.decodeStream(stream, null, o);
				    o.inSampleSize = getSampleSize(o, maxWidth, maxHeight);
				    //reset options to decode whole image
				    o.inJustDecodeBounds = false;
				    
				    pic = BitmapFactory.decodeStream(cache.getCacheInputStream(imageCacheKey), null, o);
				} else {
					//options for decoding the image
					BitmapFactory.Options o = new BitmapFactory.Options();
					//get the image dimensions (without the data) to calcualte samplesize
					o.inJustDecodeBounds = true;
				    BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o);
				    o.inSampleSize = getSampleSize(o, maxWidth, maxHeight);
				    //reset options to decode whole image
				    o.inJustDecodeBounds = false;
				    pic = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o);
				}
				*/
			}
		} 
		catch (MalformedURLException e) {
			Log.e(TAG, "getScaledBitmapFromUrl: Error parsing image URL.");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "getScaledBitmapFromUrl: Error decoding bitmap from URL.");
			e.printStackTrace();
		}
		
		Log.d(TAG, "Leaving getScaledBitmapFromUrl");
		return pic;
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
	
	//added by Kale, works on newer android devices
	public  String getFileNameByUri(Context context, Uri uri)
	{
		 Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		   cursor.moveToFirst();
		   String document_id = cursor.getString(0);
		   document_id = document_id.substring(document_id.lastIndexOf(":")+1);
		   cursor.close();

		   cursor = context.getContentResolver().query( 
		   android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		   null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
		   cursor.moveToFirst();
		   String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		   cursor.close();
		   
		   	return path;
	}

	// Return the path of the directory to store Images on the device (as a file)
	public File getImageAlbum() {
		Log.d(TAG, "Entering getImageAlbum");

		File imageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				Constants.ALBUM_NAME);

		// create the dir tree if it does not exist
		if (!imageDir.exists()) {
			if (! imageDir.mkdirs())
				imageDir = null;
		}

		Log.d(TAG, "Leaving getImageAlbum");
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

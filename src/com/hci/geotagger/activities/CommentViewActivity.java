package com.hci.geotagger.activities;

import com.hci.geotagger.R;
import com.hci.geotagger.connectors.ImageHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * This class implements the extended view of the comments and allows the user to 
 * click on a comment in the TagViewActivity and bring up this activity. This activity
 * will enlarge the photo from the comment drawer and will also enlarge the comment 
 * string itself.
 * 
 * @author: Spencer Kordecki
 * @date: 10/23/13
 */

public class CommentViewActivity extends Activity
{
	private TextView tagComment;	//default comment
	private ImageView tagPicture;	//picture attached to the comment
	private Intent intent;			//intent used for getting extras
	private String commentText;		//text of the comment
	private String imgURL;			//the url of the comment image
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_comment_extended_view);
		
		intent = getIntent();
		
		tagComment = (TextView) findViewById(R.id.commentExtended_commentTag);
		
		commentText = intent.getStringExtra("commentText");
		imgURL = intent.getStringExtra("imgURL");
		
		if(!commentText.equals(""))
		{			
			Log.d("Before", commentText);
			tagComment.setText(intent.getStringExtra("commentText"));
			Log.d("After", tagComment.getText().toString());
		}
		
		if(!imgURL.equals("")) //if there is a comment image, set it.
		{
			new DownloadImage().execute(imgURL);			
		}
	}
	
	/*
	 * Async class used to download the image of the comment and populate the ImageView
	 */
	private class DownloadImage extends AsyncTask<String, Void, Bitmap>
	{
		ImageHandler handler = new ImageHandler();
		
		@Override
		protected Bitmap doInBackground(String... urls) 
		{
			return handler.getScaledBitmapFromUrl(urls[0], R.dimen.image_width, 
					R.dimen.image_height);
		}
		
		protected void onPostExecute(Bitmap result)
		{
			tagPicture = (ImageView) findViewById(R.id.commentExtended_commentImg);
			tagPicture.setImageBitmap(result);
		}
	}
}
package com.hci.geotagger.activities;

import com.hci.geotagger.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentViewActivity extends Activity
{
	private TextView tagComment;
	private ImageView tagPicture;
	private Intent intent;
	private String commentText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_extended_view);
		
		intent = getIntent();
		
		tagComment = (TextView) findViewById(R.id.commentExtended_commentTag);
		tagPicture = (ImageView) findViewById(R.id.commentExtended_commentImg);
		
		commentText = intent.getStringExtra("commentText");
		
		if(!commentText.equals(""))
		{			
			Log.d("Before", commentText);
			tagComment.setText(intent.getStringExtra("commentText"));
			Log.d("After", tagComment.getText().toString());
		}
	}
}

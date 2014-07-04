//package com.teampterodactyl.fragments;
package com.hci.geotagger.Objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
//import com.teampterodactyl.mobsciinterface.R;
import com.hci.geotagger.R;
import com.hci.geotagger.activities.CommentViewActivity;
import com.hci.geotagger.common.Constants;
import android.annotation.SuppressLint;
import android.app.*;
import com.hci.geotagger.Objects.Comment;
 

/**
 * CommentViewFragment implements the fragment that allows for the viewing of
 * comments in the TagViewActivity. When the user clicks on the Comments button in
 * the TagView, this fragment will be displayed. The comments are displayed in a 
 * ListView and can be clicked on for more information.
 * 
 * @author: Spencer Kordecki
 * @version: January 20, 2014
 */
@SuppressLint("ValidFragment")
public class CommentViewFragment extends SherlockFragment
{
	private View fragmentView; //view that is returned for onCreateView
	private ListView commentList; //ListView to display comments
	//private String[] comments; //a dummy array of comments
	private ArrayList<Comment> comments;

	private Context context; //context
	private CommentAdapter commentAdapter;
	private Intent extendedComment;
	private ICommentViewCallBack callback;

	/*
	 * Creates the view that the user will see when the fragment is created, similar 
	 * to onCreate methods seen in activities.
	 */
	
	public interface ICommentViewCallBack
	{
		public void onCreateCommentViewCallback();

		void onCreateDescriptionViewCallback();
		
	}
	
	public CommentViewFragment(ICommentViewCallBack callback)
	{
		this.callback = callback;
		
	}
	
	public CommentViewFragment()
	{
		
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
    {
        fragmentView = inflater.inflate(R.layout.fragment_comment_view, container, false);
        
        commentList = (ListView) fragmentView.findViewById(R.id.comment_list);
        
        context = this.fragmentView.getContext();  
        
        comments = new ArrayList<Comment>();
        
        commentAdapter = new CommentAdapter(context,com.hci.geotagger.R.layout.commentrow,comments);
        
        commentList.setAdapter(commentAdapter); 
        commentList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id)
			{
				Comment comment = comments.get(position);
				extendedComment = new Intent(context,CommentViewActivity.class);
				
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
        
        //now that view is created, we can start retrieving and populating comments
                
        callback.onCreateCommentViewCallback();
        
        return fragmentView; //view must be returned
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) 
    {
    	super.onSaveInstanceState(outState);
        //outState.putInt("curChoice", mCurCheckPosition);
    };
  
    public void notifyCommentAdapterDataChanged()
    {	
    	this.commentAdapter.notifyDataSetChanged();
    }
    
    public void addComment(Comment c)
    {
    	this.commentAdapter.add(c);
    }
    
    public void setCommentListSelection(int position)
    {
    	this.commentList.setSelection(position);
    }
    
    
    private class CommentAdapter extends ArrayAdapter<Comment> 
	{
    	
		private ArrayList<Comment> comments;
		Context c;
		private HashMap<String, Bitmap> thumbCache;


		/*
		 * Constructor for the CommentAdapter
		 */
		public CommentAdapter(Context context, int textViewResourceId, ArrayList<Comment> comments) 
		{
			super(context, textViewResourceId, comments);
			this.comments = comments;
			this.c = context;
			thumbCache = new HashMap<String, Bitmap>();

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
				LayoutInflater vi = ((Activity)this.c).getLayoutInflater();
						//(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
					commentTxt.setText(comment.getText().toString());


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

}



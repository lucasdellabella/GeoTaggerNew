//package com.teampterodactyl.fragments;
package com.hci.geotagger.Objects;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
//import com.teampterodactyl.mobsciinterface.R;
import com.hci.geotagger.R;

/**
 * DescriptionViewFragment implements the fragment that allows for the viewing of
 * a tag's description in the TagViewActivity. When the user clicks the description 
 * button, this view will be displayed. The description contains the tag's description,
 * the date the tag was created, and the location of the tag.
 * 
 * @author: Spencer Kordecki
 * @version: January 20, 2014
 */
//suppressing so I can override default constructor and avoid setting method for IDescriptionViewFragment
@SuppressLint("ValidFragment")
public class DescriptionViewFragment extends SherlockFragment
{
	private View fragmentView; //view to be returned 
	private IDescriptionViewCallback callback;
	
	/*
	 * Creates the view that the user will see when the fragment is created, similar 
	 * to onCreate methods seen in activities.
	 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
    {
        
    	fragmentView = inflater.inflate(R.layout.fragment_description_view, container, false);
    
    	callback.onCreateDescriptionViewCallback();
    	
    	return fragmentView;
    }
    
    public interface IDescriptionViewCallback
    {
    	public void onCreateDescriptionViewCallback();
    }
    
    public DescriptionViewFragment(IDescriptionViewCallback callback)
    {
    	this.callback = callback;
    }
    
    public DescriptionViewFragment()
    {
    	
    }
   
    
    @Override
    public View getView() 
    {
    	return fragmentView;
    };
    
}

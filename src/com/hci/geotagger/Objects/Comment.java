package com.hci.geotagger.Objects;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id, parentTagId;
	private String text, Username, imageUrl;
	private Date createdDateTime;
	
	/*
	 * Standard comment constructor
	 */
	public Comment(long cID, long tagID, String txt, String uName, Date ts)
	{
		this.setId(cID);
		this.setParentTagId(tagID);
		this.setText(txt);
		this.setUsername(uName);
		this.setCreatedDateTime(ts);
	}
	
	/*
	 * Comment constructor for use with images
	 */
	public Comment(long cID, long tagID, String txt, String uName, Date ts, String imgUrl)
	{
		this.setId(cID);
		this.setParentTagId(tagID);
		this.setText(txt);
		this.setUsername(uName);
		this.setCreatedDateTime(ts);
		this.setImageURL(imgUrl);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentTagId() {
		return parentTagId;
	}

	public void setParentTagId(long parentTagId) {
		this.parentTagId = parentTagId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	
	public void setImageURL(String imgURL) {
		this.imageUrl = imgURL;
	}

	public String getImageURL() {
		return imageUrl;
	}
}

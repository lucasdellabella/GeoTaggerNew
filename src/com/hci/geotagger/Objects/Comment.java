package com.hci.geotagger.Objects;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id, parentTagId;
	private String text, Username;
	private Date createdDateTime;
	
	public Comment(long cID, long tagID, String txt, String uName, Date ts)
	{
		this.setId(cID);
		this.setParentTagId(tagID);
		this.setText(txt);
		this.setUsername(uName);
		this.setCreatedDateTime(ts);
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

}

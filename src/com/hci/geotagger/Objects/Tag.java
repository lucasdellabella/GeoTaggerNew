package com.hci.geotagger.Objects;

import java.io.Serializable;
import java.util.Date;

public class Tag implements Serializable {

	private long id;
	private String name, description, imageUrl, locationString, category, ownerName;
	private int ratingScore, visibility, ownerId;
	private UserAccount owner;
	private GeoLocation location;
	private Date createdDateTime;
	
	//constructor for if a User Account is available to associate with the tag
	//this is also used when a tag is first created locally and the id is not available
	public Tag(String name, String desc, String url, String locStr, String cat,
			int rating, UserAccount own, GeoLocation loc, int vis)
	{
		this.setName(name);
		this.setDescription(desc);
		this.setImageUrl(url);
		this.setLocationString(locStr);
		this.setCategory(cat);
		this.setRatingScore(rating);
		this.setOwner(own);
		this.setLocation(loc);
		this.setVisibility(vis);
		
	}
	
	//use this when getting tag object from db and ID is accessible. Also uses values for
	//owner id and name so a user account object does not need to be created at this time.
	public Tag(long tId, String name, String desc, String url, String locStr, String cat,
			int rating, int oId, String oName, GeoLocation loc, int vis, Date ts)
	{
		this.setId(tId);
		this.setName(name);
		this.setDescription(desc);
		this.setImageUrl(url);
		this.setLocationString(locStr);
		this.setCategory(cat);
		this.setRatingScore(rating);
		this.setOwnerId(oId);
		this.setOwnerName(oName);
		this.setLocation(loc);
		this.setVisibility(vis);
		this.createdDateTime = ts;
		
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLocationString() {
		return locationString;
	}

	public void setLocationString(String locationString) {
		this.locationString = locationString;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getRatingScore() {
		return ratingScore;
	}

	public void setRatingScore(int ratingScore) {
		this.ratingScore = ratingScore;
	}

	public UserAccount getOwner() {
		return owner;
	}

	public void setOwner(UserAccount owner) {
		this.owner = owner;
	}

	public GeoLocation getLocation() {
		return location;
	}

	public void setLocation(GeoLocation location) {
		this.location = location;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}

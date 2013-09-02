package com.hci.geotagger.Objects;

import java.io.Serializable;
import java.util.Date;
/*
 * Stores a user account object that is taken from the database
 */
public class UserAccount implements Serializable {

	private int id, Type, Visibility, UserRating;
	private String uName, Name, Email, Password, Description, Location, Quote;
	private String Image;
	private Date createdDateTime;
	//empty constructor for test objects
	public UserAccount(){};
	//init only required fields
	public UserAccount(int aID, String userName, String pw, int type, int vis, Date ts )
	{
		this.id = aID;
		this.uName = userName;
		this.setPass(pw);
		this.setType(type);
		this.setVisibility(vis);
		this.setCreatedDateTime(ts);
		
	}
	//init only required fields
	public UserAccount(int aID, String userName, String email, String pw, String img,
			String desc, String loc, String quote, int type, int vis, Date ts, int rating )
	{
		this.id = aID;
		this.uName = userName;
		this.setEmail(email);
		this.setPass(pw);
		this.setImage(img);
		this.setDescription(desc);
		this.setLocation(loc);
		this.setQuote(quote);
		this.setType(type);
		this.setVisibility(vis);
		this.setCreatedDateTime(ts);
		this.setUserRating(rating);
	}
	public UserAccount(int aID, String userName, String pw)
	{
		this.id = aID;
		this.uName = userName;
		this.setPass(pw);		
	}
	//init all fields
	public int getId() {
		return id;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getPass() {
		return Password;
	}
	public void setPass(String password) {
		Password = password;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public int getVisibility() {
		return Visibility;
	}
	public void setVisibility(int visibility) {
		Visibility = visibility;
	}
	public int getUserRating() {
		return UserRating;
	}
	public void setUserRating(int userRating) {
		UserRating = userRating;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getQuote() {
		return Quote;
	}
	public void setQuote(String quote) {
		Quote = quote;
	}

	
}

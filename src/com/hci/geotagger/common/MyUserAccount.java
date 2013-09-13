package com.hci.geotagger.common;

import java.util.Date;

import com.hci.geotagger.Objects.UserAccount;

public class MyUserAccount extends UserAccount {
	private String Password;
	
	public MyUserAccount(int aID, String userName, String pw, int type, int vis, Date ts )
	{
		super(aID, userName, type, vis, ts);
		this.setPass(pw);	
		
	}
	//init only required fields
	public MyUserAccount(int aID, String userName, String email, String pw, String img,
			String desc, String loc, String quote, int type, int vis, Date ts, int rating )
	{
		super(aID, userName, email, img, desc, loc, quote, type, vis, ts, rating);
		this.setPass(pw);	
	}
	public MyUserAccount(int aID, String userName, String pw)
	{
		super(aID, userName);
		this.setPass(pw);		
	}

	public String getPass() {
		return Password;
	}
	public void setPass(String password) {
		Password = password;
	}
}

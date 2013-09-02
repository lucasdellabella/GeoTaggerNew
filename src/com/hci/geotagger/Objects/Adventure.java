package com.hci.geotagger.Objects;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

public class Adventure implements Serializable 
{		
	private static final long serialVersionUID = 2L;
	private long id;
	private int visibility, creatorID;
	private String name, description, creatorName;
	private UserAccount creatorAccount;
	private Date createdDateTime;
	private ArrayList<Tag> tagArray;
	private ArrayList<UserAccount> peopleArray;	
	
	/*
	 * Initializes all fields except for creatorAccount.
	 * Used when creating a brand new adventure and id is not available.
	 * 
	 * @param:	All params are fields.
	 */
	public Adventure(int vis, int cID, String newName, String desc, String cName, Date cTime)
	{
		this.setVisibility(vis);
		this.setCreatorID(cID);
		this.setName(newName);
		this.setDescription(desc);
		this.setCreatorName(cName);
		this.createdDateTime = cTime;		
		this.tagArray = new ArrayList<Tag>();
		this.peopleArray = new ArrayList<UserAccount>();
	}
	
	/*
	 * Initializes all fields except for creatorAccount.
	 * 
	 * @param:	All params are fields.
	 */
	public Adventure(long nID, int vis, int cID, String newName, String desc, String cName, Date cTime)
	{
		this.setID(nID);
		this.setVisibility(vis);
		this.setCreatorID(cID);
		this.setName(newName);
		this.setDescription(desc);
		this.setCreatorName(cName);
		this.createdDateTime = cTime;		
		this.tagArray = new ArrayList<Tag>();
		this.peopleArray = new ArrayList<UserAccount>();
	}
	
	//All get and set methods are self-explanatory.
	
	public void setID(long id)
	{
		this.id = id;
	}
	
	public long getID()
	{
		return this.id;
	}
	
	public void setVisibility(int vis)
	{
		this.visibility = vis;
	}
	
	public int getVisibility()
	{
		return this.visibility;
	}
	
	public void setCreatorID(int cID)
	{
		this.creatorID = cID;
	}
	
	public int getCreatorID()
	{
		return this.creatorID;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setDescription(String desc)
	{
		this.description = desc;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setCreatorName(String cName)
	{
		this.creatorName = cName;
	}
	
	public String getCreatorName()
	{
		return this.creatorName;
	}
	
	public void setCreatorAccount(UserAccount cAccount)
	{
		this.creatorAccount = cAccount;
	}
	
	public UserAccount getCreatorAccount()
	{
		return this.creatorAccount;
	}
	
	public void setCreatedDateTime(Date cTime)
	{
		this.createdDateTime = cTime;
	}
	
	public Date getCreatedDateTime()
	{
		return this.createdDateTime;
	}
	
	/**
	 * These methods deal with adding and removing tags from the adventure.	 
	 * NOTE: removeTag requires the prior call of getId on the tag to be removed.
	 */
	
	public boolean addTag(Tag t)
	{
		if(t != null)
		{
			tagArray.add(t);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void removeTag(long tagID)
	{
		for(int i = 0; i < tagArray.size(); i++)
		{
			long ID = tagArray.get(i).getId();
			if(ID == tagID)
			{
				int index = tagArray.indexOf(ID);
				tagArray.remove(index);
			}
		}		
	}
	
	/**
	 * These methods deal with adding and removing people from the adventure.	 
	 * NOTE: removePerson requires the prior call of getId on the person (UserAccount) to be removed.
	 */
	
	public boolean addPerson(UserAccount u)
	{
		if(u != null)
		{
			peopleArray.add(u);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void removePerson(long userID)
	{
		for(int i = 0; i < peopleArray.size(); i++)
		{
			long ID = peopleArray.get(i).getId();
			if(ID == userID)
			{
				int index = peopleArray.indexOf(ID);
				peopleArray.remove(index);
			}
		}		
	}	
}
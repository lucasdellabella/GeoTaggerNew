package com.hci.geotagger.Objects;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

public class Adventure implements Serializable 
{		
	private static final long serialVersionUID = 2L;
	private long id;
	private int visibility, ownerID;
	private String name, description; //, creatorName;
	private UserAccount creatorAccount;
	private Date createdDateTime;
	private ArrayList<Tag> tagArray;
	private ArrayList<UserAccount> peopleArray;	
	private ArrayList<Tag> storeAddTagList, storeRemoveTagList;
	private ArrayList<UserAccount> storeAddUserList, storeRemoveUserList;
	
	/*
	 * Initializes all fields except for creatorAccount.
	 * Used when creating a brand new adventure and id is not available.
	 * 
	 * @param:	All params are fields.
	 */
	public Adventure(int vis, int cID, String newName, 
			String newDescription, /*String cName,*/ Date cTime)
	{
		this.setVisibility(vis);
		this.setCreatorID(cID);	
		this.setName(newName);
		this.setDescription(newDescription);
		//this.setCreatorName(cName);
		this.createdDateTime = cTime;		
		this.tagArray = new ArrayList<Tag>();
		this.peopleArray = new ArrayList<UserAccount>();
		this.storeAddTagList = new ArrayList<Tag>();
		this.storeRemoveTagList = new ArrayList<Tag>();
		this.storeAddUserList = new ArrayList<UserAccount>();
		this.storeRemoveUserList = new ArrayList<UserAccount>();
	}
	
	/*
	 * Initializes all fields except for creatorAccount.
	 * 
	 * @param:	All params are fields.
	 */
	public Adventure(long nID, int vis, int cID, String newName, 
			String desc, /*String cName,*/ Date cTime)
	{
		this.setID(nID);
		this.setVisibility(vis);
		this.setCreatorID(cID);
		this.setName(newName);
		this.setDescription(desc);
		//this.setCreatorName(cName);		
		this.createdDateTime = cTime;		
		this.tagArray = new ArrayList<Tag>();
		this.peopleArray = new ArrayList<UserAccount>();
		this.storeAddTagList = new ArrayList<Tag>();
		this.storeRemoveTagList = new ArrayList<Tag>();
		this.storeAddUserList = new ArrayList<UserAccount>();
		this.storeRemoveUserList = new ArrayList<UserAccount>();
	}
	
	//All get and set methods are self-explanatory.
	
	public void setID(long id)
	{
		this.id = id;
	}
	
	public long getId()
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
		this.ownerID = cID;
	}
	
	public int getCreatorID()
	{
		return this.ownerID;
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
	
	/*public void setCreatorName(String cName)
	{
		this.creatorName = cName;
	}
	
	public String getCreatorName()
	{
		return this.creatorName;
	}*/
	
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
	
	
		public void removePerson64(long userID)
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
	
		public void removePerson128(long userID)
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
	
		public void removePerson256(long userID)
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
	
	/**
	 * These methods deal with storing tags and users that are to be added and/or removed from the adventure.
	 * These methods are used by activities called from AdvEditTagTabActivity, AdvEditPeopleTabActivity and the 
	 * EditAdventureActivity since we want to ensure that all changes made to the Adventure occur only when the user
	 * presses the save or cancel buttons. Otherwise adding and removing tags and users would occur instantaneously 
	 * and we do not want that.  
	 */
	
	public boolean addStoreTagList(Tag t)
	{
		if(t != null)
		{
			storeAddTagList.add(t);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ArrayList<Tag> getStoreAddTagList()
	{
		return storeAddTagList;
	}
	
	public boolean removeStoreTagList(Tag t)
	{
		if(t != null)
		{
			storeRemoveTagList.add(t);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ArrayList<Tag> getStoreRemoveTagList()
	{
		return storeRemoveTagList;
	}
	
	public boolean addStoreUserList(UserAccount u)
	{
		if(u != null)
		{
			storeAddUserList.add(u);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ArrayList<UserAccount> getStoreAddUserList()
	{
		return storeAddUserList;
	}
	
	public boolean removeStoreUserList(UserAccount u)
	{
		if(u != null)
		{
			storeRemoveUserList.add(u);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ArrayList<UserAccount> getStoreRemoveUserList()
	{
		return storeRemoveUserList;
	}
	
	public void emptyStoreTagList(ArrayList<Tag> list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			if(list.size() > 0)
			{
				Tag t = list.get(i);
				t = null;
			}
		}
	}
	
	public void emptyStoreUserList(ArrayList<UserAccount> list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			if(list.size() > 0)
			{
				UserAccount u = list.get(i);
				u = null;
			}
		}
	}
}

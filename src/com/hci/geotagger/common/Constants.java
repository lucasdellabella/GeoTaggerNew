package com.hci.geotagger.common;

public final class Constants {
	//JSON object strings
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String ERROR_MSG = "error_msg";
	
	//Operations
	public static final String OP_LOGIN = "login";
	public static final String OP_LOGIN_ID = "loginById";
	public static final String OP_REGISTER = "register";
	public static final String OP_ADD_TAG = "addTag";
	public static final String OP_UPLOAD_IMG = "uploadImage";
	public static final String OP_GETTAGS_BYID = "getTagsById";
	public static final String OP_GETNAME_FROMID = "getNameFromId";
	public static final String OP_DELETE_TAG = "deleteTag";
	public static final String OP_ADD_FRIEND = "addFriend";
	public static final String OP_GETFRIENDS = "getFriends";
	public static final String OP_GETUSER = "getUser";
	public static final String OP_DELETE_FRIEND = "removeFriend";
	public static final String OP_EDIT_PROFILE = "editProfile";
	public static final String OP_ADD_TAGCOMMENT = "addTagComment";
	public static final String OP_GET_TAGCOMMENTS = "getTagComments";
	public static final String OP_DELETE_TAGCOMMENT = "deleteTagComment";
	
	//Shared preferences settings 
	public static final String LOGIN_DATAFILE = "LoginData";
	public static final int MODE_PRIVATE = 0;
	public static final String KEY_LOGGEDIN = "LoggedIn";
	public static final String KEY_UID= "UserID";
	public static final String KEY_PASS = "Password";
	
	//Login Mode
	public static final int LOGIN_BYID = 0;
	public static final int LOGIN_BYNAME = 1;
	
	//Exception Messages
	public static final String USERNAME_IS_TAKEN = "Requested username has already been taken. Please try a different username.";
	public static final String UNKNOWN_ERROR = "Unknown error occurred. Please try again.";
	
	//URLs for php operations (in case main script is split up in the future)
	/* Local URLs for Debug
	public static final String LOGIN_URL = "http://10.0.2.2/geotagger/index.php";
	public static final String REGISTER_URL = "http://10.0.2.2/geotagger/";
	public static final String TAGOP_URL = "http://10.0.2.2/geotagger/";
	public static final String IMAGE_URL = "http://10.0.2.2/geotagger/";
	*/
	
	//Live URLs for the server
	public static final String BASE_URL = "http://hci.montclair.edu/geotagger/";
	public static final String LOGIN_URL = BASE_URL + "index.php";
	public static final String REGISTER_URL = BASE_URL + "index.php";
	public static final String TAGOP_URL = BASE_URL + "index.php";
	public static final String IMAGE_URL = BASE_URL + "index.php";
	
	//Request codes
	public static final int SELECT_IMG = 1;
	public static final int CAPTURE_IMG = 2;
	
	//Visibility
	public static final int VISIBILITY_PRIVATE = 0;
	public static final int VISIBILITY_FULL = 1;
	public static final int VISIBILITY_LIMITED = 2;
	
	//Default rating
	public static final int RATING_DEFAULT = 0;
	
	//Categories
	public static final String CATEGORY_DEFAULT = "Default";
	
	//Images
	public static final int MAX_IMAGE_HEIGHT = 600;
	public static final int MAX_IMAGE_WIDTH = 600;
	public static final int IMAGE_QUALITY = 90;
	public static final String IMAGE_EXT = ".jpg";
	public static final String IMAGE_PREFIX = "img";
	
	//Image Album Name
	public static final String ALBUM_NAME = "Geotagger Images";
	
	//DateTime format
	public static final String DATETIME_FORMAT = "MM/dd/yy hh:mma";
	public static final String DATE_FORMAT = "MM/dd/yy";
	public static final String TIME_FORMAT = "hh:mma";
	
	//ADDFriend result constants
	public static final int ADDFRIEND_SUCCESS = 1;
	public static final int ADDFRIEND_ALREADYFRIENDS = 1;
	public static final int ADDFRIEND_USERNOTFOUND = 2;
	public static final int ADDFRIEND_ERROR = 3;
	
	//img cache size for tag/friend thumbnails
	public static final int IMG_CACHE_SIZE = 50;
}

<!DOCTYPE html>
<html class="no-js" lang="en"><!--<![endif]-->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>MobSci API</title>

	<meta name="description" content="MobSci API Implementation">
	<meta name="author" content="Zill Christian">
	<link rel="icon" href="img/favicon-navy.png" type="image/x-icon">

	<!-- Mobile -->
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- Font -->
	<link href="css/css" rel="stylesheet" type="text/css">

	<!-- LESS -->
	<link rel="stylesheet" type="text/css" href="css/navy.css">

	<!-- hightlight.js -->
	<script src="js/highlight.min.js"></script>
	<style type="text/css"></style>
	<script>hljs.initHighlightingOnLoad();</script>

	<!-- Navigation -->
	<script src="./js/jquery.min.js"></script>
	<script src="./js/bootstrap.min.js"></script>
	<script src="./js/custom.js"></script>

	<!--[if lt IE 9]>
	<script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
</head>
<body style="">
			<!-- Docs -->
	<a href="https://github.com/spencerkordecki/MobSci" target="_blank" id="github-ribbon"><img src="img/forkme_right_darkblue_121621.png" alt="Fork me on GitHub"></a>
	<div class="container-fluid fluid-height wrapper">
		<div class="navbar navbar-fixed-top">
			<div class="navbar-inner">
				<a class="brand pull-left" href="./">MobSci</a>
				<p class="navbar-text pull-right">API Documentation</p>
			</div>
		</div>

		<div class="row-fluid columns content">
			<div class="left-column article-tree span3">
				<!-- For Mobile -->
				<div class="responsive-collapse">
					<button type="button" class="btn btn-sidebar" id="menu-spinner-button">
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				    </button>
				</div>

				<div id="sub-nav-collapse" class="sub-nav-collapse">
					<!-- Navigation -->
					<ul class="nav nav-list">
						<li class="active"><a href="#gettingStarted">Getting Started</a>
							<ul class="nav nav-list">
								<li><a href="#vOperations">Valid Operations</a></li>
								<li><a href="#demos">Demos</a></li>
							</ul>
						</li>

						<li><a href="#verification" class="aj-nav folder">Verify</a>
							<ul class="nav nav-list">
								<li><a href="#login">login</a></li>
								<li><a href="#loginById">loginById</a></li>
								<li><a href="#register">register</a></li>
							</ul>
						</li>

						<li><a href="#userInformation" class="aj-nav folder">Users</a>
							<ul class="nav nav-list">
								<li><a href="#getUser">getUser</a></li>
								<li><a href="#getNameFromId">getNameFromId</a></li>
								<li><a href="#checkUser">checkUser</a></li>
								<li><a href="#uploadImage">uploadImage</a></li>
								<li><a href="#editProfile">editProfile</a></li>
							</ul>
						</li>


						<li><a href="#friends" class="aj-nav folder">Friends</a>
							<ul class="nav nav-list">
								<li><a href="#addFriend">addFriend</a></li>
								<li><a href="#removeFriend">removeFriend</a></li>
								<li><a href="#getFriends">getFriends</a></li>
							</ul>
						</li>


						<li><a href="#tags" class="aj-nav folder">Tags</a>
							<ul class="nav nav-list">
								<li><a href="#addTag">addTag</a></li>
								<li><a href="#deleteTag">deleteTag</a></li>
								<li><a href="#getTagsById">getTagsById</a></li>
							</ul>
						</li>


						<li><a href="#tagComments" class="aj-nav folder">Comments</a>
							<ul class="nav nav-list">
								<li><a href="#addTagComment">addTagComment</a></li>
								<li><a href="#getTagComments">getTagComments</a></li>
								<li><a href="#deleteTagComment">deleteTagComment</a></li>
							</ul>
						</li>


						<li><a href="#adventures" class="aj-nav folder">Adventures</a>
							<ul class="nav nav-list">
								<li><a href="#addAdventure">addAdventure</a></li>
								<li><a href="#deleteAdventure">deleteAdventure</a></li>
								<li><a href="#addTagToAdventure">addTagToAdventure</a></li>
								<li><a href="#removeTagFromAdventure">removeTagFromAdventure</a></li>
								<li><a href="#getAdventureById">getAdventureById</a></li>
								<li><a href="#getAdventureByOwnerId">getAdventureByOwnerId</a></li>
								<li><a href="#getAllAdventureTags">getAllAdventureTags</a></li>
								<li><a href="#getAllAdventuresUserPartOf">getAllAdventuresUserPartOf</a></li>
								<li><a href="#addUserToAdventureById">addUserToAdventureById</a></li>
								<li><a href="#addUserToAdventureByUsername">addUserToAdventureByUsername</a></li>
								<li><a href="#removeUserFromAdventure">removeUserFromAdventure</a></li>
							</ul>
						</li>
					</ul>

				<div class="well well-sidebar">
					<!-- Links -->
					<a href="archive/master.zip" target="_blank">Download</a><br>
					<a href="https://github.com/spencerkordecki/MobSci" target="_blank">GitHub Repo</a><br>
					<a href="mailto:zillwc@gmail.com" target="_blank">Requests/Issues</a><br>
				</div>
			</div>
		</div>


		<div class="right-column float-view content-area span9">
			<div class="content-page">
				<article>

					<div class="page-header sub-header clearfix">
						<h1><a name="gettingStarted">Getting Started</a></h1>
						<span style="float: left; font-size: 10px; color: gray;"><?php echo date('l, F j, Y') ?></span>
						<span style="float: right; font-size: 10px; color: gray;"><?php echo date("h:i a"); ?></span>
					</div>
					<p><strong>MobSci</strong> api is fully built on REST architectural calls. Each call made utilizes POST http requests and must contain an <code>operation</code> variable. This variable lets the service know the type of arguments to accept. For example, the <code>login</code> operation will only accept "username" and "password", while the <code>checkById</code> operation will accept only "id".</p>

					<p>Each request will output a response variable in JSON format. If the request made is successful, it will respond with a <code>{success:1}</code>. Most requests will also append a callback object with this JSON, which will give you the results of the call made. If the request fails or the server encounters an error, the response will send back an error code and an error_msg <code>{error:1, error_msg:'error message'}</code>.</p>

					<p>Although every call and variable sent to the server are throughly sanitized, to prevent errors, it is up to you to make sure the input data from the user is checked for consistency. This means you should make sure emails, passwords or ids are cast correctly. Make sure they fit the standard protocols (such as 6 letters for passwords or numeric input for ids).</p>

					<h2>Valid Operations</h2>
					<ul>
						<li><a href="#login">login</li>
						<li><a href="#loginById">loginById</a></li>
						<li><a href="#register">register</li>
						<li><a href="#getUser">getUser</li>
						<li><a href="#getNameFromId">getNameFromId</li>
						<li><a href="#checkUser">checkUser</li>
						<li><a href="#uploadImage">uploadImage</li>
						<li><a href="#editProfile">editProfile</li>
						<li><a href="#addTag">addTag</li>
						<li><a href="#deleteTag">deleteTag</li>
						<li><a href="#getTagsById">getTagsById</li>
						<li><a href="#getTagsByIdWOComments">getTagsByIdWOComments</li>
						<li><a href="#addTagComment">addTagComment</li>
						<li><a href="#getTagComments">getTagComments</li>
						<li><a href="#deleteTagComment">deleteTagComment</li>
						<li><a href="#removeFriend">removeFriend</li>
						<li><a href="#addFriend">addFriend</li>
						<li><a href="#getFriends">getFriends</li>
						<li><a href="#addAdventure">addAdventure</li>
						<li><a href="#deleteAdventure">deleteAdventure</li>
						<li><a href="#addTagToAdventure">addTagToAdventure</li>
						<li><a href="#removeTagFromAdventure">removeTagFromAdventure</li>
						<li><a href="#getAdventureById">getAdventureById</li>
						<li><a href="#getAdventureByOwnerId">getAdventureByOwnerId</li>
						<li><a href="#getAllAdventureTags">getAllAdventureTags</li>
						<li><a href="#getAllAdventuresUserPartOf">getAllAdventuresUserPartOf</li>
						<li><a href="#addUserToAdventureById">addUserToAdventureById</li>
						<li><a href="#addUserToAdventureByUsername">addUserToAdventureByUsername</li>
						<li><a href="#removeUserFromAdventure">removeUserFromAdventure</li>
					</ul>



					<h2 class="text-info"><a name="verification">Verification</a></h2>
					<p class="text-info">All functions in this structure allow you to add and authenticate users. It is very important while verifying a user, that you maintain the results of the response so multiple verification calls to the API aren't necessary.</p>


					<h2 class="lead"><a name="login" class="text-warning">login</a></h2>
					<p>Function allows you to authenticate a user. Accepts <span class="text-error">username</span> & <span class="text-error">password</span>. Returns a JSON object with all details on user. If the username and password combination is incorrect, an error code would be returned.</p>

<pre>
<code lang="language-json">
POST Call:
http://hci.montclair.edu/geotagger/?operation=login&username=bwayne&password=batman

{
	AccountID: "25",
	Username: "bwayne",
	EmailAddress: "bwayne@wenterprise.com",
	Image: "batman.png",
	Description: "The hero gotham needs",
	Location: "Gotham",
	Quote: "Justice!",
	Type: "0",
	Visibility: "1",
	CreatedDateTime: "2013-08-20 04:07:49",
	RatingScore: "0"
}
</code>
</pre>






					<h2 class="lead"><a name="loginById" class="text-warning">loginById</a></h2>
					<p>Use this operation when you want to reverify a user's authenticity or collect user information. Simply pass the userid (<span class="text-error">id</span>) and (<span class="text-error">password</span>) and if the credentials are correct, the server will send back a JSON object with user details.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=loginById&id=25&password=batman

{
	AccountID: "25",
	Username: "bwayne",
	EmailAddress: "bwayne@wenterprise.com",
	Image: "batman.png",
	Description: "The hero gotham needs",
	Location: "Gotham",
	Quote: "Justice!",
	Type: "0",
	Visibility: "1",
	CreatedDateTime: "2013-08-20 04:07:49",
	RatingScore: "0"
}
</code>
</pre>






					<h2 class="lead"><a name="register" class="text-warning">register</a></h2>
					<p>Use this operation when you want to register a new user. Accepts (<span class="text-error">username</span>) and (<span class="text-error">password</span>). If username is not taken, the server will add the user and send back a JSON object with new user's details. If the username is taken, the server will send back an error code.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=register&username=newUsername&password=newPass

{
	uID: "27",
	user: {
		uName: "newUsername"
	}
}
</code>
</pre>




					<h2 class="text-info"><a name="userInformation">User Information</a></h2>
					<p class="text-info">All functions in this structure allow you to obtain information on a user. This is mostly for when you want to reverify a user or manipulate their information.</p>




					<h2 class="lead"><a name="getUser" class="text-warning">getUser</a></h2>
					<p>This function gets user details by a specified (<span class="text-error">username</span>). Use this operation when you want to maybe add a friend and want to view that friend's information before adding, or want to reverify a user.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getUser&username=bwayne

{
	AccountID: "25",
	Username: "bwayne",
	EmailAddress: "bwayne@wenterprise.com",
	Image: "batman.png",
	Description: "The hero gotham needs",
	Location: "Gotham",
	Quote: "Justice!",
	Type: "0",
	Visibility: "1",
	CreatedDateTime: "2013-08-20 04:07:49",
	RatingScore: "0"
}
</code>
</pre>




					<h2 class="lead"><a name="getNameFromId" class="text-warning">getNameFromId</a></h2>
					<p>This function gets verifies that a user with a specified (<span class="text-error">id</span>) exists. Returns error response if user does not exist.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getNameFromId&id=25

{
	username: "bwayne"
}
</code>
</pre>




					<h2 class="lead"><a name="checkUser" class="text-warning">checkUser</a></h2>
					<p>Checks if (<span class="text-error">username</span>) is already taken.</p>

<pre>
<code>
POST Call 1:
http://hci.montclair.edu/geotagger/?operation=checkUser&username=bwayne

{
	error: 2,
	error_msg: "Username is already taken"
}

POST Call 2:
http://hci.montclair.edu/geotagger/?operation=checkUser&username=doesnotexist

	"User does not exist."
</code>
</pre>




					<h2 class="lead"><a name="uploadImage" class="text-warning">uploadImage</a></h2>
					<p>This call uploads images to the server. Only accepts base64. You must convert the image into a b64 string and send in a variable <span class="text-error">imageString</span></p>





					<h2 class="lead"><a name="editProfile" class="text-warning">editProfile</a></h2>
					<p>Use this call to change a user's information. Accepts userID(<span class="text-error">uId</span>), image URL(<span class="text-error">imgUrl</span>), user description(<span class="text-error">desc</span>), user location(<span class="text-error">location</span>), and a quote(<span class="text-error">quote</span>). Request will send back a new user details object.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=editProfile&uId=25&imgUrl=batman.jpg&desc=the%20hero%20gotham%20deserves&location=Gotham&quote=Justice!

{
	AccountID: "25",
	Username: "bwayne",
	Name: null,
	EmailAddress: "bwayne@wenterprise.com",
	Image: "batman.jpg",
	Description: "the hero gotham deserves",
	Location: "Gotham",
	Quote: "Justice!",
	Type: "0",
	Visibility: "1",
	RatingScore: "0"
}
</code>
</pre>




					<h2 class="text-info"><a name="friends">Friends</a></h2>
					<p class="text-info">Users can also have friends. The following operations allow you to manipulate this feature.</p>




					<h2 class="lead"><a name="addFriend" class="text-warning">addFriend</a></h2>
					<p>Use this function to add a new friend to a user. You can make this call with a user id of the owner user (<span class="text-error">uId</span>) and the email or username of the friend to add <span class="text-error">fName</span>.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=addFriend&uId=24&fName=bwayne

{
	success: 1
}
</code>
</pre>




					<h2 class="lead"><a name="removeFriend" class="text-warning">removeFriend</a></h2>
					<p>Use this function to remove a friend from a user. To make this call you need the user id of the owner friend (<span class="text-error">uId</span>) and the user id of the friend to delete <span class="text-error">fId</span>.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=removeFriend&uId=24&fId=25

{
	success: 1
}
</code>
</pre>




					<h2 class="lead"><a name="getFriends" class="text-warning">getFriends</a></h2>
					<p>Use this function to collect all the friends a user has. To make this call you need the user id of the owner friend (<span class="text-error">uId</span>). Call returns user detail object(s).</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getFriends&uId=24

{[
  {
	AccountID: "4",
	Username: "Testuser1",
	Name: "Chris L",
	EmailAddress: "loeschornc1@mail.montclair.edu",
	Image: "",
	Description: "This is my test account",
	Location: "NEW JERSEY",
	Quote: ""Hello"",
	Type: "1",
	Visibility: "1",
	RatingScore: "0"
  },
  {
	AccountID: "20",
	Username: "Failsj",
	Name: null,
	EmailAddress: null,
	Image: "",
	Description: "I am a pretty cool guy",
	Location: "Paris, France",
	Quote: "Wherever You Are There You Are",
	Type: "1",
	Visibility: "1",
	RatingScore: "0"
  }
]}
</code>
</pre>





					<h2 class="text-info"><a name="tags">Tags</a></h2>
					<p class="text-info">Tags are mainframe of this application. Their are the individual plots of data that allow users to take notes on a scene (something wrong in the environment). All functions in this structure allow you manipulate tags.</p>




					<h2 class="lead"><a name="addTag" class="text-warning">addTag</a></h2>
					<p>Use this function to add a new tag. Accepts userID(<span class="text-error">oId</span>), a name/title for the tag (<span class="text-error">name</span>), a description (<span class="text-error">desc</span>), image URL (<span class="text-error">imgUrl</span>), a location (<span class="text-error">loc</span>), latitude (<span class="text-error">lat</span>), longitude (<span class="text-error">lon</span>), and a category (<span class="text-error">cat</span>, which is usually "Default"). Request will send back a JSON object with the new tag details.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=editProfile&uId=25&imgUrl=batman.jpg&desc=the%20hero%20gotham%20deserves&location=Gotham&quote=Justice!

{
	AccountID: "25",
	Username: "bwayne",
	Name: null,
	EmailAddress: "bwayne@wenterprise.com",
	Image: "batman.jpg",
	Description: "the hero gotham deserves",
	Location: "Gotham",
	Quote: "Justice!",
	Type: "0",
	Visibility: "1",
	RatingScore: "0"
}
</code>
</pre>




					<h2 class="lead"><a name="deleteTag" class="text-warning">deleteTag</a></h2>
					<p>This request deletes a tag. It only needs the tag id (<span class="text-error">tId</span>) to specify which tag to delete.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=deleteTag&tId=35

{
	success: 1
}
</code>
</pre>





					<h2 class="lead"><a name="getTagsById" class="text-warning">getTagsById</a></h2>
					<p>This request gets all the tags associated with a user, along with the tag comments. Use this function when trying to collect all of the user's tags & their tag comments. It only needs the user id (<span class="text-error">oId</span>) to specify which user's tags to return.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getTagsById&tId=35

[{
	TagID: "82",
	OwnerID: "24",
	Username: "zillwc",
	Name: "test3",
	Description: "Test3Desc",
	ImageUrl: "test3Image.url",
	Visibility: "1",
	Location: "Montclair RI108",
	Latitude: 40,
	Longitude: 70,
	Category: Default,
	CreatedDateTime: "2013-08-15 18:46:12",
	RatingScore: "0",
	comment:
	[
		{
			ID: "26",
			ParentTagID: "82",
			Username: "Failsj",
			Title: null,
			Text: "Hey, nice tag",
			CreatedDateTime: "2013-08-19 12:29:38",
			RatingScore: "0"
		}
	]
},
{
	TagID: "80",
	OwnerID: "24",
	Username: "zillwc",
	Name: "Test1",
	Description: "Test1 Desc",
	ImageUrl: "test1Image.url",
	Visibility: "1",
	Location: "Montclair RI104",
	Latitude: 40,
	Longitude: 70,
	Category: "Default",
	CreatedDateTime: "2013-08-15 18:44:56",
	RatingScore: "0",
	comment: [ ]
}]
</code>
</pre>


					<h2 class="lead"><a name="getTagsByIdWOComments" class="text-warning">getTagsByIdWOComments</a></h2>
					<p>This request gets all the tags associated with a user, but returns it without the tag comments. Use this function when trying to collect all of the user's tags but not their tag comments. It only needs the user id (<span class="text-error">oId</span>) to specify which user's tags to return.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getTagsByIdWOComments&tId=35

[{
	TagID: "82",
	OwnerID: "24",
	Username: "zillwc",
	Name: "test3",
	Description: "Test3Desc",
	ImageUrl: "test3Image.url",
	Visibility: "1",
	Location: "Montclair RI108",
	Latitude: 40,
	Longitude: 70,
	Category: Default,
	CreatedDateTime: "2013-08-15 18:46:12",
	RatingScore: "0",
},
{
	TagID: "80",
	OwnerID: "24",
	Username: "zillwc",
	Name: "Test1",
	Description: "Test1 Desc",
	ImageUrl: "test1Image.url",
	Visibility: "1",
	Location: "Montclair RI104",
	Latitude: 40,
	Longitude: 70,
	Category: "Default",
	CreatedDateTime: "2013-08-15 18:44:56",
	RatingScore: "0",
}]
</code>
</pre>




					<h2 class="text-info"><a name="Comments">Comments</a></h2>
					<p class="text-info">Comments are essentially tag comments. They allow users to relay data, compliments, or just their point of view on an individual tag. All functions in this structure allow you manipulate tags.</p>




					<h2 class="lead"><a name="addTagComment" class="text-warning">addTagComment</a></h2>
					<p>This request adds a comment to a tag. The variables necessary include the tag id (<span class="text-error">tagId</span>), a title for the comment (<span class="text-error">title</span>), the comment itself (<span class="text-error">comment</span>), and the username of the user who made the comment (<span class="text-error">uName</span>). The response will be the tag itself with all of the attached attached comments.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=addTagComment&tagId=82&uName=bwayne&title=GoodJob&comment=good%20job%20on%20this%20find

{
	TagID: "82",
	OwnerID: "24",
	Name: "test3",
	Description: "Test3Desc",
	ImageUrl: "test3Image.url",
	Visibility: "1",
	Location: "Montclair RI108",
	Latitude: 40,
	Longitude: 70,
	Category: "Default",
	CreatedDateTime: "2013-08-15 18:46:12",
	RatingScore: "0",
	comment: [
		{
			ID: "26",
			ParentTagID: "82",
			Username: "Failsj",
			Title: null,
			Text: "Hey, nice tag",
			CreatedDateTime: "2013-08-19 12:29:38",
			RatingScore: "0"
		},
		{
			ID: "27",
			ParentTagID: "82",
			Username: "bwayne",
			Title: "GoodJob",
			Text: "good job on this find",
			CreatedDateTime: "2013-08-21 02:12:07",
			RatingScore: "0"
		}
	]
}
</code>
</pre>



					<h2 class="lead"><a name="getTagComments" class="text-warning">getTagComments</a></h2>
					<p>This request gets all the comment associated to a tag. You only need to specify the tag id (<span class="text-error">tagId</span>).

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getTagComments&tagId=82

[{
	ID: "26",
	ParentTagID: "82",
	Username: "Failsj",
	Title: null,
	Text: "Hey, nice tag",
	CreatedDateTime: "2013-08-19 12:29:38",
	RatingScore: "0"
 },
 {
	ID: "27",
	ParentTagID: "82",
	Username: "bwayne",
	Title: "GoodJob",
	Text: "good job on this find",
	CreatedDateTime: "2013-08-21 02:12:07",
	RatingScore: "0"
}]
</code>
</pre>






					<h2 class="lead"><a name="deleteTagComment" class="text-warning">deleteTagComment</a></h2>
					<p>This request deletes the tag comment. You need to specify the comment id (<span class="text-error">cId</span>). The call sends back the tag the comment belonged to in a JSON object.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=deleteTagComment&cId=27

{
	TagID: "82",
	OwnerID: "24",
	Name: "test3",
	Description: "Test3Desc",
	ImageUrl: "test3Image.url",
	Visibility: "1",
	Location: "Montclair RI108",
	Latitude: null,
	Longitude: null,
	Category: null,
	CreatedDateTime: "2013-08-15 18:46:12",
	RatingScore: "0",
	comment: [
		{
			ID: "26",
			ParentTagID: "82",
			Username: "Failsj",
			Title: null,
			Text: "Hey, nice tag",
			CreatedDateTime: "2013-08-19 12:29:38",
			RatingScore: "0"
		}
	]
}
</code>
</pre>





					<h2 class="text-info"><a name="adventures">Adventures</a></h2>
					<p class="text-info">Adventures are a social feature of MobSci that allows users to collect data in a filtered, group style, format. It is, essentially, a group of tags. The following functions allow you to manipulate this feature.</p>


					<h2 class="lead"><a name="addAdventure" class="text-warning">addAdventure</a></h2>
					<p>This function allows you to add an adventure. You need to specify the user id (<span class="text-error">uId</span>), a name/title of the adventure (<span class="text-error">name</span>), and a description of the adventure (<span class="text-error">desc</span>). The function will then return a response with the details of the newly created adventure.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=addAdventure&uId=24&name=testadv&desc=this%20is%20a%20simple%20adventure&loc=somewhere

{
	AdventureID: 9,
	OwnerID: "24",
	Name: "testadv",
	Description: "this is a simple adventure",
}
</code>
</pre>



					<h2 class="lead"><a name="deleteAdventure" class="text-warning">deleteAdventure</a></h2>
					<p>This function allows you to delete an adventure. You need to specify the id of the adventure (<span class="text-error">id</span>).</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=deleteAdventure&id=8

{
	success: 1
}
</code>
</pre>



					<h2 class="lead"><a name="addTagToAdventure" class="text-warning">addTagToAdventure</a></h2>
					<p>This function allows you to add a tag to an existing adventure. The two parameters needed are the tag id (<span class="text-error">tagId</span>) and the adventure id (<span class="text-error">advId</span>). </p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=addTagToAdventure&tagId=73&advId=6

{
	success: 1
}
</code>
</pre>




					<h2 class="lead"><a name="removeTagFromAdventure" class="text-warning">removeTagFromAdventure</a></h2>
					<p>This function allows you to remove a tag from an existing adventure. The two parameters needed are the tag id (<span class="text-error">tagId</span>) and the adventure id (<span class="text-error">advId</span>). </p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=removeTagFromAdventure&advId=6&tagId=73

{
	success: 1
}
</code>
</pre>





					<h2 class="lead"><a name="getAdventureById" class="text-warning">getAdventureById</a></h2>
					<p>This function returns an adventure specified by its unique id. This is used for when you want fresh details on an adventure. The field needed for this is the adventure id (<span class="text-error">id</span>). This function only returns the details on an adventure object.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getAdventureById&id=7

{
	ID: "7",
	OwnerID: "4",
	Name: "Test Adv",
	Description: "This is a test adv to add some test data",
	Visibility: "1",
	CreatedDateTime: "2013-08-15 18:46:58"
}
</code>
</pre>




					<h2 class="lead"><a name="getAdventureByOwnerId" class="text-warning">getAdventureByOwnerId</a></h2>
					<p>This function returns an adventure specified by the owner id. It is used to collect adventures a user owns. The field needed for this is the user id (<span class="text-error">id</span>). This function only returns the details on an adventure object.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getAdventureByOwnerId&id=4

{[
  {
	ID: "6",
	OwnerID: "4",
	Name: "New Adv",
	Description: null,
	Visibility: "1",
	CreatedDateTime: "2013-02-04 18:15:52"
  },
  {
	ID: "7",
	OwnerID: "4",
	Name: "Test Adv",
	Description: "This is a test adv to add some test data",
	Visibility: "1",
	CreatedDateTime: "2013-08-15 18:46:58"
  }
]}
</code>
</pre>




					<h2 class="lead"><a name="getAllAdventureTags" class="text-warning">getAllAdventureTags</a></h2>
					<p>This function gets all the tags that an adventure contains or is associated with. The field needed for this is the adventure id (<span class="text-error">id</span>). This function only returns the details on tag object(s).</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getAllAdventureTags&id=7

{[
	{
		TagID: "80",
		OwnerID: "24",
		Name: "Test1",
		Description: "Test1 Desc",
		ImageUrl: "test1Image.url",
		Visibility: "1",
		Location: "Montclair RI104",
		Latitude: null,
		Longitude: null,
		Category: "Default",
		CreatedDateTime: "2013-08-15 18:44:56",
		RatingScore: "0"
	},
	{
		TagID: "81",
		OwnerID: "24",
		Name: "Test2",
		Description: "test2Desc",
		ImageUrl: "test2Image.url",
		Visibility: "1",
		Location: "Montclair RI105",
		Latitude: null,
		Longitude: null,
		Category: null,
		CreatedDateTime: "2013-08-15 18:45:37",
		RatingScore: "0"
	}
]}
</code>
</pre>





					<h2 class="lead"><a name="getAllAdventuresUserPartOf" class="text-warning">getAllAdventuresUserPartOf</a></h2>
					<p>This function gets all the tags that a user owns or is part of. The field needed for this is the user id (<span class="text-error">id</span>). This function returns all the adventures with their associated tags and the comments attached to each tag.</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=getAllAdventuresUserPartOf&id=24

{[
	{
		TagID: "80",
		OwnerID: "24",
		Name: "Test1",
		Description: "Test1 Desc",
		ImageUrl: "test1Image.url",
		Visibility: "1",
		Location: "Montclair RI104",
		Latitude: null,
		Longitude: null,
		Category: "Default",
		CreatedDateTime: "2013-08-15 18:44:56",
		RatingScore: "0"
	},
	{
		TagID: "81",
		OwnerID: "24",
		Name: "Test2",
		Description: "test2Desc",
		ImageUrl: "test2Image.url",
		Visibility: "1",
		Location: "Montclair RI105",
		Latitude: null,
		Longitude: null,
		Category: null,
		CreatedDateTime: "2013-08-15 18:45:37",
		RatingScore: "0"
	}
]}
</code>
</pre>





					<h2 class="lead"><a name="addUserToAdventureById" class="text-warning">addUserToAdventureById</a></h2>
					<p>This function allows you to add a user to an adventure using their user id. Use this function if you only have access to the user's id. The two parameters needed are the user id of the user being added (<span class="text-error">uId</span>) and the adventure id (<span class="text-error">advId</span>).</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=addUserToAdventureById&advId=6&uId=24

{
	success: 1
}
</code>
</pre>





					<h2 class="lead"><a name="addUserToAdventureByUsername" class="text-warning">addUserToAdventureByUsername</a></h2>
					<p>This function allows you to add a user to an adventure using their username. Use this function if you only have access to the user's username. The two parameters needed are the username of the user being added (<span class="text-error">uName</span>) and the adventure id (<span class="text-error">advId</span>).</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=addUserToAdventureByUsername&advId=6&uName=bwayne

{
	success: 1
}
</code>
</pre>






					<h2 class="lead"><a name="removeUserFromAdventure" class="text-warning">removeUserFromAdventure</a></h2>
					<p>This function allows you to remove a user from an adventure. The two parameters needed are the user id of the user (<span class="text-error">uId</span>) and the adventure id (<span class="text-error">advId</span>).</p>

<pre>
<code>
POST Call:
http://hci.montclair.edu/geotagger/?operation=removeUserFromAdventure&advId=6&uId=25

{
	success: 1
}
</code>
</pre>





						</article>
					</div>
				</div>
			</div>
		</div>
</body>
</html>
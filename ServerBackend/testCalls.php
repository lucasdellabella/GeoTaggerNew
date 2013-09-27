<?php
/*
	* File to handle all API requests
	* Accepts POST Requests
*
	* Each request will be identified by an operation variable
	* Response will be in JSON
*/

	// If the operation is not specified, deny access
	if (empty($_GET["operation"])) {
		echo 'Access Denied';
		exit();
	}

	// Function object and mysql connection (so we can use mysql_real_escape_string)
    include_once('scripts/functions.php');
    include_once('scripts/db/connect.inc.php');

    // Make a new object for the functions
    $db = new DBFunctions();
	$operation = $_GET['operation'];

    // Init Response
    $response = array("operation" => $operation, "success" => 0, "error" => 0);


    /*
    	Switch statement to handle all the requests
    	Calls a function based on operation
    	Function is called with the db object & the operation as param
    */
    switch ($operation) {


	    // Verification functions
    	case 'login':
    	case 'loginById':
    			checkLogin($operation, $db);
    		break;
    	case 'register':
    			register($operation, $db);
    		break;


		// User Functions
    	case 'getUser':
    			getUser($operation, $db);
    		break;
    	case 'getNameFromId':
    			getNameFromId($operation, $db);
    		break;
    	case 'checkUser':
    			checkUser($operation, $db);
    		break;
    	case 'uploadImage':
    			uploadImage();
    		break;
    	case 'editProfile':
    			editProfile($operation, $db);
    		break;


    	// Tag Functions
    	case 'addTag':
    			addTag($operation, $db);
    		break;
    	case 'deleteTag':
    			deleteTag($operation, $db);
    		break;
    	case 'getTagsById':
    			getTagsById($operation, $db);
    		break;
    	case 'getTagsByIdWOComments':
    			getTagsByIdWOComments($operation, $db);
    		break;


    	// Tag Comment Functions
    	case 'addTagComment':
    			addTagComment($operation, $db);
    		break;
    	case 'getTagComments':
    			getTagComments($operation, $db);
    		break;
    	case 'deleteTagComment':
    			deleteTagComment($operation, $db);
    		break;


		// Friend Functions
    	case 'addFriend':
    			addFriend($operation, $db);
    		break;
    	case 'removeFriend':
    			removeFriend($operation, $db);
    		break;
    	case 'getFriends':
    			getFriends($operation, $db);
    		break;


    	// Adventure Functions
    	case 'addAdventure':
    			addAdventure($operation, $db);
    		break;
    	case 'deleteAdventure':
    			deleteAdventure($operation, $db);
    		break;
    	case 'addTagToAdventure':
    			addTagToAdventure($operation, $db);
    		break;
    	case 'removeTagFromAdventure':
    			removeTagFromAdventure($operation, $db);
    		break;
    	case 'getAdventureById':
    	case 'getAdventureByOwnerId':
    			getAdventureById($operation, $db);
    		break;
        case 'getTagsByAdvId':
    	case 'getAllAdventureTags':
    			getAllAdventureTags($operation, $db);
    		break;
    	case 'getAllAdventuresUserPartOf':
		    	getAllAdventuresUserPartOf($operation, $db);
    		break;
    	case 'addUserToAdventureById':
    			addUserToAdventureById($operation, $db);
    		break;
    	case 'addUserToAdventureByUsername':
    			addUserToAdventureByUsername($operation, $db);
    		break;
    	case 'removeUserFromAdventure':
    			removeUserFromAdventure($operation, $db);
    		break;

    	case 'getEverything':
    			getEverything($operation, $db);
    		break;


    	// Default Error
    	default:
    		$response["error"] = 1;
            $response["error_msg"] = "Invalid Operation";
            echo json_encode($response);
    		break;
    }




/*
	** Verification Functions
*/

    // Function calls the login function or the loginById function
    function checkLogin($operation, $db) {
        $uName = "";
        $id = -1;
        $user = false;

        // If operation is not 'login' then it's 'loginById'
		if ($operation == 'login') {
			$uName = mysql_real_escape_string($_GET['username']);
			$password = mysql_real_escape_string($_GET['password']);
			$user = $db->login($uName, $password);
		} else {
        	$id = $_GET['id'];
        	$password = $_GET['password'];
        	$user = $db->loginById($id, $password);
        }

        // User is found
        if ($user != false) {
            $response["success"] = 1;
            $response["AccountID"] = $user["AccountID"];
            $response["Username"] = $user["Username"];
            $response["EmailAddress"] = $user["EmailAddress"];
            $response["Password"] = $user["Password"];
            $response["Image"] = $user["Image"];
			$response["Description"] = $user["Description"];
            $response["Location"] = $user["Location"];
            $response["Quote"] = $user["Quote"];
			$response["Type"] = $user["Type"];
            $response["Visibility"] = $user["Visibility"];
            $response["CreatedDateTime"] = $user["CreatedDateTime"];
			$response["RatingScore"] = $user["RatingScore"];
        } else {
            // User not found
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
        }
        echo json_encode($response);
    }

    // Function registers a new user
    function register($operation, $db) {
        // Request type is Register new user
        $username = mysql_real_escape_string($_GET['username']);
        $password = mysql_real_escape_string($_GET['password']);

        // Check if username is already taken
        if ($db->checkUsername($username)) {
            $response["error"] = 2;
            $response["error_msg"] = "Username is already taken!";
            echo json_encode($response);
        }
        else {
            // store user
            $user = $db->addUser($username, $password);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
				$response["uID"] = $user["AccountID"];
				$response["user"]["uName"] = $user["Username"];
            }
            else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occurred during registration!";
            }

            echo json_encode($response);
        }
    }







/*
	** User Functions
*/


    // Function gets a user account by username
    function getUser($operation, $db) {
		$username = mysql_real_escape_string($_GET['username']);
		$user = $db->getUser($username);

        // User is found
        if ($user!=false) {
            $response["success"] = 1;
            $response["AccountID"] = $user["AccountID"];
            $response["Username"] = $user["Username"];
            $response["EmailAddress"] = $user["EmailAddress"];
            $response["Image"] = $user["Image"];
			$response["Description"] = $user["Description"];
            $response["Location"] = $user["Location"];
            $response["Quote"] = $user["Quote"];
			$response["Type"] = $user["Type"];
            $response["Visibility"] = $user["Visibility"];
            $response["CreatedDateTime"] = $user["CreatedDateTime"];
			$response["RatingScore"] = $user["RatingScore"];
        } else {
            // User not found
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
        }
        echo json_encode($response);
    }

    // Get the username from the ID
    function getNameFromId($operation, $db) {
		$id = mysql_real_escape_string($_GET['id']);
		$result = $db->getNameFromId($id);
		if ($result) {		// Return username
			$response["success"] = 1;
        	$response["username"] = $result['Username'];
		} else {
        	$response["error"] = 1;			// Error retrieving username
        	$response["error_msg"] = "Error retrieving username.";
		}
		echo json_encode($response);
    }

    // Checks if username is already taken
    function checkUser($operation, $db) {
		$name = mysql_real_escape_string($_GET['username']);
        if ($db->checkUsername($name)) {
            // user exists - error response
            $response["error"] = 2;
            $response["error_msg"] = "Username is already taken";
            echo json_encode($response);
        }
        else
            echo "User does not exist.";
    }

    // Function uploads an image to the server
    function uploadImage() {
		$encodedImage = $_GET['imageString'];
		// Path to Images base directory
		$imgFolderPath = dirname(__FILE__) . '/Images/';
		$baseUrl = "http://hci.montclair.edu/geotagger/Images/";

		// Organize Image folders by month.
		$date = getdate();
		// Append a 0 if month < 10
		$monStr = "";
		if ($date[mon] < 10) {$monStr = "0$date[mon]";}
		else {$monStr = "$date[mon]";}

		// Create folder if it doesnt exist
		$folderName = "$monStr$date[year]";
		$folderPath = $imgFolderPath . $folderName . '/';
		if (!file_exists($folderPath)){mkdir("$folderPath");}

		// Decode image and save to file
		$fileName = uniqid() . '.jpg';
		$filePath = $folderPath . $fileName;

		$imgData=base64_decode($encodedImage);
		header('Content-Type: bitmap; charset=utf-8');
		$file = fopen($filePath, 'wb');

		if (fwrite($file, $imgData) != false) {
			fclose($file);
			$url = $baseUrl . $folderName . '/' . $fileName;
        	$response["success"] = 1;
        	$response["url"] = $url;	// image saved successfully, return url in JSON
        } else {
        	$response["error"] = 1;
        	$response["error_msg"] = "Error uploading image.";		// Error uploading image
        }
        echo json_encode($response);
    }

	// Function updates the users profile
	function editProfile($operation, $db) {
		$uId = $_GET['uId'];
		$imgUrl = $_GET['imgUrl'];
		$desc = $_GET['desc'];
		$location = $_GET['location'];
		$quote = $_GET['quote'];

		$result = $db->editProfile($imgUrl, $desc, $location, $quote, $uId);
        echo json_encode($result);
	}







/*
	** Tag Functions
*/

    // Function adds a new tag and returns the id of the tag made
    function addTag($operation, $db) {
        $id = mysql_real_escape_string($_GET['oId']);
        $name = mysql_real_escape_string($_GET['name']);
        $desc = mysql_real_escape_string($_GET['desc']);
        $imgUrl = mysql_real_escape_string($_GET['imgUrl']);
        $loc = mysql_real_escape_string($_GET['loc']);
        $lat = mysql_real_escape_string($_GET['lat']);
        $lon = mysql_real_escape_string($_GET['lon']);
        $cat = mysql_real_escape_string($_GET['cat']);


        // store user
        $success = $db->addTag($id, $name, $desc, $imgUrl, $loc, $lat, $lon, $cat);
        if ($success!=false) {		// tag stored successfully
        	$response["success"] = 1;
        	$response["tagId"] = $success;
        }
        else {		// user failed to store
        	$response["error"] = 1;
        	$response["error_msg"] = "Error adding tag.";
        }
        echo json_encode($response);
    }

    // Function deletes a tag
    function deleteTag($operation, $db) {
		$tId = mysql_real_escape_string($_GET['tId']);
		$result = $db->removeTag($tId);
        echo json_encode($result);
    }

    // Function pulls all tags associated with a userID
    function getTagsById($operation, $db) {
		$oId = mysql_real_escape_string($_GET['oId']);
		$result = $db->getTagsById($oId);
		echo json_encode($result);
    }

    // Function pulls all tags associated with a userID but returns w/o comments
    function getTagsByIdWOComments($operation, $db) {
		$oId = mysql_real_escape_string($_GET['oId']);
		$result = $db->getTagsByIdWOComments($oId);
		echo json_encode($result);
    }








/*
	** TagComment Functions
*/

	// Function adds a comment to a tag
	function addTagComment($operation, $db) {
		$tID = mysql_real_escape_string($_GET['tagId']);
		$title = mysql_real_escape_string($_GET['title']);
		$comment = mysql_real_escape_string($_GET['comment']);
		$uName = mysql_real_escape_string($_GET['uName']);
		$tagUrl = "";

		if (isset($_GET['tagUrl'])) {
			$tagUrl = mysql_real_escape_string($_GET['tagUrl']);
		}

		$result = $db->addTagComment($tID, $uName, $title, $comment, $tagUrl);
		echo json_encode($result);
	}

	// Function get the comments for a tag
	function getTagComments($operation, $db) {
		$tagId = mysql_real_escape_string($_GET['tagId']);
		$result = $db->getTagComments($tagId);
		echo json_encode($result);
	}

	// Function deletes a tag comment
	function deleteTagComment($operation, $db) {
		$cId = mysql_real_escape_string($_GET['cId']);
		$result = $db->deleteTagComment($cId);
		echo json_encode($result);
	}








/*
	** Friend Functions
*/

	// Function adds a friend
	function addFriend($operation, $db) {
		$uId = mysql_real_escape_string($_GET['uId']);
		$fName = mysql_real_escape_string($_GET['fName']);
		$result = $db->addFriend($uId, $fName);
		echo json_encode($result);
	}

	// Function removes a friend
	function removeFriend($operation, $db) {
		$uId = mysql_real_escape_string($_GET['uId']);
		$fId = mysql_real_escape_string($_GET['fId']);
		$result = $db->removeFriend($uId, $fId);
		echo json_encode($result);
	}

	// Function returns all the friends
	function getFriends($operation, $db) {
		$uId = mysql_real_escape_string($_GET['uId']);
		$result = $db->getFriends($uId);
		echo json_encode($result);
	}







/*
	** Adventure Functions
*/

	// Function adds an adventure
	function addAdventure($operation, $db) {
		$uId = mysql_real_escape_string($_GET['uId']);
		$name = mysql_real_escape_string($_GET['name']);
		$desc = mysql_real_escape_string($_GET['desc']);

		$result = $db->addAdventure($uId, $name, $desc);
		echo json_encode($result);
	}

	// Function deletes an adventure
	function deleteAdventure($operation, $db) {
		$id = mysql_real_escape_string($_GET['id']);
		$result = $db->deleteAdventure($id);
		echo json_encode($result);
	}

	// Function adds a tag to an adventure
	function addTagToAdventure($operation, $db) {
		$tagId = mysql_real_escape_string($_GET['tagId']);
		$advId = mysql_real_escape_string($_GET['advId']);
		$result = $db->addTagToAdventure($tagId, $advId);
		echo json_encode($result);
	}

	// Function deletes a tag from an adventure
	function removeTagFromAdventure($operation, $db) {
		$tagId = mysql_real_escape_string($_GET['tagId']);
		$advId = mysql_real_escape_string($_GET['advId']);
		$result = $db->removeTagFromAdventure($tagId, $advId);
		echo json_encode($result);
	}

	// Function gets the adventure by its ID or the owner id
	function getAdventureById($operation, $db) {
		$id = mysql_real_escape_string($_GET['id']);

		if ($operation == 'getAdventureById')
			$adv = $db->getAdventureById($id);
		else
			$adv = $db->getAdventureByOwnerId($id);
		echo json_encode($adv);
	}

	// Function gets all the tags that an adventure contains
	function getAllAdventureTags($operation, $db) {
		$id = mysql_real_escape_string($_GET['id']);
		$adv = $db->getAllAdventureTags($id);
		echo json_encode($adv);
	}

	// Function gets all the adventures with tags that the user is part of
	function getAllAdventuresUserPartOf($operation, $db) {
		$id = mysql_real_escape_string($_GET['id']);
		$adv = $db->getAllAdventuresUserPartOf($id);
		echo json_encode($adv);
	}

	// Function adds a user to an adventure by their id
	function addUserToAdventureById($operation, $db) {
		$uId = mysql_real_escape_string($_GET['uId']);
		$advId = mysql_real_escape_string($_GET['advId']);
		$result = $db->addUserToAdventureById($uId, $advId);
		echo json_encode($result);
	}

	// Function adds a user to an adventure by their username
	function addUserToAdventureByUsername($operation, $db) {
		$uName = mysql_real_escape_string($_GET['uName']);
		$advId = mysql_real_escape_string($_GET['advId']);
		$result = $db->addUserToAdventureByUsername($uName, $advId);
		echo json_encode($result);
	}

	// Function adds a user to an adventure
	function removeUserFromAdventure($operation, $db) {
		$uId = mysql_real_escape_string($_GET['uId']);
		$advId = mysql_real_escape_string($_GET['advId']);
		$result = $db->removeUserFromAdventure($uId, $advId);
		echo json_encode($result);
	}



	// Experimental function returns EVERYTHING about a user
	function getEverything($operation, $db) {
		$username = mysql_real_escape_string($_GET['username']);
		$password = mysql_real_escape_string($_GET['password']);
		$result = $db->getEverything($username, $password);
		echo json_encode($result);
	}

?>
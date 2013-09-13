<?php
/**
 * File to handle all API requests
 * Accepts GET and POST
 *
 * Each request will be identified by operation
 * Response will be JSON data

  /**
 * check for POST request
 */

if (!empty($_POST["operation"]))
{
    // get operation
    $operation = $_POST['operation'];

    // include db handler and image server config
    require_once 'dbProcedures.php';
    $db = new dbProcedures();

    // response Array
    $response = array("operation" => $operation, "success" => 0, "error" => 0);

    // check for operation type
    if ($operation == 'login' || $operation == 'loginById')
    {
        // Request type is check Login
        $uName = "";
        $id = -1;
        $user = false;
		if ($operation == 'login')
		{
			$uName = $_POST['username'];
			$password = $_POST['password'];
			// check for user
			$user = $db->login($uName, $password);
		}
        else
        {
        	$id = $_POST['id'];
        	$password = $_POST['password'];
        	// check for user
        	$user = $db->loginById($id, $password);
        }

        if ($user != false)
        {
            // login success
            // return json with success = 1
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
            echo json_encode($response);
        }
        else
        {
            // user not found
            // return json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } //end login
    //get a user account by name
	else if ($operation == 'getUser') {
		$username = $_POST['username'];
		$user = $db->getUser($username);
		if ($user != false)
		{
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
			echo json_encode($response);
		}
		else
		{
			$response["error"] = 1;
            $response["error_msg"] = "User not found";
            echo json_encode($response);
		}
	}
    else if ($operation == 'register') {
        // Request type is Register new user
        $username = $_POST['username'];
        $password = $_POST['password'];

        // check if username is already taken
        if ($db->checkUsername($username))
        {
            // user exists - error response
            $response["error"] = 2;
            $response["error_msg"] = "Username is already taken";
            echo json_encode($response);
        }
        else
        {
            // store user
            $user = $db->addUser($username, $password);
            if ($user)
            {
                // user stored successfully
                $response["success"] = 1;
				$response["uID"] = $user["AccountID"];
				$response["user"]["uName"] = $user["Username"];
                echo json_encode($response);
            }
            else
            {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in Registartion";
                echo json_encode($response);
            }
        }
    } //end register
    else if ($operation == 'checkUser')
    {
		$name = $_POST['username'];
		// check if username is already taken
        if ($db->checkUsername($name))
        {
            // user exists - error response
            $response["error"] = 2;
            $response["error_msg"] = "Username is already taken";
            echo json_encode($response);
        }
        else
        {
            echo "User does not exist.";
		}
	} //end checkUser
	//add tag to db
	else if ($operation == 'addTag')
	{
        $oId = $_POST['oId'];
        $vis = $_POST['vis'];
        $name = $_POST['name'];
        $desc = $_POST['desc'];
        $imgUrl = $_POST['imgUrl'];
        $loc = $_POST['loc'];
        $lat = $_POST['lat'];
        $lon = $_POST['lon'];
        $cat = $_POST['cat'];

        // store user
        $success = $db->addTag($oId, $vis, $name, $desc, $imgUrl, $loc, $lat, $lon, $cat);
        if ($success)
        {
        	// tag stored successfully
        	$response["success"] = 1;
        	$response["tagId"] = mysql_result($success,0);
        	echo json_encode($response);
        }
        else
        {
        	// user failed to store
        	$response["error"] = 1;
        	$response["error_msg"] = "Error adding tag.";
        	echo json_encode($response);
        }
	}//end add tag
	//upload an image and return the url
	else if ($operation == 'uploadImage')
	{
		$encodedImage = $_POST['imageString'];
		//path to Images base directory
		$imgFolderPath = dirname(__FILE__) . '/Images/';
		$baseUrl = "http://hci.montclair.edu/geotagger/Images/";

		//Organize Image folders by month.
		$date = getdate();
		//append a 0 if month < 10
		$monStr = "";
		if ($date[mon] < 10) {$monStr = "0$date[mon]";}
		else{$monStr = "$date[mon]";}

		//create folder if it doesnt exist
		$folderName = "$monStr$date[year]";
		$folderPath = $imgFolderPath . $folderName . '/';
		if (! file_exists($folderPath)){mkdir("$folderPath");}

		//decode image and save to file
		$fileName = uniqid() . '.jpg';
		$filePath = $folderPath . $fileName;

		$imgData=base64_decode($encodedImage);
		header('Content-Type: bitmap; charset=utf-8');
		$file = fopen($filePath, 'wb');

		if (fwrite($file, $imgData) != false)
		{
			fclose($file);
			$url = $baseUrl . $folderName . '/' . $fileName;
			// image saved successfully, return url in JSON
        		$response["success"] = 1;
        		$response["url"] = $url;
        		echo json_encode($response);
		}
		else
       		 {
        	// Error uploading image
        	$response["error"] = 1;
        	$response["error_msg"] = "Error uploading image.";
        	echo json_encode($response);
        	}
	}//end save image
	else if($operation == 'getTagsById')
	{
		$oId = $_POST['oId'];
+-		$result = $db->getTagsById($oId);
		if($result)
		{
			$rows = array();
			//loop through all rows in the result
			while($r = mysql_fetch_assoc($result))
			{
				$rows[] = $r;
			}
			echo json_encode($rows);
		}
	}
	//return username for given id
	else if ($operation == 'getNameFromId')
	{
		$id = $_POST['id'];
		$result = $db->getNameFromId($id);
		//return username
		if ($result)
		{
			$response["success"] = 1;
        	$response["username"] = $result['Username'];
        	echo json_encode($response);
		}
		else
		{
			// Error retrieving username
        	$response["error"] = 1;
        	$response["error_msg"] = "Error retrieving username.";
        	echo json_encode($response);
		}
	}
	//return delete tag
	else if ($operation == 'deleteTag')
	{
		$tId = $_POST['tId'];
		$result = $db->removeTag($tId);
		//return username
		if ($result)
		{
		     $response["success"] = 1;
        	     echo json_encode($response);
		}
		else
		{
		     // Error retrieving username
        	     $response["error"] = 1;
        	     $response["error_msg"] = "Error deleting tag.";
        	     echo json_encode($response);
		}
	}
	//remove friend
	else if ($operation == 'removeFriend')
	{
		$uId = $_POST['uId'];
		$fId = $_POST['fId'];
		$result = $db->removeFriend($uId, $fId);
		//return success
		if ($result)
		{
		     $response["success"] = 1;
        	     echo json_encode($response);
		}
		else
		{
		     // Error deleting tag
        	     $response["error"] = 1;
        	     $response["error_msg"] = "Error removing friend.";
        	     echo json_encode($response);
		}
	}
	//attempt to add friend by searching for their username
	else if ($operation == 'addFriend')
	{
		$uId = $_POST['uId'];
		$fName = $_POST['fName'];
		$result = $db->addFriend($uId, $fName);
		//return username
		if ($result == 1)
		{
		     $response["success"] = 1;
        	 echo json_encode($response);
		}
		else if ($result == 0)
		{
		     // Users are already friends
        	     $response["error"] = 1;
        	     $response["error_msg"] = "Users are already friends!";
        	     echo json_encode($response);
		}
		else if ($result == -1)
		{
		     // Error retrieving username
        	     $response["error"] = 2;
        	     $response["error_msg"] = "Could not find given user";
        	     echo json_encode($response);
		}
		else
		{
		     $response["error"] = 3;
        	 $response["error_msg"] = "Error adding friend.";
        	 echo json_encode($response);
		}
	}
	//attempt to add friend by searching for their username
	else if ($operation == 'getFriends')
	{
		$uId = $_POST['uId'];
		$result = $db->getFriends($uId);
		//return array of tags as jsonarray
		if($result)
		{
			$rows = array();
			//loop through all rows in the result
			while($r = mysql_fetch_assoc($result))
			{
				$rows[] = $r;
			}
			echo json_encode($rows);
		}
		else
		{
		     $response["error"] = 1;
        	 $response["error_msg"] = "Error adding friend.";
        	 echo json_encode($response);
		}
	}
	//edit user's profile
	else if ($operation == 'editProfile')
	{
		$uId = $_POST['uId'];
		$imgUrl = $_POST['imgUrl'];
		$desc = $_POST['description'];
		$location = $_POST['location'];
		$quote = $_POST['quote'];

		$result = $db->editProfile($imgUrl, $desc, $location, $quote, $uId);
		//return success or failure
		if($result)
		{
			$response["success"] = 1;
        	echo json_encode($response);
		}
		else
		{
		     $response["error"] = 1;
        	 $response["error_msg"] = "Error editing profile.";
        	 echo json_encode($response);
		}
	}
	//add comment to a tag
	else if ($operation == 'addTagComment')
	{
		$tagId = $_POST['tagId'];
		$comment = $_POST['comment'];
		$uName = $_POST['uName'];

		$result = $db->addTagComment($tagId, $comment, $uName);
		//return comment object on success
		if($result)
		{
			$response["success"] = 1;
			$response["ID"] = $result["ID"];
			$response["ParentTagID"] = $result["ParentTagID"];
			$response["Username"] = $result["Username"];
			$response["Text"] = $result["Text"];
			$response["CreatedDateTime"] = $result["CreatedDateTime"];
        	echo json_encode($response);
		}
		else
		{
		     $response["error"] = 1;
        	 $response["error_msg"] = "Error adding comment.";
        	 echo json_encode($response);
		}
	}
	else if ($operation == 'getTagComments')
	{
		$tagId = $_POST['tagId'];

		$result = $db->getTagComments($tagId);
		//return array of comments as jsonarray
		if($result)
		{
			$rows = array();
			//loop through all rows in the result
			while($r = mysql_fetch_assoc($result))
			{
				$rows[] = $r;
			}
			echo json_encode($rows);
		}
		else
		{
		     $response["error"] = 1;
        	 $response["error_msg"] = "Error getting comments.";
        	 echo json_encode($response);
		}
	}
	//remove comment
	else if ($operation == 'deleteTagComment')
	{
		$cId = $_POST['cId'];
		$result = $db->deleteTagComment($cId);
		//return success
		if ($result)
		{
		     $response["success"] = 1;
        	     echo json_encode($response);
		}
		else
		{
		     // Error deleting tag comment
        	     $response["error"] = 1;
        	     $response["error_msg"] = "Error removing comment.";
        	     echo json_encode($response);
		}
	}
	else
	{
        echo "Invalid Request";
    }
}
else
{
    echo "Access Denied";
}
?>
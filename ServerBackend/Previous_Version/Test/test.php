<?php
	if ($operation == 'uploadImage')
	{
		$encodedImage = $_POST['imageString'];
		//path to Images base directory
		$imgFolderPath = dirname(__FILE__) . '\Images\\';
		$baseUrl = "http://10.0.2.2/geotagger/images/";
		
		//Organize Image folders by month. 
		$date = getdate();
		//append a 0 if month < 10
		$monStr = "";
		if ($date[mon] < 10) {$monStr = "0$date[mon]";}
		else{$monStr = "$date[mon]";}
		
		//create folder if it doesnt exist
		$folderName = "$monStr$date[year]";
		$folderPath = $imgFolderPath . $folderName . '\\';
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
			$url = $baseUrl . $folderName . $fileName;
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
?>
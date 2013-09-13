<?php
	echo "Test Geotagger";
	
	 $outFile = "_index.php";
   	 $out = fopen($outFile, 'w') or die("can't open write file");

	$path = "Images/writetest";
	
	$data = "TestFile";
	echo " Path = $path";
	
	if ($file = fopen($path, 'wb'))
	{
		fwrite($file, $data);
		fclose($file);
		// image saved successfully, return url in JSON
        	$response["success"] = 1;
        	$response["url"] = "Success!";
        	echo json_encode($response);
	}
	else
       	 {
       		 // Error uploading image
        	$response["error"] = 1;
        	$response["error_msg"] = "Error uploading image.";
        	echo json_encode($response);
        }
?>
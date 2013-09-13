<?php 
Print "AddTag:";
$oId = "11";
$vis = "1";
$name = "Test";
$desc = "Description";
$imgUrl = "testUrl";
$loc = "location";
$lat = "25.5";
$lon = "231.4";
$cat = "Category";

 // Connects to your Database 
mysql_connect("localhost", "root", "password"); 
Print "Connected.";
mysql_select_db("geotagger"); 
Print "DB Chosen";
$data = mysql_query("Select AddTag($oId, \"$name\", $vis, \"$desc\", \"$imgUrl\", \"$loc\", $lat, $lon,\"$cat\");");  
Print "Query Done";
if ($data == false)
{
	die(mysql_error());
	Print "Something went wrong.";
}
 else
 { 
	Print "InElse";
	Print mysql_result($data,0);
 }
 

 ?> 



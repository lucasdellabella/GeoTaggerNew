<?php
	/*
		This file is used to connect to the database
	*/

	$host="localhost";//hostname to database
	$username="root";//username to the database
	$password="123root";//password to the database
	$db_name="geotagger";//database name

	//connect to database
	mysql_connect("$host", "$username", "$password") or die("cannot connect to server");
	mysql_select_db("$db_name") or die("cannot select database");
?>
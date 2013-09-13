 <?php 
 // Connects to your Database 
 Print "before conn";
 mysql_connect("localhost", "root", "password") or die(Print "Error connecting: " );
 Print "before select"; 
 mysql_select_db("geotagger") or die(mysql_error()); 
 $data = mysql_query("SELECT * FROM accounts") or die(mysql_error()); 
 Print "<table border cellpadding=3>"; 
 Print "before fetch";
 while($info = mysql_fetch_array( $data )) 
 { 
 Print "<tr>"; 
 Print "<th>ID:</th> <td>".$info['AccountID'] . "</td> "; 
 Print "<th>UN:</th> <td>".$info['Username'] . "</td> "; 
 Print "<th>Name:</th> <td>".$info['Name'] . "</td> "; 
 Print "<th>Email:</th> <td>".$info['EmailAddress'] . " </td></tr>"; 
 } 
 Print "</table>"; 
 Print "Done";
 ?> 

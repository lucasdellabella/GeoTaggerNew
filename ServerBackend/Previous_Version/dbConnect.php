<?php
 
class dbConnect {
  
    // Connecting to database
    public function connect() {
        require_once 'dbConfig.php';
        // connecting to mysql
        $con = mysql_connect(DB_Server, DB_User, DB_Password);
        // selecting database
        mysql_select_db(DB_Name);
 
        // return database handler
        return $con;
    }
 
    // Closing database connection
    public function close() {
        mysql_close();
    }
 
}
 
?>
-- MySQL dump 10.11
--
-- Host: localhost    Database: geotagger
-- ------------------------------------------------------
-- Server version	5.0.92-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts` (
  `AccountID` int(10) unsigned NOT NULL auto_increment,
  `Username` varchar(15) NOT NULL default 'New User',
  `Name` varchar(45) default NULL,
  `EmailAddress` varchar(45) default NULL,
  `Password` varchar(20) NOT NULL,
  `Image` blob,
  `Description` mediumtext,
  `Location` varchar(50) default NULL,
  `Quote` tinytext,
  `Type` tinyint(4) NOT NULL default '0',
  `Visibility` tinyint(4) NOT NULL default '1',
  `CreatedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `RatingScore` int(11) NOT NULL default '0',
  PRIMARY KEY  (`AccountID`),
  UNIQUE KEY `AccountID_UNIQUE` (`AccountID`),
  UNIQUE KEY `Username_UNIQUE` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1 COMMENT='Stores User Account data';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (4,'Testuser1','Chris L','loeschornc1@mail.montclair.edu','pass','null','This is my test account','NEW JERSEY','\"Hello\"',1,1,'2013-04-28 17:44:03',0),(9,'Testuser',NULL,NULL,'testpw',NULL,NULL,NULL,NULL,1,1,'2013-03-06 23:14:21',0),(11,'Chris',NULL,NULL,'mypass','http://hci.montclair.edu/geotagger/Images/042013/517dee0e7c348.jpg','My name is Chris. I am a grad student at Montclair state. I work for a software company and enjoy cars and computers. ','New Jersey','My quote',1,1,'2013-04-29 03:50:38',0),(12,'Robotico',NULL,NULL,'Derpyy','http://hci.montclair.edu/geotagger/Images/042013/517df84f9a6a0.jpg','I am a robot!','Robot Land ','Errrrrrr',1,1,'2013-04-29 04:34:23',0),(13,'Amanda1234',NULL,NULL,'sunnyD',NULL,NULL,NULL,NULL,1,1,'2013-04-05 01:50:52',0),(14,'jfails',NULL,NULL,'300777',NULL,NULL,NULL,NULL,1,1,'2013-04-08 23:35:16',0),(15,'groupuser',NULL,NULL,'password',NULL,NULL,NULL,NULL,1,1,'2013-04-18 05:44:44',0),(16,'p8tro',NULL,NULL,'testee',NULL,NULL,NULL,NULL,1,1,'2013-04-18 16:43:27',0),(17,'kidsteamgirls10',NULL,NULL,'Thedreamer17',NULL,NULL,NULL,NULL,1,1,'2013-04-18 20:29:25',0),(18,'Kidsteammsu',NULL,NULL,'jerry1234',NULL,NULL,NULL,NULL,1,1,'2013-04-18 20:31:10',0),(19,'NewUser',NULL,NULL,'password',NULL,NULL,NULL,NULL,1,1,'2013-04-26 01:41:49',0),(20,'Failsj',NULL,NULL,'300777','','I am a pretty cool guy','Paris, France','Wherever You Are There You Are',1,1,'2013-04-30 07:42:26',0),(21,'msustudent',NULL,NULL,'montclair',NULL,NULL,NULL,NULL,1,1,'2013-04-30 15:02:07',0),(22,'DemoUser',NULL,NULL,'password',NULL,NULL,NULL,NULL,1,1,'2013-05-01 00:54:23',0);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adventures`
--

DROP TABLE IF EXISTS `adventures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adventures` (
  `ID` int(10) unsigned NOT NULL auto_increment,
  `OwnerID` int(10) unsigned NOT NULL,
  `Name` varchar(45) NOT NULL,
  `Description` text,
  `Location` tinytext,
  `Visibility` tinyint(4) NOT NULL,
  `CreatedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ID`),
  KEY `fk_AdvOwner_idx` (`ID`),
  KEY `fk_oID_idx` (`ID`),
  KEY `fk_oID` (`OwnerID`),
  CONSTRAINT `fk_oID` FOREIGN KEY (`OwnerID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adventures`
--

LOCK TABLES `adventures` WRITE;
/*!40000 ALTER TABLE `adventures` DISABLE KEYS */;
INSERT INTO `adventures` VALUES (6,4,'New Adv',NULL,NULL,1,'2013-02-04 23:15:52');
/*!40000 ALTER TABLE `adventures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adventuretags`
--

DROP TABLE IF EXISTS `adventuretags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adventuretags` (
  `AdvID` int(10) unsigned NOT NULL,
  `TagID` bigint(20) unsigned NOT NULL,
  `TagIndex` int(10) unsigned NOT NULL,
  KEY `fk_AdvID_idx` (`AdvID`),
  KEY `fk_TagID_idx` (`TagID`),
  CONSTRAINT `fk_AdvID` FOREIGN KEY (`AdvID`) REFERENCES `adventures` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TagID` FOREIGN KEY (`TagID`) REFERENCES `tags` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adventuretags`
--

LOCK TABLES `adventuretags` WRITE;
/*!40000 ALTER TABLE `adventuretags` DISABLE KEYS */;
/*!40000 ALTER TABLE `adventuretags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friendassociations`
--

DROP TABLE IF EXISTS `friendassociations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friendassociations` (
  `uID` int(10) unsigned NOT NULL,
  `fID` int(10) unsigned NOT NULL,
  KEY `fk_uID_idx` (`uID`),
  KEY `fk_fID_idx` (`fID`),
  CONSTRAINT `fk_fID` FOREIGN KEY (`fID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_uID` FOREIGN KEY (`uID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendassociations`
--

LOCK TABLES `friendassociations` WRITE;
/*!40000 ALTER TABLE `friendassociations` DISABLE KEYS */;
INSERT INTO `friendassociations` VALUES (11,12),(12,11),(11,4),(4,11),(11,9),(9,11),(11,14),(14,11),(4,14),(14,4),(21,11),(11,21),(22,11),(11,22);
/*!40000 ALTER TABLE `friendassociations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupadventures`
--

DROP TABLE IF EXISTS `groupadventures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupadventures` (
  `gID` int(10) unsigned NOT NULL,
  `aID` int(10) unsigned NOT NULL,
  `AddedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  KEY `fk_groupID_idx` (`gID`),
  KEY `fk_acctID_idx` (`aID`),
  CONSTRAINT `fk1_acctID` FOREIGN KEY (`aID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk1_groupID` FOREIGN KEY (`gID`) REFERENCES `groups` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupadventures`
--

LOCK TABLES `groupadventures` WRITE;
/*!40000 ALTER TABLE `groupadventures` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupadventures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupmembers`
--

DROP TABLE IF EXISTS `groupmembers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupmembers` (
  `GroupID` int(10) unsigned NOT NULL,
  `aID` int(10) unsigned NOT NULL,
  `MemberType` tinyint(4) NOT NULL,
  `MemberSince` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  KEY `fk_gID_idx` (`GroupID`),
  KEY `fk_aID_idx` (`aID`),
  CONSTRAINT `fk_aID` FOREIGN KEY (`aID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_groupID` FOREIGN KEY (`GroupID`) REFERENCES `groups` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupmembers`
--

LOCK TABLES `groupmembers` WRITE;
/*!40000 ALTER TABLE `groupmembers` DISABLE KEYS */;
INSERT INTO `groupmembers` VALUES (3,4,0,'2013-02-10 22:11:42');
/*!40000 ALTER TABLE `groupmembers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `ID` int(10) unsigned NOT NULL auto_increment,
  `Name` tinytext NOT NULL,
  `Description` text,
  `Image` blob,
  `CreationDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (3,'TestGroup','This is my test group',NULL,'2013-02-10 22:11:41');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grouptags`
--

DROP TABLE IF EXISTS `grouptags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grouptags` (
  `GroupID` int(10) unsigned NOT NULL,
  `TagID` bigint(20) unsigned NOT NULL,
  `AddedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  KEY `fk_gID_idx` (`GroupID`),
  KEY `fk_tID_idx` (`TagID`),
  CONSTRAINT `fk_gID` FOREIGN KEY (`GroupID`) REFERENCES `groups` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_tID` FOREIGN KEY (`TagID`) REFERENCES `tags` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grouptags`
--

LOCK TABLES `grouptags` WRITE;
/*!40000 ALTER TABLE `grouptags` DISABLE KEYS */;
/*!40000 ALTER TABLE `grouptags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pendingrequests`
--

DROP TABLE IF EXISTS `pendingrequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pendingrequests` (
  `RequestID` bigint(20) unsigned NOT NULL auto_increment,
  `senderID` int(10) unsigned NOT NULL,
  `recipientID` int(10) unsigned NOT NULL,
  `Message` text,
  `RequestType` tinyint(4) NOT NULL,
  `Timestamp` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`RequestID`),
  KEY `fk_Sender_idx` (`senderID`),
  KEY `fk_Recipient_idx` (`recipientID`),
  CONSTRAINT `fk_Recipient` FOREIGN KEY (`recipientID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Sender` FOREIGN KEY (`senderID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pendingrequests`
--

LOCK TABLES `pendingrequests` WRITE;
/*!40000 ALTER TABLE `pendingrequests` DISABLE KEYS */;
/*!40000 ALTER TABLE `pendingrequests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tagcomments`
--

DROP TABLE IF EXISTS `tagcomments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tagcomments` (
  `ID` bigint(20) unsigned NOT NULL auto_increment,
  `ParentTagID` bigint(20) unsigned NOT NULL,
  `Username` varchar(15) default NULL,
  `Title` tinytext,
  `Text` text,
  `CreatedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `RatingScore` int(11) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  KEY `fk_pTagID_idx` (`ParentTagID`),
  CONSTRAINT `fk_pTagID` FOREIGN KEY (`ParentTagID`) REFERENCES `tags` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tagcomments`
--

LOCK TABLES `tagcomments` WRITE;
/*!40000 ALTER TABLE `tagcomments` DISABLE KEYS */;
INSERT INTO `tagcomments` VALUES (1,19,'Chris',NULL,'My Tag Comment','2013-04-28 22:05:20',0),(2,19,'Chris',NULL,'My Tag Comment','2013-04-28 22:06:17',0),(3,19,'Chris',NULL,'Third tag Comment','2013-04-28 22:12:30',0),(6,71,'Chris',NULL,'Nice game!','2013-04-29 00:10:12',0),(10,71,'Chris',NULL,'Hey!','2013-04-29 00:25:42',0),(11,70,'Chris',NULL,'Nice tag!','2013-04-29 02:54:59',0),(14,69,'Chris',NULL,'Nice picture!','2013-04-29 03:43:39',0),(17,72,'Robotico',NULL,'This is my tag!','2013-04-29 04:17:20',0),(19,71,'Robotico',NULL,'I was watching this.game too!','2013-04-29 04:23:56',0),(20,73,'DemoUser',NULL,'My first comment on my own tag!','2013-05-01 00:59:01',0),(21,71,'DemoUser',NULL,'I did not get to see this, who won? ','2013-05-01 01:01:47',0);
/*!40000 ALTER TABLE `tagcomments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `TagID` bigint(20) unsigned NOT NULL auto_increment,
  `OwnerID` int(10) unsigned NOT NULL,
  `Visibility` int(11) NOT NULL default '1',
  `Name` varchar(35) NOT NULL,
  `Description` text,
  `ImageUrl` tinytext,
  `Location` tinytext,
  `Latitude` double default NULL,
  `Longitude` double default NULL,
  `CreatedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `Category` tinytext,
  `RatingScore` int(11) NOT NULL default '0',
  PRIMARY KEY  (`TagID`),
  UNIQUE KEY `TagID_UNIQUE` (`TagID`),
  KEY `oID_idx` (`OwnerID`),
  CONSTRAINT `fk_tagOwner` FOREIGN KEY (`OwnerID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (19,11,1,'My tag','','','',NULL,NULL,'2013-04-11 00:44:07','Default',0),(20,11,1,'Imagetag','','http://hci.montclair.edu/geotagger/Images/042013/51660e9127b59.jpg','',NULL,NULL,'2013-04-11 01:14:58','Default',0),(22,11,1,'asdf','','http://hci.montclair.edu/geotagger/Images/042013/51662007bc2f6.jpg','',NULL,NULL,'2013-04-11 02:29:28','Default',0),(23,11,1,'fsdasd','','http://hci.montclair.edu/geotagger/Images/042013/51662041ae836.jpg','',NULL,NULL,'2013-04-11 02:30:26','Default',0),(24,11,1,'gallery test','','','',NULL,NULL,'2013-04-11 02:38:44','Default',0),(26,11,1,'phone gallery tag','tag from gallery','','',NULL,NULL,'2013-04-11 02:50:09','Default',0),(27,11,1,'phone camera tag','','','',NULL,NULL,'2013-04-11 02:50:50','Default',0),(29,11,1,'gallery','','http://hci.montclair.edu/geotagger/Images/042013/5166283b7cc98.jpg','',NULL,NULL,'2013-04-11 03:04:27','Default',0),(30,11,1,'android tag','','http://hci.montclair.edu/geotagger/Images/042013/516766253beca.jpg','',NULL,NULL,'2013-04-12 01:40:53','Default',0),(35,11,1,'Test img','','http://hci.montclair.edu/geotagger/Images/042013/51677b372be6d.jpg','',NULL,NULL,'2013-04-12 03:10:47','Default',0),(39,16,1,'Nagano','Soy.sauce','http://hci.montclair.edu/geotagger/Images/042013/51702304a9bbd.jpg','',NULL,NULL,'2013-04-18 16:44:53','Default',0),(40,18,1,'Kids team room','The room where kids team works.','http://hci.montclair.edu/geotagger/Images/042013/517059456253d.jpg','The best part of MSU ',NULL,NULL,'2013-04-18 20:36:21','Default',0),(41,17,1,'Women\'sroom','Next to room 117 say women in bright red ','','',NULL,NULL,'2013-04-18 20:38:24','Default',0),(42,18,1,'Creepy Abandon Room','CREEPY ','http://hci.montclair.edu/geotagger/Images/042013/51705abfe2b68.jpg','',NULL,NULL,'2013-04-18 20:42:39','Default',0),(43,18,1,'Creepy Abandon Room','CREEPY ','http://hci.montclair.edu/geotagger/Images/042013/51705ac2b8736.jpg','',NULL,NULL,'2013-04-18 20:42:42','Default',0),(44,18,1,'Computer room','','http://hci.montclair.edu/geotagger/Images/042013/51705b50866c9.jpg','',NULL,NULL,'2013-04-18 20:45:04','Default',0),(45,17,1,'Students longe','Its right across from the bathrooms ','','',NULL,NULL,'2013-04-18 20:47:28','Default',0),(46,18,1,'Construction','','http://hci.montclair.edu/geotagger/Images/042013/51705be1d71f8.jpg','',NULL,NULL,'2013-04-18 20:47:29','Default',0),(47,18,1,'Schedules','schedules','http://hci.montclair.edu/geotagger/Images/042013/51705cdb42ba7.jpg','',NULL,NULL,'2013-04-18 20:51:39','Default',0),(48,18,1,'Astronomy board','The astronomy class put up a buliten board with news papers about space on it','','',NULL,NULL,'2013-04-18 20:57:05','Default',0),(49,18,1,'Bio hazard','Ahhhhh','http://hci.montclair.edu/geotagger/Images/042013/51705ee346676.jpg','',NULL,NULL,'2013-04-18 21:00:19','Default',0),(50,18,1,'Shower','','http://hci.montclair.edu/geotagger/Images/042013/51705fd028d15.jpg','',NULL,NULL,'2013-04-18 21:04:16','Default',0),(51,18,1,'Science Lobby','Lobby O\' Science ','http://hci.montclair.edu/geotagger/Images/042013/517061496737c.jpg','',NULL,NULL,'2013-04-18 21:10:33','Default',0),(69,11,1,'Nico','Nico in the office ','http://hci.montclair.edu/geotagger/Images/042013/5177e5c58b023.jpg','asci',NULL,NULL,'2013-04-24 14:01:41','Default',0),(70,11,1,'Image Tag','Tag with an image from the camera.s','http://hci.montclair.edu/geotagger/Images/042013/517884d3bbfb3.jpg','',NULL,NULL,'2013-04-25 01:20:20','Default',0),(71,11,1,'Baseball game','This is a picture of the baseball game I was watching.\n','http://hci.montclair.edu/geotagger/Images/042013/517d90613ae1c.jpg','In my house',NULL,NULL,'2013-04-28 21:10:57','Default',0),(72,12,1,'Database','The database that makes it all happen!','http://hci.montclair.edu/geotagger/Images/042013/517df3a5e22e3.jpg','',NULL,NULL,'2013-04-29 04:14:30','Default',0),(73,22,1,'My first tag ','This is my first tag. It is a picture I just took of my keyboard!','http://hci.montclair.edu/geotagger/Images/042013/5180689a42cf7.jpg','In my house',NULL,NULL,'2013-05-01 00:58:02','Default',0);
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'geotagger'
--
DELIMITER ;;
/*!50003 DROP FUNCTION IF EXISTS `AddFriend` */;;
/*!50003 SET SESSION SQL_MODE=""*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 FUNCTION `AddFriend`(curUserID INT, searchUserName varchar(15)) RETURNS int(11)
    DETERMINISTIC
BEGIN

-- get user's id from their name
Declare searchUserID Int;
Declare result Int;
Set searchUserID = (Select AccountID from Accounts a where a.Username = searchUserName);
-- add friend only if the two users are not already friends
IF (searchUserID IS NOT NULL) Then
    IF (Select Count(*) from FriendAssociations f where f.uID = curUserID and f.fID = searchUserID) = 0 THEN
        Insert into FriendAssociations(uID, fID)
            Values (curUserID, searchUserID);
        Insert into FriendAssociations(uID, fID) 
            Values (searchUserID, curUserID);
        Set result = 1; -- return 1 for success
    Else
        Set result = 0; -- return 0 if already friends
    End IF;
Else
    Set result = -1; -- return -1 if user does not exist
End IF;
Return result;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP FUNCTION IF EXISTS `AddTag` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 FUNCTION `AddTag`(
 ownerID INT, tagName varchar(35), Vis int, Descr Text, Image TinyText, Location TinyText,
		Lat double, Lon double, cat tinytext) RETURNS mediumtext CHARSET latin1
    DETERMINISTIC
BEGIN

	Insert into Tags(ownerID, Visibility, Name, Description, ImageUrl, Location, Latitude, 
		Longitude, CreatedDateTime, Category)
	Values(ownerID, Vis, tagName, Descr, Image, Location,
		Lat, Lon, now(), cat);

	Return LAST_INSERT_ID();

END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP FUNCTION IF EXISTS `Adv_GetNextIndex` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 FUNCTION `Adv_GetNextIndex`(advID INT) RETURNS int(11)
    DETERMINISTIC
BEGIN
Declare nextIndex Int;
Declare count Int;
Declare done Boolean;

Set count = (Select count(*) from AdventureTags where AdvID = advID)+1;
Set done = FALSE;
-- check the index to ensure it is unique, if not increment until a unique index is found
While !done DO
	IF (Select count(*) from AdventureTags where AdvID = advID and TagIndex = count) > 0 
	THEN
		set count = count + 1;
	Else set done = TRUE;
	End IF;
End While;


RETURN count;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP FUNCTION IF EXISTS `TagExists_InAdv` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 FUNCTION `TagExists_InAdv`(advID INT, tagID BIGINT) RETURNS int(11)
    DETERMINISTIC
BEGIN
	Declare chk INT;
	-- if the tag does not exist in the adventure, 0 will be returned
	Select Count(*) into chk from AdventureTags A Where A.AdvID = advID AND A.TagID = tagID;
	return chk;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP FUNCTION IF EXISTS `TagExists_InGroup` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 FUNCTION `TagExists_InGroup`(gID INT, tID BIGINT) RETURNS int(11)
    DETERMINISTIC
BEGIN
	Declare chk INT;
	-- if the tag does not exist in the group, 0 will be returned
	Select Count(*) into chk from GroupTags g Where g.GroupID = gID AND g.TagID = tID;
	return chk;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `AddAccount` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `AddAccount`(
IN uName varchar(15),
IN Pw varchar(20)
)
BEGIN
	 Insert into Accounts(Username, Password, Type, Visibility,
			CreatedDateTime)
	VALUES(uName, Pw, 1, 1, now());
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `AddAdventure` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `AddAdventure`(
IN oID INT,
IN Name varchar(45),
IN des Text,
IN loc TinyText,
In vis TinyInt
)
BEGIN

	Insert Into Adventures(OwnerID, Name, Description, Location,
		Visibility, CreatedDateTime) 
	Values(oID, Name, des, loc, vis, now());
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `AddFriend_OLD` */;;
/*!50003 SET SESSION SQL_MODE=""*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `AddFriend_OLD`(
	IN curUserID INT,
	IN searchUserName varchar(15),
	Out result INT
)
BEGIN
-- get user's id from their name
Declare searchUserID Int;
Set searchUserID = (Select AccountID from Accounts a where a.Username = searchUserName);
-- add friend only if the two users are not already friends 
IF (searchUserID IS NOT NULL) Then
	IF (Select Count(*) from FriendAssociations f where f.uID = curUserID and f.fID = searchUserID) = 0 THEN
		Insert Into FriendAssociations(uID, fID)
			Values (curUserID, searchUserID);
		Insert Into FriendAssociations(uID, fID)
			Values (searchUserID, curUserID);
		Set result = 1; -- return 1 for success
	Else
		Set result = 0; -- return 0 if already friends
	End IF;
Else 
	Set result = -1; -- return -1 if user does not exist
End IF;

END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `AddGroup` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `AddGroup`(
IN n Tinytext,
IN descr Text,
In creatorID INT
)
BEGIN
	Insert INTO Groups(Name, Description, CreationDateTime)
		Values(n, descr, now());
	Insert INTO GroupMembers(GroupID, aID, MemberType, MemberSince)
		Values(LAST_INSERT_ID(), creatorID, 0, now());
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `AddTagComment` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `AddTagComment`(
	IN pID BIGINT,
	IN ttl tinyText,
	IN txt Text
)
BEGIN
	Insert into TagComments(ParentTagID, Title, Text, CreatedDateTime) 
		Values(pID, ttl, txt, now());
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `AddTag_OLD` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `AddTag_OLD`(
IN ownerID INT,
IN tagName varchar(35),
IN Vis int,
IN Descr Text,
In Image TinyText,
IN Location TinyText,
IN Lat double,
IN Lon double,
IN cat tinytext,
Out tagId long
)
BEGIN

	Insert into Tags(ownerID, Visibility, Name, Description, Image, Location, Latitude, 
		Longitude, CreatedDateTime, Category)
	Values(ownerID, Vis, tagName, Descr, Image, Location,
		Lat, Lon, now(), cat);

	Set tagId = 12345;
	

END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `AddTag_ToAdventure` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `AddTag_ToAdventure`(
In advID INT,
In tagID BigInt
)
BEGIN
	Declare i INT;
	IF(TagExists_InAdv(advID, tagID)) = 0 THEN
		SET i = (Adv_GetNextIndex(advID));
		Insert into AdventureTags(AdvID, TagID, TagIndex)
			VALUES(advID,tagID, i);
	END IF;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `AddTag_ToGroup` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `AddTag_ToGroup`(
IN gID INT,
IN tID BIGINT
)
BEGIN
	IF(TagExists_InGroup(gID, tID)) = 0 THEN
		Insert Into GroupTags(GroupID, TagID, AddedDateTime)
			Values(gID, tID, now());
	End IF;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `DeleteTagComment` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `DeleteTagComment`(
	IN cID BIGINT
)
BEGIN
-- Delete a specified comment from a tag
	Delete from TagComments where ID = cID;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `EditAccount` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `EditAccount`(
IN uName varchar(15),
IN name varchar(45),
IN Email varchar(45),
IN Pw varchar(20),
In Img blob,
In Des MEDIUMTEXT,
In Loc varchar(50),
In Quote TinyText,
In typ tinyint,
In vis tinyint
)
BEGIN
	 Insert into Accounts(Username, Name, EmailAddress, Password,
		Image, Description, Location, Quote, Type, Visibility,
			CreatedDateTime)
	VALUES(uName, name, Email, Pw, Img, Des, Loc, Quote,
		typ, vis, now());
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `RemoveTag_FromGroup` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `RemoveTag_FromGroup`(
gID INT,
tID BIGINT
)
BEGIN
-- remove the specified tag from the groupTags of the given group
Delete from GroupTags where GroupID = gID and TagID = tID;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `SendRequest` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `SendRequest`(
	IN sID INT,
	IN rID INT,
	IN msg TEXT,
	IN typ TinyINT
)
BEGIN
-- send a request if one of the same type does not already exist between the two users. 
	IF (Select Count(*) from PendingRequests p where p.senderID = sID and p.recipientID = rID and p.RequestType = typ) = 0 THEN
		Insert Into PendingRequests(senderID, recipientID, Message, RequestType, TimeStamp)
			Values (sID, rID, msg, typ, now());
	End IF;
	
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `Test_AddAccounts` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `Test_AddAccounts`(
IN numUsers INT
)
BEGIN
declare i INT;
declare name varchar(20);

Select Count(*) into i from Accounts;
Set numUsers = numUsers + i;

While i < numUsers DO

	set name = CONCAT('User', i);

	Insert into Accounts (Username, Name, Password, Description, 
		Type, Visibility, CreatedDateTime)
	Values(name, name, 'testpw', 'my test account',1, 1, now());
	
	SET i = i+1;
End While;
END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
/*!50003 DROP PROCEDURE IF EXISTS `Test_AddTestAccount` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `Test_AddTestAccount`()
BEGIN

	call AddAccount('Testuser1', 'Chris L', 'loeschornc1@mail.montclair.edu',
		'pass', null, 'This is my test account', 'NJ', '"Hello"', 
			1,1);

END */;;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE*/;;
DELIMITER ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-01  0:16:03

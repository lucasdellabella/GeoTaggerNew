# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 130.68.20.197 (MySQL 5.0.92-log)
# Database: geotagger
# Generation Time: 2013-09-13 04:38:13 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table accounts
# ------------------------------------------------------------

DROP TABLE IF EXISTS `accounts`;

CREATE TABLE `accounts` (
  `AccountID` int(10) unsigned NOT NULL auto_increment,
  `Username` varchar(100) NOT NULL default 'New User',
  `Name` varchar(45) default NULL,
  `EmailAddress` varchar(45) default NULL,
  `Password` varchar(80) NOT NULL default '',
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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1 COMMENT='Stores User Account data';

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;

INSERT INTO `accounts` (`AccountID`, `Username`, `Name`, `EmailAddress`, `Password`, `Image`, `Description`, `Location`, `Quote`, `Type`, `Visibility`, `CreatedDateTime`, `RatingScore`)
VALUES
	(4,'Testuser1','Chris L','loeschornc1@mail.montclair.edu','1a1dc91c907325c69271ddf0c944bc72','','This is my test account','NEW JERSEY','\"Hello\"',1,1,'2013-08-19 01:11:54',0),
	(9,'Testuser',NULL,NULL,'8eee3efdde1eb6cf6639a58848362bf4',NULL,NULL,NULL,NULL,1,1,'2013-08-19 01:11:54',0),
	(11,'Chris',NULL,NULL,'a029d0df84eb5549c641e04a9ef389e5',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3034323031332F353137646565306537633334382E6A7067','My name is Chris. I am a grad student at Montclair state. I work for a software company and enjoy cars and computers. ','New Jersey','My quote',1,1,'2013-08-19 01:11:54',0),
	(12,'Robotico',NULL,NULL,'272e77718e37084bcc9d2703de7d48b4',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3034323031332F353137646638346639613661302E6A7067','I am a robot!','Robot Land ','Errrrrrr',1,1,'2013-08-19 01:11:54',0),
	(13,'Amanda1234',NULL,NULL,'3f400ad443aae200a1c16f28bac30d89',NULL,NULL,NULL,NULL,1,1,'2013-08-19 01:11:54',0),
	(14,'jfails',NULL,NULL,'9c1356267b26242dad91de0289680e7a',NULL,NULL,NULL,NULL,1,1,'2013-08-19 01:11:54',0),
	(15,'groupuser',NULL,NULL,'5f4dcc3b5aa765d61d8327deb882cf99',NULL,NULL,NULL,NULL,1,1,'2013-08-19 01:11:54',0),
	(16,'p8tro',NULL,NULL,'0dedc0f8254073a55ce8c47941d6b8e1',NULL,NULL,NULL,NULL,1,1,'2013-08-19 01:11:54',0),
	(17,'kidsteamgirls10',NULL,NULL,'868176df3355a2928acbab59af1b7326',NULL,NULL,NULL,NULL,1,1,'2013-08-19 01:11:54',0),
	(18,'Kidsteammsu',NULL,NULL,'2584474d5eced33dcb575003abb0ac41',NULL,NULL,NULL,NULL,1,1,'2013-08-19 01:11:54',0),
	(19,'NewUser',NULL,NULL,'5f4dcc3b5aa765d61d8327deb882cf99',NULL,NULL,NULL,NULL,1,1,'2013-08-19 01:11:54',0),
	(20,'Failsj',NULL,NULL,'9c1356267b26242dad91de0289680e7a','','','Paris, France Yeah','Wherever You Are There You Are',1,1,'2013-08-21 14:11:00',0),
	(21,'msustudent',NULL,NULL,'6651207704ec53e2b99b23ed13cdcc8b',NULL,NULL,NULL,NULL,1,1,'2013-08-19 01:11:54',0),
	(22,'DemoUser',NULL,NULL,'5f4dcc3b5aa765d61d8327deb882cf99',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3035323031332F353138316561643566333237372E6A7067','I am a new user. ','The World','\"I love geotagger!\"',1,1,'2013-08-19 01:11:54',0),
	(23,'ddymko',NULL,NULL,'b1b8e4f670b6722fffc328d7ee938b77','','','New Jersy','',1,1,'2013-09-03 13:32:34',0),
	(24,'zillwc','Zill Christian','zillwc@gmail.com','1c0b76fce779f78f51be339c49445c49','','','Sayreville, NJ','hello world',1,1,'2013-08-21 14:10:42',0),
	(25,'bwayne','Bruce Wayne','bwayne@wenterprise.com','ec0e2603172c73a8b644bb9456c1ff6e',X'6261746D616E2E6A7067','the hero gotham deserves','Gotham','Justice!',0,1,'2013-08-20 05:02:08',0),
	(29,'Spencer',NULL,NULL,'7c9fb847d117531433435b68b61f91f6',NULL,NULL,NULL,NULL,1,1,'2013-08-25 22:08:27',0),
	(30,'andrew',NULL,NULL,'47fab60bdcd2ffce91447d19fe9ce7f1','','','Hell','',1,1,'2013-08-26 18:29:23',0);

/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adventuremembers
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adventuremembers`;

CREATE TABLE `adventuremembers` (
  `AdvID` int(10) unsigned NOT NULL,
  `uID` int(10) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table adventures
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adventures`;

CREATE TABLE `adventures` (
  `ID` int(10) unsigned NOT NULL auto_increment,
  `OwnerID` int(10) unsigned NOT NULL,
  `Name` varchar(45) NOT NULL,
  `Description` text,
  `Visibility` tinyint(4) NOT NULL default '1',
  `CreatedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ID`),
  KEY `fk_AdvOwner_idx` (`ID`),
  KEY `fk_oID_idx` (`ID`),
  KEY `fk_oID` (`OwnerID`),
  CONSTRAINT `fk_oID` FOREIGN KEY (`OwnerID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

LOCK TABLES `adventures` WRITE;
/*!40000 ALTER TABLE `adventures` DISABLE KEYS */;

INSERT INTO `adventures` (`ID`, `OwnerID`, `Name`, `Description`, `Visibility`, `CreatedDateTime`)
VALUES
	(6,4,'New Adv',NULL,1,'2013-02-04 18:15:52'),
	(7,4,'Test Adv','This is a test adv to add some test data',1,'2013-08-15 18:46:58'),
	(8,30,'Newadv','Fesc',1,'2013-09-12 15:30:20'),
	(9,30,'Chdatunl','Xsgunbkpe',1,'2013-09-12 15:32:10'),
	(10,23,'David tesy','',1,'2013-09-12 15:53:27'),
	(11,30,'Fdiddhjb','Xfvj',1,'2013-09-12 16:03:57'),
	(12,30,'Jdj jdjsbks','Djfkdnd',1,'2013-09-12 16:15:40'),
	(13,23,'Test','Ri387',1,'2013-09-12 16:45:26'),
	(14,23,'Headaches','Show up ',1,'2013-09-12 16:46:14'),
	(15,23,'Test','from db\n',1,'2013-09-12 17:31:30');

/*!40000 ALTER TABLE `adventures` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adventuretags
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adventuretags`;

CREATE TABLE `adventuretags` (
  `AdvID` int(10) unsigned NOT NULL,
  `TagID` bigint(20) unsigned NOT NULL,
  `TagIndex` int(10) unsigned default '0',
  KEY `fk_AdvID_idx` (`AdvID`),
  KEY `fk_TagID_idx` (`TagID`),
  CONSTRAINT `fk_AdvID` FOREIGN KEY (`AdvID`) REFERENCES `adventures` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TagID` FOREIGN KEY (`TagID`) REFERENCES `tags` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `adventuretags` WRITE;
/*!40000 ALTER TABLE `adventuretags` DISABLE KEYS */;

INSERT INTO `adventuretags` (`AdvID`, `TagID`, `TagIndex`)
VALUES
	(7,80,0),
	(7,81,0),
	(6,82,0);

/*!40000 ALTER TABLE `adventuretags` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table friendassociations
# ------------------------------------------------------------

DROP TABLE IF EXISTS `friendassociations`;

CREATE TABLE `friendassociations` (
  `uID` int(10) unsigned NOT NULL,
  `fID` int(10) unsigned NOT NULL,
  KEY `fk_uID_idx` (`uID`),
  KEY `fk_fID_idx` (`fID`),
  CONSTRAINT `fk_fID` FOREIGN KEY (`fID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_uID` FOREIGN KEY (`uID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `friendassociations` WRITE;
/*!40000 ALTER TABLE `friendassociations` DISABLE KEYS */;

INSERT INTO `friendassociations` (`uID`, `fID`)
VALUES
	(11,12),
	(12,11),
	(11,4),
	(4,11),
	(11,9),
	(9,11),
	(11,14),
	(14,11),
	(4,14),
	(14,4),
	(21,11),
	(11,21),
	(22,11),
	(11,22),
	(11,20),
	(20,11),
	(4,24),
	(24,4),
	(24,20),
	(20,24),
	(24,25),
	(30,29),
	(29,23);

/*!40000 ALTER TABLE `friendassociations` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table groupadventures
# ------------------------------------------------------------

DROP TABLE IF EXISTS `groupadventures`;

CREATE TABLE `groupadventures` (
  `gID` int(10) unsigned NOT NULL,
  `aID` int(10) unsigned NOT NULL,
  `AddedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  KEY `fk_groupID_idx` (`gID`),
  KEY `fk_acctID_idx` (`aID`),
  CONSTRAINT `fk1_acctID` FOREIGN KEY (`aID`) REFERENCES `accounts` (`AccountID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk1_groupID` FOREIGN KEY (`gID`) REFERENCES `groups` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table groupmembers
# ------------------------------------------------------------

DROP TABLE IF EXISTS `groupmembers`;

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

LOCK TABLES `groupmembers` WRITE;
/*!40000 ALTER TABLE `groupmembers` DISABLE KEYS */;

INSERT INTO `groupmembers` (`GroupID`, `aID`, `MemberType`, `MemberSince`)
VALUES
	(3,4,0,'2013-02-10 17:11:42');

/*!40000 ALTER TABLE `groupmembers` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table groups
# ------------------------------------------------------------

DROP TABLE IF EXISTS `groups`;

CREATE TABLE `groups` (
  `ID` int(10) unsigned NOT NULL auto_increment,
  `Name` tinytext NOT NULL,
  `Description` text,
  `Image` blob,
  `CreationDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;

INSERT INTO `groups` (`ID`, `Name`, `Description`, `Image`, `CreationDateTime`)
VALUES
	(3,'TestGroup','This is my test group',NULL,'2013-02-10 17:11:41');

/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table grouptags
# ------------------------------------------------------------

DROP TABLE IF EXISTS `grouptags`;

CREATE TABLE `grouptags` (
  `GroupID` int(10) unsigned NOT NULL,
  `TagID` bigint(20) unsigned NOT NULL,
  `AddedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  KEY `fk_gID_idx` (`GroupID`),
  KEY `fk_tID_idx` (`TagID`),
  CONSTRAINT `fk_gID` FOREIGN KEY (`GroupID`) REFERENCES `groups` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_tID` FOREIGN KEY (`TagID`) REFERENCES `tags` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table pendingrequests
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pendingrequests`;

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



# Dump of table tagcomments
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tagcomments`;

CREATE TABLE `tagcomments` (
  `ID` bigint(20) unsigned NOT NULL auto_increment,
  `ParentTagID` bigint(20) unsigned NOT NULL,
  `Username` varchar(15) default NULL,
  `Title` tinytext,
  `Text` text,
  `ImageUrl` tinytext,
  `CreatedDateTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `RatingScore` int(11) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  KEY `fk_pTagID_idx` (`ParentTagID`),
  CONSTRAINT `fk_pTagID` FOREIGN KEY (`ParentTagID`) REFERENCES `tags` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;

LOCK TABLES `tagcomments` WRITE;
/*!40000 ALTER TABLE `tagcomments` DISABLE KEYS */;

INSERT INTO `tagcomments` (`ID`, `ParentTagID`, `Username`, `Title`, `Text`, `ImageUrl`, `CreatedDateTime`, `RatingScore`)
VALUES
	(1,19,'Chris',NULL,'My Tag Comment',NULL,'2013-04-28 18:05:20',0),
	(2,19,'Chris',NULL,'My Tag Comment',NULL,'2013-04-28 18:06:17',0),
	(3,19,'Chris',NULL,'Third tag Comment',NULL,'2013-04-28 18:12:30',0),
	(6,71,'Chris',NULL,'Nice game!',NULL,'2013-04-28 20:10:12',0),
	(10,71,'Chris',NULL,'Hey!',NULL,'2013-04-28 20:25:42',0),
	(11,70,'Chris',NULL,'Nice tag!',NULL,'2013-04-28 22:54:59',0),
	(14,69,'Chris',NULL,'Nice picture!',NULL,'2013-04-28 23:43:39',0),
	(17,72,'Robotico',NULL,'This is my tag!',NULL,'2013-04-29 00:17:20',0),
	(19,71,'Robotico',NULL,'I was watching this.game too!',NULL,'2013-04-29 00:23:56',0),
	(20,73,'DemoUser',NULL,'My first comment on my own tag!',NULL,'2013-04-30 20:59:01',0),
	(21,71,'DemoUser',NULL,'I did not get to see this, who won? ',NULL,'2013-04-30 21:01:47',0),
	(22,75,'Failsj',NULL,'Yo Wyatt a fun place',NULL,'2013-05-06 19:45:40',0),
	(24,75,'Failsj',NULL,'Thanks',NULL,'2013-05-06 19:47:31',0),
	(26,82,'Failsj',NULL,'Hey, nice tag',NULL,'2013-08-19 12:29:38',0),
	(28,95,'bwayne','','Good thing I added a picture',NULL,'2013-08-21 04:16:16',0),
	(29,80,'zillwc','','Wow this is awesome',NULL,'2013-08-24 16:31:41',0),
	(30,76,'ddymko','','Cool',NULL,'2013-08-25 16:52:16',0),
	(31,76,'ddymko','','Nice',NULL,'2013-08-25 16:52:33',0),
	(32,106,'andrew','','I totally agree',NULL,'2013-08-26 18:49:31',0),
	(33,106,'andrew','','Comments don\'t work!!!!',NULL,'2013-08-26 18:50:10',0),
	(34,114,'Spencer','','Testing out comments ',NULL,'2013-08-29 16:34:28',0),
	(35,122,'Spencer','','This tag works with location. ',NULL,'2013-09-01 16:46:19',0),
	(36,125,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 08:48:39',0),
	(37,125,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 08:51:45',0),
	(38,125,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 08:51:51',0),
	(39,125,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 08:51:55',0),
	(40,125,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 08:51:57',0),
	(41,125,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 08:52:10',0),
	(42,125,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 08:52:13',0),
	(43,125,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 08:52:16',0),
	(44,125,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 08:52:18',0),
	(45,126,'Spencer','','Lorem Ipsum ',NULL,'2013-09-03 13:33:14',0),
	(46,127,'ddymko','','Lol',NULL,'2013-09-03 15:04:20',0),
	(47,127,'ddymko','','Test 2',NULL,'2013-09-03 15:04:30',0),
	(48,127,'ddymko','','Lolo',NULL,'2013-09-03 15:04:55',0),
	(52,140,'zillwc','TestThisCoolNewComment','this is a test comment','test.jpeg','2013-09-12 16:48:37',0);

/*!40000 ALTER TABLE `tagcomments` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tags
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tags`;

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
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=latin1;

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;

INSERT INTO `tags` (`TagID`, `OwnerID`, `Visibility`, `Name`, `Description`, `ImageUrl`, `Location`, `Latitude`, `Longitude`, `CreatedDateTime`, `Category`, `RatingScore`)
VALUES
	(19,11,1,'My tag','','','',NULL,NULL,'2013-04-10 20:44:07','Default',0),
	(20,11,1,'Imagetag','','http://hci.montclair.edu/geotagger/Images/042013/51660e9127b59.jpg','',NULL,NULL,'2013-04-10 21:14:58','Default',0),
	(22,11,1,'asdf','','http://hci.montclair.edu/geotagger/Images/042013/51662007bc2f6.jpg','',NULL,NULL,'2013-04-10 22:29:28','Default',0),
	(23,11,1,'fsdasd','','http://hci.montclair.edu/geotagger/Images/042013/51662041ae836.jpg','',NULL,NULL,'2013-04-10 22:30:26','Default',0),
	(24,11,1,'gallery test','','','',NULL,NULL,'2013-04-10 22:38:44','Default',0),
	(26,11,1,'phone gallery tag','tag from gallery','','',NULL,NULL,'2013-04-10 22:50:09','Default',0),
	(27,11,1,'phone camera tag','','','',NULL,NULL,'2013-04-10 22:50:50','Default',0),
	(29,11,1,'gallery','','http://hci.montclair.edu/geotagger/Images/042013/5166283b7cc98.jpg','',NULL,NULL,'2013-04-10 23:04:27','Default',0),
	(30,11,1,'android tag','','http://hci.montclair.edu/geotagger/Images/042013/516766253beca.jpg','',NULL,NULL,'2013-04-11 21:40:53','Default',0),
	(39,16,1,'Nagano','Soy.sauce','http://hci.montclair.edu/geotagger/Images/042013/51702304a9bbd.jpg','',NULL,NULL,'2013-04-18 12:44:53','Default',0),
	(40,18,1,'Kids team room','The room where kids team works.','http://hci.montclair.edu/geotagger/Images/042013/517059456253d.jpg','The best part of MSU ',NULL,NULL,'2013-04-18 16:36:21','Default',0),
	(41,17,1,'Women\'sroom','Next to room 117 say women in bright red ','','',NULL,NULL,'2013-04-18 16:38:24','Default',0),
	(42,18,1,'Creepy Abandon Room','CREEPY ','http://hci.montclair.edu/geotagger/Images/042013/51705abfe2b68.jpg','',NULL,NULL,'2013-04-18 16:42:39','Default',0),
	(43,18,1,'Creepy Abandon Room','CREEPY ','http://hci.montclair.edu/geotagger/Images/042013/51705ac2b8736.jpg','',NULL,NULL,'2013-04-18 16:42:42','Default',0),
	(44,18,1,'Computer room','','http://hci.montclair.edu/geotagger/Images/042013/51705b50866c9.jpg','',NULL,NULL,'2013-04-18 16:45:04','Default',0),
	(45,17,1,'Students longe','Its right across from the bathrooms ','','',NULL,NULL,'2013-04-18 16:47:28','Default',0),
	(46,18,1,'Construction','','http://hci.montclair.edu/geotagger/Images/042013/51705be1d71f8.jpg','',NULL,NULL,'2013-04-18 16:47:29','Default',0),
	(47,18,1,'Schedules','schedules','http://hci.montclair.edu/geotagger/Images/042013/51705cdb42ba7.jpg','',NULL,NULL,'2013-04-18 16:51:39','Default',0),
	(48,18,1,'Astronomy board','The astronomy class put up a buliten board with news papers about space on it','','',NULL,NULL,'2013-04-18 16:57:05','Default',0),
	(49,18,1,'Bio hazard','Ahhhhh','http://hci.montclair.edu/geotagger/Images/042013/51705ee346676.jpg','',NULL,NULL,'2013-04-18 17:00:19','Default',0),
	(50,18,1,'Shower','','http://hci.montclair.edu/geotagger/Images/042013/51705fd028d15.jpg','',NULL,NULL,'2013-04-18 17:04:16','Default',0),
	(51,18,1,'Science Lobby','Lobby O\' Science ','http://hci.montclair.edu/geotagger/Images/042013/517061496737c.jpg','',NULL,NULL,'2013-04-18 17:10:33','Default',0),
	(69,11,1,'Nico','Nico in the office ','http://hci.montclair.edu/geotagger/Images/042013/5177e5c58b023.jpg','asci',NULL,NULL,'2013-04-24 10:01:41','Default',0),
	(70,11,1,'Image Tag','Tag with an image from the camera.s','http://hci.montclair.edu/geotagger/Images/042013/517884d3bbfb3.jpg','',NULL,NULL,'2013-04-24 21:20:20','Default',0),
	(71,11,1,'Baseball game','This is a picture of the baseball game I was watching.\n','http://hci.montclair.edu/geotagger/Images/042013/517d90613ae1c.jpg','In my house',NULL,NULL,'2013-04-28 17:10:57','Default',0),
	(72,12,1,'Database','The database that makes it all happen!','http://hci.montclair.edu/geotagger/Images/042013/517df3a5e22e3.jpg','',NULL,NULL,'2013-04-29 00:14:30','Default',0),
	(73,22,1,'My first tag ','This is my first tag. It is a picture I just took of my keyboard!','http://hci.montclair.edu/geotagger/Images/042013/5180689a42cf7.jpg','In my house',NULL,NULL,'2013-04-30 20:58:02','Default',0),
	(74,11,1,'School','Fence ','http://hci.montclair.edu/geotagger/Images/052013/5182ed7b7db07.jpg','at Msu',NULL,NULL,'2013-05-02 18:49:31','Default',0),
	(75,20,1,'Lab','','http://hci.montclair.edu/geotagger/Images/052013/51884045c99ab.jpg','',NULL,NULL,'2013-05-06 19:44:06','Default',0),
	(76,23,1,'Busche campus library','Inside a study room at a rutgers library','','RU Library',NULL,NULL,'2013-07-06 14:59:43','Default',0),
	(77,23,1,'Rtugers Uni - tag','On busche campus at rutgers uni','','rutgers uni',NULL,NULL,'2013-07-06 15:37:10','Default',0),
	(78,20,1,'Test','','','',NULL,NULL,'2013-07-31 14:00:34','Default',0),
	(79,23,1,'Tes11','Tesdesc11','http://hci.montclair.edu/geotagger/Images/082013/5203f312bab20.jpg','at a location',NULL,NULL,'2013-08-08 15:35:46','Default',0),
	(80,24,1,'Test1','Test1 Desc','test1Image.url','Montclair RI104',NULL,NULL,'2013-08-15 18:44:56','Default',0),
	(81,24,1,'Test2','test2Desc','test2Image.url','Montclair RI105',NULL,NULL,'2013-08-15 18:45:37',NULL,0),
	(82,24,1,'test3','Test3Desc','test3Image.url','Montclair RI108',NULL,NULL,'2013-08-15 18:46:12',NULL,0),
	(95,25,1,'SwampArea','This area is dangerous','http://hci.montclair.edu/geotagger/Images/082013/521477347d1f7.jpg','Sayreville',NULL,NULL,'2013-08-21 04:15:48','Default',0),
	(97,23,1,'Home','Testing at home\n','','',NULL,NULL,'2013-08-25 16:58:31','Default',0),
	(98,23,1,'Home test','Testing at home','http://hci.montclair.edu/geotagger/Images/082013/521a71ad630ad.jpg','',NULL,NULL,'2013-08-25 17:05:49','Default',0),
	(99,23,1,'Hi','','','',NULL,NULL,'2013-08-25 17:15:47','Default',0),
	(100,23,1,'Test 1','','','',NULL,NULL,'2013-08-25 17:23:24','Default',0),
	(101,23,1,'Test 2','','','',NULL,NULL,'2013-08-25 17:24:44','Default',0),
	(102,23,1,'Test 3','','','',NULL,NULL,'2013-08-25 17:26:04','Default',0),
	(103,23,1,'Test 4','','','',NULL,NULL,'2013-08-25 17:26:33','Default',0),
	(104,23,1,'Tee wt','','','',NULL,NULL,'2013-08-25 17:48:30','Default',0),
	(105,23,1,'Test 5','','','',NULL,NULL,'2013-08-25 17:49:54','Default',0),
	(106,30,1,'Inferno','This i what hell is like.','','',NULL,NULL,'2013-08-26 18:35:57','Default',0),
	(107,30,1,'Yes','It works','','',NULL,NULL,'2013-08-27 12:58:44','Default',0),
	(108,29,1,'Test tag','Lorem ipsum ','','Test Facility',NULL,NULL,'2013-08-27 19:45:13','Default',0),
	(109,23,1,'Test 8-1','','','',NULL,NULL,'2013-08-28 16:44:47','Default',0),
	(110,23,1,'Tesy new','','','',NULL,NULL,'2013-08-28 17:19:59','Default',0),
	(111,23,1,'Twee st ','','','',NULL,NULL,'2013-08-28 17:22:51','Default',0),
	(112,29,1,'Dorm','The place where I code. ','','Hawk Crossings ',NULL,NULL,'2013-08-29 16:12:30','Default',0),
	(113,29,1,'Dorm 2','Still the place I code. ','','',NULL,NULL,'2013-08-29 16:14:29','Default',0),
	(114,29,1,'Dorm 3','This is still the place I code. ','','',NULL,NULL,'2013-08-29 16:27:21','Default',0),
	(115,30,1,'Ughhhh','Ughhh','','',NULL,NULL,'2013-08-29 17:24:11','Default',0),
	(116,29,1,'Richardson Hall ','Richardson Hall entrance. ','','',40.86200471,-74.19701903,'2013-09-01 15:45:05','Default',0),
	(117,29,1,'Jerry\'s Lab ','','','Richardson Hall ',40.86214813,-74.19651707,'2013-09-01 15:48:16','Default',0),
	(118,29,1,'Jerry\'s Lab ','Testing location ','','Richardson Hall ',NULL,NULL,'2013-09-01 15:51:41','Default',0),
	(119,29,1,'Jerry\'s Lab ','Testing location checked ','','Richardson Hall ',40.86214813,-74.19651707,'2013-09-01 15:52:28','Default',0),
	(120,29,1,'Jerry\'s Lab ','Still testing location checked. ','','Richardson Hall ',40.86214813,-74.19651707,'2013-09-01 15:57:46','Default',0),
	(121,29,1,'Jerry\'s Lab ','','','',40.86214813,-74.19651707,'2013-09-01 16:03:25','Default',0),
	(122,29,1,'Jerry\'s Lab ','Testing location with toast ','','Richardson Hall ',40.86214813,-74.19651707,'2013-09-01 16:29:32','Default',0),
	(123,29,1,'Testing No GPS Lock ','Test ','','Jerry\'s Lab ',40.86524008,-74.19750806,'2013-09-02 10:14:18','Default',0),
	(124,23,1,'Yesy','','','',NULL,NULL,'2013-09-02 17:45:01','Default',0),
	(125,29,1,'Jerry\'s Lab with Picture ','','http://hci.montclair.edu/geotagger/Images/092013/5225185414858.jpg','Richardson Hall ',NULL,NULL,'2013-09-02 18:59:32','Default',0),
	(126,29,1,'Jerry\'s Lab ','','','Richardson Hall ',NULL,NULL,'2013-09-02 19:15:43','Default',0),
	(127,23,1,'Spencer is a cutie','Spencer is cute\n\n, ','http://hci.montclair.edu/geotagger/Images/092013/52262404c0889.jpg','Msu',NULL,NULL,'2013-09-03 14:01:40','Default',0),
	(128,29,1,'Jerry\'s Lab ','','','',40.86272264,-74.19624563,'2013-09-03 14:03:33','Default',0),
	(129,29,1,'Jerry\'s Lab with Picture ','','http://hci.montclair.edu/geotagger/Images/092013/522627b6bfc66.jpg','Richardson Hall ',NULL,NULL,'2013-09-03 14:17:26','Default',0),
	(130,29,1,'Richardson Hall Entrance ','','http://hci.montclair.edu/geotagger/Images/092013/5226280bd2d80.jpg','Richardson Hall ',40.86203341,-74.19645889,'2013-09-03 14:18:51','Default',0),
	(131,23,1,'Gps test with photo','Test','http://hci.montclair.edu/geotagger/Images/092013/522629e2a026c.jpg','Msu',40.86291854,-74.19595437,'2013-09-03 14:26:42','Default',0),
	(132,29,1,'Jerry\'s Lab ','Testing out adventure handler initialization ','','Richardson Hall ',NULL,NULL,'2013-09-03 14:32:05','Default',0),
	(133,29,1,'Jerry\'s Lab','Testing out adventure handler initialization ','','Richardson Hall ',NULL,NULL,'2013-09-03 15:13:41','Default',0),
	(134,29,1,'Jerry\'s Lab ','Testing out the door ','','',NULL,NULL,'2013-09-03 15:15:09','Default',0),
	(135,29,1,'Jerry\'s Lab ','Richardson Hall entrance ','','Richardson Hall ',NULL,NULL,'2013-09-03 15:41:03','Default',0),
	(136,29,1,'Jerry\'s Lab with Picture ','','http://hci.montclair.edu/geotagger/Images/092013/52263b806410f.jpg','Richardson Hall ',NULL,NULL,'2013-09-03 15:41:52','Default',0),
	(137,29,1,'Jerry\'s Lab with Picture ','Testing out GPS ','http://hci.montclair.edu/geotagger/Images/092013/52263bc38d891.jpg','Richardson Hall ',40.86219298,-74.19636107,'2013-09-03 15:42:59','Default',0),
	(138,23,1,'New mac pro','New mac pro','http://hci.montclair.edu/geotagger/Images/092013/52264043d013c.jpg',' msu',40.86235698,-74.19630263,'2013-09-03 16:02:11','Default',0),
	(139,29,1,'Jerry?s Lab','','http://hci.montclair.edu/geotagger/Images/092013/522742989fc73.jpg','Richardson Hall ',NULL,NULL,'2013-09-04 10:24:24','Default',0),
	(140,30,1,'New','The room','','ri 109',NULL,NULL,'2013-09-04 15:05:35','Default',0);

/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Dumping routines (PROCEDURE) for database 'geotagger'
--
DELIMITER ;;

# Dump of PROCEDURE AddAccount
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE AddAdventure
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE AddFriend_OLD
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE AddGroup
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE AddTagComment
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE AddTag_OLD
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE AddTag_ToAdventure
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE AddTag_ToGroup
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE DeleteTagComment
# ------------------------------------------------------------

/*!50003 DROP PROCEDURE IF EXISTS `DeleteTagComment` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `DeleteTagComment`(
	IN cID BIGINT
)
BEGIN
-- Delete a specified comment from a tag
	Delete from TagComments where ID = cID;
END */;;

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE EditAccount
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE RemoveTag_FromGroup
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE SendRequest
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE Test_AddAccounts
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of PROCEDURE Test_AddTestAccount
# ------------------------------------------------------------

/*!50003 DROP PROCEDURE IF EXISTS `Test_AddTestAccount` */;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER"*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `Test_AddTestAccount`()
BEGIN

	call AddAccount('Testuser1', 'Chris L', 'loeschornc1@mail.montclair.edu',
		'pass', null, 'This is my test account', 'NJ', '"Hello"', 
			1,1);

END */;;

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
DELIMITER ;

--
-- Dumping routines (FUNCTION) for database 'geotagger'
--
DELIMITER ;;

# Dump of FUNCTION AddFriend
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of FUNCTION AddTag
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of FUNCTION Adv_GetNextIndex
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of FUNCTION TagExists_InAdv
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
# Dump of FUNCTION TagExists_InGroup
# ------------------------------------------------------------

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

/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;;
DELIMITER ;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

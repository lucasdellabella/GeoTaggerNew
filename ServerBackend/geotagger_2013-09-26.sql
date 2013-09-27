# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 130.68.20.197 (MySQL 5.0.92-log)
# Database: geotagger
# Generation Time: 2013-09-26 22:21:21 +0000
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Stores User Account data';

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
	(23,'ddymko',NULL,NULL,'b1b8e4f670b6722fffc328d7ee938b77',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F353233343737393631663061622E6A7067','Hi','New Jersy','',1,1,'2013-09-14 10:49:58',0),
	(24,'zillwc','Zill Christian','zillwc@gmail.com','1c0b76fce779f78f51be339c49445c49','','Raptor','Sayreville, NJ','Hello W0rld',1,1,'2013-09-14 05:51:52',0),
	(25,'bwayne','Bruce Wayne','bwayne@wenterprise.com','ec0e2603172c73a8b644bb9456c1ff6e',X'6261746D616E2E6A7067','the hero gotham deserves','Gotham','Justice!',0,1,'2013-08-20 05:02:08',0),
	(29,'Spencer',NULL,NULL,'7c9fb847d117531433435b68b61f91f6','','','Richardson ','',1,1,'2013-09-13 11:31:47',0),
	(30,'andrew',NULL,NULL,'47fab60bdcd2ffce91447d19fe9ce7f1','','','Hell','',1,1,'2013-08-26 18:29:23',0),
	(31,'Test12',NULL,NULL,'d8578edf8458ce06fbc5bb76a58c5ca4',NULL,NULL,NULL,NULL,1,1,'2013-09-13 11:14:05',0),
	(32,'Jerry',NULL,NULL,'e10adc3949ba59abbe56e057f20f883e','','Assistant Professor','Montclair State University','Wherever you are there you are!',1,1,'2013-09-14 11:37:28',0),
	(33,'Emily',NULL,NULL,'343b1c4a3ea721b2d640fc8700db0f36','','Hi','MSU','Q',1,1,'2013-09-13 14:05:10',0),
	(34,'msu_prof','First Last','professor.edu','1c0b76fce779f78f51be339c49445c49',NULL,NULL,NULL,NULL,0,1,'2013-09-17 14:38:21',0),
	(35,'ru_prof','First Last','professor.edu','1c0b76fce779f78f51be339c49445c49',NULL,NULL,NULL,NULL,0,1,'2013-09-17 14:38:48',0),
	(37,'RU-Team-01',NULL,NULL,'cffab7f0cb85f121be9a330e2e5da9ea',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(38,'RU-Team-02',NULL,NULL,'181dad18a012fc6aa33f1c5b5e3bdd83',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(39,'RU-Team-03',NULL,NULL,'06f181ea73f5df54926c2ac4009afa48',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(40,'RU-Team-04',NULL,NULL,'79812f3341e5ce650add14828e3de866',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(41,'RU-Team-05',NULL,NULL,'123f85e7e269208c8ac377ace6484c06',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(42,'RU-Team-06',NULL,NULL,'0af8b8e67659fd57db2f95a4c1b2484b',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(43,'RU-Team-07',NULL,NULL,'847af882794a3289667c1eb7d660347d',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(44,'RU-Team-08',NULL,NULL,'482f638f13a51243a04db9ae4fde3988',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(45,'RU-Team-09',NULL,NULL,'e8b593a828b096728ff42cd1d5d14466',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(46,'RU-Team-10',NULL,NULL,'a8d4fdee29a7804381a6c5d6cc930bc2',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F727574676572732E706E67',NULL,'Rutgers University',NULL,0,1,'2013-09-26 18:14:59',0),
	(47,'MSU-Team-01',NULL,NULL,'4b0dcb85eb3bef88c7279ef47296869a',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(48,'MSU-Team-02',NULL,NULL,'748a534d078dc4b188eda8a9eda3718f',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(49,'MSU-Team-03',NULL,NULL,'b64ed9b4310d6c7c868ea72beba74f2b',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(50,'MSU-Team-04',NULL,NULL,'37ef33fd606cbc78929dfe20242fa7bb',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(51,'MSU-Team-05',NULL,NULL,'c8ad5f3ab5a4453dcac07523cc6115c5',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(52,'MSU-Team-06',NULL,NULL,'b13154f68d034eb7ecd1dd2ef90a9349',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(53,'MSU-Team-07',NULL,NULL,'a7c8be3bccaad9cbd4021108db6789a2',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(54,'MSU-Team-08',NULL,NULL,'944f0475db5afef2341c9683f82e62a9',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(55,'MSU-Team-09',NULL,NULL,'75d93e37457a50783c27fc807ecc4169',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(56,'MSU-Team-10',NULL,NULL,'f4d020aa0c8712cd438c4ceff1ddbd99',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(57,'MSU-Team-11',NULL,NULL,'acfb1ebcacd9a7de0acccdea61ade944',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(58,'MSU-Team-12',NULL,NULL,'f67922720ef5aaf5e0a0e2431c247d48',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(59,'MSU-Team-13',NULL,NULL,'d68a909826ef349db9458ab5d483fa00',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(60,'MSU-Team-14',NULL,NULL,'17f643a0da423d013c4f691a46998a10',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0),
	(61,'MSU-Team-15',NULL,NULL,'51a31763a96b9bbee404e21649564fcc',X'687474703A2F2F6863692E6D6F6E74636C6169722E6564752F67656F7461676765722F496D616765732F3039323031332F6D6F6E74636C6169722E6A7067',NULL,'Montclair State University',NULL,0,1,'2013-09-26 18:20:13',0);

/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adventuremembers
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adventuremembers`;

CREATE TABLE `adventuremembers` (
  `AdvID` int(10) unsigned NOT NULL,
  `uID` int(10) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `adventuremembers` WRITE;
/*!40000 ALTER TABLE `adventuremembers` DISABLE KEYS */;

INSERT INTO `adventuremembers` (`AdvID`, `uID`)
VALUES
	(7,25),
	(7,4),
	(32,37),
	(32,38),
	(32,39),
	(32,40),
	(32,41),
	(32,42),
	(32,43),
	(32,44),
	(32,45),
	(32,46),
	(33,37),
	(33,38),
	(33,39),
	(33,40),
	(33,41),
	(33,42),
	(33,43),
	(33,44),
	(33,45),
	(33,46),
	(34,37),
	(34,38),
	(34,39),
	(34,40),
	(34,41),
	(34,42),
	(34,43),
	(34,44),
	(34,45),
	(34,46),
	(35,37),
	(35,38),
	(35,39),
	(35,40),
	(35,41),
	(35,42),
	(35,43),
	(35,44),
	(35,45),
	(35,46),
	(36,37),
	(36,38),
	(36,39),
	(36,40),
	(36,41),
	(36,42),
	(36,43),
	(36,44),
	(36,45),
	(36,46),
	(37,37),
	(37,38),
	(37,39),
	(37,40),
	(37,41),
	(37,42),
	(37,43),
	(37,44),
	(37,45),
	(37,46),
	(38,37),
	(38,38),
	(38,39),
	(38,40),
	(38,41),
	(38,42),
	(38,43),
	(38,44),
	(38,45),
	(38,46),
	(40,47),
	(40,48),
	(40,49),
	(40,50),
	(40,51),
	(40,52),
	(40,53),
	(40,54),
	(40,55),
	(40,56),
	(40,57),
	(40,58),
	(40,59),
	(40,60),
	(40,61),
	(41,47),
	(41,48),
	(41,49),
	(41,50),
	(41,51),
	(41,52),
	(41,53),
	(41,54),
	(41,55),
	(41,56),
	(41,57),
	(41,58),
	(41,59),
	(41,60),
	(41,61),
	(42,47),
	(42,48),
	(42,49),
	(42,50),
	(42,51),
	(42,52),
	(42,53),
	(42,54),
	(42,55),
	(42,56),
	(42,57),
	(42,58),
	(42,59),
	(42,60),
	(42,61),
	(43,47),
	(43,48),
	(43,49),
	(43,50),
	(43,51),
	(43,52),
	(43,53),
	(43,54),
	(43,55),
	(43,56),
	(43,57),
	(43,58),
	(43,59),
	(43,60),
	(43,61),
	(44,47),
	(44,48),
	(44,49),
	(44,50),
	(44,51),
	(44,52),
	(44,53),
	(44,54),
	(44,55),
	(44,56),
	(44,57),
	(44,58),
	(44,59),
	(44,60),
	(44,61),
	(45,47),
	(45,48),
	(45,49),
	(45,50),
	(45,51),
	(45,52),
	(45,53),
	(45,54),
	(45,55),
	(45,56),
	(45,57),
	(45,58),
	(45,59),
	(45,60),
	(45,61),
	(46,47),
	(46,48),
	(46,49),
	(46,50),
	(46,51),
	(46,52),
	(46,53),
	(46,54),
	(46,55),
	(46,56),
	(46,57),
	(46,58),
	(46,59),
	(46,60),
	(46,61),
	(7,24);

/*!40000 ALTER TABLE `adventuremembers` ENABLE KEYS */;
UNLOCK TABLES;


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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `adventures` WRITE;
/*!40000 ALTER TABLE `adventures` DISABLE KEYS */;

INSERT INTO `adventures` (`ID`, `OwnerID`, `Name`, `Description`, `Visibility`, `CreatedDateTime`)
VALUES
	(5,24,'Zill\'s Awesome Adventure','This is a very cool adventure on how I\'m testing this adventure',0,'2013-09-16 04:26:05'),
	(6,4,'New Adv','Where is the moss growing?  Why do you think?  If the compass app on your smartphone stopped working, could you use this information to find your way out of the woods?',1,'2013-09-13 01:26:59'),
	(7,4,'Test Adv','This is a test adv to add some test data',1,'2013-08-15 18:46:58'),
	(8,30,'Newadv','Fesc',0,'2013-09-16 15:49:49'),
	(9,30,'Chdatunl','Xsgunbkpe',0,'2013-09-16 15:49:32'),
	(10,23,'David tesy','',1,'2013-09-12 15:53:27'),
	(11,30,'Fdiddhjb','Xfvj',0,'2013-09-16 15:49:37'),
	(12,30,'Jdj jdjsbks','Djfkdnd',0,'2013-09-16 15:49:40'),
	(13,23,'Test','Ri387',1,'2013-09-12 16:45:26'),
	(14,23,'Headaches','Show up ',1,'2013-09-12 16:46:14'),
	(15,23,'Test','from db\n',1,'2013-09-12 17:31:30'),
	(16,23,'Yest','Hsbs',1,'2013-09-13 09:48:41'),
	(17,23,'Ggb','Bbh',1,'2013-09-13 09:51:19'),
	(18,32,'Test1','Test one adventure',1,'2013-09-13 11:26:27'),
	(19,29,'Lorem Ipsum ','',1,'2013-09-13 11:30:52'),
	(20,23,'Bob','Xhaj',1,'2013-09-13 11:31:08'),
	(21,23,'Dogs','Sgs',1,'2013-09-13 11:34:11'),
	(22,23,'Test','Tesg',1,'2013-09-13 11:45:14'),
	(23,29,'Lorem Ipsum ','Lorem Ipsum ',1,'2013-09-13 11:58:35'),
	(24,23,'Dhdnd','Dbdb',1,'2013-09-13 12:23:54'),
	(25,29,'Lorem Ipsum ','Lorem Ipsum ',1,'2013-09-13 12:33:12'),
	(26,23,'Tesy','TeysbdnxmaMlcn',1,'2013-09-13 12:34:38'),
	(27,30,'Today','Now',0,'2013-09-16 15:49:42'),
	(28,30,'Xyz','Abc',0,'2013-09-16 15:49:45'),
	(29,33,'Testing adventure fun','Fun fun fun.',1,'2013-09-13 14:20:34'),
	(30,33,'Testing','Tttttttttt',1,'2013-09-13 15:30:58'),
	(31,23,'Test','Boo',1,'2013-09-14 09:55:04'),
	(32,34,'Montclair State University','',1,'2013-09-16 02:42:29'),
	(33,34,'Mills Reservation','Parking lot can be found at:	\nN40o 51.455? \rW74o 12.552?\r\rGo to the Lenape Trail (follow the trail markers towards the right)',1,'2013-09-16 02:13:40'),
	(34,34,'Bonsal Preserve','Parking can be found at the end of Daniels Dr. in Clifton.  Take Grove street to Chittenden Rd. in Clifton and then proceed to the end of Daniels Dr.  Park on the street.  Be respectful of the neighbors.',1,'2013-09-15 01:32:23'),
	(35,34,'Eagle Rock','',1,'2013-09-17 17:05:59'),
	(36,34,'Branchbrook Park','',1,'2013-09-16 02:42:51'),
	(37,34,'Downtown Montclair','',1,'2013-09-16 02:42:54'),
	(38,34,'Downtown Newark','Walk between the following GPS coordinates.  As you walk, record the number of shops and the types of shops.  When you are done, calculate the diversity index.  Is the diversity index relatively high or low?  Is the diversity of the shops evenly distributed or are there some dominant types?  Do you think there is a keystone shop in this area? Explain your answer.\r\rStarting Point: West Market St.\rN40o 44.122?\rW74o 10.294?\r	\rWalk to:\rN40o 44.156?	\rW74o 10.422?\r	\rWalk to:\rN40o 44.127?\rW74o 10.335? \r\rWalk to:\rN40o 44.093?	\rW74o 10.366?\r	 \rWalk to:\rN40o 44.104?\rW74o 10.446?\rFinal way point; Branford Pl. and Halsey St.',1,'2013-09-15 01:33:46'),
	(40,35,'Montclair State University','',1,'2013-09-16 02:42:29'),
	(41,35,'Mills Reservation','Parking lot can be found at:	\nN40o 51.455? \rW74o 12.552?\r\rGo to the Lenape Trail (follow the trail markers towards the right)',1,'2013-09-16 02:13:40'),
	(42,35,'Bonsal Preserve','Parking can be found at the end of Daniels Dr. in Clifton.  Take Grove street to Chittenden Rd. in Clifton and then proceed to the end of Daniels Dr.  Park on the street.  Be respectful of the neighbors.',1,'2013-09-15 01:32:23'),
	(43,35,'Eagle Rock','',1,'2013-09-16 02:42:47'),
	(44,35,'Branchbrook Park','',1,'2013-09-16 02:42:51'),
	(45,35,'Downtown Montclair','',1,'2013-09-16 02:42:54'),
	(46,35,'Downtown Newark','Walk between the following GPS coordinates.  As you walk, record the number of shops and the types of shops.  When you are done, calculate the diversity index.  Is the diversity index relatively high or low?  Is the diversity of the shops evenly distributed or are there some dominant types?  Do you think there is a keystone shop in this area? Explain your answer.\r\rStarting Point: West Market St.\rN40o 44.122?\rW74o 10.294?\r	\rWalk to:\rN40o 44.156?	\rW74o 10.422?\r	\rWalk to:\rN40o 44.127?\rW74o 10.335? \r\rWalk to:\rN40o 44.093?	\rW74o 10.366?\r	 \rWalk to:\rN40o 44.104?\rW74o 10.446?\rFinal way point; Branford Pl. and Halsey St.',1,'2013-09-15 01:33:46');

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
	(6,82,0),
	(32,157,0),
	(32,158,0),
	(32,159,0),
	(33,160,0),
	(33,161,0),
	(33,162,0),
	(33,163,0),
	(33,164,0),
	(33,165,0),
	(33,166,0),
	(33,167,0),
	(34,168,0),
	(34,169,0),
	(35,170,0),
	(35,171,0),
	(35,172,0),
	(36,173,0),
	(36,174,0),
	(36,175,0),
	(36,176,0),
	(36,177,0),
	(37,178,0),
	(37,179,0),
	(37,180,0),
	(38,181,0),
	(40,182,0),
	(40,183,0),
	(40,184,0),
	(41,185,0),
	(41,186,0),
	(41,187,0),
	(41,188,0),
	(41,189,0),
	(41,190,0),
	(41,191,0),
	(41,192,0),
	(42,193,0),
	(42,194,0),
	(43,195,0),
	(43,196,0),
	(43,197,0),
	(44,198,0),
	(44,199,0),
	(44,200,0),
	(44,201,0),
	(44,202,0),
	(45,203,0),
	(45,204,0),
	(45,205,0),
	(46,206,0);

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
	(29,23),
	(32,33),
	(32,29),
	(23,30);

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
	(52,140,'zillwc','TestThisCoolNewComment','this is a test comment','test.jpeg','2013-09-12 16:48:37',0),
	(53,125,'Spencer','','Testing out pictures ','','2013-09-13 09:19:08',0),
	(54,125,'Spencer','','Test pic ','','2013-09-13 09:30:46',0),
	(55,139,'Spencer','','Test pic ','','2013-09-13 09:45:11',0),
	(56,139,'Spencer','','Test pic ','','2013-09-13 09:49:47',0),
	(57,139,'Spencer','','Test pic ','','2013-09-13 09:55:21',0),
	(58,139,'Spencer','','Test pic ','','2013-09-13 13:20:42',0),
	(59,136,'Spencer','','Test pic. ','','2013-09-13 14:44:43',0),
	(60,137,'Spencer','','Posted ','','2013-09-13 15:01:32',0),
	(61,137,'Spencer','','Bag ','','2013-09-13 15:03:34',0),
	(62,137,'Spencer','','Still bag ','','2013-09-13 15:09:20',0),
	(63,139,'Spencer','','BAG','','2013-09-13 15:31:05',0),
	(64,137,'Spencer','','Test top ','','2013-09-13 15:32:58',0),
	(65,137,'Spencer','','Test script ','http://hci.montclair.edu/geotagger/Images/092013/52336eb832ae3.jpg','2013-09-13 15:59:52',0),
	(66,115,'andrew','','Wow','','2013-09-13 16:35:15',0),
	(67,137,'Spencer','','Stuff. ','http://hci.montclair.edu/geotagger/Images/092013/5233b5f38b6f8.jpg','2013-09-13 21:03:47',0),
	(68,80,'zillwc','Whao','I did not expect this',NULL,'2013-09-14 02:51:04',0),
	(69,154,'jerry','','Test','','2013-09-14 09:24:44',0),
	(70,154,'jerry','','Adding a picture','','2013-09-14 09:25:49',0),
	(71,51,'jerry','','Trst','','2013-09-14 09:42:48',0),
	(72,51,'jerry','','Uhhjjjj','','2013-09-14 09:42:54',0),
	(73,155,'ddymko','','Test','http://hci.montclair.edu/geotagger/Images/092013/5234776218985.jpg','2013-09-14 10:49:06',0),
	(74,154,'Jerry','','Test','','2013-09-15 09:56:58',0),
	(75,154,'Jerry','','Test','http://hci.montclair.edu/geotagger/Images/092013/5235bcd43a9d2.jpg','2013-09-15 09:57:40',0),
	(76,154,'Jerry','','Juuu','','2013-09-16 15:18:45',0),
	(77,154,'Jerry','','Gggh','','2013-09-16 16:01:16',0),
	(79,154,'Jerry','','Tttt','','2013-09-16 16:15:42',0),
	(81,154,'Jerry','','Uuiii','','2013-09-16 16:21:37',0),
	(86,154,'Jerry','','Mytest','','2013-09-16 16:41:26',0),
	(87,154,'Jerry','','Hgfd','','2013-09-16 16:47:41',0),
	(88,154,'Jerry','','Zxcv','','2013-09-16 16:55:04',0),
	(89,154,'Jerry','','Popo','','2013-09-16 17:00:41',0),
	(91,154,'Jerry','','Lolo','','2013-09-16 17:05:06',0),
	(92,154,'Jerry','','Ioio','','2013-09-16 17:05:58',0),
	(94,154,'Jerry','','Asdf','','2013-09-16 17:14:32',0),
	(95,154,'Jerry','','Aqaq','','2013-09-16 17:14:57',0),
	(96,152,'andrew','','A comment','','2013-09-16 17:15:38',0),
	(97,154,'Jerry','','Cvcv','','2013-09-16 17:23:24',0),
	(98,154,'Jerry','','Bnbn','','2013-09-16 17:25:14',0),
	(99,154,'Jerry','','Uuuu','','2013-09-16 18:44:04',0),
	(100,154,'Jerry','','Ioioio','','2013-09-16 18:45:17',0),
	(101,154,'Jerry','','Fgff','','2013-09-16 20:03:22',0),
	(102,154,'Jerry','','Jkjk','','2013-09-16 20:06:37',0),
	(103,154,'Jerry','','Yyy','','2013-09-16 20:12:51',0),
	(104,154,'Jerry','','Test','','2013-09-16 21:06:48',0),
	(106,154,'Jerry','','Op','','2013-09-17 13:51:20',0),
	(107,173,'msu_prof','','This of course a test but we','','2013-09-17 19:23:47',0),
	(108,173,'msu_prof','','Oops first mistakes,.....','','2013-09-17 19:24:35',0),
	(109,173,'msu_prof','',' unbelievable it doesn\'t understand my','','2013-09-17 19:29:14',0),
	(110,153,'Jerry','','Dirk','http://hci.montclair.edu/geotagger/Images/092013/5239f105ac8f9.jpg','2013-09-18 14:29:25',0),
	(111,154,'Jerry','','I noticed this was blue','http://hci.montclair.edu/geotagger/Images/092013/5241a6bce9bbe.jpg','2013-09-24 10:50:36',0);

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
	(81,24,1,'Test2','test2Desc','test2Image.url','Montclair RI105',NULL,NULL,'2013-09-17 17:59:11','Default',0),
	(82,24,1,'test3','Test3Desc','test3Image.url','Montclair RI108',NULL,NULL,'2013-09-17 17:59:11','Default',0),
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
	(140,30,1,'New','The room','','ri 109',NULL,NULL,'2013-09-04 15:05:35','Default',0),
	(141,23,1,'Test','Test h','','',NULL,NULL,'2013-09-13 11:28:48','Default',0),
	(142,23,1,'Dhsk','Dbd','','',NULL,NULL,'2013-09-13 11:31:50','Default',0),
	(143,23,1,'Dan','Dbsn','','',NULL,NULL,'2013-09-13 11:36:35','Default',0),
	(144,23,1,'Fhsj','','','',NULL,NULL,'2013-09-13 11:45:22','Default',0),
	(145,33,1,'Lorem Ipsum','Testing','','',NULL,NULL,'2013-09-13 14:14:44','Default',0),
	(146,29,1,'Lorem Ipsum ','Lorem Ipsum ','','',NULL,NULL,'2013-09-13 15:22:48','Default',0),
	(147,32,1,'Yhhh','Yyyyh','','',NULL,NULL,'2013-09-13 15:23:07','Default',0),
	(148,33,1,'Taga','Ttt','','',NULL,NULL,'2013-09-13 15:24:44','Default',0),
	(149,33,1,'Ttt','Ujjjj','','',NULL,NULL,'2013-09-13 15:25:05','Default',0),
	(150,33,1,'Yuii','tygdgjjb','','',NULL,NULL,'2013-09-13 15:25:24','Default',0),
	(151,33,1,'Gggghh','Tttttyy','','',NULL,NULL,'2013-09-13 16:32:50','Default',0),
	(152,30,1,'NH 1269','Sbndigo','http://hci.montclair.edu/geotagger/Images/092013/52339eb916715.jpg','',NULL,NULL,'2013-09-13 19:24:41','Default',0),
	(153,32,1,'St Thomas','A church building on Bloomfield','http://hci.montclair.edu/geotagger/Images/092013/5234576d32cef.jpg','',40.8318133833333,-74.1778868166667,'2013-09-14 08:32:45','Default',0),
	(154,32,1,'Lot 17','Where I like to park (of I get in at 7:30)','http://hci.montclair.edu/geotagger/Images/092013/52345ad9e127d.jpg','',40.86318045,-74.1965200166667,'2013-09-14 08:47:22','Default',0),
	(155,23,1,'Tag test','Tag test','','',NULL,NULL,'2013-09-14 09:55:20','Default',0),
	(156,23,1,'Test','Testetstetst','','',NULL,NULL,'2013-09-14 13:26:36','Default',0),
	(157,34,1,'Natures? Rebirth','Question: This site was disturbed by construction during the recent and past summers.  What is happening with regard to the vegetation?  Will the vegetation be the same as it was before the disturbance occurred?','http://hci.montclair.edu/geotagger/images/092013/1A-NaturesRebirth 2.jpg','',40.862817,-74.1955,'2013-09-17 17:59:11','Default',0),
	(158,34,1,'Survival of the Fittest','Observe the lichens in this area.  Where are they and why are they where they are?','http://hci.montclair.edu/geotagger/images/092013/1B-SurvivalOfTheFittestI.jpg','',40.863433,-74.197067,'2013-09-17 17:59:11','Default',0),
	(159,34,1,'The Mystery of the Greens','Be careful here.  Do not get too close to the edge!  You can observe a wide variety of plants in this small area.  Identify at least three different ecological concepts that are occurring here (hint: one of them is competition).  Explain the reasoning for your answers.  What evidence can you see of animals?  In terms of the distribution of the grasses, are they evenly, clumped or randomly distributed?  What is your rationale for your answer?','http://hci.montclair.edu/geotagger/images/092013/1C-TheMysteryOfTheGreensI.jpg','',40.863467,-74.198883,'2013-09-17 17:59:11','Default',0),
	(160,34,1,'The Clever Giants','The trees are quite tall at this location.  Why?','http://hci.montclair.edu/geotagger/images/092013/2A-TheCleverGiantsII.jpg','',40.8576,-74.209817,'2013-09-17 17:59:11','Default',0),
	(161,34,1,'Endangered Paradise','Do you see any signs of a river?  What are they?  Is the vegetation any different here than 10 meters away?  Why?  Describe the soil.','http://hci.montclair.edu/geotagger/images/092013/2B-EndangeredParadiseI.jpg','',40.85695,-74.210917,'2013-09-17 17:59:11','Default',0),
	(162,34,1,'The Marvelous Path','Where is the moss growing?  Why do you think?  If the compass app on your smartphone stopped working, could you use this information to find your way out of the woods?','http://hci.montclair.edu/geotagger/images/092013/2C-MarvelousPath.jpg','',40.8568,-74.21075,'2013-09-17 17:59:11','Default',0),
	(163,34,1,'The Tree Pose','Tree root formation-why are the roots in the air, what happened? ','http://hci.montclair.edu/geotagger/images/092013/2D-TheTreePoseII.jpg','',40.8563,-74.211367,'2013-09-17 17:59:11','Default',0),
	(164,34,1,'The Underground Vegan Creatures','Look at the holes in the ground and look at the tree branches above.  What caused the holes in the ground and killed the branch tips?  You will have to do some searching on ?Google? to find the answer to this question.','http://hci.montclair.edu/geotagger/images/092013/2E-TheUndergroundVeganCreaturesI.jpg','',40.854883,-74.211033,'2013-09-17 17:59:11','Default',0),
	(165,34,1,'Hunger Games','Observe that the tree is dead, there are holes in the bark, and there are tunnels carved into the stem and under the bark.  What made the holes and tunnels?  Why did the tree die?  What role do these creatures play in ecology (is it such a bad thing that the tree died)?','http://hci.montclair.edu/geotagger/images/092013/2F-HungerGamesI.jpg','',40.8541,-74.209917,'2013-09-17 17:59:11','Default',0),
	(166,34,1,'We are the Champions','Look around.  You will see skinny trees and fat trees.  What is going on?  What would it look like if the trees were all fat?  What would it look like if the trees were all skinny?  What does this tells us about resource availability in the area?','http://hci.montclair.edu/geotagger/images/092013/2G-WeAreTheChampions.jpg','',40.853033,-74.2094,'2013-09-17 17:59:11','Default',0),
	(167,34,1,'Comfort Zone','How are these trees different from the ones at the bottom of the reservation where you parked?  Why?','http://hci.montclair.edu/geotagger/images/092013/2H-ComfortZone.jpg','',40.851633,-74.209567,'2013-09-17 17:59:11','Default',0),
	(168,34,1,'The Source of Life','Is this a lotic or lentic aquatic system?  Explain your answer.  Can you find some insects on the surface of the water?  Describe them.  Why don?t they sink?  Can you find Japanese knotweed?  Take a picture of it.','http://hci.montclair.edu/geotagger/images/092013/3A-TheSourceOfLifeI.jpg','',40.8494,-74.188033,'2013-09-17 17:59:11','Default',0),
	(169,34,1,'You Can Tell the Differences','How does the vegetation here differ with the vegetation along the river?  Why is it different?  Look at the angles of the leaves on the trees.  Which way are the leaves facing at the bottom of the crowns?  Why?','http://hci.montclair.edu/geotagger/images/092013/3B-YouCanTellTheDifferences.jpg','',40.8503,-74.187983,'2013-09-17 17:59:11','Default',0),
	(170,34,1,'A Patchwork Quilt','i. Look out over the overlook towards NYC.  Notice that there is a lot of vegetation between here and there.  Is it continuous?  What divides the vegetation?  Think about size of animals and populations.  If you were a butterfly, how would you view this distribution of vegetation?  If you were a bird, how might the view change?  \rWhat role does the distribution of vegetation play in terms of patch size and distance between patches?  \r\r\rii. Think about ecosystems ecology, carbon cycles, and global change.  In a rough sense, identify several sources and sinks of carbon.  As the population of NJ changes, how might the relationship between carbon source and sink change?  If the trees are replaced with buildings and houses, what else would change in the ecosystem?','http://hci.montclair.edu/geotagger/images/092013/4A-APatchOfQuiltI.jpg','',40.8032,-74.237983,'2013-09-17 17:59:11','Default',0),
	(171,34,1,'A Disturbing Thought','The area outside the fence has been mowed several times during the summer, spring, and fall.  In ecological terms how would you describe the mowing?  How would the intensity of the mowing affect the differences in vegetation inside and outside the fence?  Is there a difference in the kinds of animals you can see in each area?  Why?','http://hci.montclair.edu/geotagger/images/092013/4B-ADisturbingThoughtII.jpg','',40.803633,-74.238333,'2013-09-17 17:59:11','Default',0),
	(172,34,1,'Deep Disturbing Thoughts','Wind has played an important role here.  In ecological terms, how would you describe the role of wind here?  What role does scale play?  What you are looking at is called a gap.  What changed when the gap was created?  What would you expect to happen over time in the gap?  Does the size of the gap matter?','http://hci.montclair.edu/geotagger/images/092013/4C-DeepDisturbingThoughtsIII.jpg','',40.812133,-74.233683,'2013-09-17 17:59:11','Default',0),
	(173,34,1,'The Green Threat','i. What type of aquatic system is this pond?  What do you notice?  What evidence do you have to support your conclusion?  By the end of the summer, we tend to find no living animals in the lake.  Why?  Describe the process that leads to this happening.  What do you think happens with the water temperatures during the year?  Describe the process of temperature changes in the pond.\r\rii. If one quarter of the lake is covered in algae at the beginning of the summer and the algae population doubles in size every day, how many days will it take to cover the whole pond in algae?','http://hci.montclair.edu/geotagger/images/092013/5A-TheGreenThreat.jpg','',40.768917,-74.175767,'2013-09-17 17:59:11','Default',0),
	(174,34,1,'The Perfect Pyramid','N40deg 45.938?\rW74deg 10.663?\rFrom this point walk to the right!!  Find the chestnut oak tree on the edge of the pond.\r\rDescribe the leaves at the bottom and the top of this tree.  What do you notice about their size? Compare its leaves with other trees around it.  Why do you see differences in leaf size on the chestnut oak and why are the leaves different between the other trees?\rBonus points:  Name 3 other species of trees that you see.','http://hci.montclair.edu/geotagger/images/092013/5B-ThePerfectPyramidII.jpg','',40.765633,-74.177717,'2013-09-17 17:59:11','Default',0),
	(175,34,1,'The Human Touch','Turn around and compare sites.  Why is it that we do not have an understory canopy in one area, but there is one somewhere else? ','http://hci.montclair.edu/geotagger/images/092013/5C-HumanTouchII.jpg','',40.764617,-74.178433,'2013-09-17 17:59:11','Default',0),
	(176,34,1,'The Amazing Hollow Tree','Find the tree with the metal tag 0680 on it.\r\ri. How can a hollow tree survive and have nice green leaves?\r\rii. Talk about the transport system of the tree. How do water and nutrients move through the tree?  What causes the movement?\r\riii. This tree has several habitats in or on it that form micro-ecosystems.  Identify at least two and describe them.  Describe the symbiotic relationships that are occurring in those two habitats.  What types of symbioses are occurring? What is happening in terms of material flow?','http://hci.montclair.edu/geotagger/images/092013/5D-TheAmazingHollowTreeII.jpg','',40.764667,-74.177983,'2013-09-17 17:59:11','Default',0),
	(177,34,1,'Warning!','Find the poison ivy and take a picture of it.','','',40.767217,-74.17645,'2013-09-17 17:59:11','Default',0),
	(178,34,1,'City?s View','Find an example of primary succession and describe it.','http://hci.montclair.edu/geotagger/images/092013/6A-CitysViewI.jpg','',40.813567,-74.2188,'2013-09-17 17:59:11','Default',0),
	(179,34,1,'Ecology in the City','Starting Point: Church St.\r	\rStart by counting all food establishments walking west along Church Street.  Then answer the following questions.\r\ri. If Montclair had the same amount of money flow as Newark, could Montclair be sustained?\r	\rii. If we think of these stores as representing organisms filling certain niches, what do you think is the potential niche size and what is the realized niche size?\r\riii. Explain how the concept of sexual selection can be applied to the existence of the shops on Church Street.','http://hci.montclair.edu/geotagger/images/092013/6B-EcologyInTheCityII.jpg','',40.81385,-74.217883,'2013-09-17 17:59:11','Default',0),
	(180,34,1,'Diversity Index','Start at Bloomfield and Park St.\n\rWalk to:\r(40.815, -74.219067)\n\nWalk to:\n(40.816483, -74.2211)\n\nWalk to:\n(40.814967, -74.221283)\n\nWalk to:\n(40.815, -74.219067)','','',40.815,-74.219067,'2013-09-17 17:59:11','Default',0),
	(181,34,1,'Downtown Newark','Walk between the following GPS coordinates.  As you walk, record the number of shops and the types of shops.  When you are done, calculate the diversity index.  Is the diversity index relatively high or low?  Is the diversity of the shops evenly distributed or are there some dominant types?  Do you think there is a keystone shop in this area? Explain your answer.\r\rStarting Point: West Market St.\r(40.735367, -74.171567)\r	\rWalk to:\r(40.735933, -74.1737)\r	\rWalk to:\r(40.73545, -74.17225)\r\rWalk to:\r(40.734883, -74.172767)\r	 \rWalk to:\r(40.735067, -74.1741)\n\rFinal way point; Branford Pl. and Halsey St.','http://hci.montclair.edu/geotagger/images/092013/7A-DowntownNewarkII.jpg','',40.7672,-74.2483,'2013-09-17 17:59:11','Default',0),
	(182,35,1,'Natures\' Rebirth','Question: This site was disturbed by construction during the recent and past summers.  What is happening with regard to the vegetation?  Will the vegetation be the same as it was before the disturbance occurred?','http://hci.montclair.edu/geotagger/images/092013/1A-NaturesRebirth 2.jpg','',40.862817,-74.1955,'2013-09-17 17:59:11','Default',0),
	(183,35,1,'Survival of the Fittest','Observe the lichens in this area.  Where are they and why are they where they are?','http://hci.montclair.edu/geotagger/images/092013/1B-SurvivalOfTheFittestI.jpg','',40.863433,-74.197067,'2013-09-17 17:59:11','Default',0),
	(184,35,1,'The Mystery of the Greens','Be careful here.  Do not get too close to the edge!  You can observe a wide variety of plants in this small area.  Identify at least three different ecological concepts that are occurring here (hint: one of them is competition).  Explain the reasoning for your answers.  What evidence can you see of animals?  In terms of the distribution of the grasses, are they evenly, clumped or randomly distributed?  What is your rationale for your answer?','http://hci.montclair.edu/geotagger/images/092013/1C-TheMysteryOfTheGreensI.jpg','',40.863467,-74.198883,'2013-09-17 17:59:11','Default',0),
	(185,35,1,'The Clever Giants','The trees are quite tall at this location.  Why?','http://hci.montclair.edu/geotagger/images/092013/2A-TheCleverGiantsII.jpg','',40.8576,-74.209817,'2013-09-17 17:59:11','Default',0),
	(186,35,1,'Endangered Paradise','Do you see any signs of a river?  What are they?  Is the vegetation any different here than 10 meters away?  Why?  Describe the soil.','http://hci.montclair.edu/geotagger/images/092013/2B-EndangeredParadiseI.jpg','',40.85695,-74.210917,'2013-09-17 17:59:11','Default',0),
	(187,35,1,'The Marvelous Path','Where is the moss growing?  Why do you think?  If the compass app on your smartphone stopped working, could you use this information to find your way out of the woods?','http://hci.montclair.edu/geotagger/images/092013/2C-MarvelousPath.jpg','',40.8568,-74.21075,'2013-09-17 17:59:11','Default',0),
	(188,35,1,'The Tree Pose','Tree root formation-why are the roots in the air, what happened? ','http://hci.montclair.edu/geotagger/images/092013/2D-TheTreePoseII.jpg','',40.8563,-74.211367,'2013-09-17 17:59:11','Default',0),
	(189,35,1,'The Underground Vegan Creatures','Look at the holes in the ground and look at the tree branches above.  What caused the holes in the ground and killed the branch tips?  You will have to do some searching on ?Google? to find the answer to this question.','http://hci.montclair.edu/geotagger/images/092013/2E-TheUndergroundVeganCreaturesI.jpg','',40.854883,-74.211033,'2013-09-17 17:59:11','Default',0),
	(190,35,1,'Hunger Games','Observe that the tree is dead, there are holes in the bark, and there are tunnels carved into the stem and under the bark.  What made the holes and tunnels?  Why did the tree die?  What role do these creatures play in ecology (is it such a bad thing that the tree died)?','http://hci.montclair.edu/geotagger/images/092013/2F-HungerGamesI.jpg','',40.8541,-74.209917,'2013-09-17 17:59:11','Default',0),
	(191,35,1,'We are the Champions','Look around.  You will see skinny trees and fat trees.  What is going on?  What would it look like if the trees were all fat?  What would it look like if the trees were all skinny?  What does this tells us about resource availability in the area?','http://hci.montclair.edu/geotagger/images/092013/2G-WeAreTheChampions.jpg','',40.853033,-74.2094,'2013-09-17 17:59:11','Default',0),
	(192,35,1,'Comfort Zone','How are these trees different from the ones at the bottom of the reservation where you parked?  Why?','http://hci.montclair.edu/geotagger/images/092013/2H-ComfortZone.jpg','',40.851633,-74.209567,'2013-09-17 17:59:11','Default',0),
	(193,35,1,'The Source of Life','Is this a lotic or lentic aquatic system?  Explain your answer.  Can you find some insects on the surface of the water?  Describe them.  Why don?t they sink?  Can you find Japanese knotweed?  Take a picture of it.','http://hci.montclair.edu/geotagger/images/092013/3A-TheSourceOfLifeI.jpg','',40.8494,-74.188033,'2013-09-17 17:59:11','Default',0),
	(194,35,1,'You Can Tell the Differences','How does the vegetation here differ with the vegetation along the river?  Why is it different?  Look at the angles of the leaves on the trees.  Which way are the leaves facing at the bottom of the crowns?  Why?','http://hci.montclair.edu/geotagger/images/092013/3B-YouCanTellTheDifferences.jpg','',40.8503,-74.187983,'2013-09-17 17:59:11','Default',0),
	(195,35,1,'A Patchwork Quilt','i. Look out over the overlook towards NYC.  Notice that there is a lot of vegetation between here and there.  Is it continuous?  What divides the vegetation?  Think about size of animals and populations.  If you were a butterfly, how would you view this distribution of vegetation?  If you were a bird, how might the view change?  \rWhat role does the distribution of vegetation play in terms of patch size and distance between patches?  \r\r\rii. Think about ecosystems ecology, carbon cycles, and global change.  In a rough sense, identify several sources and sinks of carbon.  As the population of NJ changes, how might the relationship between carbon source and sink change?  If the trees are replaced with buildings and houses, what else would change in the ecosystem?','http://hci.montclair.edu/geotagger/images/092013/4A-APatchOfQuiltI.jpg','',40.8032,-74.237983,'2013-09-17 17:59:11','Default',0),
	(196,35,1,'A Disturbing Thought','The area outside the fence has been mowed several times during the summer, spring, and fall.  In ecological terms how would you describe the mowing?  How would the intensity of the mowing affect the differences in vegetation inside and outside the fence?  Is there a difference in the kinds of animals you can see in each area?  Why?','http://hci.montclair.edu/geotagger/images/092013/4B-ADisturbingThoughtII.jpg','',40.803633,-74.238333,'2013-09-17 17:59:11','Default',0),
	(197,35,1,'Deep Disturbing Thoughts','Wind has played an important role here.  In ecological terms, how would you describe the role of wind here?  What role does scale play?  What you are looking at is called a gap.  What changed when the gap was created?  What would you expect to happen over time in the gap?  Does the size of the gap matter?','http://hci.montclair.edu/geotagger/images/092013/4C-DeepDisturbingThoughtsIII.jpg','',40.812133,-74.233683,'2013-09-17 17:59:11','Default',0),
	(198,35,1,'The Green Threat','i. What type of aquatic system is this pond?  What do you notice?  What evidence do you have to support your conclusion?  By the end of the summer, we tend to find no living animals in the lake.  Why?  Describe the process that leads to this happening.  What do you think happens with the water temperatures during the year?  Describe the process of temperature changes in the pond.\r\rii. If one quarter of the lake is covered in algae at the beginning of the summer and the algae population doubles in size every day, how many days will it take to cover the whole pond in algae?','http://hci.montclair.edu/geotagger/images/092013/5A-TheGreenThreat.jpg','',40.768917,-74.175767,'2013-09-17 17:59:11','Default',0),
	(199,35,1,'The Perfect Pyramid','N40deg 45.938?\rW74deg 10.663?\rFrom this point walk to the right!!  Find the chestnut oak tree on the edge of the pond.\r\rDescribe the leaves at the bottom and the top of this tree.  What do you notice about their size? Compare its leaves with other trees around it.  Why do you see differences in leaf size on the chestnut oak and why are the leaves different between the other trees?\rBonus points:  Name 3 other species of trees that you see.','http://hci.montclair.edu/geotagger/images/092013/5B-ThePerfectPyramidII.jpg','',40.765633,-74.177717,'2013-09-17 17:59:11','Default',0),
	(200,35,1,'The Human Touch','Turn around and compare sites.  Why is it that we do not have an understory canopy in one area, but there is one somewhere else? ','http://hci.montclair.edu/geotagger/images/092013/5C-HumanTouchII.jpg','',40.764617,-74.178433,'2013-09-17 17:59:11','Default',0),
	(201,35,1,'The Amazing Hollow Tree','Find the tree with the metal tag 0680 on it.\r\ri. How can a hollow tree survive and have nice green leaves?\r\rii. Talk about the transport system of the tree. How do water and nutrients move through the tree?  What causes the movement?\r\riii. This tree has several habitats in or on it that form micro-ecosystems.  Identify at least two and describe them.  Describe the symbiotic relationships that are occurring in those two habitats.  What types of symbioses are occurring? What is happening in terms of material flow?','http://hci.montclair.edu/geotagger/images/092013/5D-TheAmazingHollowTreeII.jpg','',40.764667,-74.177983,'2013-09-17 17:59:11','Default',0),
	(202,35,1,'Warning!','Find the poison ivy and take a picture of it.','','',40.767217,-74.17645,'2013-09-17 17:59:11','Default',0),
	(203,35,1,'City?s View','Find an example of primary succession and describe it.','http://hci.montclair.edu/geotagger/images/092013/6A-CitysViewI.jpg','',40.813567,-74.2188,'2013-09-17 17:59:11','Default',0),
	(204,35,1,'Ecology in the City','Starting Point: Church St.\r	\rStart by counting all food establishments walking west along Church Street.  Then answer the following questions.\r\ri. If Montclair had the same amount of money flow as Newark, could Montclair be sustained?\r	\rii. If we think of these stores as representing organisms filling certain niches, what do you think is the potential niche size and what is the realized niche size?\r\riii. Explain how the concept of sexual selection can be applied to the existence of the shops on Church Street.','http://hci.montclair.edu/geotagger/images/092013/6B-EcologyInTheCityII.jpg','',40.81385,-74.217883,'2013-09-17 17:59:11','Default',0),
	(205,35,1,'Diversity Index','Start at Bloomfield and Park St. \rN40o 48.900?\rW74o 13.144?\r\rWalk to:\rN40o 48.989?\rW74o 13.266?\r\r\rWalk to:\rN40o 48.898?\rW74o 13.277?\r\rWalk to:\rN40o 48. 900?\rW74o 13.144?','','',40.815,-74.219067,'2013-09-17 17:59:11','Default',0),
	(206,35,1,'Downtown Newark','Walk between the following GPS coordinates.  As you walk, record the number of shops and the types of shops.  When you are done, calculate the diversity index.  Is the diversity index relatively high or low?  Is the diversity of the shops evenly distributed or are there some dominant types?  Do you think there is a keystone shop in this area? Explain your answer.\r\rStarting Point: West Market St.\rN40o 44.122?\rW74o 10.294?\r	\rWalk to:\rN40o 44.156?	\rW74o 10.422?\r	\rWalk to:\rN40o 44.127?\rW74o 10.335? \r\rWalk to:\rN40o 44.093?	\rW74o 10.366?\r	 \rWalk to:\rN40o 44.104?\rW74o 10.446?\rFinal way point; Branford Pl. and Halsey St.','http://hci.montclair.edu/geotagger/images/092013/7A-DowntownNewarkII.jpg','',40.7672,-74.2483,'2013-09-17 17:59:11','Default',0);

/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

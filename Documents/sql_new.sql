/*
SQLyog Community v9.63 
MySQL - 5.5.8 : Database - xm_books
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`xm_books` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `xm_books`;

/*Table structure for table `authors` */

DROP TABLE IF EXISTS `authors`;

CREATE TABLE `authors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(30) NOT NULL,
  `Description` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Data for the table `authors` */

insert  into `authors`(`id`,`Name`,`Description`) values (1,'Kathy Sierra',NULL),(2,'Bert Bates',NULL),(3,'Brett D. McLaughlin',NULL),(4,'Gary Pollice',NULL),(5,'Dave West',NULL),(6,'Yann Martel',NULL),(7,'J.K.Rowling',NULL),(8,'Joe Fawcett, Danny Ayers, R.E.',NULL),(9,'Andrew S.Tanenbaum',NULL),(10,'Daniel D.Povet',NULL);

/*Table structure for table `bookauthor` */

DROP TABLE IF EXISTS `bookauthor`;

CREATE TABLE `bookauthor` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `BookId` int(11) NOT NULL,
  `AuthorId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `Book` (`BookId`),
  KEY `Author` (`AuthorId`),
  CONSTRAINT `Author` FOREIGN KEY (`AuthorId`) REFERENCES `authors` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Book` FOREIGN KEY (`BookId`) REFERENCES `books` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Data for the table `bookauthor` */

insert  into `bookauthor`(`Id`,`BookId`,`AuthorId`) values (1,1,1),(2,1,2),(3,2,3),(4,2,4),(5,2,5),(6,3,6),(7,4,6),(8,5,7),(9,6,7),(10,7,8),(11,8,9),(12,9,10);

/*Table structure for table `books` */

DROP TABLE IF EXISTS `books`;

CREATE TABLE `books` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ISBN` varchar(13) NOT NULL,
  `Title` varchar(100) NOT NULL,
  `ImageUrl` varchar(200) DEFAULT NULL,
  `CategoryId` int(11) NOT NULL,
  `Price` double(10,2) NOT NULL,
  `PublisherId` int(11) NOT NULL,
  `Description` varchar(250) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `Category` (`CategoryId`),
  KEY `Publisher` (`PublisherId`),
  CONSTRAINT `Category` FOREIGN KEY (`CategoryId`) REFERENCES `categories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Publisher` FOREIGN KEY (`PublisherId`) REFERENCES `publishers` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

/*Data for the table `books` */

insert  into `books`(`Id`,`ISBN`,`Title`,`ImageUrl`,`CategoryId`,`Price`,`PublisherId`,`Description`) values (1,'9780596009205','Head First Java','Resources/images/covers/JavaHeadFirst.jpg',1,45.00,1,'Learning a complex new language is no easy task especially when it s an object-oriented computer programming language like Java. You might think the problem is your brain. It seems to have a mind of its own, a mind that doesn\'t always want to take in'),(2,'9780596008673','Head First Object-Oriented Analysis and Design','Resources/images/covers/OOAHeadFirst.jpg',2,28.97,1,'Head First Object Oriented Analysis and Design is a refreshing look at subject of OOAD. What sets this book apart is its focus on learning. The authors have made the content of OOAD accessible and usable for the practitioner.'),(3,'9780547848419','Life Of Pi','Resources/images/covers/LifeOfPi.jpg',3,8.77,2,'After the sinking of a cargo ship, a solitary lifeboat remains bobbing on the wild blue Pacific. The only survivors from the wreck are a sixteen years old boy named Pi, a hyena, a wounded zebra, an orangutan and a 450 pounds royal bengal tiger. The s'),(4,'9780571219766','Self','Resources/images/covers/Self.jpg',3,7.47,3,'A fictional autobiography of a young writer which takes the reader to Canada, Portugal, Greece, Turkey and elsewhere. This story of love, sex and ambiguity is the first novel by the Canadian author of the award-winning short-story collection, The Fa'),(5,'9780439785969','Harry Potter & the Half Blood Prince','Resources/images/covers/HP6.jpg',3,10.39,4,'The war against Voldemort is not going well; even the Muggles have been affected. Dumbledore is absent from Hogwarts for long stretches of time, and the Order of the Phoenix has already suffered losses. '),(6,'9780142300275','The Little White Horse','Resources/images/covers/WhiteHorse.jpg',4,6.99,5,'When orphan Maria arrives at Moonacre Manor, she feels as if she\'s come home. Her new guardian is kind and funny, and everyone there is like an old friend. But beneath the beauty and comfort lies a tragedy. Maria is determined to find out about it, c'),(7,'9781118162132','Beginning XML','Resources/images/covers/XML.jpg',2,23.78,6,'The XML language has become the standard for writing documents on the Internet and is constantly improving and evolving. This new edition covers all the many new XML-based technologies that have appeared since the previous edition four years ago, pro'),(8,'9780136006633','Modern Operating Systems (3rd Edition)','Resources/images/covers/OS.jpg',2,124.49,7,'The widely anticipated revision of this worldwide best-seller incorporates the latest developments in operating systems technologies.  The Third Edition includes up-to-date materials on relevant operating systems such as Linux, Windows, and embedded '),(9,'9780596005658','Understanding the Linux Kernel, Third Edition','Resources/images/covers/Linux.jpg',2,36.57,1,'In order to thoroughly understand what makes Linux tick and why it works so well on a wide variety of systems, you need to delve deep into the heart of the kernel. The kernel handles all interactions between the CPU and the external world, and determ');

/*Table structure for table `categories` */

DROP TABLE IF EXISTS `categories`;

CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) NOT NULL,
  `Description` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `categories` */

insert  into `categories`(`id`,`Name`,`Description`) values (1,'Java',NULL),(2,'Programming Fundamet',NULL),(3,'Science Fiction',NULL),(4,'Fairy Tail',NULL);

/*Table structure for table `orderdetail` */

DROP TABLE IF EXISTS `orderdetail`;

CREATE TABLE `orderdetail` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `OrderId` int(11) NOT NULL,
  `BookId` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `OrderId` (`OrderId`),
  KEY `BookId` (`BookId`),
  CONSTRAINT `orderdetail_ibfk_1` FOREIGN KEY (`OrderId`) REFERENCES `orders` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `orderdetail_ibfk_2` FOREIGN KEY (`BookId`) REFERENCES `books` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `orderdetail` */

/*Table structure for table `orders` */

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `UserId` int(11) DEFAULT NULL,
  `ContactName` varchar(50) NOT NULL,
  `Address` varchar(250) NOT NULL,
  `Phone` varchar(15) NOT NULL,
  `Created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Total` decimal(15,0) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `orders_ibfk_1` (`UserId`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `orders` */

/*Table structure for table `publishers` */

DROP TABLE IF EXISTS `publishers`;

CREATE TABLE `publishers` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(30) NOT NULL,
  `Description` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Data for the table `publishers` */

insert  into `publishers`(`Id`,`Name`,`Description`) values (1,'O\'Reilly',NULL),(2,'Mariner Books',NULL),(3,'Faber & Faber',NULL),(4,'Scholastic Paperstick',NULL),(5,'Puffin',NULL),(6,'Wrox',NULL),(7,'Prentice Hall',NULL);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `IsAdmin` bit(1) NOT NULL DEFAULT b'0',
  `FullName` varchar(50) DEFAULT NULL,
  `Address` varchar(250) DEFAULT NULL,
  `Phone` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Unique` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `users` */

insert  into `users`(`Id`,`Username`,`Password`,`Email`,`IsAdmin`,`FullName`,`Address`,`Phone`) values (1,'namnq','123456','namnq60475@fpt.edu.vn','',NULL,NULL,NULL),(2,'admin','123456','admin@yahoo.com','\0','','',''),(3,'admin1','123456','admin@yahoo.com','\0',NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

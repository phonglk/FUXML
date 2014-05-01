USE `xm_books`;

/*Table structure for table `authors` */

DROP TABLE IF EXISTS `authors`;

CREATE TABLE `authors` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(30) NOT NULL,
  `Description` VARCHAR(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `authors` */

INSERT  INTO `authors`(`id`,`Name`,`Description`) VALUES (1,'Kathy Sierra',NULL),(2,'Bert Bates',NULL),(3,'Brett D. McLaughlin',NULL),(4,'Gary Pollice',NULL),(5,'Dave West',NULL);

/*Table structure for table `publishers` */

DROP TABLE IF EXISTS `publishers`;

CREATE TABLE `publishers` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(30) NOT NULL,
  `Description` VARCHAR(250) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `publishers` */

INSERT  INTO `publishers`(`Id`,`Name`,`Description`) VALUES (1,'O\'Reilly',NULL);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(50) NOT NULL,
  `Password` VARCHAR(50) NOT NULL,
  `Email` VARCHAR(50) NOT NULL,
  `IsAdmin` BIT(1) NOT NULL DEFAULT b'0',
  `FullName` VARCHAR(50) DEFAULT NULL,
  `Address` VARCHAR(250) DEFAULT NULL,
  `Phone` VARCHAR(15) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Unique` (`Username`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*Table structure for table `categories` */

DROP TABLE IF EXISTS `categories`;

CREATE TABLE `categories` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(20) NOT NULL,
  `Description` VARCHAR(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `categories` */

INSERT  INTO `categories`(`id`,`Name`,`Description`) VALUES (1,'Java',NULL),(2,'Programming Fundamet',NULL);


/*Table structure for table `orders` */

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `UserId` INT(11) DEFAULT NULL,
  `ContactName` VARCHAR(50) NOT NULL,
  `Address` VARCHAR(250) NOT NULL,
  `Phone` VARCHAR(15) NOT NULL,
  `Created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Total` DECIMAL(15,0) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `orders_ibfk_1` (`UserId`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `users` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*Data for the table `orders` */

DROP TABLE IF EXISTS `books`;

CREATE TABLE `books` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `ISBN` VARCHAR(13) NOT NULL,
  `Title` VARCHAR(100) NOT NULL,
  `ImageUrl` VARCHAR(200) DEFAULT NULL,
  `CategoryId` INT(11) NOT NULL,
  `Price` DOUBLE(10,2) NOT NULL,
  `PublisherId` INT(11) NOT NULL,
  `Description` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `Category` (`CategoryId`),
  KEY `Publisher` (`PublisherId`),
  CONSTRAINT `Category` FOREIGN KEY (`CategoryId`) REFERENCES `categories` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Publisher` FOREIGN KEY (`PublisherId`) REFERENCES `publishers` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `books` */

INSERT  INTO `books`(`Id`,`ISBN`,`Title`,`ImageUrl`,`CategoryId`,`Price`,`PublisherId`,`Description`) VALUES (1,'9780596009205','Head First Java','http://ecx.images-amazon.com/images/I/51a1RPCmv7L._BO2,204,203,200_PIsitb-sticker-arrow-click,TopRight,35,-76_AA300_SH20_OU01_.jpg',1,45.00,1,'Learning a complex new language is no easy task especially when it s an object-oriented computer programming language like Java. You might think the problem is your brain. It seems to have a mind of its own, a mind that doesn\'t always want to take in'),(2,'9780596008673','Head First Object-Oriented Analysis and Design','http://ecx.images-amazon.com/images/I/51mE7FBCw8L._BO2,204,203,200_PIsitb-sticker-arrow-click,TopRight,35,-76_AA300_SH20_OU01_.jpg',2,28.97,1,'Head First Object Oriented Analysis and Design is a refreshing look at subject of OOAD. What sets this book apart is its focus on learning. The authors have made the content of OOAD accessible and usable for the practitioner.');


/*Table structure for table `orderdetail` */

DROP TABLE IF EXISTS `orderdetail`;

CREATE TABLE `orderdetail` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `OrderId` INT(11) NOT NULL,
  `BookId` INT(11) NOT NULL,
  `Quantity` INT(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `OrderId` (`OrderId`),
  KEY `BookId` (`BookId`),
  CONSTRAINT `orderdetail_ibfk_1` FOREIGN KEY (`OrderId`) REFERENCES `orders` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `orderdetail_ibfk_2` FOREIGN KEY (`BookId`) REFERENCES `books` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*Data for the table `orderdetail` */



/*Table structure for table `bookauthor` */

DROP TABLE IF EXISTS `bookauthor`;

CREATE TABLE `bookauthor` (
  `Id` INT(11) NOT NULL AUTO_INCREMENT,
  `BookId` INT(11) NOT NULL,
  `AuthorId` INT(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `Book` (`BookId`),
  KEY `Author` (`AuthorId`),
  CONSTRAINT `Author` FOREIGN KEY (`AuthorId`) REFERENCES `authors` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Book` FOREIGN KEY (`BookId`) REFERENCES `books` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `bookauthor` */

INSERT  INTO `bookauthor`(`Id`,`BookId`,`AuthorId`) VALUES (1,1,1),(2,1,2),(3,2,3),(4,2,4),(5,2,5);

/*Table structure for table `books` */


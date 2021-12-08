-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: classroom
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `account` (
  `ID` varchar(20) NOT NULL COMMENT '6位字符串\n000-超级管理员 + 三位编号001\n001-管理员+三位编号\n002-用户+三位编号',
  `Password` varchar(45) NOT NULL DEFAULT '123456' COMMENT '密码',
  `Permission` int(11) NOT NULL DEFAULT '2' COMMENT '0-超级管理员\n1-管理员\n2-普通用户',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('001001','20021112nl',0),('003001','20021112',1),('003002','123456',1);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assets`
--

DROP TABLE IF EXISTS `assets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `assets` (
  `ID` varchar(45) NOT NULL COMMENT 'ID格式：',
  `Kind` varchar(45) NOT NULL COMMENT '种类',
  `Brand` varchar(45) DEFAULT NULL COMMENT '品牌',
  `Status` int(11) NOT NULL DEFAULT '0' COMMENT '0-正常\n1-一般\n2-差',
  `purchaseId` varchar(45) NOT NULL COMMENT '采购订单编号',
  `FixCount` int(11) DEFAULT '0' COMMENT '维修次数',
  `isInStore` int(11) NOT NULL DEFAULT '1' COMMENT '1-在仓库\\n0-不在仓库',
  `Place` int(11) DEFAULT NULL COMMENT '默认仓库0',
  `borrowed` int(11) DEFAULT '0',
  `borrowing` int(11) DEFAULT '0',
  `assetsimg` varchar(255) DEFAULT 'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png',
  PRIMARY KEY (`ID`,`isInStore`),
  KEY `fk1_idx` (`purchaseId`),
  CONSTRAINT `fk1` FOREIGN KEY (`purchaseId`) REFERENCES `purchase` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资产信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assets`
--

LOCK TABLES `assets` WRITE;
/*!40000 ALTER TABLE `assets` DISABLE KEYS */;
INSERT INTO `assets` VALUES ('10630002','花盆','塑料',1,'10630',0,0,2,1,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('164280001','黑板擦','长城',1,'164280',0,0,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('164280002','黑板擦','长城',1,'164280',0,0,2,1,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('164280003','黑板擦','长城',0,'164280',0,1,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('164280004','黑板擦','长城',1,'164280',0,0,2,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('164280005','黑板擦','长城',0,'164280',0,1,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('18226001','电脑','联想',1,'18226',0,0,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('18226002','电脑','联想',0,'18226',0,1,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('18226003','电脑','联想',0,'18226',0,1,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('18226004','电脑','联想',1,'18226',0,0,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('18226005','电脑','联想',0,'18226',0,1,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('73256001','椅子','乐歌',0,'73256',0,1,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('73256002','椅子','乐歌',0,'73256',0,1,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('73256003','椅子','乐歌',0,'73256',0,0,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('73256004','椅子','乐歌',0,'73256',0,1,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png'),('73256005','椅子','乐歌',0,'73256',0,0,0,0,0,'http://www.nilupri.xyz/2021/11/16/e15a6cf06cc81.png');
/*!40000 ALTER TABLE `assets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow`
--

DROP TABLE IF EXISTS `borrow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `borrow` (
  `id` varchar(45) NOT NULL,
  `originplace` int(11) DEFAULT NULL,
  `dstplace` int(11) DEFAULT NULL,
  `date` varchar(45) DEFAULT NULL,
  `assetsId` varchar(45) DEFAULT NULL,
  `isReturn` int(11) DEFAULT '0',
  `userid` varchar(45) DEFAULT NULL,
  `ishandle` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk6_idx` (`assetsId`),
  KEY `fk7_idx` (`userid`),
  CONSTRAINT `fk6` FOREIGN KEY (`assetsId`) REFERENCES `assets` (`id`),
  CONSTRAINT `fk7` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow`
--

LOCK TABLES `borrow` WRITE;
/*!40000 ALTER TABLE `borrow` DISABLE KEYS */;
INSERT INTO `borrow` VALUES ('20211103211',0,2,'2021-11-03','164280002',1,'001001',1),('20211103325',0,4,'2021-11-03','10630002',1,'001001',1),('20211103630',0,4,'2021-11-03','164280001',1,'001001',1),('20211103647',0,1,'2021-11-03','10630002',1,'001001',1);
/*!40000 ALTER TABLE `borrow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `message` (
  `ID` varchar(20) NOT NULL,
  `UserId` varchar(45) DEFAULT NULL COMMENT '发起人',
  `Date` varchar(45) DEFAULT NULL COMMENT '发送时间',
  `Content` varchar(255) DEFAULT NULL,
  `Status` int(11) DEFAULT '0' COMMENT '是否已读，1-已读',
  `FromUserId` varchar(45) NOT NULL,
  `isReply` int(11) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `fk3_idx` (`UserId`),
  CONSTRAINT `fk3` FOREIGN KEY (`UserId`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES ('211105357','003001','2021-11-05','很高兴的通知您，您已经成为本系统的管理员，和我一起管理本站吧~',1,'001001',0);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `outinput`
--

DROP TABLE IF EXISTS `outinput`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `outinput` (
  `ID` varchar(45) NOT NULL,
  `inDate` varchar(45) DEFAULT NULL COMMENT '入库时间',
  `outDate` varchar(45) DEFAULT NULL COMMENT '出库时间',
  PRIMARY KEY (`ID`),
  CONSTRAINT `fk4` FOREIGN KEY (`ID`) REFERENCES `assets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `outinput`
--

LOCK TABLES `outinput` WRITE;
/*!40000 ALTER TABLE `outinput` DISABLE KEYS */;
INSERT INTO `outinput` VALUES ('10630002','2021-11-03','2021-11-03'),('164280001','2021-11-03','2021-11-03'),('164280002','2021-11-03','2021-11-03'),('164280003','2021-11-03','2021-11-03'),('164280004','2021-11-03','2021-11-03'),('164280005','2021-11-02',''),('18226001','2021-11-03','2021-11-03'),('18226002','2021-11-02',''),('18226003','2021-11-02',''),('18226004','2021-11-03','2021-11-03'),('18226005','2021-11-02',''),('73256001','2021-11-02',''),('73256002','2021-11-02',''),('73256003','2021-11-03','2021-11-03'),('73256004','2021-11-02',''),('73256005','2021-11-03','2021-11-03');
/*!40000 ALTER TABLE `outinput` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `place`
--

DROP TABLE IF EXISTS `place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `place` (
  `ID` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL COMMENT '地点名',
  `count` int(11) DEFAULT NULL COMMENT '当前容量',
  `maxCount` int(11) DEFAULT NULL COMMENT '最大容量',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `place`
--

LOCK TABLES `place` WRITE;
/*!40000 ALTER TABLE `place` DISABLE KEYS */;
INSERT INTO `place` VALUES (0,'仓库',13,1000000),(1,'第一教学楼',0,200),(2,'第二教学楼',3,200),(3,'第三教学楼',0,150),(4,'第一学科楼',0,250),(5,'综合楼',0,300),(6,'实验楼',0,100),(7,'活动中心',0,80),(8,'图书馆',0,400);
/*!40000 ALTER TABLE `place` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `purchase` (
  `ID` varchar(20) NOT NULL COMMENT '订单编号格式\n年月日编号\n如211031001',
  `UserName` varchar(45) NOT NULL COMMENT '采购人\n',
  `PurchaseTime` varchar(45) NOT NULL COMMENT '采购时间\n',
  `num` varchar(45) NOT NULL COMMENT '采购数量',
  `Kind` varchar(45) NOT NULL COMMENT '种类',
  `Brand` varchar(45) NOT NULL COMMENT '品牌',
  `UserId` varchar(45) NOT NULL,
  `isHandle` int(11) NOT NULL DEFAULT '0' COMMENT '0-未处理\n1-已处理',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='采购信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` VALUES ('10630','Admin2','2021-11-02','5','花盆','塑料','003001',1),('164280','Admin2','2021-11-02','5','黑板擦','长城','003001',1),('18226','Admin2','2021-11-02','5','电脑','联想','003001',1),('73256','Admin2','2021-11-02','5','椅子','乐歌','003001',1),('87380','Admin2','2021-11-02','5','桌子','乐歌','003001',0);
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `ID` varchar(20) NOT NULL,
  `UserName` varchar(45) DEFAULT 'user',
  `Telephone` varchar(45) DEFAULT '11111111111',
  `Place` varchar(45) DEFAULT '上海' COMMENT '地区',
  `School` varchar(45) DEFAULT 'SIT',
  `Address` varchar(45) DEFAULT NULL COMMENT '详细地址',
  `LoginTime` varchar(45) DEFAULT '2021-10-30' COMMENT '注册时间',
  `permission` int(11) DEFAULT '2',
  `sex` int(11) DEFAULT '0' COMMENT '0--未知\n1--男\n2-女',
  `avatarurl` varchar(255) DEFAULT 'http://www.nilupri.xyz/2021/10/30/af5c6db641e32.png',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('001001','SuAdmin','12345678901','上海','SIT','上海应用技术大学','2021-10-31',0,2,'img\\avatar\\23b94photo_3.jpg'),('003001','Admin','17272727223','内江','asd','四川内江','2021-11-01',1,1,'http://www.nilupri.xyz/2021/10/30/af5c6db641e32.png'),('003002','Admin2','暂无信息','暂无信息','SIT','暂无信息','2021-11-02',1,2,'http://www.nilupri.xyz/2021/10/30/af5c6db641e32.png');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'classroom'
--

--
-- Dumping routines for database 'classroom'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-16 12:51:51

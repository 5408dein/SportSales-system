-- MySQL dump 10.13  Distrib 9.7.0, for Win64 (x86_64)
--
-- Host: localhost    Database: sale_system
-- ------------------------------------------------------
-- Server version	9.7.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `sale_system`
--

/*!40000 DROP DATABASE IF EXISTS `sale_system`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `sale_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `sale_system`;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin','123456');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `customer_code` varchar(20) DEFAULT NULL,
  `customer_name` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `address` varchar(200) DEFAULT NULL,
  `postcode` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `password` varchar(50) DEFAULT '123456' COMMENT '登录密码',
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'C001','朝阳体育','张三丰','北京市朝阳区体育路88号','100020','13800000001','123456'),(2,'C002','海珠体校','李四海','广州市海珠区运动大道168号','510220','13900000002','123456'),(3,'C003','锦江体育用品店','王五洲','成都市锦江区东风路55号','610020','13600000003','123456'),(4,'C004','西湖健身中心','赵六艺','杭州市西湖区体育场路12号','310000','13500000004','123456'),(5,'C005','浦东运动馆','陈七剑','上海市浦东新区张江路100号','200120','13700000005','123456'),(6,'13638697352','爱因斯坦','爱因斯坦','花园小区',NULL,'13638697352','123456');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manufacturer`
--

DROP TABLE IF EXISTS `manufacturer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manufacturer` (
  `manufacturer_id` int NOT NULL AUTO_INCREMENT,
  `manufacturer_code` varchar(20) NOT NULL,
  `manufacturer_name` varchar(100) NOT NULL,
  `contact_person` varchar(50) DEFAULT '' COMMENT '联系人',
  `phone` varchar(20) DEFAULT '' COMMENT '联系电话',
  `email` varchar(100) DEFAULT '' COMMENT '邮箱',
  `address` varchar(200) DEFAULT '' COMMENT '厂家地址',
  PRIMARY KEY (`manufacturer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manufacturer`
--

LOCK TABLES `manufacturer` WRITE;
/*!40000 ALTER TABLE `manufacturer` DISABLE KEYS */;
INSERT INTO `manufacturer` VALUES (1,'MF001','耐克体育','王经理','010-88886666','nike@sports.com','北京市朝阳区建国路88号'),(2,'MF002','阿迪达斯','李主管','021-66668888','adidas@sports.com','上海市静安区南京西路168号'),(3,'MF003','李宁运动','张先生','010-55552222','lining@sports.com','北京市通州区梨园镇李宁中心'),(4,'MF004','安踏体育','陈经理','0595-88889999','anta@sports.com','福建省晋江市安踏工业园'),(5,'MF005','斯伯丁','刘总','021-33338888','spalding@sports.com','上海市浦东新区陆家嘴金融中心');
/*!40000 ALTER TABLE `manufacturer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `message_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `order_id` int DEFAULT NULL,
  `order_no` varchar(50) DEFAULT NULL,
  `title` varchar(200) NOT NULL COMMENT '消息标题',
  `content` varchar(500) DEFAULT NULL COMMENT '消息内容',
  `msg_type` varchar(20) DEFAULT 'system' COMMENT 'ship=发货 shortage=缺货 system=系统',
  `is_read` tinyint DEFAULT '0' COMMENT '0未读 1已读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,1,3,'ORD20260603001','订单已发货','您的订单 ORD20260603001 已发货，请注意查收！','ship',1,'2026-06-09 14:01:46');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `detail_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `manufacturer_id` int NOT NULL,
  PRIMARY KEY (`detail_id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_detail_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `order_detail_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (1,1,1,1,299.00,5),(2,1,3,1,899.00,1),(3,2,2,2,698.00,1),(4,2,7,1,259.00,2),(5,3,6,3,597.00,1),(6,4,9,1,399.00,2),(7,4,10,1,459.00,3),(8,5,5,2,1198.00,3),(9,5,8,1,179.00,4),(10,6,12,1,299.00,1),(11,7,4,1,1099.00,2),(12,8,3,1,899.00,1);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL,
  `order_date` datetime DEFAULT NULL,
  `customer_id` int DEFAULT NULL,
  `shipping_requirement` varchar(255) DEFAULT NULL,
  `supply_flag` char(1) DEFAULT NULL,
  `shipping_date` datetime DEFAULT NULL,
  `total_weight` decimal(10,2) DEFAULT NULL,
  `freight` decimal(10,2) DEFAULT NULL,
  `payment_status` varchar(20) DEFAULT NULL,
  `receiver` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `invoice_flag` char(1) DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'ORD20260601001','2026-06-01 09:30:00',1,NULL,'Y',NULL,3.00,15.00,'3','张三丰','13800000001','北京市朝阳区体育路88号','Y','支付宝'),(2,'ORD20260602001','2026-06-02 14:20:00',2,NULL,'Y',NULL,5.00,25.00,'2','李四海','13900000002','广州市海珠区运动大道168号','N','微信'),(3,'ORD20260603001','2026-06-03 10:00:00',1,NULL,'Y','2026-06-09 14:01:47',2.00,10.00,'2','张三丰','13800000001','北京市朝阳区体育路88号','Y','银行卡'),(4,'ORD20260604001','2026-06-04 16:45:00',3,NULL,'N',NULL,4.00,20.00,'0','王五洲','13600000003','成都市锦江区东风路55号','Y','支付宝'),(5,'ORD20260605001','2026-06-05 11:15:00',4,NULL,'Y',NULL,6.00,30.00,'3','赵六艺','13500000004','杭州市西湖区体育场路12号','N','微信'),(6,'ORD20260606001','2026-06-06 08:50:00',2,NULL,'Y',NULL,1.00,5.00,'0','李四海','13900000002','广州市海珠区运动大道168号','Y','银行卡'),(7,'ORD20260607001','2026-06-07 13:30:00',5,NULL,'N',NULL,3.00,15.00,'1','陈七剑','13700000005','上海市浦东新区张江路100号','Y','支付宝'),(8,'ORD1780974392732','2026-06-09 11:06:33',6,NULL,'Y',NULL,1.00,5.00,'1','爱因斯坦','13638697352','花园小区','Y','微信');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_code` varchar(20) NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `manufacturer_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `status` varchar(10) DEFAULT 'ON',
  `category_code` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `manufacturer_id` (`manufacturer_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`manufacturer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'P001','斯伯丁篮球',5,'74-604Y 比赛用球',299.00,25,'ON','BALL'),(2,'P002','耐克篮球',1,'Nike Elite 比赛篮球',349.00,8,'ON','BALL'),(3,'P003','耐克跑鞋',1,'Air Zoom 飞马40',899.00,14,'ON','SHOE'),(4,'P004','阿迪达斯跑鞋',2,'Ultra Boost 缓震跑鞋',1099.00,6,'ON','SHOE'),(5,'P005','李宁篮球鞋',3,'音速11 实战篮球鞋',599.00,20,'ON','SHOE'),(6,'P006','耐克运动T恤',1,'Dri-FIT 速干短袖',199.00,40,'ON','CLOTH'),(7,'P007','阿迪达斯运动裤',2,'透气运动长裤',259.00,30,'ON','CLOTH'),(8,'P008','安踏卫衣',4,'加绒运动卫衣',179.00,12,'ON','CLOTH'),(9,'P009','阿迪达斯足球',2,'欧冠比赛用球',399.00,3,'ON','BALL'),(10,'P010','李宁羽毛球拍',3,'碳纤维羽毛球拍',459.00,5,'ON','EQUIP'),(11,'P011','安踏跳绳',4,'竞速跳绳 轴承款',49.00,50,'ON','EQUIP'),(12,'P012','耐克背包',1,'训练双肩包',299.00,18,'ON','BAG');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase` (
  `purchase_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `manufacturer_id` int NOT NULL,
  `purchase_quantity` int NOT NULL DEFAULT '1',
  `purchase_date` datetime DEFAULT NULL,
  `arrival_date` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT 'pending' COMMENT 'pending=待收货 arrived=已到货',
  `notes` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`purchase_id`),
  KEY `product_id` (`product_id`),
  KEY `manufacturer_id` (`manufacturer_id`),
  CONSTRAINT `purchase_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `purchase_ibfk_2` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`manufacturer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` VALUES (1,2,1,10,'2026-06-09 13:51:59',NULL,'pending','低库存自动补货');
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-09 15:32:50

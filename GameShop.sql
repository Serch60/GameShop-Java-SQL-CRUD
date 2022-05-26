DROP DATABASE IF EXISTS GameShop; 
CREATE DATABASE GameShop;
SHOW databases;
USE GameShop;

CREATE TABLE games (
	id int (11) AUTO_INCREMENT,
	name varchar (255),
	launch date,
	platform varchar (255),
	price float,
	PRIMARY KEY (id)	
);
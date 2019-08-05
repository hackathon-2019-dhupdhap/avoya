--liquibase formatted sql
--changeset asif:avoya
--comment initial database creation


CREATE TABLE `users` (
 `id` int NOT NULL AUTO_INCREMENT,
 `account_id` INT NOT NULL,
 `current_tracker` varchar(50) DEFAULT NULL,
 `name` varchar(50) DEFAULT NULL,
 `nid` varchar(15) DEFAULT NULL,
 `address` varchar(50) DEFAULT NULL,
 `created_by` int NOT NULL,
 `created_at` datetime NOT NULL,
 `updated_by` int NOT NULL,
 `updated_at` datetime NOT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB;



CREATE TABLE `emergency_contacts` (
 `id` int NOT NULL AUTO_INCREMENT,
 `user_id` int NOT NULL,
 `contact_1` varchar(15) NOT NULL,
 `contact_2` varchar(15) NOT NULL,
 `contact_3` varchar(15) NOT NULL,
 `created_by` int NOT NULL,
 `created_at` datetime NOT NULL,
 `updated_by` int NOT NULL,
 `updated_at` datetime NOT NULL,
 PRIMARY KEY (`id`),
 INDEX `user_id` (`user_id`),
 CONSTRAINT `user_id_fk` 
 	FOREIGN KEY (`user_id`) 
 	REFERENCES `users` (`id`) 
 	ON DELETE NO ACTION 
 	ON UPDATE NO ACTION
) ENGINE=InnoDB;


CREATE TABLE `stations` (
 `id` int NOT NULL AUTO_INCREMENT,
 `location` varchar(50),
 `lat` DECIMAL(10,8),
 `lon` DECIMAL(11,8),
 PRIMARY KEY (`id`)
) ENGINE=InnoDB;





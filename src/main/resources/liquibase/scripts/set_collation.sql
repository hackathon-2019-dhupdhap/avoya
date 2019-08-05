--liquibase formatted sql
--changeset asif:avoya
--comment change database default character set and collation settings

ALTER DATABASE `avoya` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
SET default_storage_engine=INNODB;

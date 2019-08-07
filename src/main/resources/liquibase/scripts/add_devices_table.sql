CREATE TABLE `devices` (
  `id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `device_id` varchar(300) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) NOT NULL,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` int(11) NOT NULL
) ENGINE=InnoDB;

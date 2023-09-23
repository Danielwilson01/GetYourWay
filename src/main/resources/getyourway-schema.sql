-- Drop Table & Create table queries
DROP table `customer`;
CREATE TABLE `customer` (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(200) NOT NULL,
    `password` VARCHAR(250) NOT NULL
);
-- Drop Table & Create table queries
DROP table `booking`;
DROP table `users`;
CREATE TABLE `users` (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(200) NOT NULL,
    `password` VARCHAR(250) NOT NULL
);

CREATE TABLE `booking` (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `order_reference` VARCHAR(100) NOT NULL,
    `customer_id` INTEGER,
    FOREIGN KEY (`customer_id`) REFERENCES users(`id` )
);
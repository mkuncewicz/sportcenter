
DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
                           `date_of_birth` datetime(6) DEFAULT NULL,
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `apartment_number` varchar(255) DEFAULT NULL,
                           `building_number` varchar(255) DEFAULT NULL,
                           `city` varchar(255) DEFAULT NULL,
                           `first_name` varchar(255) NOT NULL,
                           `last_name` varchar(255) NOT NULL,
                           `phone_number` varchar(255) NOT NULL,
                           `street` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees` (
                             `date_of_birth` datetime(6) DEFAULT NULL,
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `apartment_number` varchar(255) DEFAULT NULL,
                             `building_number` varchar(255) DEFAULT NULL,
                             `city` varchar(255) DEFAULT NULL,
                             `first_name` varchar(255) NOT NULL,
                             `last_name` varchar(255) NOT NULL,
                             `phone_number` varchar(255) NOT NULL,
                             `street` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `specializations`;
CREATE TABLE `specializations` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `name` varchar(255) NOT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `spaces`;
CREATE TABLE `spaces` (
                          `capacity` int DEFAULT NULL,
                          `goal_count` int DEFAULT NULL,
                          `hoop_count` int DEFAULT NULL,
                          `is_sterile` bit(1) DEFAULT NULL,
                          `lane_count` int DEFAULT NULL,
                          `mat_count` int DEFAULT NULL,
                          `pool_depth` double DEFAULT NULL,
                          `pool_length` double DEFAULT NULL,
                          `square_footage` double DEFAULT NULL,
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `space_type` varchar(31) NOT NULL,
                          `name` varchar(255) NOT NULL,
                          `court_type` enum('WOOD','CONCRETE','TAR','SYNTHETIC') DEFAULT NULL,
                          `turf_type` enum('NATURAL','ARTIFICIAL','HYBRID') DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `contracts`;
CREATE TABLE `contracts` (
                             `date_end` date DEFAULT NULL,
                             `date_start` date DEFAULT NULL,
                             `salary` double NOT NULL,
                             `employee_id` bigint NOT NULL,
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `contract_status` enum('NEW','PENDING','REJECTED','CONFIRMED','IN_PROGRESS','COMPLETED','EXPIRING') DEFAULT NULL,
                             `contract_type` enum('EMPLOYMENT_CONTRACT','MANDATE_CONTRACT','SPECIFIC_TASK_CONTRACT','INTERNSHIP','B2B_CONTRACT') DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             KEY `FKf5c9xgkxh0n28hbhsgo5rkq58` (`employee_id`),
                             CONSTRAINT `FKf5c9xgkxh0n28hbhsgo5rkq58` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `employee_specialization`;
CREATE TABLE `employee_specialization` (
                                           `employee_id` bigint NOT NULL,
                                           `specialization_id` bigint NOT NULL,
                                           PRIMARY KEY (`employee_id`,`specialization_id`),
                                           KEY `FKlwwa7xsyua7vobmc7hospc7i7` (`specialization_id`),
                                           CONSTRAINT `FKixkb6qxlckjvh3ulo5s2emm1t` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`),
                                           CONSTRAINT `FKlwwa7xsyua7vobmc7hospc7i7` FOREIGN KEY (`specialization_id`) REFERENCES `specializations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `offers`;
CREATE TABLE `offers` (
                          `price` double NOT NULL,
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `description` varchar(510) NOT NULL,
                          `name` varchar(255) NOT NULL,
                          `offer_type` enum('ONE_TIME','SUBSCRIPTION') DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `offer_specialization`;
CREATE TABLE `offer_specialization` (
                                        `offer_id` bigint NOT NULL,
                                        `specialization_id` bigint NOT NULL,
                                        PRIMARY KEY (`offer_id`,`specialization_id`),
                                        KEY `FKm5heos8e2k97nev4lnr69vm6u` (`specialization_id`),
                                        CONSTRAINT `FKjhtrxwiisfxxtie5baw6q3m5g` FOREIGN KEY (`offer_id`) REFERENCES `offers` (`id`),
                                        CONSTRAINT `FKm5heos8e2k97nev4lnr69vm6u` FOREIGN KEY (`specialization_id`) REFERENCES `specializations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `space_specialization`;
CREATE TABLE `space_specialization` (
                                        `space_id` bigint NOT NULL,
                                        `specialization_id` bigint NOT NULL,
                                        PRIMARY KEY (`space_id`,`specialization_id`),
                                        KEY `FKn2smxw1snsxm36ngug8fxuyqf` (`specialization_id`),
                                        CONSTRAINT `FK5o270boav0u1jh9fpg4mvdxwe` FOREIGN KEY (`space_id`) REFERENCES `spaces` (`id`),
                                        CONSTRAINT `FKn2smxw1snsxm36ngug8fxuyqf` FOREIGN KEY (`specialization_id`) REFERENCES `specializations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `space_id` bigint DEFAULT NULL,
                             `name` varchar(255) NOT NULL,
                             PRIMARY KEY (`id`),
                             KEY `FKkp7stskibptrnc62v34peel05` (`space_id`),
                             CONSTRAINT `FKkp7stskibptrnc62v34peel05` FOREIGN KEY (`space_id`) REFERENCES `spaces` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `reservations`;
CREATE TABLE `reservations` (
                                `client_id` bigint DEFAULT NULL,
                                `date` datetime(6) DEFAULT NULL,
                                `employee_id` bigint DEFAULT NULL,
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `offer_id` bigint DEFAULT NULL,
                                `space_id` bigint DEFAULT NULL,
                                `reservation_status` enum('PENDING','PAID') DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FK6lekctbt4u88agg0b7cjsj6lf` (`client_id`),
                                KEY `FK26cya250clfqgfl59s9vi9e8x` (`employee_id`),
                                KEY `FKjk5eau9ty1r4m4ibk0tat5ulr` (`offer_id`),
                                KEY `FKamig3ih2d03a4kb15cuoax1rm` (`space_id`),
                                CONSTRAINT `FK26cya250clfqgfl59s9vi9e8x` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`),
                                CONSTRAINT `FK6lekctbt4u88agg0b7cjsj6lf` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`),
                                CONSTRAINT `FKamig3ih2d03a4kb15cuoax1rm` FOREIGN KEY (`space_id`) REFERENCES `spaces` (`id`),
                                CONSTRAINT `FKjk5eau9ty1r4m4ibk0tat5ulr` FOREIGN KEY (`offer_id`) REFERENCES `offers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8171 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
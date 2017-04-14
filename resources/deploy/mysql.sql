/* run this inside `mysql -u root', by issuing source resources/deploy/mysql.sql */

USE boteval_new;
CREATE USER 'boteval'@'localhost' IDENTIFIED BY 'boteval234%^&';
GRANT ALL ON boteval_new . * TO 'boteval'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE `boteval_new`.`sessions` (
  `text` VARCHAR(2048) NULL,
  `is_user` TINYINT NOT NULL,
  `time` DATETIME(6) NOT NULL,
  `session_id` INT NOT NULL,
  PRIMARY KEY (`is_user`, `time`, `session_id`));

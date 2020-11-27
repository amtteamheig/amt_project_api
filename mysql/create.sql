DROP SCHEMA IF EXISTS `gamificationdb`;
CREATE SCHEMA IF NOT EXISTS `gamificationdb` DEFAULT CHARACTER SET utf8;
USE `gamificationdb`;

create table if not exists `gamificationdb`.`api_key_entity` (`id` bigint not null auto_increment, `value` varchar(255), primary key (`id`)) engine=InnoDB;
create table if not exists `gamificationdb`.`badge_entity` (`id` bigint not null auto_increment, `image_url` tinyblob, `kind` varchar(255), `obtained_date` DATE, primary key (`id`)) engine=InnoDB;
create table if not exists `gamificationdb`.`point_scale_entity` (`id` bigint not null auto_increment, `kind` varchar(255), `points` integer, primary key (`id`)) engine=InnoDB;

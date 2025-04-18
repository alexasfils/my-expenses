CREATE TABLE Users (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name varchar(45),
	surname varchar(45),
	email varchar(50),
	password_hash varchar(100),
	phone varchar(100),
	currency varchar(100),
	creation_date Date
);
CREATE TABLE Users (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name varchar(50) NOT NULL,
	surname varchar(50) NOT NULL,
	email varchar(100) NOT NULL UNIQUE,
	password_hash varchar(255) NOT NULL,
	phone varchar(20) NOT NULL,
	currency varchar(3) NOT NULL,
	creation_date Date NOT NULL
);
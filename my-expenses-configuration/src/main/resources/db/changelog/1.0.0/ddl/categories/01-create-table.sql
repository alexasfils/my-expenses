CREATE TABLE Categories (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name varchar(100) NOT NULL,
	color varchar(100),
	is_default boolean NOT NULL DEFAULT FALSE
);
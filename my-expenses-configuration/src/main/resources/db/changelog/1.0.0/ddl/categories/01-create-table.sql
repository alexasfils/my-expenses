CREATE TABLE Categories (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name varchar(100) NOT NULL UNIQUE,
	color varchar(100),
	is_default boolean NOT NULL DEFAULT FALSE,
	id_user BIGINT NULL,
	FOREIGN KEY (id_user) REFERENCES users (id)
);
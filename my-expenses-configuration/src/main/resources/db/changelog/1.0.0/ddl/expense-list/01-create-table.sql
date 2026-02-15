CREATE TABLE Expense_list (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name varchar(100) NOT NULL,
	month NOT NULL,
	budget decimal(10,2) NOT NULL,
	total_expense decimal(10,2) NOT NULL,
	id_user BIGINT NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES users (id),
	CONSTRAINT uk_name_user UNIQUE (name, id_user),
	CONSTRAINT chk_month CHECK (month BETWEEN 1 AND 12)
);
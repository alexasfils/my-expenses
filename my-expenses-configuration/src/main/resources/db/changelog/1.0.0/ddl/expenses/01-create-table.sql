CREATE TABLE Expenses (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name varchar(100) NOT NULL,
	expense_date DATE NOT NULL,
	amount decimal(10,2) NOT NULL,
	description TEXT,
	id_expense_list BIGINT NOT NULL,
	id_category BIGINT NOT NULL,
	FOREIGN KEY (id_expense_list) REFERENCES expense_list (id),
	FOREIGN KEY (id_category) REFERENCES categories (id)
);
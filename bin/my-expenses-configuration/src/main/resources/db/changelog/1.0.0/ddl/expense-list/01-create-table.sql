CREATE TABLE Expense_list (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name varchar(100) NOT NULL,
	month int(12),
	Budget decimal(10,2),
	total_expense decimal(10,2),
	id_user BIGINT NOT NULL,
	FOREIGN KEY (id_user) REFERENCES users (id)
);
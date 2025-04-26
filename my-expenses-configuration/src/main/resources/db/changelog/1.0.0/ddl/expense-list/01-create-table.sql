CREATE TABLE Expense_list (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name varchar(100) NOT NULL,
	month int(12) NOT NULL,
	budget decimal(10,2) NOT NULL,
	total_expense decimal(10,2) NOT NULL,
	id_user BIGINT NOT NULL,
	FOREIGN KEY (id_user) REFERENCES users (id)
);
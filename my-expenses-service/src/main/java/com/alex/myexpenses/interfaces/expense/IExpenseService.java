package com.alex.myexpenses.interfaces.expense;

import java.util.List;

import com.alex.myexpenses.dto.expense.ExpenseDTO;

import lombok.NonNull;

public interface IExpenseService {
	
	List<ExpenseDTO> getAllExpenseByExpenseListId(@NonNull Long expenseListId);
	
	ExpenseDTO save(ExpenseDTO expenseDTO);
	
	Boolean deleteExpenseById(@NonNull Long expenseId);
	
	Boolean deleteExpenseByExpenseListAndUserId(Long expenseListId);
	
	ExpenseDTO updateExpense(ExpenseDTO expenseDTO);

}

package com.alex.myexpenses.interfaces.expense;

import com.alex.myexpenses.dto.expense.ExpenseCreateDTO;
import com.alex.myexpenses.dto.expense.ExpenseDetailDTO;
import com.alex.myexpenses.dto.expense.ExpenseSimpleDTO;
import com.alex.myexpenses.utility.PaginatorDTO;

import lombok.NonNull;

public interface IExpenseService {
	
	PaginatorDTO<ExpenseDetailDTO> getAllExpenseByExpenseListId(@NonNull Long expenseListId, int page, long size);
	
	ExpenseDetailDTO save(ExpenseCreateDTO expenseCreateDTO);
	
	Boolean deleteExpenseById(@NonNull Long expenseId);
	
	Boolean deleteExpenseByExpenseListAndUserId(Long expenseListId);
	
	ExpenseDetailDTO updateExpense(ExpenseSimpleDTO expenseSimpleDTO);

}

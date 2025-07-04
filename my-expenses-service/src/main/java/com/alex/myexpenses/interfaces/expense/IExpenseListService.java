package com.alex.myexpenses.interfaces.expense;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.alex.myexpenses.dto.expense.ExpenseListDTO;

public interface IExpenseListService {

	List<ExpenseListDTO> getAllUserExpenseList();
	
	ExpenseListDTO save(ExpenseListDTO expenseListDTO);
	
	Boolean deleteUserExpenseListByIdAndUserId(Long id);
	
	ExpenseListDTO updateUserExpenseListByIdAndUserId(ExpenseListDTO expenseListDTO);
	
	ExpenseListDTO getExpenseListById(@NotNull Long id);
	
//	List<ExpenseListDTO> getRandomExpenseListForDemo();
	
}

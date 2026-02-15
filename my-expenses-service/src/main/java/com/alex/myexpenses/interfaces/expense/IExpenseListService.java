package com.alex.myexpenses.interfaces.expense;

import javax.validation.constraints.NotNull;

import com.alex.myexpenses.dto.expense.ExpenseListCreateDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDetailDTO;
import com.alex.myexpenses.utility.PaginatorDTO;

public interface IExpenseListService {

	PaginatorDTO<ExpenseListDetailDTO> getAllUserExpenseList(int page, long size);
	
	ExpenseListDTO save(ExpenseListCreateDTO expenseListCreateDTO);
	
	Boolean deleteUserExpenseListByIdAndUserId(Long id);
	
	ExpenseListDTO updateUserExpenseListByIdAndUserId(ExpenseListDTO expenseListDTO);
	
	ExpenseListDetailDTO getExpenseListById(@NotNull Long id);
	
//	List<ExpenseListDTO> getRandomExpenseListForDemo();
	
}

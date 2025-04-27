package com.alex.myexpenses.service.expense;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.dto.expense.ExpenseDTO;
import com.alex.myexpenses.entity.expenses.ExpenseEntity;
import com.alex.myexpenses.entity.expenses.ExpenseListEntity;
import com.alex.myexpenses.interfaces.expense.IExpenseService;
import com.alex.myexpenses.repository.expense.ExpenseRepository;

@Service
public class ExpenseService implements IExpenseService{
	
	@Autowired
	private ExpenseRepository expenseRepository;

//	@Override
//	public List<ExpenseDTO> getAllExpenseByExpenseListId(Long expenseListId) {
//		List<ExpenseEntity> expenseLists = expenseRepository.findByExpenseListId(expenseListId);
//		
//		return null;
//	}

}

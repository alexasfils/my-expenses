package com.alex.myexpenses.service.expense.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.dto.expense.ExpenseDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseListDemoService {
	
	@Autowired
	private DemoDataStore store;
	
	 public ExpenseListDTO createExpenseList(ExpenseListDTO dto) {
	        return store.createExpenseList(dto);
	    }

	    public List<ExpenseListDTO> getAll() {
	        return store.getAllExpenseLists();
	    }

	    public boolean delete(Long id) {
	        return store.deleteExpenseList(id);
	    }

	    public ExpenseDTO addExpense(ExpenseDTO dto) {
	        return store.addExpense(dto);
	    }

	    public ExpenseListDTO getExpenseListById(Long listId) {
	        return store.getExpenseList(listId);
	    }

	    public void resetSession() {
	        store.reset();
	    }

}

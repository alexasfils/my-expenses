package com.alex.myexpenses.service.expense.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.dto.expense.ExpenseDetailDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDetailDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseListDemoService {
	
	private final DemoDataStore store;
	
	public ExpenseListDemoService(DemoDataStore store) {
		super();
		this.store = store;
	}
	
	 public ExpenseListDetailDTO createExpenseList(ExpenseListDetailDTO dto) {
	        return store.createExpenseList(dto);
	    }

	    public List<ExpenseListDetailDTO> getAll() {
	        return store.getAllExpenseLists();
	    }

	    public boolean delete(Long id) {
	        return store.deleteExpenseList(id);
	    }

	    public ExpenseDetailDTO addExpense(ExpenseDetailDTO dto) {
	        return store.addExpense(dto);
	    }

	    public ExpenseListDetailDTO getExpenseListById(Long listId) {
	        return store.getExpenseList(listId);
	    }

	    public void resetSession() {
	        store.reset();
	    }

}

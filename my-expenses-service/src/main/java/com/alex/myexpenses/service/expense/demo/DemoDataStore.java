package com.alex.myexpenses.service.expense.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.alex.myexpenses.controller.exception.DemoLimitReachedException;
import com.alex.myexpenses.dto.expense.ExpenseDetailDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDetailDTO;

import static com.alex.myexpenses.utility.AppConstants.Entity.*;
import static com.alex.myexpenses.utility.AppConstants.ExceptionMessage.*;


import lombok.extern.slf4j.Slf4j;

@Component
@SessionScope
@Slf4j
public class DemoDataStore {
	
	private final Map<Long, ExpenseListDetailDTO> expenseLists = Collections.synchronizedMap(new HashMap<>());
	private final Map<Long, List<ExpenseDetailDTO>> expenses = Collections.synchronizedMap(new HashMap<>());
	private final AtomicLong listIdCounter = new AtomicLong(1);
	private final AtomicLong expenseIdCounter = new AtomicLong(1);
	
	public ExpenseListDetailDTO createExpenseList(ExpenseListDetailDTO dto) {
		if (!expenseLists.isEmpty()) {
			log.warn("Tentativo di creazione lista multipla in modalità demo bloccato");
			throw new DemoLimitReachedException(DEMO_LIMIT_REACHED);
		}

		Long newId = listIdCounter.getAndIncrement();
		dto.setId(newId);
		dto.setTotalExpense(0.0);
		
		expenseLists.put(newId, dto);
		expenses.put(newId, new ArrayList<>());

		log.info("Created Demo ExpenseList: {}", dto);

		return dto;
	}

	public List<ExpenseListDetailDTO> getAllExpenseLists() {
	    List<ExpenseListDetailDTO> lists = new ArrayList<>(expenseLists.values());
	    for (ExpenseListDetailDTO list : lists) {
	        List<ExpenseDetailDTO> listExpenses = expenses.get(list.getId());
	        if (listExpenses == null) {
	            listExpenses = new ArrayList<>();
	        }
	        list.setExpenses(listExpenses);
	    }
	    return lists;
	}

	public boolean deleteExpenseList(Long id) {
		if (expenseLists.remove(id) != null) {
			expenses.remove(id);
			return true;
		}
		return false;
	}

	public ExpenseDetailDTO addExpense(ExpenseDetailDTO dto) {
		log.info("Current expenseLists: {}", expenseLists.keySet());
		Long listId = dto.getExpenseListId();

		// Verifica se esiste la lista
		if (!expenseLists.containsKey(listId)) {
			throw new IllegalArgumentException(EXPENSE_LIST + " con id " + listId + " non esiste.");
		}
		List<ExpenseDetailDTO> listExpenses = expenses.computeIfAbsent(listId, k -> Collections.synchronizedList(new ArrayList<>()));

		Long newId = expenseIdCounter.getAndIncrement();
		dto.setId(newId);
		listExpenses.add(dto);

		// Aggiorna il totale delle spese nella lista
		ExpenseListDetailDTO list = expenseLists.get(listId);
		double total = listExpenses.stream().mapToDouble(e -> e.getAmount() != null ? e.getAmount() : 0.0).sum();
		log.info("Added Expense ID {} to List ID {}", newId, listId);
		list.setTotalExpense(total);

		return dto;
	}

	public ExpenseListDetailDTO getExpenseList(Long listId) {
		ExpenseListDetailDTO list = expenseLists.get(listId);
		
		if(list != null) {
			List<ExpenseDetailDTO> listExpenses = expenses.get(listId);
			
			list.setExpenses(listExpenses != null ? listExpenses : new ArrayList<>());
			return list;
		}
		
		
		return null;
	}

	public void reset() {
		expenseLists.clear();
		expenses.clear();
		listIdCounter.set(1);
		expenseIdCounter.set(1);
	}

}

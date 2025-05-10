package com.alex.myexpenses.service.expense.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.alex.myexpenses.dto.expense.ExpenseDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DemoDataStore {
	private final Map<Long, ExpenseListDTO> expenseLists = Collections.synchronizedMap(new HashMap<>());
	private final Map<Long, List<ExpenseDTO>> expenses = Collections.synchronizedMap(new HashMap<>());
	private Long listIdCounter = 1L;
	private Long expenseIdCounter = 1L;

	public ExpenseListDTO createExpenseList(ExpenseListDTO dto) {
		if (!expenseLists.isEmpty()) {
			throw new RuntimeException("Puoi creare solo una lista nella modalità demo");
		}

		dto.setId(listIdCounter);
		dto.setTotalExpense(0.0);
		expenseLists.put(dto.getId(), dto);
		expenses.put(dto.getId(), new ArrayList<>());

		System.out.println(dto);

		return dto;
	}

	public List<ExpenseListDTO> getAllExpenseLists() {
	    List<ExpenseListDTO> lists = new ArrayList<>(expenseLists.values());
	    for (ExpenseListDTO list : lists) {
	        List<ExpenseDTO> listExpenses = expenses.get(list.getId());
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

	public ExpenseDTO addExpense(ExpenseDTO dto) {

		log.info("Current expenseLists: {}", expenseLists.keySet());
		Long listId = dto.getExpenseListId();

		System.out.println("dto.getExpenseListId() " + dto.getExpenseListId());

		// Verifica se esiste la lista
		if (!expenseLists.containsKey(listId)) {
			throw new IllegalArgumentException("La lista spese con ID " + listId + " non esiste.");
		}

		List<ExpenseDTO> listExpenses = expenses.get(listId);
		if (listExpenses == null) {
			// Per sicurezza, inizializza una lista vuota se manca
			listExpenses = new ArrayList<>();
			expenses.put(listId, listExpenses);
		}

		dto.setId(expenseIdCounter++);
		listExpenses.add(dto);

		// Aggiorna il totale delle spese nella lista
		ExpenseListDTO list = expenseLists.get(listId);
		double total = listExpenses.stream().mapToDouble(ExpenseDTO::getAmount).sum();
		list.setTotalExpense(total);

		return dto;
	}

	public ExpenseListDTO getExpenseList(Long listId) {
		ExpenseListDTO list = expenseLists.get(listId);
		
		if(list != null) {
			List<ExpenseDTO> listExpenses = expenses.get(listId);
			if(listExpenses ==null) {
				listExpenses = new ArrayList<>();
			}
			list.setExpenses(listExpenses);
		}
		
		
		return expenseLists.get(listId);
	}

	public void reset() {
		expenseLists.clear();
		expenses.clear();
		listIdCounter = 1L;
		expenseIdCounter = 1L;
	}

}

package com.alex.myexpenses.controller.expense;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alex.myexpenses.dto.expense.ExpenseCreateDTO;
import com.alex.myexpenses.dto.expense.ExpenseDetailDTO;
import com.alex.myexpenses.dto.expense.ExpenseSimpleDTO;
import com.alex.myexpenses.interfaces.expense.IExpenseService;
import com.alex.myexpenses.utility.PaginatorDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/expense")
public class ExpenseController {

	@Autowired
	private IExpenseService expenseService;
	
	@GetMapping("/all/{expenseListId}")
	public ResponseEntity<PaginatorDTO<ExpenseDetailDTO>> getAllUserExpense(
			@PathVariable Long expenseListId,
			@RequestParam(defaultValue = "0") Integer page, 
            @RequestParam(defaultValue = "5") Integer size) {
		
		return new ResponseEntity<PaginatorDTO<ExpenseDetailDTO>>(expenseService.getAllExpenseByExpenseListId(expenseListId,page, size), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ExpenseDetailDTO> createExpenseList(@Valid @RequestBody ExpenseCreateDTO expenseCreateDTO) {
		return new ResponseEntity<ExpenseDetailDTO>(expenseService.save(expenseCreateDTO) , HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{expenseId}")
	public ResponseEntity<Boolean> deleteExpenseById(@PathVariable Long expenseId) {
		return ResponseEntity.ok(expenseService.deleteExpenseById(expenseId));
	}
	
	@DeleteMapping("/deleteall/{expenselistId}")
	public ResponseEntity<Boolean> deleteExpenseByExpenseListId(@PathVariable Long expenseListId) {
		return ResponseEntity.ok(expenseService.deleteExpenseByExpenseListAndUserId(expenseListId));
	}
	
	@PutMapping("/update")
	public ResponseEntity<ExpenseDetailDTO> updateUserExpense(@Valid @RequestBody ExpenseSimpleDTO expenseSimpleDTO) {
		return new ResponseEntity<ExpenseDetailDTO>(expenseService.updateExpense(expenseSimpleDTO) , HttpStatus.OK);
	}
	
}

package com.alex.myexpenses.controller.expense;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.alex.myexpenses.dto.expense.ExpenseDTO;
import com.alex.myexpenses.interfaces.expense.IExpenseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/expense")
public class ExpenseController {

	@Autowired
	private IExpenseService expenseService;
	
	@GetMapping("/all/{expenseListId}")
	public ResponseEntity<List<ExpenseDTO>> getAllUserExpense(@PathVariable Long expenseListId){
		
		return new ResponseEntity<List<ExpenseDTO>>(expenseService.getAllExpenseByExpenseListId(expenseListId), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ExpenseDTO> createExpenseList(@RequestBody ExpenseDTO expenseDTO){
		return new ResponseEntity<ExpenseDTO>(expenseService.save(expenseDTO) , HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{expenseId}")
	public ResponseEntity<Boolean> deleteExpenseById(@PathVariable Long expenseId) {
		return ResponseEntity.ok(expenseService.deleteExpenseById(expenseId));
	}
	
	@DeleteMapping("/deleteall/{expenselistId}")
	public ResponseEntity<Boolean> deleteExpenseByExpenseListId(@PathVariable Long expenseListId) {
		return ResponseEntity.ok(expenseService.deleteExpenseByExpenseListAndUserId(expenseListId));
	}
	
	@PutMapping("/udate")
	public ResponseEntity<ExpenseDTO> updateUserExpense(@RequestBody ExpenseDTO expenseDTO){
		return new ResponseEntity<ExpenseDTO>(expenseService.updateExpense(expenseDTO) , HttpStatus.OK);

	}
	
}

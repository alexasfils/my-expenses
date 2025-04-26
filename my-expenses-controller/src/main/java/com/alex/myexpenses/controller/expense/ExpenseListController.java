package com.alex.myexpenses.controller.expense;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.myexpenses.dto.expense.ExpenseListDTO;
import com.alex.myexpenses.interfaces.expense.IExpenseListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/expenses")
public class ExpenseListController {
	
	@Autowired
	private IExpenseListService expenseListService;
	
	@GetMapping("/all")
	public ResponseEntity<List<ExpenseListDTO>> getAllUserExpenseList(){
		
		return new ResponseEntity<List<ExpenseListDTO>>(expenseListService.getAllUserExpenseList(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ExpenseListDTO> createExpenseList(@RequestBody ExpenseListDTO expenseListDTO){
		return new ResponseEntity<ExpenseListDTO>(expenseListService.save(expenseListDTO) , HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{expenseListId}")
	public ResponseEntity<Boolean> deleteUserExpenseList(@PathVariable Long expenseListId) {
		return ResponseEntity.ok(expenseListService.deleteUserExpenseListByidAndUserId(expenseListId));
	}

}

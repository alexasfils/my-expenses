package com.alex.myexpenses.controller.expense;

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

import com.alex.myexpenses.dto.expense.ExpenseListCreateDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDetailDTO;
import com.alex.myexpenses.interfaces.expense.IExpenseListService;
import com.alex.myexpenses.utility.PaginatorDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/expenses")
public class ExpenseListController {
	
	@Autowired
	private IExpenseListService expenseListService;
	
	@GetMapping("/all")
	public ResponseEntity<PaginatorDTO<ExpenseListDetailDTO>> getAllUserExpenseList(
			@RequestParam(defaultValue = "0") Integer page, 
            @RequestParam(defaultValue = "5") Integer size){
		
		return new ResponseEntity<PaginatorDTO<ExpenseListDetailDTO>>(expenseListService.getAllUserExpenseList(page, size), HttpStatus.OK);
	}
	
	@GetMapping("/{expenseListId}")
	public ResponseEntity<ExpenseListDetailDTO> getExpenseListById(@PathVariable Long expenseListId){
		
		return new ResponseEntity<ExpenseListDetailDTO>(expenseListService.getExpenseListById(expenseListId), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ExpenseListDTO> createExpenseList(@RequestBody ExpenseListCreateDTO expenseListCreateDTO){
		return new ResponseEntity<ExpenseListDTO>(expenseListService.save(expenseListCreateDTO) , HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{expenseListId}")
	public ResponseEntity<Boolean> deleteUserExpenseList(@PathVariable Long expenseListId) {
		return ResponseEntity.ok(expenseListService.deleteUserExpenseListByIdAndUserId(expenseListId));
	}
	
	@PutMapping("/udate")
	public ResponseEntity<ExpenseListDTO> updateUserExpenseList(@RequestBody ExpenseListDTO expenseListDTO){
		return new ResponseEntity<ExpenseListDTO>(expenseListService.updateUserExpenseListByIdAndUserId(expenseListDTO) , HttpStatus.OK);
	}
	
}

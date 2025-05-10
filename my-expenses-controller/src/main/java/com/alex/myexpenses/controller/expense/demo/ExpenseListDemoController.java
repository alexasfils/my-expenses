package com.alex.myexpenses.controller.expense.demo;

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

import com.alex.myexpenses.dto.expense.ExpenseDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDTO;
import com.alex.myexpenses.service.expense.demo.ExpenseListDemoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/demo")
public class ExpenseListDemoController {
	
	@Autowired
    private ExpenseListDemoService demoService;

    @PostMapping
    public ResponseEntity<ExpenseListDTO> createList(@RequestBody ExpenseListDTO dto) {
        return new ResponseEntity<>(demoService.createExpenseList(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseListDTO>> getAllLists() {
        return ResponseEntity.ok(demoService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteList(@PathVariable Long id) {
        return ResponseEntity.ok(demoService.delete(id));
    }

    @PostMapping("/{listId}/expenses")
    public ResponseEntity<ExpenseDTO> addExpense(@PathVariable Long listId, @RequestBody ExpenseDTO dto) {
        dto.setExpenseListId(listId);
        return new ResponseEntity<>(demoService.addExpense(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{listId}/expenses")
    public ResponseEntity<ExpenseListDTO> getExpenses(@PathVariable Long listId) {
        return ResponseEntity.ok(demoService.getExpenseListById(listId));
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        demoService.resetSession();
        return ResponseEntity.noContent().build();
    }

}

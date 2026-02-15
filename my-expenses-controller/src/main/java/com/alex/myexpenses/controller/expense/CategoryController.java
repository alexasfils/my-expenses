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

import com.alex.myexpenses.dto.expense.CategoryDTO;
import com.alex.myexpenses.dto.expense.CategoryDetailDTO;
import com.alex.myexpenses.interfaces.expense.ICategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private ICategoryService categoryService;
	
	@GetMapping("/all")
	public ResponseEntity<List<CategoryDetailDTO>> getAllUserCategories(){
		
		return new ResponseEntity<List<CategoryDetailDTO>>(categoryService.getAllCategories(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO){
		return new ResponseEntity<CategoryDTO>(categoryService.save(categoryDTO) , HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Boolean> deleteUserCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.deleteCategoryAndMigrateExpenses(categoryId));
	}
	
	@PutMapping("/udate")
	public ResponseEntity<CategoryDTO> updateUserCategory(@RequestBody CategoryDTO categoryDTO){
		return new ResponseEntity<CategoryDTO>(categoryService.updateUserCategoryByIdAndUserId(categoryDTO) , HttpStatus.OK);
	}

}

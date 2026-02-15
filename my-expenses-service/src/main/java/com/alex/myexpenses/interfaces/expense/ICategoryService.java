package com.alex.myexpenses.interfaces.expense;

import java.util.List;

import com.alex.myexpenses.dto.expense.CategoryDTO;
import com.alex.myexpenses.dto.expense.CategoryDetailDTO;

public interface ICategoryService {
	
	List<CategoryDetailDTO> getAllCategories();
	
	public CategoryDTO save(CategoryDTO categoryDTO);
	
	public Boolean deleteCategoryAndMigrateExpenses(Long categoryId);
	
	public CategoryDTO updateUserCategoryByIdAndUserId(CategoryDTO categoryDTO);

}

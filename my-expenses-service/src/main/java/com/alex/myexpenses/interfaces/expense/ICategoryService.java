package com.alex.myexpenses.interfaces.expense;

import java.util.List;

import com.alex.myexpenses.dto.expense.CategoryDTO;

public interface ICategoryService {
	
	List<CategoryDTO> getAllCategories();
	
	public CategoryDTO save(CategoryDTO categoryDTO);
	
	public Boolean deleteUserCategoryByIdAndUserId(Long id);
	
	public CategoryDTO updateUserCategoryByIdAndUserId(CategoryDTO categoryDTO);

}

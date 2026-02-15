package com.alex.myexpenses.service.expense;

import static com.alex.myexpenses.utility.AppConstants.Entity.CATEGORY;
import static com.alex.myexpenses.utility.AppConstants.Field.ID;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.controller.exception.AlreadyExistsException;
import com.alex.myexpenses.controller.exception.ResourceNotFoundException;
import com.alex.myexpenses.dto.expense.CategoryDTO;
import com.alex.myexpenses.dto.expense.CategoryDetailDTO;
import com.alex.myexpenses.entity.expenses.CategoryEntity;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.interfaces.expense.ICategoryService;
import com.alex.myexpenses.repository.expense.CategoryRepository;
import com.alex.myexpenses.repository.expense.ExpenseRepository;
import com.alex.myexpenses.service.security.SecurityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryService implements ICategoryService {
	
	private final CategoryRepository categoryRepository;
	private final ExpenseRepository expenseRepository;
	private final SecurityService securityService;
	private final ModelMapper modelMapper;
	
	public CategoryService(CategoryRepository categoryRepository, ExpenseRepository expenseRepository, SecurityService securityService, ModelMapper modelMapper) {
		super();
		this.categoryRepository = categoryRepository;
		this.expenseRepository = expenseRepository;
		this.securityService = securityService;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<CategoryDetailDTO> getAllCategories() {
		UserEntity user = securityService.getAuthenticatedUser();
		
		List<CategoryEntity> categoryList = categoryRepository.findDefaultAndUserCategories(user.getId());
		return categoryList.stream()
				.map(x -> modelMapper.map(x, CategoryDetailDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public CategoryDTO save(CategoryDTO categoryDTO) {
		UserEntity user = securityService.getAuthenticatedUser();

		if (categoryRepository.existsByNameIgnoreCaseAndUserId(categoryDTO.getName().trim(), user.getId())) {
	        throw new AlreadyExistsException("Category already exists.");
	    }

		CategoryEntity categoryEntity = modelMapper.map(categoryDTO, CategoryEntity.class);
		categoryEntity.initUserCategory(categoryDTO.getName(), user);

		return modelMapper.map(categoryRepository.save(categoryEntity), CategoryDTO.class);
	}

	@Override
	@Transactional
	public Boolean deleteCategoryAndMigrateExpenses(Long categoryId) {
		UserEntity user = securityService.getAuthenticatedUser();
		
		CategoryEntity categoryToDelete = findCategoryByIdAndUserIdOrThrow(categoryId, user.getId());
	    
	    categoryToDelete.checkActionAllowed();
	    
	    CategoryEntity defaultCategory = categoryRepository.findByNameAndIsDefaultTrue("Others")
	            .orElseThrow(() -> new ResourceNotFoundException("Default Category Others", "name", "Others"));
	    
	    expenseRepository.migrateExpenses(categoryToDelete, defaultCategory, user.getId());
	    
	    categoryRepository.delete(categoryToDelete);
	    
	    log.info("Category {} deleted. Expenses migrated to 'Altro' for user {}", categoryId, user.getEmail());
		
		return true;
	}

	@Override
	@Transactional
	public CategoryDTO updateUserCategoryByIdAndUserId(CategoryDTO categoryDTO) {
		UserEntity user = securityService.getAuthenticatedUser();
	    
	    CategoryEntity categoryEntity = findCategoryByIdAndUserIdOrThrow(categoryDTO.getId(), user.getId());
	    
	    categoryEntity.checkActionAllowed();

	    categoryEntity.updateName(categoryDTO.getName());
	    categoryEntity.setColor(categoryDTO.getColor());
		return modelMapper.map(categoryRepository.save(categoryEntity), CategoryDTO.class);
	}
	
	private CategoryEntity findCategoryByIdAndUserIdOrThrow(Long categoryId, Long userId) {
		return categoryRepository.findByIdAndUserId(categoryId, userId)
	    		.orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, categoryId));
	}

}

package com.alex.myexpenses.service.expense;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.dto.expense.CategoryDTO;
import com.alex.myexpenses.dto.user.UserPrincipal;
import com.alex.myexpenses.entity.expenses.CategoryEntity;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.interfaces.expense.ICategoryService;
import com.alex.myexpenses.repository.expense.CategoryRepository;
import com.alex.myexpenses.repository.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryService implements ICategoryService{
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<CategoryDTO> getAllCategories() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("user is not Authenticated");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername(); // ← la tua email
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
		
		List<CategoryEntity> categoryList = categoryRepository.findByIsDefaultTrueAndUserId(user.getId());
		return categoryList.stream()
				.map(x -> modelMapper.map(x, CategoryDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public CategoryDTO save(CategoryDTO categoryDTO) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("user is not Authenticated");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername();
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    CategoryEntity categoryEntity = modelMapper.map(categoryDTO, CategoryEntity.class);
	    categoryEntity.setUser(user);
	    
		return modelMapper.map(categoryRepository.save(categoryEntity), CategoryDTO.class);
	}

	@Override
	@Transactional
	public Boolean deleteUserCategoryByIdAndUserId(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("user is not Authenticated");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername();
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    Integer count = categoryRepository.deleteByIdAndUserId(id, user.getId());
	    if(count == 0) {
	    	throw new EntityNotFoundException("UserExpenseList not found in database or not valid id or userId ");
	    }
	    log.info("User Category deleted: {}", id);
		
		return true;
	}

	@Override
	@Transactional
	public CategoryDTO updateUserCategoryByIdAndUserId(CategoryDTO categoryDTO) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("user is not Authenticated");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername(); // ← la tua email
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    CategoryEntity categoryEntity = categoryRepository.findByIdAndUserId(categoryDTO.getId(), user.getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Category not found in database or not valid id or userId"));
	    
	    categoryEntity.setName(categoryDTO.getName());
	    categoryEntity.setColor(categoryDTO.getColor());
		return modelMapper.map(categoryRepository.save(categoryEntity), CategoryDTO.class);
	}

}

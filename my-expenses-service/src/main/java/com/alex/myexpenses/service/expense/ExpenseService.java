package com.alex.myexpenses.service.expense;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.dto.expense.ExpenseDTO;
import com.alex.myexpenses.dto.user.UserPrincipal;
import com.alex.myexpenses.entity.expenses.CategoryEntity;
import com.alex.myexpenses.entity.expenses.ExpenseEntity;
import com.alex.myexpenses.entity.expenses.ExpenseListEntity;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.interfaces.expense.IExpenseService;
import com.alex.myexpenses.repository.expense.CategoryRepository;
import com.alex.myexpenses.repository.expense.ExpenseListRepository;
import com.alex.myexpenses.repository.expense.ExpenseRepository;
import com.alex.myexpenses.repository.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseService implements IExpenseService{
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
	private ExpenseListRepository expenseListRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<ExpenseDTO> getAllExpenseByExpenseListId(Long expenseListId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("user is not Authenticated");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername(); // ← la tua email
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
		
		List<ExpenseEntity> expenseLists = expenseRepository.findByExpenseListIdAndExpenseListUserId(expenseListId, user.getId());
		
		return expenseLists.stream()
				.map(x -> modelMapper.map(x, ExpenseDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ExpenseDTO save(ExpenseDTO expenseDTO) {
		//Find category
		CategoryEntity categoryEntity = categoryRepository.findById(expenseDTO.getCategory().getId())
				.orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + expenseDTO.getExpenseListId()));
		//Find expenseList
		ExpenseListEntity expenseListEntity = expenseListRepository.findById(expenseDTO.getExpenseListId())
				.orElseThrow(() -> new EntityNotFoundException("ExpenseListEntity not found with ID: " + expenseDTO.getExpenseListId()));
		//Crate and mapp ExpenseEntity
		ExpenseEntity expenseEntity = modelMapper.map(expenseDTO, ExpenseEntity.class);
		//set category
		expenseEntity.setCategory(categoryEntity);
		//save ExpenseEntity
		ExpenseEntity savedExpense = expenseRepository.save(expenseEntity);
		//calcolate ExpenseLists totalExpenses
		Double updatedTotal = expenseListEntity.getTotalExpense() + savedExpense.getAmount();
		//set ExpenseLists totalExpenses
		expenseListEntity.setTotalExpense(updatedTotal);
		//save ExpenseListEntity
		expenseListRepository.save(expenseListEntity);
		
		return modelMapper.map(savedExpense, ExpenseDTO.class);
	}

	@Override
	@Transactional
	public Boolean deleteExpenseById(Long expenseId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("user is not Authenticated");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername(); // ← la tua email
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    ExpenseEntity expense = expenseRepository.findById(expenseId)
	            .orElseThrow(() -> new EntityNotFoundException("Expense not found in database or not valid id "));
	    
	    if (!expense.getExpenseList().getUser().getId().equals(user.getId())) {
	        throw new RuntimeException("You are not authorized to delete this expense");
	    }
	     expenseRepository.deleteById(expenseId);
	    
	    log.info("Expense {} deleted by user {}", expenseId, email);
		
		return true;
	}

	@Override
	@Transactional
	public Boolean deleteExpenseByExpenseListAndUserId(Long expenseListId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("user is not Authenticated");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername(); // ← la tua email
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    Integer deletedCount = expenseRepository.deleteByExpenseListIdAndUserId(expenseListId, user.getId());
	    		if(deletedCount == 0) {
	            throw new EntityNotFoundException("No expenses found for the given expense list and user.");
	    		}
	    		
	    		log.info("Deleted {} expenses from expenseList {} by user {}", deletedCount, expenseListId, email);
		return true;
	}

	@Override
	@Transactional
	public ExpenseDTO updateExpense(ExpenseDTO expenseDTO) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("user is not Authenticated");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername(); // ← la tua email
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
	    
	    CategoryEntity category = categoryRepository.findById(expenseDTO.getCategory().getId())
	    		.orElseThrow(() -> new EntityNotFoundException("Category not found in database or not valid id "));
	    
	    ExpenseEntity expense = expenseRepository.findByIdAndExpenseListUserId(expenseDTO.getId(), user.getId())
	            .orElseThrow(() -> new AccessDeniedException("You are not allowed to update this expense"));
	    
	    expense.setName(expenseDTO.getName());
	    expense.setExpenseDate(expenseDTO.getExpenseDate());
	    expense.setAmount(expenseDTO.getAmount());
	    expense.setDescription(expenseDTO.getDescription());
	    expense.setCategory(category);
	    
	    log.info("Expense with id {} updated by user {}", expense.getId(), email);
		
		return modelMapper.map(expenseRepository.save(expense), ExpenseDTO.class);
	}

}

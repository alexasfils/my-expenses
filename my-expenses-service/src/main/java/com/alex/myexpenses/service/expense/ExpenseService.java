package com.alex.myexpenses.service.expense;

import static com.alex.myexpenses.utility.AppConstants.Entity.CATEGORY;
import static com.alex.myexpenses.utility.AppConstants.Entity.EXPENSE;
import static com.alex.myexpenses.utility.AppConstants.Entity.EXPENSE_LIST;
import static com.alex.myexpenses.utility.AppConstants.Field.ID;
import static com.alex.myexpenses.utility.AppConstants.Field.NAME;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.controller.exception.ResourceNotFoundException;
import com.alex.myexpenses.dto.expense.ExpenseCreateDTO;
import com.alex.myexpenses.dto.expense.ExpenseDetailDTO;
import com.alex.myexpenses.dto.expense.ExpenseSimpleDTO;
import com.alex.myexpenses.entity.expenses.CategoryEntity;
import com.alex.myexpenses.entity.expenses.ExpenseEntity;
import com.alex.myexpenses.entity.expenses.ExpenseListEntity;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.interfaces.expense.IExpenseService;
import com.alex.myexpenses.repository.expense.CategoryRepository;
import com.alex.myexpenses.repository.expense.ExpenseListRepository;
import com.alex.myexpenses.repository.expense.ExpenseRepository;
import com.alex.myexpenses.service.security.SecurityService;
import com.alex.myexpenses.utility.PaginatorDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseService implements IExpenseService {
	
	private final ExpenseRepository expenseRepository;
	private final ExpenseListRepository expenseListRepository;
	private final CategoryRepository categoryRepository;
	private final SecurityService securityService;
	private final ModelMapper modelMapper;
	
	public ExpenseService(ExpenseRepository expenseRepository, ExpenseListRepository expenseListRepository,
			CategoryRepository categoryRepository, SecurityService securityService, ModelMapper modelMapper) {
		super();
		this.expenseRepository = expenseRepository;
		this.expenseListRepository = expenseListRepository;
		this.categoryRepository = categoryRepository;
		this.securityService = securityService;
		this.modelMapper = modelMapper;
	}

	@Override
	public PaginatorDTO<ExpenseDetailDTO> getAllExpenseByExpenseListId(Long expenseListId, int page, long size) {
		Pageable pageable = PageRequest.of(page, (int) size);
		
		UserEntity user = securityService.getAuthenticatedUser();
		
		Page<ExpenseEntity> expenseLists = expenseRepository.findByExpenseListIdAndExpenseListUserId(expenseListId, user.getId(), pageable);
		List<ExpenseDetailDTO> expenseDTO = expenseLists.getContent().stream()
				.map(x -> modelMapper.map(x, ExpenseDetailDTO.class))
				.collect(Collectors.toList());
		return new PaginatorDTO<>(expenseDTO, expenseLists.getTotalPages(), expenseLists.getTotalElements());
	}

	@Override
	@Transactional
	public ExpenseDetailDTO save(ExpenseCreateDTO expenseCreateDTO) {
		UserEntity user = securityService.getAuthenticatedUser();
		
		//Find category
	    CategoryEntity category = findCategoryOrThrow(expenseCreateDTO.getCategoryName(), user.getId());
	    
		//Find expenseList
		ExpenseListEntity expenseListEntity = expenseListRepository.findByIdAndUserId(expenseCreateDTO.getExpenseListId(), user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(EXPENSE_LIST, ID, expenseCreateDTO.getExpenseListId()));
		//Crate and map ExpenseEntity
		ExpenseEntity expenseEntity = modelMapper.map(expenseCreateDTO, ExpenseEntity.class);
		expenseEntity.setId(null);
		expenseEntity.setExpenseList(expenseListEntity);
		expenseEntity.setCategory(category);
		
		//calcolate add expense amount to ExpenseList totalExpenses
		expenseListEntity.addAmount(expenseEntity.getAmount());
		
		ExpenseEntity savedExpense = expenseRepository.saveAndFlush(expenseEntity);
		
		expenseListRepository.save(expenseListEntity);
		
		return modelMapper.map(savedExpense, ExpenseDetailDTO.class);
	}
	
	@Override
	@Transactional
	public Boolean deleteExpenseById(Long expenseId) {
		UserEntity user = securityService.getAuthenticatedUser();
	    
	    ExpenseEntity expense = expenseRepository.findById(expenseId)
	            .orElseThrow(() -> new ResourceNotFoundException(EXPENSE, ID, expenseId));
	    
	    if (!expense.getExpenseList().getUser().getId().equals(user.getId())) {
	        throw new AccessDeniedException("You are not authorized to delete this expense");
	    }
	    expenseRepository.deleteById(expenseId);
	    expenseRepository.flush();
	    
	    ExpenseListEntity list = expense.getExpenseList();

	    list.removeExpenseFromTotal(expense.getAmount());
	    expenseListRepository.save(list);
	    
	    log.info("Expense {} deleted by user {}", expenseId, user.getEmail());
		
		return true;
	}

	@Override
	@Transactional
	public Boolean deleteExpenseByExpenseListAndUserId(Long expenseListId) {
		UserEntity user = securityService.getAuthenticatedUser();

		Integer deletedCount = expenseRepository.deleteByExpenseListIdAndUserId(expenseListId, user.getId());
		if (deletedCount == 0) {
			throw new ResourceNotFoundException(EXPENSE, ID, expenseListId);
		}

		// aggiorna il totale nella lista
		ExpenseListEntity list = expenseListRepository.findById(expenseListId)
				.orElseThrow(() -> new ResourceNotFoundException(EXPENSE_LIST, ID, expenseListId));
		list.resetTotal();
		expenseListRepository.save(list);

		log.info("Deleted {} expenses from expenseList {} by user {}", deletedCount, expenseListId, user.getEmail());
		return true;
	}

	@Override
	@Transactional
	public ExpenseDetailDTO updateExpense(ExpenseSimpleDTO expenseSimpleDTO) {
		UserEntity user = securityService.getAuthenticatedUser();

		ExpenseEntity expense = expenseRepository.findByIdAndExpenseListUserId(expenseSimpleDTO.getId(), user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(EXPENSE, ID, expenseSimpleDTO.getId()));

		ExpenseListEntity expenseList = expense.getExpenseList();
		Double oldAmount = expense.getAmount();

		modelMapper.map(expenseSimpleDTO, expense);
		// Find category
		CategoryEntity category = findCategoryOrThrow(expenseSimpleDTO.getCategoryName(), user.getId());
		expense.setCategory(category);

		if (! Objects.equals(oldAmount, expense.getAmount())) {
			expenseList.updateBalance(oldAmount, expense.getAmount());
			expenseListRepository.save(expenseList);
		}
		
		ExpenseEntity updatedExpense = expenseRepository.saveAndFlush(expense);

		log.info("Expense with id {} updated by user {}", expense.getId(), user.getEmail());

		return modelMapper.map(updatedExpense, ExpenseDetailDTO.class);
	}
	
	private CategoryEntity findCategoryOrThrow(String categoryName, Long userId) {
		return categoryRepository.findByNameAndUserId(categoryName, userId).or(() -> 
		categoryRepository.findByNameAndIsDefaultTrue(categoryName)).orElseThrow(() -> 
		new ResourceNotFoundException(CATEGORY, NAME, categoryName));
	}

}

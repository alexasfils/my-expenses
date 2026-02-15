package com.alex.myexpenses.service.expense;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.controller.exception.AlreadyExistsException;
import com.alex.myexpenses.controller.exception.ResourceNotFoundException;
import com.alex.myexpenses.dto.expense.ExpenseListCreateDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDetailDTO;
import com.alex.myexpenses.entity.expenses.ExpenseListEntity;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.interfaces.expense.IExpenseListService;
import com.alex.myexpenses.repository.expense.ExpenseListRepository;
import com.alex.myexpenses.service.security.SecurityService;
import com.alex.myexpenses.utility.PaginatorDTO;

import static com.alex.myexpenses.utility.AppConstants.Entity.*;
import static com.alex.myexpenses.utility.AppConstants.Field.*;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseListService implements IExpenseListService {

	private final ExpenseListRepository expenseListRepository;
	private final SecurityService securityService;
	private final ModelMapper modelMapper;
	
	public ExpenseListService(ExpenseListRepository expenseListRepository, SecurityService securityService, ModelMapper modelMapper) {
		super();
		this.expenseListRepository = expenseListRepository;
		this.securityService = securityService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public PaginatorDTO<ExpenseListDetailDTO> getAllUserExpenseList(int page, long size) {
		Pageable pageable = PageRequest.of(page, (int) size);
		
		UserEntity user = securityService.getAuthenticatedUser();

	    Page<ExpenseListEntity> expenseLists = expenseListRepository.findByUserId(user.getId(), pageable);
		List<ExpenseListDetailDTO> expenseListDTO = expenseLists.getContent().stream()
				.map(x -> modelMapper.map(x, ExpenseListDetailDTO.class))
	    	    .collect(Collectors.toList());
		    return new PaginatorDTO<>(expenseListDTO, expenseLists.getTotalPages(), expenseLists.getTotalElements());
	}

	@Override
	@Transactional
	public ExpenseListDTO save(ExpenseListCreateDTO expenseListCreateDTO) {
		UserEntity user = securityService.getAuthenticatedUser();
		
		IsExistingExpenseListName(expenseListCreateDTO.getName(), user.getId());
	    
	    ExpenseListEntity expenseListEntity = new ExpenseListEntity();
	    expenseListEntity.initUserExpenseList(expenseListCreateDTO.getName(), expenseListCreateDTO.getMonth(), expenseListCreateDTO.getBudget(), user);

	    ExpenseListEntity saved = expenseListRepository.save(expenseListEntity);

	    return modelMapper.map(saved, ExpenseListDTO.class);
	}

	@Override
	@Transactional
	public Boolean deleteUserExpenseListByIdAndUserId(Long id) {
		UserEntity user = securityService.getAuthenticatedUser();
		
	    Integer count = expenseListRepository.deleteByIdAndUserId(id, user.getId());
	    if(count == 0) {
	    	throw new ResourceNotFoundException(EXPENSE_LIST, ID, id);
	    }
	    log.info("UserExpenseList deleted: {}", id);
		
		return true;
	}

	@Override
	@Transactional
	public ExpenseListDTO updateUserExpenseListByIdAndUserId(ExpenseListDTO expenseListDTO) {
		UserEntity user = securityService.getAuthenticatedUser();
			    
	    ExpenseListEntity list = expenseListRepository.findByIdAndUserId(expenseListDTO.getId(), user.getId())
	    		.orElseThrow(() -> new ResourceNotFoundException(EXPENSE_LIST, ID, expenseListDTO.getId()));
	    
	    if (!list.getName().equalsIgnoreCase(expenseListDTO.getName())) {
	    	IsExistingExpenseListName(expenseListDTO.getName(), user.getId());
	    }
	    
	    list.updateExpenseList(expenseListDTO.getName(), expenseListDTO.getMonth(), expenseListDTO.getBudget());
	    
	    return modelMapper.map(expenseListRepository.save(list), ExpenseListDTO.class);
	}

	@Override
	public ExpenseListDetailDTO getExpenseListById(@NotNull Long id) {
		UserEntity user = securityService.getAuthenticatedUser();
		
		ExpenseListEntity expenseList = expenseListRepository.findByIdAndUserId(id, user.getId())
				.orElseThrow(() -> new ResourceNotFoundException(EXPENSE_LIST, ID, id));
		return modelMapper.map(expenseList, ExpenseListDetailDTO.class);
	}
	
	private void IsExistingExpenseListName(String name, Long userId) {
		 if(expenseListRepository.existsByNameIgnoreCaseAndUserId(name, userId)) {
			 throw new AlreadyExistsException("ExpenseList name must be Unique");
		 }
	}

//	@Override
//	public List<ExpenseListDTO> getRandomExpenseListForDemo() {
//		List<ExpenseListEntity> last30 = expenseListRepository.findTop30ByOrderByCreatedAtDesc();
//		Collections.shuffle(last30);
//		
//		return last30.stream().limit(3).map(x -> modelMapper.map(x, ExpenseListDTO.class)).collect(Collectors.toList());
//	}

}

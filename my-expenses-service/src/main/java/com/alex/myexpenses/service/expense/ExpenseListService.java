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

import com.alex.myexpenses.dto.expense.ExpenseListDTO;
import com.alex.myexpenses.dto.user.UserPrincipal;
import com.alex.myexpenses.entity.expenses.ExpenseEntity;
import com.alex.myexpenses.entity.expenses.ExpenseListEntity;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.interfaces.expense.IExpenseListService;
import com.alex.myexpenses.repository.expense.ExpenseListRepository;
import com.alex.myexpenses.repository.expense.ExpenseRepository;
import com.alex.myexpenses.repository.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseListService implements IExpenseListService{

	@Autowired
	private ExpenseListRepository expenseListRepository;
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<ExpenseListDTO> getAllUserExpenseList() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("Utente non autenticato");
	    }
		 
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername(); // ← la tua email
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

		    List<ExpenseListEntity> expenseLists = expenseListRepository.findByUserId(user.getId());
		
		    return expenseLists.stream()
		    	    .map(x -> modelMapper.map(x, ExpenseListDTO.class))
		    	    .collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ExpenseListDTO save(ExpenseListDTO expenseListDTO) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("Utente non autenticato");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername(); // ← la tua email
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
	    
	    ExpenseListEntity expenseListEntity = modelMapper.map(expenseListDTO, ExpenseListEntity.class);
	    expenseListEntity.setUser(user);
	    expenseListEntity.setTotalExpense(0.0); 

	    ExpenseListEntity saved = expenseListRepository.save(expenseListEntity);

	    return modelMapper.map(saved, ExpenseListDTO.class);
	}

	@Override
	@Transactional
	public Boolean deleteUserExpenseListByidAndUserId(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new RuntimeException("Utente non autenticato");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername(); // ← la tua email
	    UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
		
	    Integer count = expenseListRepository.deleteByIdAndUserId(id, user.getId());
	    if(count == 0) {
	    	throw new EntityNotFoundException("UserExpenseList not found in database or not valid id or userId ");
	    }
	    log.info("UserExpenseList deleted: {}", id);
		
		return true;
	}

}

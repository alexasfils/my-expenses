package com.alex.myexpenses.service.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.alex.myexpenses.controller.exception.ResourceNotFoundException;
import com.alex.myexpenses.dto.expense.CategoryDetailDTO;
import com.alex.myexpenses.dto.expense.ExpenseCreateDTO;
import com.alex.myexpenses.dto.expense.ExpenseDetailDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDetailDTO;
import com.alex.myexpenses.dto.expense.ExpenseSimpleDTO;
import com.alex.myexpenses.entity.expenses.CategoryEntity;
import com.alex.myexpenses.entity.expenses.ExpenseEntity;
import com.alex.myexpenses.entity.expenses.ExpenseListEntity;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.repository.expense.CategoryRepository;
import com.alex.myexpenses.repository.expense.ExpenseListRepository;
import com.alex.myexpenses.repository.expense.ExpenseRepository;
import com.alex.myexpenses.service.security.SecurityService;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {
	
	@Mock
	private ExpenseRepository expenseRepository;
	@Mock
	private ExpenseListRepository expenseListRepository;
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private SecurityService securityService;
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private ExpenseService expenseService;
	
	private UserEntity mockUser;
	
    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("alex@test.com");
    }
    
    @Test
    @DisplayName("Sould save expense into ExpenseList and set a catecory success")
    void saveExpense_Success() {
    	//Given  	
    	ExpenseCreateDTO inputExpenseDto = new ExpenseCreateDTO();
    	inputExpenseDto.setName("supermercato");
    	inputExpenseDto.setAmount(20.0);
    	inputExpenseDto.setCategoryName("Category");
    	inputExpenseDto.setExpenseListId(3L);
    	
    	//Entities
    	CategoryEntity categoryEntity = new CategoryEntity();
    	categoryEntity.setName("Category");
    	
    	ExpenseListEntity expenseListEntity = new ExpenseListEntity();
    	expenseListEntity.setId(3L);
    	expenseListEntity.setName("List");
    	expenseListEntity.setTotalExpense(00.0);
    	
    	ExpenseEntity expenseEntity = new ExpenseEntity();
    	expenseEntity.setName("supermercato");
    	expenseEntity.setAmount(20.0);
    	
    	ExpenseEntity savedExpense = new ExpenseEntity();
        savedExpense.setId(2L);
        savedExpense.setName("supermercato");
        savedExpense.setAmount(20.0);
        savedExpense.setExpenseList(expenseListEntity);
        
    	//OUTPUT DTO
        ExpenseDetailDTO expectedResponse = new ExpenseDetailDTO();
        expectedResponse.setId(2l);
        expectedResponse.setName("supermercato");
        expectedResponse.setAmount(20.0);
        expectedResponse.setExpenseListId(3L);

    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(categoryRepository.findByNameAndUserId(eq(inputExpenseDto.getCategoryName()), eq(mockUser.getId()))).thenReturn(Optional.of(categoryEntity));
    	when(expenseListRepository.findByIdAndUserId(3L, mockUser.getId())).thenReturn(Optional.of(expenseListEntity));
    	
    	when(modelMapper.map(any(ExpenseCreateDTO.class), eq(ExpenseEntity.class))).thenReturn(expenseEntity);
    	
    	when(expenseRepository.saveAndFlush(any(ExpenseEntity.class))).thenReturn(savedExpense);
    	when(expenseRepository.sumAmountsByExpenseListId(3L)).thenReturn(20.0);
    	//Used save two times, so i concatenated results in one when
        when(expenseListRepository.save(expenseListEntity)).thenReturn(expenseListEntity);
    	when(modelMapper.map(any(ExpenseEntity.class), eq(ExpenseDetailDTO.class))).thenReturn(expectedResponse);
    	
    	//WHEN
    	ExpenseDetailDTO result = expenseService.save(inputExpenseDto);
    
    	assertNotNull(result);
    	assertEquals(2L, result.getId());
    	assertEquals(20.0, expenseListEntity.getTotalExpense());

    	//VERIFY
    	verify(securityService).getAuthenticatedUser();
    	verify(categoryRepository).findByNameAndUserId(eq(inputExpenseDto.getCategoryName()), eq(mockUser.getId()));
    	verify(expenseListRepository).findByIdAndUserId(3L, mockUser.getId());
    	verify(modelMapper).map(any(ExpenseCreateDTO.class), eq(ExpenseEntity.class));
    	
    	InOrder inOrder = Mockito.inOrder(expenseListRepository, expenseRepository);
    	
    	inOrder.verify(expenseRepository).saveAndFlush(any(ExpenseEntity.class));
    	inOrder.verify(expenseRepository).sumAmountsByExpenseListId(3L);
    	inOrder.verify(expenseListRepository).save(expenseListEntity);
    	    	
    	verify(modelMapper).map(any(ExpenseEntity.class), eq(ExpenseDetailDTO.class));
    }
    
    @Test
    @DisplayName("Should launch an exception because of not present category ResourceNotFoundException")
    void saveExpense_CategoryNotFound_ResourceNotFoundException() {
    	CategoryDetailDTO categoryDto = new CategoryDetailDTO();
    	categoryDto.setId(1L);
    	categoryDto.setName("Fake");
    	
    	String categoryToSearach = categoryDto.getName();
    	
    	ExpenseCreateDTO inputExpenseDto = new ExpenseCreateDTO();
    	inputExpenseDto.setName("supermercato");
    	inputExpenseDto.setAmount(20.0);
    	inputExpenseDto.setCategoryName(categoryDto.getName());
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(categoryRepository.findByNameAndUserId(eq(categoryToSearach), eq(mockUser.getId()))).thenReturn(Optional.empty());
    	when(categoryRepository.findByNameAndIsDefaultTrue(categoryToSearach)).thenReturn(Optional.empty());
    	
    	ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
    		expenseService.save(inputExpenseDto);
    	});
    	
    	assertTrue(exception.getMessage().contains("Category"));
    	assertTrue(exception.getMessage().contains("name"));
    	assertTrue(exception.getMessage().contains(categoryToSearach));
    	
    	//Verify
    	verify(securityService).getAuthenticatedUser();
    	verify(categoryRepository).findByNameAndUserId(eq(categoryToSearach), eq(mockUser.getId()));
    	verify(categoryRepository).findByNameAndIsDefaultTrue(categoryToSearach);
    	verify(expenseListRepository, never()).findByIdAndUserId(any(), any());
    	verify(expenseListRepository, never()).save(any());
    	verify(expenseRepository, never()).saveAndFlush(any());
    	verify(expenseRepository, never()).sumAmountsByExpenseListId(any());
    	verify(modelMapper, never()).map(any(), any());
    }
    
    @Test
    @DisplayName("Should not call sumAmountByExpenseListId repository because the amount to update didn't changed ")
    void updateExpense_AmountNotChanged_ShouldNotUpdateTotal() {
    	ExpenseSimpleDTO inputDto = setExpenseSimpleDto();
    	
    	ExpenseEntity expense = new ExpenseEntity();
    	expense.setName("Ciro");
    	expense.setAmount(20.0);
    	
    	ExpenseDetailDTO responseDto = setExpenseDetaiDto();
    	    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseRepository.findByIdAndExpenseListUserId(3L, mockUser.getId())).thenReturn(Optional.of(expense));
    	doNothing().when(modelMapper).map(any(ExpenseSimpleDTO.class), eq(expense));
    	when(categoryRepository.findByNameAndUserId(eq("Category1"), eq(mockUser.getId()))).thenReturn(Optional.of(new CategoryEntity()));
    	when(expenseRepository.saveAndFlush(expense)).thenReturn(expense);
    	when(modelMapper.map(any(ExpenseEntity.class), eq(ExpenseDetailDTO.class))).thenReturn(responseDto);    	
    	//WHEN
    	ExpenseDetailDTO result = expenseService.updateExpense(inputDto);
    	
    	assertEquals(50.0, result.getAmount());
    	assertEquals("Pippo", result.getName());
    	assertEquals("Category1", result.getCategoryName());
    	    	
    	verify(expenseRepository, never()).sumAmountsByExpenseListId(any());
    	verify(expenseListRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should update totalExpemsies value")
    void updateExpense_WhenAmountChanges_ShouldRecalculateTotalExpenseAndSave() {
    	ExpenseSimpleDTO inputDto = setExpenseSimpleDto();
    	
    	ExpenseListEntity list = new ExpenseListEntity();
    	list.setId(1L);
    	list.setTotalExpense(20.0);
    	
    	ExpenseEntity expense = setExpenseEntity(list);
    	
    	ExpenseDetailDTO responseDto = setExpenseDetaiDto();
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseRepository.findByIdAndExpenseListUserId(3L, mockUser.getId())).thenReturn(Optional.of(expense));
    	//I take invoked mapper and set manually the values to see the real difference between them
    	doAnswer(invocation -> {
    		ExpenseSimpleDTO dto = invocation.getArgument(0);
    		ExpenseEntity entity = invocation.getArgument(1);
    		entity.setAmount(dto.getAmount());
    		return null;
    	}).when(modelMapper).map(any(ExpenseSimpleDTO.class), any(ExpenseEntity.class));
    	when(categoryRepository.findByNameAndIsDefaultTrue("Category1")).thenReturn(Optional.of(new CategoryEntity()));
    	when(expenseRepository.sumAmountsByExpenseListId(1L)).thenReturn(80.0);
    	when(expenseListRepository.save(list)).thenReturn(list);
    	when(expenseRepository.saveAndFlush(expense)).thenReturn(expense);
    	when(modelMapper.map(any(ExpenseEntity.class), eq(ExpenseDetailDTO.class))).thenReturn(responseDto);
    	
    	//WHEN
    	ExpenseDetailDTO result = expenseService.updateExpense(inputDto);
    	
    	assertEquals(50.0, result.getAmount());
    	assertEquals(50.0, expense.getAmount());
    	assertEquals(80.0, list.getTotalExpense());
    	
    	InOrder inOrder = Mockito.inOrder(modelMapper, expenseRepository, expenseListRepository);
    	
    	inOrder.verify(modelMapper).map(eq(inputDto), eq(expense));
    	inOrder.verify(expenseRepository, times(1)).sumAmountsByExpenseListId(any());
    	inOrder.verify(expenseListRepository, times(1)).save(list);
    	inOrder.verify(expenseRepository, times(1)).saveAndFlush(expense);
    }
    
    @Test
    @DisplayName("Should lounch ResourceNotFoundException Exception becouse of wrong id")
    void updateExpense_WhenIdDoesNotExist_SouldLounchResourceNotFound() {
    	ExpenseSimpleDTO inputDto = setExpenseSimpleDto();
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseRepository.findByIdAndExpenseListUserId(3L, mockUser.getId())).thenReturn(Optional.empty());
    	
    	//When
    	ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
    		expenseService.updateExpense(inputDto);
    	});
    	
    	assertTrue(exception.getMessage().contains("Expense"));
    	assertTrue(exception.getMessage().contains("id"));
    	assertTrue(exception.getMessage().contains(inputDto.getId().toString()));
    	
    	verify(expenseRepository, times(1)).findByIdAndExpenseListUserId(eq(3L), any());
    	verify(modelMapper, never()).map(any(), any());
    	verify(categoryRepository, never()).findByNameAndUserId(any(), any());
    	verify(categoryRepository, never()).findByNameAndIsDefaultTrue(any());
    	verify(categoryRepository, never()).findByNameAndUserId(any(), any());
    	verify(expenseRepository, never()).sumAmountsByExpenseListId(any());
    	verify(expenseListRepository, never()).save(any());
    	verify(expenseRepository, never()).saveAndFlush(any());
    }
    
    @Test
    @DisplayName("Sould delete expence sucessfully")
    void deleteExpenseByExpenseListId_ShoulddeleteExpenseAndUpdateExpenseListTotal() {
    	ExpenseListEntity list = new ExpenseListEntity();
    	list.setId(1L);
    	list.setTotalExpense(120.0);
    	
    	ExpenseEntity expense = setExpenseEntity(list);
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseRepository.deleteByExpenseListIdAndUserId(1L,  mockUser.getId())).thenReturn(1);
    	when(expenseRepository.sumAmountsByExpenseListId(1L)).thenReturn(100.0);
    	when(expenseListRepository.findById(1L)).thenReturn(Optional.of(list));
    	when(expenseListRepository.save(list)).thenReturn(list);
    	
    	Boolean result = expenseService.deleteExpenseByExpenseListAndUserId(1L);
    	
    	assertTrue(result);
    	assertEquals(100.0, list.getTotalExpense());
    	
    	InOrder inOrder = Mockito.inOrder(expenseRepository, expenseListRepository);

    	verify(securityService).getAuthenticatedUser();
    	inOrder.verify(expenseRepository).deleteByExpenseListIdAndUserId(eq(1L),  eq(mockUser.getId()));
    	inOrder.verify(expenseRepository).sumAmountsByExpenseListId(eq(1L));
    	inOrder.verify(expenseListRepository).findById(eq(1L));
    	inOrder.verify(expenseListRepository).save(list);
    }
    
    @Test
    @DisplayName("Sould launch ResourceNotFoundException because of wrong Id")
    void deleteExpenseByExpenseListId_WhenWrongPassId_ShouldLaunchResourceNotFoundException() {
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseRepository.deleteByExpenseListIdAndUserId(1L,  mockUser.getId())).thenReturn(0);
    	
    	ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->{
    		expenseService.deleteExpenseByExpenseListAndUserId(1L);
    	});
    	
    	assertTrue(exception.getMessage().contains("Expense"));
    	assertTrue(exception.getMessage().contains("id"));
    	assertTrue(exception.getMessage().contains("1"));
    	
    	verify(securityService).getAuthenticatedUser();
    	verify(expenseRepository).deleteByExpenseListIdAndUserId(eq(1L),  eq(mockUser.getId()));
    	verify(expenseRepository, never()).sumAmountsByExpenseListId(any());
    	verify(expenseListRepository, never()).findById(any());
    	verify(expenseListRepository, never()).save(any());
    }
    
    private ExpenseSimpleDTO setExpenseSimpleDto() {
    	ExpenseSimpleDTO inputDto = new ExpenseSimpleDTO();
    	inputDto.setId(3L);
    	inputDto.setName("Pippo");
    	inputDto.setCategoryName("Category1");
    	inputDto.setAmount(50.0);
    	return inputDto;
    }
    
    private ExpenseDetailDTO setExpenseDetaiDto() {
    	ExpenseDetailDTO responseDto = new ExpenseDetailDTO();
    	responseDto.setId(3L);
    	responseDto.setName("Pippo");
    	responseDto.setCategoryName("Category1");
    	responseDto.setAmount(50.0);
    	return responseDto;
    }
    
    private ExpenseEntity setExpenseEntity(ExpenseListEntity list) {
    	ExpenseEntity expense = new ExpenseEntity();
    	expense.setId(3L);
    	expense.setName("Ciro");
    	expense.setAmount(20.0);
    	expense.setExpenseList(list);
    	return expense;
    }
    
}

package com.alex.myexpenses.service.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alex.myexpenses.controller.exception.ResourceNotFoundException;
import com.alex.myexpenses.dto.expense.ExpenseListCreateDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDTO;
import com.alex.myexpenses.dto.expense.ExpenseListDetailDTO;
import com.alex.myexpenses.entity.expenses.ExpenseEntity;
import com.alex.myexpenses.entity.expenses.ExpenseListEntity;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.repository.expense.ExpenseListRepository;
import com.alex.myexpenses.service.security.SecurityService;
import com.alex.myexpenses.utility.PaginatorDTO;


@ExtendWith(MockitoExtension.class)
public class ExpenseListServiceTest {
	
	@Mock
	private ExpenseListRepository expenseListRepository;
	@Mock
	private SecurityService securityService;
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private ExpenseListService expenseListService;
	
	private UserEntity mockUser;
	
    @BeforeEach
    void setUp() {
        // Prepariamo un utente finto che verrà usato in quasi tutti i test
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("alex@test.com");
    }
    
    @Test
    @DisplayName("Should return paginated ExpenseList")
    void getAllPaginatedUserExpenseList_Success() {
    	//Given
    	ExpenseListEntity firstList = new ExpenseListEntity();
    	firstList.setId(1L);
    	firstList.setUser(mockUser);
    	firstList.setBudget(100.0);
    	firstList.setTotalExpense(23.20);
    	
    	ExpenseListEntity secondList = new ExpenseListEntity();
    	secondList.setId(2L);
    	secondList.setUser(mockUser);
    	secondList.setBudget(102.0);
    	secondList.setTotalExpense(24.20);

    	Pageable pageable = PageRequest.of(0, 10);
    	List<ExpenseListEntity> list = List.of(firstList, secondList);
    	Page<ExpenseListEntity> page = new PageImpl<>(list, pageable, 2);
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseListRepository.findByUserId(eq(mockUser.getId()), any(Pageable.class))).thenReturn(page);
    	when(modelMapper.map(any(ExpenseListEntity.class), eq(ExpenseListDetailDTO.class))).thenReturn(new ExpenseListDetailDTO());
    	
    	PaginatorDTO<ExpenseListDetailDTO> result = expenseListService.getAllUserExpenseList(0, 10);
    	
    	assertNotNull(result);
    	assertEquals(2, result.getContent().size());
    	
    	verify(modelMapper, times(2)).map(any(ExpenseListEntity.class), eq(ExpenseListDetailDTO.class));
    	verify(expenseListRepository).findByUserId(eq(mockUser.getId()),any(Pageable.class));
    	verify(securityService).getAuthenticatedUser();
    }
    
    @Test
    @DisplayName("Should save Succes")
    void save_CheckIfUerCanSetTotalExpense_Success() {
    	ExpenseListCreateDTO inputDto = new ExpenseListCreateDTO();
    	inputDto.setMonth(1);
    	inputDto.setBudget(100.0);
    	inputDto.setName("List");
    	
    	ExpenseListDTO responceDto = new ExpenseListDTO();
    	responceDto.setId(1L);
    	responceDto.setMonth(1);
    	responceDto.setBudget(100.0);
    	responceDto.setName("List");
    	
    	ExpenseListEntity dirtyEntity = new ExpenseListEntity();
    	dirtyEntity.setId(1L);
    	dirtyEntity.setMonth(1);
    	dirtyEntity.setUser(mockUser);
    	dirtyEntity.setBudget(100.0);
    	dirtyEntity.setName("List");
    	dirtyEntity.setTotalExpense(23.20);
    	dirtyEntity.setExpenses(new ArrayList<ExpenseEntity>());
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(modelMapper.map(any(ExpenseListCreateDTO.class), eq(ExpenseListEntity.class))).thenReturn(dirtyEntity);
    	when(expenseListRepository.save(any(ExpenseListEntity.class))).thenReturn(dirtyEntity);
    	when(modelMapper.map(any(ExpenseListEntity.class), eq(ExpenseListDTO.class))).thenReturn(responceDto);
    	
    	//Preper capture 
    	ArgumentCaptor<ExpenseListEntity> captor = ArgumentCaptor.forClass(ExpenseListEntity.class);
    	
    	//WHEN
    	expenseListService.save(inputDto);
    	
    	//THEN
    	verify(expenseListRepository).save(captor.capture());
    	ExpenseListEntity savedEntity = captor.getValue();
    	assertEquals(savedEntity.getTotalExpense(),  0.00, "The totatl must be 0.00");
    	assertNull(savedEntity.getId(), "L'ID deve essere forzato a null prima del salvataggio");
    }
    
    @Test
    @DisplayName("Update ExpenseList Success")
    void updateUserExpenseListByIdAndUserId_Success() {
    	ExpenseListDTO inputDto = new ExpenseListDTO();
    	inputDto.setId(1L);
    	inputDto.setMonth(2);
    	inputDto.setBudget(105.0);
    	inputDto.setName("New List");
    	
    	ExpenseListEntity oldEntity = new ExpenseListEntity();
    	oldEntity.setId(1L);
    	oldEntity.setMonth(1);
    	oldEntity.setUser(mockUser);
    	oldEntity.setBudget(100.0);
    	oldEntity.setName("List");
    	oldEntity.setExpenses(new ArrayList<ExpenseEntity>());
    	
    	ExpenseListEntity newEntity = new ExpenseListEntity();
    	newEntity.setId(1L);
    	newEntity.setMonth(2);
    	newEntity.setUser(mockUser);
    	newEntity.setBudget(105.0);
    	newEntity.setName("New List");
    	newEntity.setExpenses(new ArrayList<ExpenseEntity>());
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseListRepository.findByIdAndUserId(eq(inputDto.getId()), eq(mockUser.getId()))).thenReturn(Optional.of(oldEntity));
    	doNothing().when(modelMapper).map(any(ExpenseListDTO.class), any(ExpenseListEntity.class));
    	when(expenseListRepository.save(any(ExpenseListEntity.class))).thenReturn(newEntity);
    	when(modelMapper.map(any(ExpenseListEntity.class), eq(ExpenseListDTO.class))).thenReturn(inputDto);
    	
    	ExpenseListDTO result = expenseListService.updateUserExpenseListByIdAndUserId(inputDto);
    	
    	assertNotNull(result);
    	assertEquals(inputDto.getName(), result.getName());
    	assertEquals(inputDto.getBudget(), result.getBudget());
    	assertEquals(inputDto.getMonth(), result.getMonth());
    	
    	InOrder inOrder = Mockito.inOrder(modelMapper, expenseListRepository);
    	
    	//Check if everithing is done in order
    	inOrder.verify(modelMapper).map(any(ExpenseListDTO.class), any(ExpenseListEntity.class));
    	inOrder.verify(expenseListRepository).save(any(ExpenseListEntity.class));
    	inOrder.verify(modelMapper).map(any(ExpenseListEntity.class), eq(ExpenseListDTO.class));
    	verify(securityService).getAuthenticatedUser();
    	
    	//check if there are no more other calls
    	verifyNoMoreInteractions(expenseListRepository, securityService, modelMapper);
    }
    
    @Test
    @DisplayName("Should delete ExpenseList sucessfully")
    void deleteExpenseListByIdAndUserId_sucess() {
    	Long idToDelete = 1L;
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseListRepository.deleteByIdAndUserId(idToDelete, mockUser.getId())).thenReturn(1);
    	
    	Boolean result = expenseListService.deleteUserExpenseListByIdAndUserId(idToDelete);
    	
    	assertTrue(result);
    	
    	verify(securityService).getAuthenticatedUser();
    	verify(expenseListRepository).deleteByIdAndUserId(eq(idToDelete),eq(mockUser.getId()));
    }
    
    @Test
    @DisplayName("Should launch ResourceNotFoundException")
    void deleteExpenseListByIdAndUserId_NotFoudId_ResourceNotFoundException() {
    	Long idToDelete = 1L;
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseListRepository.deleteByIdAndUserId(idToDelete, mockUser.getId())).thenReturn(0);
    	
    	ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
    		expenseListService.deleteUserExpenseListByIdAndUserId(idToDelete);
    	});
    	
    	assertTrue(ex.getMessage().contains("ExpenseList"));
    	assertTrue(ex.getMessage().contains("id"));
    	assertTrue(ex.getMessage().contains(idToDelete.toString()));
    	
    	verify(securityService).getAuthenticatedUser();
    	verify(expenseListRepository).deleteByIdAndUserId(eq(idToDelete),eq(mockUser.getId()));
    	verifyNoMoreInteractions(securityService, expenseListRepository);
    }
    
    @Test
    @DisplayName("Sould return ExpenseList success")
    void getExpenseListById_success() {
    	Long givenId = 1L;
    	ExpenseListDetailDTO responseDto = new ExpenseListDetailDTO();
    	responseDto.setId(givenId);
    	responseDto.setName("Lista nuova");
    	
    	ExpenseListEntity entityList = new ExpenseListEntity();
    	entityList.setId(givenId);
    	entityList.setUser(mockUser);
    	entityList.setName("Lista nuova");
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseListRepository.findByIdAndUserId(givenId, mockUser.getId())).thenReturn(Optional.of(entityList));
    	when(modelMapper.map(any(ExpenseListEntity.class), eq(ExpenseListDetailDTO.class))).thenReturn(responseDto);
    	
    	// 1. create a captor
    	ArgumentCaptor<ExpenseListEntity> entityCaptor = ArgumentCaptor.forClass(ExpenseListEntity.class);
    	
    	ExpenseListDetailDTO result = expenseListService.getExpenseListById(givenId);
    	
    	//Check if what i pass to modelMapper is wat i've got from entity
    	verify(modelMapper).map(entityCaptor.capture(), eq(ExpenseListDetailDTO.class));
    	
    	assertNotNull(result);
    	
    	// Check if the value are really same
    	ExpenseListEntity capturedEntity = entityCaptor.getValue();
    	assertEquals(givenId, capturedEntity.getId());
    	assertEquals("Lista nuova", capturedEntity.getName());
    	
    	verify(securityService).getAuthenticatedUser();
    	verify(expenseListRepository, times(1)).findByIdAndUserId(givenId, mockUser.getId());
    }
    
    @Test
    @DisplayName("Sould launch ResourceNotFoundException")
    void getExpenseListById_ResourceNotFoundException() {
    	Long givenId = 1L;
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(expenseListRepository.findByIdAndUserId(givenId, mockUser.getId())).thenReturn(Optional.empty());
    	
    	assertThrows(ResourceNotFoundException.class, () -> {
    		expenseListService.getExpenseListById(givenId);
    	});
    	
    	verify(securityService).getAuthenticatedUser();
    	verify(expenseListRepository).findByIdAndUserId(givenId, mockUser.getId());
    	verifyNoMoreInteractions(securityService, expenseListRepository, modelMapper);
    }

}

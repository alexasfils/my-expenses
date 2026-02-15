package com.alex.myexpenses.service.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

import com.alex.myexpenses.controller.exception.AlreadyExistsException;
import com.alex.myexpenses.controller.exception.ResourceNotFoundException;
import com.alex.myexpenses.core.exception.NotAllowedOperationException;
import com.alex.myexpenses.dto.expense.CategoryDTO;
import com.alex.myexpenses.dto.expense.CategoryDetailDTO;
import com.alex.myexpenses.entity.expenses.CategoryEntity;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.repository.expense.CategoryRepository;
import com.alex.myexpenses.repository.expense.ExpenseRepository;
import com.alex.myexpenses.service.security.SecurityService;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private ExpenseRepository expenseRepository;
	@Mock
	private SecurityService securityService;
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks // Inietta i mock sopra dentro il Service
    private CategoryService categoryService;

    private UserEntity mockUser;
    
    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("alex@test.com");
    }
    
    @Test
    @DisplayName("Should save a category successfully")
    void saveCategory_Success() {
    	//Given
    	CategoryDTO inputDTO = new CategoryDTO();
    	inputDTO.setName("Food");
    	
    	CategoryEntity entityAfterSave = new CategoryEntity();
    	entityAfterSave.setId(10L);
    	entityAfterSave.setName("Food");
    	entityAfterSave.setUser(mockUser);
    	
    	// Definiamo il comportamento dei Mock (Stubbing)
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(modelMapper.map(any(CategoryDTO.class), eq(CategoryEntity.class))).thenReturn(entityAfterSave);
    	when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(entityAfterSave);
    	when(modelMapper.map(any(CategoryEntity.class), eq(CategoryDTO.class))).thenReturn(new CategoryDTO());
    	
    	// WHEN
    	CategoryDTO result = categoryService.save(inputDTO);
    	
    	//THEN
    	assertNotNull(result);
    	verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    	verify(securityService).getAuthenticatedUser();
    }
    
    @Test
    @DisplayName("Should force isDefault to false even if input entity was true")
    void saveCategory_ForceIsDefaultFalse() {
    	//Given
    	CategoryDTO inputDTO = new CategoryDTO();
    	inputDTO.setName("Travel");
    	
    	CategoryEntity entityWithTrue = new CategoryEntity();
        entityWithTrue.setIsDefault(true);
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(modelMapper.map(any(CategoryDTO.class), eq(CategoryEntity.class))).thenReturn(entityWithTrue);
    	when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(entityWithTrue);
    	
    	// ArgumentCaptor per "intercettare" l'entità prima del salvataggio
        ArgumentCaptor<CategoryEntity> captor = ArgumentCaptor.forClass(CategoryEntity.class);
        
        // WHEN
        categoryService.save(inputDTO);
    	
    	//THEN
        verify(categoryRepository).save(captor.capture());
        CategoryEntity savedEntity = captor.getValue();
        
     // Verifichiamo che il service abbia fatto il suo dovere di "pulizia"
        assertFalse(savedEntity.getIsDefault(), "The service must force isDefault to false");
    }
    
    @Test
    @DisplayName("Shold fail because the name is null or empty")
    void saveCategory_MissingName_ThrowsException() {
    	//Given
    	CategoryDTO inputDTO = new CategoryDTO();
    	inputDTO.setName("  ");

        // Verifichiamo che il service propaghi l'eccezione
        assertThrows(IllegalArgumentException.class, () -> {
        	categoryService.save(inputDTO); 
        	});
        
        verify(securityService, never()).getAuthenticatedUser();
        //check if the flow is stopped while save and didn't go after
        verify(categoryRepository, never()).existsByNameIgnoreCaseAndUserId(any(), any());
        verify(categoryRepository, never()).save(any());
        verify(modelMapper, never()).map(any(CategoryEntity.class), eq(CategoryDTO.class));
    }
    
    @Test
    @DisplayName("Shold fail because the name is already exists")
    void saveCategory_ExistingName_AlreadyExistsException() {
    	//Given
    	CategoryDTO inputDTO = new CategoryDTO();
    	inputDTO.setName("Pippo");
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(categoryRepository.existsByNameIgnoreCaseAndUserId(inputDTO.getName(), mockUser.getId())).thenReturn(true);
    	
        // Verifichiamo che il service propaghi l'eccezione
        assertThrows(AlreadyExistsException.class, () -> {
        	categoryService.save(inputDTO); 
        	});
        
        verify(securityService).getAuthenticatedUser();
        //check if the flow is stopped while save and didn't go after
        verify(categoryRepository, never()).save(any());
        verify(modelMapper, never()).map(any(CategoryEntity.class), eq(CategoryDTO.class));
    }
    
    @Test
    @DisplayName("Should update a category succesfully")
    void updateCategory_Success() {
    	//Given DTO
    	CategoryDTO inputDTO = new CategoryDTO();
    	inputDTO.setName("Fish");
    	inputDTO.setId(10L);
    	
    	//Response DTO the one that i expect at the and of the process
    	CategoryDTO responseDTO = new CategoryDTO();
    	responseDTO.setName("Fish");
    	responseDTO.setId(10L);

    	//Given Entity
    	CategoryEntity oldEntitySave = new CategoryEntity();
    	oldEntitySave.setId(10L);
    	oldEntitySave.setName("Food");
    	oldEntitySave.setUser(mockUser);
    	oldEntitySave.setIsDefault(false);
    	
    	//New updated Entity
    	CategoryEntity updatedEntity = new CategoryEntity();
    	updatedEntity.setId(10L);
    	updatedEntity.setName("Fish");
    	updatedEntity.setUser(mockUser);
    	updatedEntity.setIsDefault(false);
    	
    	// Define how mocks will act Mock (Stubbing)
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	//First of all i check if i have that category in a db(old version)
    	//In thenReturn i put saved version(old version of entity(like saved into db))
    	when(categoryRepository.findByIdAndUserId(responseDTO.getId(), mockUser.getId())).thenReturn(Optional.of(oldEntitySave));
    	
    	//The service recive a DTO as an input and and convert it into entity to pass to the db, so we will have a mocked(updated) entity 
    	//Here i use doNothing just because this mapping just convert the entity
    	doNothing().when(modelMapper).map(any(CategoryDTO.class), any(CategoryEntity.class));    	
    	//So i try to save the updated entity into db. So as a result i expect the updated entity.
    	when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(updatedEntity);
    	
    	//Here i need to convert updated Entity into a dto to pas to the controller. So i convert back it to a DTO.
    	when(modelMapper.map(any(CategoryEntity.class), eq(CategoryDTO.class))).thenReturn(responseDTO);
    	
    	// WHEN (Esecuzione)
    	CategoryDTO result = categoryService.updateUserCategoryByIdAndUserId(inputDTO);
    	
    	//THEN
    	assertNotNull(result);
    	assertEquals("Fish", result.getName());
    	assertEquals(10L, result.getId());
    	verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    	verify(securityService).getAuthenticatedUser();
    	//Check if there is no other calls to methods, servicer or repositories.
    	verifyNoMoreInteractions(categoryRepository, securityService, modelMapper);
    }
    
    @Test
    @DisplayName("Sould launch NotAlloudedException to not give a possibility to update not allowed category")
    void updateUserCategory_WithNotAlloudedUserId_NotAlloudedException() {
    	CategoryDTO dto = new CategoryDTO();
    	dto.setId(10L);
    	dto.setName("Flowers");
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(categoryRepository.findByIdAndUserId(dto.getId(), mockUser.getId())).thenReturn(Optional.empty());
    	
    	assertThrows(ResourceNotFoundException.class, ( )-> {
    		categoryService.updateUserCategoryByIdAndUserId(dto);
    	});
    	
    	verify(securityService).getAuthenticatedUser();
        verify(categoryRepository).findByIdAndUserId(any(), any());
    	
        //check if the flow is stopped while save and didn't go after
        verify(modelMapper, never()).map(any(CategoryEntity.class), eq(CategoryDTO.class));
        verify(categoryRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should launch NotAllowedOperationException because user can not update default categories")
    void update_DefaultCategories_NotAllowedOperationException() {
    	CategoryDTO dto = new CategoryDTO();
    	dto.setId(10L);
    	dto.setName("Flowers");
    	
    	CategoryEntity entity = new CategoryEntity();
    	entity.setId(10L);
    	entity.setName("Food");
    	entity.setUser(mockUser);
    	entity.setIsDefault(true);
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(categoryRepository.findByIdAndUserId(dto.getId(), mockUser.getId())).thenReturn(Optional.of(entity));
    	
    	assertThrows(NotAllowedOperationException.class, ( )-> {
    		categoryService.updateUserCategoryByIdAndUserId(dto);
    	});
    	
    	verify(securityService).getAuthenticatedUser();
        verify(categoryRepository).findByIdAndUserId(any(), any());
        
      //check if the flow is stopped while save and didn't go after
        verify(modelMapper, never()).map(any(CategoryEntity.class), eq(CategoryDTO.class));
        verify(categoryRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should migrate expenses and delete category successfully")
    void deleteAndMigrateCategory_Succes() {
    	//Given
    	Long idToDelete = 10L;
    	Long userId = mockUser.getId();
    	
    	//Given Entity
    	CategoryEntity categoryEntityToDelete = new CategoryEntity();
    	categoryEntityToDelete.setId(idToDelete);
    	categoryEntityToDelete.setName("Custom Food");
    	categoryEntityToDelete.setUser(mockUser);
    	categoryEntityToDelete.setIsDefault(false);
    	
    	//New updated Entity
    	CategoryEntity defaultCategoryEntity = new CategoryEntity();
    	defaultCategoryEntity.setId(11L);
    	defaultCategoryEntity.setName("Others");
    	defaultCategoryEntity.setUser(mockUser);
    	defaultCategoryEntity.setIsDefault(true);
    	
    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	
    	//Find category to delete
    	when(categoryRepository.findByIdAndUserId(eq(idToDelete), eq(userId))).thenReturn(Optional.of(categoryEntityToDelete));
    	//Find default category "Others"
    	when(categoryRepository.findByNameAndIsDefaultTrue(eq("Others"))).thenReturn(Optional.of(defaultCategoryEntity));
    	    	
    	// WHEN
    	Boolean result = categoryService.deleteCategoryAndMigrateExpenses(idToDelete);
    	//THEN
    	assertTrue(result);
    	
    	//Verify if migration was don with correct data
    	verify(expenseRepository).migrateExpenses(eq(categoryEntityToDelete), eq(defaultCategoryEntity), eq(userId));
    	verify(securityService).getAuthenticatedUser();
    	verify(categoryRepository).delete(categoryEntityToDelete);
    	
    	// Verifichiamo l'ordine: prima migrazione, poi cancellazione (Critico per i vincoli del DB!)
        InOrder inOrder = Mockito.inOrder(expenseRepository, categoryRepository);
        inOrder.verify(expenseRepository).migrateExpenses(any(), any(), any());
        inOrder.verify(categoryRepository).delete(any());
    }
    
    @Test
    @DisplayName("Should find all categories default and users successfully")
    void getAllCategories_Succes(){    	
    	CategoryEntity foodCategory = new CategoryEntity();
    	foodCategory.setId(10L);
    	foodCategory.setName("Food");
    	foodCategory.setUser(mockUser);
    	
    	CategoryEntity fishCategory = new CategoryEntity();
    	fishCategory.setId(11L);
    	fishCategory.setName("Fish");
    	fishCategory.setUser(mockUser);
    	
    	CategoryEntity newCategory = new CategoryEntity();
    	newCategory.setId(12L);
    	newCategory.setName("New");
    	newCategory.setUser(mockUser);
    	newCategory.setIsDefault(true);
    	
    	List<CategoryEntity> categories = List.of(foodCategory, fishCategory, newCategory);

    	when(securityService.getAuthenticatedUser()).thenReturn(mockUser);
    	when(categoryRepository.findDefaultAndUserCategories(eq(mockUser.getId()))).thenReturn(categories);
    	when(modelMapper.map(any(CategoryEntity.class), eq(CategoryDetailDTO.class))).thenReturn(new CategoryDetailDTO());

    	//WHEN
    	List<CategoryDetailDTO> result = categoryService.getAllCategories();
    	
    	assertNotNull(result);
    	assertEquals(3, result.size());
    	
    	verify(modelMapper, times(3)).map(any(), eq(CategoryDetailDTO.class));
    	verify(categoryRepository).findDefaultAndUserCategories(mockUser.getId());
    	verify(securityService).getAuthenticatedUser();
    }
    
}

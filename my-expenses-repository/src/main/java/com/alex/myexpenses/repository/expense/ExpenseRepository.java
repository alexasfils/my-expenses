package com.alex.myexpenses.repository.expense;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alex.myexpenses.entity.expenses.CategoryEntity;
import com.alex.myexpenses.entity.expenses.ExpenseEntity;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>{

	List<ExpenseEntity> findByExpenseListId(Long expenseListId);
	
	Page<ExpenseEntity> findByExpenseListIdAndExpenseListUserId(Long expenseListId, Long userId, Pageable pageable);
	
	Optional<ExpenseEntity> findByIdAndExpenseListUserId(Long expenseId, Long userId);
	
	@Modifying
	@Query("DELETE FROM ExpenseEntity e WHERE e.expenseList.id = :expenseListId AND e.expenseList.user.id = :userId")
	Integer deleteByExpenseListIdAndUserId(@Param("expenseListId") Long expenseListId, @Param("userId") Long userId);
	
	 @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e WHERE e.expenseList.id = :expenseListId")
	 Double sumAmountsByExpenseListId(@Param("expenseListId") Long expenseListId);
	 
	 @Modifying(clearAutomatically = true)
	 @Query("UPDATE ExpenseEntity e SET e.category = :defaultCat " +
	        "WHERE e.category = :oldCat AND e.user.id = :userId")
	 void migrateExpenses(@Param("oldCat") CategoryEntity oldCat, 
	                     @Param("defaultCat") CategoryEntity defaultCat, 
	                     @Param("userId") Long userId);

}

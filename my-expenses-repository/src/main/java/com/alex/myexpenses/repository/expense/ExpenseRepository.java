package com.alex.myexpenses.repository.expense;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alex.myexpenses.entity.expenses.ExpenseEntity;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>{

	List<ExpenseEntity> findByExpenseListId(Long expenseListId);
	
	List<ExpenseEntity> findByExpenseListIdAndExpenseListUserId(Long expenseListId, Long userId);
	
	Optional<ExpenseEntity> findByIdAndExpenseListUserId(Long expenseId, Long userId);
	
	@Modifying
	@Query("DELETE FROM ExpenseEntity e WHERE e.expenseList.id = :expenseListId AND e.expenseList.user.id = :userId")
	Integer deleteByExpenseListIdAndUserId(@Param("expenseListId") Long expenseListId, @Param("userId") Long userId);

}

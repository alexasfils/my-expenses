package com.alex.myexpenses.repository.expense;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.alex.myexpenses.entity.expenses.ExpenseListEntity;

public interface ExpenseListRepository extends JpaRepository<ExpenseListEntity, Long>{

	List<ExpenseListEntity> findByUserId(Long userId);
	
	@Modifying
	Integer deleteByIdAndUserId(Long listId, Long userId);
	
	@Modifying
	Optional<ExpenseListEntity> findByIdAndUserId(Long listId, Long userId);
}

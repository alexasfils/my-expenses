package com.alex.myexpenses.repository.expense;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.alex.myexpenses.entity.expenses.ExpenseListEntity;

@Repository
public interface ExpenseListRepository extends JpaRepository<ExpenseListEntity, Long>{

	List<ExpenseListEntity> findByUserId(Long userId);
	
	@Modifying
	Integer deleteByIdAndUserId(Long listId, Long userId);
	
	Optional<ExpenseListEntity> findByIdAndUserId(Long listId, Long userId);
	
//	List<ExpenseListEntity> findTop30ByOrderByCreatedAtDesc();
}

package com.alex.myexpenses.repository.expense;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.alex.myexpenses.entity.expenses.ExpenseListEntity;

@Repository
public interface ExpenseListRepository extends JpaRepository<ExpenseListEntity, Long>{

	Page<ExpenseListEntity> findByUserId(Long userId, Pageable pageable);
	
	@Modifying
	Integer deleteByIdAndUserId(Long listId, Long userId);
	
	Optional<ExpenseListEntity> findByIdAndUserId(Long listId, Long userId);
	
     Optional<ExpenseListEntity> findById(Long listId);
     
     boolean existsByNameIgnoreCaseAndUserId(String name, Long userId);
	
//	List<ExpenseListEntity> findTop30ByOrderByCreatedAtDesc();
}

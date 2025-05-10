package com.alex.myexpenses.repository.expense;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.alex.myexpenses.entity.expenses.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	List<CategoryEntity> findByIsDefaultTrueAndUserId(Long userId);
	
	@Modifying
	Integer deleteByIdAndUserId(Long listId, Long userId);
	
	Optional<CategoryEntity> findByIdAndUserId(Long listId, Long userId);
}

package com.alex.myexpenses.repository.expense;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alex.myexpenses.entity.expenses.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	@Query("SELECT c FROM CategoryEntity c WHERE c.isDefault = true OR c.user.id = :userId")
	List<CategoryEntity> findDefaultAndUserCategories(@Param("userId") Long userId);
	
	@Modifying
	Integer deleteByIdAndUserId(Long listId, Long userId);
	
	Optional<CategoryEntity> findByIdAndUserId(Long categoryId, Long userId);
	
	Optional<CategoryEntity> findByNameAndUserId(String categoryName, Long userId);
	
	Optional<CategoryEntity> findByNameAndIsDefaultTrue(String categoryName);
	
	Boolean existsByNameIgnoreCaseAndUserId(String categoryName, Long userId);
	
}

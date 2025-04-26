package com.alex.myexpenses.repository.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alex.myexpenses.entity.expenses.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}

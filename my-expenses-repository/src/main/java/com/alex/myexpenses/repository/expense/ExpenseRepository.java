package com.alex.myexpenses.repository.expense;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alex.myexpenses.entity.expenses.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>{

//	List<ExpenseEntity> findByExpenseListId(Long expenseListID);
}

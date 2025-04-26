package com.alex.myexpenses.repository.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alex.myexpenses.entity.expenses.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>{

}

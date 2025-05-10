package com.alex.myexpenses.entity.expenses;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
public class ExpenseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6626933298806944820L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "expense_date", nullable = false)
	private LocalDate expenseDate;
	
	@Column(name = "amount", length = 50, nullable = false)
	private Double amount;
	
	@Column(name = "description", length = 500)
	private String description;
	
	@ManyToOne
    @JoinColumn(name = "id_expense_list")
	private ExpenseListEntity expenseList;
	
	@ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
	private CategoryEntity category;

}

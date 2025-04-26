package com.alex.myexpenses.dto.expense;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
	
	private Long id;
	@NotNull(message = "Name cannot be null")
	private String name;
	@NotNull(message = "ExpenseDate informations cannot be null")
	private LocalDate expenseDate;
	@Positive(message = "Amount must be positive")
	private Double amount;
	private String description;
	private Long expenseListId;
	@NotNull(message = "Category cannot be null")
	private CategoryDTO category;

}

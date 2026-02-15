package com.alex.myexpenses.dto.expense;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCreateDTO {
	
	@NotBlank(message = "Name is Mandadory")
	 @Size(min = 2, max = 100, message = "Name must be between 2 and 50 characters")
	private String name;
	
	@NotNull(message = "ExpenseDate informations cannot be null")
	@PastOrPresent(message = "Date cannot be in the future")
	private LocalDate expenseDate;
	
	@NotNull(message = "Amount is mandadory")
    @Positive(message = "Amount must be positive")
	private Double amount;
	
	@Size(min = 2, max = 500, message = "Description must be between 2 and 50 characters")
	private String description;
	
	@NotNull(message = "ExpenseList Id is mandadory")
	private Long expenseListId;
	
	@NotNull(message = "Category cannot be null")
	private String categoryName;

}

package com.alex.myexpenses.dto.expense;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
	
	private Long id;
	@NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 100)
	private String name;
	@NotNull(message = "ExpenseDate informations cannot be null")
	private LocalDate expenseDate;
	@NotNull(message = "L'importo è obbligatorio")
    @Positive(message = "L'importo deve essere positivo")
	private Double amount;
	@Size(max = 500)
	private String description;
	private Long expenseListId;
	@NotNull(message = "Category cannot be null")
	private Long categoryId;

}

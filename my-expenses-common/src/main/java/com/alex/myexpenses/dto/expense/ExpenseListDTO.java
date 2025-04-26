package com.alex.myexpenses.dto.expense;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseListDTO {
	
	private Long id;
	@NotNull(message = "Name cannot be null")
	private String name;
	@NotNull(message = "Month informations cannot be null")
	private Integer month;
	@Positive(message = "Budget must be positive")
	private Double budget;
	@NotNull(message = "TotalExpense cannot be null")
	private Double totalExpense;
//	@NotNull(message = "User Id cannot be null")
//	private Long userId;
	
	private List<ExpenseDTO> expense;

}

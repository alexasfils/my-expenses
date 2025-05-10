package com.alex.myexpenses.dto.expense;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseListDTO {

	private Long id;
	@NotBlank(message = "Il nome è obbligatorio")
	@Size(max = 100, message = "Il nome non può superare 100 caratteri")
	private String name;
	@NotNull(message = "Il mese è obbligatorio")
	@Min(1)
	@Max(12)
	private Integer month;
	@NotNull(message = "Il budget è obbligatorio")
	@PositiveOrZero(message = "Il budget non può essere negativo")
	private Double budget;
	@NotNull(message = "TotalExpense cannot be null")
	private Double totalExpense;

	private List<ExpenseDTO> expenses;

}

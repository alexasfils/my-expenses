package com.alex.myexpenses.dto.expense;


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
		@NotBlank(message = "Name is mandatory value")
		@Size(max = 100, message = "Name cannot be mor then 100 characters")
		private String name;
		@NotNull(message = "Month is mandatory value")
		@Min(1)
		@Max(12)
		private Integer month;
		@NotNull(message = "Budget is mandatory value")
		@PositiveOrZero(message = "Budget cannot be negative value")
		private Double budget;

}

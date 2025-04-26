package com.alex.myexpenses.dto.expense;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
	
	private Long id;
	@NotNull(message = "Name informations cannot be null")
	private String name;
	
	private String color;
	@NotNull(message = "Isdefault informations cannot be null")
	private String isdefault;
	private Long userId;
	
	private List<ExpenseDTO> expence;

}

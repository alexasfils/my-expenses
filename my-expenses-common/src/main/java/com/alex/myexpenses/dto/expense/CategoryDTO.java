package com.alex.myexpenses.dto.expense;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
	
	@NotNull(message = "Id is required for update")
	private Long id;
	
	@NotNull(message = "Name informations cannot be null")
	@NotBlank(message = "Name cannot be empty") // Meglio di NotNull per le stringhe
	private String name;
	
	private String color;
}

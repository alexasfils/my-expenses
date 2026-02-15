package com.alex.myexpenses.dto.expense;


import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailDTO {
	
	private Long id;
	@NotNull(message = "Name informations cannot be null")
	private String name;
	
	private String color;
	@NotNull(message = "Isdefault informations cannot be null")
	private Boolean isdefault = false;

}

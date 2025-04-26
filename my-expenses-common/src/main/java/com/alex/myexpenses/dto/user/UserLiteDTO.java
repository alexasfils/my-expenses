package com.alex.myexpenses.dto.user;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLiteDTO {
	
	private Long id;
	@NotNull(message = "Name informations cannot be null")
	private String name;
	@NotNull(message = "Surname informations cannot be null")
	private String surname;
	@NotNull(message = "Email informations cannot be null")
	private String email;
	@NotNull(message = "Phone informations cannot be null")
	private String phone;
	@NotNull(message = "Currency informations cannot be null")
	private String currency;

}

package com.alex.myexpenses.dto.user;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLiteDTO {
	
	private Long id;
	@NotNull(message = "NAme informations cannot be null")
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

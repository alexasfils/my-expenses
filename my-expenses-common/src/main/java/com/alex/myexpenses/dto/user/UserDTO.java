package com.alex.myexpenses.dto.user;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO extends UserLiteDTO{
	
	@NotNull(message = "Password informations cannot be null")
	private String password;
	@NotNull(message = "Creation Date informations cannot be null")
	private LocalDate creation_date;

}

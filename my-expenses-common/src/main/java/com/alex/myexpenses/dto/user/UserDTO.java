package com.alex.myexpenses.dto.user;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
	
	private Long id;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String phone;
	private String currency;
	private LocalDate creation_date;

}

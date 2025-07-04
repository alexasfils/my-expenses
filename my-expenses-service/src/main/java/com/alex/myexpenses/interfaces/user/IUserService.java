package com.alex.myexpenses.interfaces.user;

import com.alex.myexpenses.dto.user.LoginResponseDTO;
import com.alex.myexpenses.dto.user.UserDTO;

public interface IUserService {

	public UserDTO addUser(UserDTO user);
	public LoginResponseDTO login(String email, String rawPassword);
}

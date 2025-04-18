package com.alex.myexpenses.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.myexpenses.dto.user.UserDTO;
import com.alex.myexpenses.interfaces.user.IUserService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<UserDTO> register(@RequestBody() UserDTO user){
		UserDTO newUser = userService.addUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}

}

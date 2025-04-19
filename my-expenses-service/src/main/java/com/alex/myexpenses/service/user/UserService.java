package com.alex.myexpenses.service.user;

import java.time.LocalDate;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.dto.user.JwtTokenUtil;
import com.alex.myexpenses.dto.user.LoginResponseDTO;
import com.alex.myexpenses.dto.user.UserDTO;
import com.alex.myexpenses.dto.user.UserPrincipal;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.interfaces.user.IUserService;
import com.alex.myexpenses.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public LoginResponseDTO login(String email, String rawPassword) {
		
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
				"USer not found for email and password: " + email));

		if(!passwordEncoder.matches(rawPassword, userEntity.getPassword())) {
			 throw new BadCredentialsException("Invalid password");
		}
		System.out.println("Password e questa: " + userEntity.getPassword());
		
		UserDetails userDetails = new UserPrincipal(userEntity);
	    String token = jwtTokenUtil.generateToken(userDetails);

	    return new LoginResponseDTO(token, userEntity.getEmail());
	}

	@Override
	public UserDTO addUser(UserDTO user) {
		UserEntity userEntity = new UserEntity();
		userEntity.setName(user.getName());
		userEntity.setSurname(user.getSurname());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
		userEntity.setPhone(user.getPhone());
		userEntity.setCurrency(user.getCurrency());
		userEntity.setCreation_date(LocalDate.now());
		
		System.out.println("USER DTO e questo: " + user);
		System.out.println("USER Entity e questo: " + userEntity);
		
		return modelMapper.map(userRepository.save(userEntity), UserDTO.class);
	}
	
	public UserDTO updateUser(UserDTO user) {
		UserEntity userEntity = userRepository.findById(user.getId()).orElse(null);
		userEntity.setName(user.getName());
		userEntity.setSurname(user.getSurname());
		userEntity.setPhone(user.getPhone());
		userEntity.setCurrency(user.getCurrency());
		return modelMapper.map(userRepository.save(userEntity), UserDTO.class);
	}
	
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}

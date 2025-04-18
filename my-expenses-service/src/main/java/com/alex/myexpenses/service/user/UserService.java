package com.alex.myexpenses.service.user;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.dto.user.UserDTO;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.interfaces.user.IUserService;
import com.alex.myexpenses.repository.user.UserRepository;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserDTO addUser(UserDTO user) {
		UserEntity userEntity = new UserEntity();
		userEntity.setName(user.getName());
		userEntity.setSurname(user.getSurname());
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

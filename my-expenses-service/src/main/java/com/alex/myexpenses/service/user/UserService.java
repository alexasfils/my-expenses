package com.alex.myexpenses.service.user;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.dto.user.JwtTokenUtil;
import com.alex.myexpenses.dto.user.LoginResponseDTO;
import com.alex.myexpenses.dto.user.UserDTO;
import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.interfaces.user.IUserService;
import com.alex.myexpenses.repository.user.UserRepository;
import com.alex.myexpenses.service.security.UserPrincipal;

import static com.alex.myexpenses.utility.AppConstants.ExceptionMessage.*;


@Service
public class UserService implements IUserService {
		
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtTokenUtil jwtTokenUtil;
	
	public UserService(ModelMapper modelMapper, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
			JwtTokenUtil jwtTokenUtil) {
		super();
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	public LoginResponseDTO login(String email, String password) {
		
		UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException(INVALID_CREDENTIALS));

		if(!passwordEncoder.matches(password, userEntity.getPassword())) {
			 throw new BadCredentialsException(INVALID_CREDENTIALS);
		}
		
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

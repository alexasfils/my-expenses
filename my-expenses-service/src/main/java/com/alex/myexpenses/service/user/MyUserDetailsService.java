package com.alex.myexpenses.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.repository.user.UserRepository;
import com.alex.myexpenses.service.security.UserPrincipal;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public MyUserDetailsService (UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		if(user == null) {
			throw new UsernameNotFoundException("This user does not exist in the database");
		}
		return new UserPrincipal(user);
	}

}

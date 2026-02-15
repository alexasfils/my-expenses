package com.alex.myexpenses.service.security;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.alex.myexpenses.entity.user.UserEntity;
import com.alex.myexpenses.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityService {
	
	private final UserRepository userRepository;
	
	public UserEntity getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
	        throw new InsufficientAuthenticationException("user is not authenticated or the session is not valid");
	    }
	    String email = ((UserPrincipal) auth.getPrincipal()).getUsername();
	    return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found whith the email: " + email));
	}

}

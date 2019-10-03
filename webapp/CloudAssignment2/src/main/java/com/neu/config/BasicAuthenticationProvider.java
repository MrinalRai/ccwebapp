package com.neu.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.neu.service.UserService;

@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {

	public BasicAuthenticationProvider() {
		super();
	}

	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoderConfig passwordEncoderConfig;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
		final String username = authentication.getName();
		final String password = authentication.getCredentials().toString();
		com.neu.model.User user = new com.neu.model.User();
		user = userService.getUserByEmail(username);
		if (null != user && username.equals(user.getEmail()) && passwordEncoderConfig.customPasswordEncoder().matches(password, user.getPassword())) {
			final List<GrantedAuthority> grantedAuths = new ArrayList<>();
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
			final UserDetails userDetails = new User(username, password, grantedAuths);
			final Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, password, grantedAuths);
			return auth;
		} else {
			return usernamePasswordAuthenticationToken;
		}
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}

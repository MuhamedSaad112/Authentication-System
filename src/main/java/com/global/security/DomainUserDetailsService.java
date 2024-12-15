package com.global.security;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.global.entity.User;
import com.global.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Authenticate a user from the database.
 */

@Component("userDetailsService")
@RequiredArgsConstructor
@Log4j2
public class DomainUserDetailsService implements UserDetailsService {

	private final UserRepository repository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String login) {
		log.debug("Authenticating {}", login);
		if (new EmailValidator().isValid(login, null)) {
			return repository.findOneWithRoleByEmailIgnoreCase(login).map(user -> createSpringSecurityUser(login, user))
					.orElseThrow(() -> new UsernameNotFoundException(
							"User With Email " + login + "was not found in the dataBase"));
		}

		String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
		return repository.findOneWithRoleByLogin(lowercaseLogin)
				.map(user -> createSpringSecurityUser(lowercaseLogin, user))
				.orElseThrow(() -> new UsernameNotFoundException(
						"User " + lowercaseLogin + "was not found in the dataBase"));
	}

	private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin,
			User user) {

		if (!user.isActivated()) {
			throw new UserNotActivatedException("User" + lowercaseLogin + "was not actived");
		}

		List<GrantedAuthority> roles = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), roles);

	}

}
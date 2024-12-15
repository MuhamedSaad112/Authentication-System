package com.global.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * View Model object for storing a user's credentials.
 */

@Getter
@Setter
public class LoginVM {

	@NotNull
	@Size(min = 1, max = 60)
	private String username;

	@NotNull
	@Size(min = 10, max = 100)
	private String password;

	private boolean rememberMe;

}

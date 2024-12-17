package com.global.config;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Validation {

	public static final String LOGIN_PATTERN = "^[a-zA-Z0-9]{5,20}$";
	public static final String SYSTEM = "system";
	public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,100}$";
	public static final String DEFAULT_LANGUAGE = "en";


}

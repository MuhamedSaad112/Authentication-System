package com.global.config;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Validation {

//	public static final String LOGIN_PATTERN = "^[a-zA-Z0-9]{6,20}$";
//	public static final String SYSTEM = "system";
//	public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,100}$";
//	public static final String DEFAULT_LANGUAGE = "en";


	public static final String LOGIN_PATTERN = "^[a-zA-Z0-9]{6,20}$";
	public static final String SYSTEM = "system";
	public static final String PASSWORD_PATTERN = "^[A-Za-z\\d]{6,20}$"; // minimum 6 characters, letters and numbers only
	public static final String DEFAULT_LANGUAGE = "en";
}

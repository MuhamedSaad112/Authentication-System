package com.global.dto;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ManagedUserVM extends AdminUserDTO {
	public static final int PASSWORD_MIN_LENGTH = 10;

	public static final int PASSWORD_MAX_LENGTH = 100;
	
	
	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password ;

}

package com.global.dto;

import com.global.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

	private Long id;

	private String login;

	public UserDto(User user) {
		super();
		this.id = user.getId();
		this.login = user.getLogin();
	}

}

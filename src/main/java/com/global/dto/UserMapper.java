package com.global.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import com.global.entity.Role;
import com.global.entity.User;

@Service
public class UserMapper {

	// Converts a list of User entities to a list of UserDto objects.
	public List<UserDto> usersToUserDtos(List<User> users) {
		return users.stream().filter(Objects::nonNull).map(this::userToUserDto).collect(Collectors.toList());
	}

	// Converts a User entity to a UserDto object.
	public UserDto userToUserDto(User user) {
		return new UserDto(user);
	}

	// Converts a list of User entities to a list of AdminUserDTO objects.
	public List<AdminUserDTO> usersToAdminUserDTOs(List<User> users) {
		return users.stream().filter(Objects::nonNull).map(this::userToAdminUserDTO).collect(Collectors.toList());
	}

	// Converts a User entity to an AdminUserDTO object.
	public AdminUserDTO userToAdminUserDTO(User user) {
		return new AdminUserDTO(user);
	}

	// Converts a list of AdminUserDTO objects to a list of User entities.
	public List<User> userDTOsToUsers(List<AdminUserDTO> userDtos) {
		return userDtos.stream().filter(Objects::nonNull).map(this::userDtoToUser).collect(Collectors.toList());
	}

	// Converts an AdminUserDTO object to a User entity.
	public User userDtoToUser(AdminUserDTO userDto) {
		if (userDto == null) {
			return null;
		} else {
			User user = new User();
			user.setId(userDto.getId());
			user.setLogin(userDto.getLogin());
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setLangKey(userDto.getLangKey());
			user.setEmail(userDto.getEmail());
			user.setActivated(userDto.isActivated());
			user.setImageUrl(userDto.getImageUrl());
			Set<Role> roles = this.rolesFromStrings(userDto.getRoles());
			user.setRoles(roles);
			return user;
		}
	}

	// Converts a Set of role strings to a Set of Role entities.
	private Set<Role> rolesFromStrings(Set<String> rolesString) {
		Set<Role> roles = new HashSet<>();
		if (rolesString != null) {
			roles = rolesString.stream().map(string -> {
				Role role = new Role();
				role.setName(string);
				return role;
			}).collect(Collectors.toSet());
		}
		return roles;
	}

	//////////////////

	// Creates a User entity with only an ID.
	public User userFromId(Long id) {
		if (id == null) {
			return null;
		}
		User user = new User();
		user.setId(id);
		return user;
	}

	//////////////////

	// Converts a User entity to a UserDto with only the ID.
	@Named("id")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	public UserDto toDtoId(User user) {
		if (user == null) {
			return null;
		}
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		return userDto;
	}

	// Converts a list of User entities to a Set of UserDto objects with only the
	// ID.
	@Named("idSet")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	public Set<UserDto> toDtoIdSet(List<User> users) {
		if (users == null) {
			return Collections.emptySet();
		}
		Set<UserDto> userSet = new HashSet<>();
		for (User UserEntity : users) {
			userSet.add(this.toDtoId(UserEntity));
		}
		return userSet;
	}

	//////////////////

	// Converts a User entity to a UserDto with the ID and login.
	@Named("login")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	@Mapping(target = "login", source = "login")
	public UserDto toDtoLogin(User user) {
		if (user == null) {
			return null;
		}
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setLogin(user.getLogin());
		return userDto;
	}

	// Converts a list of User entities to a Set of UserDto objects with the ID and
	// login.
	@Named("loginSet")
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	@Mapping(target = "login", source = "login")
	public Set<UserDto> toDtoLoginSet(List<User> users) {
		if (users == null) {
			return Collections.emptySet();
		}
		Set<UserDto> userSet = new HashSet<>();
		for (User UserEntity : users) {
			userSet.add(this.toDtoLogin(UserEntity));
		}
		return userSet;
	}
}

package com.global.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import com.global.config.Validation;
import com.global.entity.Role;
import com.global.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO representing a user, with his roles.
 */

@NoArgsConstructor
@Getter
@Setter
public class AdminUserDTO implements Serializable {

	private Long id;

	@NotBlank
	@Pattern(regexp = Validation.LOGIN_PATTERN)
	@Size(min = 1, max = 60)
	private String login;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 10, max = 254)
	private String email;

	@Size(max = 255)
	private String imageUrl;

	private String createdBy;

	private Instant createdDate;

	private String langKey;

	private String lastModifiedBy;

	private Instant lastModifiedDate;

	private boolean activated = false;

	private Set<String> roles;

	public AdminUserDTO(User user) {
		super();
		this.id = user.getId();
		this.login = user.getLogin();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.langKey = user.getLangKey();
		this.email = user.getEmail();
		this.activated = user.isActivated();
		this.imageUrl = user.getImageUrl();
		this.createdBy = user.getCreatedBy();
		this.createdDate = user.getCreatedDate();
		this.lastModifiedBy = user.getLastModifiedBy();
		this.lastModifiedDate = user.getLastModifiedDate();
		this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
	}

}

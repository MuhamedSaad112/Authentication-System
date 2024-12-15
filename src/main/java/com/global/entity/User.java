package com.global.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.global.config.Validation;

import jakarta.annotation.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sec_user")
public class User extends AbstractAuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sec_user_id")
	private Long id;

	@Column(name = "sec_user_login", unique = true, length = 60)
	@NotBlank
	//@Pattern(regexp = Validation.LOGIN_PATTERN)
	@Size(min = 1, max = 60)
	private String login;

	@Email
	@Column(name = "sec_user_email", length = 254, unique = true)
	@Size(min = 10, max = 254)
	private String email;

	@Column(name = "sec_user_password_hash", length = 100, nullable = false)
	@Nonnull
	//@Pattern(regexp = Validation.PASSWORD_PATTERN, message = "Password must meet the security requirements.")
	@JsonIgnore
	@Size(min = 10, max = 100)
	private String password;

	@Column(name = "first_name", length = 50)
	@Size(max = 50)
	@NotEmpty
	private String firstName;

	@Column(name = "last_name", length = 50)
	@NotEmpty
	@Size(max = 50)
	private String lastName;

	@Size(min = 2, max = 10)
	@Column(name = "lang_key", length = 10)
	private String langKey;

	@Column(name = "image_url", length = 256)
	@Size(max = 256)
	private String imageUrl;

	@NotNull
	@Column(nullable = false)
	private boolean activated = false;

	@Column(name = "activation_key", length = 50)
	@Size(max = 50)
	@JsonIgnore
	private String activationKey;

	@Column(name = "reset_key", length = 50)
	@Size(max = 50)
	private String resetKey;

	@Column(name = "reset_date")
	private Instant resetDate = null;

	// Relationship

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "sec_user_role", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "sec_user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id", referencedColumnName = "name") })
	@BatchSize(size = 20)
	private Set<Role> roles = new HashSet<>();

	// Lowercase the login before saving it in database

	public void setLogin(String login) {
		this.login = login.toLowerCase();
	}

}

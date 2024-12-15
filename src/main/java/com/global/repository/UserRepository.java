package com.global.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.global.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public static final String USERS_BY_LOGIN_CACHE = "usersByLogin";
	public static final String USERS_BY_EMAIL_CACHE = "usersByEmail";

	Optional<User> findOneByActivationKey(String activationKey);

	List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

	Optional<User> findOneByResetKey(String resetKey);

	Optional<User> findOneByEmailIgnoreCase(String email);

	Optional<User> findOneByLogin(String login);

	@EntityGraph(attributePaths = "roles")
	Optional<User> findOneWithRoleByEmailIgnoreCase(String email);

	@EntityGraph(attributePaths = "roles")
	Optional<User> findOneWithRoleByLogin(String login);

	Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

}

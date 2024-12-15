package com.global.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.global.dto.AdminUserDTO;
import com.global.dto.UserDto;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.global.config.Validation;
import com.global.entity.Role;
import com.global.entity.User;
import com.global.errors.EmailAlreadyUsedException;
import com.global.errors.InvalidPasswordException;
import com.global.errors.LoginAlreadyUsedException;
import com.global.repository.RoleRepository;
import com.global.repository.UserRepository;
import com.global.security.RolesConstants;
import com.global.security.SecurityUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Service class for managing users.
 */

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final RoleRepository roleRepository;

	private final CacheManager cacheManager;

	// Account Controller
	public Optional<User> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);

		return userRepository.findOneByActivationKey(key).map(user -> {
			user.setActivated(true);
			user.setActivationKey(null);
			this.clearUserCaches(user);
			log.debug("Activated User :{}", user);
			return user;
		});
	}

	// Account Controller
	public Optional<User> completePasswordReset(String newPassword, String key) {

		log.debug("Reset user password for reset key {}", key);

		return userRepository.findOneByResetKey(key)
				.filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS))).map(user -> {
					user.setPassword(passwordEncoder.encode(newPassword));
					user.setResetDate(null);
					user.setResetKey(null);
					this.clearUserCaches(user);
					return user;

				});

	}

	// Account Controller
	public Optional<User> requestPasswordReset(String mail) {
		log.debug("Searching for email: {}", mail);

		return userRepository.findOneByEmailIgnoreCase(mail).filter(User::isActivated).map(user -> {
			log.debug("User found and activated: {}", user.getEmail());
			user.setResetKey(RandomUtil.generateResetKey().substring(0, 50));
			user.setResetDate(Instant.now());
			this.clearUserCaches(user);
			return user;

		});
	}

	// Account Controller
	public User registerUser(AdminUserDTO userDTO, String password) {
		userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {

			boolean removed = removeNonActivatedUser(existingUser);
			if (!removed) {
				throw new LoginAlreadyUsedException();
			}

		});

		userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {

			boolean removed = removeNonActivatedUser(existingUser);
			if (!removed) {
				throw new EmailAlreadyUsedException();
			}

		});

		User newUser = new User();

		String encryptedPassword = passwordEncoder.encode(password);

		newUser.setLogin(userDTO.getLogin().toLowerCase());

		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(userDTO.getFirstName());
		newUser.setLastName(userDTO.getLastName());
		if (userDTO.getEmail() != null) {
			newUser.setEmail(userDTO.getEmail().toLowerCase());
		}

		if (userDTO.getLangKey() == null) {
			newUser.setLangKey(Validation.DEFAULT_LANGUAGE);
		} else {

			newUser.setLangKey(userDTO.getLangKey());
		}

		newUser.setImageUrl(userDTO.getImageUrl());
		newUser.setActivated(false);
		newUser.setActivationKey(RandomUtil.generateActivationKey().substring(0, 50));

		Set<Role> roles = new HashSet<>();

		roleRepository.findById(RolesConstants.USER).ifPresent(roles::add);
		newUser.setRoles(roles);
		userRepository.save(newUser);
		this.clearUserCaches(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;

	}

	private Boolean removeNonActivatedUser(User existingUser) {
		if (existingUser.isActivated()) {
			return false;
		}
		userRepository.delete(existingUser);
		userRepository.flush();
		this.clearUserCaches(existingUser);
		return true;
	}

	// Admin Controller
	public User createUser(AdminUserDTO userDTO) {

		User user = new User();

		user.setLogin(userDTO.getLogin().toLowerCase());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setImageUrl(userDTO.getImageUrl());
		if (userDTO.getEmail() != null) {
			user.setEmail(userDTO.getEmail());
		}

		if (userDTO.getLangKey() == null) {
			user.setLangKey(Validation.DEFAULT_LANGUAGE);// default language
		} else {
			user.setLangKey(userDTO.getLangKey());
		}
		String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
		user.setPassword(encryptedPassword);
		user.setResetKey(RandomUtil.generateResetKey().substring(0, 50));
		user.setResetDate(Instant.now());
		user.setActivated(true);

		if (userDTO.getRoles() != null) {
			Set<Role> roles = userDTO.getRoles().stream().map(roleRepository::findById).filter(Optional::isPresent)
					.map(Optional::get).collect(Collectors.toSet());
			user.setRoles(roles);
		}

		userRepository.save(user);
		this.clearUserCaches(user);
		log.debug("Created Information for User: {}", user);
		return user;
	}

	// Admin Controller
	public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {

		return Optional.of(userRepository.findById(userDTO.getId())).filter(Optional::isPresent).map(Optional::get)
				.map(user -> {

					user.setLogin(userDTO.getLogin());
					user.setFirstName(userDTO.getFirstName());
					user.setLastName(userDTO.getLastName());
					if (userDTO.getEmail() != null) {
						user.setEmail(userDTO.getEmail());
					}
					user.setImageUrl(userDTO.getImageUrl());
					user.setActivated(userDTO.isActivated());
					user.setLangKey(userDTO.getLangKey());
					Set<Role> roles = user.getRoles();

					userDTO.getRoles().stream().map(roleRepository::findById).filter(Optional::isEmpty)
							.map(Optional::get).forEach(roles::add);
					this.clearUserCaches(user);
					log.debug("Changed Information for User: {}", user);
					return user;
				}).map(AdminUserDTO::new);

	}

	// Admin Controller
	public void deleteUser(String login) {

		userRepository.findOneByLogin(login).ifPresent(user -> {
			userRepository.delete(user);
			this.clearUserCaches(user);
			log.debug("Delete   User: {}", user);
		});
	}

	// Account Controller
	public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {

		SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {

			user.setFirstName(firstName);
			user.setLastName(lastName);
			if (email != null) {
				user.setEmail(email);
			}

			user.setLangKey(langKey);
			user.setImageUrl(imageUrl);
			this.clearUserCaches(user);
			log.debug("Changed Information for User: {}", user);
		});

	}

	// Account Controller
	public void changePassword(String currentClearTextPassword, String newPassword) {

		SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {

			String currentEncryptedPassword = user.getPassword();

			if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
				throw new InvalidPasswordException();
			}

			String EncryptedPassword = passwordEncoder.encode(newPassword);
			user.setPassword(EncryptedPassword);
			this.clearUserCaches(user);
			log.debug("Changed password for User: {}", user);

		});

	}

	// Admin Controller
	@Cacheable(value = "users", key = "'all-users'")
	@Transactional(readOnly = true)
	public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
		return userRepository.findAll(pageable).map(AdminUserDTO::new);
	}

	@Transactional(readOnly = true)
	public Page<UserDto> getAllPublicUsers(Pageable pageable) {
		return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDto::new);
	}

	// Admin Controller
	@Transactional(readOnly = true)
	public Optional<User> getAllWithRolesByLogin(String login) {
		return userRepository.findOneWithRoleByLogin(login);
	}

	// Account Controller
	@Transactional(readOnly = true)
	public Optional<User> getUserWithRoles() {
		return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithRoleByLogin);
	}

	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(
				Instant.now().minus(30, ChronoUnit.DAYS)).forEach(user -> {
					log.debug("Deleting not activated user {}", user.getLogin());
					userRepository.delete(user);
					this.clearUserCaches(user);
				});

	}

	@Transactional(readOnly = true)
	public List<String> getRoles() {
		return roleRepository.findAll().stream().map(Role::getName).collect(Collectors.toList());
	}

	public void clearUserCaches(User user) {
		Objects.requireNonNull(cacheManager.getCache(userRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
		if (user.getEmail() != null) {
			Objects.requireNonNull(cacheManager.getCache(userRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
		}
	}

}

/**
 * Utility class for generating random values.
 */
class RandomUtil {

	private static final SecureRandom RANDOM = new SecureRandom();
	private static final int DEFAULT_LENGTH = 50;

	public static String generateRandomKey(int length) {
		byte[] randomBytes = new byte[length];
		RANDOM.nextBytes(randomBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
	}

	public static String generateResetKey() {
		return generateRandomKey(DEFAULT_LENGTH);
	}

	public static String generateActivationKey() {
		return generateRandomKey(DEFAULT_LENGTH);
	}

	public static String generatePassword() {
		return generateRandomKey(DEFAULT_LENGTH);
	}
}

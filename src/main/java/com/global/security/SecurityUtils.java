package com.global.security;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NoArgsConstructor;

/**
 * Utility class for Spring Security.
 * 
 * This class provides various static utility methods to interact with the
 * Spring Security context, such as retrieving the current user's details,
 * checking their roles, and verifying their authentication status.
 */

@NoArgsConstructor
public final class SecurityUtils {

	/**
	 * Extracts the principal (username) from the given Authentication object.
	 *
	 * @param authentication the Authentication object.
	 * @return the username of the principal if it exists; null otherwise.
	 */
	private static String extractPrincipal(Authentication authentication) {
		if (authentication == null) {
			return null; // No authentication object available
		} else if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getUsername(); // Return username if the principal is UserDetails
		} else if (authentication.getPrincipal() instanceof String) {
			return (String) authentication.getPrincipal(); // Return principal directly if it's a string
		}
		return null; // Unknown principal type
	}

	/**
	 * Retrieves the login (username) of the currently authenticated user.
	 *
	 * @return an Optional containing the current user's login, or empty if not
	 *         available.
	 */
	public static Optional<String> getCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
	}

	/**
	 * Retrieves the JWT token of the currently authenticated user.
	 *
	 * @return an Optional containing the JWT token, or empty if not available.
	 */
	public static Optional<String> getCurrentJWT() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(securityContext.getAuthentication())
				.filter(authenticathion -> authenticathion.getCredentials() instanceof String)
				.map(authenticathion -> (String) authenticathion.getCredentials());
	}

	/**
	 * Extracts the roles (authorities) of the authenticated user from the
	 * Authentication object.
	 *
	 * @param authentication the Authentication object.
	 * @return a Stream of roles assigned to the user.
	 */
	private static Stream<String> getRoles(Authentication authentication) {
		return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
	}

	/**
	 * Determines if the current user is authenticated.
	 *
	 * @return true if the user is authenticated and not anonymous; false otherwise.
	 */
	public static boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && getRoles(authentication).noneMatch(RolesConstants.ANONYMOUS::equals);
	}

	/**
	 * Checks if the current user has at least one of the specified roles.
	 *
	 * @param roles the roles to check.
	 * @return true if the user has any of the roles; false otherwise.
	 */
	public static boolean hasCurrentUserAnyOfRoles(String... roles) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (authentication != null
				&& getRoles(authentication).anyMatch(role -> Arrays.asList(roles).contains(role)));
	}

	/**
	 * Checks if the current user has none of the specified roles.
	 *
	 * @param roles the roles to check.
	 * @return true if the user has none of the roles; false otherwise.
	 */
	public static boolean hasCurrentUserNoneOfRoles(String... roles) {
		return !hasCurrentUserAnyOfRoles(roles); // Negates the result of hasCurrentUserAnyOfRoles
	}

	/**
	 * Checks if the current user has a specific role.
	 *
	 * @param role the role to check.
	 * @return true if the user has the specified role; false otherwise.
	 */
	public static boolean hasCurrentUserThisRoles(String role) {
		return hasCurrentUserAnyOfRoles(role); // Delegates to hasCurrentUserAnyOfRoles for single role
	}
}

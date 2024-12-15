package com.global.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.global.entity.Role;
import com.global.entity.User;
import com.global.repository.RoleRepository;
import com.global.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if the database is empty
        if (userRepository.count() == 0) {
            // Create 'ROLE_ADMIN' role if it doesn't exist
            Role adminRole = roleRepository.findById("ROLE_ADMIN").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("ROLE_ADMIN");
                roleRepository.save(newRole);
                return newRole;
            });

            // Create 'ROLE_USER' role if it doesn't exist
            Role userRole = roleRepository.findById("ROLE_USER").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("ROLE_USER");
                roleRepository.save(newRole);
                return newRole;
            });

            // Create 2 admin users and assign the 'ROLE_ADMIN' role
            User admin1 = createUser("admin1", "1a41c06ae2@emailvb.pro", "Admin1", " ", "MySecure@Password1", adminRole);
            User admin2 = createUser("admin2", "admin2@example.com", "Admin2", " ", "MySecure@Password1", adminRole);

            // Create 2 regular users and assign the 'ROLE_USER' role
            User user1 = createUser("user1", "e6eb8b9fab@emailvb.pro", "User1", " ", "MySecure@Password1", userRole);
            User user2 = createUser("user2", "user2@example.com", "User2", " ", "MySecure@Password1", userRole);

            // Save all created users into the database
            userRepository.save(admin1);
            userRepository.save(admin2);
            userRepository.save(user1);
            userRepository.save(user2);

            // Print a success message
            System.out.println("Admin and user accounts created successfully");
        }
    }

    // Helper method to create a user and assign a role to them
    private User createUser(String login, String email, String firstName, String lastName, String password, Role role) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password)); // Encode the password
        user.setActivated(true); // Mark user as activated
        user.setResetKey("resetKey"); // Set a dummy reset key
        user.getRoles().add(role); // Assign the provided role to the user
        return user;
    }
}

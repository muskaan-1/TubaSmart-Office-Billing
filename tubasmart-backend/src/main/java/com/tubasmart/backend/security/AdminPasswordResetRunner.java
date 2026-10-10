
package com.tubasmart.backend.security;

import com.tubasmart.backend.model.UserAccount;
import com.tubasmart.backend.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminPasswordResetRunner implements CommandLineRunner {

    private final UserAccountRepository users;
    private final PasswordEncoder encoder;

    public AdminPasswordResetRunner(
            UserAccountRepository users,
            PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        UserAccount user = users.findByUsernameIgnoreCase("admin")
                .orElseThrow(() ->
                        new IllegalStateException("Admin account not found"));

        user.setPasswordHash(encoder.encode("TubaAdmin@2026"));
        user.setActive(true);

        users.save(user);

        System.out.println("Admin password reset successfully.");
    }
}

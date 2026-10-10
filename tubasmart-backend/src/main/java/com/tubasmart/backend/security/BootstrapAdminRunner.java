package com.tubasmart.backend.security;

import com.tubasmart.backend.model.Role;
import com.tubasmart.backend.model.UserAccount;
import com.tubasmart.backend.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Creates the first ADMIN user from BOOTSTRAP_ADMIN_USERNAME / BOOTSTRAP_ADMIN_PASSWORD,
 * only when the app_users table is empty. Replaces the old hard-coded admin/admin123 login.
 */
@Component
public class BootstrapAdminRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(BootstrapAdminRunner.class);
    private final UserAccountRepository users;
    private final PasswordEncoder encoder;
    private final String username;
    private final String password;

    public BootstrapAdminRunner(UserAccountRepository users, PasswordEncoder encoder,
                                @Value("${app.bootstrap-admin.username:}") String username,
                                @Value("${app.bootstrap-admin.password:}") String password) {
        this.users = users; this.encoder = encoder; this.username = username; this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (users.count() > 0) return;
        if (username.isBlank() || password.length() < 8) {
            log.warn("No users exist. Set BOOTSTRAP_ADMIN_USERNAME and BOOTSTRAP_ADMIN_PASSWORD (min 8 chars) to create the first admin.");
            return;
        }
        UserAccount u = new UserAccount();
        u.setUsername(username.trim());
        u.setPasswordHash(encoder.encode(password));
        u.setRole(Role.ADMIN);
        u.setActive(true);
        users.save(u);
        log.info("Bootstrap admin '{}' created.", u.getUsername());
    }
}

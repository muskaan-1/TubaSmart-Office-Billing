package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.Role;
import com.tubasmart.backend.model.UserAccount;
import com.tubasmart.backend.repository.UserAccountRepository;
import com.tubasmart.backend.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwt; private final UserAccountRepository users; private final PasswordEncoder encoder;
    public AuthController(JwtService jwt, UserAccountRepository users, PasswordEncoder encoder){this.jwt=jwt;this.users=users;this.encoder=encoder;}
    public record LoginRequest(@NotBlank String username,@NotBlank String password){}
    public record LoginResponse(String token,String role){}
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest r){
        UserAccount u=users.findByUsernameIgnoreCase(r.username().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials"));
        if(!u.isActive() || !encoder.matches(r.password(),u.getPasswordHash()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials");
        return new LoginResponse(jwt.generate(u.getUsername(),u.getRole().name()),u.getRole().name());
    }
}

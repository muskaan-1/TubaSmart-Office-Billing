package com.tubasmart.backend.controller;

import com.tubasmart.backend.model.*;
import com.tubasmart.backend.repository.UserAccountRepository;
import jakarta.validation.Valid; import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.tubasmart.backend.api.BadRequestException; import com.tubasmart.backend.api.ConflictException;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.*;

@RestController @RequestMapping("/api/users") @PreAuthorize("hasRole('ADMIN')")
public class UserController {
 private final UserAccountRepository users; private final PasswordEncoder encoder;
 public UserController(UserAccountRepository users,PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
 public record CreateUser(@NotBlank String username,@NotBlank @jakarta.validation.constraints.Size(min=8,message="Password must be at least 8 characters") String password,@NotBlank String role){}
 @GetMapping public List<Map<String,Object>> all(){
  return users.findAll().stream().map(this::toResponse).toList();
 }
 private Map<String,Object> toResponse(UserAccount u){
  Map<String,Object> m=new LinkedHashMap<>();
  m.put("id",u.getId()); m.put("username",u.getUsername()); m.put("role",u.getRole()); m.put("active",u.isActive());
  return m;
 }
 @PostMapping public Map<String,Object> create(@Valid @RequestBody CreateUser r){if(users.findByUsernameIgnoreCase(r.username()).isPresent())throw new ConflictException("Username already exists");UserAccount u=new UserAccount();u.setUsername(r.username().trim());u.setPasswordHash(encoder.encode(r.password()));try{u.setRole(Role.valueOf(r.role().trim().toUpperCase(Locale.ROOT)));}catch(IllegalArgumentException e){throw new BadRequestException("INVALID_ROLE","Role must be one of "+Arrays.toString(Role.values()));}u.setActive(true);u=users.save(u);return toResponse(u);}
 @PatchMapping("/{id}/active") public Map<String,Object> active(@PathVariable Long id,@RequestParam boolean value){
  UserAccount u=users.findById(id).orElseThrow();
  var me=SecurityContextHolder.getContext().getAuthentication();
  if(!value && me!=null && u.getUsername().equalsIgnoreCase(me.getName())) throw new ConflictException("You cannot deactivate your own account");
  u.setActive(value); users.save(u);
  Map<String,Object> m=new LinkedHashMap<>(); m.put("id",u.getId()); m.put("active",u.isActive());
  return m;
 }
}

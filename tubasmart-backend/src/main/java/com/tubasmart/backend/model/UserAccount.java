package com.tubasmart.backend.model;
import jakarta.persistence.*;
@Entity @Table(name="app_users")
public class UserAccount extends BaseEntity {
 @Column(nullable=false, unique=true) private String username; @Column(nullable=false) private String passwordHash;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role = Role.BILLING_FINANCE; private boolean active=true;
 public String getUsername(){return username;} public void setUsername(String v){username=v;} public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;} public Role getRole(){return role;} public void setRole(Role v){role=v;} public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}

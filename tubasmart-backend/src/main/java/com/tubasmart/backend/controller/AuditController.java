package com.tubasmart.backend.controller;
import com.tubasmart.backend.repository.AuditLogRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/audit") @PreAuthorize("hasRole('ADMIN')")
public class AuditController { private final AuditLogRepository repo; public AuditController(AuditLogRepository repo){this.repo=repo;} @GetMapping public Object all(){return repo.findTop100ByOrderByOccurredAtDesc();} }

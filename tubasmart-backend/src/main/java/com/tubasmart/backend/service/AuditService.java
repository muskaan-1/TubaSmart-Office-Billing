package com.tubasmart.backend.service;
import com.tubasmart.backend.model.AuditLog; import com.tubasmart.backend.repository.AuditLogRepository;
import org.springframework.stereotype.Service; import org.springframework.security.core.Authentication; import org.springframework.security.core.context.SecurityContextHolder;
@Service public class AuditService { private final AuditLogRepository repo; public AuditService(AuditLogRepository repo){this.repo=repo;} public void log(String action,String entityType,Long entityId,String details){Authentication a=SecurityContextHolder.getContext().getAuthentication();AuditLog l=new AuditLog();l.setUsername(a==null?"system":a.getName());l.setAction(action);l.setEntityType(entityType);l.setEntityId(entityId);l.setDetails(details);repo.save(l);} }

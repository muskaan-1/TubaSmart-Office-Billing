package com.tubasmart.backend.model;
import jakarta.persistence.*; import java.time.OffsetDateTime;
@Entity @Table(name="audit_logs")
public class AuditLog extends BaseEntity {
 @Column(nullable=false) private String username; @Column(nullable=false) private String action; @Column(nullable=false) private String entityType; private Long entityId; private String details; private OffsetDateTime occurredAt=OffsetDateTime.now();
 public String getUsername(){return username;} public void setUsername(String v){username=v;} public String getAction(){return action;} public void setAction(String v){action=v;} public String getEntityType(){return entityType;} public void setEntityType(String v){entityType=v;} public Long getEntityId(){return entityId;} public void setEntityId(Long v){entityId=v;} public String getDetails(){return details;} public void setDetails(String v){details=v;} public OffsetDateTime getOccurredAt(){return occurredAt;} public void setOccurredAt(OffsetDateTime v){occurredAt=v;}
}

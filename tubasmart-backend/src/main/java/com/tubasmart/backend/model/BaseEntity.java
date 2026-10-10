package com.tubasmart.backend.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@MappedSuperclass
public abstract class BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, updatable=false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    void touch() { updatedAt = OffsetDateTime.now(); }

    public Long getId(){ return id; }
    public OffsetDateTime getCreatedAt(){ return createdAt; }
    public OffsetDateTime getUpdatedAt(){ return updatedAt; }
}

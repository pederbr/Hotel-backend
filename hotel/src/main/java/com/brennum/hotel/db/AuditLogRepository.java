package com.brennum.hotel.db;

import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;

import com.brennum.hotel.api.model.AuditLog;

public interface AuditLogRepository extends CrudRepository<AuditLog, Integer> {
    Iterable<AuditLog> findByEntityTypeAndEntityId(AuditLog.EntityType entityType, Integer entityId);
    Iterable<AuditLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Iterable<AuditLog> findByUserId(Integer userId);
}
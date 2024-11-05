package com.brennum.hotel.db;

import com.brennum.hotel.api.model.AuditLog;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface AuditLogRepository extends CrudRepository<AuditLog, Integer> {
    Iterable<AuditLog> findByEntityTypeAndEntityId(AuditLog.EntityType entityType, Integer entityId);
    Iterable<AuditLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Iterable<AuditLog> findByUserId(Integer userId);
}
package com.brennum.hotel.api.service;

import com.brennum.hotel.api.model.AuditLog;
import com.brennum.hotel.api.model.Customer;
import com.brennum.hotel.db.AuditLogRepository;

import jakarta.transaction.Transactional;

public class AuditLogService {
    
    private final AuditLogRepository auditLogRepository;


    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void createAuditLog(AuditLog.ActionType action, AuditLog.EntityType entityType, Integer entityId, Customer user) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUser(user);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLogRepository.save(auditLog);
    }

}

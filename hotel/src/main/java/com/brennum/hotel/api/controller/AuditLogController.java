
package com.brennum.hotel.api.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brennum.hotel.api.model.AuditLog;
import com.brennum.hotel.db.AuditLogRepository;

@RestController
@RequestMapping("/api/auditlog")
public class AuditLogController {
    
    @Autowired private AuditLogRepository auditLogRepository;


    @GetMapping("/entity/{entityType}/{entityId}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Iterable<AuditLog>> getAuditByEntityAndID(@PathVariable String entityType, @PathVariable Integer entityId) {
        return ResponseEntity.ok(auditLogRepository.findByEntityTypeAndEntityId(AuditLog.EntityType.valueOf(entityType), entityId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Iterable<AuditLog>> getAuditByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(auditLogRepository.findByUserId(userId));
    }

    @GetMapping("/date/{start}/{end}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Iterable<AuditLog>> getAuditByDate(@PathVariable String startStr, @PathVariable String endStr) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);
        
        return ResponseEntity.ok(auditLogRepository.findByCreatedAtBetween(start, end));
    }
    
}

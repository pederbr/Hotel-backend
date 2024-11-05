package com.brennum.hotel.api.service;

import org.springframework.stereotype.Service;

import com.brennum.hotel.api.model.AuditLog;
import com.brennum.hotel.api.model.Customer;
import com.brennum.hotel.db.CustomerRepository;

import jakarta.transaction.Transactional;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AuditLogService auditLogService;

    public CustomerService(CustomerRepository customerRepository, AuditLogService auditLogService) {
        this.customerRepository = customerRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public Customer createCustomer(String name, String email, String phone) {
        if (customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Validate phone number
        if("^(?:\\+47|0047)?\\s?(\\d{2}\\s?\\d{2}\\s?\\d{2}\\s?\\d{2}|\\d{8})$".matches(phone)){
            throw new IllegalArgumentException("Invalid phone number");
        }

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        
        customerRepository.save(customer);
        auditLogService.createAuditLog(AuditLog.ActionType.CREATE, AuditLog.EntityType.CUSTOMER, customer.getId(), customer);
        return customer;
    }

    @Transactional
    public void updateLoyaltyPoints(Integer customerId, int points) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        customerRepository.save(customer);
        auditLogService.createAuditLog(AuditLog.ActionType.UPDATE, AuditLog.EntityType.CUSTOMER, customer.getId(), customer);

    }

    @Transactional
    public void makeAdmin(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        customer.setAdmin(true);
        customerRepository.save(customer);
        auditLogService.createAuditLog(AuditLog.ActionType.UPDATE, AuditLog.EntityType.CUSTOMER, customer.getId(), customer);
    }

    @Transactional
    public Customer updateCustomer(Integer costumerID, String name, String email, String phone) {
        Customer customer = customerRepository.findById(costumerID)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customerRepository.save(customer);
        auditLogService.createAuditLog(AuditLog.ActionType.UPDATE, AuditLog.EntityType.CUSTOMER, customer.getId(), customer);
        return customer;
    }


}
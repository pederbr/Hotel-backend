package com.brennum.hotel.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brennum.hotel.api.model.Customer;
import com.brennum.hotel.api.service.CustomerService;
import com.brennum.hotel.db.CustomerRepository;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;
    

    @PostMapping
    public ResponseEntity<Customer> createCustomer(
        @RequestBody String name, 
        @RequestBody String email, 
        @RequestBody String phone) {
            Customer c = customerService.createCustomer(name, email, phone);
            return ResponseEntity.ok(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
        @PathVariable Integer id, 
        @RequestBody String name, 
        @RequestBody String email, 
        @RequestBody String phone) {
            Customer c = customerService.updateCustomer(id, name, email, phone);
            return ResponseEntity.ok(c);
    }

    @PutMapping("{id}/setAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> setAdmin(@PathVariable Integer id) {
        Customer c = customerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            c.setAdmin(true);
        return ResponseEntity.ok(c);      
    }

    @GetMapping
    public ResponseEntity<Iterable<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerRepository.findAll());
       
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


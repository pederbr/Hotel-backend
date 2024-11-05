package com.brennum.hotel.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.brennum.hotel.api.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);

}


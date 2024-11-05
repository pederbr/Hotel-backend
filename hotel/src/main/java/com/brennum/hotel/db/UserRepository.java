package com.brennum.hotel.db;

import org.springframework.data.repository.CrudRepository;

import com.brennum.hotel.api.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}

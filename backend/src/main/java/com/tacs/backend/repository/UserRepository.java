package com.tacs.backend.repository;

import com.tacs.backend.model.User;

import java.util.Optional;

public interface UserRepository {

    boolean exists(String username);
    Optional<User> findByUsername(String username);
    User save(User user);
}

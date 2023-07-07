package com.tacs.backend.repository.impl;

import com.tacs.backend.repository.UserRepository;
import com.tacs.backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public boolean exists(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return mongoTemplate.exists(query, User.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return Optional.ofNullable(mongoTemplate.findOne(query, User.class));
    }

    @Override
    public User save(User user) {
        return mongoTemplate.save(user);
    }
}

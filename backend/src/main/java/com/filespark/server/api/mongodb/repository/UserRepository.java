package com.filespark.server.api.mongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.filespark.server.api.mongodb.models.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByGoogleId (String googleId);

    Optional<User> findByEmail (String email);
    
}

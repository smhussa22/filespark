package com.filespark.server.services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.filespark.server.api.mongodb.models.User;
import com.filespark.server.api.mongodb.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StatsService statsService;

    @Autowired
    public AuthService(UserRepository userRepository, StatsService statsService){

        this.userRepository = userRepository;
        this.statsService = statsService;

    }

    public User getOrCreateUser(String email, String name, String googleId, String picture){

        Optional<User> byGoogleId = userRepository.findByGoogleId(googleId);
        if (byGoogleId.isPresent()) {

            return byGoogleId.get();

        }

        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {

            User user = byEmail.get();
            user.setGoogleId(googleId);
            user.setName(name);
            user.setPicture(picture);
            user.setUpdatedAt(Instant.now());
            return userRepository.save(user);

        }

        try {

            User newUser = new User(email, name, googleId, picture);
            User saved = userRepository.save(newUser);
            statsService.incrementUsers();
            return saved;

        }
        catch(DuplicateKeyException exception) {

            return userRepository.findByGoogleId(googleId)
                .or(() -> userRepository.findByEmail(email))
                .orElseThrow(() -> new IllegalStateException("User created but not found."));

        }

    }

    /* 
    public verifyGoogleIdToken(String idToken, String client_id){



    }

    public issue_session_token(String user_id, String secret) {



    }

    public getCurrentUser(){


        
    }
    */

}

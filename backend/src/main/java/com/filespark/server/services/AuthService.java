package com.filespark.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filespark.server.api.mongodb.models.User;
import com.filespark.server.api.mongodb.repository.UserRepository;
import com.mongodb.DuplicateKeyException;

@Service
public class AuthService {
    
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository){

        this.userRepository = userRepository;

    }

    public User getOrCreateUser(String email, String name, String googleId, String picture){

        Optional<User> existingUser = userRepository.findByGoogleId(googleId);
        if (existingUser.isPresent()) {

            return existingUser.get();

        }

        try {

            User newUser = new User(email, name, googleId, picture);
            return userRepository.save(newUser);

        }
        catch(DuplicateKeyException exception) {

            return userRepository.findByGoogleId(googleId).orElseThrow(() -> new IllegalStateException("User created but not found."));

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

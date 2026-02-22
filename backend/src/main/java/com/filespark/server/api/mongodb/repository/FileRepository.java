package com.filespark.server.api.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.filespark.server.api.mongodb.models.File;

public interface FileRepository extends MongoRepository<File, String> {
    
}
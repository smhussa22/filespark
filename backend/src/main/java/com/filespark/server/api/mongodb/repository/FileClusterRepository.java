package com.filespark.server.api.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.filespark.server.api.mongodb.models.FileCluster;

public interface FileClusterRepository extends MongoRepository<FileCluster, String>{
    
}

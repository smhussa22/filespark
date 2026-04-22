package com.filespark.server.api.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.filespark.server.api.mongodb.models.File;

public interface FileRepository extends MongoRepository<File, String> {

    List<File> findByOwnerIdOrderByCreatedAtDesc(String ownerId);

}

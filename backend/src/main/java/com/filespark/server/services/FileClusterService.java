package com.filespark.server.services;

import com.filespark.server.api.mongodb.models.FileCluster;
import com.filespark.server.api.mongodb.repository.FileClusterRepository;

public class FileClusterService {

    private final FileClusterRepository fileClusterRepository;

    public FileClusterService(FileClusterRepository repository) {

        this.fileClusterRepository = repository;

    }

    public FileCluster createCluster(String ownerId, String name) {

        FileCluster cluster = new FileCluster(ownerId, name);
        return fileClusterRepository.save(cluster);

    }
    
}

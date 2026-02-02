package com.filespark.server.services.mongodb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@Configuration
public class Mongo {

    @Bean
    public MongoClient client (@Value("${spring.data.mongodb.uri}") String mongoUri) {

        return MongoClients.create(mongoUri);

    }


    @Bean
    public MongoDatabase database (MongoClient client, String dbName) {

        return client.getDatabase(dbName);

    }
    
}

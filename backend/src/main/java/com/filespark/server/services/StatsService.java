package com.filespark.server.services;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.filespark.server.api.mongodb.models.Stats;

@Service
public class StatsService {

    private final MongoTemplate mongoTemplate;

    public StatsService(MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;

    }

    public Stats getCounts() {

        Stats stats = mongoTemplate.findById(Stats.SINGLETON_ID, Stats.class);
        if (stats == null) {

            stats = new Stats();
            try { mongoTemplate.save(stats); }
            catch (Exception ignored) {}

        }
        return stats;

    }

    public void incrementUsers() {

        increment("totalUsers");

    }

    public void incrementUploads() {

        increment("totalUploads");

    }

    private void increment(String field) {

        try {

            Query query = new Query(Criteria.where("_id").is(Stats.SINGLETON_ID));
            Update update = new Update().inc(field, 1L);
            mongoTemplate.upsert(query, update, Stats.class);

        }
        catch (Exception ignored) {

            // never let stats bookkeeping break the upload/signup path

        }

    }

}

package com.filespark.server.routes;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filespark.server.api.mongodb.models.Stats;
import com.filespark.server.services.StatsService;

@RestController
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {

        this.statsService = statsService;

    }

    @GetMapping("/stats")
    public Map<String, Long> stats() {

        Stats s = statsService.getCounts();
        return Map.of(
            "totalUsers", s.getTotalUsers(),
            "totalUploads", s.getTotalUploads()
        );

    }

}
